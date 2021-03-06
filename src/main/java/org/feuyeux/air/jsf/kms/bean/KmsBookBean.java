package org.feuyeux.air.jsf.kms.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.feuyeux.air.jsf.kms.dao.exceptions.PreexistingEntityException;
import org.feuyeux.air.jsf.kms.dao.jpa2.KmsBookDao;
import org.feuyeux.air.jsf.kms.dao.jpa2.KmsKnowledgeDao;
import org.feuyeux.air.jsf.kms.dao.util.KmsPrimaryKey;
import org.feuyeux.air.jsf.kms.entity.KmsBook;
import org.feuyeux.air.jsf.kms.entity.KmsKnowledge;
import org.feuyeux.air.jsf.kms.entity.KmsUser;

/**
 * @author feuyeux@gmail.com
 * @version 2.0
 */
@ManagedBean
@ViewScoped
public class KmsBookBean implements Serializable{
	private static final long serialVersionUID = -6048033994535490103L;
	private KmsBook kmsBook = null;
	private List<KmsBook> kmsBookItems = null;
	private KmsBookDao dao = null;
	private KmsKnowledgeDao kdao;
	private String kmsKnowledgeId;

	public KmsBookBean() {
		dao = new KmsBookDao();
		kdao = new KmsKnowledgeDao();
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String bId = params.get("bId");

		if (bId != null) {
			kmsBook = dao.findKmsBook(bId);
		} else {
			kmsBook = new KmsBook();
		}
	}

	public KmsBook getKmsBook() {
		return kmsBook;
	}

	public String create() throws PreexistingEntityException, Exception {

		KmsKnowledge kmsKnowledge = kdao.findKmsKnowledge(kmsKnowledgeId);
		kmsBook.setKmsKnowledge(kmsKnowledge);

		String bookId = KmsPrimaryKey.BOOK + System.nanoTime();
		kmsBook.setBookId(bookId);

		Map<String, Object> sesions = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		KmsUser kmsUser = (KmsUser) sesions.get("kmsUser");
		kmsBook.setKmsUser(kmsUser);

		dao.create(kmsBook);
		return "kmsBook_list?faces-redirect=true";
	}

	public String update() {
		return "kmsBook_list?faces-redirect=true";
	}

	public List<KmsBook> getKmsBookItems() {
		if (kmsBookItems == null) {
			kmsBookItems = dao.findKmsBookEntities();
		}
		return kmsBookItems;
	}

	public String getKmsKnowledgeId() {
		return kmsKnowledgeId;
	}

	public void setKmsKnowledgeId(String kmsKnowledgeId) {
		this.kmsKnowledgeId = kmsKnowledgeId;
	}
}

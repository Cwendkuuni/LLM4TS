package fr.unice.gfarce.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {
	
	private OracleIdentiteDao dao;
	private EntityManagerFactory emf;
	private EntityManager em;
	
	@Before
	public void setUp() {
		this.dao = OracleIdentiteDao.getDAO();
		this.emf = Persistence.createEntityManagerFactory("Test");
		this.em = emf.createEntityManager();
	}
	
	@After
	public void tearDown() {
		this.em.close();
		this.emf.close();
	}
	
	@Test
	public void testInsert() {
		Identite identite = new Identite();
		identite.setId(1);
		identite.setEmail("test@test.com");
		identite.setNom("Test");
		identite.setPrenom("User");
		identite.setSex("M");
		identite.setType(Identite.TypeIdentite.CANDIDAT);
		Calendar dateLimite = Calendar.getInstance();
		
		dao.insert(identite, "Form", dateLimite);
		
		Identite result = em.find(Identite.class, 1);
		
		assertNotNull(result);
		assertEquals(identite.getEmail(), result.getEmail());
		assertEquals(identite.getNom(), result.getNom());
		assertEquals(identite.getPrenom(), result.getPrenom());
		assertEquals(identite.getSex(), result.getSex());
		assertEquals(identite.getType(), result.getType());
	}
	
	@Test
	public void testUpdate() {
		Identite identite = new Identite();
		identite.setId(1);
		identite.setEmail("test@test.com");
		identite.setNom("Test");
		identite.setPrenom("User");
		identite.setSex("M");
		identite.setType(Identite.TypeIdentite.CANDIDAT);
		
		dao.update(identite);
		
		Identite result = em.find(Identite.class, 1);
		
		assertNotNull(result);
		assertEquals(identite.getEmail(), result.getEmail());
		assertEquals(identite.getNom(), result.getNom());
		assertEquals(identite.getPrenom(), result.getPrenom());
		assertEquals(identite.getSex(), result.getSex());
		assertEquals(identite.getType(), result.getType());
	}
	
	@Test
	public void testDelete() {
		Identite identite = new Identite();
		identite.setId(1);
		identite.setEmail("test@test.com");
		identite.setNom("Test");
		identite.setPrenom("User");
		identite.setSex("M");
		identite.setType(Identite.TypeIdentite.CANDIDAT);
		
		dao.delete(identite);
		
		Identite result = em.find(Identite.class, 1);
		
		assertNull(result);
	}
	
	@Test
	public void testFind() {
		List<Identite> resultList = dao.find("Test", "User", "M", "test@test.com", Identite.TypeIdentite.CANDIDAT);
		
		assertNotNull(resultList);
		
		Candidat[] result = dao.findCandidat("Test", "User", "M", "test@test.com");
		
		assertNotNull(result);
	}
	
	@Test
	public void testFindCandidat() {
		Candidat[] result = dao.findCandidat("Test", "User", "M", "test@test.com");
		
		assertNotNull(result);
	}
	
	@Test
	public void testFindRespForm() {
		RespFormation[] result = dao.findRespForm("Test", "User", "M", "test@test.com");
		
		assertNotNull(result);
	}
	
}
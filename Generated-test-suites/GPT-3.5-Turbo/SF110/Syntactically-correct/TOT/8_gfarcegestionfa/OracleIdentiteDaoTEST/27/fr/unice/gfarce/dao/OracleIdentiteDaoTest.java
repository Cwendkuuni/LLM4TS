import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {
  
  private OracleIdentiteDao oracleIdentiteDao;
  private EntityManagerFactory emf;
  private EntityManager em;
  private EntityTransaction tx;
  
  @Before
  public void setUp() {
    emf = Persistence.createEntityManagerFactory("Identite");
    em = emf.createEntityManager();
    tx = em.getTransaction();
    oracleIdentiteDao = new OracleIdentiteDao();
    oracleIdentiteDao.em = em;
  }
  
  @Test
  public void testInsert() {
    // Typical Use Case
    Identite identite = new Candidat("John", "Doe", "M", "john.doe@test.com");
    oracleIdentiteDao.insert(identite, "test", Calendar.getInstance());
    Identite result = (Identite) em.find(identite.getClass(), identite.getId());
    Assert.assertNotNull(result);
    Assert.assertEquals(identite, result);
    
    // Edge Case
    identite = new Candidat("", "", "", "");
    oracleIdentiteDao.insert(identite, "test", null);
    result = (Identite) em.find(identite.getClass(), identite.getId());
    Assert.assertNotNull(result);
    Assert.assertEquals(identite, result);
    
    // Error Scenario
    identite = null;
    oracleIdentiteDao.insert(null, "test", Calendar.getInstance());
    result = (Identite) em.find(identite.getClass(), identite.getId());
    Assert.assertNull(result);
  }
  
  @Test
  public void testUpdate() {
    // Typical Use Case
    Identite identite = new Candidat("John", "Doe", "M", "john.doe@test.com");
    em.persist(identite);
    oracleIdentiteDao.update(identite);
    Identite result = (Identite) em.find(identite.getClass(), identite.getId());
    Assert.assertEquals(identite, result);
    
    // Edge Case
    identite = new Candidat("", "", "", "");
    em.persist(identite);
    oracleIdentiteDao.update(identite);
    result = (Identite) em.find(identite.getClass(), identite.getId());
    Assert.assertEquals(identite, result);
    
    // Error Scenario
    identite = null;
    oracleIdentiteDao.update(null);
    result = (Identite) em.find(identite.getClass(), identite.getId());
    Assert.assertNull(result);
  }
  
  @Test
  public void testDelete() {
    // Typical Use Case
    Identite identite = new Candidat("John", "Doe", "M", "john.doe@test.com");
    em.persist(identite);
    oracleIdentiteDao.delete(identite);
    Identite result = (Identite) em.find(identite.getClass(), identite.getId());
    Assert.assertNull(result);
    
    // Edge Case
    identite = new Candidat("", "", "", "");
    em.persist(identite);
    oracleIdentiteDao.delete(identite);
    result = (Identite) em.find(identite.getClass(), identite.getId());
    Assert.assertNull(result);
    
    // Error Scenario
    identite = null;
    oracleIdentiteDao.delete(null);
    result = (Identite) em.find(identite.getClass(), identite.getId());
    Assert.assertNull(result);
  }
  
  @Test
  public void testFind() {
    // Typical Use Case
    String nom = "John";
    String prenom = "Doe";
    String sex = "M";
    String email = "john.doe@test.com";
    Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
    List<Identite> expectedResult = new ArrayList<>();
    Identite candidate = new Candidat(nom, prenom, sex, email);
    expectedResult.add(candidate);
    em.persist(candidate);
    
    List<Identite> result = oracleIdentiteDao.find(nom, prenom, sex, email, type);
    Assert.assertEquals(expectedResult, result);
    
    // Edge Case
    nom = null;
    prenom = null;
    sex = null;
    email = null;
    type = Identite.TypeIdentite.FORMATEUR;
    expectedResult = new ArrayList<>();
    result = oracleIdentiteDao.find(nom, prenom, sex, email, type);
    Assert.assertEquals(expectedResult, result);
    
    // Error Scenario
    nom = "Invalid Name";
    prenom = "Invalid First Name";
    sex = "Invalid Gender";
    email = "invalidEmail";
    type = null;
    expectedResult = new ArrayList<>();
    result = oracleIdentiteDao.find(nom, prenom, sex, email, type);
    Assert.assertEquals(expectedResult, result);
  }
  
  @Test
  public void testFindCandidat() {
    // Typical Use Case
    String nom = "John";
    String prenom = "Doe";
    String sex = "M";
    String email = "john.doe@test.com";
    List<Identite> expectedResult = new ArrayList<>();
    Identite candidate = new Candidat(nom, prenom, sex, email);
    expectedResult.add(candidate);
    em.persist(candidate);
    
    Canditat[] result = oracleIdentiteDao.findCandidat(nom, prenom, sex, email);
    Assert.assertArrayEquals(expectedResult.toArray(), result);
    
    // Edge Case
    nom = null;
    prenom = null;
    sex = null;
    email = null;
    expectedResult = new ArrayList<>();
    result = oracleIdentiteDao.findCandidat(nom, prenom, sex, email);
    Assert.assertEquals(expectedResult, result);
    
    // Error Scenario
    nom = "Invalid Name";
    prenom = "Invalid First Name";
    sex = "Invalid Gender";
    email = "invalidEmail";
    expectedResult = null;
    result = oracleIdentiteDao.findCandidat(nom, prenom, sex, email);
    Assert.assertEquals(expectedResult, result);
  }
  
  @Test
  public void testFindRespForm() {
    // Typical Use Case
    String nom = "John";
    String prenom = "Doe";
    String sex = "M";
    String email = "john.doe@test.com";
    List<Identite> expectedResult = new ArrayList<>();
    Identite respForm = new RespFormation(nom, prenom, sex, email);
    expectedResult.add(respForm);
    em.persist(respForm);
    
    RespFormation[] result = oracleIdentiteDao.findRespForm(nom, prenom, sex, email);
    Assert.assertArrayEquals(expectedResult.toArray(), result);
    
    // Edge Case
    nom = null;
    prenom = null;
    sex = null;
    email = null;
    expectedResult = new ArrayList<>();
    result = oracleIdentiteDao.findRespForm(nom, prenom, sex, email);
    Assert.assertEquals(expectedResult, result);
    
    // Error Scenario
    nom = "Invalid Name";
    prenom = "Invalid First Name";
    sex = "Invalid Gender";
    email = "invalidEmail";
    expectedResult = null;
    result = oracleIdentiteDao.findRespForm(nom, prenom, sex, email);
    Assert.assertEquals(expectedResult, result);
  }
  
  @After
  public void tearDown() {
    if (em != null) {
      em.close();
    }
    if (emf != null) {
      emf.close();
    }
  }
}
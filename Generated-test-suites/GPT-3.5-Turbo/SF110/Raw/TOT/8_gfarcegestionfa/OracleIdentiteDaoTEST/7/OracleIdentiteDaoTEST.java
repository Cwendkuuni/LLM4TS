import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import fr.unice.gfarce.dao.*;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {
  
  private OracleIdentiteDao dao;
  
  @Before
  public void setup() {
    dao = OracleIdentiteDao.getDAO();
  }
  
  @After
  public void tearDown() {
    dao = null;
  }
  
  @Test
  public void testInsert() {
    // Typical use case
    Identite identite = new Identite();
    identite.setId(1L);
    identite.setNom("John");
    identite.setPrenom("Doe");
    identite.setSex("M");
    identite.setEmail("john.doe@example.com");
    Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
    Calendar dateLimite = Calendar.getInstance();
    dao.insert(identite, "", dateLimite);
    
    // Verify insertion
    Identite insertedIdentite = dao.findCandidat(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail())[0];
    assertEquals(identite, insertedIdentite);
    
    // Edge case - insert null values
    identite.setId(null);
    identite.setNom(null);
    identite.setPrenom(null);
    identite.setSex(null);
    identite.setEmail(null);
    dao.insert(identite, null, null);
    
    // Verify insertion
    insertedIdentite = dao.findCandidat(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail())[0];
    assertEquals(identite, insertedIdentite);
    
    // Error scenario - throw exception
    identite.setId(1L);
    assertThrows(Exception.class, () -> dao.insert(identite, null, dateLimite));
  }
  
  @Test
  public void testUpdate() {
    // Typical use case
    Identite identite = new Identite();
    identite.setId(1L);
    identite.setNom("John");
    identite.setPrenom("Doe");
    identite.setSex("M");
    identite.setEmail("john.doe@example.com");
    Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
    Calendar dateLimite = Calendar.getInstance();
    dao.insert(identite, "", dateLimite);
    
    // Update values
    identite.setNom("Jane");
    identite.setPrenom("Smith");
    identite.setSex("F");
    identite.setEmail("jane.smith@example.com");
    dao.update(identite);
    
    // Verify update
    Identite updatedIdentite = dao.findCandidat(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail())[0];
    assertEquals(identite, updatedIdentite);
    
    // Edge case - update non-existent identite
    identite.setId(2L);
    assertThrows(Exception.class, () -> dao.update(identite));
    
    // Error scenario - throw exception
    identite.setId(1L);
    identite.setNom(null);
    assertThrows(Exception.class, () -> dao.update(identite));
  }
  
  @Test
  public void testDelete() {
    // Typical use case
    Identite identite = new Identite();
    identite.setId(1L);
    identite.setNom("John");
    identite.setPrenom("Doe");
    identite.setSex("M");
    identite.setEmail("john.doe@example.com");
    Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
    Calendar dateLimite = Calendar.getInstance();
    dao.insert(identite, "", dateLimite);
    
    // Delete identite
    dao.delete(identite);
    
    // Verify deletion
    Identite[] resultList = dao.findCandidat(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail());
    assertEquals(0, resultList.length);
    
    // Edge case - delete non-existent identite
    identite.setId(2L);
    assertThrows(Exception.class, () -> dao.delete(identite));
  }
  
  @Test
  public void testFind() {
    // Typical use case
    Identite identite1 = new Identite();
    identite1.setId(1L);
    identite1.setNom("John");
    identite1.setPrenom("Doe");
    identite1.setSex("M");
    identite1.setEmail("john.doe@example.com");
    Identite.TypeIdentite type1 = Identite.TypeIdentite.CANDIDAT;
    Calendar dateLimite1 = Calendar.getInstance();
    dao.insert(identite1, "", dateLimite1);
    
    Identite identite2 = new Identite();
    identite2.setId(2L);
    identite2.setNom("Jane");
    identite2.setPrenom("Smith");
    identite2.setSex("F");
    identite2.setEmail("jane.smith@example.com");
    Identite.TypeIdentite type2 = Identite.TypeIdentite.FORMATEUR;
    Calendar dateLimite2 = Calendar.getInstance();
    dao.insert(identite2, "", dateLimite2);
    
    // Find candidat
    String nom = identite1.getNom();
    String prenom = identite1.getPrenom();
    String sex = identite1.getSex();
    String email = identite1.getEmail();
    Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
    List<Identite> resultList = dao.find(nom, prenom, sex, email, type);
    assertEquals(1, resultList.size());
    assertEquals(identite1, resultList.get(0));
    
    // Find formateur
    nom = identite2.getNom();
    prenom = identite2.getPrenom();
    sex = identite2.getSex();
    email = identite2.getEmail();
    type = Identite.TypeIdentite.FORMATEUR;
    resultList = dao.find(nom, prenom, sex, email, type);
    assertEquals(1, resultList.size());
    assertEquals(identite2, resultList.get(0));
    
    // Edge case - find non-existent identite
    nom = "Nonexistent";
    prenom = "Identite";
    sex = "F";
    email = "nonexistent.identite@example.com";
    type = Identite.TypeIdentite.CANDIDAT;
    resultList = dao.find(nom, prenom, sex, email, type);
    assertEquals(0, resultList.size());
    
    // Error scenario - throw exception
    nom = null;
    prenom = null;
    sex = null;
    email = null;
    type = null;
    assertThrows(Exception.class, () -> dao.find(nom, prenom, sex, email, type));
  }
  
  @Test
  public void testFindCandidat() {
    // Typical use case
    Identite identite1 = new Identite();
    identite1.setId(1L);
    identite1.setNom("John");
    identite1.setPrenom("Doe");
    identite1.setSex("M");
    identite1.setEmail("john.doe@example.com");
    Identite.TypeIdentite type1 = Identite.TypeIdentite.CANDIDAT;
    Calendar dateLimite1 = Calendar.getInstance();
    dao.insert(identite1, "", dateLimite1);
    
    Identite identite2 = new Identite();
    identite2.setId(2L);
    identite2.setNom("Jane");
    identite2.setPrenom("Smith");
    identite2.setSex("F");
    identite2.setEmail("jane.smith@example.com");
    Identite.TypeIdentite type2 = Identite.TypeIdentite.FORMATEUR;
    Calendar dateLimite2 = Calendar.getInstance();
    dao.insert(identite2, "", dateLimite2);
    
    // Find candidat
    String nom = identite1.getNom();
    String prenom = identite1.getPrenom();
    String sex = identite1.getSex();
    String email = identite1.getEmail();
    Candidat[] resultList = dao.findCandidat(nom, prenom, sex, email);
    assertEquals(1, resultList.length);
    assertEquals(identite1, resultList[0]);
    
    // Edge case - find non-existent candidat
    nom = "Nonexistent";
    prenom = "Candidat";
    sex = "F";
    email = "nonexistent.candidat@example.com";
    resultList = dao.findCandidat(nom, prenom, sex, email);
    assertNull(resultList);
    
    // Error scenario - throw exception
    nom = null;
    prenom = null;
    sex = null;
    email = null;
    assertThrows(Exception.class, () -> dao.findCandidat(nom, prenom, sex, email));
  }
  
  @Test
  public void testFindRespForm() {
    // Typical use case
    Identite identite1 = new Identite();
    identite1.setId(1L);
    identite1.setNom("John");
    identite1.setPrenom("Doe");
    identite1.setSex("M");
    identite1.setEmail("john.doe@example.com");
    Identite.TypeIdentite type1 = Identite.TypeIdentite.CANDIDAT;
    Calendar dateLimite1 = Calendar.getInstance();
    dao.insert(identite1, "", dateLimite1);
    
    Identite identite2 = new Identite();
    identite2.setId(2L);
    identite2.setNom("Jane");
    identite2.setPrenom("Smith");
    identite2.setSex("F");
    identite2.setEmail("jane.smith@example.com");
    Identite.TypeIdentite type2 = Identite.TypeIdentite.FORMATEUR;
    Calendar dateLimite2 = Calendar.getInstance();
    dao.insert(identite2, "", dateLimite2);
    
    // Find formateur
    String nom = identite2.getNom();
    String prenom = identite2.getPrenom();
    String sex = identite2.getSex();
    String email = identite2.getEmail();
    RespFormation[] resultList = dao.findRespForm(nom, prenom, sex, email);
    assertEquals(1, resultList.length);
    assertEquals(identite2, resultList[0]);
    
    // Edge case - find non-existent formateur
    nom = "Nonexistent";
    prenom = "Formateur";
    sex = "F";
    email = "nonexistent.formateur@example.com";
    resultList = dao.findRespForm(nom, prenom, sex, email);
    assertNull(resultList);
    
    // Error scenario - throw exception
    nom = null;
    prenom = null;
    sex = null;
    email = null;
    assertThrows(Exception.class, () -> dao.findRespForm(nom, prenom, sex, email));
  }
}
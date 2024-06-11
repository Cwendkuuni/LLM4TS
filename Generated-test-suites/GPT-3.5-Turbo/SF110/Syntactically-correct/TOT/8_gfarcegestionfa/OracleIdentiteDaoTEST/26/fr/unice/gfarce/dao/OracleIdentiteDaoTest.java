import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OracleIdentiteDaoTest {
  private OracleIdentiteDao dao;
  
  @Before
  public void setUp() {
    dao = OracleIdentiteDao.getDAO();
  }
  
  @After
  public void tearDown() {
    dao = null;
  }
  
  @Test
  public void testInsert_typicalCase() {
    // typical case where all parameters are valid
    Identite identite = new Identite();
    identite.setId(1);
    identite.setNom("John");
    identite.setPrenom("Doe");
    identite.setEmail("john.doe@example.com");
    identite.setSex("Male");
    identite.setType(Identite.TypeIdentite.CANDIDAT);
    Calendar dateLimite = Calendar.getInstance();
    dateLimite.set(2022, 0, 1);
    
    dao.insert(identite, "Form", dateLimite);
    
    // check if the identite was inserted correctly
    List<Identite> resultList = dao.findCandidat("John", "Doe", "Male", "john.doe@example.com");
    assertNotNull(resultList);
    assertEquals(1, resultList.size());
    Identite insertedIdentite = resultList.get(0);
    assertEquals(1, insertedIdentite.getId());
    assertEquals("John", insertedIdentite.getNom());
    assertEquals("Doe", insertedIdentite.getPrenom());
    assertEquals("john.doe@example.com", insertedIdentite.getEmail());
    assertEquals("Male", insertedIdentite.getSex());
    assertEquals(Identite.TypeIdentite.CANDIDAT, insertedIdentite.getType());
  }
  
  @Test
  public void testInsert_nullIdentite() {
    // test case where identite parameter is null
    Identite identite = null;
    Calendar dateLimite = Calendar.getInstance();
    dateLimite.set(2022, 0, 1);
    
    // expect an exception to be thrown
    assertThrows(Exception.class, () -> {
      dao.insert(identite, "Form", dateLimite);
    });
  }
  
  @Test
  public void testUpdate_typicalCase() {
    // typical case where a existing identite is updated
    Identite identite = new Identite();
    identite.setId(1);
    identite.setNom("John");
    identite.setPrenom("Doe");
    identite.setEmail("john.doe@example.com");
    identite.setSex("Male");
    identite.setType(Identite.TypeIdentite.CANDIDAT);
    
    dao.update(identite);
    
    // check if the identite was updated correctly
    Identite updatedIdentite = dao.findCandidat("John", "Doe", "Male", "john.doe@example.com");
    assertNotNull(updatedIdentite);
    assertEquals(1, updatedIdentite.getId());
    assertEquals("John", updatedIdentite.getNom());
    assertEquals("Doe", updatedIdentite.getPrenom());
    assertEquals("john.doe@example.com", updatedIdentite.getEmail());
    assertEquals("Male", updatedIdentite.getSex());
    assertEquals(Identite.TypeIdentite.CANDIDAT, updatedIdentite.getType());
  }
  
  @Test
  public void testUpdate_identiteNotFound() {
    // test case where the identite to be updated is not found
    Identite identite = new Identite();
    identite.setId(999);
    identite.setNom("John");
    identite.setPrenom("Doe");
    identite.setEmail("john.doe@example.com");
    identite.setSex("Male");
    identite.setType(Identite.TypeIdentite.CANDIDAT);
    
    // expect an exception to be thrown
    assertThrows(Exception.class, () -> {
      dao.update(identite);
    });
  }
  
  @Test
  public void testDelete_typicalCase() {
    // typical case where a existing identite is deleted
    Identite identite = new Identite();
    identite.setId(1);
    identite.setNom("John");
    identite.setPrenom("Doe");
    identite.setEmail("john.doe@example.com");
    identite.setSex("Male");
    identite.setType(Identite.TypeIdentite.CANDIDAT);
    
    dao.delete(identite);
    
    // check if the identite was deleted correctly
    List<Identite> resultList = dao.findCandidat("John", "Doe", "Male", "john.doe@example.com");
    assertNull(resultList);
  }
  
  @Test
  public void testDelete_identiteNotFound() {
    // test case where the identite to be deleted is not found
    Identite identite = new Identite();
    identite.setId(999);
    identite.setNom("John");
    identite.setPrenom("Doe");
    identite.setEmail("john.doe@example.com");
    identite.setSex("Male");
    identite.setType(Identite.TypeIdentite.CANDIDAT);
    
    // expect an exception to be thrown
    assertThrows(Exception.class, () -> {
      dao.delete(identite);
    });
  }
  
  @Test
  public void testFind_typicalCase() {
    // test case where all search parameters are specified with typical values
    String nom = "John";
    String prenom = "Doe";
    String sex = "Male";
    String email = "john.doe@example.com";
    Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
    
    List<Identite> resultList = dao.find(nom, prenom, sex, email, type);
    
    // verify if the correct identites are returned
    assertNotNull(resultList);
    assertEquals(1, resultList.size());
    Identite resultIdentite = resultList.get(0);
    assertEquals(1, resultIdentite.getId());
    assertEquals("John", resultIdentite.getNom());
    assertEquals("Doe", resultIdentite.getPrenom());
    assertEquals("john.doe@example.com", resultIdentite.getEmail());
    assertEquals("Male", resultIdentite.getSex());
    assertEquals(Identite.TypeIdentite.CANDIDAT, resultIdentite.getType());
  }
  
  @Test
  public void testFind_nullParameters() {
    // test case where all parameters are set to null
    String nom = null;
    String prenom = null;
    String sex = null;
    String email = null;
    Identite.TypeIdentite type = null;
    
    // expect all identites to be returned
    List<Identite> resultList = dao.find(nom, prenom, sex, email, type);
    
    // verify if all identites are returned
    assertNotNull(resultList);
    assertEquals(1, resultList.size());
  }
  
  @Test
  public void testFind_identiteNotFound() {
    // test case where the identite is not found
    String nom = "Nonexistent";
    String prenom = "Person";
    String sex = "Unknown";
    String email = "nonexistent.person@example.com";
    Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
    
    // expect an empty list to be returned
    List<Identite> resultList = dao.find(nom, prenom, sex, email, type);
    
    // verify if no identites are returned
    assertNotNull(resultList);
    assertTrue(resultList.isEmpty());
  }
  
  @Test
  public void testFindCandidat_typicalCase() {
    // typical case where all parameters are specified for finding candidates
    String nom = "John";
    String prenom = "Doe";
    String sex = "Male";
    String email = "john.doe@example.com";
    
    Candidat[] resultArray = dao.findCandidat(nom, prenom, sex, email);
    
    // verify if the correct candidates are returned
    assertNotNull(resultArray);
    assertEquals(1, resultArray.length);
    Candidat resultCandidat = resultArray[0];
    assertEquals(1, resultCandidat.getId());
    assertEquals("John", resultCandidat.getNom());
    assertEquals("Doe", resultCandidat.getPrenom());
    assertEquals("john.doe@example.com", resultCandidat.getEmail());
    assertEquals("Male", resultCandidat.getSex());
  }
  
  @Test
  public void testFindCandidat_nullParameters() {
    // test case where all parameters for finding candidats are set to null
    String nom = null;
    String prenom = null;
    String sex = null;
    String email = null;
    
    // expect a null result
    Candidat[] resultArray = dao.findCandidat(nom, prenom, sex, email);
    assertNull(resultArray);
  }
  
  @Test
  public void testFindCandidat_candidatNotFound() {
    // test case where the candidat is not found
    String nom = "Nonexistent";
    String prenom = "Person";
    String sex = "Unknown";
    String email = "nonexistent.person@example.com";
    
    // expect a null result
    Candidat[] resultArray = dao.findCandidat(nom, prenom, sex, email);
    assertNull(resultArray);
  }
  
  @Test
  public void testFindRespForm_typicalCase() {
    // typical case where all parameters are specified for finding responsables de formation
    String nom = "John";
    String prenom = "Doe";
    String sex = "Male";
    String email = "john.doe@example.com";
    
    RespFormation[] resultArray = dao.findRespForm(nom, prenom, sex, email);
    
    // verify if the correct responsables de formation are returned
    assertNotNull(resultArray);
    assertEquals(1, resultArray.length);
    RespFormation resultRespForm = resultArray[0];
    assertEquals(1, resultRespForm.getId());
    assertEquals("John", resultRespForm.getNom());
    assertEquals("Doe", resultRespForm.getPrenom());
    assertEquals("john.doe@example.com", resultRespForm.getEmail());
    assertEquals("Male", resultRespForm.getSex());
  }
  
  @Test
  public void testFindRespForm_nullParameters() {
    // test case where all parameters for finding responsables de formation are set to null
    String nom = null;
    String prenom = null;
    String sex = null;
    String email = null;
    
    // expect a null result
    RespFormation[] resultArray = dao.findRespForm(nom, prenom, sex, email);
    assertNull(resultArray);
  }
  
  @Test
  public void testFindRespForm_respFormNotFound() {
    // test case where the responsable de formation is not found
    String nom = "Nonexistent";
    String prenom = "Person";
    String sex = "Unknown";
    String email = "nonexistent.person@example.com";
    
    // expect a null result
    RespFormation[] resultArray = dao.findRespForm(nom, prenom, sex, email);
    assertNull(resultArray);
  }
}
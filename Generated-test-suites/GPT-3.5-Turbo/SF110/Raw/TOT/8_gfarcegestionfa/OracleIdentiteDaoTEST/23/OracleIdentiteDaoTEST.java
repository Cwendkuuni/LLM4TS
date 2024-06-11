import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Calendar;
import java.util.List;
import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {
  
  private OracleIdentiteDao dao;
  
  @Before
  public void setup() {
    dao = OracleIdentiteDao.getDAO();
  }
  
  @Test
  public void testInsert() {
    Identite identite = new Identite();
    identite.setNom("John");
    identite.setPrenom("Doe");
    identite.setEmail("john.doe@example.com");
    Calendar dateLimite = Calendar.getInstance();
    dao.insert(identite, "Form1", dateLimite);
    Identite insertedIdentite = dao.find(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail(), identite.getType()).get(0);
    assertEquals("Insert test failed!", identite.getNom(), insertedIdentite.getNom());
  }
  
  @Test
  public void testUpdate() {
    Identite identite = new Identite();
    identite.setId(1);
    identite.setNom("John");
    identite.setPrenom("Doe");
    identite.setEmail("john.doe@example.com");
    identite.setType(Identite.TypeIdentite.CANDIDAT);
    dao.update(identite);
    Identite updatedIdentite = dao.find(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail(), identite.getType()).get(0);
    assertEquals("Update test failed!", identite.getNom(), updatedIdentite.getNom());
  }
  
  @Test
  public void testDelete() {
    Identite identite = new Identite();
    identite.setId(1);
    identite.setNom("John");
    identite.setPrenom("Doe");
    identite.setEmail("john.doe@example.com");
    identite.setType(Identite.TypeIdentite.CANDIDAT);
    dao.delete(identite);
    List<Identite> resultList = dao.find(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail(), identite.getType());
    assertEquals("Delete test failed!", 0, resultList.size());
  }
  
  @Test
  public void testFindCandidat() {
    String nom = "John";
    String prenom = "Doe";
    String sex = "Male";
    String email = "john.doe@example.com";
    Candidat candidat = new Candidat();
    candidat.setNom(nom);
    candidat.setPrenom(prenom);
    candidat.setEmail(email);
    candidat.setSex(sex);
    Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
    dao.insert(candidat, "Form1", Calendar.getInstance());
    Candidat[] result = dao.findCandidat(nom, prenom, sex, email);
    assertEquals("Find Candidat test failed!", 1, result.length);
  }
  
  @Test
  public void testFindRespForm() {
    String nom = "John";
    String prenom = "Doe";
    String sex = "Male";
    String email = "john.doe@example.com";
    RespFormation respForm = new RespFormation();
    respForm.setNom(nom);
    respForm.setPrenom(prenom);
    respForm.setEmail(email);
    respForm.setSex(sex);
    Identite.TypeIdentite type = Identite.TypeIdentite.FORMATEUR;
    dao.insert(respForm, "Form1", Calendar.getInstance());
    RespFormation[] result = dao.findRespForm(nom, prenom, sex, email);
    assertEquals("Find RespFormation test failed!", 1, result.length);
  }
  
  @Test
  public void testFind() {
    String nom = "John";
    String prenom = "Doe";
    String sex = "Male";
    String email = "john.doe@example.com";
    Identite candidat = new Candidat();
    candidat.setNom(nom);
    candidat.setPrenom(prenom);
    candidat.setEmail(email);
    candidat.setSex(sex);
    dao.insert(candidat, "Form1", Calendar.getInstance());
    List<Identite> resultList = dao.find(nom, prenom, sex, email, Identite.TypeIdentite.CANDIDAT);
    assertEquals("Find test for Candidat failed!", 1, resultList.size());
    
    Identite respForm = new RespFormation();
    respForm.setNom(nom);
    respForm.setPrenom(prenom);
    respForm.setEmail(email);
    respForm.setSex(sex);
    dao.insert(respForm, "Form1", Calendar.getInstance());
    resultList = dao.find(nom, prenom, sex, email, Identite.TypeIdentite.FORMATEUR);
    assertEquals("Find test for RespFormation failed!", 1, resultList.size());
    
    resultList = dao.find(nom, prenom, sex, email, null);
    assertEquals("Find test for null type failed!", 2, resultList.size());
  }
  
}
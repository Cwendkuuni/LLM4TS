import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class OracleIdentiteDaoTest {
    private OracleIdentiteDao dao;
    
    @Before
    public void setUp() {
        dao = new OracleIdentiteDao();
    }
    
    @Test
    public void testInsert() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setEmail("test@example.com");
        identite.setNom("Test");
        identite.setPrenom("Test");
        identite.setSex("Male");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        
        Calendar dateLimite = Calendar.getInstance();
        dateLimite.set(Calendar.YEAR, 2022);
        dateLimite.set(Calendar.MONTH, Calendar.JANUARY);
        dateLimite.set(Calendar.DAY_OF_MONTH, 1);
        
        dao.insert(identite, "Form", dateLimite);
        
        // Verify insertion
        List<Identite> list = dao.find(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail(), identite.getType());
        assertEquals(1, list.size());
        
        Identite insertedIdentite = list.get(0);
        
        assertEquals(identite.getId(), insertedIdentite.getId());
        assertEquals(identite.getEmail(), insertedIdentite.getEmail());
        assertEquals(identite.getNom(), insertedIdentite.getNom());
        assertEquals(identite.getPrenom(), insertedIdentite.getPrenom());
        assertEquals(identite.getSex(), insertedIdentite.getSex());
        assertEquals(identite.getType(), insertedIdentite.getType());
        
        // Verify dateLimite
        Calendar insertedDateLimite = insertedIdentite.getDateLimite();
        assertEquals(dateLimite.get(Calendar.YEAR), insertedDateLimite.get(Calendar.YEAR));
        assertEquals(dateLimite.get(Calendar.MONTH), insertedDateLimite.get(Calendar.MONTH));
        assertEquals(dateLimite.get(Calendar.DAY_OF_MONTH), insertedDateLimite.get(Calendar.DAY_OF_MONTH));
    }
    
    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setEmail("test@example.com");
        identite.setNom("Test");
        identite.setPrenom("Test");
        identite.setSex("Male");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        
        Calendar dateLimite = Calendar.getInstance();
        dateLimite.set(Calendar.YEAR, 2022);
        dateLimite.set(Calendar.MONTH, Calendar.JANUARY);
        dateLimite.set(Calendar.DAY_OF_MONTH, 1);
        
        dao.insert(identite, "Form", dateLimite);
        
        // Update identite
        identite.setEmail("updated@test.com");
        identite.setPrenom("Updated");
        
        dao.update(identite);
        
        // Verify update
        List<Identite> list = dao.find(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail(), identite.getType());
        assertEquals(1, list.size());
        
        Identite updatedIdentite = list.get(0);
        
        assertEquals(identite.getEmail(), updatedIdentite.getEmail());
        assertEquals(identite.getPrenom(), updatedIdentite.getPrenom());
    }
    
    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setEmail("test@example.com");
        identite.setNom("Test");
        identite.setPrenom("Test");
        identite.setSex("Male");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        
        Calendar dateLimite = Calendar.getInstance();
        dateLimite.set(Calendar.YEAR, 2022);
        dateLimite.set(Calendar.MONTH, Calendar.JANUARY);
        dateLimite.set(Calendar.DAY_OF_MONTH, 1);
        
        dao.insert(identite, "Form", dateLimite);
        
        // Delete identite
        dao.delete(identite);
        
        // Verify deletion
        List<Identite> list = dao.find(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail(), identite.getType());
        assertEquals(0, list.size());
    }
    
    @Test
    public void testFind() {
        Identite identite1 = new Identite();
        identite1.setId(1);
        identite1.setEmail("test1@example.com");
        identite1.setNom("Test1");
        identite1.setPrenom("Test1");
        identite1.setSex("Male");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);
        
        Identite identite2 = new Identite();
        identite2.setId(2);
        identite2.setEmail("test2@example.com");
        identite2.setNom("Test2");
        identite2.setPrenom("Test2");
        identite2.setSex("Female");
        identite2.setType(Identite.TypeIdentite.FORMATEUR);
        
        Calendar dateLimite = Calendar.getInstance();
        dateLimite.set(Calendar.YEAR, 2022);
        dateLimite.set(Calendar.MONTH, Calendar.JANUARY);
        dateLimite.set(Calendar.DAY_OF_MONTH, 1);
        
        dao.insert(identite1, "Form", dateLimite);
        dao.insert(identite2, "Form", dateLimite);
        
        // Find by nom, prenom, sex, email and type
        List<Identite> list1 = dao.find("Test1", "Test1", "Male", "test1@example.com", Identite.TypeIdentite.CANDIDAT);
        assertEquals(1, list1.size());
        assertEquals(identite1.getId(), list1.get(0).getId());
        
        List<Identite> list2 = dao.find("Test2", "Test2", "Female", "test2@example.com", Identite.TypeIdentite.FORMATEUR);
        assertEquals(1, list2.size());
        assertEquals(identite2.getId(), list2.get(0).getId());
        
        // Find all
        List<Identite> list3 = dao.find(null, null, null, null, null);
        assertEquals(2, list3.size());
    }
    
    @Test
    public void testFindCandidat() {
        Identite identite1 = new Identite();
        identite1.setId(1);
        identite1.setEmail("test1@example.com");
        identite1.setNom("Test1");
        identite1.setPrenom("Test1");
        identite1.setSex("Male");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);
        
        Identite identite2 = new Identite();
        identite2.setId(2);
        identite2.setEmail("test2@example.com");
        identite2.setNom("Test2");
        identite2.setPrenom("Test2");
        identite2.setSex("Female");
        identite2.setType(Identite.TypeIdentite.FORMATEUR);
        
        Calendar dateLimite = Calendar.getInstance();
        dateLimite.set(Calendar.YEAR, 2022);
        dateLimite.set(Calendar.MONTH, Calendar.JANUARY);
        dateLimite.set(Calendar.DAY_OF_MONTH, 1);
        
        dao.insert(identite1, "Form", dateLimite);
        dao.insert(identite2, "Form", dateLimite);
        
        // Find candidat by nom, prenom, sex and email
        Candidat[] candidatList1 = dao.findCandidat("Test1", "Test1", "Male", "test1@example.com");
        assertEquals(1, candidatList1.length);
        assertEquals(identite1.getId(), candidatList1[0].getId());
        
        // Find candidat by nom, prenom, sex and email when there are no results
        Candidat[] candidatList2 = dao.findCandidat("Test2", "Test2", "Female", "test2@example.com");
        assertNull(candidatList2);
    }
    
    @Test
    public void testFindRespForm() {
        Identite identite1 = new Identite();
        identite1.setId(1);
        identite1.setEmail("test1@example.com");
        identite1.setNom("Test1");
        identite1.setPrenom("Test1");
        identite1.setSex("Male");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);
        
        Identite identite2 = new Identite();
        identite2.setId(2);
        identite2.setEmail("test2@example.com");
        identite2.setNom("Test2");
        identite2.setPrenom("Test2");
        identite2.setSex("Female");
        identite2.setType(Identite.TypeIdentite.FORMATEUR);
        
        Calendar dateLimite = Calendar.getInstance();
        dateLimite.set(Calendar.YEAR, 2022);
        dateLimite.set(Calendar.MONTH, Calendar.JANUARY);
        dateLimite.set(Calendar.DAY_OF_MONTH, 1);
        
        dao.insert(identite1, "Form", dateLimite);
        dao.insert(identite2, "Form", dateLimite);
        
        // Find RespFormation by nom, prenom, sex and email
        RespFormation[] respFormList1 = dao.findRespForm("Test2", "Test2", "Female", "test2@example.com");
        assertEquals(1, respFormList1.length);
        assertEquals(identite2.getId(), respFormList1[0].getId());
        
        // Find RespFormation by nom, prenom, sex and email when there are no results
        RespFormation[] respFormList2 = dao.findRespForm("Test1", "Test1", "Male", "test1@example.com");
        assertNull(respFormList2);
    }

}
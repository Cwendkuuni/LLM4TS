import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class OracleIdentiteDaoTest {
    
    private OracleIdentiteDao dao;
    
    @Before
    public void setUp() {
        dao = OracleIdentiteDao.getDAO();
    }
    
    @Test
    public void testInsert() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setEmail("test@example.com");
        identite.setNom("Test");
        identite.setPrenom("User");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar dateLimite = Calendar.getInstance();
        
        dao.insert(identite, "nomForm", dateLimite);
        
        List<Identite> list = dao.find("Test", "User", "M", "test@example.com", Identite.TypeIdentite.CANDIDAT);
        assertEquals(1, list.size());
        Identite retrieved = list.get(0);
        assertEquals(1, retrieved.getId());
        assertEquals("test@example.com", retrieved.getEmail());
        assertEquals("Test", retrieved.getNom());
        assertEquals("User", retrieved.getPrenom());
        assertEquals("M", retrieved.getSex());
        assertEquals(Identite.TypeIdentite.CANDIDAT, retrieved.getType());
    }
    
    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setEmail("test@example.com");
        identite.setNom("Test");
        identite.setPrenom("User");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        
        dao.insert(identite, "nomForm", Calendar.getInstance());
        
        identite.setEmail("newemail@example.com");
        identite.setNom("NewTest");
        identite.setPrenom("NewUser");
        identite.setSex("F");
        identite.setType(Identite.TypeIdentite.FORMATEUR);
        
        dao.update(identite);
        
        List<Identite> list = dao.find("NewTest", "NewUser", "F", "newemail@example.com", Identite.TypeIdentite.FORMATEUR);
        assertEquals(1, list.size());
        Identite retrieved = list.get(0);
        assertEquals(1, retrieved.getId());
        assertEquals("newemail@example.com", retrieved.getEmail());
        assertEquals("NewTest", retrieved.getNom());
        assertEquals("NewUser", retrieved.getPrenom());
        assertEquals("F", retrieved.getSex());
        assertEquals(Identite.TypeIdentite.FORMATEUR, retrieved.getType());
    }
    
    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setEmail("test@example.com");
        identite.setNom("Test");
        identite.setPrenom("User");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        
        dao.insert(identite, "nomForm", Calendar.getInstance());
        
        dao.delete(identite);
        
        List<Identite> list = dao.find("Test", "User", "M", "test@example.com", Identite.TypeIdentite.CANDIDAT);
        assertEquals(0, list.size());
        
    }
    
    @Test
    public void testFindCandidat() {
        Identite identite1 = new Identite();
        identite1.setId(1);
        identite1.setEmail("test1@example.com");
        identite1.setNom("Test1");
        identite1.setPrenom("User1");
        identite1.setSex("M");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);
        
        dao.insert(identite1, "nomForm", Calendar.getInstance());
        
        Identite identite2 = new Identite();
        identite2.setId(2);
        identite2.setEmail("test2@example.com");
        identite2.setNom("Test2");
        identite2.setPrenom("User2");
        identite2.setSex("F");
        identite2.setType(Identite.TypeIdentite.CANDIDAT);
        
        dao.insert(identite2, "nomForm", Calendar.getInstance());
        
        Candidat[] candidats = dao.findCandidat("Test", null, null, null);
        assertEquals(2, candidats.length);
        
        assertEquals(1, candidats[0].getId());
        assertEquals("test1@example.com", candidats[0].getEmail());
        assertEquals("Test1", candidats[0].getNom());
        assertEquals("User1", candidats[0].getPrenom());
        assertEquals("M", candidats[0].getSex());
        
        assertEquals(2, candidats[1].getId());
        assertEquals("test2@example.com", candidats[1].getEmail());
        assertEquals("Test2", candidats[1].getNom());
        assertEquals("User2", candidats[1].getPrenom());
        assertEquals("F", candidats[1].getSex());
    }
    
    @Test
    public void testFindRespForm() {
        Identite identite1 = new Identite();
        identite1.setId(1);
        identite1.setEmail("test1@example.com");
        identite1.setNom("Test1");
        identite1.setPrenom("User1");
        identite1.setSex("M");
        identite1.setType(Identite.TypeIdentite.FORMATEUR);
        
        dao.insert(identite1, "nomForm", Calendar.getInstance());
        
        Identite identite2 = new Identite();
        identite2.setId(2);
        identite2.setEmail("test2@example.com");
        identite2.setNom("Test2");
        identite2.setPrenom("User2");
        identite2.setSex("F");
        identite2.setType(Identite.TypeIdentite.FORMATEUR);
        
        dao.insert(identite2, "nomForm", Calendar.getInstance());
        
        RespFormation[] respForms = dao.findRespForm("Test", null, null, null);
        assertEquals(2, respForms.length);
        
        assertEquals(1, respForms[0].getId());
        assertEquals("test1@example.com", respForms[0].getEmail());
        assertEquals("Test1", respForms[0].getNom());
        assertEquals("User1", respForms[0].getPrenom());
        assertEquals("M", respForms[0].getSex());
        
        assertEquals(2, respForms[1].getId());
        assertEquals("test2@example.com", respForms[1].getEmail());
        assertEquals("Test2", respForms[1].getNom());
        assertEquals("User2", respForms[1].getPrenom());
        assertEquals("F", respForms[1].getSex());
    }
    
}
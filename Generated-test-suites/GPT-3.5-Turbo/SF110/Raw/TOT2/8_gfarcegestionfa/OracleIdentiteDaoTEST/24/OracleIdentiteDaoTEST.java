import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OracleIdentiteDaoTest {
    
    private OracleIdentiteDao dao;
    private Identite identite;
    
    @Before
    public void setUp() {
        dao = new OracleIdentiteDao();
        identite = new Identite();
        identite.setId(1);
        identite.setEmail("test@test.com");
        identite.setNom("TestNom");
        identite.setPrenom("TestPrenom");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
    }
    
    @After
    public void tearDown() {
        dao = null;
        identite = null;
    }
    
    @Test
    public void testInsert() {
        dao.insert(identite, "Form", Calendar.getInstance());
        assertNotNull(dao.findCandidat("TestNom", "TestPrenom", "M", "test@test.com"));
    }
    
    @Test
    public void testUpdate() {
        dao.insert(identite, "Form", Calendar.getInstance());
        identite.setEmail("updated@test.com");
        dao.update(identite);
        assertEquals("updated@test.com", dao.findCandidat("TestNom", "TestPrenom", "M", "updated@test.com")[0].getEmail());
    }
    
    @Test
    public void testDelete() {
        dao.insert(identite, "Form", Calendar.getInstance());
        dao.delete(identite);
        assertEquals(null, dao.findCandidat("TestNom", "TestPrenom", "M", "test@test.com"));
    }
    
    @Test
    public void testFind() {
        assertNotNull(dao.find("TestNom", "TestPrenom", "M", "test@test.com", Identite.TypeIdentite.CANDIDAT));
    }
    
    @Test
    public void testFindCandidat() {
        assertNotNull(dao.findCandidat("TestNom", "TestPrenom", "M", "test@test.com"));
    }
    
    @Test
    public void testFindRespForm() {
        assertNotNull(dao.findRespForm("TestNom", "TestPrenom", "M", "test@test.com"));
    }

}
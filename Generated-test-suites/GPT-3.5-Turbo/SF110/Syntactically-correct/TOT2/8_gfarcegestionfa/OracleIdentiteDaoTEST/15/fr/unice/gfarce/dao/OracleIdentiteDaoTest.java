package fr.unice.gfarce.dao;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Calendar;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {
    OracleIdentiteDao dao = new OracleIdentiteDao();
    
    @Test
    public void testInsert() {
        Identite identite = new Identite("Test", "User", "test@test.com", "M", Identite.TypeIdentite.CANDIDAT);
        dao.insert(identite, "Form", Calendar.getInstance());
        assertNotNull(identite.getId());
    }
    
    @Test
    public void testUpdate() {
        Identite identite = new Identite("Test", "User", "test@test.com", "M", Identite.TypeIdentite.CANDIDAT);
        dao.insert(identite, "Form", Calendar.getInstance());
        identite.setEmail("new@test.com");
        dao.update(identite);
        Identite updatedIdentite = (Identite) dao.find(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail(), identite.getType()).get(0);
        assertEquals("new@test.com", updatedIdentite.getEmail());
    }
    
    @Test
    public void testDelete() {
        Identite identite = new Identite("Test", "User", "test@test.com", "M", Identite.TypeIdentite.CANDIDAT);
        dao.insert(identite, "Form", Calendar.getInstance());
        dao.delete(identite);
        assertTrue(dao.find(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail(), identite.getType()).isEmpty());
    }
    
    @Test
    public void testFind() {
        Identite identite1 = new Identite("Test1", "User1", "test1@test.com", "M", Identite.TypeIdentite.CANDIDAT);
        Identite identite2 = new Identite("Test2", "User2", "test2@test.com", "F", Identite.TypeIdentite.FORMATEUR);
        dao.insert(identite1, "Form", Calendar.getInstance());
        dao.insert(identite2, "Form", Calendar.getInstance());
        
        assertEquals(2, dao.find(null, null, null, null, null).size());
        assertEquals(1, dao.find("Test1", null, null, null, null).size());
        assertEquals(1, dao.find(null, "User2", null, null, null).size());
        assertEquals(1, dao.find(null, null, "F", null, null).size());
    }
    
    @Test
    public void testFindCandidat() {
        Identite identite1 = new Identite("Test1", "User1", "test1@test.com", "M", Identite.TypeIdentite.CANDIDAT);
        Identite identite2 = new Identite("Test2", "User2", "test2@test.com", "F", Identite.TypeIdentite.FORMATEUR);
        dao.insert(identite1, "Form", Calendar.getInstance());
        dao.insert(identite2, "Form", Calendar.getInstance());
        
        assertEquals(1, dao.findCandidat("Test1", null, null, null).length);
        assertEquals("Test1", dao.findCandidat("Test1", null, null, null)[0].getNom());
    }
    
    @Test
    public void testFindRespForm() {
        Identite identite1 = new Identite("Test1", "User1", "test1@test.com", "M", Identite.TypeIdentite.CANDIDAT);
        Identite identite2 = new Identite("Test2", "User2", "test2@test.com", "F", Identite.TypeIdentite.FORMATEUR);
        dao.insert(identite1, "Form", Calendar.getInstance());
        dao.insert(identite2, "Form", Calendar.getInstance());
        
        assertEquals(1, dao.findRespForm(null, "User2", null, null).length);
        assertEquals("User2", dao.findRespForm(null, "User2", null, null)[0].getPrenom());
    }
}
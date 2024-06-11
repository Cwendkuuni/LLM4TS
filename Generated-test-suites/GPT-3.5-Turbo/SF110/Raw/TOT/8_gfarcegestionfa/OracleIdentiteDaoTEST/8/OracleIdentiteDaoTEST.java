import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {
    
    private OracleIdentiteDao dao;
    private Identite validIdentite;
    private Identite invalidIdentite;
    
    @Before
    public void setUp() {
        dao = OracleIdentiteDao.getDAO();
        validIdentite = new Identite(1, "John", "Doe", "john.doe@example.com", "M", Identite.TypeIdentite.CANDIDAT);
        invalidIdentite = new Identite(2, "Jane", "Doe", "jane.doe@example.com", "F", Identite.TypeIdentite.FORMATEUR);
    }
    
    @Test
    public void testInsert() {
        try {
            dao.insert(validIdentite, "TestForm", Calendar.getInstance());
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }
    
    @Test
    public void testUpdate() {
        try {
            dao.update(validIdentite);
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }
    
    @Test
    public void testDelete() {
        try {
            dao.delete(validIdentite);
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }
    
    @Test
    public void testFind() {
        try {
            List<Identite> result = dao.find(validIdentite.getNom(), validIdentite.getPrenom(), validIdentite.getSex(), validIdentite.getEmail(), validIdentite.getType());
            assertNotNull(result);
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }
    
    @Test
    public void testFindCandidat() {
        try {
            Candidat[] result = dao.findCandidat(validIdentite.getNom(), validIdentite.getPrenom(), validIdentite.getSex(), validIdentite.getEmail());
            assertNotNull(result);
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }
    
    @Test
    public void testFindRespForm() {
        try {
            RespFormation[] result = dao.findRespForm(validIdentite.getNom(), validIdentite.getPrenom(), validIdentite.getSex(), validIdentite.getEmail());
            assertNotNull(result);
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }
    
    @Test(expected = Exception.class)
    public void testInsertException() throws Exception {
        dao.insert(invalidIdentite, "TestForm", Calendar.getInstance());
    }
    
    @Test(expected = Exception.class)
    public void testUpdateException() throws Exception {
        dao.update(invalidIdentite);
    }
    
    @Test(expected = Exception.class)
    public void testDeleteException() throws Exception {
        dao.delete(invalidIdentite);
    }
    
    @Test(expected = Exception.class)
    public void testFindException() throws Exception {
        dao.find(invalidIdentite.getNom(), invalidIdentite.getPrenom(), invalidIdentite.getSex(), invalidIdentite.getEmail(), invalidIdentite.getType());
    }
    
    @Test(expected = Exception.class)
    public void testFindCandidatException() throws Exception {
        dao.findCandidat(invalidIdentite.getNom(), invalidIdentite.getPrenom(), invalidIdentite.getSex(), invalidIdentite.getEmail());
    }
    
    @Test(expected = Exception.class)
    public void testFindRespFormException() throws Exception {
        dao.findRespForm(invalidIdentite.getNom(), invalidIdentite.getPrenom(), invalidIdentite.getSex(), invalidIdentite.getEmail());
    }
}
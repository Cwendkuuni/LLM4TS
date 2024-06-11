import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {
    
    private OracleIdentiteDao dao;
    private Identite identite;
    
    @Before
    public void setUp() {
        dao = OracleIdentiteDao.getDAO();
        identite = new Identite();
        identite.setId(1);
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("john.doe@example.com");
        identite.setSex("male");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
    }
    
    @Test
    public void testInsert() {
        dao.insert(identite, "form", Calendar.getInstance());
        Identite inserted = dao.findCandidat(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail())[0];
        assertNotNull(inserted);
        assertEquals(identite.getId(), inserted.getId());
        assertEquals(identite.getNom(), inserted.getNom());
        assertEquals(identite.getPrenom(), inserted.getPrenom());
        assertEquals(identite.getEmail(), inserted.getEmail());
        assertEquals(identite.getSex(), inserted.getSex());
        assertEquals(identite.getType(), inserted.getType());
    }
    
    @Test
    public void testUpdate() {
        dao.insert(identite, "form", Calendar.getInstance());
        identite.setEmail("updated.email@example.com");
        identite.setNom("Updated");
        identite.setPrenom("Name");
        identite.setSex("female");
        identite.setType(Identite.TypeIdentite.FORMATEUR);
        dao.update(identite);
        Identite updated = dao.findRespForm(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail())[0];
        assertNotNull(updated);
        assertEquals(identite.getId(), updated.getId());
        assertEquals(identite.getNom(), updated.getNom());
        assertEquals(identite.getPrenom(), updated.getPrenom());
        assertEquals(identite.getEmail(), updated.getEmail());
        assertEquals(identite.getSex(), updated.getSex());
        assertEquals(identite.getType(), updated.getType());
    }
    
    @Test
    public void testDelete() {
        dao.insert(identite, "form", Calendar.getInstance());
        dao.delete(identite);
        Identite deleted = dao.findCandidat(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail())[0];
        assertNull(deleted);
    }
    
    @Test
    public void testFind_withNullValues() {
        List<Identite> resultList = dao.find(null, null, null, null, null);
        assertNotNull(resultList);
        assertEquals(0, resultList.size());
    }
    
    @Test
    public void testFindCandidat() {
        dao.insert(identite, "form", Calendar.getInstance());
        Candidat[] result = dao.findCandidat(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail());
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals(identite.getId(), result[0].getId());
        assertEquals(identite.getNom(), result[0].getNom());
        assertEquals(identite.getPrenom(), result[0].getPrenom());
        assertEquals(identite.getEmail(), result[0].getEmail());
        assertEquals(identite.getSex(), result[0].getSex());
        assertEquals(identite.getType(), result[0].getType());
    }
    
    @Test
    public void testFindRespForm() {
        dao.insert(identite, "form", Calendar.getInstance());
        RespFormation[] result = dao.findRespForm(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail());
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals(identite.getId(), result[0].getId());
        assertEquals(identite.getNom(), result[0].getNom());
        assertEquals(identite.getPrenom(), result[0].getPrenom());
        assertEquals(identite.getEmail(), result[0].getEmail());
        assertEquals(identite.getSex(), result[0].getSex());
        assertEquals(identite.getType(), result[0].getType());
    }
}
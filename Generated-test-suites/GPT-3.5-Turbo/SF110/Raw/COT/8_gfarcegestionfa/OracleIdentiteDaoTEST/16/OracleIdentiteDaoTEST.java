import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

public class OracleIdentiteDaoTest {

    private OracleIdentiteDao dao;

    @Before
    public void setup() {
        dao = OracleIdentiteDao.getDAO();
    }

    @Test
    public void testInsert() {
        Identite identite = new Identite();
        identite.setEmail("test@test.com");
        identite.setNom("Test");
        identite.setPrenom("User");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar dateLimite = Calendar.getInstance();

        dao.insert(identite, "form", dateLimite);

        // Assert the insertion was successful
        List<Identite> result = dao.find(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail(), identite.getType());
        assertTrue(result.contains(identite));
    }

    @Test
    public void testUpdate() {
        // Insert initial identite
        Identite identite = new Identite();
        identite.setEmail("test@test.com");
        identite.setNom("Test");
        identite.setPrenom("User");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar dateLimite = Calendar.getInstance();
        dao.insert(identite, "form", dateLimite);

        // Update the identite
        identite.setEmail("updated@test.com");
        identite.setNom("Updated");
        identite.setPrenom("User");

        dao.update(identite);

        // Assert the update was successful
        List<Identite> result = dao.find(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail(), identite.getType());
        assertTrue(result.contains(identite));
    }

    @Test
    public void testDelete() {
        // Insert initial identite
        Identite identite = new Identite();
        identite.setEmail("test@test.com");
        identite.setNom("Test");
        identite.setPrenom("User");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar dateLimite = Calendar.getInstance();
        dao.insert(identite, "form", dateLimite);

        // Delete the identite
        dao.delete(identite);

        // Assert the deletion was successful
        List<Identite> result = dao.find(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail(), identite.getType());
        assertFalse(result.contains(identite));
    }

    @Test
    public void testFind() {
        // Insert a test candidate and a test trainer
        Identite candidate = new Identite();
        candidate.setEmail("test@test.com");
        candidate.setNom("Test");
        candidate.setPrenom("User");
        candidate.setSex("M");
        candidate.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar dateLimite = Calendar.getInstance();
        dao.insert(candidate, "form", dateLimite);

        Identite trainer = new Identite();
        trainer.setEmail("test2@test.com");
        trainer.setNom("Test");
        trainer.setPrenom("User");
        trainer.setSex("M");
        trainer.setType(Identite.TypeIdentite.FORMATEUR);
        dao.insert(trainer, "form", dateLimite);

        // Find candidates with the given criteria
        List<Identite> candidates = dao.find(candidate.getNom(), candidate.getPrenom(), candidate.getSex(), candidate.getEmail(), candidate.getType());

        // Find trainers with the given criteria
        List<Identite> trainers = dao.find(trainer.getNom(), trainer.getPrenom(), trainer.getSex(), trainer.getEmail(), trainer.getType());

        // Assert the correct candidates and trainers are returned
        assertTrue(candidates.contains(candidate));
        assertFalse(candidates.contains(trainer));

        assertFalse(trainers.contains(candidate));
        assertTrue(trainers.contains(trainer));
    }

    @Test
    public void testFindCandidat() {
        // Insert a test candidate
        Identite candidate = new Identite();
        candidate.setEmail("test@test.com");
        candidate.setNom("Test");
        candidate.setPrenom("User");
        candidate.setSex("M");
        candidate.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar dateLimite = Calendar.getInstance();
        dao.insert(candidate, "form", dateLimite);

        // Find candidates with the given criteria
        Candidat[] candidates = dao.findCandidat(candidate.getNom(), candidate.getPrenom(), candidate.getSex(), candidate.getEmail());

        // Assert the correct candidate is returned
        assertEquals(1, candidates.length);
        assertEquals(candidate, candidates[0]);
    }

    @Test
    public void testFindRespForm() {
        // Insert a test trainer
        Identite trainer = new Identite();
        trainer.setEmail("test@test.com");
        trainer.setNom("Test");
        trainer.setPrenom("User");
        trainer.setSex("M");
        trainer.setType(Identite.TypeIdentite.FORMATEUR);
        Calendar dateLimite = Calendar.getInstance();
        dao.insert(trainer, "form", dateLimite);

        // Find trainers with the given criteria
        RespFormation[] trainers = dao.findRespForm(trainer.getNom(), trainer.getPrenom(), trainer.getSex(), trainer.getEmail());

        // Assert the correct trainer is returned
        assertEquals(1, trainers.length);
        assertEquals(trainer, trainers[0]);
    }

}
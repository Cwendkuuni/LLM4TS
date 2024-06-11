import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class OracleIdentiteDaoTest {

    private OracleIdentiteDao dao;

    @Before
    public void setup() {
        dao = new OracleIdentiteDao();
    }

    // Test insert method
    @Test
    public void testInsert() {
        Identite identite = mock(Identite.class);
        String nomForm = "Formateur";
        Calendar dateLimite = Calendar.getInstance();

        dao.insert(identite, nomForm, dateLimite);

        // Assert that insert method successfully persists the identite object
        // Verify that em.persist method is called
        verify(dao.em).persist(identite);
    }

    // Test update method
    @Test
    public void testUpdate() {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        EntityManager em = mock(EntityManager.class);
        EntityTransaction tx = mock(EntityTransaction.class);
        Identite identite = mock(Identite.class);
        
        when(emf.createEntityManager()).thenReturn(em);
        
        dao.em = em;
        dao.persistUnitName = "testPersistUnitName";

        // Test updating existing Identite object
        when(em.find(Identite.class, identite.getId())).thenReturn(identite);
        
        dao.update(identite);
        
        // Verify that em.merge method is called
        verify(em).merge(identite);
    }

    // Test delete method
    @Test
    public void testDelete() {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        EntityManager em = mock(EntityManager.class);
        EntityTransaction tx = mock(EntityTransaction.class);
        Identite identite = mock(Identite.class);

        when(emf.createEntityManager()).thenReturn(em);
        
        dao.em = em;
        dao.persistUnitName = "testPersistUnitName";

        // Test deleting an Identite object
        when(em.find(Identite.class, identite.getId())).thenReturn(identite);
        
        dao.delete(identite);
        
        // Verify that em.remove method is called
        verify(em).remove(identite);
    }

    // Test find method
    @Test
    public void testFind() {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        EntityManager em = mock(EntityManager.class);
        EntityTransaction tx = mock(EntityTransaction.class);
        List<Identite> resultList = mock(List.class);

        when(emf.createEntityManager()).thenReturn(em);
        
        dao.em = em;
        dao.persistUnitName = "testPersistUnitName";

        String nom = "TestNom";
        String prenom = "TestPrenom";
        String sex = "Male";
        String email = "test@gmail.com";
        Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

        // Test find method with specified parameters
        when(dao.em.createQuery(anyString())).thenReturn(queryMock);
        when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(resultList);
        
        List<Identite> result = dao.find(nom, prenom, sex, email, type);

        // Assert that the result list is equal to the mocked result list
        Assert.assertEquals(resultList, result);
    }

    // Test findCandidat method
    @Test
    public void testFindCandidat() {
        String nom = "TestNom";
        String prenom = "TestPrenom";
        String sex = "Male";
        String email = "test@gmail.com";
        List<Identite> resultList = mock(List.class);
        Candidat[] expected = new Candidat[1];
        expected[0] = new Candidat();

        when(dao.find(nom, prenom, sex, email, Identite.TypeIdentite.CANDIDAT)).thenReturn(resultList);
        when(resultList.toArray(any(Candidat[].class))).thenReturn(expected);
        
        Candidat[] result = dao.findCandidat(nom, prenom, sex, email);

        // Verify that the dao.find method is called with specified parameters
        verify(dao).find(nom, prenom, sex, email, Identite.TypeIdentite.CANDIDAT);

        // Assert that the result array is equal to the expected array
        Assert.assertArrayEquals(expected, result);
    }

    // Test findRespForm method
    @Test
    public void testFindRespForm() {
        String nom = "TestNom";
        String prenom = "TestPrenom";
        String sex = "Male";
        String email = "test@gmail.com";
        List<Identite> resultList = mock(List.class);
        RespFormation[] expected = new RespFormation[1];
        expected[0] = new RespFormation();

        when(dao.find(nom, prenom, sex, email, Identite.TypeIdentite.FORMATEUR)).thenReturn(resultList);
        when(resultList.toArray(any(RespFormation[].class))).thenReturn(expected);
        
        RespFormation[] result = dao.findRespForm(nom, prenom, sex, email);

        // Verify that the dao.find method is called with specified parameters
        verify(dao).find(nom, prenom, sex, email, Identite.TypeIdentite.FORMATEUR);

        // Assert that the result array is equal to the expected array
        Assert.assertArrayEquals(expected, result);
    }

}
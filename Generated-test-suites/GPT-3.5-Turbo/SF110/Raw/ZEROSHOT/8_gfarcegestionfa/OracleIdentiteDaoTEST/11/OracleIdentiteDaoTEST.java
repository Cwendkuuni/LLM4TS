import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;
import javax.persistence.*;
import java.util.*;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest{
    
    @Test
    public void testInsert(){
        EntityManagerFactory emfMock = Mockito.mock(EntityManagerFactory.class);
        EntityManager emMock = Mockito.mock(EntityManager.class);
        
        OracleIdentiteDao dao = new OracleIdentiteDao();
        dao.em = emMock;
        
        Identite identite = Mockito.mock(Identite.class);
        String nomForm = "nomForm";
        Calendar dateLimite = Calendar.getInstance();
        
        try{
            Mockito.when(emMock.getTransaction()).thenReturn(Mockito.mock(EntityTransaction.class));
            
            dao.insert(identite, nomForm, dateLimite);
            
            Mockito.verify(emMock, Mockito.times(1)).persist(Mockito.anyObject());
            Mockito.verify(emMock.getTransaction(), Mockito.times(1)).commit();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    @Test
    public void testUpdate(){
        EntityManagerFactory emfMock = Mockito.mock(EntityManagerFactory.class);
        EntityManager emMock = Mockito.mock(EntityManager.class);
        
        OracleIdentiteDao dao = new OracleIdentiteDao();
        dao.em = emMock;
        
        Identite identite = Mockito.mock(Identite.class);
        
        try{
            Mockito.when(emMock.getTransaction()).thenReturn(Mockito.mock(EntityTransaction.class));
            Mockito.when(emMock.find(Mockito.anyObject(), Mockito.anyObject())).thenReturn(Mockito.mock(Identite.class));
            
            dao.update(identite);
            
            Mockito.verify(emMock, Mockito.times(1)).merge(Mockito.anyObject());
            Mockito.verify(emMock.getTransaction(), Mockito.times(1)).commit();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    @Test
    public void testDelete(){
        EntityManagerFactory emfMock = Mockito.mock(EntityManagerFactory.class);
        EntityManager emMock = Mockito.mock(EntityManager.class);
        
        OracleIdentiteDao dao = new OracleIdentiteDao();
        dao.em = emMock;
        
        Identite identite = Mockito.mock(Identite.class);
        
        try{
            Mockito.when(emMock.getTransaction()).thenReturn(Mockito.mock(EntityTransaction.class));
            Mockito.when(emMock.find(Mockito.anyObject(), Mockito.anyObject())).thenReturn(Mockito.mock(Identite.class));
            
            dao.delete(identite);
            
            Mockito.verify(emMock, Mockito.times(1)).remove(Mockito.anyObject());
            Mockito.verify(emMock.getTransaction(), Mockito.times(1)).commit();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    @Test
    public void testFind(){
        EntityManagerFactory emfMock = Mockito.mock(EntityManagerFactory.class);
        EntityManager emMock = Mockito.mock(EntityManager.class);
        
        OracleIdentiteDao dao = new OracleIdentiteDao();
        dao.em = emMock;
        
        String nom = "nom";
        String prenom = "prenom";
        String sex = "sex";
        String email = "email";
        String type = "type";
        
        try{
            Mockito.when(emMock.getTransaction()).thenReturn(Mockito.mock(EntityTransaction.class));
            Mockito.when(emMock.createQuery(Mockito.anyString())).thenReturn(Mockito.mock(Query.class));
            Mockito.when(emMock.createQuery(Mockito.anyString()).setParameter(Mockito.anyString(), Mockito.anyObject())).thenReturn(Mockito.mock(Query.class));
            Mockito.when(emMock.createQuery(Mockito.anyString()).setParameter(Mockito.anyString(), Mockito.anyObject()).getResultList()).thenReturn(Arrays.asList(Mockito.mock(Identite.class)));
            
            List<Identite> result = dao.find(nom, prenom, sex, email, Identite.TypeIdentite.CANDIDAT);
            
            assertNotNull(result);
            assertEquals(1, result.size());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    @Test
    public void testFindCandidat(){
        EntityManagerFactory emfMock = Mockito.mock(EntityManagerFactory.class);
        EntityManager emMock = Mockito.mock(EntityManager.class);
        
        OracleIdentiteDao dao = new OracleIdentiteDao();
        dao.em = emMock;
        
        String nom = "nom";
        String prenom = "prenom";
        String sex = "sex";
        String email = "email";
        
        try{
            Mockito.when(emMock.getTransaction()).thenReturn(Mockito.mock(EntityTransaction.class));
            Mockito.when(emMock.createQuery(Mockito.anyString())).thenReturn(Mockito.mock(Query.class));
            Mockito.when(emMock.createQuery(Mockito.anyString()).setParameter(Mockito.anyString(), Mockito.anyObject())).thenReturn(Mockito.mock(Query.class));
            Mockito.when(emMock.createQuery(Mockito.anyString()).setParameter(Mockito.anyString(), Mockito.anyObject()).getResultList()).thenReturn(Arrays.asList(Mockito.mock(Identite.class)));
            
            Candidat[] result = dao.findCandidat(nom, prenom, sex, email);
            
            assertNotNull(result);
            assertEquals(1, result.length);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    @Test
    public void testFindRespForm(){
        EntityManagerFactory emfMock = Mockito.mock(EntityManagerFactory.class);
        EntityManager emMock = Mockito.mock(EntityManager.class);
        
        OracleIdentiteDao dao = new OracleIdentiteDao();
        dao.em = emMock;
        
        String nom = "nom";
        String prenom = "prenom";
        String sex = "sex";
        String email = "email";
        
        try{
            Mockito.when(emMock.getTransaction()).thenReturn(Mockito.mock(EntityTransaction.class));
            Mockito.when(emMock.createQuery(Mockito.anyString())).thenReturn(Mockito.mock(Query.class));
            Mockito.when(emMock.createQuery(Mockito.anyString()).setParameter(Mockito.anyString(), Mockito.anyObject())).thenReturn(Mockito.mock(Query.class));
            Mockito.when(emMock.createQuery(Mockito.anyString()).setParameter(Mockito.anyString(), Mockito.anyObject()).getResultList()).thenReturn(Arrays.asList(Mockito.mock(Identite.class)));
            
            RespFormation[] result = dao.findRespForm(nom, prenom, sex, email);
            
            assertNotNull(result);
            assertEquals(1, result.length);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
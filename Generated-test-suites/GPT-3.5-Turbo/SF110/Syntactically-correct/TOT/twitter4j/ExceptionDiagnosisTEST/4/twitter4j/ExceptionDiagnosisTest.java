import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosis() {
        Throwable t = new Throwable("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(t);
        
        assertNotNull(exceptionDiagnosis);
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
        assertEquals("00000000-00000000", exceptionDiagnosis.asHexString());
    }
    
    @Test
    public void testExceptionDiagnosisWithInclusionFilter() {
        Throwable t = new Throwable("Test Exception");
        String[] inclusionFilter = {"twitter4j"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(t, inclusionFilter);
        
        assertNotNull(exceptionDiagnosis);
        assertNotEquals(0, exceptionDiagnosis.getStackLineHash());
        assertNotEquals(0, exceptionDiagnosis.getLineNumberHash());
        assertNotEquals("00000000-00000000", exceptionDiagnosis.asHexString());
    }
    
    @Test
    public void testEquals() {
        Throwable t1 = new Throwable("Test Exception");
        Throwable t2 = new Throwable("Test Exception");
        
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(t1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(t2);
        
        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis2));
    }
    
    @Test
    public void testHashCode() {
        Throwable t = new Throwable("Test Exception");
        
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(t);
        
        assertNotNull(exceptionDiagnosis);
        assertNotEquals(0, exceptionDiagnosis.hashCode());
    }
    
    @Test
    public void testToString() {
        Throwable t = new Throwable("Test Exception");
        
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(t);
        
        assertNotNull(exceptionDiagnosis);
        assertTrue(exceptionDiagnosis.toString().contains("ExceptionDiagnosis{stackLineHash="));
    }
}
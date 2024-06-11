import static org.junit.Assert.*;
import org.junit.Test;

public class ExceptionDiagnosisTest {

    // Test case for ExceptionDiagnosis(th) constructor
    @Test
    public void testExceptionDiagnosisConstructorWithThrowable() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        
        assertNotNull(exceptionDiagnosis);
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
        assertEquals("00000000-00000000", exceptionDiagnosis.asHexString());
    }

    // Test case for ExceptionDiagnosis(th, inclusionFilter) constructor
    @Test
    public void testExceptionDiagnosisConstructorWithThrowableAndInclusionFilter() {
        Throwable th = new Throwable("Test Exception");
        String[] inclusionFilter = new String[]{"twitter4j"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        
        assertNotNull(exceptionDiagnosis);
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
        assertEquals("00000000-00000000", exceptionDiagnosis.asHexString());     
    }

    // Test case for getStackLineHash() method
    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        
        int stackLineHash = exceptionDiagnosis.getStackLineHash();
        
        assertEquals(0, stackLineHash);
    }

    // Test case for getLineNumberHash() method
    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        
        int lineNumberHash = exceptionDiagnosis.getLineNumberHash();
        
        assertEquals(0, lineNumberHash);
    }

    // Test case for asHexString() method
    @Test
    public void testAsHexString() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        
        String hexString = exceptionDiagnosis.asHexString();
        
        assertEquals("00000000-00000000", hexString);
    }

    // Test case for equals() method
    @Test
    public void testEquals() {
        Throwable th1 = new Throwable("Test Exception 1");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(th1);
        
        Throwable th2 = new Throwable("Test Exception 2");
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(th2);
        
        assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));
    }

    // Test case for hashCode() method
    @Test
    public void testHashCode() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        
        int hashCode = exceptionDiagnosis.hashCode();
        
        assertNotEquals(0, hashCode);
    }

    // Test case for toString() method
    @Test
    public void testToString() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        
        String diagnosisString = exceptionDiagnosis.toString();
        
        assertNotNull(diagnosisString);
        assertTrue(diagnosisString.contains("ExceptionDiagnosis"));
    }
}
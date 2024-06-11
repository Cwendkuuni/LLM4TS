
import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {
    @Test
    public void testConstructorWithThrowable() {
        Throwable th = new RuntimeException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("", diagnosis.asHexString());
    }
    
    @Test
    public void testConstructorWithThrowableAndInclusionFilter() {
        Throwable th = new RuntimeException("Test Exception");
        String[] inclusionFilter = {"filter1", "filter2"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("", diagnosis.asHexString());
    }
    
    @Test
    public void testGetStackLineHash() {
        Throwable th = new RuntimeException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        assertEquals(0, diagnosis.getStackLineHash());
    }
    
    @Test
    public void testGetLineNumberHash() {
        Throwable th = new RuntimeException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        assertEquals(0, diagnosis.getLineNumberHash());
    }
    
    @Test
    public void testAsHexString() {
        Throwable th = new RuntimeException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        assertEquals("", diagnosis.asHexString());
    }
    
    @Test
    public void testEquals() {
        Throwable th1 = new RuntimeException("Test Exception 1");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th1);
        
        Throwable th2 = new RuntimeException("Test Exception 2");
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th2);
        
        assertTrue(diagnosis1.equals(diagnosis1));
        assertFalse(diagnosis1.equals(null));
        assertFalse(diagnosis1.equals(diagnosis2));
    }
    
    @Test
    public void testHashCode() {
        Throwable th = new RuntimeException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        int expectedHashCode = 31 * diagnosis.getStackLineHash() + diagnosis.getLineNumberHash();
        
        assertEquals(expectedHashCode, diagnosis.hashCode());
    }
    
    @Test
    public void testToString() {
        Throwable th = new RuntimeException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        String expectedString = "ExceptionDiagnosis{stackLineHash=" + diagnosis.getStackLineHash() 
            + ", lineNumberHash=" + diagnosis.getLineNumberHash() + '}';
        
        assertEquals(expectedString, diagnosis.toString());
    }
}

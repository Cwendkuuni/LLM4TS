
import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {
    
    @Test
    public void testConstructor1() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        assertNotNull(diagnosis);
    }
        
    @Test
    public void testConstructor2() {
        Throwable th = new Throwable("Test Exception");
        String[] inclusionFilter = {"twitter4j"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        
        assertNotNull(diagnosis);
    }
    
    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        int stackLineHash = diagnosis.getStackLineHash();
        
        assertEquals(0, stackLineHash);
    }
    
    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        int lineNumberHash = diagnosis.getLineNumberHash();
        
        assertEquals(0, lineNumberHash);
    }
    
    @Test
    public void testAsHexString() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        String hexString = diagnosis.asHexString();
        
        assertEquals("", hexString);
    }
    
    @Test
    public void testEquals() {
        Throwable th1 = new Throwable("Test Exception 1");
        Throwable th2 = new Throwable("Test Exception 2");
        
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th2);
        
        assertFalse(diagnosis1.equals(diagnosis2));
    }
    
    @Test
    public void testHashCode() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        int hashCode = diagnosis.hashCode();
        
        assertEquals(0, hashCode);
    }
    
    @Test
    public void testToString() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        String toString = diagnosis.toString();
        
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", toString);
    }
}

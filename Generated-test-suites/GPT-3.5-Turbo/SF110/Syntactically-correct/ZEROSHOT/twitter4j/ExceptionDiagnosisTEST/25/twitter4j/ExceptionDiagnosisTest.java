import org.junit.*;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    // Test case for constructor with throwable only
    @Test
    public void testConstructor1() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("", diagnosis.asHexString());
    }
    
    // Test case for constructor with throwable and inclusion filter
    @Test
    public void testConstructor2() {
        Throwable th = new Throwable();
        String[] filter = {"java."};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, filter);
        
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("", diagnosis.asHexString());
    }
    
    // Test case for getStackLineHash method
    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        assertEquals(0, diagnosis.getStackLineHash());
    }
    
    // Test case for getLineNumberHash method
    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        assertEquals(0, diagnosis.getLineNumberHash());
    }
    
    // Test case for asHexString method
    @Test
    public void testAsHexString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        assertEquals("", diagnosis.asHexString());
    }
    
    // Test case for equals method
    @Test
    public void testEquals() {
        Throwable th1 = new Throwable();
        Throwable th2 = new Throwable();
        
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th2);
        
        assertTrue(diagnosis1.equals(diagnosis1));
        assertFalse(diagnosis1.equals(diagnosis2));
    }
    
    // Test case for hashCode method
    @Test
    public void testHashCode() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        int expectedHashCode = 31 * diagnosis.getStackLineHash() + diagnosis.getLineNumberHash();
        
        assertEquals(expectedHashCode, diagnosis.hashCode());
    }
    
    // Test case for toString method
    @Test
    public void testToString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        String expectedString = "ExceptionDiagnosis{stackLineHash=" + diagnosis.getStackLineHash() + ", lineNumberHash=" + diagnosis.getLineNumberHash() + "}";
        
        assertEquals(expectedString, diagnosis.toString());
    }
}
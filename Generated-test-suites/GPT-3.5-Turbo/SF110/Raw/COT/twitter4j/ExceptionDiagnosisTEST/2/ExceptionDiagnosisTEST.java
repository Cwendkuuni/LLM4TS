import static org.junit.Assert.*;
import org.junit.Test;

public class ExceptionDiagnosisTest {
    
    // Test the constructor ExceptionDiagnosis(Throwable th)
    @Test
    public void testConstructor1() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals("", diagnosis.asHexString());
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
    }
    
    // Test the constructor ExceptionDiagnosis(Throwable th, String[] inclusionFilter)
    @Test
    public void testConstructor2() {
        String[] inclusionFilter = {"twitter4j."};
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertEquals("", diagnosis.asHexString());
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
    }
    
    // Test the method asHexString()
    @Test
    public void testAsHexString() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals("", diagnosis.asHexString());
    }
    
    // Test the method toHexString(int value)
    @Test
    public void testToHexString() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals("00000000", diagnosis.toHexString(0));
        assertEquals("00000001", diagnosis.toHexString(1));
        assertEquals("0000000A", diagnosis.toHexString(10));
        assertEquals("00000064", diagnosis.toHexString(100));
        assertEquals("7FFFFFFF", diagnosis.toHexString(Integer.MAX_VALUE));
    }
    
    // Test the method equals(Object o)
    @Test
    public void testEquals() {
        Throwable th1 = new Throwable("Test Exception");
        Throwable th2 = new Throwable("Another Exception");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th1);
        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(th2);
        
        assertTrue(diagnosis1.equals(diagnosis2));
        assertFalse(diagnosis1.equals(diagnosis3));
    }
    
    // Test the method hashCode()
    @Test
    public void testHashCode() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        assertEquals(0, diagnosis.hashCode());
    }
    
    // Test the method toString()
    @Test
    public void testToString() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", diagnosis.toString());
    }
}
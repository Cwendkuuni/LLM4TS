import static org.junit.Assert.*;
import org.junit.Test;

public class ExceptionDiagnosisTest {

    // Test case for ExceptionDiagnosis(Throwable th)
    @Test
    public void testExceptionDiagnosis1() {
        Throwable th = new Throwable("Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals("", diagnosis.asHexString());
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
    }
  
    // Test case for ExceptionDiagnosis(Throwable th, String[] inclusionFilter)
    @Test
    public void testExceptionDiagnosis2() {
        Throwable th = new Throwable("Exception");
        String[] inclusionFilter = {"twitter4j"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertEquals("", diagnosis.asHexString());
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
    }
  
    // Test case for getStackLineHash()
    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable("Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, diagnosis.getStackLineHash());
    }
  
    // Test case for getLineNumberHash()
    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable("Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, diagnosis.getLineNumberHash());
    }
  
    // Test case for asHexString()
    @Test
    public void testAsHexString() {
        Throwable th = new Throwable("Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals("", diagnosis.asHexString());
    }
  
    // Test case for equals(Object o)
    @Test
    public void testEquals() {
        Throwable th1 = new Throwable("Exception");
        Throwable th2 = new Throwable("Exception");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th2);
        assertEquals(diagnosis1, diagnosis1);
        assertEquals(diagnosis1, diagnosis2);
        assertEquals(diagnosis2, diagnosis1);
        assertEquals(diagnosis2, diagnosis2);
    }
  
    // Test case for hashCode()
    @Test
    public void testHashCode() {
        Throwable th = new Throwable("Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertNotNull(diagnosis.hashCode());
    }
  
    // Test case for toString()
    @Test
    public void testToString() {
        Throwable th = new Throwable("Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertNotNull(diagnosis.toString());
    }

}
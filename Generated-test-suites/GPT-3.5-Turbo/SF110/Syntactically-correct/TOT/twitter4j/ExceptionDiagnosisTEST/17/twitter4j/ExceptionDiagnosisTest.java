import org.junit.*;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructorThrowable() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("00000000-00000000", diagnosis.asHexString());
    }
    
    @Test
    public void testConstructorThrowableWithFilters() {
        Throwable th = new Throwable("Test Exception");
        String[] filters = {"twitter4j.ExceptionDiagnosis"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, filters);
        assertNotEquals(0, diagnosis.getStackLineHash());
        assertNotEquals(0, diagnosis.getLineNumberHash());
        assertNotEquals("00000000-00000000", diagnosis.asHexString());
    }
    
    @Test
    public void testConstructorThrowableWithFiltersNoMatch() {
        Throwable th = new Throwable("Test Exception");
        String[] filters = {"notmatching.Exception"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, filters);
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("00000000-00000000", diagnosis.asHexString());
    }
    
    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, diagnosis.getStackLineHash());
    }
    
    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, diagnosis.getLineNumberHash());
    }
    
    @Test
    public void testAsHexString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals("00000000-00000000", diagnosis.asHexString());
    }
    
    @Test
    public void testEquals() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th);
        assertTrue(diagnosis1.equals(diagnosis2));
    }
    
    @Test
    public void testNotEquals() {
        Throwable th1 = new Throwable();
        Throwable th2 = new Throwable();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th2);
        assertFalse(diagnosis1.equals(diagnosis2));
    }
    
    @Test
    public void testHashCode() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertNotNull(diagnosis.hashCode());
    }
    
    @Test
    public void testToString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertNotNull(diagnosis.toString());
    }
}
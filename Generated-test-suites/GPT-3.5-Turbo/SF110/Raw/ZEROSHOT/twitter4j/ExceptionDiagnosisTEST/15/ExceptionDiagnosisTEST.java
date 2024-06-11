import org.junit.*;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructorWithThrowable() {
        Throwable th = new Throwable();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        assertNotNull(exceptionDiagnosis);
        assertEquals("", exceptionDiagnosis.asHexString());
    }

    @Test
    public void testConstructorWithThrowableAndInclusionFilter() {
        Throwable th = new Throwable();
        String[] inclusionFilter = new String[]{"filter1", "filter2"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertNotNull(exceptionDiagnosis);
        assertEquals("", exceptionDiagnosis.asHexString());
    }

    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
    }

    @Test
    public void testAsHexString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        assertEquals("", exceptionDiagnosis.asHexString());
    }

    @Test
    public void testEquals() {
        Throwable th1 = new Throwable();
        Throwable th2 = new Throwable();
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(th1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(th1);
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(th2);
        
        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis2));
        assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis3));
    }

    @Test
    public void testHashCode() {
        Throwable th = new Throwable();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        assertNotNull(exceptionDiagnosis.hashCode());
    }

    @Test
    public void testToString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        assertNotNull(exceptionDiagnosis.toString());
    }
}
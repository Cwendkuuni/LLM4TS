package twitter4j;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testGetStackLineHash() {
        // Test for typical case
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, diagnosis.getStackLineHash());
    }

    @Test
    public void testGetLineNumberHash() {
        // Test for typical case
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, diagnosis.getLineNumberHash());
    }

    @Test
    public void testAsStringHexString() {
        // Test for typical case
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals("", diagnosis.asHexString());
    }

    @Test
    public void testEquals() {
        // Test for typical case
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th);
        assertTrue(diagnosis1.equals(diagnosis2));

        // Test for different objects
        assertFalse(diagnosis1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        // Test for typical case
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, diagnosis.hashCode());
    }
}
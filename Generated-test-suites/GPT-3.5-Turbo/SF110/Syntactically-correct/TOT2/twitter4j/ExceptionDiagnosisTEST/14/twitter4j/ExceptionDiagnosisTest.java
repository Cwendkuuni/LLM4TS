package twitter4j;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testGetStackLineHash() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new NullPointerException());
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
    }

    @Test
    public void testGetLineNumberHash() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new NullPointerException());
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
    }

    @Test
    public void testAsStringHexString() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new NullPointerException());
        assertEquals("", exceptionDiagnosis.asHexString());
    }

    @Test
    public void testEquals() {
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new NullPointerException());
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new NullPointerException());
        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis2));
    }

    @Test
    public void testHashCode() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new NullPointerException());
        assertEquals(0, exceptionDiagnosis.hashCode());
    }
}
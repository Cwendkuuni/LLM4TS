package twitter4j;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testGetStackLineHash() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new Exception());

        int stackLineHash = exceptionDiagnosis.getStackLineHash();

        assertEquals(0, stackLineHash);
    }

    @Test
    public void testGetLineNumberHash() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new Exception());

        int lineNumberHash = exceptionDiagnosis.getLineNumberHash();

        assertEquals(0, lineNumberHash);
    }

    @Test
    public void testAsHexString() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new Exception());

        String hexString = exceptionDiagnosis.asHexString();

        assertEquals("", hexString);
    }

    @Test
    public void testEquals() {
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new Exception());
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new Exception());

        boolean isEqual = exceptionDiagnosis1.equals(exceptionDiagnosis2);

        assertTrue(isEqual);
    }

    @Test
    public void testHashCode() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new Exception());

        int hashCode = exceptionDiagnosis.hashCode();

        assertEquals(0, hashCode);
    }

    @Test
    public void testToString() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new Exception());

        String toString = exceptionDiagnosis.toString();

        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", toString);
    }
}
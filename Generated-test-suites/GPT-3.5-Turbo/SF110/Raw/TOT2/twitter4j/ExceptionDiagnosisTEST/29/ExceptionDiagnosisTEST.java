
import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructor() {
        Throwable throwable = new Throwable("Testing Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        
        assertNotNull(exceptionDiagnosis);
        assertEquals(throwable, exceptionDiagnosis.th);
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new Throwable("Testing Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        int stackLineHash = exceptionDiagnosis.getStackLineHash();

        assertEquals(0, stackLineHash);
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new Throwable("Testing Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        int lineNumberHash = exceptionDiagnosis.getLineNumberHash();

        assertEquals(0, lineNumberHash);
    }

    @Test
    public void testAsStringHexString() {
        Throwable throwable = new Throwable("Testing Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        String hexString = exceptionDiagnosis.asHexString();

        assertNotNull(hexString);
    }

    @Test
    public void testEquals() {
        Throwable throwable = new Throwable("Testing Exception");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable);

        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis2));
        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis1));
        assertFalse(exceptionDiagnosis1.equals(null));
    }

    @Test
    public void testHashCode() {
        Throwable throwable = new Throwable("Testing Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        int hashCode = exceptionDiagnosis.hashCode();

        assertNotNull(hashCode);
    }

    @Test
    public void testToString() {
        Throwable throwable = new Throwable("Testing Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        String toString = exceptionDiagnosis.toString();

        assertNotNull(toString);
    }
}

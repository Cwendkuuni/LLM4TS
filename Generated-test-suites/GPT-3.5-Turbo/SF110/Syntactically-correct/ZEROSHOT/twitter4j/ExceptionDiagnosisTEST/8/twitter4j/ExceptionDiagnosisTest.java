import org.junit.*;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructor() {
        Throwable th = new Throwable();
        String[] inclusionFilter = new String[]{"test."};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);

        assertNotNull(exceptionDiagnosis);
    }

    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable();
        String[] inclusionFilter = new String[]{"test."};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);

        int stackLineHash = exceptionDiagnosis.getStackLineHash();

        assertEquals(0, stackLineHash);
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable();
        String[] inclusionFilter = new String[]{"test."};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);

        int lineNumberHash = exceptionDiagnosis.getLineNumberHash();

        assertEquals(0, lineNumberHash);
    }

    @Test
    public void testAsHexString() {
        Throwable th = new Throwable();
        String[] inclusionFilter = new String[]{"test."};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);

        String hexString = exceptionDiagnosis.asHexString();

        assertEquals("", hexString);
    }

    @Test
    public void testEquals() {
        Throwable th = new Throwable();
        String[] inclusionFilter = new String[]{"test."};
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(th, inclusionFilter);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(th, inclusionFilter);

        boolean equal = exceptionDiagnosis1.equals(exceptionDiagnosis2);

        assertTrue(equal);
    }

    @Test
    public void testHashCode() {
        Throwable th = new Throwable();
        String[] inclusionFilter = new String[]{"test."};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);

        int hashCode = exceptionDiagnosis.hashCode();

        assertEquals(0, hashCode);
    }

    @Test
    public void testToString() {
        Throwable th = new Throwable();
        String[] inclusionFilter = new String[]{"test."};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);

        String toString = exceptionDiagnosis.toString();

        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", toString);
    }
}
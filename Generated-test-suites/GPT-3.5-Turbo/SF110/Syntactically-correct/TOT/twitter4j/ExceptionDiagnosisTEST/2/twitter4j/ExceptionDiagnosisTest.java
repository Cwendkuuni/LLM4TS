
import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    // Test case for constructor ExceptionDiagnosis(Throwable th)
    @Test
    public void testConstructor1() {
        // Typical use case
        Throwable th = new Throwable("Test");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        assertNotNull(exceptionDiagnosis);
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
    }

    // Test case for constructor ExceptionDiagnosis(Throwable th, String[] inclusionFilter)
    @Test
    public void testConstructor2() {
        // Typical use case
        Throwable th = new Throwable("Test");
        String[] inclusionFilter = {"twitter4j."};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertNotNull(exceptionDiagnosis);
        // Calculate expected stack line hash and line number hash for inclusion filter
        int expectedStackLineHash = ("twitter4j.ExceptionDiagnosis".hashCode() +
                "ExceptionDiagnosis.testConstructor2".hashCode());
        int expectedLineNumberHash = 28;
        assertEquals(expectedStackLineHash, exceptionDiagnosis.getStackLineHash());
        assertEquals(expectedLineNumberHash, exceptionDiagnosis.getLineNumberHash());
    }

    // Test case for asHexString()
    @Test
    public void testAsHexString() {
        // Edge case: Empty ExceptionDiagnosis object
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(null);
        assertEquals("00000000-00000000", exceptionDiagnosis.asHexString());

        // Typical use case
        Throwable th = new Throwable("Test");
        String[] inclusionFilter = {"twitter4j."};
        exceptionDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        // Calculate expected stack line hash and line number hash for inclusion filter
        int expectedStackLineHash = ("twitter4j.ExceptionDiagnosis".hashCode() +
                "ExceptionDiagnosis.testAsHexString".hashCode());
        int expectedLineNumberHash = 37;
        assertEquals(expectedStackLineHash + "-" + expectedLineNumberHash, exceptionDiagnosis.asHexString());

        // Error scenario: Recursive ExceptionDiagnosis object
        exceptionDiagnosis = new ExceptionDiagnosis(th);
        ExceptionDiagnosis recursiveExceptionDiagnosis = new ExceptionDiagnosis(exceptionDiagnosis.getThrowable());
        assertEquals(expectedStackLineHash + "-" + expectedLineNumberHash + " " + exceptionDiagnosis.asHexString(),
                recursiveExceptionDiagnosis.asHexString());
    }

    // Test case for equals(Object o)
    @Test
    public void testEquals() {
        // Typical use case: Object is equal
        Throwable th = new Throwable("Test");
        String[] inclusionFilter = {"twitter4j."};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(th, inclusionFilter);
        assertTrue(exceptionDiagnosis.equals(exceptionDiagnosis2));

        // Typical use case: Object is not equal
        Throwable th2 = new Throwable("Test 2");
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(th2, inclusionFilter);
        assertFalse(exceptionDiagnosis.equals(exceptionDiagnosis3));

        // Edge case: Object is null
        assertFalse(exceptionDiagnosis.equals(null));

        // Edge case: Object is of different class
        Object object = new Object();
        assertFalse(exceptionDiagnosis.equals(object));
    }

    // Test case for hashCode()
    @Test
    public void testHashCode() {
        // Typical use case
        Throwable th = new Throwable("Test");
        String[] inclusionFilter = {"twitter4j."};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        // Calculate expected hash code
        int expectedHashCode = 31 * (expectedStackLineHash) + expectedLineNumberHash;
        assertEquals(expectedHashCode, exceptionDiagnosis.hashCode());
    }

    // Test case for toString()
    @Test
    public void testToString() {
        // Typical use case
        Throwable th = new Throwable("Test");
        String[] inclusionFilter = {"twitter4j."};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertEquals("ExceptionDiagnosis{stackLineHash=" + expectedStackLineHash +
                ", lineNumberHash=" + expectedLineNumberHash + '}', exceptionDiagnosis.toString());
    }
}


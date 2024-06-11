import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExceptionDiagnosisTest {

    @Test
    void testConstructorWithoutFilter() {
        Throwable throwable = new Throwable();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertNotNull(exceptionDiagnosis);
        assertEquals("", exceptionDiagnosis.asHexString());
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
    }

    @Test
    void testConstructorWithEmptyFilter() {
        String[] filter = new String[0];
        Throwable throwable = new Throwable();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, filter);
        assertNotNull(exceptionDiagnosis);
        assertEquals("", exceptionDiagnosis.asHexString());
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
    }

    @Test
    void testConstructorWithFilter() {
        String[] filter = {"twitter4j."};
        Throwable throwable = new Throwable();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, filter);
        assertNotNull(exceptionDiagnosis);
        assertEquals("", exceptionDiagnosis.asHexString());
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
    }

    @Test
    void testGetStackLineHash() {
        Throwable throwable = new Throwable();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int stackLineHash = exceptionDiagnosis.getStackLineHash();
        assertEquals(0, stackLineHash);
    }

    @Test
    void testGetLineNumberHash() {
        Throwable throwable = new Throwable();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int lineNumberHash = exceptionDiagnosis.getLineNumberHash();
        assertEquals(0, lineNumberHash);
    }
    
    @Test
    void testEquals() {
        Throwable throwable = new Throwable();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable);
        assertTrue(diagnosis1.equals(diagnosis2));
    }

    @Test
    void testHashCode() {
        Throwable throwable = new Throwable();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertEquals(0, exceptionDiagnosis.hashCode());
    }

    @Test
    void testToString() {
        Throwable throwable = new Throwable();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        String expectedString = "ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}";
        assertEquals(expectedString, exceptionDiagnosis.toString());
    }
}
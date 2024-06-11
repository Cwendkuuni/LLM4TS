import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    private ExceptionDiagnosis exceptionDiagnosis;

    @Before
    public void setUp() {
        exceptionDiagnosis = new ExceptionDiagnosis(new Throwable());
    }

    @Test
    public void testGetStackLineHash() {
        int expected = 0;
        int actual = exceptionDiagnosis.getStackLineHash();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLineNumberHash() {
        int expected = 0;
        int actual = exceptionDiagnosis.getLineNumberHash();
        assertEquals(expected, actual);
    }

    @Test
    public void testAsHexString() {
        String expected = "";
        String actual = exceptionDiagnosis.asHexString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        ExceptionDiagnosis sameException = new ExceptionDiagnosis(new Throwable());
        assertTrue(exceptionDiagnosis.equals(exceptionDiagnosis));
        assertFalse(exceptionDiagnosis.equals("This is a string"));
        assertTrue(exceptionDiagnosis.equals(sameException));
    }

    @Test
    public void testHashCode() {
        int expected = exceptionDiagnosis.getStackLineHash() * 31 + exceptionDiagnosis.getLineNumberHash();
        int actual = exceptionDiagnosis.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String expected = "ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}";
        String actual = exceptionDiagnosis.toString();
        assertEquals(expected, actual);
    }
}
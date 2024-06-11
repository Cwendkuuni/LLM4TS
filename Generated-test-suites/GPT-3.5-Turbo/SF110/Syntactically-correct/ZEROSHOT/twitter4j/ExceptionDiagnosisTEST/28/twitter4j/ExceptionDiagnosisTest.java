import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructor1() {
        Throwable exception = new Exception("Test Exception");
        ExceptionDiagnosis diag = new ExceptionDiagnosis(exception);

        assertEquals(0, diag.getStackLineHash());
        assertEquals(0, diag.getLineNumberHash());
    }

    @Test
    public void testConstructor2() {
        Throwable exception = new Exception("Test Exception");
        String[] inclusionFilter = new String[]{"twitter4j."};
        ExceptionDiagnosis diag = new ExceptionDiagnosis(exception, inclusionFilter);

        assertEquals(0, diag.getStackLineHash());
        assertEquals(0, diag.getLineNumberHash());
    }

    @Test
    public void testGetStackLineHash() {
        Throwable exception = new Exception("Test Exception");
        ExceptionDiagnosis diag = new ExceptionDiagnosis(exception);

        int expected = diag.getStackLineHash();
        int actual = diag.getStackLineHash();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable exception = new Exception("Test Exception");
        ExceptionDiagnosis diag = new ExceptionDiagnosis(exception);

        int expected = diag.getLineNumberHash();
        int actual = diag.getLineNumberHash();

        assertEquals(expected, actual);
    }

    @Test
    public void testAsHexString() {
        Throwable exception = new Exception("Test Exception");
        ExceptionDiagnosis diag = new ExceptionDiagnosis(exception);

        String expected = diag.asHexString();
        String actual = diag.asHexString();

        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Throwable exception1 = new Exception("Test Exception");
        Throwable exception2 = new Exception("Test Exception");
        ExceptionDiagnosis diag1 = new ExceptionDiagnosis(exception1);
        ExceptionDiagnosis diag2 = new ExceptionDiagnosis(exception2);

        boolean expected = diag1.equals(diag2);
        boolean actual = diag2.equals(diag1);

        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        Throwable exception = new Exception("Test Exception");
        ExceptionDiagnosis diag = new ExceptionDiagnosis(exception);

        int expected = diag.hashCode();
        int actual = diag.hashCode();

        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        Throwable exception = new Exception("Test Exception");
        ExceptionDiagnosis diag = new ExceptionDiagnosis(exception);

        String expected = diag.toString();
        String actual = diag.toString();

        assertEquals(expected, actual);
    }
}
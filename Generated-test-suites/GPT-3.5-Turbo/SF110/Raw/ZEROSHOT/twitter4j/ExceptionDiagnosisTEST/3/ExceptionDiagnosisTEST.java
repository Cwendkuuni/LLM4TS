import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        int expected = 0;
        int actual = diagnosis.getStackLineHash();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        int expected = 0;
        int actual = diagnosis.getLineNumberHash();
        assertEquals(expected, actual);
    }

    @Test
    public void testAsHexString() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        String expected = "00000000";
        String actual = diagnosis.asHexString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Throwable th1 = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th1);

        Throwable th2 = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th2);

        assertEquals(diagnosis1, diagnosis2);
    }

    @Test
    public void testHashCode() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        int expected = 0;
        int actual = diagnosis.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        String expected = "ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}";
        String actual = diagnosis.toString();
        assertEquals(expected, actual);
    }
}
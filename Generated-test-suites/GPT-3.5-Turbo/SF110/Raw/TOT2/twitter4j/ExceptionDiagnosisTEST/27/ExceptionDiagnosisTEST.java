import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructorWithThrowable() {
        Throwable exception = new NullPointerException();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(exception);
        assertNotNull(diagnosis);
    }

    @Test
    public void testConstructorWithThrowableAndFilter() {
        Throwable exception = new RuntimeException();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(exception, new String[]{"twitter4j"});
        assertNotNull(diagnosis);
    }

    @Test
    public void testGetStackLineHash() {
        Throwable exception = new RuntimeException();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(exception);
        int stackLineHash = diagnosis.getStackLineHash();
        assertEquals(0, stackLineHash);
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable exception = new RuntimeException();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(exception);
        int lineNumberHash = diagnosis.getLineNumberHash();
        assertEquals(0, lineNumberHash);
    }

    @Test
    public void testAsStringHexString() {
        Throwable exception = new RuntimeException();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(exception);
        String hexString = diagnosis.asHexString();
        assertEquals("00000000-00000000", hexString);
    }

    @Test
    public void testEquals() {
        Throwable exception = new NullPointerException();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(exception);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(exception);
        assertEquals(diagnosis1, diagnosis2);
    }

    @Test
    public void testHashCode() {
        Throwable exception = new NullPointerException();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(exception);
        assertEquals(0, diagnosis.hashCode());
    }

    @Test
    public void testToString() {
        Throwable exception = new NullPointerException();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(exception);
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", diagnosis.toString());
    }
}
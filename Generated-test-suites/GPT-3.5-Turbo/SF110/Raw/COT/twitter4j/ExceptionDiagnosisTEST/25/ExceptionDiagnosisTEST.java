
import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructorWithThrowable() {
        Throwable throwable = new Throwable("Test");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);

        assertEquals(throwable, diagnosis.th);
        assertEquals("", diagnosis.hexString);
        assertEquals(0, diagnosis.stackLineHash);
        assertEquals(0, diagnosis.lineNumberHash);
    }

    @Test
    public void testConstructorWithThrowableAndFilters() {
        Throwable throwable = new Throwable("Test");
        String[] filters = {"twitter4j"};

        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, filters);

        assertEquals(throwable, diagnosis.th);
        assertEquals("", diagnosis.hexString);
        assertEquals(0, diagnosis.stackLineHash);
        assertEquals(0, diagnosis.lineNumberHash);
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new Throwable("Test");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);

        int stackLineHash = diagnosis.getStackLineHash();
        assertEquals(0, stackLineHash);
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new Throwable("Test");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);

        int lineNumberHash = diagnosis.getLineNumberHash();
        assertEquals(0, lineNumberHash);
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new Throwable("Test");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);

        String hexString = diagnosis.asHexString();
        assertEquals("", hexString);
    }

    @Test
    public void testEquals() {
        Throwable throwable = new Throwable("Test");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable);

        assertTrue(diagnosis1.equals(diagnosis2));
    }

    @Test
    public void testHashCode() {
        Throwable throwable = new Throwable("Test");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);

        int hashCode = diagnosis.hashCode();
        assertEquals(0, hashCode);
    }

    @Test
    public void testToString() {
        Throwable throwable = new Throwable("Test");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);

        String diagnosisString = diagnosis.toString();
        assertTrue(diagnosisString.contains("stackLineHash=0"));
        assertTrue(diagnosisString.contains("lineNumberHash=0"));
    }
}

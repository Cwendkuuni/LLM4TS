import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructor1() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);

        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("", diagnosis.asHexString());
    }

    @Test
    public void testConstructor2() {
        Throwable th = new Throwable("Test Exception");
        String[] inclusionFilter = {"twitter4j."};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);

        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("", diagnosis.asHexString());
    }

    @Test
    public void testEquals() {
        Throwable th1 = new Throwable("Test Exception");
        Throwable th2 = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th2);

        assertTrue(diagnosis1.equals(diagnosis2));
    }

    @Test
    public void testHashCode() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);

        int expectedHashCode = diagnosis.getStackLineHash() * 31 + diagnosis.getLineNumberHash();
        assertEquals(expectedHashCode, diagnosis.hashCode());
    }

    @Test
    public void testToString() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);

        String expectedString = "ExceptionDiagnosis{stackLineHash=" + diagnosis.getStackLineHash() + ", lineNumberHash=" + diagnosis.getLineNumberHash() + '}';
        assertEquals(expectedString, diagnosis.toString());
    }
}
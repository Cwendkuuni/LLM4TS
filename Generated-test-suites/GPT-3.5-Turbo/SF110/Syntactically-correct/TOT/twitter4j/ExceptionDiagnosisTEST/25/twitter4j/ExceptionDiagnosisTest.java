import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructorWithThrowable() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);

        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("00000000-00000000", diagnosis.asHexString());
    }

    @Test
    public void testConstructorWithThrowableAndInclusionFilter() {
        String[] inclusionFilter = {"twitter4j."};
        Throwable th = new IllegalArgumentException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);

        assertNotEquals(0, diagnosis.getStackLineHash());
        assertNotEquals(0, diagnosis.getLineNumberHash());
        assertNotEquals("00000000-00000000", diagnosis.asHexString());

        Throwable cause = new NullPointerException("Test Cause");
        Throwable th2 = new IllegalArgumentException("Test Exception", cause);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th2, inclusionFilter);

        assertNotEquals(diagnosis.asHexString(), diagnosis2.asHexString());
    }

    @Test
    public void testEqualsMethod() {
        String[] inclusionFilter = {"twitter4j."};
        Throwable th = new IllegalArgumentException("Test Exception");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th, inclusionFilter);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th, inclusionFilter);

        assertTrue(diagnosis1.equals(diagnosis2));
    }

    @Test
    public void testHashCodeMethod() {
        String[] inclusionFilter = {"twitter4j."};
        Throwable th = new IllegalArgumentException("Test Exception");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th, inclusionFilter);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th, inclusionFilter);

        assertEquals(diagnosis1.hashCode(), diagnosis2.hashCode());
    }

    @Test
    public void testToStringMethod() {
        String[] inclusionFilter = {"twitter4j."};
        Throwable th = new IllegalArgumentException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);

        String expected = "ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}";
        assertEquals(expected, diagnosis.toString());
    }

}
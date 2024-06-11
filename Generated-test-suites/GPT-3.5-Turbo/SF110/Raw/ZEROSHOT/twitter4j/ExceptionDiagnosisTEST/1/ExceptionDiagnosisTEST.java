import org.junit.Test;

import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructor() {
        Throwable th = new Throwable();

        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);

        assertNotNull(diagnosis);
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("", diagnosis.asHexString());
    }

    @Test
    public void testConstructorWithInclusionFilter() {
        Throwable th = new Throwable();
        String[] inclusionFilter = {"twitter4j."};

        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);

        assertNotNull(diagnosis);
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("", diagnosis.asHexString());
    }

    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable();

        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);

        assertEquals(0, diagnosis.getStackLineHash());
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable();

        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);

        assertEquals(0, diagnosis.getLineNumberHash());
    }

    @Test
    public void testAsHexString() {
        Throwable th = new Throwable();

        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);

        assertEquals("", diagnosis.asHexString());
    }

    @Test
    public void testEquals() {
        Throwable th1 = new Throwable();
        Throwable th2 = new Throwable();

        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th1);
        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(th2);

        assertTrue(diagnosis1.equals(diagnosis2));
        assertFalse(diagnosis1.equals(diagnosis3));
    }

    @Test
    public void testHashCode() {
        Throwable th = new Throwable();

        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);

        int expectedHashCode = 31 * 0 + 0;
        assertEquals(expectedHashCode, diagnosis.hashCode());
    }

    @Test
    public void testToString() {
        Throwable th = new Throwable();

        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);

        String expectedString = "ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}";
        assertEquals(expectedString, diagnosis.toString());
    }
}
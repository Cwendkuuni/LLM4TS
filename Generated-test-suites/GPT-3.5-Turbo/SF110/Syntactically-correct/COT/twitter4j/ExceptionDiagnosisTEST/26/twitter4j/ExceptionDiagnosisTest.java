
import org.junit.Test;

import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosis() {
        Throwable th = new RuntimeException("Test exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);

        assertNotNull(diagnosis);
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("", diagnosis.asHexString());
    }

    @Test
    public void testExceptionDiagnosisWithInclusionFilter() {
        Throwable th = new RuntimeException("Test exception");
        String[] inclusionFilter = {"twitter4j"};

        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);

        assertNotNull(diagnosis);
        assertNotEquals(0, diagnosis.getStackLineHash());
        assertNotEquals(0, diagnosis.getLineNumberHash());
        assertNotEquals("", diagnosis.asHexString());
    }

    @Test
    public void testEquals() {
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(new RuntimeException("Test exception"));
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(new RuntimeException("Test exception"));
        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(new IllegalArgumentException("Test exception"));

        assertTrue(diagnosis1.equals(diagnosis2));
        assertFalse(diagnosis1.equals(diagnosis3));
    }

    @Test
    public void testHashCode() {
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(new RuntimeException("Test exception"));
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(new RuntimeException("Test exception"));

        assertEquals(diagnosis1.hashCode(), diagnosis2.hashCode());
    }

    @Test
    public void testToString() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new RuntimeException("Test exception"));

        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", diagnosis.toString());
    }
}

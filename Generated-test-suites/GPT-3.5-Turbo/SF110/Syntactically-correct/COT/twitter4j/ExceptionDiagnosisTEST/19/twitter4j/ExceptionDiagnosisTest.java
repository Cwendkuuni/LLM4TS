import org.junit.Test;

import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructorWithThrowable() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new RuntimeException());
        assertNotNull(diagnosis);
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("", diagnosis.asHexString());
    }

    @Test
    public void testConstructorWithThrowableAndInclusionFilter() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new RuntimeException(), new String[]{"twitter4j"});
        assertNotNull(diagnosis);
        // TODO: Add assertions for stackLineHash, lineNumberHash, and hexString
    }

    @Test
    public void testEquals() {
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(new RuntimeException());
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(new RuntimeException());
        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(new IllegalArgumentException());
        assertTrue(diagnosis1.equals(diagnosis1));
        assertTrue(diagnosis1.equals(diagnosis2));
        assertFalse(diagnosis1.equals(diagnosis3));
    }

    @Test
    public void testHashCode() {
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(new RuntimeException());
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(new RuntimeException());
        assertEquals(diagnosis1.hashCode(), diagnosis2.hashCode());
    }

    @Test
    public void testToString() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new RuntimeException());
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", diagnosis.toString());
    }
}
package twitter4j;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testGetStackLineHash() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new Throwable());
        int expected = 0;
        int actual = diagnosis.getStackLineHash();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLineNumberHash() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new Throwable());
        int expected = 0;
        int actual = diagnosis.getLineNumberHash();
        assertEquals(expected, actual);
    }

    @Test
    public void testAsHexString() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new Throwable());
        String expected = "";
        String actual = diagnosis.asHexString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(new Throwable());
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(new Throwable());
        assertTrue(diagnosis1.equals(diagnosis1));
        assertFalse(diagnosis1.equals(null));
        assertFalse(diagnosis1.equals(new Object()));
        assertTrue(diagnosis1.equals(diagnosis2));
    }

    @Test
    public void testHashCode() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new Throwable());
        int expected = diagnosis.getStackLineHash() * 31 + diagnosis.getLineNumberHash();
        int actual = diagnosis.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new Throwable());
        String expected = "ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}";
        String actual = diagnosis.toString();
        assertEquals(expected, actual);
    }
}
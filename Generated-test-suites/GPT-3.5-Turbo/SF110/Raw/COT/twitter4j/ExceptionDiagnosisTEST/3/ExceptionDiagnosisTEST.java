
import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosis_withThrowable() {
        Throwable th = new Exception("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("", diagnosis.asHexString());
    }

    @Test
    public void testExceptionDiagnosis_withThrowableAndInclusionFilter() {
        Throwable th1 = new Exception("Test Exception");
        Throwable th2 = new Exception("Test Exception", th1);
        String[] inclusionFilter = {"twitter4j."};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th2, inclusionFilter);
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("", diagnosis.asHexString());
    }

    @Test
    public void testToHexString() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new Exception());
        assertEquals("00000000", diagnosis.toHexString(0));
        assertEquals("00000001", diagnosis.toHexString(1));
        assertEquals("0000000a", diagnosis.toHexString(10));
        assertEquals("000003e8", diagnosis.toHexString(1000));
        assertEquals("000186a0", diagnosis.toHexString(100000));
        assertEquals("7fffffff", diagnosis.toHexString(Integer.MAX_VALUE));
        assertEquals("80000000", diagnosis.toHexString(Integer.MIN_VALUE));
    }

    @Test
    public void testEquals() {
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(new Exception());
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(new Exception());
        assertTrue(diagnosis1.equals(diagnosis1));
        assertFalse(diagnosis1.equals(null));
        assertFalse(diagnosis1.equals(new Object()));
        assertTrue(diagnosis1.equals(diagnosis2));
    }

    @Test
    public void testHashCode() {
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(new Exception());
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(new Exception());
        assertEquals(diagnosis1.hashCode(), diagnosis2.hashCode());
    }

    @Test
    public void testToString() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new Exception());
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", diagnosis.toString());
    }
}


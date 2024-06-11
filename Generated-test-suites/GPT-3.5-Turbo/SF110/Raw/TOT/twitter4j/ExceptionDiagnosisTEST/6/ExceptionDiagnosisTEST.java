import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructorWithThrowable() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertNotNull(diagnosis);
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("00000000-00000000", diagnosis.asHexString());
    }

    @Test
    public void testConstructorWithThrowableAndInclusionFilter() {
        Throwable th = new Throwable("Test Exception");
        String[] inclusionFilter = {"java."};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertNotNull(diagnosis);
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("00000000-00000000", diagnosis.asHexString());
    }

    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, diagnosis.getStackLineHash());
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, diagnosis.getLineNumberHash());
    }

    @Test
    public void testAsHexString() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals("00000000-00000000", diagnosis.asHexString());
    }

    @Test
    public void testEquals() {
        Throwable th1 = new Throwable("Test Exception 1");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th1);
        Throwable th2 = new Throwable("Test Exception 2");
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th2);
        assertEquals(diagnosis1, diagnosis1);
        assertNotEquals(diagnosis1, diagnosis2);
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
        String expectedString = "ExceptionDiagnosis{stackLineHash=" + diagnosis.getStackLineHash() + ", lineNumberHash=" + diagnosis.getLineNumberHash() + "}";
        assertEquals(expectedString, diagnosis.toString());
    }

    @Test
    public void testConstructorWithNestedThrowable() {
        Throwable innerTh = new Throwable("Inner Exception");
        Throwable outerTh = new Throwable("Outer Exception", innerTh);
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(outerTh);
        assertNotNull(diagnosis);
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("00000000-00000000", diagnosis.asHexString());
    }
}
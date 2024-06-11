import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructor1() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        assertNotNull(exceptionDiagnosis);
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
        assertEquals("00000000-00000000", exceptionDiagnosis.asHexString());
    }

    @Test
    public void testConstructor2() {
        Throwable th = new Throwable("Test Exception");
        String[] inclusionFilter = {"twitter4j."};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertNotNull(exceptionDiagnosis);
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
        assertEquals("00000000-00000000", exceptionDiagnosis.asHexString());
    }

    @Test
    public void testConstructor2_WithStackTrace() {
        Throwable th = new Throwable("Test Exception");
        th.setStackTrace(new StackTraceElement[]{
                new StackTraceElement("twitter4j.ExceptionDiagnosis", "toHexString", "ExceptionDiagnosis.java", 42),
                new StackTraceElement("twitter4j.ExceptionDiagnosis", "asHexString", "ExceptionDiagnosis.java", 56)
        });
        String[] inclusionFilter = {"twitter4j."};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertNotNull(exceptionDiagnosis);
        assertEquals(1502180892, exceptionDiagnosis.getStackLineHash());
        assertEquals(98, exceptionDiagnosis.getLineNumberHash());
        assertEquals("587d4d64-00000062", exceptionDiagnosis.asHexString());
    }

    @Test
    public void testEquals() {
        Throwable th1 = new Throwable("Test Exception 1");
        Throwable th2 = new Throwable("Test Exception 2");
        Throwable th3 = new Throwable("Test Exception 3");

        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(th1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(th2);
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(th3);

        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis1));
        assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));
        assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis3));
    }

    @Test
    public void testHashCode() {
        Throwable th1 = new Throwable("Test Exception 1");
        Throwable th2 = new Throwable("Test Exception 2");
        Throwable th3 = new Throwable("Test Exception 3");

        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(th1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(th2);
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(th3);

        assertEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis1.hashCode());
        assertNotEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis2.hashCode());
        assertNotEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis3.hashCode());
    }

    @Test
    public void testToString() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", exceptionDiagnosis.toString());
    }
}
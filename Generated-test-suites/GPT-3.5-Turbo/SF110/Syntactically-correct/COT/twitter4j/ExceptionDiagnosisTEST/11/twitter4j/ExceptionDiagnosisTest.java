
import org.junit.*;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructor_th() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis exception = new ExceptionDiagnosis(th);
        assertNotNull(exception);
        assertEquals(0, exception.getStackLineHash());
        assertEquals(0, exception.getLineNumberHash());
        assertEquals("00000000-00000000", exception.asHexString());
    }

    @Test
    public void testExceptionDiagnosisConstructor_th_inclusionFilter() {
        Throwable th = new Throwable("Test Exception");
        String[] inclusionFilter = {"twitter4j."};
        ExceptionDiagnosis exception = new ExceptionDiagnosis(th, inclusionFilter);
        assertNotNull(exception);
        assertEquals(0, exception.getStackLineHash());
        assertEquals(0, exception.getLineNumberHash());
        assertEquals("00000000-00000000", exception.asHexString());
    }

    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis exception = new ExceptionDiagnosis(th);
        exception.stackLineHash = 123;
        assertEquals(123, exception.getStackLineHash());
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis exception = new ExceptionDiagnosis(th);
        exception.lineNumberHash = 456;
        assertEquals(456, exception.getLineNumberHash());
    }

    @Test
    public void testAsHexString() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis exception = new ExceptionDiagnosis(th);
        exception.hexString = "ABCDEF";
        assertEquals("ABCDEF", exception.asHexString());
    }

    @Test
    public void testEquals_sameObject() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis exception = new ExceptionDiagnosis(th);
        assertTrue(exception.equals(exception));
    }

    @Test
    public void testEquals_nullObject() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis exception = new ExceptionDiagnosis(th);
        assertFalse(exception.equals(null));
    }

    @Test
    public void testEquals_differentClass() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis exception = new ExceptionDiagnosis(th);
        Object obj = new Object();
        assertFalse(exception.equals(obj));
    }

    @Test
    public void testEquals_differentLineNumber() {
        Throwable th1 = new Throwable("Test Exception");
        ExceptionDiagnosis exception1 = new ExceptionDiagnosis(th1);
        exception1.lineNumberHash = 123;
        
        Throwable th2 = new Throwable("Test Exception");
        ExceptionDiagnosis exception2 = new ExceptionDiagnosis(th2);
        exception2.lineNumberHash = 456;
        
        assertFalse(exception1.equals(exception2));
    }

    @Test
    public void testEquals_differentStackLine() {
        Throwable th1 = new Throwable("Test Exception");
        ExceptionDiagnosis exception1 = new ExceptionDiagnosis(th1);
        exception1.stackLineHash = 123;
        
        Throwable th2 = new Throwable("Test Exception");
        ExceptionDiagnosis exception2 = new ExceptionDiagnosis(th2);
        exception2.stackLineHash = 456;
        
        assertFalse(exception1.equals(exception2));
    }

    @Test
    public void testEquals_sameLineNumberAndStackLine() {
        Throwable th1 = new Throwable("Test Exception");
        ExceptionDiagnosis exception1 = new ExceptionDiagnosis(th1);
        exception1.lineNumberHash = 123;
        exception1.stackLineHash = 456;
        
        Throwable th2 = new Throwable("Test Exception");
        ExceptionDiagnosis exception2 = new ExceptionDiagnosis(th2);
        exception2.lineNumberHash = 123;
        exception2.stackLineHash = 456;
        
        assertTrue(exception1.equals(exception2));
    }

    @Test
    public void testHashCode() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis exception = new ExceptionDiagnosis(th);
        exception.stackLineHash = 123;
        exception.lineNumberHash = 456;
        assertEquals(15420, exception.hashCode());
    }

    @Test
    public void testToString() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis exception = new ExceptionDiagnosis(th);
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", exception.toString());
    }
}


import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructor() {
        Throwable th = new NullPointerException("Test");
        String[] filter = {"twitter4j."};
        ExceptionDiagnosis ed = new ExceptionDiagnosis(th, filter);
        assertNotNull(ed);
    }
    
    @Test
    public void testGetStackLineHash() {
        Throwable th = new NullPointerException("Test");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(th);
        assertEquals(0, ed.getStackLineHash());
    }
    
    @Test
    public void testGetLineNumberHash() {
        Throwable th = new NullPointerException("Test");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(th);
        assertEquals(0, ed.getLineNumberHash());
    }

    @Test
    public void testEquals() {
        Throwable th = new NullPointerException("Test");
        ExceptionDiagnosis ed1 = new ExceptionDiagnosis(th);
        ExceptionDiagnosis ed2 = new ExceptionDiagnosis(th);
        assertTrue(ed1.equals(ed2));
    }
}
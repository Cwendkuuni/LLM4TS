package twitter4j;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testToString() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", exceptionDiagnosis.toString());
    }
    
    // Additional test cases for other methods and scenarios
    
    @Test
    public void testInclusionFilterEmpty() {
        Throwable th = new Throwable("Test Exception");
        String[] inclusionFilter = {};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertEquals("", exceptionDiagnosis.asHexString());
    }
    
    @Test
    public void testThrowableCause() {
        Throwable cause = new Throwable("Cause Exception");
        Throwable th = new Throwable("Test Exception", cause);
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        assertTrue(exceptionDiagnosis.asHexString().contains("Cause Exception"));
    }
    
    @Test
    public void testCustomInclusionFilter() {
        Throwable th = new Throwable("Test Exception");
        String[] inclusionFilter = {"twitter4j."};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertTrue(exceptionDiagnosis.asHexString().contains("twitter4j."));
    }
}
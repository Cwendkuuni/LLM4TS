import org.junit.Assert;
import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisWithNoInclusionFilter() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);

        Assert.assertEquals("", exceptionDiagnosis.asHexString());
        Assert.assertEquals(0, exceptionDiagnosis.getStackLineHash());
        Assert.assertEquals(0, exceptionDiagnosis.getLineNumberHash());
    }
    
    @Test
    public void testExceptionDiagnosisWithInclusionFilter() {
        String[] inclusionFilter = {"twitter4j"};
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        
        Assert.assertEquals("00000000-00000000", exceptionDiagnosis.asHexString());
        Assert.assertEquals(0, exceptionDiagnosis.getStackLineHash());
        Assert.assertEquals(0, exceptionDiagnosis.getLineNumberHash());
    }
    
    @Test
    public void testExceptionDiagnosisWithCause() {
        Throwable cause = new Throwable("Cause Exception");
        Throwable th = new Throwable("Test Exception", cause);
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        
        Assert.assertEquals("", exceptionDiagnosis.asHexString());
        Assert.assertEquals(0, exceptionDiagnosis.getStackLineHash());
        Assert.assertEquals(0, exceptionDiagnosis.getLineNumberHash());
    }

}
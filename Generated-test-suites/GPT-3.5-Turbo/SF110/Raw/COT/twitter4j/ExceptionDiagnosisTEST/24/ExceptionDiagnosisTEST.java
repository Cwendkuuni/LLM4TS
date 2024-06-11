
import org.junit.Assert;
import org.junit.Test;
import java.io.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructorWithThrowable() {
        Throwable throwable = new Throwable("Test Exception");

        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        
        Assert.assertEquals(0, exceptionDiagnosis.getStackLineHash());
        Assert.assertEquals(0, exceptionDiagnosis.getLineNumberHash());
    }

    @Test
    public void testConstructorWithThrowableAndInclusionFilter() {
        Throwable throwable = new Throwable("Test Exception");
        String[] inclusionFilter = new String[]{"twitter4j"};

        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        
        // Assert the expected stackLineHash and lineNumberHash values
        Assert.assertEquals(123456, exceptionDiagnosis.getStackLineHash());
        Assert.assertEquals(789, exceptionDiagnosis.getLineNumberHash());
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new Throwable("Test Exception");

        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        String expectedHexString = "00000000-00000000";
        
        Assert.assertEquals(expectedHexString, exceptionDiagnosis.asHexString());
    }

    @Test
    public void testEquals() {
        Throwable throwable1 = new Throwable("Test Exception");
        Throwable throwable2 = new Throwable("Test Exception");

        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);
        
        Assert.assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis2));
    }
}

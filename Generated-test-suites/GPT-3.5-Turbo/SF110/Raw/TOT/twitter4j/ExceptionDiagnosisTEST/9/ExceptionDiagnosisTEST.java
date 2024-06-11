import org.junit.Test;
import twitter4j.ExceptionDiagnosis;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructor1() {
        Throwable th = new Throwable("Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        // Assert statements
    }

    @Test
    public void testConstructor2() {
        Throwable th = new Throwable("Exception");
        String[] inclusionFilter = {"twitter4j."};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        // Assert statements
    }

    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable("Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        // Assert statements
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable("Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        // Assert statements
    }

    @Test
    public void testAsHexString() {
        Throwable th = new Throwable("Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        // Assert statements
    }

    @Test
    public void testEquals() {
        Throwable th1 = new Throwable("Exception");
        Throwable th2 = new Throwable("Exception");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th2);
        // Assert statements
    }

    @Test
    public void testHashCode() {
        Throwable th = new Throwable("Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        // Assert statements
    }

    @Test
    public void testToString() {
        Throwable th = new Throwable("Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        // Assert statements
    }
}
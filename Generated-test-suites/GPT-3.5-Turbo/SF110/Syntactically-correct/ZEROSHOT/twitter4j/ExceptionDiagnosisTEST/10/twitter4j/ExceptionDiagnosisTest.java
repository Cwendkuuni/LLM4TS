import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        int stackLineHash = diagnosis.getStackLineHash();
        // Test assertion here
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        int lineNumberHash = diagnosis.getLineNumberHash();
        // Test assertion here
    }

    @Test
    public void testAsHexString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        String hexString = diagnosis.asHexString();
        // Test assertion here
    }

    @Test
    public void testEquals() {
        Throwable th1 = new Throwable();
        Throwable th2 = new Throwable();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th2);
        boolean result = diagnosis1.equals(diagnosis2);
        // Test assertion here
    }

    @Test
    public void testHashCode() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        int hashCode = diagnosis.hashCode();
        // Test assertion here
    }

    @Test
    public void testToString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        String toString = diagnosis.toString();
        // Test assertion here
    }

}
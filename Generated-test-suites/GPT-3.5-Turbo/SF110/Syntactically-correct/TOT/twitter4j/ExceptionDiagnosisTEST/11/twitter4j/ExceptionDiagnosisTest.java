import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructor1() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertNotNull(diagnosis);
    }
    
    @Test
    public void testExceptionDiagnosisConstructor2() {
        Throwable th = new Throwable("Test Exception");
        String[] inclusionFilter = {"twitter4j."};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertNotNull(diagnosis);
    }
    
    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        int stackLineHash = diagnosis.getStackLineHash();
        assertEquals(0, stackLineHash);
    }
    
    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        int lineNumberHash = diagnosis.getLineNumberHash();
        assertEquals(0, lineNumberHash);
    }
    
    @Test
    public void testAsHexString() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        String hexString = diagnosis.asHexString();
        assertNotNull(hexString);
    }
    
    @Test
    public void testEquals() {
        Throwable th1 = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th1);
        Throwable th2 = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th2);
        assertTrue(diagnosis1.equals(diagnosis2));
    }
    
    @Test
    public void testHashCode() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        int hashCode = diagnosis.hashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testToString() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        String diagnosisString = diagnosis.toString();
        assertNotNull(diagnosisString);
    }
}
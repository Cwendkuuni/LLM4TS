import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructor() {
        Throwable th = new Throwable("Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals(diagnosis.getStackLineHash(), 0);
        assertEquals(diagnosis.getLineNumberHash(), 0);
        assertEquals(diagnosis.asHexString(), "00000000-00000000");
    }

    @Test
    public void testConstructorWithInclusionFilter() {
        Throwable th = new Throwable("Exception");
        String[] inclusionFilter = { "twitter4j" };
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertEquals(diagnosis.getStackLineHash(), 0);
        assertEquals(diagnosis.getLineNumberHash(), 0);
        assertEquals(diagnosis.asHexString(), "00000000-00000000");
    }

    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable("Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals(diagnosis.getStackLineHash(), 0);
    }
    
    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable("Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals(diagnosis.getLineNumberHash(), 0);
    }
    
    @Test
    public void testAsHexString() {
        Throwable th = new Throwable("Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals(diagnosis.asHexString(), "00000000-00000000");
    }
    
    @Test
    public void testEquals() {
        Throwable th = new Throwable("Exception");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th);
        assertTrue(diagnosis1.equals(diagnosis2));
    }
    
    @Test
    public void testHashCode() {
        Throwable th = new Throwable("Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals(diagnosis.hashCode(), 0);
    }
    
    @Test
    public void testToString() {
        Throwable th = new Throwable("Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals(diagnosis.toString(), "ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}");
    }
}
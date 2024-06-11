import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {
    
    @Test
    public void testExceptionDiagnosisConstructor() {
        Throwable th = new Exception();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals("00000000-00000000", diagnosis.asHexString());
    }
    
    @Test
    public void testExceptionDiagnosisConstructorWithFilter() {
        Throwable th = new Exception();
        String[] inclusionFilter = new String[]{"twitter4j"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals("00000000-00000000", diagnosis.asHexString());
    }
    
    @Test
    public void testToString() {
        Throwable th = new Exception();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", diagnosis.toString());
    }
    
    @Test
    public void testEquals() {
        Throwable th = new Exception();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th);
        
        assertTrue(diagnosis1.equals(diagnosis2));
    }
    
    @Test
    public void testHashCode() {
        Throwable th = new Exception();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        assertEquals(0, diagnosis.hashCode());
    }
    
}
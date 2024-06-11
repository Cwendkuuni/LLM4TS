
import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosis() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        assertNotNull(diagnosis);
    }
    
    @Test
    public void testExceptionDiagnosisWithInclusionFilter() {
        Throwable th = new Throwable();
        String[] inclusionFilter = {"filter"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        
        assertNotNull(diagnosis);
    }
    
    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        int stackLineHash = diagnosis.getStackLineHash();
        
        assertEquals(0, stackLineHash);
    }
    
    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        int lineNumberHash = diagnosis.getLineNumberHash();
        
        assertEquals(0, lineNumberHash);
    }
    
    @Test
    public void testAsHexString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        String hexString = diagnosis.asHexString();
        
        assertNotNull(hexString);
    }
    
    @Test
    public void testEquals() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th);
        
        boolean equals = diagnosis1.equals(diagnosis2);
        
        assertTrue(equals);
    }
    
    @Test
    public void testHashCode() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        int hashCode = diagnosis.hashCode();
        
        assertNotNull(hashCode);
    }
    
    @Test
    public void testToString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        String str = diagnosis.toString();
        
        assertNotNull(str);
    }
}

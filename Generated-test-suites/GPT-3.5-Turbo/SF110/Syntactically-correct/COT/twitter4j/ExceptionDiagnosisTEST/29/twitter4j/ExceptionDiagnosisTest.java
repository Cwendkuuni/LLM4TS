import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {
    
    @Test
    public void testConstructor() {
        Throwable th = new Throwable();
        String[] inclusionFilter = {};
        
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        
        assertNotNull(diagnosis);
    }
    
    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable();
        String[] inclusionFilter = {};
        
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        
        int stackLineHash = diagnosis.getStackLineHash();
        
        assertEquals(0, stackLineHash);
    }
    
    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable();
        String[] inclusionFilter = {};
        
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        
        int lineNumberHash = diagnosis.getLineNumberHash();
        
        assertEquals(0, lineNumberHash);
    }
    
    @Test
    public void testAsHexString() {
        Throwable th = new Throwable();
        String[] inclusionFilter = {};
        
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        
        String hexString = diagnosis.asHexString();
        
        assertNotNull(hexString);
    }
    
    @Test
    public void testEquals() {
        Throwable th1 = new Throwable();
        Throwable th2 = new Throwable();
        String[] inclusionFilter = {};
        
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th1, inclusionFilter);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th2, inclusionFilter);
        
        boolean equals = diagnosis1.equals(diagnosis2);
        
        assertFalse(equals);
    }
    
    @Test
    public void testHashCode() {
        Throwable th = new Throwable();
        String[] inclusionFilter = {};
        
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        
        int hashCode = diagnosis.hashCode();
        
        assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testToString() {
        Throwable th = new Throwable();
        String[] inclusionFilter = {};
        
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        
        String str = diagnosis.toString();
        
        assertNotNull(str);
    }
}
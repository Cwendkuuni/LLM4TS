package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructor1() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        assertNotNull(diagnosis);
        assertNotNull(diagnosis.th);
        assertEquals("", diagnosis.hexString);
        assertEquals(0, diagnosis.stackLineHash);
        assertEquals(0, diagnosis.lineNumberHash);
    }
    
    @Test
    public void testConstructor2() {
        Throwable th = new Throwable("Test Exception");
        String[] inclusionFilter = {"twitter4j."};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        
        assertNotNull(diagnosis);
        assertNotNull(diagnosis.th);
        assertEquals("", diagnosis.hexString);
        assertEquals(0, diagnosis.stackLineHash);
        assertEquals(0, diagnosis.lineNumberHash);
    }
    
    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        int hash = diagnosis.getStackLineHash();
        assertEquals(0, hash);
    }
    
    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        int hash = diagnosis.getLineNumberHash();
        assertEquals(0, hash);
    }
    
    @Test
    public void testAsHexString() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        String hexString = diagnosis.asHexString();
        assertEquals("", hexString);
    }
    
    @Test
    public void testEquals() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th);
        
        boolean result = diagnosis1.equals(diagnosis2);
        assertTrue(result);
    }
    
    @Test
    public void testHashCode() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        int hashCode = diagnosis.hashCode();
        assertEquals(0, hashCode);
    }
    
    @Test
    public void testToString() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        String str = diagnosis.toString();
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", str);
    }
}
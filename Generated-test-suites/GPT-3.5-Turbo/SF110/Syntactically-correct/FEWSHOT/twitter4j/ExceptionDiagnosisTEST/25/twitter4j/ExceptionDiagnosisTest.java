package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ExceptionDiagnosisTest {
    
    @Test
    public void testExceptionDiagnosisConstructorWithThrowable() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diag = new ExceptionDiagnosis(th);
        
        assertNotNull(diag);
        assertEquals(0, diag.getStackLineHash());
        assertEquals(0, diag.getLineNumberHash());
        assertEquals("", diag.asHexString());
    }
    
    @Test
    public void testExceptionDiagnosisConstructorWithThrowableAndInclusionFilter() {
        Throwable th = new Throwable("Test Exception");
        String[] inclusionFilter = { "twitter4j." };
        ExceptionDiagnosis diag = new ExceptionDiagnosis(th, inclusionFilter);
        
        assertNotNull(diag);
        assertEquals(0, diag.getStackLineHash());
        assertEquals(0, diag.getLineNumberHash());
        assertEquals("", diag.asHexString());
    }
    
    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diag = new ExceptionDiagnosis(th);
        
        assertEquals(0, diag.getStackLineHash());
    }
    
    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diag = new ExceptionDiagnosis(th);
        
        assertEquals(0, diag.getLineNumberHash());
    }
    
    @Test
    public void testAsHexString() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diag = new ExceptionDiagnosis(th);
        
        assertEquals("", diag.asHexString());
    }
    
    @Test
    public void testEquals() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diag1 = new ExceptionDiagnosis(th);
        ExceptionDiagnosis diag2 = new ExceptionDiagnosis(th);
        
        assertTrue(diag1.equals(diag2));
    }
    
    @Test
    public void testHashCode() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diag = new ExceptionDiagnosis(th);
        
        assertEquals(0, diag.hashCode());
    }
    
    @Test
    public void testToString() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diag = new ExceptionDiagnosis(th);
        
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", diag.toString());
    }
}
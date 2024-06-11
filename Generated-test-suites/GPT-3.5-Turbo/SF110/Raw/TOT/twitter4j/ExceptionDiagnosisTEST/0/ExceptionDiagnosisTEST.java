import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructor1() {
        Throwable th = new Throwable("Exception");
        ExceptionDiagnosis diag = new ExceptionDiagnosis(th);
        
        assertEquals("", diag.asHexString());
        assertNull(th.getCause());
        assertEquals(0, diag.getStackLineHash());
        assertEquals(0, diag.getLineNumberHash());
    }
    
    @Test
    public void testConstructor2() {
        Throwable cause = new Throwable("Cause exception");
        Throwable th = new Throwable("Exception", cause);
        ExceptionDiagnosis diag = new ExceptionDiagnosis(th);
        
        assertEquals("", diag.asHexString());
        assertEquals(cause, th.getCause());
        assertEquals(0, diag.getStackLineHash());
        assertEquals(0, diag.getLineNumberHash());
    }
    
    @Test
    public void testConstructor3() {
        Throwable cause = new Throwable("Cause exception");
        Throwable th = new Throwable("Exception", cause);
        ExceptionDiagnosis diag = new ExceptionDiagnosis(th, new String[]{});
        
        assertEquals("", diag.asHexString());
        assertEquals(cause, th.getCause());
        assertEquals(0, diag.getStackLineHash());
        assertEquals(0, diag.getLineNumberHash());
    }
    
    @Test
    public void testConstructor4() {
        Throwable cause = new Throwable("Cause exception");
        Throwable th = new Throwable("Exception", cause);
        ExceptionDiagnosis diag = new ExceptionDiagnosis(th, new String[]{"java"});
        
        assertEquals("", diag.asHexString());
        assertEquals(cause, th.getCause());
        assertEquals(0, diag.getStackLineHash());
        assertEquals(0, diag.getLineNumberHash());
    }
    
    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable("Exception");
        ExceptionDiagnosis diag = new ExceptionDiagnosis(th);
        
        assertEquals(0, diag.getStackLineHash());
    }
    
    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable("Exception");
        ExceptionDiagnosis diag = new ExceptionDiagnosis(th);
        
        assertEquals(0, diag.getLineNumberHash());
    }
    
    @Test
    public void testAsHexString() {
        Throwable th = new Throwable("Exception");
        ExceptionDiagnosis diag = new ExceptionDiagnosis(th);
        
        assertEquals("", diag.asHexString());
    }
    
    @Test
    public void testEquals() {
        Throwable th1 = new Throwable("Exception");
        ExceptionDiagnosis diag1 = new ExceptionDiagnosis(th1);
        
        Throwable th2 = new Throwable("Exception");
        ExceptionDiagnosis diag2 = new ExceptionDiagnosis(th2);
        
        assertTrue(diag1.equals(diag1));
        assertFalse(diag1.equals(diag2));
    }
    
    @Test
    public void testHashCode() {
        Throwable th = new Throwable("Exception");
        ExceptionDiagnosis diag = new ExceptionDiagnosis(th);
        
        assertEquals(0, diag.hashCode());
    }
    
    @Test
    public void testToString() {
        Throwable th = new Throwable("Exception");
        ExceptionDiagnosis diag = new ExceptionDiagnosis(th);
        
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", diag.toString());
    }
}
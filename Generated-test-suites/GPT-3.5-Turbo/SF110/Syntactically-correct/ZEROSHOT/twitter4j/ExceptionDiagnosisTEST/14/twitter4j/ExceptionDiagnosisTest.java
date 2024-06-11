import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructorWithThrowable() {
        Throwable th = new Throwable("test");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
        assertEquals("", exceptionDiagnosis.asHexString());
    }
    
    @Test
    public void testConstructorWithThrowableAndFilter() {
        Throwable th = new Throwable("test");
        String[] filter = {"TestClass"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th, filter);
        
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
        assertEquals("", exceptionDiagnosis.asHexString());
    }
    
    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable("test");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        
        int result = exceptionDiagnosis.getStackLineHash();
        
        assertEquals(0, result);
    }
    
    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable("test");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        
        int result = exceptionDiagnosis.getLineNumberHash();
        
        assertEquals(0, result);
    }
    
    @Test
    public void testAsHexString() {
        Throwable th = new Throwable("test");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        
        String result = exceptionDiagnosis.asHexString();
        
        assertEquals("", result);
    }
    
    @Test
    public void testEquals() {
        Throwable th1 = new Throwable("test1");
        Throwable th2 = new Throwable("test2");
        
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(th1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(th2);
        
        boolean result = exceptionDiagnosis1.equals(exceptionDiagnosis2);
        
        assertFalse(result);
    }
    
    @Test
    public void testHashCode() {
        Throwable th = new Throwable("test");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        
        int result = exceptionDiagnosis.hashCode();
        
        assertEquals(0, result);
    }
    
    @Test
    public void testToString() {
        Throwable th = new Throwable("test");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        
        String result = exceptionDiagnosis.toString();
        
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", result);
    }
}
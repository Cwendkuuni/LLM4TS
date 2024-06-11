import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExceptionDiagnosisTest {
    
    @Test
    public void testConstructorWithThrowable() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("00000000-00000000", diagnosis.asHexString());
    }
    
    @Test
    public void testConstructorWithThrowableAndFilter() {
        Throwable th = new Throwable("Test Exception");
        String[] filter = {"twitter4j."};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, filter);
        
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("00000000-00000000", diagnosis.asHexString());
    }
    
    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable("Test Exception");
        String[] filter = {"twitter4j."};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, filter);
        
        assertEquals(0, diagnosis.getStackLineHash());
    }
    
    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable("Test Exception");
        String[] filter = {"twitter4j."};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, filter);
        
        assertEquals(0, diagnosis.getLineNumberHash());
    }
    
    @Test
    public void testAsHexString() {
        Throwable th = new Throwable("Test Exception");
        String[] filter = {"twitter4j."};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, filter);
        
        assertEquals("00000000-00000000", diagnosis.asHexString());
    }
    
    @Test
    public void testEquals() {
        Throwable th1 = new Throwable("Test Exception 1");
        Throwable th2 = new Throwable("Test Exception 2");
        String[] filter = {"twitter4j."};
        
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th1, filter);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th2, filter);
        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(th2, filter);
        
        assertEquals(false, diagnosis1.equals(null));
        assertEquals(false, diagnosis1.equals(new ExceptionDiagnosis(th1)));
        assertEquals(false, diagnosis1.equals(diagnosis2));
        assertEquals(true, diagnosis2.equals(diagnosis3));
    }
    
    @Test
    public void testHashCode() {
        Throwable th = new Throwable("Test Exception");
        String[] filter = {"twitter4j."};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, filter);
        
        assertEquals(0, diagnosis.hashCode());
    }
    
    @Test
    public void testToString() {
        Throwable th = new Throwable("Test Exception");
        String[] filter = {"twitter4j."};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, filter);
        
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", diagnosis.toString());
    }
}
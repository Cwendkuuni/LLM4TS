package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructor() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("", diagnosis.asHexString());
    }
    
    @Test
    public void testEquals() {
        Throwable th1 = new Throwable("Test Exception");
        Throwable th2 = new Throwable("Test Exception");
        
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th2);
        
        assertTrue(diagnosis1.equals(diagnosis2));
    }
    
    @Test
    public void testHashCode() {
        Throwable th1 = new Throwable("Test Exception");
        Throwable th2 = new Throwable("Test Exception");
        
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th2);
        
        assertEquals(diagnosis1.hashCode(), diagnosis2.hashCode());
    }
    
    @Test
    public void testToString() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", diagnosis.toString());
    }
}
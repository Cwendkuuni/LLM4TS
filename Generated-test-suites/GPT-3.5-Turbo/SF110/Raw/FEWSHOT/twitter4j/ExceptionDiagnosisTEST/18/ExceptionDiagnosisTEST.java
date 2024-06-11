import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Test;

public class ExceptionDiagnosisTest {
    
    @Test
    public void testConstructorWithThrowable() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertNotNull(diagnosis.asHexString());
    }
    
    @Test
    public void testConstructorWithThrowableAndInclusionFilter() {
        Throwable th = new Throwable("Test Exception");
        String[] inclusionFilter = {"org.junit."};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertNotNull(diagnosis.asHexString());
    }
    
    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        assertEquals(0, diagnosis.getStackLineHash());
    }
    
    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        assertEquals(0, diagnosis.getLineNumberHash());
    }
    
    @Test
    public void testAsHexString() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        assertNotNull(diagnosis.asHexString());
    }
    
    @Test
    public void testEquals() {
        Throwable th1 = new Throwable("Test Exception 1");
        Throwable th2 = new Throwable("Test Exception 2");
        
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th2);
        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(th1);
        
        assertFalse(diagnosis1.equals(diagnosis2));
        assertEquals(diagnosis1, diagnosis3);
    }
    
    @Test
    public void testHashCode() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        assertNotNull(diagnosis.hashCode());
    }
    
    @Test
    public void testToString() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        assertNotNull(diagnosis.toString());
    }
}
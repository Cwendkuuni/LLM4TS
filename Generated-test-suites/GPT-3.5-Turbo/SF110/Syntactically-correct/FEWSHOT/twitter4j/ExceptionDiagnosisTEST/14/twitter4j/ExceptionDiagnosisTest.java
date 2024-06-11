
package twitter4j;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExceptionDiagnosisTest {
    @Test
    public void testExceptionDiagnosisConstructor() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", diagnosis.toString());
    }
    
    @Test
    public void testExceptionDiagnosisConstructorWithInclusionFilter() {
        Throwable th = new Throwable("Test Exception");
        String[] inclusionFilter = {"twitter4j."};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", diagnosis.toString());
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
        
        assertEquals("00000000-00000000", diagnosis.asHexString());
    }
    
    @Test
    public void testEquals() {
        Throwable th1 = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th1);
        
        Throwable th2 = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th2);
        
        assertEquals(true, diagnosis1.equals(diagnosis2));
    }
    
    @Test
    public void testHashCode() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        assertEquals(0, diagnosis.hashCode());
    }
}

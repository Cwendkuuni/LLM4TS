
package twitter4j;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructorWithThrowable() {
        Throwable th = new Throwable();
        
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
    }
    
    @Test
    public void testConstructorWithThrowableAndInclusionFilter() {
        Throwable th = new Throwable();
        String[] inclusionFilter = {"twitter4j.ExceptionDiagnosis"};
        
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
    }
    
    @Test
    public void testEquals() {
        Throwable th = new Throwable();
        Throwable th2 = new Throwable();
        String[] inclusionFilter = {};
        
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(th2);
        
        assertEquals(true, exceptionDiagnosis.equals(exceptionDiagnosis));
        assertEquals(false, exceptionDiagnosis.equals(null));
        assertEquals(false, exceptionDiagnosis.equals(new Object()));
        assertEquals(false, exceptionDiagnosis.equals(exceptionDiagnosis2));
    }
    
    @Test
    public void testHashCode() {
        Throwable th = new Throwable();
        Throwable th2 = new Throwable();
        String[] inclusionFilter = {};
        
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(th2);
        
        assertEquals(exceptionDiagnosis.hashCode(), exceptionDiagnosis.hashCode());
        assertEquals(false, exceptionDiagnosis.hashCode() == exceptionDiagnosis2.hashCode());
    }

    @Test
    public void testToString() {
        Throwable th = new Throwable();
        
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", exceptionDiagnosis.toString());
    }
}

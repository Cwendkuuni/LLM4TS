package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExceptionDiagnosisTest {
    @Test
    public void testConstructorWithThrowable() {
        Throwable th = new NullPointerException();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
        assertTrue(exceptionDiagnosis.asHexString().isEmpty());
        
        th = new IllegalArgumentException();
        exceptionDiagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
        assertTrue(exceptionDiagnosis.asHexString().isEmpty());
    }
    
    @Test
    public void testConstructorWithThrowableAndInclusionFilter() {
        Throwable th = new NullPointerException();
        String[] inclusionFilter = {"java.lang."};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
        assertTrue(exceptionDiagnosis.asHexString().isEmpty());
        
        th = new IllegalArgumentException();
        exceptionDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
        assertTrue(exceptionDiagnosis.asHexString().isEmpty());
    }
    
    @Test
    public void testGetStackLineHash() {
        Throwable th = new NullPointerException();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
        
        th = new IllegalArgumentException();
        exceptionDiagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
    }
    
    @Test
    public void testGetLineNumberHash() {
        Throwable th = new NullPointerException();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
        
        th = new IllegalArgumentException();
        exceptionDiagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
    }
    
    @Test
    public void testAsHexString() {
        Throwable th = new NullPointerException();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        assertTrue(exceptionDiagnosis.asHexString().isEmpty());
        
        th = new IllegalArgumentException();
        exceptionDiagnosis = new ExceptionDiagnosis(th);
        assertTrue(exceptionDiagnosis.asHexString().isEmpty());
    }
    
    @Test
    public void testEquals() {
        Throwable th1 = new NullPointerException();
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(th1);
        Throwable th2 = new NullPointerException();
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(th2);
        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis2));
        
        th1 = new NullPointerException();
        exceptionDiagnosis1 = new ExceptionDiagnosis(th1);
        th2 = new IllegalArgumentException();
        exceptionDiagnosis2 = new ExceptionDiagnosis(th2);
        assertTrue(!exceptionDiagnosis1.equals(exceptionDiagnosis2));
    }
    
    @Test
    public void testHashCode() {
        Throwable th = new NullPointerException();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, exceptionDiagnosis.hashCode());
        
        th = new IllegalArgumentException();
        exceptionDiagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, exceptionDiagnosis.hashCode());
    }
    
    @Test
    public void testToString() {
        Throwable th = new NullPointerException();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", exceptionDiagnosis.toString());
        
        th = new IllegalArgumentException();
        exceptionDiagnosis = new ExceptionDiagnosis(th);
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", exceptionDiagnosis.toString());
    }
}
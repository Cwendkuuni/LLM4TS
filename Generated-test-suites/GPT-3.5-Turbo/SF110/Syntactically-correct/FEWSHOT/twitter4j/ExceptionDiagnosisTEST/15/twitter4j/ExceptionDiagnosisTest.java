package twitter4j;

import static org.junit.Assert.*;

import org.junit.Test;


public class ExceptionDiagnosisTest {
    
    @Test
    public void testConstructorWithThrowable() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertNotNull(diagnosis);
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("", diagnosis.asHexString());
    }
    
    @Test
    public void testConstructorWithThrowableAndInclusionFilter() {
        Throwable th = new Throwable();
        String[] inclusionFilter = {"filter"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertNotNull(diagnosis);
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("", diagnosis.asHexString());
    }
    
    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, diagnosis.getStackLineHash());
    }
    
    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, diagnosis.getLineNumberHash());
    }
    
    @Test
    public void testAsHexString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals("", diagnosis.asHexString());
    }
    
    @Test
    public void testEquals() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th);
        assertTrue(diagnosis1.equals(diagnosis2));
    }
    
    @Test
    public void testHashCode() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        int expectedHash = diagnosis.getStackLineHash() * 31 + diagnosis.getLineNumberHash();
        assertEquals(expectedHash, diagnosis.hashCode());
    }
    
    @Test
    public void testToString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        String expectedString = "ExceptionDiagnosis{stackLineHash=" + diagnosis.getStackLineHash() + ", lineNumberHash=" + diagnosis.getLineNumberHash() + "}";
        assertEquals(expectedString, diagnosis.toString());
    }
}
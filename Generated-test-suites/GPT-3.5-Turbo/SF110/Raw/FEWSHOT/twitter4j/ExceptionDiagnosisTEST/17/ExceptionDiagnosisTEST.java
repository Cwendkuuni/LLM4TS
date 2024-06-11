package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ExceptionDiagnosisTest {
    
    @Test
    public void testConstructorWithThrowable() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new NullPointerException());
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("00000000-00000000", diagnosis.asHexString());
    }
    
    @Test
    public void testConstructorWithThrowableAndInclusionFilter() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new NullPointerException(), new String[]{"twitter4j."});
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("00000000-00000000", diagnosis.asHexString());
    }
    
    @Test
    public void testConstructorWithThrowableAndEmptyInclusionFilter() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new NullPointerException(), new String[0]);
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("00000000-00000000", diagnosis.asHexString());
    }
    
    @Test
    public void testGetStackLineHash() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new NullPointerException());
        assertEquals(0, diagnosis.getStackLineHash());
    }
    
    @Test
    public void testGetLineNumberHash() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new NullPointerException());
        assertEquals(0, diagnosis.getLineNumberHash());
    }
    
    @Test
    public void testAsHexString() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new NullPointerException());
        assertEquals("00000000-00000000", diagnosis.asHexString());
    }
    
    @Test
    public void testEquals() {
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(new NullPointerException());
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(new NullPointerException());
        assertTrue(diagnosis1.equals(diagnosis2));
    }
    
    @Test
    public void testHashCode() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new NullPointerException());
        assertEquals(0, diagnosis.hashCode());
    }
    
    @Test
    public void testToString() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new NullPointerException());
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", diagnosis.toString());
    }
}
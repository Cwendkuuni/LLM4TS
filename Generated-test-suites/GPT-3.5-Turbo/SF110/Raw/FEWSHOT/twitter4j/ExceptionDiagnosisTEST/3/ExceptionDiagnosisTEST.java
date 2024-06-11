package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ExceptionDiagnosisTest {
    @Test
    public void testConstructorWithThrowable() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("", diagnosis.asHexString());
        assertFalse(diagnosis.equals(null));
        assertFalse(diagnosis.equals(new Object()));
        assertFalse(diagnosis.equals(new ExceptionDiagnosis(new Throwable())));
        assertFalse(diagnosis.equals(new ExceptionDiagnosis(new Throwable(), new String[0])));
    }
    
    @Test
    public void testConstructorWithThrowableAndFilter() {
        Throwable th = new Throwable();
        String[] filter = {"twitter4j."};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, filter);
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("", diagnosis.asHexString());
        assertFalse(diagnosis.equals(null));
        assertFalse(diagnosis.equals(new Object()));
        assertFalse(diagnosis.equals(new ExceptionDiagnosis(new Throwable())));
        assertFalse(diagnosis.equals(new ExceptionDiagnosis(new Throwable(), new String[0])));
    }
    
    @Test
    public void testConstructorWithThrowableAndInclusionFilter() {
        Throwable th = new Throwable();
        String[] filter = {"twitter4j."};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, filter);
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("", diagnosis.asHexString());
        assertFalse(diagnosis.equals(null));
        assertFalse(diagnosis.equals(new Object()));
        assertFalse(diagnosis.equals(new ExceptionDiagnosis(new Throwable())));
        assertFalse(diagnosis.equals(new ExceptionDiagnosis(new Throwable(), new String[0])));
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
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertFalse(diagnosis.equals(null));
        assertFalse(diagnosis.equals(new Object()));
        assertFalse(diagnosis.equals(new ExceptionDiagnosis(new Throwable())));
        assertFalse(diagnosis.equals(new ExceptionDiagnosis(new Throwable(), new String[0])));
    }
    
    @Test
    public void testHashCode() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        int expectedHash = 31 * diagnosis.getStackLineHash() + diagnosis.getLineNumberHash();
        assertEquals(expectedHash, diagnosis.hashCode());
    }
    
    @Test
    public void testToString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        String expectedString = "ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}";
        assertEquals(expectedString, diagnosis.toString());
    }
}
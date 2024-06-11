
package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExceptionDiagnosisTest {
    
    @Test
    public void testConstructorWithThrowable() {
        Throwable throwable = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertNotNull(diagnosis);
        assertEquals(throwable, diagnosis.th);
        assertEquals("", diagnosis.hexString);
        assertEquals(0, diagnosis.stackLineHash);
        assertEquals(0, diagnosis.lineNumberHash);
    }
    
    @Test
    public void testConstructorWithThrowableAndInclusionFilter() {
        Throwable throwable = new Throwable();
        String[] inclusionFilter = {"twitter4j"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        assertNotNull(diagnosis);
        assertEquals(throwable, diagnosis.th);
        assertEquals("", diagnosis.hexString);
        assertEquals(0, diagnosis.stackLineHash);
        assertEquals(0, diagnosis.lineNumberHash);
    }
    
    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertEquals(0, diagnosis.getStackLineHash());
    }
    
    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertEquals(0, diagnosis.getLineNumberHash());
    }
    
    @Test
    public void testAsHexString() {
        Throwable throwable = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertEquals("", diagnosis.asHexString());
    }
    
    @Test
    public void testToHexString() {
        Throwable throwable = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertEquals("00000000", diagnosis.toHexString(0));
    }
    
    @Test
    public void testEquals() {
        Throwable throwable = new Throwable();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable);
        assertTrue(diagnosis1.equals(diagnosis1));
        assertFalse(diagnosis1.equals(null));
        assertFalse(diagnosis1.equals(new Object()));
        assertTrue(diagnosis1.equals(diagnosis2));
    }
    
    @Test
    public void testHashCode() {
        Throwable throwable = new Throwable();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable);
        assertEquals(diagnosis1.hashCode(), diagnosis2.hashCode());
    }
    
    @Test
    public void testToString() {
        Throwable throwable = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", diagnosis.toString());
    }
}

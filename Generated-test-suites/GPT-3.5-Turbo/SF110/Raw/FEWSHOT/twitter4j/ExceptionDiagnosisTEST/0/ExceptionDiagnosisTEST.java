package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExceptionDiagnosisTest {
    
    @Test
    public void testConstructorWithoutInclusionFilter() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("", diagnosis.asHexString());
    }
    
    @Test
    public void testConstructorWithInclusionFilter() {
        Throwable th = new Throwable();
        String[] inclusionFilter = {"twitter4j."};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("", diagnosis.asHexString());
    }
    
    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        diagnosis.stackLineHash = 123;
        assertEquals(123, diagnosis.getStackLineHash());
    }
    
    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        diagnosis.lineNumberHash = 456;
        assertEquals(456, diagnosis.getLineNumberHash());
    }
    
    @Test
    public void testAsHexString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        diagnosis.hexString = "abc123";
        assertEquals("abc123", diagnosis.asHexString());
    }
    
    @Test
    public void testToHexString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        String hexString = diagnosis.toHexString(123);
        assertEquals("0000007b", hexString);
    }
    
    @Test
    public void testEquals() {
        Throwable th1 = new Throwable();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th1);
        diagnosis1.stackLineHash = 123;
        diagnosis1.lineNumberHash = 456;
        
        Throwable th2 = new Throwable();
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th2);
        diagnosis2.stackLineHash = 123;
        diagnosis2.lineNumberHash = 456;
        
        Throwable th3 = new Throwable();
        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(th3);
        diagnosis3.stackLineHash = 789;
        diagnosis3.lineNumberHash = 101112;
        
        assertTrue(diagnosis1.equals(diagnosis2));
        assertTrue(!diagnosis1.equals(diagnosis3));
    }
    
    @Test
    public void testHashCode() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        diagnosis.stackLineHash = 123;
        diagnosis.lineNumberHash = 456;
        
        int hashCode = diagnosis.hashCode();
        assertEquals(7047, hashCode);
    }
    
    @Test
    public void testToString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        diagnosis.stackLineHash = 123;
        diagnosis.lineNumberHash = 456;
        
        String toString = diagnosis.toString();
        assertEquals("ExceptionDiagnosis{stackLineHash=123, lineNumberHash=456}", toString);
    }
}
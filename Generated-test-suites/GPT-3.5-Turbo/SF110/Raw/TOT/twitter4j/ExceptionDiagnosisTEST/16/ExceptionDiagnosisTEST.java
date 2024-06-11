
package twitter4j;

import org.junit.*;

import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {
    
    private static final String[] DEFAULT_FILTER = {};
    
    // Test for constructor ExceptionDiagnosis(Throwable th)
    @Test
    public void testConstructor1() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(th);
        assertNotNull(ed);
        assertNull(ed.asHexString());
        assertNull(ed.getCause());
        assertEquals(0, ed.getLineNumberHash());
        assertEquals(0, ed.getStackLineHash());
    }
    
    // Test for constructor ExceptionDiagnosis(Throwable th, String[] inclusionFilter)
    @Test
    public void testConstructor2() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(th, DEFAULT_FILTER);
        assertNotNull(ed);
        assertNull(ed.asHexString());
        assertNull(ed.getCause());
        assertEquals(0, ed.getLineNumberHash());
        assertEquals(0, ed.getStackLineHash());
    }
    
    // Test for getStackLineHash()
    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(th, DEFAULT_FILTER);
        assertEquals(0, ed.getStackLineHash());
        
        th = new NullPointerException("Null Pointer Exception");
        ed = new ExceptionDiagnosis(th, DEFAULT_FILTER);
        assertNotEquals(0, ed.getStackLineHash());
    }
    
    // Test for getLineNumberHash()
    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(th, DEFAULT_FILTER);
        assertEquals(0, ed.getLineNumberHash());
        
        th = new ArrayIndexOutOfBoundsException("Array Index Out Of Bounds Exception");
        ed = new ExceptionDiagnosis(th, DEFAULT_FILTER);
        assertNotEquals(0, ed.getLineNumberHash());
    }
    
    // Test for asHexString()
    @Test
    public void testAsHexString() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(th, DEFAULT_FILTER);
        assertNull(ed.asHexString());
        
        th = new IllegalArgumentException("Illegal Argument Exception");
        ed = new ExceptionDiagnosis(th, DEFAULT_FILTER);
        assertNotNull(ed.asHexString());
    }
    
    // Test for equals()
    @Test
    public void testEquals() {
        Throwable th1 = new Throwable("Test Exception");
        Throwable th2 = new Throwable("Test Exception");
        ExceptionDiagnosis ed1 = new ExceptionDiagnosis(th1, DEFAULT_FILTER);
        ExceptionDiagnosis ed2 = new ExceptionDiagnosis(th2, DEFAULT_FILTER);
        assertFalse(ed1.equals(ed2));
        
        ExceptionDiagnosis ed3 = new ExceptionDiagnosis(th1, DEFAULT_FILTER);
        assertTrue(ed1.equals(ed3));
    }
    
    // Test for hashCode()
    @Test
    public void testHashCode() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(th, DEFAULT_FILTER);
        assertNotNull(ed.hashCode());
    }
    
    // Test for toString()
    @Test
    public void testToString() {
        Throwable th = new Throwable("Test Exception");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(th, DEFAULT_FILTER);
        assertNotNull(ed.toString());
        assertTrue(ed.toString().contains("stackLineHash"));
        assertTrue(ed.toString().contains("lineNumberHash"));
    }
}


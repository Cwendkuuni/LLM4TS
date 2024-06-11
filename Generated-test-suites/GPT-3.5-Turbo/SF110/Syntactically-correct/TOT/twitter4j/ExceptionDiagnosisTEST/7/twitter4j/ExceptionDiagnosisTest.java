package twitter4j;

import org.junit.*;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructor1() {
        Throwable th = new Throwable();
        ExceptionDiagnosis exDiagnosis = new ExceptionDiagnosis(th);
        assertNotNull(exDiagnosis);
    }
    
    @Test
    public void testConstructor2() {
        String[] inclusionFilter = {"twitter4j."};
        Throwable th = new Throwable();
        ExceptionDiagnosis exDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertNotNull(exDiagnosis);
    }
    
    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis exDiagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, exDiagnosis.getStackLineHash());
    }
    
    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis exDiagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, exDiagnosis.getLineNumberHash());
    }
    
    @Test
    public void testAsHexString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis exDiagnosis = new ExceptionDiagnosis(th);
        assertEquals("00000000-00000000", exDiagnosis.asHexString());
    }
    
    @Test
    public void testEquals() {
        Throwable th1 = new Throwable();
        ExceptionDiagnosis exDiagnosis1 = new ExceptionDiagnosis(th1);
        
        Throwable th2 = new Throwable();
        ExceptionDiagnosis exDiagnosis2 = new ExceptionDiagnosis(th2);
        
        Throwable th3 = new Throwable();
        ExceptionDiagnosis exDiagnosis3 = new ExceptionDiagnosis(th3);
        
        assertEquals(true, exDiagnosis1.equals(exDiagnosis2));
        assertEquals(true, exDiagnosis2.equals(exDiagnosis1));
        assertEquals(true, exDiagnosis1.equals(exDiagnosis3));
        assertEquals(false, exDiagnosis1.equals(null));
        assertEquals(false, exDiagnosis1.equals(new Object()));
    }
    
    @Test
    public void testHashCode() {
        Throwable th = new Throwable();
        ExceptionDiagnosis exDiagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, exDiagnosis.hashCode());
    }
    
    @Test
    public void testToString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis exDiagnosis = new ExceptionDiagnosis(th);
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", exDiagnosis.toString());
    }
}
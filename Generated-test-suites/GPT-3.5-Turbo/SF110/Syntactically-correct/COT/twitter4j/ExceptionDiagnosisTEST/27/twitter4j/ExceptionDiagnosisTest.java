
import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {
    
    @Test
    public void testConstructor() {
        Throwable th = new Throwable();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        assertNotNull(exceptionDiagnosis);
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
        assertEquals("", exceptionDiagnosis.asHexString());
    }
    
    @Test
    public void testConstructorWithInclusionFilter() {
        Throwable th = new Throwable();
        String[] inclusionFilter = {"twitter4j"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertNotNull(exceptionDiagnosis);
    }
    
    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        int stackLineHash = exceptionDiagnosis.getStackLineHash();
        assertEquals(0, stackLineHash);
    }
    
    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        int lineNumberHash = exceptionDiagnosis.getLineNumberHash();
        assertEquals(0, lineNumberHash);
    }
    
    @Test
    public void testAsHexString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        String hexString = exceptionDiagnosis.asHexString();
        assertEquals("", hexString);
    }
    
    @Test
    public void testToString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        String toString = exceptionDiagnosis.toString();
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", toString);
    }
    
    @Test
    public void testEquals() {
        Throwable th = new Throwable();
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(th);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(th);
        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis2));
    }
    
    @Test
    public void testHashCode() {
        Throwable th = new Throwable();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);
        int hashCode = exceptionDiagnosis.hashCode();
        assertEquals(0, hashCode);
    }
}

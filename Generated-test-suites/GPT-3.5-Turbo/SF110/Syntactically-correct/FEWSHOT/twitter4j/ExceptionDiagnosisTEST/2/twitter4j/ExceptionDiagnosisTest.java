import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import twitter4j.ExceptionDiagnosis;

public class ExceptionDiagnosisTest {
    
    @Test
    public void testConstructor() {
        Throwable th = new Throwable();
        String[] inclusionFilter= new String[0];
        ExceptionDiagnosis exDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        
        assertEquals(0, exDiagnosis.getStackLineHash());
        assertEquals(0, exDiagnosis.getLineNumberHash());
        assertEquals("", exDiagnosis.asHexString());
    }
    
    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable();
        String[] inclusionFilter= new String[0];
        ExceptionDiagnosis exDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        
        assertEquals(0, exDiagnosis.getStackLineHash());
    }
    
    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable();
        String[] inclusionFilter= new String[0];
        ExceptionDiagnosis exDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        
        assertEquals(0, exDiagnosis.getLineNumberHash());
    }
    
    @Test
    public void testAsHexString() {
        Throwable th = new Throwable();
        String[] inclusionFilter= new String[0];
        ExceptionDiagnosis exDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        
        assertEquals("", exDiagnosis.asHexString());
    }
    
    @Test
    public void testEquals() {
        Throwable th = new Throwable();
        String[] inclusionFilter= new String[0];
        ExceptionDiagnosis exDiagnosis1 = new ExceptionDiagnosis(th, inclusionFilter);
        ExceptionDiagnosis exDiagnosis2 = new ExceptionDiagnosis(th, inclusionFilter);
        
        assertTrue(exDiagnosis1.equals(exDiagnosis2));
    }
    
    @Test
    public void testHashCode() {
        Throwable th = new Throwable();
        String[] inclusionFilter= new String[0];
        ExceptionDiagnosis exDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        
        assertEquals(0, exDiagnosis.hashCode());
    }
    
    @Test
    public void testToString() {
        Throwable th = new Throwable();
        String[] inclusionFilter= new String[0];
        ExceptionDiagnosis exDiagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", exDiagnosis.toString());
    }
}
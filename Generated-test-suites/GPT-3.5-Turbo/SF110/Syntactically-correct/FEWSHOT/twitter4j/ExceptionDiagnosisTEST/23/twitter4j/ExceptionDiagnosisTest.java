package twitter4j;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExceptionDiagnosisTest {
    
    @Test
    public void testConstructor() {
        Throwable th = new Throwable();
        String[] inclusionFilter = {};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertEquals("", diagnosis.toHexString(diagnosis.stackLineHash));
        assertEquals("", diagnosis.toHexString(diagnosis.lineNumberHash));
        assertEquals("", diagnosis.asHexString());
    }
    
    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable();
        String[] inclusionFilter = {};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertEquals(0, diagnosis.getStackLineHash());
    }
    
    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable();
        String[] inclusionFilter = {};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertEquals(0, diagnosis.getLineNumberHash());
    }
    
    @Test
    public void testAsHexString() {
        Throwable th = new Throwable();
        String[] inclusionFilter = {};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertEquals("", diagnosis.asHexString());
    }
    
    @Test
    public void testToHexString() {
        Throwable th = new Throwable();
        String[] inclusionFilter = {};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertEquals("", diagnosis.toHexString(0));
    }
    
    @Test
    public void testEquals() {
        Throwable th = new Throwable();
        String[] inclusionFilter = {};
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th, inclusionFilter);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th, inclusionFilter);
        assertEquals(true, diagnosis1.equals(diagnosis2));
    }
    
    @Test
    public void testHashCode() {
        Throwable th = new Throwable();
        String[] inclusionFilter = {};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertEquals(0, diagnosis.hashCode());
    }
    
    @Test
    public void testToString() {
        Throwable th = new Throwable();
        String[] inclusionFilter = {};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", diagnosis.toString());
    }  
}
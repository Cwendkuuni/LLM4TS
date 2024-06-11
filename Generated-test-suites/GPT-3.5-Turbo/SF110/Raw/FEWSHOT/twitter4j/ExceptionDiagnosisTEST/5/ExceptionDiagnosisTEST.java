package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ExceptionDiagnosisTest {
    @Test
    public void testConstructorWithThrowable() {
        Throwable throwable = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertEquals(throwable.getStackTrace().length, diagnosis.getStackLineHash());
        assertNull(diagnosis.getLineNumberHash());
    }

    @Test
    public void testConstructorWithThrowableAndInclusionFilter() {
        Throwable throwable = new Throwable();
        String[] filter = {"twitter4j."};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, filter);
        assertEquals(throwable.getStackTrace().length, diagnosis.getStackLineHash());
        assertNull(diagnosis.getLineNumberHash());
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertEquals(throwable.getStackTrace().length, diagnosis.getStackLineHash());
    }
    
    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertNull(diagnosis.getLineNumberHash());
    }
    
    @Test
    public void testAsHexString() {
        Throwable throwable = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertNotEquals("", diagnosis.asHexString());
    }
    
    @Test
    public void testEquals() {
        Throwable throwable1 = new Throwable();
        Throwable throwable2 = new Throwable();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable2);
        assertEquals(diagnosis1, diagnosis1);
        assertNotEquals(diagnosis1, diagnosis2);
    }
    
    @Test
    public void testHashCode() {
        Throwable throwable = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertNotEquals(0, diagnosis.hashCode());
    }
    
    @Test
    public void testToString() {
        Throwable throwable = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertNotEquals("", diagnosis.toString());
    }
}
package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructor1() {
        Throwable th = new Throwable();
        ExceptionDiagnosis ed = new ExceptionDiagnosis(th);
        assertEquals(0, ed.getStackLineHash());
        assertEquals(0, ed.getLineNumberHash());
        assertEquals("", ed.asHexString());
    }

    @Test
    public void testConstructor2() {
        Throwable cause = new Throwable();
        Throwable th = new Throwable(cause);
        String[] inclusionFilter = {"twitter4j."};
        ExceptionDiagnosis ed = new ExceptionDiagnosis(th, inclusionFilter);
        assertEquals(0, ed.getStackLineHash());
        assertEquals(0, ed.getLineNumberHash());
        assertEquals("", ed.asHexString());
    }

    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis ed = new ExceptionDiagnosis(th);
        assertEquals(0, ed.getStackLineHash());
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis ed = new ExceptionDiagnosis(th);
        assertEquals(0, ed.getLineNumberHash());
    }

    @Test
    public void testAsHexString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis ed = new ExceptionDiagnosis(th);
        assertEquals("", ed.asHexString());
    }

    @Test
    public void testEquals() {
        Throwable th1 = new Throwable();
        Throwable th2 = new Throwable();
        ExceptionDiagnosis ed1 = new ExceptionDiagnosis(th1);
        ExceptionDiagnosis ed2 = new ExceptionDiagnosis(th2);
        assertFalse(ed1.equals(null));
        assertTrue(ed1.equals(ed1));
        assertFalse(ed1.equals(ed2));
        assertFalse(ed2.equals(ed1));
    }

    @Test
    public void testHashCode() {
        Throwable th = new Throwable();
        ExceptionDiagnosis ed = new ExceptionDiagnosis(th);
        assertEquals(0, ed.hashCode());
    }
    
    @Test
    public void testToString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis ed = new ExceptionDiagnosis(th);
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", ed.toString());
    }
}
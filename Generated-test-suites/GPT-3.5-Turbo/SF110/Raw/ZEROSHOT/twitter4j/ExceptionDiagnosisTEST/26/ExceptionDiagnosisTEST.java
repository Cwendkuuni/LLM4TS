import org.junit.*;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructor_Throwable() {
        Throwable th = new Throwable("Testing Throwable");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(th);
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", ed.toString());
    }

    @Test
    public void testConstructor_ThrowableAndInclusionFilter() {
        Throwable th = new Throwable("Testing Throwable");
        String[] inclusionFilter = {"package1", "package2"};
        ExceptionDiagnosis ed = new ExceptionDiagnosis(th, inclusionFilter);
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", ed.toString());
    }

    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable("Testing Throwable");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(th);
        assertEquals(0, ed.getStackLineHash());
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable("Testing Throwable");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(th);
        assertEquals(0, ed.getLineNumberHash());
    }

    @Test
    public void testAsHexString() {
        Throwable th = new Throwable("Testing Throwable");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(th);
        assertEquals("", ed.asHexString());
    }

    @Test
    public void testEquals_SameObject() {
        Throwable th = new Throwable("Testing Throwable");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(th);
        assertTrue(ed.equals(ed));
    }

    @Test
    public void testEquals_NullObject() {
        Throwable th = new Throwable("Testing Throwable");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(th);
        assertFalse(ed.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        Throwable th = new Throwable("Testing Throwable");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(th);
        assertFalse(ed.equals(new Object()));
    }

    @Test
    public void testEquals_SameLineAndStackHash() {
        Throwable th1 = new Throwable("Testing Throwable");
        ExceptionDiagnosis ed1 = new ExceptionDiagnosis(th1);
        Throwable th2 = new Throwable("Testing Throwable");
        ExceptionDiagnosis ed2 = new ExceptionDiagnosis(th2);
        assertTrue(ed1.equals(ed2));
    }

    @Test
    public void testEquals_DifferentLineHash() {
        Throwable th1 = new Throwable("Testing Throwable");
        ExceptionDiagnosis ed1 = new ExceptionDiagnosis(th1);
        Throwable th2 = new Throwable("Testing Throwable");
        ed2.lineNumberHash = 123;
        ExceptionDiagnosis ed2 = new ExceptionDiagnosis(th2);
        assertFalse(ed1.equals(ed2));
    }

    @Test
    public void testHashCode() {
        Throwable th = new Throwable("Testing Throwable");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(th);
        assertEquals(0, ed.hashCode());
    }
}
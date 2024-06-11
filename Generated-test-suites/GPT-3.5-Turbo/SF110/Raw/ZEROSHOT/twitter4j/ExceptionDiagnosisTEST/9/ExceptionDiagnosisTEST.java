import static org.junit.Assert.*;
import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructor() {
        Throwable th = new Throwable("Test Exception");
        String[] inclusionFilter = new String[]{"twitter4j.TestClass"};

        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);

        assertNotNull(diagnosis);
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("", diagnosis.asHexString());
    }

    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable("Test Exception");
        String[] inclusionFilter = new String[]{"twitter4j.TestClass"};

        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);

        assertEquals(0, diagnosis.getStackLineHash());
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable("Test Exception");
        String[] inclusionFilter = new String[]{"twitter4j.TestClass"};

        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);

        assertEquals(0, diagnosis.getLineNumberHash());
    }

    @Test
    public void testAsHexString() {
        Throwable th = new Throwable("Test Exception");
        String[] inclusionFilter = new String[]{"twitter4j.TestClass"};

        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);

        assertEquals("", diagnosis.asHexString());
    }

    @Test
    public void testEquals() {
        Throwable th1 = new Throwable("Test Exception");
        String[] inclusionFilter1 = new String[]{"twitter4j.TestClass"};

        Throwable th2 = new Throwable("Test Exception");
        String[] inclusionFilter2 = new String[]{"twitter4j.AnotherTestClass"};

        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th1, inclusionFilter1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th2, inclusionFilter2);

        assertTrue(diagnosis1.equals(diagnosis1));
        assertFalse(diagnosis1.equals(null));
        assertFalse(diagnosis1.equals("Test String"));
        assertFalse(diagnosis1.equals(diagnosis2));
    }

    @Test
    public void testHashCode() {
        Throwable th = new Throwable("Test Exception");
        String[] inclusionFilter = new String[]{"twitter4j.TestClass"};

        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);

        int expectedHashCode = 31 * diagnosis.getStackLineHash() + diagnosis.getLineNumberHash();
        assertEquals(expectedHashCode, diagnosis.hashCode());
    }

    @Test
    public void testToString() {
        Throwable th = new Throwable("Test Exception");
        String[] inclusionFilter = new String[]{"twitter4j.TestClass"};

        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);

        String expectedString = "ExceptionDiagnosis{stackLineHash=" + diagnosis.getStackLineHash() + ", lineNumberHash=" + diagnosis.getLineNumberHash() + '}';
        assertEquals(expectedString, diagnosis.toString());
    }
}
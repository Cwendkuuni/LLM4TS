
import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructor() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals("", diagnosis.asHexString());
        assertNull(diagnosis.th.getCause());

        String[] inclusionFilter = {"twitter4j"};
        ExceptionDiagnosis diagnosisWithFilter = new ExceptionDiagnosis(th, inclusionFilter);
        assertEquals("", diagnosisWithFilter.asHexString());
        assertNull(diagnosisWithFilter.th.getCause());
    }

    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, diagnosis.getStackLineHash());

        String[] inclusionFilter = {"twitter4j"};
        ExceptionDiagnosis diagnosisWithFilter = new ExceptionDiagnosis(th, inclusionFilter);
        assertEquals(0, diagnosisWithFilter.getStackLineHash());
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals(0, diagnosis.getLineNumberHash());

        String[] inclusionFilter = {"twitter4j"};
        ExceptionDiagnosis diagnosisWithFilter = new ExceptionDiagnosis(th, inclusionFilter);
        assertEquals(0, diagnosisWithFilter.getLineNumberHash());
    }

    @Test
    public void testEquals() {
        Throwable th1 = new Throwable();
        Throwable th2 = new Throwable();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th2);
        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(th1);

        assertFalse(diagnosis1.equals(diagnosis2));
        assertTrue(diagnosis1.equals(diagnosis3));
        assertFalse(diagnosis1.equals(null));
    }

    @Test
    public void testHashCode() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        int expectedHashCode = th.hashCode();
        assertEquals(expectedHashCode, diagnosis.hashCode());
    }

    @Test
    public void testToString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        String expectedToString = "ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}";
        assertEquals(expectedToString, diagnosis.toString());
    }
}

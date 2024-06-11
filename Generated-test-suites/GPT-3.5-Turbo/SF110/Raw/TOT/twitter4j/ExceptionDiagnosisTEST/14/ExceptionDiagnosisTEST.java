import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructorWithThrowable() {
        Throwable throwable = mock(Throwable.class);
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertNotNull(diagnosis);
    }

    @Test
    public void testConstructorWithThrowableAndInclusionFilter() {
        Throwable throwable = mock(Throwable.class);
        String[] inclusionFilter = {"filter1", "filter2"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        assertNotNull(diagnosis);
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = mock(Throwable.class);
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        int stackLineHash = diagnosis.getStackLineHash();
        assertEquals(0, stackLineHash);
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = mock(Throwable.class);
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        int lineNumberHash = diagnosis.getLineNumberHash();
        assertEquals(0, lineNumberHash);
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = mock(Throwable.class);
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        String hexString = diagnosis.asHexString();
        assertNotNull(hexString);
        assertTrue(hexString.length() > 0);
    }

    @Test
    public void testEquals() {
        Throwable throwable1 = mock(Throwable.class);
        Throwable throwable2 = mock(Throwable.class);
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable2);
        assertFalse(diagnosis1.equals(diagnosis2));
    }

    @Test
    public void testHashCode() {
        Throwable throwable = mock(Throwable.class);
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        int hashCode = diagnosis.hashCode();
        assertNotNull(hashCode);
    }

    @Test
    public void testToString() {
        Throwable throwable = mock(Throwable.class);
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        String toString = diagnosis.toString();
        assertNotNull(toString);
        assertTrue(toString.length() > 0);
    }

}
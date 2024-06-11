import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructor_WithThrowable() {
        Throwable exception = new RuntimeException();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(exception);

        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("00000000-00000000", diagnosis.asHexString());
    }

    @Test
    public void testConstructor_WithThrowableAndInclusionFilter() {
        Throwable exception = new IllegalArgumentException();
        String[] inclusionFilter = { "java.lang." };
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(exception, inclusionFilter);

        assertEquals(2661555842, diagnosis.getStackLineHash());
        assertEquals(1076714033, diagnosis.getLineNumberHash());
        assertEquals("9EC6B802-4013E361", diagnosis.asHexString());
    }

    @Test
    public void testEquals_SameObject() {
        Throwable exception = new IllegalArgumentException();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(exception);
        ExceptionDiagnosis diagnosis2 = diagnosis1;

        assertTrue(diagnosis1.equals(diagnosis2));
    }

    @Test
    public void testEquals_NullObject() {
        Throwable exception = new NullPointerException();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(exception);
        ExceptionDiagnosis diagnosis2 = null;

        assertFalse(diagnosis1.equals(diagnosis2));
    }

    @Test
    public void testEquals_DifferentClass() {
        Throwable exception = new ArrayIndexOutOfBoundsException();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(exception);
        String diagnosis2 = "diagnosis";

        assertFalse(diagnosis1.equals(diagnosis2));
    }

    @Test
    public void testEquals_SameAttributes() {
        Throwable exception = new IllegalStateException();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(exception);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(exception);

        assertTrue(diagnosis1.equals(diagnosis2));
    }

    @Test
    public void testHashCode_EqualsObjects() {
        Throwable exception = new IllegalArgumentException();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(exception);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(exception);

        assertEquals(diagnosis1.hashCode(), diagnosis2.hashCode());
    }

    @Test
    public void testToString() {
        Throwable exception = new Exception();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(exception);

        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", diagnosis.toString());
    }

}
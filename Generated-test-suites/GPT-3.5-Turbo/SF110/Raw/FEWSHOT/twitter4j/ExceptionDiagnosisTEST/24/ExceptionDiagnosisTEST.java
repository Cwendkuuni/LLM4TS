package twitter4j;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosis() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals(th.getStackTrace().length, diagnosis.getStackLineHash());

        String[] inclusionFilter = {"filter"};
        diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertEquals(th.getStackTrace().length, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());

        Throwable cause = new Throwable();
        diagnosis = new ExceptionDiagnosis(cause, inclusionFilter);
        assertEquals(cause.getStackTrace().length, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
    }

    @Test
    public void testAsHexString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals("", diagnosis.asHexString());

        String[] inclusionFilter = {"filter"};
        diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertEquals("", diagnosis.asHexString());

        Throwable cause = new Throwable();
        diagnosis = new ExceptionDiagnosis(cause, inclusionFilter);
        assertEquals("", diagnosis.asHexString());
    }

    @Test
    public void testEquals() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th);
        assertEquals(true, diagnosis1.equals(diagnosis2));

        String[] inclusionFilter = {"filter"};
        diagnosis1 = new ExceptionDiagnosis(th, inclusionFilter);
        diagnosis2 = new ExceptionDiagnosis(th, inclusionFilter);
        assertEquals(true, diagnosis1.equals(diagnosis2));

        Throwable cause = new Throwable();
        diagnosis1 = new ExceptionDiagnosis(cause, inclusionFilter);
        diagnosis2 = new ExceptionDiagnosis(cause, inclusionFilter);
        assertEquals(true, diagnosis1.equals(diagnosis2));
    }

    @Test
    public void testHashCode() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th);
        assertEquals(diagnosis1.hashCode(), diagnosis2.hashCode());

        String[] inclusionFilter = {"filter"};
        diagnosis1 = new ExceptionDiagnosis(th, inclusionFilter);
        diagnosis2 = new ExceptionDiagnosis(th, inclusionFilter);
        assertEquals(diagnosis1.hashCode(), diagnosis2.hashCode());

        Throwable cause = new Throwable();
        diagnosis1 = new ExceptionDiagnosis(cause, inclusionFilter);
        diagnosis2 = new ExceptionDiagnosis(cause, inclusionFilter);
        assertEquals(diagnosis1.hashCode(), diagnosis2.hashCode());
    }

    @Test
    public void testToString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", diagnosis.toString());

        String[] inclusionFilter = {"filter"};
        diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", diagnosis.toString());

        Throwable cause = new Throwable();
        diagnosis = new ExceptionDiagnosis(cause, inclusionFilter);
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", diagnosis.toString());
    }
}
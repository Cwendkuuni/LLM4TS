
package twitter4j;

import org.junit.*;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructorWithOneParam() {
        Throwable exception = new Exception("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(exception);
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("", diagnosis.asHexString());
    }

    @Test
    public void testExceptionDiagnosisConstructorWithTwoParams() {
        Throwable exception = new Exception("Test Exception");
        String[] inclusionFilter = {"twitter4j."};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(exception, inclusionFilter);
        assertEquals(-1620060117, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("ffffff63-0", diagnosis.asHexString());
    }

    @Test
    public void testExceptionDiagnosisEquals() {
        Throwable exception1 = new Exception("Test Exception");
        Throwable exception2 = new Exception("Test Exception");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(exception1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(exception2);
        assertTrue(diagnosis1.equals(diagnosis1));
        assertFalse(diagnosis1.equals(null));
        assertFalse(diagnosis1.equals(exception1));
        assertTrue(diagnosis1.equals(diagnosis2));
    }

    @Test
    public void testExceptionDiagnosisHashCode() {
        Throwable exception = new Exception("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(exception);
        int expectedHashCode = 0;
        assertEquals(expectedHashCode, diagnosis.hashCode());
    }

    @Test
    public void testExceptionDiagnosisToString() {
        Throwable exception = new Exception("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(exception);
        String expectedToString = "ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}";
        assertEquals(expectedToString, diagnosis.toString());
    }

}


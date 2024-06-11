
package twitter4j;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    // Test case for the first constructor
    @Test
    public void testExceptionDiagnosisConstructor1() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertNotNull(diagnosis);
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("00000000-00000000", diagnosis.asHexString());
    }

    // Test case for the second constructor with empty inclusion filter
    @Test
    public void testExceptionDiagnosisConstructor2EmptyFilter() {
        Throwable th = new Throwable();
        String[] inclusionFilter = {};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertNotNull(diagnosis);
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("00000000-00000000", diagnosis.asHexString());
    }

    // Test case for the second constructor with non-empty inclusion filter and matching stack trace elements
    @Test
    public void testExceptionDiagnosisConstructor2NonEmptyFilterMatchingElements() {
        Throwable th = new Throwable();
        StackTraceElement[] stackTrace = {
                new StackTraceElement("twitter4j.ExampleClass", "method1", "ExampleClass.java", 10),
                new StackTraceElement("twitter4j.ExampleClass", "method2", "ExampleClass.java", 20)
        };
        th.setStackTrace(stackTrace);

        String[] inclusionFilter = {"twitter4j."};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertNotNull(diagnosis);
        assertEquals(-1973719134, diagnosis.getStackLineHash());
        assertEquals(-1155484574, diagnosis.getLineNumberHash());
        assertEquals("8A1863B2-8B4B8FCA", diagnosis.asHexString());
    }

    // Test case for the second constructor with non-empty inclusion filter and non-matching stack trace elements
    @Test
    public void testExceptionDiagnosisConstructor2NonEmptyFilterNonMatchingElements() {
        Throwable th = new Throwable();
        StackTraceElement[] stackTrace = {
                new StackTraceElement("twitter4j.ExampleClass", "method1", "ExampleClass.java", 10),
                new StackTraceElement("other.Class", "method2", "OtherClass.java", 20)
        };
        th.setStackTrace(stackTrace);

        String[] inclusionFilter = {"twitter4j."};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
        assertNotNull(diagnosis);
        assertEquals(1176649853, diagnosis.getStackLineHash());
        assertEquals(-152019230, diagnosis.getLineNumberHash());
        assertEquals("4586DF6D-F9FEF586", diagnosis.asHexString());
    }

    // Test case for getCause method when there is a cause
    @Test
    public void testGetCauseWithCause() {
        Throwable cause = new Throwable("This is the cause");
        Throwable th = new Throwable(cause);
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertNotNull(diagnosis);
        assertTrue(diagnosis.asHexString().contains(new ExceptionDiagnosis(cause).asHexString()));
    }

    // Test case for getCause method when there is no cause
    @Test
    public void testGetCauseWithoutCause() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertNotNull(diagnosis);
        assertFalse(diagnosis.asHexString().contains(new ExceptionDiagnosis(new Throwable()).asHexString()));
    }

    // Test case for equals method when objects are the same
    @Test
    public void testEqualsSameObject() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th);
        assertTrue(diagnosis1.equals(diagnosis1));
    }

    // Test case for equals method when objects are different class
    @Test
    public void testEqualsDifferentClass() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th);
        assertFalse(diagnosis1.equals(new Object()));
    }

    // Test case for equals method when objects have same stack line and line number hash
    @Test
    public void testEqualsSameHashes() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th);
        assertTrue(diagnosis1.equals(diagnosis2));
    }

    // Test case for equals method when objects have different stack line hash
    @Test
    public void testEqualsDifferentStackLineHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th);
        Throwable th2 = new Throwable();
        StackTraceElement[] stackTrace = {
                new StackTraceElement("twitter4j.ExampleClass", "method1", "ExampleClass.java", 10)
        };
        th2.setStackTrace(stackTrace);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th2);
        assertFalse(diagnosis1.equals(diagnosis2));
    }

    // Test case for equals method when objects have different line number hash
    @Test
    public void testEqualsDifferentLineNumberHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th);
        Throwable th2 = new Throwable();
        StackTraceElement[] stackTrace = {
                new StackTraceElement("twitter4j.ExampleClass", "method1", "ExampleClass.java", 20)
        };
        th2.setStackTrace(stackTrace);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th2);
        assertFalse(diagnosis1.equals(diagnosis2));
    }

    // Test case for hashCode method when objects are the same
    @Test
    public void testHashCodeSameObject() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th);
        assertEquals(diagnosis1.hashCode(), diagnosis1.hashCode());
    }

    // Test case for hashCode method when objects have same stack line and line number hash
    @Test
    public void testHashCodeSameHashes() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th);
        assertEquals(diagnosis1.hashCode(), diagnosis2.hashCode());
    }

    // Test case for hashCode method when objects have different stack line hash
    @Test
    public void testHashCodeDifferentStackLineHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th);
        Throwable th2 = new Throwable();
        StackTraceElement[] stackTrace = {
                new StackTraceElement("twitter4j.ExampleClass", "method1", "ExampleClass.java", 10)
        };
        th2.setStackTrace(stackTrace);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th2);
        assertNotEquals(diagnosis1.hashCode(), diagnosis2.hashCode());
    }

    // Test case for hashCode method when objects have different line number hash
    @Test
    public void testHashCodeDifferentLineNumberHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th);
        Throwable th2 = new Throwable();
        StackTraceElement[] stackTrace = {
                new StackTraceElement("twitter4j.ExampleClass", "method1", "ExampleClass.java", 20)
        };
        th2.setStackTrace(stackTrace);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th2);
        assertNotEquals(diagnosis1.hashCode(), diagnosis2.hashCode());
    }

    // Test case for toString method
    @Test
    public void testToString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertNotNull(diagnosis.toString());
    }
}


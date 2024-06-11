import static org.junit.Assert.*;
import org.junit.*;

public class ExceptionDiagnosisTest {

    ExceptionDiagnosis exceptionDiagnosis;
    Throwable throwable;
    String[] inclusionFilter;

    @Before
    public void setup() {
        throwable = new Throwable("Test Throwable");
        inclusionFilter = new String[]{"twitter4j."};
    }

    @Test
    public void testExceptionDiagnosis_Constructor_WithThrowable() {
        exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertNotNull(exceptionDiagnosis);
        assertEquals("", exceptionDiagnosis.asHexString());
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
    }

    @Test
    public void testExceptionDiagnosis_Constructor_WithThrowableAndInclusionFilter() {
        exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        assertNotNull(exceptionDiagnosis);
        assertEquals("", exceptionDiagnosis.asHexString());
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
    }

    @Test
    public void testGetStackLineHash_WithoutSettingStackLineHash() {
        exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int expected = exceptionDiagnosis.toHashString(throwable).hashCode();
        assertEquals(expected, exceptionDiagnosis.getStackLineHash());
    }

    @Test
    public void testGetLineNumberHash_WithoutSettingLineNumberHash() {
        exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int expected = exceptionDiagnosis.toHashString(throwable).hashCode();
        assertEquals(expected, exceptionDiagnosis.getLineNumberHash());
    }

    @Test
    public void testAsHexString_WithoutSettingStackLineHashAndLineNumberHash() {
        exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertEquals("", exceptionDiagnosis.asHexString());
    }

    @Test
    public void testToHexString() {
        exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int value = 12345;
        String expected = "00003039";
        assertEquals(expected, exceptionDiagnosis.toHexString(value));
    }

    @Test
    public void testEquals_WithEqualObjects() {
        exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        ExceptionDiagnosis anotherExceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertTrue(exceptionDiagnosis.equals(anotherExceptionDiagnosis));
    }

    @Test
    public void testEquals_WithDifferentObjects() {
        exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        Throwable anotherThrowable = new Throwable("Another Test Throwable");
        ExceptionDiagnosis anotherExceptionDiagnosis = new ExceptionDiagnosis(anotherThrowable);
        assertFalse(exceptionDiagnosis.equals(anotherExceptionDiagnosis));
    }

    @Test
    public void testHashCode() {
        exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int expected = 31 * 0 + 0;
        assertEquals(expected, exceptionDiagnosis.hashCode());
    }

    @Test
    public void testToString() {
        exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        String expected = "ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}";
        assertEquals(expected, exceptionDiagnosis.toString());
    }

    @Test
    public void testExceptionDiagnosis_Constructor_WithThrowableAndInclusionFilter_WithMatchingStackTraceElement() {
        throwable.setStackTrace(new StackTraceElement[]{new StackTraceElement("twitter4j.TestClass", "testMethod", "TestClass.java", 10)});
        exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        assertNotNull(exceptionDiagnosis);
        int expectedHash = "twitter4j.TestClass".hashCode() + "testMethod".hashCode();
        assertEquals(expectedHash, exceptionDiagnosis.getStackLineHash());
        assertEquals(10, exceptionDiagnosis.getLineNumberHash());
        assertEquals("a64c6f85-0000000a", exceptionDiagnosis.asHexString());
    }

    @Test
    public void testExceptionDiagnosis_Constructor_WithThrowableAndInclusionFilter_WithoutMatchingStackTraceElement() {
        throwable.setStackTrace(new StackTraceElement[]{new StackTraceElement("com.example.AnotherClass", "anotherMethod", "AnotherClass.java", 5)});
        exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        assertNotNull(exceptionDiagnosis);
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
        assertEquals("00000000-00000000", exceptionDiagnosis.asHexString());
    }

    @Test
    public void testExceptionDiagnosis_Constructor_WithThrowableAndInclusionFilter_WithNestedCause() {
        Throwable cause = new Throwable("Cause Throwable");
        throwable.initCause(cause);
        exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        assertNotNull(exceptionDiagnosis);
        assertTrue(exceptionDiagnosis.asHexString().contains(new ExceptionDiagnosis(cause, inclusionFilter).asHexString()));
    }
}
import static org.junit.Assert.*;
import org.junit.Test;

public class ExceptionDiagnosisTest {

    // Test case for ExceptionDiagnosis Constructor 1
    @Test
    public void testExceptionDiagnosisConstructor1() {
        Throwable throwable = new Throwable();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int expectedStackLineHash = 0;
        int expectedLineNumberHash = 0;
        assertEquals(expectedStackLineHash, exceptionDiagnosis.getStackLineHash());
        assertEquals(expectedLineNumberHash, exceptionDiagnosis.getLineNumberHash());
    }
    
    // Test case for ExceptionDiagnosis Constructor 2
    @Test
    public void testExceptionDiagnosisConstructor2() {
        Throwable throwable = new Throwable();
        String[] inclusionFilter = {"test"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        int expectedStackLineHash = 0;
        int expectedLineNumberHash = 0;
        assertEquals(expectedStackLineHash, exceptionDiagnosis.getStackLineHash());
        assertEquals(expectedLineNumberHash, exceptionDiagnosis.getLineNumberHash());
    }
    
    // Test case for asHexString()
    @Test
    public void testAsHexString() {
        Throwable throwable = new Throwable();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        String expectedHexString = "";
        assertEquals(expectedHexString, exceptionDiagnosis.asHexString());
    }
    
    // Test case for toHexString()
    @Test
    public void testToHexString() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new Throwable());
        int value = 10;
        String expectedHexString = "0000000a";
        assertEquals(expectedHexString, exceptionDiagnosis.toHexString(value));
    }
    
    // Test case for equals()
    @Test
    public void testEquals() {
        Throwable throwable1 = new Throwable();
        Throwable throwable2 = new Throwable();
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);
        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis1));
        assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));
        assertFalse(exceptionDiagnosis1.equals(null));
        assertFalse(exceptionDiagnosis1.equals(new Object()));
    }
    
    // Test case for hashCode()
    @Test
    public void testHashCode() {
        Throwable throwable = new Throwable();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int expectedHashCode = exceptionDiagnosis.getStackLineHash() * 31 + exceptionDiagnosis.getLineNumberHash();
        assertEquals(expectedHashCode, exceptionDiagnosis.hashCode());
    }
    
    // Test case for toString()
    @Test
    public void testToString() {
        Throwable throwable = new Throwable();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        String expectedToString = "ExceptionDiagnosis{stackLineHash=" + exceptionDiagnosis.getStackLineHash() +
                ", lineNumberHash=" + exceptionDiagnosis.getLineNumberHash() + "}";
        assertEquals(expectedToString, exceptionDiagnosis.toString());
    }
}
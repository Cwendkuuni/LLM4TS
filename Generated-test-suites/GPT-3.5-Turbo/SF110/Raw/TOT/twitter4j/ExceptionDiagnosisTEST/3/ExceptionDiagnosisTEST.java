import org.junit.*;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {
    
    @Test
    public void testConstructorWithThrowable() {
        try {
            throw new NullPointerException();
        } catch (NullPointerException e) {
            ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(e);
            assertNotNull(diagnosis);
            assertEquals(0, diagnosis.getStackLineHash());
            assertEquals(0, diagnosis.getLineNumberHash());
        }
    }
    
    @Test
    public void testConstructorWithThrowableAndInclusionFilter() {
        try {
            throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            String[] inclusionFilter = {"java."};
            ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(e, inclusionFilter);
            assertNotNull(diagnosis);
            assertEquals(0, diagnosis.getStackLineHash());
            assertEquals(0, diagnosis.getLineNumberHash());
        }
    }
    
    @Test
    public void testAsHexString() {
        try {
            throw new ArrayIndexOutOfBoundsException();
        } catch (ArrayIndexOutOfBoundsException e) {
            ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(e);
            assertNotNull(diagnosis);
            assertEquals("00000000-00000000", diagnosis.asHexString());
        }
    }
    
    @Test
    public void testEquals() {
        try {
            throw new IllegalStateException();
        } catch (IllegalStateException e) {
            ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(e);
            ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(e);
            assertTrue(diagnosis1.equals(diagnosis2));
        }
    }
    
    @Test
    public void testHashCode() {
        try {
            throw new UnsupportedOperationException();
        } catch (UnsupportedOperationException e) {
            ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(e);
            ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(e);
            assertEquals(diagnosis1.hashCode(), diagnosis2.hashCode());
        }
    }
    
    @Test
    public void testToString() {
        try {
            throw new RuntimeException();
        } catch (RuntimeException e) {
            ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(e);
            assertNotNull(diagnosis);
            assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", diagnosis.toString());
        }
    }
}
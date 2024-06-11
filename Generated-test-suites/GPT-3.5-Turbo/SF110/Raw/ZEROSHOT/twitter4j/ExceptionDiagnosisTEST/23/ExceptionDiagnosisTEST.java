import org.junit.*;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {
    
    @Test
    public void testConstructor1() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new NullPointerException());
        assertNotNull(diagnosis);
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
    }
    
    @Test
    public void testConstructor2() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new IllegalArgumentException(), new String[]{"java.lang"});
        assertNotNull(diagnosis);
        assertNotEquals(0, diagnosis.getStackLineHash());
        assertNotEquals(0, diagnosis.getLineNumberHash());
    }
    
    @Test
    public void testAsHexString() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new IllegalStateException());
        String hexString = diagnosis.asHexString();
        assertNotNull(hexString);
        assertEquals(16, hexString.length());
    }
    
    @Test
    public void testEquals() {
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(new Exception());
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(new Exception());
        assertTrue(diagnosis1.equals(diagnosis2));
    }
    
    @Test
    public void testHashCode() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new Exception());
        int hashCode = diagnosis.hashCode();
        assertEquals(hashCode, diagnosis.getStackLineHash() * 31 + diagnosis.getLineNumberHash());
    }
    
    @Test
    public void testToString() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new IndexOutOfBoundsException());
        String str = diagnosis.toString();
        assertNotNull(str);
    }
}
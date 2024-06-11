
import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testGetStackLineHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        int expectedHash = 0;
        int actualHash = diagnosis.getStackLineHash();
        
        assertEquals(expectedHash, actualHash);
    }
    
    @Test
    public void testGetLineNumberHash() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        int expectedHash = 0;
        int actualHash = diagnosis.getLineNumberHash();
        
        assertEquals(expectedHash, actualHash);
    }
    
    @Test
    public void testAsHexString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        String expectedHex = "";
        String actualHex = diagnosis.asHexString();
        
        assertEquals(expectedHex, actualHex);
    }
    
    @Test
    public void testEquals() {
        Throwable th1 = new Throwable();
        Throwable th2 = new Throwable();
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th2);
        
        boolean expectedEquals = true;
        boolean actualEquals = diagnosis1.equals(diagnosis1);
        
        assertEquals(expectedEquals, actualEquals);
        
        expectedEquals = false;
        actualEquals = diagnosis1.equals(null);
        
        assertEquals(expectedEquals, actualEquals);
        
        expectedEquals = false;
        actualEquals = diagnosis1.equals(diagnosis2);
        
        assertEquals(expectedEquals, actualEquals);
    }
    
    @Test
    public void testHashCode() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        int expectedHashCode = diagnosis.getStackLineHash() * 31 + diagnosis.getLineNumberHash();
        int actualHashCode = diagnosis.hashCode();
        
        assertEquals(expectedHashCode, actualHashCode);
    }
    
    @Test
    public void testToString() {
        Throwable th = new Throwable();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        
        String expectedString = "ExceptionDiagnosis{stackLineHash=" + diagnosis.getStackLineHash() + ", lineNumberHash=" + diagnosis.getLineNumberHash() + '}';
        String actualString = diagnosis.toString();
        
        assertEquals(expectedString, actualString);
    }
}

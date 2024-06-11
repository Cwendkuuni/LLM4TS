import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class ExceptionDiagnosisTest {
  
  @Test
  public void testConstructor() {
    Throwable th = new Throwable("Test Exception");
    ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
    assertSame(th, diagnosis.th);
    assertEquals("", diagnosis.hexString);
    assertEquals(0, diagnosis.stackLineHash);
    assertEquals(0, diagnosis.lineNumberHash);
  }
  
  @Test
  public void testConstructorWithFilter() {
    Throwable th = new Throwable("Test Exception");
    String[] inclusionFilter = {"org.junit."};
    ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
    assertSame(th, diagnosis.th);
    assertEquals("", diagnosis.hexString);
    assertEquals(0, diagnosis.stackLineHash);
    assertEquals(0, diagnosis.lineNumberHash);
  }
  
  @Test
  public void testGetStackLineHash() {
    Throwable th = new Throwable("Test Exception");
    ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
    diagnosis.stackLineHash = 123456789;
    assertEquals(123456789, diagnosis.getStackLineHash());
  }
  
  @Test
  public void testGetLineNumberHash() {
    Throwable th = new Throwable("Test Exception");
    ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
    diagnosis.lineNumberHash = 987654321;
    assertEquals(987654321, diagnosis.getLineNumberHash());
  }
  
  @Test
  public void testAsHexString() {
    Throwable th = new Throwable("Test Exception");
    ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
    diagnosis.hexString = "abcdef";
    assertEquals("abcdef", diagnosis.asHexString());
  }
  
  @Test
  public void testToHexString() {
    Throwable th = new Throwable("Test Exception");
    ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
    String hexString = diagnosis.toHexString(123456789);
    assertEquals("075bcd15", hexString);
  }
  
  @Test
  public void testEquals() {
    Throwable th1 = new Throwable("Test Exception 1");
    Throwable th2 = new Throwable("Test Exception 2");
    ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th1);
    ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th2);
    diagnosis1.stackLineHash = 123456789;
    diagnosis1.lineNumberHash = 987654321;
    diagnosis2.stackLineHash = 123456789;
    diagnosis2.lineNumberHash = 987654321;
    assertTrue(diagnosis1.equals(diagnosis2));
  }
  
  @Test
  public void testHashCode() {
    Throwable th = new Throwable("Test Exception");
    ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
    diagnosis.stackLineHash = 123456789;
    diagnosis.lineNumberHash = 987654321;
    int hashCode = 31 * 123456789 + 987654321;
    assertEquals(hashCode, diagnosis.hashCode());
  }
  
  @Test
  public void testToString() {
    Throwable th = new Throwable("Test Exception");
    ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
    diagnosis.stackLineHash = 123456789;
    diagnosis.lineNumberHash = 987654321;
    String expectedString = "ExceptionDiagnosis{stackLineHash=123456789, lineNumberHash=987654321}";
    assertEquals(expectedString, diagnosis.toString());
  }
}
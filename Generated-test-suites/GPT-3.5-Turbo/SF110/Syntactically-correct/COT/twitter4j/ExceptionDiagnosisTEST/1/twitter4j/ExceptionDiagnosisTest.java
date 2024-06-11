
import static org.junit.Assert.*;
import org.junit.Test;

public class ExceptionDiagnosisTest {
  
  @Test
  public void testEquals() {
    ExceptionDiagnosis exception1 = new ExceptionDiagnosis(new Throwable());
    ExceptionDiagnosis exception2 = new ExceptionDiagnosis(new Throwable());
    assertTrue("Equals method should return true", exception1.equals(exception2));
  }
  
  @Test
  public void testEquals_sameInstance() {
    ExceptionDiagnosis exception = new ExceptionDiagnosis(new Throwable());
    assertTrue("Equals method should return true for the same instance", exception.equals(exception));
  }
  
  @Test
  public void testEquals_differentClass() {
    ExceptionDiagnosis exception = new ExceptionDiagnosis(new Throwable());
    assertFalse("Equals method should return false for different class", exception.equals(new Object()));
  }
  
  @Test
  public void testEquals_differentLineNumberHash() {
    ExceptionDiagnosis exception1 = new ExceptionDiagnosis(new Throwable());
    ExceptionDiagnosis exception2 = new ExceptionDiagnosis(new Throwable());
    exception2.lineNumberHash = 12345;
    assertFalse("Equals method should return false for different lineNumberHash", exception1.equals(exception2));
  }
  
  @Test
  public void testEquals_differentStackLineHash() {
    ExceptionDiagnosis exception1 = new ExceptionDiagnosis(new Throwable());
    ExceptionDiagnosis exception2 = new ExceptionDiagnosis(new Throwable());
    exception2.stackLineHash = 54321;
    assertFalse("Equals method should return false for different stackLineHash", exception1.equals(exception2));
  }
  
  @Test
  public void testToString() {
    ExceptionDiagnosis exception = new ExceptionDiagnosis(new Throwable());
    assertNotNull("toString method should return a non-null string", exception.toString());
  }
  
  @Test
  public void testGetStackLineHash() {
    ExceptionDiagnosis exception = new ExceptionDiagnosis(new Throwable());
    assertNotNull("getStackLineHash method should return a non-null value", exception.getStackLineHash());
  }
  
  @Test
  public void testGetLineNumberHash() {
    ExceptionDiagnosis exception = new ExceptionDiagnosis(new Throwable());
    assertNotNull("getLineNumberHash method should return a non-null value", exception.getLineNumberHash());
  }
  
  @Test
  public void testAsHexString() {
    ExceptionDiagnosis exception = new ExceptionDiagnosis(new Throwable());
    assertNotNull("asHexString method should return a non-null string", exception.asHexString());
  }
}

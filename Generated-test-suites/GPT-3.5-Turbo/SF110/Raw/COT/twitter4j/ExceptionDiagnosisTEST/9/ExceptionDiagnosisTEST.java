

import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {
  
  @Test
  public void testConstructor1() {
    Throwable th = new Throwable();
    ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
    assertNotNull(diagnosis);
    assertEquals(0, diagnosis.getStackLineHash());
    assertEquals(0, diagnosis.getLineNumberHash());
    assertEquals("00000000-00000000", diagnosis.asHexString());
  }
  
  @Test
  public void testConstructor2() {
    Throwable th = new Throwable();
    String[] inclusionFilter = {"twitter4j."};
    ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, inclusionFilter);
    assertNotNull(diagnosis);
    assertEquals(0, diagnosis.getStackLineHash());
    assertEquals(0, diagnosis.getLineNumberHash());
    assertEquals("00000000-00000000", diagnosis.asHexString());
  }
  
  @Test
  public void testGetStackLineHash() {
    Throwable th = new Throwable();
    ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
    assertEquals(0, diagnosis.getStackLineHash());
  }
  
  @Test
  public void testGetLineNumberHash() {
    Throwable th = new Throwable();
    ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
    assertEquals(0, diagnosis.getLineNumberHash());
  }
  
  @Test
  public void testAsHexString() {
    Throwable th = new Throwable();
    ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
    assertEquals("00000000-00000000", diagnosis.asHexString());
  }
  
  @Test
  public void testEquals() {
    Throwable th = new Throwable();
    ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th);
    ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th);
    assertTrue(diagnosis1.equals(diagnosis2));
  }
  
  @Test
  public void testHashCode() {
    Throwable th = new Throwable();
    ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
    assertEquals(0, diagnosis.hashCode());
  }
  
  @Test
  public void testToString() {
    Throwable th = new Throwable();
    ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
    assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", diagnosis.toString());
  }
  
}

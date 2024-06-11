package twitter4j;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

  @Test
  public void testConstructorWithThrowable() {
    Throwable throwable = new Throwable("Test Throwable");
    ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
    assertNotNull(exceptionDiagnosis);
    assertEquals(0, exceptionDiagnosis.getStackLineHash());
    assertEquals(0, exceptionDiagnosis.getLineNumberHash());
    assertEquals("00000000-00000000", exceptionDiagnosis.asHexString());
  }

  @Test
  public void testConstructorWithThrowableAndInclusionFilter() {
    Throwable throwable = new Throwable("Test Throwable");
    String[] inclusionFilter = {"twitter4j"};
    ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
    assertNotNull(exceptionDiagnosis);
    assertEquals(0, exceptionDiagnosis.getStackLineHash());
    assertEquals(0, exceptionDiagnosis.getLineNumberHash());
    assertEquals("00000000-00000000", exceptionDiagnosis.asHexString());
  }

  @Test
  public void testGetStackLineHash() {
    Throwable throwable = new Throwable("Test Throwable");
    ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
    assertEquals(0, exceptionDiagnosis.getStackLineHash());
  }

  @Test
  public void testGetLineNumberHash() {
    Throwable throwable = new Throwable("Test Throwable");
    ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
    assertEquals(0, exceptionDiagnosis.getLineNumberHash());
  }

  @Test
  public void testAsHexString() {
    Throwable throwable = new Throwable("Test Throwable");
    ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
    assertEquals("00000000-00000000", exceptionDiagnosis.asHexString());
  }

  @Test
  public void testEquals() {
    Throwable throwable1 = new Throwable("Test Throwable1");
    ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
    Throwable throwable2 = new Throwable("Test Throwable2");
    ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);
    assertEquals(exceptionDiagnosis1, exceptionDiagnosis1);
    assertNotEquals(exceptionDiagnosis1, exceptionDiagnosis2);
  }

  @Test
  public void testHashCode() {
    Throwable throwable = new Throwable("Test Throwable");
    ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
    assertEquals(0, exceptionDiagnosis.hashCode());
  }

  @Test
  public void testToString() {
    Throwable throwable = new Throwable("Test Throwable");
    ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
    assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", exceptionDiagnosis.toString());
  }
}
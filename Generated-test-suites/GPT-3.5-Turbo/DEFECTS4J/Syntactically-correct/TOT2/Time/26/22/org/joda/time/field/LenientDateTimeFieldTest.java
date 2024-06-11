import static org.junit.Assert.*;
import org.junit.Test;

public class LenientDateTimeFieldTest {
  LenientDateTimeField ldtf = new LenientDateTimeField();

  @Test
  public void testSet() {
    long instant = 123456;
    int value = 10;
    
    long result = ldtf.set(instant, value);
    
    assertEquals(result, instant + value);
  }

  @Test
  public void testIsLenient() {
    assertTrue(ldtf.isLenient());
  }

  @Test
  public void testGetInstance() {
    DateTimeField field = new DateTimeField();
    Chronology base = new Chronology();

    DateTimeField result = LenientDateTimeField.getInstance(field, base);
    
    assertNotNull(result);
  }

}
import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest {

  @Test
  public void testGetInstance() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    assertNotNull(field);
    assertEquals(DurationFieldType.seconds(), field.getType());
  }

  @Test
  public void testGetName() {
    UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.minutes());
    assertEquals("minutes", field.getName());
  }

  @Test
  public void testIsSupported() {
    UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.hours());
    assertFalse(field.isSupported());
  }

  @Test
  public void testIsPrecise() {
    UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.days());
    assertTrue(field.isPrecise());
  }

  // Additional test cases for remaining methods...

}
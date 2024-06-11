import org.joda.time.field.UnsupportedDurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

  @Test
  public void testGetType() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    assertEquals(DurationFieldType.SECONDS, field.getType());
  }

  @Test
  public void testGetName() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    assertEquals("Seconds", field.getName());
  }

  @Test
  public void testIsSupported() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    assertFalse(field.isSupported());
  }

  @Test
  public void testIsPrecise() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    assertTrue(field.isPrecise());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValue() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    field.getValue(1000);
  }
  
  // ... additional test methods for other methods in the UnsupportedDurationField class

}
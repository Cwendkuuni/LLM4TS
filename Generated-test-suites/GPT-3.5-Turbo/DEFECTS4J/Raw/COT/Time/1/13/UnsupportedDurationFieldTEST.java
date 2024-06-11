import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest {
  
  @Test
  public void testGetType() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
    assertEquals(DurationFieldType.YEARS, field.getType());
  }
  
  @Test
  public void testGetName() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MONTHS);
    assertEquals("Months", field.getName());
  }
  
  @Test
  public void testIsSupported() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
    assertEquals(false, field.isSupported());
  }
  
  @Test
  public void testIsPrecise() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
    assertEquals(true, field.isPrecise());
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetValue() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
    field.getValue(1000);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAsLong() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    field.getValueAsLong(2000);
  }
  
  // More test cases for other public methods
  
  @Test(expected = UnsupportedOperationException.class)
  public void testUnsupportedOperationMessage() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
    try {
      field.getValue(500);
    } catch (UnsupportedOperationException e) {
      assertEquals("Millis field is unsupported", e.getMessage());
    }
  }
  
}
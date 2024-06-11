import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {
  
  @Test
  public void testGetInstance() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    assertEquals(DurationFieldType.seconds(), field.getType());
  }
  
  @Test
  public void testGetName() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
    assertEquals("minutes", field.getName());
  }
  
  @Test
  public void testIsSupported() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
    assertFalse(field.isSupported());
  }
  
  @Test
  public void testIsPrecise() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    assertTrue(field.isPrecise());
  }
  
  @Test
  public void testGetMillis() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
    try {
      field.getMillis(10);
      fail("UnsupportedOperationException not thrown");
    } catch (UnsupportedOperationException e) {
      // expected
    }
  }
  
  @Test
  public void testAdd() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    try {
      field.add(1000, 10);
      fail("UnsupportedOperationException not thrown");
    } catch (UnsupportedOperationException e) {
      // expected
    }
  }
  
  @Test
  public void testGetDifference() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
    try {
      field.getDifference(2000, 1000);
      fail("UnsupportedOperationException not thrown");
    } catch (UnsupportedOperationException e) {
      // expected
    }
  }
  
  @Test
  public void testGetUnitMillis() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    assertEquals(0, field.getUnitMillis());
  }
  
  @Test
  public void testCompareTo() {
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
    assertTrue(field1.compareTo(field2) > 0);
  }
}
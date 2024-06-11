
import static org.junit.Assert.*;

import org.joda.time.DurationFieldType;
import org.joda.time.field.UnsupportedDurationField;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

  @Test
  public void testGetType() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    assertEquals(DurationFieldType.years(), field.getType());
  }

  @Test
  public void testGetName() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    assertEquals("years", field.getName());
  }

  @Test
  public void testIsSupported() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    assertFalse(field.isSupported());
  }

  @Test
  public void testIsPrecise() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    assertTrue(field.isPrecise());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValue_long() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    field.getValue(0);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAsLong_long() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    field.getValueAsLong(0);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValue_long_long() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    field.getValue(0, 0);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAsLong_long_long() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    field.getValueAsLong(0, 0);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillis_int() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    field.getMillis(0);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillis_long() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    field.getMillis(0);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillis_int_long() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    field.getMillis(0, 0);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillis_long_long() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    field.getMillis(0, 0);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAdd_long_int() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    field.add(0, 0);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAdd_long_long() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    field.add(0, 0);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifference_long_long() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    field.getDifference(0, 0);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifferenceAsLong_long_long() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    field.getDifferenceAsLong(0, 0);
  }

  @Test
  public void testGetUnitMillis() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    assertEquals(0, field.getUnitMillis());
  }

  @Test
  public void testCompareTo() {
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.months());
    assertEquals(0, field1.compareTo(field2));
  }

  @Test
  public void testEquals() {
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.years());
    assertEquals(field1, field2);
  }

  @Test
  public void testHashCode() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    assertNotNull(field.hashCode());
  }

  @Test
  public void testToString() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    assertEquals("UnsupportedDurationField[years]", field.toString());
  }
}

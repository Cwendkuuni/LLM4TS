import static org.junit.Assert.*;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

  @Test
  public void testGetType() {
    // Test typical use case
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
    assertEquals(DurationFieldType.millis(), field.getType());
  }

  @Test
  public void testGetName() {
    // Test typical use case
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    assertEquals("seconds", field.getName());
  }

  @Test
  public void testIsSupported() {
    // Test typical use case
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    assertFalse(field1.isSupported());

    // Test edge case
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
    assertFalse(field2.isSupported());
  }

  @Test
  public void testIsPrecise() {
    // Test typical use case
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
    assertTrue(field1.isPrecise());

    // Test edge case
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.years());
    assertTrue(field2.isPrecise());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValue() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    long duration = 5000;
    field.getValue(duration);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAsLong() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
    long duration = 86400000;
    field.getValueAsLong(duration);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueWithInstant() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
    long duration = 100;
    long instant = 1234567890;
    field.getValue(duration, instant);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAsLongWithInstant() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    long duration = 1000;
    long instant = 987654321;
    field.getValueAsLong(duration, instant);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithValue() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
    int value = 60;
    field.getMillis(value);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithInstant() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
    long value = 3600000;
    long instant = 456789123;
    field.getMillis(value, instant);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAddWithValue() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
    long instant = 1234567890;
    int value = 100;
    field.add(instant, value);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAddWithLongValue() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
    long instant = 987654321;
    long value = 3600000;
    field.add(instant, value);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifference() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
    long minuendInstant = 1234567890;
    long subtrahendInstant = 987654321;
    field.getDifference(minuendInstant, subtrahendInstant);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifferenceAsLong() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    long minuendInstant = 987654321;
    long subtrahendInstant = 1234567890;
    field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
  }

  @Test
  public void testGetUnitMillis() {
    // Test typical use case
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
    assertEquals(0, field.getUnitMillis());

    // Test edge case
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    assertEquals(0, field2.getUnitMillis());
  }

  @Test
  public void testCompareTo() {
    // Test typical use case
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
    assertEquals(1, field1.compareTo(field2));

    // Test edge case
    UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    UnsupportedDurationField field4 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    assertEquals(0, field3.compareTo(field4));
  }

  @Test
  public void testEquals() {
    // Test typical use case
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.days());
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.days());
    assertTrue(field1.equals(field2));

    // Test edge case
    UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
    UnsupportedDurationField field4 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
    assertFalse(field3.equals(field4));
  }

  @Test
  public void testHashCode() {
    // Test typical use case
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    assertEquals("seconds".hashCode(), field.hashCode());
  }

  @Test
  public void testToString() {
    // Test typical use case
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
    assertEquals("UnsupportedDurationField[minutes]", field.toString());
  }

  @Test
  public void testReadResolve() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
    UnsupportedDurationField resolvedField = (UnsupportedDurationField) field.readResolve();
    assertEquals(field.getType(), resolvedField.getType());
  }

}
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

  @Test
  public void testGetType() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
    assertEquals(DurationFieldType.millis(), field.getType());
  }

  @Test
  public void testGetName() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    assertEquals("seconds", field.getName());
  }

  @Test
  public void testIsSupported() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
    assertFalse(field.isSupported());
  }

  @Test
  public void testIsPrecise() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
    assertTrue(field.isPrecise());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValue() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    field.getValue(1000);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAsLong() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
    field.getValueAsLong(2000);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueWithInstant() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    field.getValue(1000, 2000);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAsLongWithInstant() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
    field.getValueAsLong(2000, 3000);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithValue() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    field.getMillis(10);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithValueLong() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
    field.getMillis(10L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithValueWithInstant() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
    field.getMillis(10, 1000);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithValueLongWithInstant() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    field.getMillis(10L, 1000);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAdd() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
    field.add(1000, 10);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAddLong() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
    field.add(1000, 10L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifference() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    field.getDifference(2000, 1000);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifferenceAsLong() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
    field.getDifferenceAsLong(3000, 2000);
  }

  @Test
  public void testGetUnitMillis() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
    assertEquals(0, field.getUnitMillis());
  }

  @Test
  public void testCompareTo() {
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.years());
    assertEquals(0, field1.compareTo(field2));
  }

  @Test
  public void testEquals() {
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
    assertEquals(field1, field2);
  }

  @Test
  public void testHashCode() {
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
    assertEquals(field1.hashCode(), field2.hashCode());
  }

  @Test
  public void testToString() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    assertEquals("UnsupportedDurationField[seconds]", field.toString());
  }
}
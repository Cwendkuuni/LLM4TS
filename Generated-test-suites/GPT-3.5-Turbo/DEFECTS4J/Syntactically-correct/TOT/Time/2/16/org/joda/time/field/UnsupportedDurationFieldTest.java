import static org.junit.Assert.*;
import java.util.HashMap;
import org.joda.time.DurationFieldType;
import org.joda.time.field.UnsupportedDurationField;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

  @Test
  public void testGetInstance() {
    DurationFieldType type = DurationFieldType.ERA;
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    assertNotNull(field);
  }

  @Test
  public void testGetType() {
    DurationFieldType type = DurationFieldType.DAY_OF_MONTH;
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    assertEquals(type, field.getType());
  }

  @Test
  public void testGetName() {
    DurationFieldType type = DurationFieldType.MINUTE_OF_HOUR;
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    assertEquals(type.getName(), field.getName());
  }

  @Test
  public void testIsSupported() {
    DurationFieldType type = DurationFieldType.YEAR;
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    assertFalse(field.isSupported());
  }

  @Test
  public void testIsPrecise() {
    DurationFieldType type = DurationFieldType.HOUR_OF_DAY;
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    assertTrue(field.isPrecise());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValue() {
    DurationFieldType type = DurationFieldType.MONTH_OF_YEAR;
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    field.getValue(System.currentTimeMillis());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAsLong() {
    DurationFieldType type = DurationFieldType.SECOND_OF_MINUTE;
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    field.getValueAsLong(System.currentTimeMillis());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueWithInstant() {
    DurationFieldType type = DurationFieldType.HOUR_OF_DAY;
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    field.getValue(System.currentTimeMillis(), 1000000000L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAsLongWithInstant() {
    DurationFieldType type = DurationFieldType.MINUTE_OF_DAY;
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    field.getValueAsLong(System.currentTimeMillis(), 1000000000L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithValue() {
    DurationFieldType type = DurationFieldType.MINUTE_OF_HOUR;
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    field.getMillis(10);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithValueLong() {
    DurationFieldType type = DurationFieldType.MINUTE_OF_HOUR;
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    field.getMillis(10L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithValueAndInstant() {
    DurationFieldType type = DurationFieldType.HOUR_OF_DAY;
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    field.getMillis(10, System.currentTimeMillis());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithValueLongAndInstant() {
    DurationFieldType type = DurationFieldType.SECOND_OF_MINUTE;
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    field.getMillis(10L, System.currentTimeMillis());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAddWithInstantAndValue() {
    DurationFieldType type = DurationFieldType.MONTH_OF_YEAR;
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    field.add(1000000000L, 10);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAddWithInstantAndValueLong() {
    DurationFieldType type = DurationFieldType.MONTH_OF_YEAR;
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    field.add(1000000000L, 10L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifference() {
    DurationFieldType type = DurationFieldType.YEAR;
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    long now = System.currentTimeMillis();
    field.getDifference(now + 1000000000L, now);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifferenceAsLong() {
    DurationFieldType type = DurationFieldType.MONTHS;
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    long now = System.currentTimeMillis();
    field.getDifferenceAsLong(now + 1000000000L, now);
  }

  @Test
  public void testGetUnitMillis() {
    DurationFieldType type = DurationFieldType.MINUTE_OF_HOUR;
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    assertEquals(0, field.getUnitMillis());
  }

  @Test
  public void testCompareTo() {
    DurationFieldType type = DurationFieldType.SECOND_OF_MINUTE;
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
    assertEquals(0, field1.compareTo(field2));
  }

  @Test
  public void testEquals() {
    DurationFieldType type = DurationFieldType.DAY_OF_WEEK;
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
    assertTrue(field1.equals(field2));
  }

  @Test
  public void testHashCode() {
    DurationFieldType type = DurationFieldType.HOUR_OF_DAY;
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
    assertEquals(field1.hashCode(), field2.hashCode());
  }

  @Test
  public void testToString() {
    DurationFieldType type = DurationFieldType.MONTH_OF_YEAR;
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    assertEquals("UnsupportedDurationField[monthOfYear]", field.toString());
  }

}
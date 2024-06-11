import static org.junit.Assert.*;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

  @Test
  public void testGetInstance() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    assertSame(field, UnsupportedDurationField.getInstance(DurationFieldType.years()));
    assertNotSame(field, UnsupportedDurationField.getInstance(DurationFieldType.millis()));
    assertNotNull(field);
  }

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
  public void testGetValue() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    field.getValue(12345);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAsLong() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    field.getValueAsLong(12345);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueWithInstant() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    field.getValue(12345, 67890);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAsLongWithInstant() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    field.getValueAsLong(12345, 67890);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithValue() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    field.getMillis(123);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithValueAsLong() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    field.getMillis(1234567890L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithValueAndInstant() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    field.getMillis(123, 4567890);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithValueAsLongAndInstant() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    field.getMillis(1234567890L, 67890);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAddWithInstantAndValue() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    field.add(12345, 678);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAddWithInstantAndValueAsLong() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    field.add(1234567890L, 67890L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifference() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    field.getDifference(1234567890L, 67890L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifferenceAsLong() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    field.getDifferenceAsLong(1234567890L, 67890L);
  }

  @Test
  public void testGetUnitMillis() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    assertEquals(0, field.getUnitMillis());
  }

  @Test
  public void testCompareTo() {
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.millis());
    assertTrue(field1.compareTo(field2) > 0);
    assertTrue(field2.compareTo(field1) == 0);
  }

  @Test
  public void testEquals() {
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.years());
    UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.millis());
    assertTrue(field1.equals(field1));
    assertTrue(field1.equals(field2));
    assertFalse(field1.equals(field3));
    assertFalse(field1.equals(null));
    assertFalse(field1.equals("field1"));
  }

  @Test
  public void testHashCode() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    assertEquals("years".hashCode(), field.hashCode());
  }

  @Test
  public void testToString() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    assertEquals("UnsupportedDurationField[years]", field.toString());
  }

}
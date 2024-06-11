import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

  @Test
  public void testGetType() {
    DurationFieldType type = DurationFieldType.years();
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    assertEquals(type, field.getType());
  }

  @Test
  public void testGetName() {
    DurationFieldType type = DurationFieldType.years();
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    assertEquals(type.getName(), field.getName());
  }

  @Test
  public void testIsSupported() {
    DurationFieldType type = DurationFieldType.years();
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    assertFalse(field.isSupported());
  }

  @Test
  public void testIsPrecise() {
    DurationFieldType type = DurationFieldType.years();
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    assertTrue(field.isPrecise());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValue() {
    DurationFieldType type = DurationFieldType.years();
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    field.getValue(1000L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAsLong() {
    DurationFieldType type = DurationFieldType.years();
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    field.getValueAsLong(1000L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueWithInstant() {
    DurationFieldType type = DurationFieldType.years();
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    field.getValue(1000L, 2000L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAsLongWithInstant() {
    DurationFieldType type = DurationFieldType.years();
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    field.getValueAsLong(1000L, 2000L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithValue() {
    DurationFieldType type = DurationFieldType.years();
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    field.getMillis(1000);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithValueLong() {
    DurationFieldType type = DurationFieldType.years();
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    field.getMillis(1000L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithValueAndInstant() {
    DurationFieldType type = DurationFieldType.years();
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    field.getMillis(1000, 2000L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithValueLongAndInstant() {
    DurationFieldType type = DurationFieldType.years();
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    field.getMillis(1000L, 2000L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAddWithInstantAndValue() {
    DurationFieldType type = DurationFieldType.years();
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    field.add(1000L, 10);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAddWithInstantAndValueLong() {
    DurationFieldType type = DurationFieldType.years();
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    field.add(1000L, 10L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifference() {
    DurationFieldType type = DurationFieldType.years();
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    field.getDifference(1000L, 2000L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifferenceAsLong() {
    DurationFieldType type = DurationFieldType.years();
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    field.getDifferenceAsLong(1000L, 2000L);
  }

  @Test
  public void testGetUnitMillis() {
    DurationFieldType type = DurationFieldType.years();
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    assertEquals(0, field.getUnitMillis());
  }

  @Test
  public void testCompareTo() {
    DurationFieldType type = DurationFieldType.years();
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
    DurationFieldType type2 = DurationFieldType.months();
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
    assertTrue(field1.compareTo(field2) > 0);
  }

  @Test
  public void testEquals() {
    DurationFieldType type = DurationFieldType.years();
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
    assertEquals(field1, field2);
  }

  @Test
  public void testHashCode() {
    DurationFieldType type = DurationFieldType.years();
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
    assertEquals(field1.hashCode(), field2.hashCode());
  }

  @Test
  public void testToString() {
    DurationFieldType type = DurationFieldType.years();
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    assertEquals("UnsupportedDurationField[" + type.getName() + ']', field.toString());
  }
}
import org.junit.Test;
import org.junit.Assert;
import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest {

  @Test
  public void testGetType() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    Assert.assertEquals(DurationFieldType.SECONDS, field.getType());
  }

  @Test
  public void testGetName() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    Assert.assertEquals("Seconds", field.getName());
  }

  @Test
  public void testIsSupported() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    Assert.assertFalse(field.isSupported());
  }

  @Test
  public void testIsPrecise() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    Assert.assertTrue(field.isPrecise());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValue() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    field.getValue(100);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAsLong() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    field.getValueAsLong(100);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueWithInstant() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    field.getValue(100, 200);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAsLongWithInstant() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    field.getValueAsLong(100, 200);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithValue() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    field.getMillis(100);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithValueLong() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    field.getMillis(100L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithValueAndInstant() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    field.getMillis(100, 200);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithValueLongAndInstant() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    field.getMillis(100L, 200L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAddWithInstantAndValue() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    field.add(100, 200);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAddWithInstantAndValueLong() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    field.add(100L, 200L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifference() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    field.getDifference(100, 200);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifferenceAsLong() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    field.getDifferenceAsLong(100L, 200L);
  }

  @Test
  public void testGetUnitMillis() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    Assert.assertEquals(0, field.getUnitMillis());
  }

  @Test
  public void testCompareTo() {
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    Assert.assertEquals(0, field1.compareTo(field2));
  }

  @Test
  public void testEquals() {
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    Assert.assertEquals(field1, field2);
  }

  @Test
  public void testHashCode() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    Assert.assertEquals(field.getName().hashCode(), field.hashCode());
  }

  @Test
  public void testToString() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    Assert.assertEquals("UnsupportedDurationField[Seconds]", field.toString());
  }

  @Test
  public void testReadResolve() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    UnsupportedDurationField resolvedField = (UnsupportedDurationField) field.readResolve();
    Assert.assertEquals(field, resolvedField);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testUnsupportedOperationException() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    throw field.unsupported();
  }

}
import org.joda.time.DurationFieldType;
import org.joda.time.field.UnsupportedDurationField;
import org.junit.Assert;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

  @Test
  public void testGetInstance() {
    DurationFieldType fieldType = DurationFieldType.days();
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(fieldType);
    Assert.assertNotNull(field);
    Assert.assertEquals(fieldType, field.getType());
  }

  @Test
  public void testGetName() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    Assert.assertEquals("days", field.getName());
  }

  @Test
  public void testIsSupported() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    Assert.assertFalse(field.isSupported());
  }

  @Test
  public void testIsPrecise() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    Assert.assertTrue(field.isPrecise());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValue1() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    field.getValue(10);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAsLong1() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    field.getValueAsLong(10);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValue2() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    field.getValue(10, 20);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAsLong2() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    field.getValueAsLong(10, 20);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillis1() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    field.getMillis(10);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillis2() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    field.getMillis(10L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillis3() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    field.getMillis(10, 20L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillis4() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    field.getMillis(10L, 20L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAdd1() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    field.add(10L, 20);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAdd2() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    field.add(10L, 20L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifference1() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    field.getDifference(10L, 20L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifferenceAsLong() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    field.getDifferenceAsLong(10L, 20L);
  }

  @Test
  public void testGetUnitMillis() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    Assert.assertEquals(0L, field.getUnitMillis());
  }

  @Test
  public void testCompareTo() {
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.days());
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
    Assert.assertEquals(1, field1.compareTo(field2));
  }

  @Test
  public void testEquals() {
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.days());
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.days());
    UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
    Assert.assertTrue(field1.equals(field1));
    Assert.assertTrue(field1.equals(field2));
    Assert.assertFalse(field1.equals(null));
    Assert.assertFalse(field1.equals("test"));
    Assert.assertFalse(field1.equals(field3));
  }

  @Test
  public void testHashCode() {
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.days());
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.days());
    UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
    Assert.assertEquals(field1.hashCode(), field2.hashCode());
    Assert.assertNotEquals(field1.hashCode(), field3.hashCode());
  }

  @Test
  public void testToString() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    Assert.assertEquals("UnsupportedDurationField[days]", field.toString());
  }
}
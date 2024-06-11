package org.joda.time.field;
import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest {
  
  @Test
  public void testGetType() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    assertEquals(DurationFieldType.days(), field.getType());
  }
  
  @Test
  public void testGetName() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
    assertEquals(DurationFieldType.weeks().getName(), field.getName());
  }
  
  @Test
  public void testIsSupported() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
    assertFalse(field.isSupported());
  }
  
  @Test
  public void testIsPrecise() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
    assertTrue(field.isPrecise());
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetValue() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
    field.getValue(1000);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAsLong() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    field.getValueAsLong(1000L);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueWithInstant() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    field.getValue(1000, 2000);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAsLongWithInstant() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
    field.getValueAsLong(1000L, 2000L);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithValue() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
    field.getMillis(1000);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithValueLong() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
    field.getMillis(1000L);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithInstant() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    field.getMillis(1000, 2000L);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithInstantLong() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    field.getMillis(1000L, 2000L);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testAdd() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    field.add(1000L, 5);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testAddLong() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
    field.add(1000L, 100L);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifference() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
    field.getDifference(1000L, 500L);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifferenceAsLong() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
    field.getDifferenceAsLong(1000L, 500L);
  }
  
  @Test
  public void testGetUnitMillis() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    assertEquals(0, field.getUnitMillis());
  }
  
  @Test
  public void testCompareTo() {
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.days());
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
    assertEquals(0, field1.compareTo(field2));
  }
  
  @Test
  public void testEquals() {
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
    assertTrue(field1.equals(field2));
  }
  
  @Test
  public void testHashCode() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    assertEquals(DurationFieldType.seconds().getName().hashCode(), field.hashCode());
  }
  
  @Test
  public void testToString() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
    assertEquals("UnsupportedDurationField[" + DurationFieldType.minutes().getName() + ']', field.toString());
  }
  
}
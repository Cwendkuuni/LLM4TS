package org.joda.time.field;
import org.joda.time.DurationFieldType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class UnsupportedDurationFieldTest {

  private UnsupportedDurationField field;

  @Before
  public void setup() {
      field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
  }

  // Test getType() method
  @Test
  public void testGetType() {
      assertEquals(DurationFieldType.minutes(), field.getType());
  }

  // Test getName() method
  @Test
  public void testGetName() {
      assertEquals("minutes", field.getName());
  }

  // Test isSupported() method
  @Test
  public void testIsSupported() {
      assertFalse(field.isSupported());
  }

  // Test isPrecise() method
  @Test
  public void testIsPrecise() {
      assertTrue(field.isPrecise());
  }

  // Test getValue() method
  @Test(expected = UnsupportedOperationException.class)
  public void testGetValue() {
      field.getValue(1000L);
  }

  // Test getValueAsLong() method
  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAsLong() {
      field.getValueAsLong(1000L);
  }

  // Test getValue() method with instant parameter
  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueWithInstant() {
      field.getValue(1000L, 2000L);
  }

  // Test getValueAsLong() method with instant parameter
  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAsLongWithInstant() {
      field.getValueAsLong(1000L, 2000L);
  }

  // Test getMillis() method
  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillis() {
      field.getMillis(1000);
  }

  // Test getMillis() method with long parameter
  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithLong() {
      field.getMillis(1000L);
  }

  // Test getMillis() method with instant parameter
  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithInstant() {
      field.getMillis(1000, 2000L);
  }

  // Test getMillis() method with instant parameter
  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithLongInstant() {
      field.getMillis(1000L, 2000L);
  }

  // Test add() method
  @Test(expected = UnsupportedOperationException.class)
  public void testAdd() {
      field.add(1000L, 10);
  }

  // Test add() method with long parameter
  @Test(expected = UnsupportedOperationException.class)
  public void testAddWithLong() {
      field.add(1000L, 10000L);
  }

  // Test getDifference() method
  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifference() {
      field.getDifference(2000L, 1000L);
  }

  // Test getDifferenceAsLong() method
  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifferenceAsLong() {
      field.getDifferenceAsLong(2000L, 1000L);
  }

  // Test getUnitMillis() method
  @Test
  public void testGetUnitMillis() {
      assertEquals(0L, field.getUnitMillis());
  }

  // Test compareTo() method
  @Test
  public void testCompareTo() {
      assertEquals(0, field.compareTo(UnsupportedDurationField.getInstance(DurationFieldType.minutes())));
  }

  // Test equals() method
  @Test
  public void testEquals() {
      assertEquals(field, UnsupportedDurationField.getInstance(DurationFieldType.minutes()));
  }

  // Test hashCode() method
  @Test
  public void testHashCode() {
      assertEquals(field.hashCode(), UnsupportedDurationField.getInstance(DurationFieldType.minutes()).hashCode());
  }

  // Test toString() method
  @Test
  public void testToString() {
      assertEquals("UnsupportedDurationField[minutes]", field.toString());
  }
}
import static org.junit.Assert.*;
import org.joda.time.DurationFieldType;
import org.junit.Before;
import org.junit.Test;

public class UnsupportedDurationFieldTest {
  
  private UnsupportedDurationField field;
  
  @Before
  public void setUp() {
    field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
  }
  
  @Test
  public void testGetType() {
    assertEquals(DurationFieldType.millis(), field.getType());
  }
  
  @Test
  public void testGetName() {
    assertEquals("millis", field.getName());
  }
  
  @Test
  public void testIsSupported() {
    assertFalse(field.isSupported());
  }
  
  @Test
  public void testIsPrecise() {
    assertTrue(field.isPrecise());
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetValue() {
    field.getValue(100);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAsLong() {
    field.getValueAsLong(100);
  }
  
  // Edge case: Instant is equal to duration
  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueWithInstant() {
    field.getValue(100, 100);
  }
  
  // Edge case: Instant is greater than duration
  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueWithInstantGreater() {
    field.getValue(100, 200);
  }
  
  // Edge case: Instant is less than duration
  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueWithInstantLess() {
    field.getValue(100, 50);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAsLongWithInstant() {
    field.getValueAsLong(100, 100);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAsLongWithInstantGreater() {
    field.getValueAsLong(100, 200);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetValueAsLongWithInstantLess() {
    field.getValueAsLong(100, 50);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillis() {
    field.getMillis(1);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithLong() {
    field.getMillis(1L);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithInstant() {
    field.getMillis(1, 100);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithInstantGreater() {
    field.getMillis(1, 200);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetMillisWithInstantLess() {
    field.getMillis(1, 50);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testAddWithInstant() {
    field.add(100, 1);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testAddWithInstantGreater() {
    field.add(200, 1);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testAddWithInstantLess() {
    field.add(50, 1);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testAddWithLong() {
    field.add(100L, 1);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testAddWithLongGreater() {
    field.add(200L, 1);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testAddWithLongLess() {
    field.add(50L, 1);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifference() {
    field.getDifference(100, 50);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifferenceWithInstantGreater() {
    field.getDifference(200, 100);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifferenceWithInstantLess() {
    field.getDifference(50, 100);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifferenceAsLong() {
    field.getDifferenceAsLong(100, 50);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifferenceAsLongWithInstantGreater() {
    field.getDifferenceAsLong(200, 100);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetDifferenceAsLongWithInstantLess() {
    field.getDifferenceAsLong(50, 100);
  }
  
  @Test
  public void testGetUnitMillis() {
    assertEquals(0L, field.getUnitMillis());
  }
  
  @Test
  public void testCompareTo() {
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
    assertEquals(1, field.compareTo(field2));
    assertEquals(0, field.compareTo(field));
    assertEquals(0, field2.compareTo(field2));
  }
  
  @Test
  public void testEquals() {
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.millis());
    UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
    assertTrue(field.equals(field));
    assertTrue(field.equals(field2));
    assertFalse(field.equals(field3));
    assertFalse(field.equals(null));
    assertFalse(field.equals("Not a field"));
  }
  
  @Test
  public void testHashCode() {
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.millis());
    assertEquals(field.hashCode(), field2.hashCode());
  }
  
  @Test
  public void testToString() {
    assertEquals("UnsupportedDurationField[millis]", field.toString());
  }
  
  @Test
  public void testReadResolve() {
    UnsupportedDurationField field2 = (UnsupportedDurationField) field.readResolve();
    assertEquals(field, field2);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testUnsupportedOperationException() {
    throw field.unsupported();
  }
}
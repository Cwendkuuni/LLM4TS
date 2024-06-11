import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.field.ImpreciseDateTimeField;
import org.joda.time.field.FieldUtils;

import static org.junit.Assert.*;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
  
  @Test
  public void testIsLenient() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
    assertFalse(field.isLenient());
  }
  
  @Test
  public void testGet() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
    long n = 12345678;
    assertEquals(3, field.get(n));
  }
  
  @Test
  public void testAddIntZero() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
    long n = 12345678;
    assertEquals(n, field.add(n, 0));
  }
  
  @Test
  public void testAddIntNonZero() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
    long n = 12345678;
    assertEquals(12345687, field.add(n, 9));
  }
  
  @Test
  public void testAddLongNonZero() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
    long n = 12345678;
    assertEquals(12345687, field.add(n, 9L));
  }
  
  @Test
  public void testAddWrapField() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
    long n = 12345678;
    assertEquals(12345681, field.addWrapField(n, 3));
  }
  
  @Test
  public void testGetDifferenceAsLong() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
    long n1 = 12345678;
    long n2 = 987654321;
    assertEquals(808601, field.getDifferenceAsLong(n1, n2));
  }
  
  @Test
  public void testSet() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
    long n = 12345678;
    assertEquals(12345966, field.set(n, 6));
  }
  
  @Test
  public void testIsLeap() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 2);
    long n = 12345678;
    assertTrue(field.isLeap(n));
  }
  
  @Test
  public void testGetLeapAmount() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 2);
    long n = 12345678;
    assertEquals(1, field.getLeapAmount(n));
  }
  
  @Test
  public void testGetMinimumValue() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
    assertEquals(1, field.getMinimumValue());
  }
  
  @Test
  public void testGetMaximumValue() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
    assertEquals(12, field.getMaximumValue());
  }
  
  @Test
  public void testRoundFloor() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
    long n = 12345678;
    assertEquals(12304194, field.roundFloor(n));
  }
  
  @Test
  public void testRemainder() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
    long n = 12345678;
    assertEquals(41484, field.remainder(n));
  }
}
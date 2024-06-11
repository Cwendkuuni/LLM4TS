import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.ImpreciseDateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeField;
import org.joda.time.ReadablePartial;
import org.joda.time.DurationField;
import org.joda.time.DateTimeUtils;
import org.joda.time.field.FieldUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
  
  private BasicMonthOfYearDateTimeField field;
  
  @Before
  public void setUp() {
    BasicChronology chronology = new BasicChronology();
    field = new BasicMonthOfYearDateTimeField(chronology, 6);
  }
  
  @Test
  public void testIsLenient() {
    assertFalse(field.isLenient());
  }
  
  @Test
  public void testGet() {
    long n = new DateTime().getMillis();
    assertEquals(6, field.get(n));
  }
  
  @Test
  public void testAddZero() {
    long n = new DateTime().getMillis();
    assertEquals(n, field.add(n, 0));
  }
  
  @Test
  public void testAddPositive() {
    long n = new DateTime().getMillis();
    assertEquals(n + (31 * 24 * 60 * 60 * 1000), field.add(n, 1));
  }
  
  @Test
  public void testAddNegative() {
    long n = new DateTime().getMillis();
    assertEquals(n - (31 * 24 * 60 * 60 * 1000), field.add(n, -1));
  }
  
  @Test
  public void testAddWrapField() {
    long n = new DateTime().getMillis();
    assertEquals(n + (30 * 24 * 60 * 60 * 1000), field.addWrapField(n, 1));
  }
  
  @Test
  public void testAddWrapFieldMax() {
    long n = new DateTime().getMillis();
    assertEquals(n - (30 * 24 * 60 * 60 * 1000), field.addWrapField(n, -1));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testAddWrapFieldOutOfRange() {
    long n = new DateTime().getMillis();
    field.addWrapField(n, 13);
  }
  
  @Test
  public void testGetDifferenceAsLong_Positive() {
    long n1 = new DateTime(2021, 6, 15, 0, 0).getMillis();
    long n2 = new DateTime(2021, 1, 15, 0, 0).getMillis();
    assertEquals(5, field.getDifferenceAsLong(n1, n2));
  }
  
  @Test
  public void testGetDifferenceAsLong_Negative() {
    long n1 = new DateTime(2021, 1, 15, 0, 0).getMillis();
    long n2 = new DateTime(2021, 6, 15, 0, 0).getMillis();
    assertEquals(-5, field.getDifferenceAsLong(n1, n2));
  }
  
  @Test
  public void testSet() {
    long n = new DateTime().getMillis();
    assertEquals(new DateTime().withMonthOfYear(6).getMillis(), field.set(n, 6));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSetInvalidValue() {
    long n = new DateTime().getMillis();
    field.set(n, 13);
  }
  
  @Test
  public void testIsLeap() {
    long n1 = new DateTime(2021, 7, 15, 0, 0).getMillis();
    assertTrue(field.isLeap(n1));
    
    long n2 = new DateTime(2021, 8, 15, 0, 0).getMillis();
    assertFalse(field.isLeap(n2));
  }
  
  @Test
  public void testGetLeapAmount() {
    long n1 = new DateTime(2021, 7, 15, 0, 0).getMillis();
    assertEquals(1, field.getLeapAmount(n1));
    
    long n2 = new DateTime(2021, 8, 15, 0, 0).getMillis();
    assertEquals(0, field.getLeapAmount(n2));
  }
  
  @Test
  public void testGetMinimumValue() {
    assertEquals(1, field.getMinimumValue());
  }
  
  @Test
  public void testGetMaximumValue() {
    assertEquals(12, field.getMaximumValue());
  }
  
  @Test
  public void testRoundFloor() {
    long n = new DateTime(2021, 6, 15, 0, 0).getMillis();
    assertEquals(new DateTime(2021, 6, 1, 0, 0).getMillis(), field.roundFloor(n));
  }
  
  @Test
  public void testRemainder() {
    long n = new DateTime(2021, 6, 15, 0, 0).getMillis();
    assertEquals(14 * 24 * 60 * 60 * 1000, field.remainder(n));
  }
}
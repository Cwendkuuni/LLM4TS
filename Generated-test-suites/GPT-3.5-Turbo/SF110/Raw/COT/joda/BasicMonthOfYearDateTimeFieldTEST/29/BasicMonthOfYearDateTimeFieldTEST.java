import org.junit.*;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

  @Test
  public void testIsLenient() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
    assertFalse(field.isLenient());
  }

  @Test
  public void testGet() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
    long n = 1598424000000L;  // August 26, 2020
    assertEquals(8, field.get(n));
  }

  @Test
  public void testAddZero() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
    long n = 1598424000000L;  // August 26, 2020
    assertEquals(n, field.add(n, 0));
  }

  // Test for potential edge case: adding positive number of months
  @Test
  public void testAddPositiveMonths() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
    long n = 1598424000000L;  // August 26, 2020
    long expected = 1606786800000L;  // November 30, 2020
    assertEquals(expected, field.add(n, 3));
  }

  // Test for potential edge case: adding negative number of months
  @Test
  public void testAddNegativeMonths() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
    long n = 1598424000000L;  // August 26, 2020
    long expected = 1583052000000L;  // March 1, 2020
    assertEquals(expected, field.add(n, -6));
  }

  // Test for potential edge case: adding zero milliseconds
  @Test
  public void testAddZeroMillis() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
    long n = 1598424000000L;  // August 26, 2020
    assertEquals(n, field.add(n, 0L));
  }

  // Test for potential edge case: adding positive milliseconds
  @Test
  public void testAddPositiveMillis() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
    long n = 1598424000000L;  // August 26, 2020
    long expected = 1609411200000L;  // December 31, 2020
    assertEquals(expected, field.add(n, 12672000000L));
  }

  // Test for potential exception: adding too large positive milliseconds
  @Test(expected = IllegalArgumentException.class)
  public void testAddPositiveLargeMillis() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
    long n = 1598424000000L;  // August 26, 2020
    field.add(n, 10000000000000L);
  }

  // Test for potential exception: adding too large negative milliseconds
  @Test(expected = IllegalArgumentException.class)
  public void testAddNegativeLargeMillis() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
    long n = 1598424000000L;  // August 26, 2020
    field.add(n, -10000000000000L);
  }

  // Test for potential edge case: adding positive number of months to partial date
  @Test
  public void testAddPartialDate() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
    int[] array = {2020};
    int[] expected = {2021};
    assertArrayEquals(expected, field.add(null, 0, array, 12));
  }

  @Test
  public void testAddWrapField() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
    long n = 1598424000000L;  // August 26, 2020
    long expected = 1611842400000L;  // January 28, 2021
    assertEquals(expected, field.addWrapField(n, 5));
  }

  @Test
  public void testGetDifferenceAsLong() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
    long n1 = 1604076000000L;  // October 31, 2020
    long n2 = 1598424000000L;  // August 26, 2020
    assertEquals(2, field.getDifferenceAsLong(n1, n2));
  }

  @Test
  public void testSet() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
    long n = 1598424000000L;  // August 26, 2020
    long expected = 1601416800000L;  // October 1, 2020
    assertEquals(expected, field.set(n, 10));
  }

  @Test
  public void testGetRangeDurationField() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
    assertNotNull(field.getRangeDurationField());
    assertEquals("years", field.getRangeDurationField().getName());
  }

  @Test
  public void testIsLeap() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
    long n = 1609411200000L;  // December 31, 2020
    assertTrue(field.isLeap(n));
  }

  @Test
  public void testGetLeapAmount() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
    long n = 1609411200000L;  // December 31, 2020
    assertEquals(1, field.getLeapAmount(n));
  }

  @Test
  public void testGetLeapDurationField() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
    assertNotNull(field.getLeapDurationField());
    assertEquals("days", field.getLeapDurationField().getName());
  }

  @Test
  public void testGetMinimumValue() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
    assertEquals(1, field.getMinimumValue());
  }

  @Test
  public void testGetMaximumValue() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
    assertEquals(12, field.getMaximumValue());
  }

}
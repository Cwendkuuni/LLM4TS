import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

  private BasicMonthOfYearDateTimeField field;
  
  @Test
  public void testIsLenient() {
    field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
    assertFalse(field.isLenient());
  }
  
  @Test
  public void testGet() {
    field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
    long n = 1631557200000L; // October 2021
    assertEquals(10, field.get(n));
  }
  
  @Test
  public void testAddIntZero() {
    field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
    long n = 1631557200000L; // October 2021
    assertEquals(n, field.add(n, 0));
  }
  
  @Test
  public void testAddLongZero() {
    field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
    long n = 1631557200000L; // October 2021
    long n2 = 0L;
    assertEquals(n, field.add(n, n2));
  }
  
  @Test
  public void testAddIntPositive() {
    field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
    long n = 1631557200000L; // October 2021
    int n2 = 3;
    long expected = 1654093200000L; // January 2022
    assertEquals(expected, field.add(n, n2));
  }
  
  @Test
  public void testAddIntNegative() {
    field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
    long n = 1631557200000L; // October 2021
    int n2 = -5;
    long expected = 1612136400000L; // May 2021
    assertEquals(expected, field.add(n, n2));
  }
  
  @Test
  public void testAddLongPositive() {
    field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
    long n = 1631557200000L; // October 2021
    long n2 = 3L;
    long expected = 1654093200000L; // January 2022
    assertEquals(expected, field.add(n, n2));
  }
  
  @Test
  public void testAddLongNegative() {
    field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
    long n = 1631557200000L; // October 2021
    long n2 = -5L;
    long expected = 1612136400000L; // May 2021
    assertEquals(expected, field.add(n, n2));
  }
  
  @Test
  public void testAddWrapField() {
    field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
    long n = 1631557200000L; // October 2021
    int n2 = 3;
    long expected = 1654093200000L; // January 2022
    assertEquals(expected, field.addWrapField(n, n2));
  }
  
  @Test
  public void testGetDifferenceAsLongPositive() {
    field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
    long n1 = 1631557200000L; // October 2021
    long n2 = 1663074000000L; // January 2022
    long expected = 3L;
    assertEquals(expected, field.getDifferenceAsLong(n2, n1));
  }
  
  @Test
  public void testGetDifferenceAsLongNegative() {
    field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
    long n1 = 1631557200000L; // October 2021
    long n2 = 1612136400000L; // May 2021
    long expected = -5L;
    assertEquals(expected, field.getDifferenceAsLong(n1, n2));
  }
  
  @Test
  public void testSet() {
    field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
    long n = 1631557200000L; // October 2021
    int n2 = 8;
    long expected = 1659325200000L; // August 2022
    assertEquals(expected, field.set(n, n2));
  }
  
  @Test
  public void testIsLeap() {
    field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 2);
    long n = 1631557200000L; // October 2021
    assertFalse(field.isLeap(n));
    long n2 = 1583020800000L; // March 2020
    assertTrue(field.isLeap(n2));
  }
  
  @Test
  public void testGetLeapAmount() {
    field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 2);
    long n = 1631557200000L; // October 2021
    assertEquals(0, field.getLeapAmount(n));
    long n2 = 1583020800000L; // March 2020
    assertEquals(1, field.getLeapAmount(n2));
  }

  @Test
  public void testGetMinimumValue() {
    field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
    assertEquals(1, field.getMinimumValue());
  }
  
  @Test
  public void testGetMaximumValue() {
    field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
    assertEquals(12, field.getMaximumValue());
  }
  
  @Test
  public void testRoundFloor() {
    field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
    long n = 1631557200000L; // October 2021
    long expected = 1633040400000L; // October 2021
    assertEquals(expected, field.roundFloor(n));
  }
  
  @Test
  public void testRemainder() {
    field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
    long n = 1631557200000L; // October 2021
    assertEquals(0, field.remainder(n));
  }
}
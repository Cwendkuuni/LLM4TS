import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
  
  @Test
  public void testGet() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, leapMonth);
    assertEquals(1, field.get(instant));
  }
  
  @Test
  public void testAdd() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, leapMonth);
    assertEquals(expectedResult, field.add(instant, value));
  }
  
  @Test
  public void testAddWrapField() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, leapMonth);
    assertEquals(expectedResult, field.addWrapField(instant, value));
  }
  
  @Test
  public void testGetDifferenceAsLong() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, leapMonth);
    assertEquals(expectedResult, field.getDifferenceAsLong(minuendInstant, subtrahendInstant));
  }
  
  @Test
  public void testSet() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, leapMonth);
    assertEquals(expectedResult, field.set(instant, month));
  }
  
  @Test
  public void testIsLeap() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, leapMonth);
    assertEquals(false, field.isLeap(instant));
  }
  
  @Test
  public void testGetLeapAmount() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, leapMonth);
    assertEquals(expectedResult, field.getLeapAmount(instant));
  }
  
  @Test
  public void testGetMinimumValue() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, leapMonth);
    assertEquals(1, field.getMinimumValue());
  }
  
  @Test
  public void testGetMaximumValue() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, leapMonth);
    assertEquals(12, field.getMaximumValue());
  }
  
  @Test
  public void testRoundFloor() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, leapMonth);
    assertEquals(floorInstant, field.roundFloor(instant));
  }
  
  @Test
  public void testRemainder() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, leapMonth);
    assertEquals(expectedResult, field.remainder(instant));
  }
}
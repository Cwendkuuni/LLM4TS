import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.joda.time.field.DateTimeFieldType;

public class BasicMonthOfYearDateTimeFieldTest {
  
  @Test
  public void testIsLenient() {
    BasicMonthOfYearDateTimeField fieldUnderTest = new BasicMonthOfYearDateTimeField(null, 1);
    assertEquals(false, fieldUnderTest.isLenient());
  }
  
  @Test
  public void testGet() {
    BasicMonthOfYearDateTimeField fieldUnderTest = new BasicMonthOfYearDateTimeField(null, 1);
    assertEquals(1, fieldUnderTest.get(0));
  }
  
  @Test
  public void testAddWithZeroAmount() {
    BasicMonthOfYearDateTimeField fieldUnderTest = new BasicMonthOfYearDateTimeField(null, 1);
    assertEquals(0, fieldUnderTest.add(0, 0));
  }
  
  @Test
  public void testAddWithPositiveAmount() {
    BasicMonthOfYearDateTimeField fieldUnderTest = new BasicMonthOfYearDateTimeField(null, 1);
    assertEquals(1, fieldUnderTest.add(0, 1));
  }
  
  @Test
  public void testAddWithNegativeAmount() {
    BasicMonthOfYearDateTimeField fieldUnderTest = new BasicMonthOfYearDateTimeField(null, 1);
    assertEquals(-1, fieldUnderTest.add(0, -1));
  }
  
  @Test
  public void testAddWrapFieldWithZeroAmount() {
    BasicMonthOfYearDateTimeField fieldUnderTest = new BasicMonthOfYearDateTimeField(null, 1);
    assertEquals(0, fieldUnderTest.addWrapField(0, 0));
  }
  
  @Test
  public void testAddWrapFieldWithPositiveAmount() {
    BasicMonthOfYearDateTimeField fieldUnderTest = new BasicMonthOfYearDateTimeField(null, 1);
    assertEquals(1, fieldUnderTest.addWrapField(0, 1));
  }
  
  @Test
  public void testAddWrapFieldWithNegativeAmount() {
    BasicMonthOfYearDateTimeField fieldUnderTest = new BasicMonthOfYearDateTimeField(null, 1);
    assertEquals(-1, fieldUnderTest.addWrapField(0, -1));
  }
  
  @Test
  public void testGetDifferenceAsLongWithPositiveAmount() {
    BasicMonthOfYearDateTimeField fieldUnderTest = new BasicMonthOfYearDateTimeField(null, 1);
    assertEquals(1, fieldUnderTest.getDifferenceAsLong(1, 0));
  }
  
  @Test
  public void testGetDifferenceAsLongWithNegativeAmount() {
    BasicMonthOfYearDateTimeField fieldUnderTest = new BasicMonthOfYearDateTimeField(null, 1);
    assertEquals(-1, fieldUnderTest.getDifferenceAsLong(0, 1));
  }
  
  @Test
  public void testSet() {
    BasicMonthOfYearDateTimeField fieldUnderTest = new BasicMonthOfYearDateTimeField(null, 1);
    assertEquals(1, fieldUnderTest.set(0, 1));
  }
  
  @Test
  public void testIsLeapWithLeapYearAndLeapMonth() {
    BasicMonthOfYearDateTimeField fieldUnderTest = new BasicMonthOfYearDateTimeField(null, 1);
    assertEquals(true, fieldUnderTest.isLeap(0));
  }
  
  @Test
  public void testIsLeapWithLeapYearAndNonLeapMonth() {
    BasicMonthOfYearDateTimeField fieldUnderTest = new BasicMonthOfYearDateTimeField(null, 2);
    assertEquals(false, fieldUnderTest.isLeap(0));
  }
  
  @Test
  public void testIsLeapWithNonLeapYear() {
    BasicMonthOfYearDateTimeField fieldUnderTest = new BasicMonthOfYearDateTimeField(null, 1);
    assertEquals(false, fieldUnderTest.isLeap(1));
  }
  
  @Test
  public void testGetLeapAmountWithLeapYearAndLeapMonth() {
    BasicMonthOfYearDateTimeField fieldUnderTest = new BasicMonthOfYearDateTimeField(null, 1);
    assertEquals(1, fieldUnderTest.getLeapAmount(0));
  }
  
  @Test
  public void testGetLeapAmountWithLeapYearAndNonLeapMonth() {
    BasicMonthOfYearDateTimeField fieldUnderTest = new BasicMonthOfYearDateTimeField(null, 2);
    assertEquals(0, fieldUnderTest.getLeapAmount(0));
  }
  
  @Test
  public void testGetLeapDurationField() {
    BasicMonthOfYearDateTimeField fieldUnderTest = new BasicMonthOfYearDateTimeField(null, 1);
    assertNotNull(fieldUnderTest.getLeapDurationField());
  }
  
  @Test
  public void testGetMinimumValue() {
    BasicMonthOfYearDateTimeField fieldUnderTest = new BasicMonthOfYearDateTimeField(null, 1);
    assertEquals(1, fieldUnderTest.getMinimumValue());
  }
  
  @Test
  public void testGetMaximumValue() {
    BasicMonthOfYearDateTimeField fieldUnderTest = new BasicMonthOfYearDateTimeField(null, 12);
    assertEquals(12, fieldUnderTest.getMaximumValue());
  }
  
  @Test
  public void testRoundFloor() {
    BasicMonthOfYearDateTimeField fieldUnderTest = new BasicMonthOfYearDateTimeField(null, 1);
    assertEquals(1, fieldUnderTest.roundFloor(1));
  }
  
  @Test
  public void testRemainder() {
    BasicMonthOfYearDateTimeField fieldUnderTest = new BasicMonthOfYearDateTimeField(null, 1);
    assertEquals(0, fieldUnderTest.remainder(0));
  }
}
import static org.junit.Assert.*;
import org.junit.*;

public class BasicMonthOfYearDateTimeFieldTest {

  @Test
  public void testGet() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
    assertEquals(3, field.get(1000000000));
  }
  
  @Test
  public void testAdd() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
    assertEquals(3, field.add(1000000000, 1));
  }
  
  @Test
  public void testAddWrapField() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
    assertEquals(3, field.addWrapField(1000000000, 1));
  }
  
  @Test
  public void testGetDifferenceAsLong() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
    assertEquals(12, field.getDifferenceAsLong(2000000000, 1000000000));
  }
  
  @Test
  public void testSet() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
    assertEquals(1000000000, field.set(1234567890, 2));
  }
  
  @Test
  public void testGetMinimumValue() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
    assertEquals(1, field.getMinimumValue());
  }
  
  @Test
  public void testGetMaximumValue() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
    assertEquals(12, field.getMaximumValue());
  }
  
  @Test
  public void testIsLeap() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
    assertEquals(false, field.isLeap(1000000000));
  }
  
  @Test
  public void testGetLeapAmount() {
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
    assertEquals(0, field.getLeapAmount(1000000000));
  }

}
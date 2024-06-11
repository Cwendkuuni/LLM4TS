import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

  private BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
  
  @Test
  public void testGet() {
    long instant = 0;
    int result = field.get(instant);
    assert result == 1;
  }
  
  @Test
  public void testAdd() {
    long instant = 0;
    int months = 1;
    long result = field.add(instant, months);
    assert result == 2629746000L;
  }
  
  @Test
  public void testAddWrapField() {
    long instant = 0;
    int months = 1;
    long result = field.addWrapField(instant, months);
    assert result == 2629746000L;
  }
  
  @Test
  public void testSet() {
    long instant = 0;
    int month = 6;
    long result = field.set(instant, month);
    assert result == 157540800000L;
  }
  
  @Test
  public void testIsLeap() {
    long instant = 0;
    boolean result = field.isLeap(instant);
    assert !result;
  }
  
  @Test
  public void testGetLeapAmount() {
    long instant = 0;
    int result = field.getLeapAmount(instant);
    assert result == 0;
  }
  
  @Test
  public void testGetMinimumValue() {
    int result = field.getMinimumValue();
    assert result == 1;
  }
  
  @Test
  public void testGetMaximumValue() {
    int result = field.getMaximumValue();
    assert result == 12;
  }
  
  @Test
  public void testRoundFloor() {
    long instant = 0;
    long result = field.roundFloor(instant);
    assert result == 0;
  }
  
  @Test
  public void testRemainder() {
    long instant = 0;
    long result = field.remainder(instant);
    assert result == 0;
  }
}
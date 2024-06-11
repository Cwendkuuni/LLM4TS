
import org.junit.Assert;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {
  
  @Test
  public void testGetValue() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
    int value = period.getValue();
    Assert.assertEquals(5, value);
  }
  
  @Test
  public void testSetValue() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
    period.setValue(10);
    int value = period.getValue();
    Assert.assertEquals(10, value);
  }
  
  @Test
  public void testGetFieldType() {
    // TODO: Write the test case
  }
  
  @Test
  public void testGetPeriodType() {
    // TODO: Write the test case
  }
  
  @Test
  public void testSize() {
    // TODO: Write the test case
  }
  
  @Test
  public void testGetFieldTypeInt() {
    // TODO: Write the test case
  }
  
  @Test
  public void testGetValueInt() {
    // TODO: Write the test case
  }
  
  @Test
  public void testGet() {
    // TODO: Write the test case
  }
  
  @Test
  public void testIsSupported() {
    // TODO: Write the test case
  }
  
  @Test
  public void testToPeriod() {
    // TODO: Write the test case
  }
  
  @Test
  public void testToMutablePeriod() {
    // TODO: Write the test case
  }
  
  @Test
  public void testEquals() {
    // TODO: Write the test case
  }
  
  @Test
  public void testHashCode() {
    // TODO: Write the test case
  }
  
  @Test
  public void testCompareTo() {
    // TODO: Write the test case
  }
}

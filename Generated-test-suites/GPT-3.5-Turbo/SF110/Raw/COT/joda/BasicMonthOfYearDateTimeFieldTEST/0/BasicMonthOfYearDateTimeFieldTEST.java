import org.joda.time.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
  
  private BasicChronology iChronology;
  private BasicMonthOfYearDateTimeField iMonthField;
  
  @Before
  public void setup() {
    iChronology = new BasicChronology();
    iMonthField = new BasicMonthOfYearDateTimeField(iChronology, 1);
  }
  
  @Test
  public void testIsLenient() {
    Assert.assertFalse(iMonthField.isLenient());
  }
  
  @Test
  public void testGet() {
    long n = 1234567890;
    Assert.assertEquals(3, iMonthField.get(n));
    
    n = 9876543210;
    Assert.assertEquals(10, iMonthField.get(n));
    
    n = 123456789012345L;
    Assert.assertEquals(9, iMonthField.get(n));
    
    n = -9876543210L;
    Assert.assertEquals(5, iMonthField.get(n));
  }
  
  @Test
  public void testAddWithInt() {
    long n = 1234567890;
    int n2 = 5;
    long expected = 1237053090;
    Assert.assertEquals(expected, iMonthField.add(n, n2));
    
    n = 9876543210L;
    n2 = -2;
    expected = 9868821210L;
    Assert.assertEquals(expected, iMonthField.add(n, n2));
    
    n = 123456789012345L;
    n2 = 7;
    expected = 123518526106560L;
    Assert.assertEquals(expected, iMonthField.add(n, n2));
    
    n = -9876543210L;
    n2 = -12;
    expected = -9885822790L;
    Assert.assertEquals(expected, iMonthField.add(n, n2));
  }
  
  @Test
  public void testAddWithLong() {
    long n = 1234567890;
    long n2 = 5;
    long expected = 1237053090;
    Assert.assertEquals(expected, iMonthField.add(n, n2));
    
    n = 9876543210L;
    n2 = -2;
    expected = 9868821210L;
    Assert.assertEquals(expected, iMonthField.add(n, n2));
    
    n = 123456789012345L;
    n2 = 7;
    expected = 123518526106560L;
    Assert.assertEquals(expected, iMonthField.add(n, n2));
    
    n = -9876543210L;
    n2 = -12;
    expected = -9885822790L;
    Assert.assertEquals(expected, iMonthField.add(n, n2));
  }
  
  @Test
  public void testAddWrapField() {
    long n = 1234567890;
    int n2 = 5;
    long expected = 1234567890;
    Assert.assertEquals(expected, iMonthField.addWrapField(n, n2));
    
    n = 9876543210L;
    n2 = -2;
    expected = 9876543210L;
    Assert.assertEquals(expected, iMonthField.addWrapField(n, n2));
    
    n = 123456789012345L;
    n2 = 7;
    expected = 123456789012345L;
    Assert.assertEquals(expected, iMonthField.addWrapField(n, n2));
    
    n = -9876543210L;
    n2 = -12;
    expected = -9876543210L;
    Assert.assertEquals(expected, iMonthField.addWrapField(n, n2));
  }
  
  @Test
  public void testGetDifferenceAsLong() {
    long n = 123456789012345L;
    long set = 123456789012345L;
    long expected = 0;
    Assert.assertEquals(expected, iMonthField.getDifferenceAsLong(n, set));
    
    n = 1234567890;
    set = 1234567890;
    expected = 0;
    Assert.assertEquals(expected, iMonthField.getDifferenceAsLong(n, set));
    
    n = 9876543210L;
    set = 1234567890;
    expected = 6;
    Assert.assertEquals(expected, iMonthField.getDifferenceAsLong(n, set));
    
    n = 9876543210L;
    set = -1357924680;
    expected = 12;
    Assert.assertEquals(expected, iMonthField.getDifferenceAsLong(n, set));
  }
  
  @Test
  public void testSet() {
    long n = 123456789012345L;
    int n2 = 8;
    long expected = 123468999512345L;
    Assert.assertEquals(expected, iMonthField.set(n, n2));
    
    n = 1234567890;
    n2 = 12;
    expected = 1234582500;
    Assert.assertEquals(expected, iMonthField.set(n, n2));
    
    n = 9876543210L;
    n2 = 2;
    expected = 9876540108L;
    Assert.assertEquals(expected, iMonthField.set(n, n2));
    
    n = -9876543210L;
    n2 = 6;
    expected = -9876527082L;
    Assert.assertEquals(expected, iMonthField.set(n, n2));
  }
  
  @Test
  public void testIsLeap() {
    long n = 1234567890;
    Assert.assertFalse(iMonthField.isLeap(n));
    
    n = 9876543210L;
    Assert.assertFalse(iMonthField.isLeap(n));
    
    n = 123456789012345L;
    Assert.assertFalse(iMonthField.isLeap(n));
    
    n = -9876543210L;
    Assert.assertFalse(iMonthField.isLeap(n));
  }
  
  @Test
  public void testGetLeapAmount() {
    long n = 1234567890;
    Assert.assertEquals(0, iMonthField.getLeapAmount(n));
    
    n = 9876543210L;
    Assert.assertEquals(0, iMonthField.getLeapAmount(n));
    
    n = 123456789012345L;
    Assert.assertEquals(0, iMonthField.getLeapAmount(n));
    
    n = -9876543210L;
    Assert.assertEquals(0, iMonthField.getLeapAmount(n));
  }
  
  @Test
  public void testGetMinimumValue() {
    Assert.assertEquals(1, iMonthField.getMinimumValue());
  }
  
  @Test
  public void testGetMaximumValue() {
    Assert.assertEquals(iChronology.getMaxMonth(), iMonthField.getMaximumValue());
  }
  
  @Test
  public void testRoundFloor() {
    long n = 1234567890;
    long expected = 1234567890;
    Assert.assertEquals(expected, iMonthField.roundFloor(n));
    
    n = 9876543210L;
    expected = 9876543210L;
    Assert.assertEquals(expected, iMonthField.roundFloor(n));
    
    n = 123456789012345L;
    expected = 123456789012345L;
    Assert.assertEquals(expected, iMonthField.roundFloor(n));
    
    n = -9876543210L;
    expected = -9876543210L;
    Assert.assertEquals(expected, iMonthField.roundFloor(n));
  }
  
  @Test
  public void testRemainder() {
    long n = 1234567890;
    long expected = 0;
    Assert.assertEquals(expected, iMonthField.remainder(n));
    
    n = 9876543210L;
    expected = 0;
    Assert.assertEquals(expected, iMonthField.remainder(n));
    
    n = 123456789012345L;
    expected = 0;
    Assert.assertEquals(expected, iMonthField.remainder(n));
    
    n = -9876543210L;
    expected = 0;
    Assert.assertEquals(expected, iMonthField.remainder(n));
  }
  
}
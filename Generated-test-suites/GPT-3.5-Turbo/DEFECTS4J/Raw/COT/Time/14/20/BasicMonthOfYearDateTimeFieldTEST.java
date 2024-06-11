import org.joda.time.DateTimeFieldType;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
  
  private BasicChronology chronology;
  private BasicMonthOfYearDateTimeField monthField;
  
  @Before
  public void setUp() {
    chronology = new BasicChronology();
    monthField = new BasicMonthOfYearDateTimeField(chronology, 0);
  }
  
  @Test
  public void testGet() {
    long instant = 123456789L;
    int expected = 10;
    int actual = monthField.get(instant);
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testAddZero() {
    long instant = 123456789L;
    int months = 0;
    long expected = 123456789L;
    long actual = monthField.add(instant, months);
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testAddPositive() {
    long instant = 123456789L;
    int months = 5;
    long expected = 128504189L;
    long actual = monthField.add(instant, months);
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testAddNegative() {
    long instant = 123456789L;
    int months = -5;
    long expected = 118409389L;
    long actual = monthField.add(instant, months);
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testAddWrapField() {
    long instant = 123456789L;
    int months = 12;
    long expected = 123456789L;
    long actual = monthField.addWrapField(instant, months);
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testGetDifferenceAsLong() {
    long minuendInstant = 123456789L;
    long subtrahendInstant = 987654321L;
    long expected = -57;
    long actual = monthField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testSet() {
    long instant = 123456789L;
    int month = 2;
    long expected = 104267789L;
    long actual = monthField.set(instant, month);
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testGetMinimumValue() {
    int expected = 1;
    int actual = monthField.getMinimumValue();
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testGetMaximumValue() {
    int expected = 12;
    int actual = monthField.getMaximumValue();
    Assert.assertEquals(expected, actual);
  }
}


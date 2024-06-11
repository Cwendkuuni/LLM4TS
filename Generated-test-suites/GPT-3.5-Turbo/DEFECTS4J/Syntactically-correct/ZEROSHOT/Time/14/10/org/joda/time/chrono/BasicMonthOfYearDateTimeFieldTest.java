import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

  private BasicMonthOfYearDateTimeField field;

  @Before
  public void setUp() {
    field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
  }

  @Test
  public void testIsLenient() {
    assertFalse(field.isLenient());
  }

  @Test
  public void testGet() {
    long instant = 1000;
    assertEquals(1, field.get(instant));
  }

  @Test
  public void testAdd() {
    long instant = 1000;
    int months = 5;
    assertEquals(3600000, field.add(instant, months));
  }

  @Test
  public void testAddWrapField() {
    long instant = 1000;
    int months = 5;
    assertEquals(14206000, field.addWrapField(instant, months));
  }

  @Test
  public void testGetDifferenceAsLong() {
    long minuendInstant = 1000;
    long subtrahendInstant = 2000;
    assertEquals(-1, field.getDifferenceAsLong(minuendInstant, subtrahendInstant));
  }

  @Test
  public void testSet() {
    long instant = 1000;
    int month = 10;
    assertEquals(18876000, field.set(instant, month));
  }

  @Test
  public void testIsLeap() {
    long instant = 1000;
    assertFalse(field.isLeap(instant));
  }

  @Test
  public void testGetLeapAmount() {
    long instant = 1000;
    assertEquals(0, field.getLeapAmount(instant));
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
    long instant = 1000;
    assertEquals(946684800000L, field.roundFloor(instant));
  }

  @Test
  public void testRemainder() {
    long instant = 1000;
    assertEquals(1000, field.remainder(instant));
  }
}
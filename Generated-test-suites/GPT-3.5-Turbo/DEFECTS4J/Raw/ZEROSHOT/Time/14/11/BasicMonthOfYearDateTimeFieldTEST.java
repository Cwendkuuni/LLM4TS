import static org.junit.Assert.*;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

  BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);

  @Test
  public void testIsLenient() {
    assertFalse(field.isLenient());
  }

  @Test
  public void testGet() {
    long instant = 0L;
    int expected = 1;
    int actual = field.get(instant);
    assertEquals(expected, actual);
  }

  @Test
  public void testAdd() {
    long instant = 0L;
    int months = 1;
    long expected = 1L;
    long actual = field.add(instant, months);
    assertEquals(expected, actual);
  }

  @Test
  public void testAddWrapField() {
    long instant = 0L;
    int months = 1;
    long expected = 1L;
    long actual = field.addWrapField(instant, months);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetDifferenceAsLong() {
    long minuendInstant = 0L;
    long subtrahendInstant = 0L;
    long expected = 0L;
    long actual = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    assertEquals(expected, actual);
  }

  @Test
  public void testSet() {
    long instant = 0L;
    int month = 1;
    long expected = 0L;
    long actual = field.set(instant, month);
    assertEquals(expected, actual);
  }

  @Test
  public void testIsLeap() {
    long instant = 0L;
    boolean expected = false;
    boolean actual = field.isLeap(instant);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetLeapAmount() {
    long instant = 0L;
    int expected = 0;
    int actual = field.getLeapAmount(instant);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetMinimumValue() {
    int expected = 1;
    int actual = field.getMinimumValue();
    assertEquals(expected, actual);
  }

  @Test
  public void testGetMaximumValue() {
    int expected = 0;
    int actual = field.getMaximumValue();
    assertEquals(expected, actual);
  }

  @Test
  public void testRoundFloor() {
    long instant = 0L;
    long expected = 0L;
    long actual = field.roundFloor(instant);
    assertEquals(expected, actual);
  }

  @Test
  public void testRemainder() {
    long instant = 0L;
    long expected = 0L;
    long actual = field.remainder(instant);
    assertEquals(expected, actual);
  }
}
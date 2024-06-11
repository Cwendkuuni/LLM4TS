import static org.junit.Assert.*;
import org.joda.time.*;
import org.junit.Before;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

  private BaseSingleFieldPeriod period;

  @Before
  public void setUp() {
    period = new BaseSingleFieldPeriod(10) {
      @Override
      public DurationFieldType getFieldType() {
        return DurationFieldType.days();
      }

      @Override
      public PeriodType getPeriodType() {
        return PeriodType.standard();
      }
    };
  }

  @Test
  public void testGetValue() {
    assertEquals(10, period.getValue());
  }

  @Test
  public void testSetValue() {
    period.setValue(20);
    assertEquals(20, period.getValue());
  }

  @Test
  public void testGetFieldType() {
    assertEquals(DurationFieldType.days(), period.getFieldType());
  }

  @Test
  public void testGetPeriodType() {
    assertEquals(PeriodType.standard(), period.getPeriodType());
  }

  @Test
  public void testSize() {
    assertEquals(1, period.size());
  }

  @Test
  public void testGetFieldTypeInt() {
    assertEquals(DurationFieldType.days(), period.getFieldType(0));
    try {
      period.getFieldType(1);
      fail("IndexOutOfBoundsException not thrown");
    } catch (IndexOutOfBoundsException e) {
      // expected
    }
  }

  @Test
  public void testGetValueInt() {
    assertEquals(10, period.getValue(0));
    try {
      period.getValue(1);
      fail("IndexOutOfBoundsException not thrown");
    } catch (IndexOutOfBoundsException e) {
      // expected
    }
  }

  @Test
  public void testGet() {
    assertEquals(10, period.get(DurationFieldType.days()));
    assertEquals(0, period.get(DurationFieldType.hours()));
  }

  @Test
  public void testIsSupported() {
    assertTrue(period.isSupported(DurationFieldType.days()));
    assertFalse(period.isSupported(DurationFieldType.hours()));
  }

  @Test
  public void testToPeriod() {
    Period expected = Period.days(10);
    assertEquals(expected, period.toPeriod());
  }

  @Test
  public void testToMutablePeriod() {
    MutablePeriod expected = new MutablePeriod();
    expected.add(period);
    assertEquals(expected, period.toMutablePeriod());
  }

  @Test
  public void testEquals() {
    BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(10) {
      @Override
      public DurationFieldType getFieldType() {
        return DurationFieldType.days();
      }

      @Override
      public PeriodType getPeriodType() {
        return PeriodType.standard();
      }
    };
    assertTrue(period.equals(other));
    assertFalse(period.equals(null));
    assertFalse(period.equals(new Object()));
    other.setValue(20);
    assertFalse(period.equals(other));
  }

  @Test
  public void testHashCode() {
    int expected = 17;
    expected = 27 * expected + 10;
    expected = 27 * expected + DurationFieldType.days().hashCode();
    assertEquals(expected, period.hashCode());
  }

  @Test
  public void testCompareTo() {
    BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(20) {
      @Override
      public DurationFieldType getFieldType() {
        return DurationFieldType.days();
      }

      @Override
      public PeriodType getPeriodType() {
        return PeriodType.standard();
      }
    };
    assertTrue(period.compareTo(other) < 0);
    assertTrue(other.compareTo(period) > 0);
    other.setValue(10);
    assertEquals(0, period.compareTo(other));
    try {
      period.compareTo(new Object());
      fail("ClassCastException not thrown");
    } catch (ClassCastException e) {
      // expected
    }
  }
}
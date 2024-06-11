import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

  private BaseSingleFieldPeriod baseSingleFieldPeriod;

  @Before
  public void setUp() {
    baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(5);
  }

  @Test
  public void testGetValue() {
    assertEquals(5, baseSingleFieldPeriod.getValue());
  }

  @Test
  public void testSetValue() {
    baseSingleFieldPeriod.setValue(10);
    assertEquals(10, baseSingleFieldPeriod.getValue());
  }

  @Test
  public void testGetFieldType() {
    assertEquals(DurationFieldType.UNDEFINED, baseSingleFieldPeriod.getFieldType());
  }

  @Test
  public void testGetPeriodType() {
    assertEquals(PeriodType.standard(), baseSingleFieldPeriod.getPeriodType());
  }

  @Test
  public void testSize() {
    assertEquals(1, baseSingleFieldPeriod.size());
  }

  @Test
  public void testGetFieldTypeAtIndex() {
    assertEquals(DurationFieldType.UNDEFINED, baseSingleFieldPeriod.getFieldType(0));
  }

  @Test
  public void testGetValueAtIndex() {
    assertEquals(5, baseSingleFieldPeriod.getValue(0));
  }

  @Test
  public void testGet() {
    assertEquals(5, baseSingleFieldPeriod.get(DurationFieldType.UNDEFINED));
  }

  @Test
  public void testIsSupported() {
    assertTrue(baseSingleFieldPeriod.isSupported(DurationFieldType.UNDEFINED));
    assertFalse(baseSingleFieldPeriod.isSupported(DurationFieldType.years()));
  }

  @Test
  public void testToPeriod() {
    Period period = baseSingleFieldPeriod.toPeriod();
    assertEquals(baseSingleFieldPeriod.getValue(), period.get(DurationFieldType.UNDEFINED));
  }

  @Test
  public void testToMutablePeriod() {
    MutablePeriod mutablePeriod = baseSingleFieldPeriod.toMutablePeriod();
    assertEquals(baseSingleFieldPeriod.getValue(), mutablePeriod.get(DurationFieldType.UNDEFINED));
  }

  @Test
  public void testEquals() {
    BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriodImpl(5);
    assertTrue(baseSingleFieldPeriod.equals(baseSingleFieldPeriod2));
    assertFalse(baseSingleFieldPeriod.equals(null));
    assertFalse(baseSingleFieldPeriod.equals("String"));
  }

  @Test
  public void testHashCode() {
    BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriodImpl(5);
    assertEquals(baseSingleFieldPeriod.hashCode(), baseSingleFieldPeriod2.hashCode());
  }

  @Test
  public void testCompareTo() {
    BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriodImpl(10);
    assertTrue(baseSingleFieldPeriod.compareTo(baseSingleFieldPeriod2) < 0);
    assertEquals(0, baseSingleFieldPeriod.compareTo(baseSingleFieldPeriod));
  }

  private class BaseSingleFieldPeriodImpl extends BaseSingleFieldPeriod {
    public BaseSingleFieldPeriodImpl(int period) {
      super(period);
    }
    public DurationFieldType getFieldType() {
      return DurationFieldType.UNDEFINED;
    }
    public PeriodType getPeriodType() {
      return PeriodType.standard();
    }
  }

}
import org.joda.time.DurationFieldType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.junit.Assert;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

  @Test
  public void testBetweenReadbleInstant() {
    ReadableInstant start = null;
    ReadableInstant end = null;
    DurationFieldType field = null;
    try {
      BaseSingleFieldPeriod.between(start, end, field);
      Assert.fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException ex) {
      // expected
    }
  }

  @Test
  public void testBetweenReadablePartial() {
    ReadablePartial start = null;
    ReadablePartial end = null;
    ReadablePeriod zeroInstance = null;
    try {
      BaseSingleFieldPeriod.between(start, end, zeroInstance);
      Assert.fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException ex) {
      // expected
    }
  }

  @Test
  public void testStandardPeriodIn() {
    ReadablePeriod period = null;
    long millisPerUnit = 0L;
    int result = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
    Assert.assertEquals(0, result);
  }

  @Test
  public void testBaseSingleFieldPeriod() {
    int period = 0;
    BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(period);
    Assert.assertEquals(period, baseSingleFieldPeriod.getValue());
  }

  @Test
  public void testGetFieldType() {
    BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(0);
    DurationFieldType fieldType = baseSingleFieldPeriod.getFieldType();
    Assert.assertNotNull(fieldType);
  }

  @Test
  public void testGetPeriodType() {
    BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(0);
    PeriodType periodType = baseSingleFieldPeriod.getPeriodType();
    Assert.assertNotNull(periodType);
  }

  @Test
  public void testSize() {
    BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(0);
    int size = baseSingleFieldPeriod.size();
    Assert.assertEquals(1, size);
  }

  @Test
  public void testGetFieldTypeInt() {
    BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(0);
    try {
      baseSingleFieldPeriod.getFieldType(1);
      Assert.fail("Expected IndexOutOfBoundsException to be thrown");
    } catch (IndexOutOfBoundsException ex) {
      // expected
    }
    DurationFieldType fieldType = baseSingleFieldPeriod.getFieldType(0);
    Assert.assertNotNull(fieldType);
  }

  @Test
  public void testGetValueInt() {
    BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(0);
    try {
      baseSingleFieldPeriod.getValue(1);
      Assert.fail("Expected IndexOutOfBoundsException to be thrown");
    } catch (IndexOutOfBoundsException ex) {
      // expected
    }
    int value = baseSingleFieldPeriod.getValue(0);
    Assert.assertEquals(0, value);
  }

  @Test
  public void testGet() {
    DurationFieldType type = null;
    BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(0);
    int result = baseSingleFieldPeriod.get(type);
    Assert.assertEquals(0, result);
  }

  @Test
  public void testIsSupported() {
    DurationFieldType type = null;
    BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(0);
    boolean result = baseSingleFieldPeriod.isSupported(type);
    Assert.assertTrue(result);
  }

  @Test
  public void testToPeriod() {
    BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(0);
    Period period = baseSingleFieldPeriod.toPeriod();
    Assert.assertNotNull(period);
  }

  @Test
  public void testToMutablePeriod() {
    BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(0);
    MutablePeriod period = baseSingleFieldPeriod.toMutablePeriod();
    Assert.assertNotNull(period);
  }

  @Test
  public void testEquals() {
    BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(0);
    boolean result = baseSingleFieldPeriod.equals(null);
    Assert.assertFalse(result);
  }

  @Test
  public void testHashCode() {
    BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(0);
    int hashCode = baseSingleFieldPeriod.hashCode();
    Assert.assertNotNull(hashCode);
  }

  @Test
  public void testCompareTo() {
    BaseSingleFieldPeriod other = new BaseSingleFieldPeriodImpl(0);
    BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(0);
    try {
      baseSingleFieldPeriod.compareTo(other);
      Assert.fail("Expected ClassCastException to be thrown");
    } catch (ClassCastException ex) {
      // expected
    }
  }

  private static class BaseSingleFieldPeriodImpl extends BaseSingleFieldPeriod {

    private static final long serialVersionUID = 1L;

    public BaseSingleFieldPeriodImpl(int period) {
      super(period);
    }

    @Override
    public DurationFieldType getFieldType() {
      return null;
    }

    @Override
    public PeriodType getPeriodType() {
      return null;
    }
  }
}
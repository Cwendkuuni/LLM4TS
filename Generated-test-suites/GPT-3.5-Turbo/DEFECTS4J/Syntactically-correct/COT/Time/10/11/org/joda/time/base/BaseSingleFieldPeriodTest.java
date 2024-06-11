import org.joda.time.*;
import org.junit.*;

public class BaseSingleFieldPeriodTest {

    // Test for null input in the between method (ReadableInstant)
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_NullReadableInstant() {
        BaseSingleFieldPeriod.between(null, new DateTime(), DurationFieldType.days());
    }

    // Test for null input in the between method (ReadablePartial)
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_NullReadablePartial() {
        BaseSingleFieldPeriod.between(new LocalDate(), null, new Period());
    }

    // Test for null input in the standardPeriodIn method (ReadablePeriod)
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodIn_NullReadablePeriod() {
        BaseSingleFieldPeriod.standardPeriodIn(null, 1000);
    }

    // Test for negative period value in the constructor
    @Test
    public void testConstructor_NegativePeriodValue() {
        BaseSingleFieldPeriod period = new TestBaseSingleFieldPeriod(-5);
        Assert.assertEquals(-5, period.getValue());
    }

    // Test for zero period value in the constructor
    @Test
    public void testConstructor_ZeroPeriodValue() {
        BaseSingleFieldPeriod period = new TestBaseSingleFieldPeriod(0);
        Assert.assertEquals(0, period.getValue());
    }

    // Test for non-zero period value in the constructor
    @Test
    public void testConstructor_NonZeroPeriodValue() {
        BaseSingleFieldPeriod period = new TestBaseSingleFieldPeriod(10);
        Assert.assertEquals(10, period.getValue());
    }

    // Test for different field types in the get() method
    @Test
    public void testGet_DifferentFieldType() {
        BaseSingleFieldPeriod period = new TestBaseSingleFieldPeriod(10);
        Assert.assertEquals(0, period.get(DurationFieldType.days()));
    }

    // Test for null input in the isSupported() method
    @Test
    public void testIsSupported_NullDurationFieldType() {
        BaseSingleFieldPeriod period = new TestBaseSingleFieldPeriod(10);
        Assert.assertFalse(period.isSupported(null));
    }

    // Test for comparing with a different class in the compareTo() method
    @Test(expected = ClassCastException.class)
    public void testCompareTo_DifferentClass() {
        BaseSingleFieldPeriod period1 = new TestBaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new TestBaseSingleFieldPeriod(5);
        period1.compareTo(period2);
    }

    // Custom test class extending BaseSingleFieldPeriod for testing purposes
    class TestBaseSingleFieldPeriod extends BaseSingleFieldPeriod {

        protected TestBaseSingleFieldPeriod(int period) {
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
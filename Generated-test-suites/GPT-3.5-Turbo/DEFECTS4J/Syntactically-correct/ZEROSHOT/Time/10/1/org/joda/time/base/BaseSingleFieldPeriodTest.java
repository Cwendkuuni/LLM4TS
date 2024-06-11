import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.junit.Assert;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween_Instants_Valid() {
        // Arrange
        ReadableInstant start = null; // TODO: Initialize start instant
        ReadableInstant end = null; // TODO: Initialize end instant
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(0);

        // Act
        int period = baseSingleFieldPeriod.between(start, end, null);

        // Assert
        Assert.assertEquals(0, period);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_Instants_InvalidStart() {
        // Arrange
        ReadableInstant start = null; // TODO: Initialize start instant
        ReadableInstant end = null; // TODO: Initialize end instant
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(0);

        // Act
        int period = baseSingleFieldPeriod.between(start, end, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_Instants_InvalidEnd() {
        // Arrange
        ReadableInstant start = null; // TODO: Initialize start instant
        ReadableInstant end = null; // TODO: Initialize end instant
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(0);

        // Act
        int period = baseSingleFieldPeriod.between(start, end, null);
    }

    @Test
    public void testBetween_Partials_Valid() {
        // Arrange
        ReadablePartial start = null; // TODO: Initialize start partial
        ReadablePartial end = null; // TODO: Initialize end partial
        ReadablePeriod zeroInstance = null; // TODO: Initialize zeroInstance
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(0);

        // Act
        int period = baseSingleFieldPeriod.between(start, end, zeroInstance);

        // Assert
        Assert.assertEquals(0, period);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_Partials_InvalidStart() {
        // Arrange
        ReadablePartial start = null; // TODO: Initialize start partial
        ReadablePartial end = null; // TODO: Initialize end partial
        ReadablePeriod zeroInstance = null; // TODO: Initialize zeroInstance
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(0);

        // Act
        int period = baseSingleFieldPeriod.between(start, end, zeroInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_Partials_InvalidEnd() {
        // Arrange
        ReadablePartial start = null; // TODO: Initialize start partial
        ReadablePartial end = null; // TODO: Initialize end partial
        ReadablePeriod zeroInstance = null; // TODO: Initialize zeroInstance
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(0);

        // Act
        int period = baseSingleFieldPeriod.between(start, end, zeroInstance);
    }

    @Test
    public void testStandardPeriodIn() {
        // Arrange
        ReadablePeriod period = null; // TODO: Initialize period
        long millisPerUnit = 0; // TODO: Initialize millisPerUnit
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(0);

        // Act
        int standardPeriodIn = baseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);

        // Assert
        Assert.assertEquals(0, standardPeriodIn);
    }

    @Test
    public void testEquals_EqualPeriods() {
        // Arrange
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(0);
        Object period = new Period();

        // Act
        boolean equals = baseSingleFieldPeriod.equals(period);

        // Assert
        Assert.assertTrue(equals);
    }

    @Test
    public void testEquals_NotEqualPeriods() {
        // Arrange
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(0);
        Object period = new Period().withYears(1);

        // Act
        boolean equals = baseSingleFieldPeriod.equals(period);

        // Assert
        Assert.assertFalse(equals);
    }

    @Test
    public void testHashCode() {
        // Arrange
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(0);

        // Act
        int hashCode = baseSingleFieldPeriod.hashCode();

        // Assert
        Assert.assertNotNull(hashCode);
    }

    @Test
    public void testCompareTo_GreaterPeriod() {
        // Arrange
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriodImpl(0);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriodImpl(1);

        // Act
        int compareTo = period1.compareTo(period2);

        // Assert
        Assert.assertEquals(-1, compareTo);
    }

    @Test
    public void testCompareTo_SmallerPeriod() {
        // Arrange
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriodImpl(1);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriodImpl(0);

        // Act
        int compareTo = period1.compareTo(period2);

        // Assert
        Assert.assertEquals(1, compareTo);
    }

    private class BaseSingleFieldPeriodImpl extends BaseSingleFieldPeriod {

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
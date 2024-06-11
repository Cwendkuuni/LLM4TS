import org.junit.Test;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        assertEquals(1, period.size());
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
    }

    @Test
    public void testGetValueInvalidIndex() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        try {
            period.getValue(1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        assertTrue(period.isSupported(DurationFieldType.DAYS));
        assertFalse(period.isSupported(DurationFieldType.HOURS));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        Period result = period.toPeriod();
        assertEquals(5, result.getDays());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(5, result.getDays());
    }

    @Test
    public void testEqualsSameObject() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        assertTrue(period.equals(period));
    }

    @Test
    public void testEqualsNull() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        assertFalse(period.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        assertFalse(period.equals(new Object()));
    }

    @Test
    public void testEqualsDifferentPeriod() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(3);
        assertFalse(period1.equals(period2));
    }

    @Test
    public void testEqualsSamePeriod() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(5);
        assertTrue(period1.equals(period2));
    }

    @Test
    public void testHashCodeSameObject() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        int hashCode1 = period.hashCode();
        period = period;
        int hashCode2 = period.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testHashCodeDifferentObjects() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(3);
        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testCompareToSameObject() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = period1;
        int result = period1.compareTo(period2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareToZero() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(5);
        int result = period1.compareTo(BaseSingleFieldPeriod.ZERO);
        assertTrue(result > 0);
    }

    @Test
    public void testCompareToNegative() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(-5);
        int result = period1.compareTo(BaseSingleFieldPeriod.ZERO);
        assertTrue(result < 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullStart() {
        BaseSingleFieldPeriod.between(null, new TestReadableInstant(1L), DurationFieldType.DAYS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullEnd() {
        BaseSingleFieldPeriod.between(new TestReadableInstant(1L), null, DurationFieldType.DAYS);
    }

    @Test
    public void testBetween() {
        TestReadableInstant start = new TestReadableInstant(1L);
        TestReadableInstant end = new TestReadableInstant(2L);
        assertEquals(1, BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS));
    }

    @Test
    public void testStandardPeriodInZeroPeriod() {
        Period period = Period.ZERO;
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1000);
        assertEquals(0, result);
    }

    @Test
    public void testStandardPeriodInOneDayPeriod() {
        Period period = new Period(1, DurationFieldType.DAYS);
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1000);
        assertEquals(86400000, result); // 1 day = 86,400,000 milliseconds
    }

    @Test
    public void testStandardPeriodInOneWeekPeriod() {
        Period period = new Period(7, DurationFieldType.DAYS);
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1000);
        assertEquals(604800000, result); // 1 week = 604,800,000 milliseconds
    }

    static class TestSingleFieldPeriod extends BaseSingleFieldPeriod {
        TestSingleFieldPeriod(int period) {
            super(period);
        }

        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.DAYS;
        }
    }

    static class TestReadableInstant implements ReadableInstant {
        private final long instant;

        TestReadableInstant(long instant) {
            this.instant = instant;
        }

        @Override
        public long getMillis() {
            return instant;
        }

        @Override
        public Chronology getChronology() {
            return ISOChronology.getInstanceUTC();
        }
    }

}
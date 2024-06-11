import org.junit.Test;
import org.junit.Assert.*;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.Period;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testConstructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = period.getFieldType();
        assertEquals(DurationFieldType.DAYS, fieldType);
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int size = period.size();
        assertEquals(1, size);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int value = period.getValue();
        assertEquals(5, value);
    }

    @Test
    public void testGetValueInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        try {
            period.getValue(1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testGetFieldTypeInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        try {
            period.getFieldType(1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        assertTrue(period1.equals(period2));
    }

    @Test
    public void testNotEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3);
        assertFalse(period1.equals(period2));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testCompareToSame() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        int result = period1.compareTo(period2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareToGreater() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3);
        int result = period1.compareTo(period2);
        assertEquals(1, result);
    }

    @Test
    public void testCompareToLess() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        int result = period1.compareTo(period2);
        assertEquals(-1, result);
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Period result = period.toPeriod();
        assertEquals(new Period(5, PeriodUnit.DAYS), result);
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(new MutablePeriod(5, PeriodUnit.DAYS), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullInstants() {
        BaseSingleFieldPeriod.between(null, null, DurationFieldType.DAYS);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenInvalidInstant() {
        ReadableInstant start = DateTimeUtils.currentTime().plusDays(1);
        ReadableInstant end = null;
        try {
            BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBetweenSameInstant() {
        ReadableInstant instant = DateTimeUtils.currentTime();
        int result = BaseSingleFieldPeriod.between(instant, instant, DurationFieldType.DAYS);
        assertEquals(0, result);
    }

    @Test
    public void testBetweenDifferentInstantDays() {
        ReadableInstant start = DateTimeUtils.currentTime();
        ReadableInstant end = start.plusDays(2);
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        assertEquals(2, result);
    }

    @Test
    public void testBetweenDifferentInstantHours() {
        ReadableInstant start = DateTimeUtils.currentTime();
        ReadableInstant end = start.plusHours(3);
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.HOURS);
        assertEquals(3, result);
    }

    @Test
    public void testBetweenDifferentInstantMinutes() {
        ReadableInstant start = DateTimeUtils.currentTime();
        ReadableInstant end = start.plusMinutes(45);
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.MINUTES);
        assertEquals(45, result);
    }

    @Test
    public void testBetweenDifferentInstantSeconds() {
        ReadableInstant start = DateTimeUtils.currentTime();
        ReadableInstant end = start.plusSeconds(60 * 60 * 24 + 30 * 60 + 10);
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.SECONDS);
        assertEquals(60 * 60 * 24 + 30 * 60 + 10, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenInvalidPartial() {
        ReadablePartial start = null;
        ReadablePartial end = new org.joda.time.LocalDate(2022, 1, 1);
        BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenInvalidZeroInstance() {
        ReadablePartial start = new org.joda.time.LocalDate(2022, 1, 1);
        ReadablePartial end = null;
        BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenInvalidChronology() {
        ReadablePartial start = new org.joda.time.LocalDate(2022, 1, 1).withChronology(DateTimeUtils.getCZ());
        ReadablePartial end = new org.joda.time.LocalDate(2022, 1, 1);
        BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testBetweenSamePartial() {
        ReadablePartial start = new org.joda.time.LocalDate(2022, 1, 1);
        ReadablePartial end = start;
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        assertEquals(0, result);
    }

    @Test
    public void testBetweenDifferentFieldSize() {
        ReadablePartial start = new org.joda.time.LocalDate(2022, 1, 1);
        ReadablePartial end = new org.joda.time.LocalDateTime(2022, 1, 1, 12, 30, 0);
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        assertEquals(0, result);
    }

    @Test
    public void testBetweenDifferentPartialDays() {
        ReadablePartial start = new org.joda.time.LocalDate(2022, 1, 1);
        ReadablePartial end = new org.joda.time.LocalDate(2022, 1, 3);
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        assertEquals(2, result);
    }

    @Test
    public void testBetweenDifferentPartialHours() {
        ReadablePartial start = new org.joda.time.LocalDateTime(2022, 1, 1, 12, 30, 0);
        ReadablePartial end = new org.joda.time.LocalDateTime(2022, 1, 1, 14, 0, 0);
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.HOURS);
        assertEquals(2, result);
    }

    @Test
    public void testBetweenDifferentPartialMinutes() {
        ReadablePartial start = new org.joda.time.LocalDateTime(2022, 1, 1, 12, 30, 0);
        ReadablePartial end = new org.joda.time.LocalDateTime(2022, 1, 1, 12, 45, 0);
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.MINUTES);
        assertEquals(15, result);
    }

    @Test
    public void testBetweenDifferentPartialSeconds() {
        ReadablePartial start = new org.joda.time.LocalDateTime(2022, 1, 1, 12, 30, 0);
        ReadablePartial end = new org.joda.time.LocalDateTime(2022, 1, 1, 12, 31, 10);
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.SECONDS);
        assertEquals(11, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInNullPeriod() {
        BaseSingleFieldPeriod.standardPeriodIn(null, 1000L);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInImprecisePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.MONTHS;
            }
        };
        BaseSingleFieldPeriod.standardPeriodIn(period, 1000L);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testStandardPeriodInDays() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1000L);
        assertEquals(5, result);
    }

    @Test
    public void testStandardPeriodInHours() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(12);
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1000L);
        assertEquals(12 * 24, result);
    }

    @Test
    public void testStandardPeriodInMinutes() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(60 * 30);
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1000L);
        assertEquals(60 * 30, result);
    }

    @Test
    public void testStandardPeriodInSeconds() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1000 * 60 * 60 * 24 * 30);
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1000L);
        assertEquals(1000 * 60 * 60 * 24 * 30, result);
    }

}
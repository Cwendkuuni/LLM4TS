import org.junit.Test;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testConstructor_withValidPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        DurationFieldType fieldType = DurationFieldType.days();
        assertEquals(7, period.getValue());
        assertEquals(fieldType, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        assertEquals(7, period.getValue());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        period.setValue(5);
        assertEquals(5, period.getValue());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        DurationFieldType expected = DurationFieldType.days();
        assertEquals(expected, period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        PeriodType expected = PeriodType.standard();
        assertEquals(expected, period.getPeriodType());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        int expected = 1;
        assertEquals(expected, period.size());
    }

    @Test
    public void testGetFieldType_invalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        try {
            period.getFieldType(2);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testGetValue_invalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        try {
            period.getValue(2);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testGet_validFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        int expected = 7;
        assertEquals(expected, period.get(DurationFieldType.days()));
    }

    @Test
    public void testIsSupported_validFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        boolean expected = true;
        assertTrue(expected == period.isSupported(DurationFieldType.days()));
    }

    @Test
    public void testIsSupported_invalidFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        boolean expected = false;
        assertTrue(expected == period.isSupported(DurationFieldType.hours()));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        Period expected = new Period(7, 0, 0, 0);
        assertEquals(expected, period.toPeriod());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        MutablePeriod expected = new MutablePeriod();
        expected.add(PeriodType.standard(), 7);
        assertEquals(expected, period.toMutablePeriod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEquals_null() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        assertEquals(period, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEquals_invalidClass() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        assertEquals(period, new Object());
    }

    @Test
    public void testEquals_sameInstance() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(7);
        BaseSingleFieldPeriod period2 = period1;
        assertTrue(period1.equals(period2));
    }

    @Test
    public void testEquals_sameValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(7);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(7);
        assertTrue(period1.equals(period2));
    }

    @Test
    public void testEquals_differentValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(7);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        assertFalse(period1.equals(period2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEquals_differentFieldType() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(7, DurationFieldType.days());
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5, DurationFieldType.hours());
        assertEquals(period1, period2);
        fail();
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        int expected = 346109541; // calculated based on JodaTime source code
        int hashCode = period.hashCode();
        assertEquals(expected, hashCode);
    }

    @Test
    public void testCompareTo_less() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        int expected = -2;
        assertEquals(expected, period1.compareTo(period2));
    }

    @Test
    public void testCompareTo_equal() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        int expected = 0;
        assertEquals(expected, period1.compareTo(period2));
    }

    @Test
    public void testCompareTo_greater() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(7);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        int expected = 2;
        assertEquals(expected, period1.compareTo(period2));
    }

    @Test
    public void testStandardPeriodIn() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3652);
        long millisPerUnit = DateTimeUtils.MILLIS_PER_DAY;
        int expected = 10;
        assertEquals(expected, BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodIn_invalidPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3652);
        long millisPerUnit = DateTimeUtils.MILLIS_PER_HOUR;
        BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        fail();
    }

    @Test
    public void testBetween_validInstants() {
        ReadableInstant start = DateTimeUtils.currentTime().withMillisecondOfSecond(0).withSecondOfMinute(0).withMinuteOfHour(0).withHourOfDay(0).withDayOfMonth(1).withMonthOfYear(1).withYear(2022);
        ReadableInstant end = DateTimeUtils.currentTime().withMillisecondOfSecond(1).withSecondOfMinute(1).withMinuteOfHour(1).withHourOfDay(1).withDayOfMonth(1).withMonthOfYear(1).withYear(2022);
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(DurationFieldType.days().getField(DateTimeUtils.getInstantChronology(start)).getDifference(end.getMillis(), start.getMillis()));
        int expected = 1;
        assertEquals(expected, BaseSingleFieldPeriod.between(start, end, DurationFieldType.days()));
        assertEquals(expected, period.getValue());
    }

    @Test
    public void testBetween_invalidInstants() {
        ReadableInstant start = null;
        ReadableInstant end = null;
        try {
            BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBetween_partials() {
        ReadablePartial start = DateTimeUtils.currentTime().withField(DateTimeConstants.MILLI_SECONDS, 0).withField(DateTimeConstants.SECONDS, 0).withField(DateTimeConstants.MINUTES, 0).withField(DateTimeConstants.HOURS, 0).withField(DateTimeConstants.DAY_OF_MONTH, 1).withField(DateTimeConstants.MONTH_OF_YEAR, 1).withField(DateTimeConstants.YEAR, 2022);
        ReadablePartial end = DateTimeUtils.currentTime().withField(DateTimeConstants.MILLI_SECONDS, 1).withField(DateTimeConstants.SECONDS, 1).withField(DateTimeConstants.MINUTES, 1).withField(DateTimeConstants.HOURS, 1).withField(DateTimeConstants.DAY_OF_MONTH, 1).withField(DateTimeConstants.MONTH_OF_YEAR, 1).withField(DateTimeConstants.YEAR, 2022);
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(BaseSingleFieldPeriod.between(start, end, DateTimeUtils.getChronology(start).withUTC()));
        int expected = 1;
        assertEquals(expected, period.getValue());
    }

    @Test
    public void testBetween_invalidPartition() {
        ReadablePartial start = null;
        ReadablePartial end = null;
        try {
            BaseSingleFieldPeriod.between(start, end, DateTimeUtils.getChronology(start).withUTC());
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * BaseSingleFieldPeriodTest.
 */
public class BaseSingleFieldPeriodTest {

    //-----------------------------------------------------------------------
    @Test
    public void testInstance() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        assertNotNull(period);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInstance() {
        new BaseSingleFieldPeriod(-1);
    }

    //-----------------------------------------------------------------------
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        DurationFieldType fieldType = period.getFieldType();
        assertNotNull(fieldType);
        assertEquals(DurationFieldType.DAYS, fieldType);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetFieldTypeAfterSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        period.setValue(2);
        period.getFieldType();
    }

    //-----------------------------------------------------------------------
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        PeriodType periodType = period.getPeriodType();
        assertNotNull(periodType);
        assertEquals(PeriodType.standard(), periodType);
    }

    //-----------------------------------------------------------------------
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        assertEquals(1, period.size());
    }

    @Test
    public void testGetFieldTypeAndValueAtIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        DurationFieldType fieldType = period.getFieldType(0);
        assertEquals(DurationFieldType.DAYS, fieldType);
        int value = period.getValue(0);
        assertEquals(1, value);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeAndValueAtInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        period.getFieldType(1);
    }

    //-----------------------------------------------------------------------
    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        int value = period.getValue();
        assertEquals(1, value);
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        period.setValue(2);
        assertEquals(2, period.getValue());
    }

    //-----------------------------------------------------------------------
    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        int value = period.get(DurationFieldType.DAYS);
        assertEquals(1, value);
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        assertTrue(period.isSupported(DurationFieldType.DAYS));
        assertFalse(period.isSupported(DurationFieldType.HOURS));
    }

    //-----------------------------------------------------------------------
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(2);
        Period result = period.toPeriod();
        assertEquals(PeriodType.standard(), result.getPeriodType());
        assertEquals(2, result.getValue(DurationFieldType.DAYS));
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(2);
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(2, result.get(DurationFieldType.DAYS));
    }

    //-----------------------------------------------------------------------
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(2);
        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(1);
        assertEquals(period1, period1);
        assertEquals(period1, period4);
        assertFalse(period1.equals(period2));
        assertFalse(period1.equals(period3));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(2);
        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(1);
        assertEquals(period1.hashCode(), period1.hashCode());
        assertEquals(period1.hashCode(), period4.hashCode());
        assertNotEquals(period1.hashCode(), period2.hashCode());
        assertNotEquals(period1.hashCode(), period3.hashCode());
    }

    //-----------------------------------------------------------------------
    @Test
    public void testCompareToSameInstance() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period2 = period1;
        assertEquals(0, period1.compareTo(period2));
    }

    @Test
    public void testCompareToZeroInstance() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
        assertEquals(1, period1.compareTo(BaseSingleFieldPeriod.ZERO));
    }

    @Test
    public void testCompareToNegativeInstance() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(-1);
        assertEquals(-1, period1.compareTo(period2));
    }

    @Test
    public void testCompareToGreaterInstance() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(2);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1);
        assertEquals(1, period1.compareTo(period2));
    }

    //-----------------------------------------------------------------------
    @Test
    public void testBetween() {
        ReadableInstant start = DateTimeUtils.currentTime().withFieldAdded(DurationFieldType.MILLISECONDS, 1);
        ReadableInstant end = DateTimeUtils.currentTime().withFieldAdded(DurationFieldType.MILLISECONDS, 2000);
        int value = BaseSingleFieldPeriod.between(start, end, DurationFieldType.MILLISECONDS);
        assertEquals(1999, value);
    }

    @Test
    public void testBetweenWithZeroInstance() {
        ReadablePartial start = new org.joda.time.local.LocalDate();
        ReadablePartial end = new org.joda.time.local.LocalDate();
        int value = BaseSingleFieldPeriod.between(start, end, PeriodType.standard());
        assertEquals(0, value);
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new Period(1, 2, 3, 4, 5, 6, 7);
        int value = BaseSingleFieldPeriod.standardPeriodIn(period, DurationFieldType.MILLISECONDS.getMillisPerSecond());
        assertEquals(84672000L, value);
    }

}
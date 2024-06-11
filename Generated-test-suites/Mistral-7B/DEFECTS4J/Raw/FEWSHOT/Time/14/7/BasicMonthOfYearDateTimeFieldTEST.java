package org.joda.time.chrono;

import static org.junit.Assert.*;

import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.ReadablePartial;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;

public class BasicMonthOfYearDateTimeFieldTest {

    private static final BasicMonthOfYearDateTimeField monthOfYear = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 2);

    @Test
    public void testBasicConstruction() {
        assertNotNull(monthOfYear);
        assertEquals(DateTimeFieldType.monthOfYear(), monthOfYear.getFieldType());
        assertEquals(GregorianChronology.INSTANCE, monthOfYear.getChronology());
        assertEquals(2, monthOfYear.getMaximumValue());
    }

    @Test
    public void testGet() {
        long instant = DateTimeConstants.MILLIS_PER_DAY * 30;
        assertEquals(1, monthOfYear.get(instant));
        long instantLeap = DateTimeConstants.MILLIS_PER_YEAR * 2 - DateTimeConstants.MILLIS_PER_DAY;
        assertEquals(2, monthOfYear.get(instantLeap));
    }

    @Test
    public void testAdd() {
        long instant = DateTimeConstants.MILLIS_PER_YEAR - DateTimeConstants.MILLIS_PER_MONTH;
        long result = monthOfYear.add(instant, 12);
        assertEquals(DateTimeConstants.MILLIS_PER_YEAR * 2, result);

        long instantNegative = DateTimeConstants.MILLIS_PER_YEAR - DateTimeConstants.MILLIS_PER_MONTH * 2;
        result = monthOfYear.add(instantNegative, -12);
        assertEquals(DateTimeConstants.MILLIS_PER_YEAR, result);

        long instantEdge = DateTimeConstants.MILLIS_PER_YEAR - DateTimeConstants.MILLIS_PER_MONTH * 11;
        result = monthOfYear.add(instantEdge, 1);
        assertEquals(DateTimeConstants.MILLIS_PER_YEAR, result);

        result = monthOfYear.add(instantEdge, 2);
        assertEquals(DateTimeConstants.MILLIS_PER_YEAR + DateTimeConstants.MILLIS_PER_MONTH, result);

        long instantLeap = DateTimeConstants.MILLIS_PER_YEAR - DateTimeConstants.MILLIS_PER_MONTH * 2 + DateTimeConstants.MILLIS_PER_DAY;
        result = monthOfYear.add(instantLeap, 1);
        assertEquals(DateTimeConstants.MILLIS_PER_YEAR + DateTimeConstants.MILLIS_PER_MONTH + DateTimeConstants.MILLIS_PER_DAY, result);

        result = monthOfYear.add(instantLeap, 2);
        assertEquals(DateTimeConstants.MILLIS_PER_YEAR + DateTimeConstants.MILLIS_PER_MONTH * 2, result);
    }

    @Test
    public void testAddWrapField() {
        long instant = DateTimeConstants.MILLIS_PER_YEAR - DateTimeConstants.MILLIS_PER_MONTH * 12;
        long result = monthOfYear.addWrapField(instant, 13);
        assertEquals(DateTimeConstants.MILLIS_PER_YEAR * 2, result);

        instant = DateTimeConstants.MILLIS_PER_YEAR - DateTimeConstants.MILLIS_PER_MONTH * 12;
        result = monthOfYear.addWrapField(instant, -13);
        assertEquals(DateTimeConstants.MILLIS_PER_YEAR - DateTimeConstants.MILLIS_PER_MONTH * 11, result);
    }

    @Test
    public void testGetDifferenceAsLong() {
        long minuendInstant = DateTimeConstants.MILLIS_PER_DAY * 30;
        long subtrahendInstant = DateTimeConstants.MILLIS_PER_DAY * 20;
        long difference = monthOfYear.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(1, difference);

        long minuendInstantLeap = DateTimeConstants.MILLIS_PER_YEAR * 2 - DateTimeConstants.MILLIS_PER_DAY;
        long subtrahendInstantLeap = DateTimeConstants.MILLIS_PER_YEAR;
        difference = monthOfYear.getDifferenceAsLong(minuendInstantLeap, subtrahendInstantLeap);
        assertEquals(11, difference);

        long minuendInstantEdge = DateTimeConstants.MILLIS_PER_YEAR * 2 - DateTimeConstants.MILLIS_PER_MONTH * 11;
        long subtrahendInstantEdge = minuendInstantEdge + DateTimeConstants.MILLIS_PER_DAY;
        difference = monthOfYear.getDifferenceAsLong(minuendInstantEdge, subtrahendInstantEdge);
        assertEquals(1, difference);

        long minuendInstantLeapEdge = DateTimeConstants.MILLIS_PER_YEAR * 2 - DateTimeConstants.MILLIS_PER_MONTH * 11 + DateTimeConstants.MILLIS_PER_DAY;
        long subtrahendInstantLeapEdge = minuendInstantLeapEdge + DateTimeConstants.MILLIS_PER_MONTH;
        difference = monthOfYear.getDifferenceAsLong(minuendInstantLeapEdge, subtrahendInstantLeapEdge);
        assertEquals(0, difference);
    }

    @Test
    public void testSet() {
        long instant = DateTimeConstants.MILLIS_PER_DAY * 30;
        long result = monthOfYear.set(instant, 2);
        assertEquals(DateTimeConstants.MILLIS_PER_YEAR + DateTimeConstants.MILLIS_PER_MONTH * 2 - DateTimeConstants.MILLIS_PER_DAY, result);

        long instantEdge = DateTimeConstants.MILLIS_PER_YEAR - DateTimeConstants.MILLIS_PER_MONTH * 11;
        result = monthOfYear.set(instantEdge, 1);
        assertEquals(DateTimeConstants.MILLIS_PER_YEAR - DateTimeConstants.MILLIS_PER_MONTH * 10, result);

        result = monthOfYear.set(instantEdge, 3);
        assertEquals(DateTimeConstants.MILLIS_PER_YEAR - DateTimeConstants.MILLIS_PER_MONTH * 10 + DateTimeConstants.MILLIS_PER_MONTH, result);

        long instantLeap = DateTimeConstants.MILLIS_PER_YEAR - DateTimeConstants.MILLIS_PER_MONTH * 2 + DateTimeConstants.MILLIS_PER_DAY;
        result = monthOfYear.set(instantLeap, 2);
        assertEquals(DateTimeConstants.MILLIS_PER_YEAR + DateTimeConstants.MILLIS_PER_MONTH * 2 + DateTimeConstants.MILLIS_PER_DAY, result);

        result = monthOfYear.set(instantLeap, 3);
        assertEquals(DateTimeConstants.MILLIS_PER_YEAR + DateTimeConstants.MILLIS_PER_MONTH * 3, result);
    }

    @Test
    public void testGetRangeDurationField() {
        DurationField result = monthOfYear.getRangeDurationField();
        assertEquals(DurationFieldType.years(), result.getFieldType());
    }

    @Test
    public void testIsLeap() {
        long instant = DateTimeConstants.MILLIS_PER_YEAR - DateTimeConstants.MILLIS_PER_MONTH * 2 + DateTimeConstants.MILLIS_PER_DAY;
        assertTrue(monthOfYear.isLeap(instant));

        long instantNegative = DateTimeConstants.MILLIS_PER_YEAR - DateTimeConstants.MILLIS_PER_MONTH * 2 - DateTimeConstants.MILLIS_PER_DAY;
        assertFalse(monthOfYear.isLeap(instantNegative));
    }

    @Test
    public void testGetLeapAmount() {
        long instantLeap = DateTimeConstants.MILLIS_PER_YEAR - DateTimeConstants.MILLIS_PER_MONTH * 2 + DateTimeConstants.MILLIS_PER_DAY;
        assertEquals(1, monthOfYear.getLeapAmount(instantLeap));

        long instantLeapNegative = DateTimeConstants.MILLIS_PER_YEAR - DateTimeConstants.MILLIS_PER_MONTH * 2 - DateTimeConstants.MILLIS_PER_DAY;
        assertEquals(0, monthOfYear.getLeapAmount(instantLeapNegative));
    }

    @Test
    public void testGetLeapDurationField() {
        DurationField result = monthOfYear.getLeapDurationField();
        assertEquals(DurationFieldType.days(), result.getFieldType());
    }

    @Test
    public void testGetMinimumValue() {
        assertEquals(DateTimeConstants.JANUARY, monthOfYear.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        assertEquals(DateTimeConstants.DECEMBER, monthOfYear.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        long instant = DateTimeConstants.MILLIS_PER_YEAR - DateTimeConstants.MILLIS_PER_MONTH * 11;
        long result = monthOfYear.roundFloor(instant);
        assertEquals(DateTimeConstants.MILLIS_PER_YEAR - DateTimeConstants.MILLIS_PER_MONTH * 11, result);

        long instantLeap = DateTimeConstants.MILLIS_PER_YEAR - DateTimeConstants.MILLIS_PER_MONTH * 11 + DateTimeConstants.MILLIS_PER_DAY;
        result = monthOfYear.roundFloor(instantLeap);
        assertEquals(DateTimeConstants.MILLIS_PER_YEAR - DateTimeConstants.MILLIS_PER_MONTH * 11, result);
    }

    @Test
    public void testRemainder() {
        long instant = DateTimeConstants.MILLIS_PER_YEAR - DateTimeConstants.MILLIS_PER_MONTH * 11;
        long result = monthOfYear.remainder(instant);
        assertEquals(DateTimeConstants.MILLIS_PER_MONTH, result);

        long instantLeap = DateTimeConstants.MILLIS_PER_YEAR - DateTimeConstants.MILLIS_PER_MONTH * 11 + DateTimeConstants.MILLIS_PER_DAY;
        result = monthOfYear.remainder(instantLeap);
        assertEquals(DateTimeConstants.MILLIS_PER_DAY, result);
    }
}
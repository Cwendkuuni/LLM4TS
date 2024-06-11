package org.joda.time.chrono;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField field;
    private BasicChronology iChronology;

    @Before
    public void setUp() {
        iChronology = new BasicChronology();
        field = new BasicMonthOfYearDateTimeField(iChronology, 6);
    }

    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        long instant = iChronology.getYearMonthDayMillis(2021, 10, 15);
        assertEquals(10, field.get(instant));
    }

    @Test
    public void testAddInt() {
        long instant = iChronology.getYearMonthDayMillis(2021, 10, 15);
        long result = field.add(instant, 2);
        assertEquals(iChronology.getYearMonthDayMillis(2021, 12, 15), result);
    }

    @Test
    public void testAddLong() {
        long instant = iChronology.getYearMonthDayMillis(2021, 10, 15);
        long result = field.add(instant, 2L);
        assertEquals(iChronology.getYearMonthDayMillis(2021, 12, 15), result);
    }

    @Test
    public void testAddWrapField() {
        long instant = iChronology.getYearMonthDayMillis(2021, 10, 15);
        long result = field.addWrapField(instant, 2);
        assertEquals(iChronology.getYearMonthDayMillis(2021, 12, 15), result);
    }

    @Test
    public void testGetDifferenceAsLong() {
        long instant1 = iChronology.getYearMonthDayMillis(2021, 10, 15);
        long instant2 = iChronology.getYearMonthDayMillis(2022, 6, 10);
        long result = field.getDifferenceAsLong(instant1, instant2);
        assertEquals(7, result);
    }

    @Test
    public void testSet() {
        long instant = iChronology.getYearMonthDayMillis(2021, 10, 15);
        long result = field.set(instant, 12);
        assertEquals(iChronology.getYearMonthDayMillis(2021, 12, 15), result);
    }

    @Test
    public void testGetRangeDurationField() {
        DurationField durationField = field.getRangeDurationField();
        assertNotNull(durationField);
        assertEquals(iChronology.years(), durationField);
    }

    @Test
    public void testIsLeap() {
        long leapInstant = iChronology.getYearMonthDayMillis(2020, 6, 15); // Leap year
        long nonLeapInstant = iChronology.getYearMonthDayMillis(2021, 6, 15); // Non-leap year

        assertTrue(field.isLeap(leapInstant));
        assertFalse(field.isLeap(nonLeapInstant));
    }

    @Test
    public void testGetLeapAmount() {
        long leapInstant = iChronology.getYearMonthDayMillis(2020, 6, 15); // Leap year
        long nonLeapInstant = iChronology.getYearMonthDayMillis(2021, 6, 15); // Non-leap year

        assertEquals(1, field.getLeapAmount(leapInstant));
        assertEquals(0, field.getLeapAmount(nonLeapInstant));
    }

    @Test
    public void testGetLeapDurationField() {
        DurationField durationField = field.getLeapDurationField();
        assertNotNull(durationField);
        assertEquals(iChronology.days(), durationField);
    }

    @Test
    public void testGetMinimumValue() {
        assertEquals(1, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        assertEquals(DateTimeConstants.MONTHS_PER_YEAR, field.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        long instant = iChronology.getYearMonthDayMillis(2021, 10, 15);
        long result = field.roundFloor(instant);
        assertEquals(iChronology.getYearMonthMillis(2021, 10), result);
    }

    @Test
    public void testRemainder() {
        long instant = iChronology.getYearMonthDayMillis(2021, 10, 15);
        long result = field.remainder(instant);
        assertEquals(15, result);
    }
}
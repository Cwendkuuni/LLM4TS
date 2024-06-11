package org.joda.time.chrono;

import static org.junit.Assert.*;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.ReadablePartial;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;
import org.joda.time.LocalDate;

public class BasicMonthOfYearDateTimeFieldTest {

    private static final BasicMonthOfYearDateTimeField TEST_FIELD = new BasicMonthOfYearDateTimeField(EasternStandardTz.INSTANCE, DateTimeConstants.JANUARY);

    @Test
    public void testGet() {
        long instant = DateTimeUtils.currentMillis();
        int month = TEST_FIELD.get(instant);
        assertTrue(month >= 1 && month <= 12);
    }

    @Test
    public void testAdd() {
        long instant = DateTimeUtils.currentMillis();
        int months = 2;
        long newInstant = TEST_FIELD.add(instant, months);
        int newMonth = TEST_FIELD.get(newInstant);
        assertEquals(months + TEST_FIELD.get(instant), newMonth);
    }

    @Test
    public void testAddWrapField() {
        long instant = DateTimeUtils.currentMillis();
        int months = 13;
        long newInstant = TEST_FIELD.addWrapField(instant, months);
        int newMonth = TEST_FIELD.get(newInstant);
        assertEquals(months % 12 + TEST_FIELD.get(instant), newMonth);
    }

    @Test
    public void testGetDifferenceAsLong() {
        long minuendInstant = DateTimeUtils.currentMillis();
        long subtrahendInstant = minuendInstant + 1L * DateTimeConstants.MILLIS_PER_MONTH;
        long difference = TEST_FIELD.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(1, difference);
    }

    @Test
    public void testSet() {
        long instant = DateTimeUtils.currentMillis();
        int month = 5;
        long newInstant = TEST_FIELD.set(instant, month);
        int newMonth = TEST_FIELD.get(newInstant);
        assertEquals(month, newMonth);
    }

    @Test
    public void testGetRangeDurationField() {
        DurationField range = TEST_FIELD.getRangeDurationField();
        assertEquals(DateTimeFieldType.years(), range.getType());
    }

    @Test
    public void testIsLeap() {
        long instant = DateTimeUtils.currentMillis();
        boolean isLeap = TEST_FIELD.isLeap(instant);
        assertFalse(isLeap);
    }

    @Test
    public void testGetLeapAmount() {
        long instant = DateTimeUtils.currentMillis();
        int leapAmount = TEST_FIELD.getLeapAmount(instant);
        assertEquals(0, leapAmount);
    }

    @Test
    public void testGetLeapDurationField() {
        DurationField leap = TEST_FIELD.getLeapDurationField();
        assertEquals(DateTimeFieldType.days(), leap.getType());
    }

    @Test
    public void testGetMinimumValue() {
        int minValue = TEST_FIELD.getMinimumValue();
        assertEquals(DateTimeConstants.JANUARY, minValue);
    }

    @Test
    public void testGetMaximumValue() {
        int maxValue = TEST_FIELD.getMaximumValue();
        assertEquals(DateTimeConstants.DECEMBER, maxValue);
    }

    @Test
    public void testRoundFloor() {
        long instant = DateTimeUtils.currentMillis();
        long rounded = TEST_FIELD.roundFloor(instant);
        long expected = TEST_FIELD.getYearMonthMillis(iChronology.getYear(instant), TEST_FIELD.getMonthOfYear(instant));
        assertEquals(expected, rounded);
    }

    @Test
    public void testRemainder() {
        long instant = DateTimeUtils.currentMillis();
        long remainder = TEST_FIELD.remainder(instant);
        long expected = instant % (DateTimeConstants.MILLIS_PER_MONTH);
        assertEquals(expected, remainder);
    }

    @Test
    public void testAddWithPartial() {
        ReadablePartial partial = new LocalDate().toLocalDateTime().toLocalDate().toLocalDateTime().toDateTimeField();
        int[] values = new int[]{1, 15};
        int monthToAdd = 3;
        int[] newValues = TEST_FIELD.add(partial, TEST_FIELD.getFieldIndex(), values, monthToAdd);
        assertEquals(values[0] + monthToAdd, newValues[0]);
        assertEquals(values[1], newValues[1]);
    }
}
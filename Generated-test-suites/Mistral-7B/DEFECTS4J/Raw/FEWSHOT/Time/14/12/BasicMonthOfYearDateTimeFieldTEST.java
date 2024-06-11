package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.FieldUtils;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadableDateTime;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;

/**
 * Test cases for the {@code BasicMonthOfYearDateTimeField} class.
 */
public class BasicMonthOfYearDateTimeFieldTest {

    //-----------------------------------------------------------------------
    @Test
    public void testBasic() {
        BasicChronology chronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        // basic methods
        assertEquals(DateTimeFieldType.monthOfYear(), field.getType());
        assertEquals(chronology, field.getChronology());
        assertEquals(0, field.get(0L));
        assertEquals(12, field.getMaxValue());
        assertFalse(field.isLenient());

        // get and set methods
        MutableDateTime dateTime = new MutableDateTime();
        dateTime.setYear(2022);
        dateTime.setMonthOfYear(1);
        dateTime.setDayOfMonth(31);
        long instant = dateTime.getMillis();
        assertEquals(1, field.get(instant));
        field.set(instant, 13);
        assertEquals(13, field.get(instant));

        // add methods
        long result = field.add(instant, 3);
        dateTime.setMillis(result);
        assertEquals(4, dateTime.getMonthOfYear());

        result = field.add(instant, -3);
        dateTime.setMillis(result);
        assertEquals(1, dateTime.getMonthOfYear());

        // addWrapField method
        result = field.addWrapField(instant, 13);
        dateTime.setMillis(result);
        assertEquals(1, dateTime.getMonthOfYear());

        // getDifferenceAsLong method
        long minuendInstant = result;
        long subtrahendInstant = instant;
        long difference = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(3, difference);

        // set method
        int month = 7;
        result = field.set(instant, month);
        dateTime.setMillis(result);
        assertEquals(7, dateTime.getMonthOfYear());

        // getRangeDurationField method
        DurationField rangeDurationField = field.getRangeDurationField();
        assertEquals(DateTimeFieldType.years(), rangeDurationField.getType());

        // isLeap method and getLeapAmount method
        result = instant + DateTimeConstants.JANUARY_31_2000;
        assertTrue(field.isLeap(result));
        assertTrue(field.isLeap(result + DateTimeConstants.MILLIS_PER_DAY));
        assertTrue(field.isLeap(result + 2 * DateTimeConstants.MILLIS_PER_DAY));
        assertTrue(field.isLeap(result + 3 * DateTimeConstants.MILLIS_PER_DAY));
        assertTrue(field.isLeap(result + 4 * DateTimeConstants.MILLIS_PER_DAY));
        assertTrue(field.isLeap(result + 5 * DateTimeConstants.MILLIS_PER_DAY));
        assertTrue(field.isLeap(result + 6 * DateTimeConstants.MILLIS_PER_DAY));
        assertTrue(field.isLeap(result + 7 * DateTimeConstants.MILLIS_PER_DAY));
        assertTrue(field.isLeap(result + 8 * DateTimeConstants.MILLIS_PER_DAY));
        assertTrue(field.isLeap(result + 9 * DateTimeConstants.MILLIS_PER_DAY));
        assertTrue(field.isLeap(result + 10 * DateTimeConstants.MILLIS_PER_DAY));
        assertTrue(field.isLeap(result + 11 * DateTimeConstants.MILLIS_PER_DAY));
        assertTrue(field.isLeap(result + 12 * DateTimeConstants.MILLIS_PER_DAY));
        assertEquals(0, field.getLeapAmount(instant));
        assertEquals(0, field.getLeapAmount(instant + DateTimeConstants.JANUARY_31_2000));
        assertEquals(1, field.getLeapAmount(result));
        assertEquals(1, field.getLeapAmount(result + DateTimeConstants.MILLIS_PER_DAY));
        assertEquals(1, field.getLeapAmount(result + 2 * DateTimeConstants.MILLIS_PER_DAY));
        assertEquals(1, field.getLeapAmount(result + 3 * DateTimeConstants.MILLIS_PER_DAY));
        assertEquals(1, field.getLeapAmount(result + 4 * DateTimeConstants.MILLIS_PER_DAY));
        assertEquals(1, field.getLeapAmount(result + 5 * DateTimeConstants.MILLIS_PER_DAY));
        assertEquals(1, field.getLeapAmount(result + 6 * DateTimeConstants.MILLIS_PER_DAY));
        assertEquals(1, field.getLeapAmount(result + 7 * DateTimeConstants.MILLIS_PER_DAY));
        assertEquals(1, field.getLeapAmount(result + 8 * DateTimeConstants.MILLIS_PER_DAY));
        assertEquals(1, field.getLeapAmount(result + 9 * DateTimeConstants.MILLIS_PER_DAY));
        assertEquals(1, field.getLeapAmount(result + 10 * DateTimeConstants.MILLIS_PER_DAY));
        assertEquals(1, field.getLeapAmount(result + 11 * DateTimeConstants.MILLIS_PER_DAY));
        assertEquals(1, field.getLeapAmount(result + 12 * DateTimeConstants.MILLIS_PER_DAY));

        // getLeapDurationField method
        DurationField leapDurationField = field.getLeapDurationField();
        assertEquals(DateTimeFieldType.days(), leapDurationField.getType());

        // getMinimumValue and getMaximumValue methods
        assertEquals(1, field.getMinimumValue());
        assertEquals(12, field.getMaximumValue());

        // roundFloor and remainder methods
        long roundFloor = field.roundFloor(instant);
        assertEquals(instant, field.roundFloor(roundFloor));
        assertEquals(instant, field.remainder(instant));
        assertEquals(instant, field.remainder(roundFloor));
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAdd() {
        BasicChronology chronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        long instant = 0L;

        // add(long, int)
        for (int i = 0; i < 13; i++) {
            long result = field.add(instant, i);
            int month = iChronology.getMonthOfYear(result);
            assertEquals(i + 1, month);
        }

        // add(long, long)
        for (long i = 1; i <= 12; i++) {
            long result = field.add(instant, i);
            int month = iChronology.getMonthOfYear(result);
            assertEquals(i + 1, month);
        }

        // addWrapField(long, int)
        long result = field.addWrapField(instant, 13);
        int month = iChronology.getMonthOfYear(result);
        assertEquals(1, month);

        // add(ReadablePartial, int, int[], int)
        ReadablePartial partial = iChronology.newLocalDate(2022, 1, 31);
        int[] values = {1, 2, 3, 4};
        int valueToAdd = 5;
        long[] results = new long[values.length];
        for (int i = 0; i < values.length; i++) {
            results[i] = field.add(partial, i, values, valueToAdd).getMillis();
        }
        for (int i = 0; i < values.length; i++) {
            int month = iChronology.getMonthOfYear(results[i]);
            assertEquals(values[i] + 5, month);
        }
    }
}
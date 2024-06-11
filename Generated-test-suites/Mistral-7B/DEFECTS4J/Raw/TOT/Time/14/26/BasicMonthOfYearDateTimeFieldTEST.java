package org.joda.time.chrono.tests;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.ReadableDateTime;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.BasicGregorianChronology;
import org.joda.time.chrono.BasicJulianChronology;
import org.joda.time.chrono.BasicLeapDayMonthChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        BasicChronology chronoGregorian = new BasicGregorianChronology();
        Chronology chronoJulian = new BasicJulianChronology();
        Chronology chronoISO = new ISOChronology();
        Chronology chronoLeapDayMonth = new BasicLeapDayMonthChronology();

        // Test with Gregorian
        ImpreciseDateTimeField fieldGregorian = chronoGregorian.monthOfYear();
        assertEquals(DateTimeFieldType.monthOfYear(), fieldGregorian.getFieldType());
        assertEquals(31, fieldGregorian.getMaximumValue());
        assertEquals(0, fieldGregorian.getMinimumValue());
        assertEquals(12, fieldGregorian.get(BaseDateTime.UTC.getMillis()));
        assertEquals(12, fieldGregorian.get(LocalDateTime.now().getMillis()));
        assertEquals(12, fieldGregorian.get(LocalDate.now().toLocalDateTime().getMillis()));
        assertEquals(12, fieldGregorian.get(LocalTime.now().toLocalDateTime().getMillis()));
        assertEquals(12, fieldGregorian.get(new LocalDateTime(2022, 12, 31).getMillis()));
        assertEquals(12, fieldGregorian.get(new LocalDate(2022, 12, 31).toLocalDateTime().getMillis()));
        assertEquals(12, fieldGregorian.get(new LocalTime(2022, 12, 31, 23, 59, 59).toLocalDateTime().getMillis()));

        // Test with Julian
        ImpreciseDateTimeField fieldJulian = chronoJulian.monthOfYear();
        assertEquals(365, fieldJulian.getMaximumValue());
        assertEquals(0, fieldJulian.getMinimumValue());
        assertEquals(1, fieldJulian.get(BaseDateTime.UTC.getMillis()));
        assertEquals(1, fieldJulian.get(LocalDateTime.now().getMillis()));
        assertEquals(1, fieldJulian.get(LocalDate.now().toLocalDateTime().getMillis()));
        assertEquals(1, fieldJulian.get(LocalTime.now().toLocalDateTime().getMillis()));
        assertEquals(1, fieldJulian.get(new LocalDateTime(2022, 12, 31).getMillis()));
        assertEquals(1, fieldJulian.get(new LocalDate(2022, 12, 31).toLocalDateTime().getMillis()));
        assertEquals(1, fieldJulian.get(new LocalTime(2022, 12, 31, 23, 59, 59).toLocalDateTime().getMillis()));

        // Test with ISO
        ImpreciseDateTimeField fieldISO = chronoISO.monthOfYear();
        assertEquals(12, fieldISO.getMaximumValue());
        assertEquals(1, fieldISO.getMinimumValue());
        assertEquals(1, fieldISO.get(BaseDateTime.UTC.getMillis()));
        assertEquals(1, fieldISO.get(LocalDateTime.now().getMillis()));
        assertEquals(1, fieldISO.get(LocalDate.now().toLocalDateTime().getMillis()));
        assertEquals(1, fieldISO.get(LocalTime.now().toLocalDateTime().getMillis()));
        assertEquals(1, fieldISO.get(new LocalDateTime(2022, 12, 31).getMillis()));
        assertEquals(1, fieldISO.get(new LocalDate(2022, 12, 31).toLocalDateTime().getMillis()));
        assertEquals(1, fieldISO.get(new LocalTime(2022, 12, 31, 23, 59, 59).toLocalDateTime().getMillis()));

        // Test with LeapDayMonth
        ImpreciseDateTimeField fieldLeapDayMonth = chronoLeapDayMonth.monthOfYear();
        assertEquals(31, fieldLeapDayMonth.getMaximumValue());
        assertEquals(0, fieldLeapDayMonth.getMinimumValue());
        assertEquals(1, fieldLeapDayMonth.get(BaseDateTime.UTC.getMillis()));
        assertEquals(1, fieldLeapDayMonth.get(LocalDateTime.now().getMillis()));
        assertEquals(1, fieldLeapDayMonth.get(LocalDate.now().toLocalDateTime().getMillis()));
        assertEquals(1, fieldLeapDayMonth.get(LocalTime.now().toLocalDateTime().getMillis()));
        assertEquals(1, fieldLeapDayMonth.get(new LocalDateTime(2022, 12, 31).getMillis()));
        assertEquals(1, fieldLeapDayMonth.get(new LocalDate(2022, 12, 31).toLocalDateTime().getMillis()));
        assertEquals(1, fieldLeapDayMonth.get(new LocalTime(2022, 12, 31, 23, 59, 59).toLocalDateTime().getMillis()));

        // Test get() with invalid values
        Chronology chronoCustom = new BasicChronology() {
            @Override
            public int getMaxMonth() {
                return -1;
            }
        };
        ImpreciseDateTimeField fieldCustom = chronoCustom.monthOfYear();
        try {
            fieldCustom.get(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test get() with valid but out of bounds values
        ImpreciseDateTimeField fieldOutOfBounds = chronoGregorian.monthOfYear();
        try {
            fieldOutOfBounds.get(Long.MIN_VALUE);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            fieldOutOfBounds.get(Long.MAX_VALUE);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test set() with valid values
        LocalDateTime date = new LocalDateTime(2022, 10, 25, 12, 0);
        long setMillis = fieldGregorian.set(date.getMillis(), 5);
        LocalDateTime setDate = new LocalDateTime(setMillis);
        assertEquals(5, fieldGregorian.get(setMillis));
        assertEquals(10, setDate.getMonthOfYear());

        // Test set() with invalid values
        LocalDateTime invalidDate = new LocalDateTime(2022, 14, 25, 12, 0);
        long invalidMillis = fieldGregorian.set(invalidDate.getMillis(), 5);
        fail("Expected IllegalArgumentException");

        // Test add() with valid amounts
        LocalDateTime addDate = new LocalDateTime(2022, 1, 1, 12, 0);
        long addMillis = fieldGregorian.add(addDate.getMillis(), 10);
        LocalDateTime addDateResult = new LocalDateTime(addMillis);
        assertEquals(11, fieldGregorian.get(addMillis));
        assertEquals(1, addDateResult.getMonthOfYear());

        // Test add() with invalid amounts
        LocalDateTime invalidAddDate = new LocalDateTime(2022, 1, 1, 12, 0);
        long invalidAddMillis = fieldGregorian.add(invalidAddDate.getMillis(), Integer.MIN_VALUE);
        fail("Expected IllegalArgumentException");

        // Test add() with wraps around
        LocalDateTime wrapDate = new LocalDateTime(2022, 12, 31, 12, 0);
        long wrapMillis = fieldGregorian.add(wrapDate.getMillis(), 1);
        LocalDateTime wrapDateResult = new LocalDateTime(wrapMillis);
        assertEquals(1, fieldGregorian.get(wrapMillis));
        assertEquals(1, wrapDateResult.getMonthOfYear());

        // Test addWrapField()
        long addWrapMillis = fieldGregorian.addWrapField(wrapDate.getMillis(), 1);
        LocalDateTime addWrapDateResult = new LocalDateTime(addWrapMillis);
        assertEquals(12, fieldGregorian.get(addWrapMillis));
        assertEquals(1, addWrapDateResult.getMonthOfYear());

        // Test getDifferenceAsLong()
        long difference = fieldGregorian.getDifferenceAsLong(BaseDateTime.UTC.getMillis(), LocalDateTime.now().getMillis());
        int months = (int) Math.abs(difference / (long) fieldGregorian.getMillisPerField());
        assertEquals(11, months);

        // Test roundFloor()
        long roundFloorMillis = fieldGregorian.roundFloor(wrapDate.getMillis());
        LocalDateTime roundFloorDate = new LocalDateTime(roundFloorMillis);
        assertEquals(1, fieldGregorian.get(roundFloorMillis));
        assertEquals(1, roundFloorDate.getMonthOfYear());

        // Test remainder()
        long remainderMillis = fieldGregorian.remainder(wrapDate.getMillis());
        assertEquals(0, remainderMillis);

        // Test toString()
        String string = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ").print(new LocalDateTime(2022, 1, 1, 12, 0, 0, 0).toDate());
        String expected = "2022-01-01T12:00:00.000Z";
        assertEquals(expected, string);
    }

    @Test
    public void testBasicMonthOfYearDateTimeFieldSetLeapYear() {
        BasicChronology chronoGregorian = new BasicGregorianChronology();
        ImpreciseDateTimeField fieldGregorian = chronoGregorian.monthOfYear();

        // Test set() with valid values in a leap year
        LocalDateTime date = new LocalDateTime(2022, 2, 29, 12, 0);
        long setMillis = fieldGregorian.set(date.getMillis(), 5);
        LocalDateTime setDate = new LocalDateTime(setMillis);
        assertEquals(5, fieldGregorian.get(setMillis));
        assertEquals(2, setDate.getMonthOfYear());

        // Test set() with invalid values in a leap year
        LocalDateTime invalidDate = new LocalDateTime(2022, 15, 29, 12, 0);
        long invalidMillis = fieldGregorian.set(invalidDate.getMillis(), 5);
        fail("Expected IllegalArgumentException");

        // Test add() with valid amounts in a leap year
        LocalDateTime addDate = new LocalDateTime(2022, 1, 1, 12, 0);
        long addMillis = fieldGregorian.add(addDate.getMillis(), 10);
        LocalDateTime addDateResult = new LocalDateTime(addMillis);
        assertEquals(11, fieldGregorian.get(addMillis));
        assertEquals(2, addDateResult.getMonthOfYear());

        // Test add() with invalid amounts in a leap year
        LocalDateTime invalidAddDate = new LocalDateTime(2022, 1, 1, 12, 0);
        long invalidAddMillis = fieldGregorian.add(invalidAddDate.getMillis(), Integer.MIN_VALUE);
        fail("Expected IllegalArgumentException");

        // Test addWrapField() in a leap year
        long addWrapMillis = fieldGregorian.addWrapField(wrapDate.getMillis(), 1);
        LocalDateTime addWrapDateResult = new LocalDateTime(addWrapMillis);
        assertEquals(3, fieldGregorian.get(addWrapMillis));
        assertEquals(2, addWrapDateResult.getMonthOfYear());

        // Test getDifferenceAsLong() in a leap year
        long difference = fieldGregorian.getDifferenceAsLong(BaseDateTime.UTC.getMillis(), LocalDateTime.now().getMillis());
        int months = (int) Math.abs(difference / (long) fieldGregorian.getMillisPerField());
        assertEquals(11, months);

        // Test roundFloor() in a leap year
        long roundFloorMillis = fieldGregorian.roundFloor(wrapDate.getMillis());
        LocalDateTime roundFloorDate = new LocalDateTime(roundFloorMillis);
        assertEquals(3, fieldGregorian.get(roundFloorMillis));
        assertEquals(2, roundFloorDate.getMonthOfYear());

        // Test remainder() in a leap year
        long remainderMillis = fieldGregorian.remainder(wrapDate.getMillis());
        assertEquals(0, remainderMillis);
    }
}
package org.joda.time.field;

import static org.junit.Assert.*;

import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDate;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.MutableLocalTime;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;

import java.util.TimeZone;

/**
 * Test cases for LenientDateTimeField class.
 */
public class LenientDateTimeFieldTest {

    private static final DateTimeField DAY_OF_MONTH = StrictDateTimeField.days();
    private static final DateTimeField HOUR_OF_DAY = StrictDateTimeField.hourOfDay();
    private static final DateTimeField MINUTE_OF_HOUR = StrictDateTimeField.minuteOfHour();
    private static final DateTimeField SECOND_OF_MINUTE = StrictDateTimeField.secondOfMinute();

    @org.junit.BeforeClass
    public static void init() {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        GregorianChronology gc = new GregorianChronology(utc);
        LenientDateTimeField lenientDay = LenientDateTimeField.lenientInstance(DAY_OF_MONTH, gc);
        LenientDateTimeField lenientHour = LenientDateTimeField.lenientInstance(HOUR_OF_DAY, gc);
        LenientDateTimeField lenientMinute = LenientDateTimeField.lenientInstance(MINUTE_OF_HOUR, gc);
        LenientDateTimeField lenientSecond = LenientDateTimeField.lenientInstance(SECOND_OF_MINUTE, gc);

        testLenientMethods(lenientDay, gc);
        testLenientMethods(lenientHour, gc);
        testLenientMethods(lenientMinute, gc);
        testLenientMethods(lenientSecond, gc);
    }

    private static void testLenientMethods(LenientDateTimeField lenientField, Chronology chronology) {
        long instant = DateTimeUtils.currentMillis();
        MutableDateTime mutableDateTime = new MutableDateTime(instant, chronology);

        // Test base methods
        testGetter(lenientField, mutableDateTime);
        testSetter(lenientField, mutableDateTime);
        testIsLenient(lenientField);

        // Test edge cases
        testSetOutOfBoundsValue(lenientField, mutableDateTime, -1);
        testSetOutOfBoundsValue(lenientField, mutableDateTime, Integer.MAX_VALUE);

        // Test with different chronologies
        Chronology gmt = new GregorianChronology(TimeZone.getTimeZone("GMT"));
        LenientDateTimeField lenientFieldGmt = LenientDateTimeField.lenientInstance(lenientField, gmt);
        testLenientMethods(lenientFieldGmt, gmt);
    }

    private static void testGetter(LenientDateTimeField lenientField, MutableDateTime mutableDateTime) {
        int expected = mutableDateTime.get(lenientField);
        int actual = lenientField.get(mutableDateTime.toLong());
        assertEquals("Expected: " + expected + ", Actual: " + actual, expected, actual);
    }

    private static void testSetter(LenientDateTimeField lenientField, MutableDateTime mutableDateTime) {
        int newValue = mutableDateTime.get(lenientField) + 1;
        long newInstant = lenientField.set(mutableDateTime.toLong(), newValue);
        mutableDateTime.set(newInstant);
        int actual = mutableDateTime.get(lenientField);
        assertEquals("Expected: " + newValue + ", Actual: " + actual, newValue, actual);
    }

    private static void testIsLenient(LenientDateTimeField lenientField) {
        assertTrue("Expected: true, Actual: " + lenientField.isLenient(), lenientField.isLenient());
    }

    private static void testSetOutOfBoundsValue(LenientDateTimeField lenientField, MutableDateTime mutableDateTime, int newValue) {
        long newInstant = lenientField.set(mutableDateTime.toLong(), newValue);
        mutableDateTime.set(newInstant);
        int actual = mutableDateTime.get(lenientField);
        int expected = lenientField.get(mutableDateTime.toLong()) + (newValue - mutableDateTime.get(lenientField));
        assertEquals("Expected: " + expected + ", Actual: " + actual, expected, actual);
    }
}
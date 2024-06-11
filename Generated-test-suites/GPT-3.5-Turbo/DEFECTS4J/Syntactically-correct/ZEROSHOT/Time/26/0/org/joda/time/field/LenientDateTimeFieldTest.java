package org.joda.time.field;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

    private LenientDateTimeField lenientDateTimeField;
    private DateTimeField baseField;
    private Chronology baseChronology;

    @Before
    public void setUp() {
        baseField = new MockDateTimeField(DateTimeFieldType.dayOfMonth(), null);
        baseChronology = new MockChronology();
        lenientDateTimeField = new LenientDateTimeField(baseField, baseChronology);
    }

    @Test
    public void testIsLenient() {
        assertTrue(lenientDateTimeField.isLenient());
    }

    @Test
    public void testSet() {
        long instant = 1000L;
        int value = 1;
        long expected = 2000L;

        lenientDateTimeField.set(instant, value);

        assertEquals(expected, baseChronology.getConverted());
    }
}

class MockDateTimeField extends DateTimeField {

    public MockDateTimeField(DateTimeFieldType fieldType, Chronology chronology) {
        super(fieldType, chronology);
    }

    @Override
    public int get(long instant) {
        return 0;
    }

    @Override
    public String getAsText(long instant, Locale locale) {
        return null;
    }

    @Override
    public String getAsShortText(long instant, Locale locale) {
        return null;
    }

    @Override
    public long add(long instant, int value) {
        return 0;
    }

    @Override
    public long add(long instant, long value) {
        return 0;
    }

    @Override
    public int[] add(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
        return new int[0];
    }

    @Override
    public int[] addWrapPartial(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
        return new int[0];
    }

    @Override
    public long addWrapField(long instant, int value) {
        return 0;
    }

    @Override
    public int[] addWrapField(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
        return new int[0];
    }

    @Override
    public int getDifference(long minuendInstant, long subtrahendInstant) {
        return 0;
    }

    @Override
    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        return 0;
    }

    @Override
    public long set(long instant, int value) {
        return 1000L;
    }

    @Override
    public boolean isLeap(long instant) {
        return false;
    }

    @Override
    public int getLeapAmount(long instant) {
        return 0;
    }

    @Override
    public DurationField getLeapDurationField() {
        return null;
    }

    @Override
    public int getMinimumValue() {
        return 0;
    }

    @Override
    public int getMaximumValue() {
        return 0;
    }

    @Override
    public long roundFloor(long instant) {
        return 0;
    }

    @Override
    public long roundCeiling(long instant) {
        return 0;
    }

    @Override
    public long roundHalfFloor(long instant) {
        return 0;
    }

    @Override
    public long roundHalfCeiling(long instant) {
        return 0;
    }

    @Override
    public long roundHalfEven(long instant) {
        return 0;
    }

    @Override
    public long remainder(long instant) {
        return 0;
    }

    @Override
    public DateTimeField getWrappedField() {
        return null;
    }
}

class MockChronology extends Chronology {

    private long converted;

    public long getConverted() {
        return converted;
    }

    @Override
    public DateTimeZone getZone() {
        return null;
    }

    @Override
    public Chronology withUTC() {
        return null;
    }

    @Override
    public Chronology withZone(DateTimeZone zone) {
        return null;
    }

    @Override
    public boolean isLeapYear(int year) {
        return false;
    }

    @Override
    public int[] getPartialValues(ReadablePartial instant, long millis) {
        return new int[0];
    }

    @Override
    public int[] get(ReadablePartial partial, long millis) {
        return new int[0];
    }

    @Override
    public long set(ReadablePartial instant, long millis) {
        return 0;
    }

    @Override
    public int[] add(ReadablePartial instant, int[] values, int fieldIndex, int valueToAdd) {
        return new int[0];
    }

    @Override
    public long add(ReadablePartial instant, long instantToAdd, int fieldIndex) {
        return 0;
    }

    @Override
    public int[] addWrapPartial(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
        return new int[0];
    }

    @Override
    public long addWrapField(long instant, int value) {
        return 0;
    }

    @Override
    public int[] addWrapField(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
        return new int[0];
    }

    @Override
    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        return 0;
    }

    @Override
    public long getMillis(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, int secondOfMinute, int millisOfSecond) {
        return 0;
    }

    @Override
    public long getMillis(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, int secondOfMinute, int millisOfSecond, boolean strict) {
        return 0;
    }

    @Override
    public long getMillis(ReadablePartial partial) {
        return 0;
    }

    @Override
    public long getMillis(ReadablePartial partial, long[] instantChronology) {
        return 0;
    }

    @Override
    public DurationField millis() {
        return null;
    }

    @Override
    public DateTimeField millisOfSecond() {
        return null;
    }

    @Override
    public DateTimeField millisOfDay() {
        return null;
    }

    @Override
    public DurationField seconds() {
        return null;
    }

    @Override
    public DateTimeField secondOfMinute() {
        return null;
    }

    @Override
    public DateTimeField secondOfDay() {
        return null;
    }

    @Override
    public DurationField minutes() {
        return null;
    }

    @Override
    public DateTimeField minuteOfHour() {
        return null;
    }

    @Override
    public DateTimeField minuteOfDay() {
        return null;
    }

    @Override
    public DurationField hours() {
        return null;
    }

    @Override
    public DateTimeField hourOfDay() {
        return null;
    }

    @Override
    public DateTimeField clockhourOfDay() {
        return null;
    }

    @Override
    public DurationField halfdays() {
        return null;
    }

    @Override
    public DateTimeField hourOfHalfday() {
        return null;
    }

    @Override
    public DateTimeField clockhourOfHalfday() {
        return null;
    }

    @Override
    public DateTimeField halfdayOfDay() {
        return null;
    }

    @Override
    public DurationField days() {
        return null;
    }

    @Override
    public DateTimeField dayOfWeek() {
        return null;
    }

    @Override
    public DateTimeField dayOfMonth() {
        return null;
    }

    @Override
    public DateTimeField dayOfYear() {
        return null;
    }

    @Override
    public DurationField weeks() {
        return null;
    }

    @Override
    public DateTimeField weekOfWeekyear() {
        return null;
    }

    @Override
    public DurationField weekyears() {
        return null;
    }

    @Override
    public DateTimeField weekyear() {
        return null;
    }

    @Override
    public DateTimeField weekyearOfCentury() {
        return null;
    }

    @Override
    public DurationField months() {
        return null;
    }

    @Override
    public DateTimeField monthOfYear() {
        return null;
    }

    @Override
    public DurationField years() {
        return null;
    }

    @Override
    public DateTimeField year() {
        return null;
    }

    @Override
    public DateTimeField yearOfEra() {
        return null;
    }

    @Override
    public DateTimeField yearOfCentury() {
        return null;
    }

    @Override
    public DurationField centuries() {
        return null;
    }

    @Override
    public DateTimeField centuryOfEra() {
        return null;
    }

    @Override
    public DurationField eras() {
        return null;
    }

    @Override
    public DateTimeField era() {
        return null;
    }
}
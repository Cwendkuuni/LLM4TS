package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.joda.time.DateTimeField;
import org.joda.time.Chronology;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {
    private static final long TEST_INSTANT = 1577836800000L; // 2020-01-01T00:00:00Z
    private LenientDateTimeField lenientField;

    @Before
    public void setUp() {
        DateTimeField strictField = new MockDateTimeField();
        Chronology base = new MockChronology();
        lenientField = LenientDateTimeField.getInstance(strictField, base);
    }

    @Test
    public void testIsLenient() {
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSet_LenientValue() {
        long result = lenientField.set(TEST_INSTANT, 10);
        assertEquals(10, lenientField.get(result));
    }

    @Test
    public void testSet_OutOfBoundsValue() {
        long result = lenientField.set(TEST_INSTANT, 100);
        // Since the field is lenient, it should perform an addition instead of throwing an exception
        assertEquals(100, lenientField.get(result));
    }

    @Test
    public void testSet_LowestValue() {
        long result = lenientField.set(TEST_INSTANT, Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, lenientField.get(result));
    }

    @Test
    public void testSet_HighestValue() {
        long result = lenientField.set(TEST_INSTANT, Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, lenientField.get(result));
    }

    @Test
    public void testSet_NullField() {
        DateTimeField nullField = null;
        assertNull(LenientDateTimeField.getInstance(nullField, new MockChronology()));
    }

    @Test
    public void testSet_StrictField() {
        DateTimeField strictField = new StrictDateTimeField();
        assertFalse(lenientField.equals(strictField));
        lenientField = LenientDateTimeField.getInstance(strictField, new MockChronology());
        assertTrue(lenientField.equals(strictField));
    }
}

class MockDateTimeField extends DateTimeField {
    MockDateTimeField() {
        super(null);
    }

    @Override
    public int get(long instant) {
        return 0;
    }

    @Override
    public String getAsText(long instant) {
        return null;
    }

    @Override
    public String getAsText(long instant, java.util.Locale locale) {
        return null;
    }

    @Override
    public String getAsShortText(int fieldValue, java.util.Locale locale) {
        return null;
    }

    @Override
    public String getAsShortText(long instant) {
        return null;
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
    public long add(long instant, int value) {
        return 0;
    }

    @Override
    public long add(long instant, long value) {
        return 0;
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
    public long set(long instant, int value) {
        return 0;
    }

    @Override
    public long set(long instant, String text, java.util.Locale locale) {
        return 0;
    }

    @Override
    public int[] set(ReadablePartial instant, int fieldIndex, int[] values, int newValue) {
        return new int[0];
    }

    @Override
    public boolean isSupported() {
        return false;
    }

    @Override
    public String toString() {
        return null;
    }
}

class MockChronology extends Chronology {
    MockChronology() {
        super(null, null, null);
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
    public DateTimeField minuteOfHour() {
        return null;
    }

    @Override
    public DateTimeField minuteOfDay() {
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
    public DateTimeField weekOfWeekyear() {
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
    public DateTimeField monthOfYear() {
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
    public DateTimeField centuryOfEra() {
        return null;
    }

    @Override
    public DateTimeField era() {
        return null;
    }

    @Override
    public String toString() {
        return null;
    }
}

class StrictDateTimeField extends MockDateTimeField {
    StrictDateTimeField() {
        super();
    }
}
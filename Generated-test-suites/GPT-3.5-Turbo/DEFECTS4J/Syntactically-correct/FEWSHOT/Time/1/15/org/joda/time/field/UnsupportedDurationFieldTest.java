package org.joda.time.field;

import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.years(), UnsupportedDurationField.getInstance(DurationFieldType.years()).getType());
        assertNotEquals(DurationFieldType.months(), UnsupportedDurationField.getInstance(DurationFieldType.days()).getType());
    }

    @Test
    public void testGetName() {
        assertEquals("years", UnsupportedDurationField.getInstance(DurationFieldType.years()).getName());
        assertNotEquals("months", UnsupportedDurationField.getInstance(DurationFieldType.days()).getName());
    }

    @Test
    public void testIsSupported() {
        assertFalse(UnsupportedDurationField.getInstance(DurationFieldType.years()).isSupported());
        assertFalse(UnsupportedDurationField.getInstance(DurationFieldType.days()).isSupported());
    }

    @Test
    public void testIsPrecise() {
        assertTrue(UnsupportedDurationField.getInstance(DurationFieldType.years()).isPrecise());
        assertTrue(UnsupportedDurationField.getInstance(DurationFieldType.days()).isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField.getInstance(DurationFieldType.years()).getValue(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField.getInstance(DurationFieldType.years()).getValueAsLong(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField.getInstance(DurationFieldType.years()).getValue(100, 200);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField.getInstance(DurationFieldType.years()).getValueAsLong(100, 200);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        UnsupportedDurationField.getInstance(DurationFieldType.years()).getMillis(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLong() {
        UnsupportedDurationField.getInstance(DurationFieldType.years()).getMillis(100L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        UnsupportedDurationField.getInstance(DurationFieldType.years()).getMillis(100, 200);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLongAndInstant() {
        UnsupportedDurationField.getInstance(DurationFieldType.years()).getMillis(100L, 200L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValue() {
        UnsupportedDurationField.getInstance(DurationFieldType.years()).add(100, 200);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValueAsLong() {
        UnsupportedDurationField.getInstance(DurationFieldType.years()).add(100, 200L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField.getInstance(DurationFieldType.years()).getDifference(100, 200);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField.getInstance(DurationFieldType.years()).getDifferenceAsLong(100, 200);
    }

    @Test
    public void testGetUnitMillis() {
        assertEquals(0, UnsupportedDurationField.getInstance(DurationFieldType.years()).getUnitMillis());
        assertNotEquals(1000, UnsupportedDurationField.getInstance(DurationFieldType.days()).getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        assertTrue(UnsupportedDurationField.getInstance(DurationFieldType.years()).compareTo(UnsupportedDurationField.getInstance(DurationFieldType.seconds())) > 0);
        assertFalse(UnsupportedDurationField.getInstance(DurationFieldType.minutes()).compareTo(UnsupportedDurationField.getInstance(DurationFieldType.hours())) > 0);
    }

    @Test
    public void testEquals() {
        assertEquals(UnsupportedDurationField.getInstance(DurationFieldType.years()), UnsupportedDurationField.getInstance(DurationFieldType.years()));
        assertNotEquals(UnsupportedDurationField.getInstance(DurationFieldType.days()), UnsupportedDurationField.getInstance(DurationFieldType.hours()));
    }

    @Test
    public void testHashCode() {
        assertEquals(UnsupportedDurationField.getInstance(DurationFieldType.years()).hashCode(), UnsupportedDurationField.getInstance(DurationFieldType.years()).hashCode());
        assertNotEquals(UnsupportedDurationField.getInstance(DurationFieldType.days()).hashCode(), UnsupportedDurationField.getInstance(DurationFieldType.hours()).hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[years]", UnsupportedDurationField.getInstance(DurationFieldType.years()).toString());
        assertNotEquals("UnsupportedDurationField[days]", UnsupportedDurationField.getInstance(DurationFieldType.hours()).toString());
    }
}
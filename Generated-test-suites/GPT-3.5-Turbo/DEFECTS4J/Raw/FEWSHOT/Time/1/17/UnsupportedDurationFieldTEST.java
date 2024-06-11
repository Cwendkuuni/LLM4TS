package org.joda.time.field;

import org.joda.time.DurationFieldType;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {
    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.millis(), UnsupportedDurationField.getInstance(DurationFieldType.millis()).getType());
        assertEquals(DurationFieldType.seconds(), UnsupportedDurationField.getInstance(DurationFieldType.seconds()).getType());
        assertEquals(DurationFieldType.minutes(), UnsupportedDurationField.getInstance(DurationFieldType.minutes()).getType());
        assertEquals(DurationFieldType.hours(), UnsupportedDurationField.getInstance(DurationFieldType.hours()).getType());
        assertEquals(DurationFieldType.days(), UnsupportedDurationField.getInstance(DurationFieldType.days()).getType());
        assertEquals(DurationFieldType.weeks(), UnsupportedDurationField.getInstance(DurationFieldType.weeks()).getType());
        assertEquals(DurationFieldType.months(), UnsupportedDurationField.getInstance(DurationFieldType.months()).getType());
        assertEquals(DurationFieldType.years(), UnsupportedDurationField.getInstance(DurationFieldType.years()).getType());
    }

    @Test
    public void testGetName() {
        assertEquals("millis", UnsupportedDurationField.getInstance(DurationFieldType.millis()).getName());
        assertEquals("seconds", UnsupportedDurationField.getInstance(DurationFieldType.seconds()).getName());
        assertEquals("minutes", UnsupportedDurationField.getInstance(DurationFieldType.minutes()).getName());
        assertEquals("hours", UnsupportedDurationField.getInstance(DurationFieldType.hours()).getName());
        assertEquals("days", UnsupportedDurationField.getInstance(DurationFieldType.days()).getName());
        assertEquals("weeks", UnsupportedDurationField.getInstance(DurationFieldType.weeks()).getName());
        assertEquals("months", UnsupportedDurationField.getInstance(DurationFieldType.months()).getName());
        assertEquals("years", UnsupportedDurationField.getInstance(DurationFieldType.years()).getName());
    }

    @Test
    public void testIsSupported() {
        assertFalse(UnsupportedDurationField.getInstance(DurationFieldType.millis()).isSupported());
        assertFalse(UnsupportedDurationField.getInstance(DurationFieldType.seconds()).isSupported());
        assertFalse(UnsupportedDurationField.getInstance(DurationFieldType.minutes()).isSupported());
        assertFalse(UnsupportedDurationField.getInstance(DurationFieldType.hours()).isSupported());
        assertFalse(UnsupportedDurationField.getInstance(DurationFieldType.days()).isSupported());
        assertFalse(UnsupportedDurationField.getInstance(DurationFieldType.weeks()).isSupported());
        assertFalse(UnsupportedDurationField.getInstance(DurationFieldType.months()).isSupported());
        assertFalse(UnsupportedDurationField.getInstance(DurationFieldType.years()).isSupported());
    }

    @Test
    public void testIsPrecise() {
        assertTrue(UnsupportedDurationField.getInstance(DurationFieldType.millis()).isPrecise());
        assertTrue(UnsupportedDurationField.getInstance(DurationFieldType.seconds()).isPrecise());
        assertTrue(UnsupportedDurationField.getInstance(DurationFieldType.minutes()).isPrecise());
        assertTrue(UnsupportedDurationField.getInstance(DurationFieldType.hours()).isPrecise());
        assertTrue(UnsupportedDurationField.getInstance(DurationFieldType.days()).isPrecise());
        assertTrue(UnsupportedDurationField.getInstance(DurationFieldType.weeks()).isPrecise());
        assertTrue(UnsupportedDurationField.getInstance(DurationFieldType.months()).isPrecise());
        assertTrue(UnsupportedDurationField.getInstance(DurationFieldType.years()).isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField.getInstance(DurationFieldType.millis()).getValue(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField.getInstance(DurationFieldType.millis()).getValueAsLong(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField.getInstance(DurationFieldType.millis()).getValue(1000, 10000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField.getInstance(DurationFieldType.millis()).getValueAsLong(10000L, 100000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField.getInstance(DurationFieldType.millis()).getMillis(10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        UnsupportedDurationField.getInstance(DurationFieldType.millis()).getMillis(100L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstant() {
        UnsupportedDurationField.getInstance(DurationFieldType.millis()).getMillis(100, 1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        UnsupportedDurationField.getInstance(DurationFieldType.millis()).getMillis(100L, 1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValue() {
        UnsupportedDurationField.getInstance(DurationFieldType.millis()).add(1000, 10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValueAsLong() {
        UnsupportedDurationField.getInstance(DurationFieldType.millis()).add(1000L, 10L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceWithInstants() {
        UnsupportedDurationField.getInstance(DurationFieldType.millis()).getDifference(1000, 10000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLongWithInstants() {
        UnsupportedDurationField.getInstance(DurationFieldType.millis()).getDifferenceAsLong(1000L, 10000L);
    }

    @Test
    public void testGetUnitMillis() {
        assertEquals(0L, UnsupportedDurationField.getInstance(DurationFieldType.millis()).getUnitMillis());
        assertEquals(0L, UnsupportedDurationField.getInstance(DurationFieldType.seconds()).getUnitMillis());
        assertEquals(0L, UnsupportedDurationField.getInstance(DurationFieldType.minutes()).getUnitMillis());
        assertEquals(0L, UnsupportedDurationField.getInstance(DurationFieldType.hours()).getUnitMillis());
        assertEquals(0L, UnsupportedDurationField.getInstance(DurationFieldType.days()).getUnitMillis());
        assertEquals(0L, UnsupportedDurationField.getInstance(DurationFieldType.weeks()).getUnitMillis());
        assertEquals(0L, UnsupportedDurationField.getInstance(DurationFieldType.months()).getUnitMillis());
        assertEquals(0L, UnsupportedDurationField.getInstance(DurationFieldType.years()).getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        assertEquals(1, UnsupportedDurationField.getInstance(DurationFieldType.millis()).compareTo(new UnsupportedDurationField(DurationFieldType.hours())));
        assertEquals(1, UnsupportedDurationField.getInstance(DurationFieldType.seconds()).compareTo(new UnsupportedDurationField(DurationFieldType.minutes())));
        assertEquals(1, UnsupportedDurationField.getInstance(DurationFieldType.minutes()).compareTo(new UnsupportedDurationField(DurationFieldType.hours())));
    }

    @Test
    public void testEquals() {
        assertEquals(UnsupportedDurationField.getInstance(DurationFieldType.millis()), UnsupportedDurationField.getInstance(DurationFieldType.millis()));
        assertEquals(UnsupportedDurationField.getInstance(DurationFieldType.seconds()), UnsupportedDurationField.getInstance(DurationFieldType.seconds()));
        assertEquals(UnsupportedDurationField.getInstance(DurationFieldType.minutes()), UnsupportedDurationField.getInstance(DurationFieldType.minutes()));
    }

    @Test
    public void testHashCode() {
        assertEquals(UnsupportedDurationField.getInstance(DurationFieldType.millis()).hashCode(), UnsupportedDurationField.getInstance(DurationFieldType.millis()).hashCode());
        assertEquals(UnsupportedDurationField.getInstance(DurationFieldType.seconds()).hashCode(), UnsupportedDurationField.getInstance(DurationFieldType.seconds()).hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[millis]", UnsupportedDurationField.getInstance(DurationFieldType.millis()).toString());
        assertEquals("UnsupportedDurationField[seconds]", UnsupportedDurationField.getInstance(DurationFieldType.seconds()).toString());
        assertEquals("UnsupportedDurationField[minutes]", UnsupportedDurationField.getInstance(DurationFieldType.minutes()).toString());
        assertEquals("UnsupportedDurationField[hours]", UnsupportedDurationField.getInstance(DurationFieldType.hours()).toString());
    }
}
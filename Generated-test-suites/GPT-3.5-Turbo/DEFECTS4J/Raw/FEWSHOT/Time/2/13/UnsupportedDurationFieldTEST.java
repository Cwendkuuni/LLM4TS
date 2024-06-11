package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {
    
    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.days(), UnsupportedDurationField.getInstance(DurationFieldType.days()).getType());
        assertEquals(DurationFieldType.hours(), UnsupportedDurationField.getInstance(DurationFieldType.hours()).getType());
        assertEquals(DurationFieldType.minutes(), UnsupportedDurationField.getInstance(DurationFieldType.minutes()).getType());
    }

    @Test
    public void testGetName() {
        assertEquals("days", UnsupportedDurationField.getInstance(DurationFieldType.days()).getName());
        assertEquals("hours", UnsupportedDurationField.getInstance(DurationFieldType.hours()).getName());
        assertEquals("minutes", UnsupportedDurationField.getInstance(DurationFieldType.minutes()).getName());
    }

    @Test
    public void testIsSupported() {
        assertFalse(UnsupportedDurationField.getInstance(DurationFieldType.days()).isSupported());
        assertFalse(UnsupportedDurationField.getInstance(DurationFieldType.hours()).isSupported());
        assertFalse(UnsupportedDurationField.getInstance(DurationFieldType.minutes()).isSupported());
    }

    @Test
    public void testIsPrecise() {
        assertTrue(UnsupportedDurationField.getInstance(DurationFieldType.days()).isPrecise());
        assertTrue(UnsupportedDurationField.getInstance(DurationFieldType.hours()).isPrecise());
        assertTrue(UnsupportedDurationField.getInstance(DurationFieldType.minutes()).isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField.getInstance(DurationFieldType.days()).getValue(1000);
        UnsupportedDurationField.getInstance(DurationFieldType.hours()).getValue(2000);
        UnsupportedDurationField.getInstance(DurationFieldType.minutes()).getValue(5000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField.getInstance(DurationFieldType.days()).getValueAsLong(2000);
        UnsupportedDurationField.getInstance(DurationFieldType.hours()).getValueAsLong(5000);
        UnsupportedDurationField.getInstance(DurationFieldType.minutes()).getValueAsLong(10000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField.getInstance(DurationFieldType.days()).getValue(1000, 2000);
        UnsupportedDurationField.getInstance(DurationFieldType.hours()).getValue(2000, 3000);
        UnsupportedDurationField.getInstance(DurationFieldType.minutes()).getValue(5000, 6000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField.getInstance(DurationFieldType.days()).getValueAsLong(2000, 3000);
        UnsupportedDurationField.getInstance(DurationFieldType.hours()).getValueAsLong(5000, 6000);
        UnsupportedDurationField.getInstance(DurationFieldType.minutes()).getValueAsLong(10000, 20000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        UnsupportedDurationField.getInstance(DurationFieldType.days()).getMillis(10);
        UnsupportedDurationField.getInstance(DurationFieldType.hours()).getMillis(20);
        UnsupportedDurationField.getInstance(DurationFieldType.minutes()).getMillis(50);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLong() {
        UnsupportedDurationField.getInstance(DurationFieldType.days()).getMillis(2000L);
        UnsupportedDurationField.getInstance(DurationFieldType.hours()).getMillis(5000L);
        UnsupportedDurationField.getInstance(DurationFieldType.minutes()).getMillis(10000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstantAndValue() {
        UnsupportedDurationField.getInstance(DurationFieldType.days()).getMillis(1000, 2000);
        UnsupportedDurationField.getInstance(DurationFieldType.hours()).getMillis(2000, 3000);
        UnsupportedDurationField.getInstance(DurationFieldType.minutes()).getMillis(5000, 6000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstantAndValueAsLong() {
        UnsupportedDurationField.getInstance(DurationFieldType.days()).getMillis(2000, 3000L);
        UnsupportedDurationField.getInstance(DurationFieldType.hours()).getMillis(5000, 6000L);
        UnsupportedDurationField.getInstance(DurationFieldType.minutes()).getMillis(10000, 20000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValue() {
        UnsupportedDurationField.getInstance(DurationFieldType.days()).add(1000, 2000);
        UnsupportedDurationField.getInstance(DurationFieldType.hours()).add(2000, 3000);
        UnsupportedDurationField.getInstance(DurationFieldType.minutes()).add(5000, 6000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValueAsLong() {
        UnsupportedDurationField.getInstance(DurationFieldType.days()).add(2000, 3000L);
        UnsupportedDurationField.getInstance(DurationFieldType.hours()).add(5000, 6000L);
        UnsupportedDurationField.getInstance(DurationFieldType.minutes()).add(10000, 20000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField.getInstance(DurationFieldType.days()).getDifference(1000, 2000);
        UnsupportedDurationField.getInstance(DurationFieldType.hours()).getDifference(2000, 3000);
        UnsupportedDurationField.getInstance(DurationFieldType.minutes()).getDifference(5000, 6000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField.getInstance(DurationFieldType.days()).getDifferenceAsLong(2000, 3000);
        UnsupportedDurationField.getInstance(DurationFieldType.hours()).getDifferenceAsLong(5000, 6000);
        UnsupportedDurationField.getInstance(DurationFieldType.minutes()).getDifferenceAsLong(10000, 20000);
    }

    @Test
    public void testGetUnitMillis() {
        assertEquals(0, UnsupportedDurationField.getInstance(DurationFieldType.days()).getUnitMillis());
        assertEquals(0, UnsupportedDurationField.getInstance(DurationFieldType.hours()).getUnitMillis());
        assertEquals(0, UnsupportedDurationField.getInstance(DurationFieldType.minutes()).getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        assertEquals(0, UnsupportedDurationField.getInstance(DurationFieldType.days()).compareTo(UnsupportedDurationField.getInstance(DurationFieldType.days())));
        assertEquals(0, UnsupportedDurationField.getInstance(DurationFieldType.hours()).compareTo(UnsupportedDurationField.getInstance(DurationFieldType.hours())));
        assertEquals(0, UnsupportedDurationField.getInstance(DurationFieldType.minutes()).compareTo(UnsupportedDurationField.getInstance(DurationFieldType.minutes())));
    }
}
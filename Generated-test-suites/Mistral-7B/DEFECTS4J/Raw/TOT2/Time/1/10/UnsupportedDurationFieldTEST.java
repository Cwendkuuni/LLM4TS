package org.junit.test;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        DurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        DurationFieldType type = durationField.getType();
        assertEquals(DurationFieldType.DAYS, type);
    }

    @Test
    public void testGetName() {
        DurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        String name = durationField.getName();
        assertEquals("DAYS", name);
    }

    @Test
    public void testIsSupported() {
        DurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertFalse(durationField.isSupported());
    }

    @Test
    public void testGetInstanceInvalidType() {
        DurationFieldType invalidType = new DurationFieldType("INVALID");
        DurationField durationField = UnsupportedDurationField.getInstance(invalidType);
        assertNotNull(durationField);
    }

    @Test
    public void testIsPrecise() {
        DurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertTrue(durationField.isPrecise());
    }

    @Test
    public void testGetInstanceMultipleTimes() {
        DurationFieldType type = DurationFieldType.DAYS;
        DurationField unsupportedField1 = UnsupportedDurationField.getInstance(type);
        DurationField unsupportedField2 = UnsupportedDurationField.getInstance(type);
        Assert.assertSame(unsupportedField1, unsupportedField2);
    }

    @Test
    public void testGetValueLongDuration() {
        DurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        throwsException(() -> durationField.getValue(123456789L));
    }

    @Test
    public void testGetInstanceNullType() {
        DurationFieldType type = null;
        DurationField durationField = UnsupportedDurationField.getInstance(type);
        assertNull(durationField);
    }

    @Test
    public void testCompareTo() {
        DurationField supportedField = DurationField.standardInstances().get(DurationFieldType.MILLISECONDS);
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        int result = unsupportedField.compareTo(supportedField);
        assertEquals(1, result);
    }
}
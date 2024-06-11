
package org.example.junit;

import org.junit.*;
import static org.junit.Assert.*;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedDurationField;

public class UnsupportedDurationFieldTest {

    @Test
    public void testIsSupported() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertFalse(unsupportedField.isSupported());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueWithDifferentDuration() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        long duration = 1000;
        unsupportedField.getValue(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueWithDifferentInstant() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        long duration = 1000;
        long instant = 1234567890L;
        unsupportedField.getValue(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        long instant = 1234567890L;
        int value = 10;
        unsupportedField.add(instant, value);
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertTrue(unsupportedField1.equals(unsupportedField2));
    }

    @Test
    public void testGetInstanceWithNullType() {
        assertNull(UnsupportedDurationField.getInstance(null));
    }

    @Test
    public void testGetInstanceWithEmptyCache() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField cachedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertSame(unsupportedField, cachedField);
    }

    @Test
    public void testGetInstanceWithSameTypeTwice() {
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertSame(unsupportedField1, unsupportedField2);
    }

}
package org.joda.time.field.test;

import org.junit.Test;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testUnsupportedDurationField() {
        DurationFieldType type = DurationFieldType.weeks();
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);

        // Test getters
        assertEquals(type, unsupportedField.getType());
        assertEquals(type.getName(), unsupportedField.getName());
        assertFalse(unsupportedField.isSupported());
        assertTrue(unsupportedField.isPrecise());

        // Test value methods
        try {
            int value = unsupportedField.getValue(1);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            long value = unsupportedField.getValueAsLong(1);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            int value = unsupportedField.getValue(1, 1);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            long value = unsupportedField.getValueAsLong(1, 1);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            long millis = unsupportedField.getMillis(1);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            long millis = unsupportedField.getMillis(1L);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            long millis = unsupportedField.getMillis(1, 1);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            long millis = unsupportedField.getMillis(1L, 1);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test add methods
        try {
            long result = unsupportedField.add(1, 1);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            long result = unsupportedField.add(1L, 1);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test difference methods
        try {
            int difference = unsupportedField.getDifference(1, 1);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            long differenceAsLong = unsupportedField.getDifferenceAsLong(1, 1);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test getUnitMillis
        assertEquals(0, unsupportedField.getUnitMillis());

        // Test compareTo
        DurationField other = DurationField.standardHours(1);
        assertEquals(0, unsupportedField.compareTo(other));

        // Test equals and hashCode
        UnsupportedDurationField same = UnsupportedDurationField.getInstance(type);
        assertEquals(same, unsupportedField);
        assertEquals(same.hashCode(), unsupportedField.hashCode());
    }
}
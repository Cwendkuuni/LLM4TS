package org.joda.time.field.test;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testUnsupportedDurationField() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);

        // Test getters
        assertEquals(DurationFieldType.DAYS, field.getType());
        assertEquals("days", field.getName());
        assertFalse(field.isSupported());
        assertTrue(field.isPrecise());

        // Test value methods
        try {
            field.getValue(0L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            field.getValueAsLong(0L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            field.getValue(0L, 0L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            field.getValueAsLong(0L, 0L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            field.getMillis(0);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            field.getMillis(0L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            field.getMillis(0, 0L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            field.getMillis(0L, 0L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test add methods
        try {
            field.add(0L, 1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            field.add(0L, 1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test difference methods
        try {
            field.getDifference(0L, 0L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            field.getDifferenceAsLong(0L, 0L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test unit millis method
        assertEquals(0, field.getUnitMillis());

        // Test compareTo method
        DurationField other = DurationField.FIFTEEN_MINUTES;
        assertNotSame(field, other);
        assertTrue(field.compareTo(other) > 0);
    }
}
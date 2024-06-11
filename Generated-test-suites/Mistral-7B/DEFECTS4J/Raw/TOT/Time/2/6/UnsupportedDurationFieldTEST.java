package org.joda.time.field.test;

import org.junit.Test;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testUnsupportedDurationField() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);

        // Test isSupported returns false
        assertFalse(unsupportedField.isSupported());

        // Test getValue, getValueAsLong throws UnsupportedOperationException
        try {
            unsupportedField.getValue(1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.getValueAsLong(1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test getDifference, getDifferenceAsLong throws UnsupportedOperationException
        try {
            unsupportedField.getDifference(0, 1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.getDifferenceAsLong(0, 1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test add, add throws UnsupportedOperationException
        try {
            unsupportedField.add(0, 1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            unsupportedField.add(0, 1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
}
package org.joda.time.field.test;

import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.UnsupportedOperationException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

    @Test
    public void testUnsupportedDurationField() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANT_DAYS);

        try {
            int value = unsupportedField.getValue(1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            long valueAsLong = unsupportedField.getValueAsLong(1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            int valueAtInstant = unsupportedField.getValue(1, 1234567890123L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            long valueAsLongAtInstant = unsupportedField.getValueAsLong(1, 1234567890123L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            long millis = unsupportedField.getMillis(1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            long millis = unsupportedField.getMillis(1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            long millisAtInstant = unsupportedField.getMillis(1, 1234567890123L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            long millisAtInstant = unsupportedField.getMillis(1L, 1234567890123L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            long add = unsupportedField.add(1234567890123L, 1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            long add = unsupportedField.add(1234567890123L, 1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            int difference = unsupportedField.getDifference(1234567890123L, 1234567890124L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            long differenceAsLong = unsupportedField.getDifferenceAsLong(1234567890123L, 1234567890124L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            long unitMillis = unsupportedField.getUnitMillis();
            fail("Expected zero");
        } catch (AssertionError e) {
            // expected
        }

        try {
            int compareTo = unsupportedField.compareTo(null);
            fail("Expected zero");
        } catch (AssertionError e) {
            // expected
        }
    }
}
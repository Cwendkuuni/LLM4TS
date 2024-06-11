package org.joda.time.field;

import static org.junit.Assert.*;

import org.joda.time.DurationFieldType;

import org.joda.time.base.AbstractBaseTestCase;
import org.joda.time.UnsupportedOperationException;

/**
 * JUnit test file for UnsupportedDurationField class.
 */
public class UnsupportedDurationFieldTest extends AbstractBaseTestCase {

    /**
     * Test method for UnsupportedDurationField.
     */
    public void testUnsupportedDurationField() {
        DurationFieldType type = DurationFieldType.FOO;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);

        assertEquals("Unexpected type", type, unsupportedField.getType());
        assertEquals("Unexpected name", type.getName(), unsupportedField.getName());
        assertEquals("Unexpected isSupported", false, unsupportedField.isSupported());
        assertEquals("Unexpected isPrecise", true, unsupportedField.isPrecise());

        try {
            unsupportedField.getValue(1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }

        try {
            unsupportedField.getValueAsLong(1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }

        try {
            unsupportedField.getValue(1L, 1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }

        try {
            unsupportedField.getValueAsLong(1L, 1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }

        try {
            unsupportedField.getMillis(1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }

        try {
            unsupportedField.getMillis(1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }

        try {
            unsupportedField.getMillis(1, 1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }

        try {
            unsupportedField.getMillis(1L, 1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }

        try {
            unsupportedField.add(1L, 1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }

        try {
            unsupportedField.add(1L, 1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }

        try {
            unsupportedField.getDifference(1L, 1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }

        try {
            unsupportedField.getDifferenceAsLong(1L, 1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }

        assertEquals("Unexpected getUnitMillis", 0, unsupportedField.getUnitMillis());

        try {
            unsupportedField.compareTo(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            //Expected
        }

        try {
            unsupportedField.compareTo(new UnsupportedDurationField(type));
            fail("Expected 1");
        } catch (Throwable e) {
            //Unexpected
        }

        try {
            unsupportedField.compareTo(new DurationField() {});
            fail("Expected 1");
        } catch (ClassCastException e) {
            //Expected
        }

        try {
            unsupportedField.compareTo(new DurationFieldType() {});
            fail("Expected 1");
        } catch (ClassCastException e) {
            //Expected
        }

        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type);
        assertEquals("Unexpected unsupported fields", unsupportedField, unsupportedField1);
    }
}
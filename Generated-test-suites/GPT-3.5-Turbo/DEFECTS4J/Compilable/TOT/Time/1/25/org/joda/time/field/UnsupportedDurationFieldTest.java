package org.joda.time.field;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        assertEquals(DurationFieldType.centuries(), field.getType());
    }

    @Test
    public void testGetName_SupportedField() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        assertEquals("centuries", field.getName());
    }

    @Test
    public void testGetName_UnsupportedField() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertEquals("months", field.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertFalse(unsupportedField.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertTrue(unsupportedField.isPrecise());
    }

    @Test
    public void testGetValue_ThrowsUnsupportedOperationException() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.months());
        try {
            unsupportedField.getValue(1000);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Passed
        }
    }

    @Test
    public void testGetValueAsLong_ThrowsUnsupportedOperationException() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.months());
        try {
            unsupportedField.getValueAsLong(1000);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Passed
        }
    }

    @Test
    public void testGetValue_WithInstant_ThrowsUnsupportedOperationException() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.months());
        try {
            unsupportedField.getValue(1000, 500);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Passed
        }
    }

    @Test
    public void testGetValueAsLong_WithInstant_ThrowsUnsupportedOperationException() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.months());
        try {
            unsupportedField.getValueAsLong(1000, 500);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Passed
        }
    }

    @Test
    public void testGetMillis_ThrowsUnsupportedOperationException() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.months());
        try {
            unsupportedField.getMillis(10);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Passed
        }
    }

    @Test
    public void testGetMillis_WithInstant_ThrowsUnsupportedOperationException() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.months());
        try {
            unsupportedField.getMillis(10, 5);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Passed
        }
    }

    @Test
    public void testAdd_ThrowsUnsupportedOperationException() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.months());
        try {
            unsupportedField.add(1000, 10);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Passed
        }
    }

    @Test
    public void testGetDifference_ThrowsUnsupportedOperationException() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.months());
        try {
            unsupportedField.getDifference(2000, 1000);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Passed
        }
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertEquals(0, unsupportedField.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        assertEquals(1, unsupportedField1.compareTo(unsupportedField2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        UnsupportedDurationField unsupportedField3 = UnsupportedDurationField.getInstance(DurationFieldType.weeks());

        assertTrue(unsupportedField1.equals(unsupportedField2));
        assertFalse(unsupportedField1.equals(unsupportedField3));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        UnsupportedDurationField unsupportedField3 = UnsupportedDurationField.getInstance(DurationFieldType.weeks());

        assertEquals(unsupportedField1.hashCode(), unsupportedField2.hashCode());
        assertFalse(unsupportedField1.hashCode() == unsupportedField3.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertEquals("UnsupportedDurationField[months]", unsupportedField.toString());
    }
}
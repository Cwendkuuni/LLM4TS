package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.joda.time.DurationFieldType;
import org.joda.time.TestUtil;
import org.joda.time.field.UnsupportedDurationField;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance_singleton() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertTrue(field1 == field2);
    }

    @Test
    public void testGetInstance_typeNotNull() {
        try {
            UnsupportedDurationField.getInstance(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {}
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertTrue(field.isPrecise());
    }

    @Test
    public void testGetValue_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getValue(0);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {}
    }

    @Test
    public void testGetValueAsLong_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getValueAsLong(0);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {}
    }

    @Test
    public void testGetValue_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getValue(0, 0);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {}
    }

    @Test
    public void testGetValueAsLong_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getValueAsLong(0, 0);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {}
    }

    @Test
    public void testGetMillis_int() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getMillis(0);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {}
    }

    @Test
    public void testGetMillis_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getMillis(0L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {}
    }

    @Test
    public void testGetMillis_int_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getMillis(0, 0L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {}
    }

    @Test
    public void testGetMillis_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getMillis(0L, 0L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {}
    }

    @Test
    public void testAdd_long_int() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.add(0L, 0);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {}
    }

    @Test
    public void testAdd_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.add(0L, 0L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {}
    }

    @Test
    public void testGetDifference_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getDifference(0L, 0L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {}
    }

    @Test
    public void testGetDifferenceAsLong_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getDifferenceAsLong(0L, 0L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {}
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertEquals(0L, field.getUnitMillis());
    }

    @Test
    public void testCompareTo_supportedFalse() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        DurationField supportedField = TestUtil.getField(DurationFieldType.millis());
        assertTrue(field.compareTo(supportedField) > 0);
    }

    @Test
    public void testCompareTo_supportedTrue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        UnsupportedDurationField otherField = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertTrue(field.compareTo(otherField) == 0);
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        UnsupportedDurationField otherField = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertTrue(field.equals(otherField));
    }

    @Test
    public void testEquals_objNull() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertFalse(field.equals(null));
    }

    @Test
    public void testEquals_objSame() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertTrue(field.equals(field));
    }

    @Test
    public void testEquals_objDiffClass() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertFalse(field.equals(""));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        UnsupportedDurationField otherField = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertTrue(field.hashCode() == otherField.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertEquals("UnsupportedDurationField[millis]", field.toString());
    }
}
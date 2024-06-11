package org.joda.time.field.test;

import org.junit.Test;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test for UnsupportedDurationField.
 */
public class UnsupportedDurationFieldTest {

    /**
     * Test getInstance method.
     */
    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.Seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type, field.getType());
        assertEquals("Seconds", field.getName());
        assertEquals(false, field.isSupported());
        assertEquals(true, field.isPrecise());
    }

    /**
     * Test value method throws UnsupportedOperationException.
     */
    @Test
    public void testValueThrowsException() {
        DurationFieldType type = DurationFieldType.Seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long duration = 1000L;
        assertThrows(UnsupportedOperationException.class, () -> field.getValue(duration));
    }

    /**
     * Test valueAsLong method throws UnsupportedOperationException.
     */
    @Test
    public void testValueAsLongThrowsException() {
        DurationFieldType type = DurationFieldType.Seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long duration = 1000L;
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(duration));
    }

    /**
     * Test add method throws UnsupportedOperationException.
     */
    @Test
    public void testAddThrowsException() {
        DurationFieldType type = DurationFieldType.Seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long instant = 1000L;
        int value = 2;
        assertThrows(UnsupportedOperationException.class, () -> field.add(instant, value));
    }

    /**
     * Test compareTo method returns 1.
     */
    @Test
    public void testCompareTo() {
        DurationFieldType supportedFieldType = DurationFieldType.Seconds();
        DurationField supportedField = DurationFieldFactory.getInstance().getDurationField(supportedFieldType);
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(supportedFieldType);

        assertEquals(1, unsupportedField.compareTo(supportedField));
    }

    /**
     * Test equals method returns true for same instance.
     */
    @Test
    public void testEqualsSameInstance() {
        DurationFieldType type = DurationFieldType.Seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(field, field);
    }

    /**
     * Test equals method returns false for different instance.
     */
    @Test
    public void testEqualsDifferentInstance() {
        DurationFieldType type = DurationFieldType.Seconds();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.Milliseconds());

        assertFalse(field1.equals(field2));
        assertFalse(field1.equals(field3));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.Seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        String name = "Seconds";
        assertEquals(name.hashCode(), field.hashCode());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.Seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[Seconds]", field.toString());
    }
}
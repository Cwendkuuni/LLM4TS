package org.joda.time.field.test;

import org.junit.Test;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedDurationField;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test class for UnsupportedDurationField.
 *
 * @author John Doe
 */
public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupportedField1, unsupportedField2);
    }

    @Test
    public void testIsSupported() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertFalse(unsupportedField.isSupported());
    }

    @Test
    public void testGetName() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertEquals("milliseconds", unsupportedField.getName());
    }

    @Test
    public void testGetValue() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValue(1L));
    }

    @Test
    public void testGetValueAsLong() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValueAsLong(1L));
    }

    @Test
    public void testGetValueAtInstant() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValue(1L, 1L));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValueAsLong(1L, 1L));
    }

    @Test
    public void testGetMillis() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1L));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1, 1L));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1L, 1L));
    }

    @Test
    public void testAdd() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(1L, 1));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(1L, 1L));
    }

    @Test
    public void testGetDifference() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifference(1L, 1L));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifferenceAsLong(1L, 1L));
    }

    @Test
    public void testGetUnitMillis() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertEquals(0, unsupportedField.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        DurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        DurationField supportedField = new DurationField(1, DurationFieldType.MILLISECONDS);
        assertEquals(1, unsupportedField1.compareTo(unsupportedField2));
        assertEquals(0, unsupportedField1.compareTo(supportedField));
    }

    @Test
    public void testEquals() {
        DurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        DurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertFalse(unsupportedField1.equals(null));
        assertFalse(unsupportedField1.equals(new Object()));
        assertFalse(unsupportedField1.equals(unsupportedField2.getClass()));
        assertTrue(unsupportedField1.equals(unsupportedField2));
    }

    @Test
    public void testHashCode() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        String name = unsupportedField.getName();
        int hashCode = unsupportedField.hashCode();
        assertEquals(hashCode, name.hashCode());
    }

    @Test
    public void testToString() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        String expected = "UnsupportedDurationField[milliseconds]";
        assertEquals(expected, unsupportedField.toString());
    }

    @Test
    public void testReadResolve() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertNull(unsupportedField.readResolve());
    }

    @Test
    public void testUnsupported() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedOperationException unsupported = unsupportedField.unsupported();
        assertEquals("milliseconds field is unsupported", unsupported.getMessage());
    }
}
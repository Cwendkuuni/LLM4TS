package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.milliseconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupported, unsupported2);
    }

    @Test
    public void testUnsupportedIsNotSupported() {
        DurationFieldType type = DurationFieldType.milliseconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertFalse(unsupported.isSupported());
    }

    @Test
    public void testUnsupportedIsPrecise() {
        DurationFieldType type = DurationFieldType.milliseconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertTrue(unsupported.isPrecise());
    }

    @Test
    public void testUnsupportedValue() {
        DurationFieldType type = DurationFieldType.milliseconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValue(1));
    }

    @Test
    public void testUnsupportedValueAsLong() {
        DurationFieldType type = DurationFieldType.milliseconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValueAsLong(1));
    }

    @Test
    public void testUnsupportedValueAtInstant() {
        DurationFieldType type = DurationFieldType.milliseconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValue(1, 1l));
    }

    @Test
    public void testUnsupportedValueAsLongAtInstant() {
        DurationFieldType type = DurationFieldType.milliseconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValueAsLong(1, 1l));
    }

    @Test
    public void testUnsupportedMillis() {
        DurationFieldType type = DurationFieldType.milliseconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals(0, unsupported.getUnitMillis());
    }

    @Test
    public void testUnsupportedCompareTo() {
        DurationFieldType type1 = DurationFieldType.milliseconds();
        DurationFieldType type2 = DurationFieldType.seconds();
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);
        int comparison = unsupported1.compareTo(unsupported2);
        assertTrue(comparison > 0);
    }

    @Test
    public void testUnsupportedEquals() {
        DurationFieldType type1 = DurationFieldType.milliseconds();
        DurationFieldType type2 = DurationFieldType.milliseconds();
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);
        UnsupportedDurationField unsupported3 = UnsupportedDurationField.getInstance(type1);
        assertEquals(unsupported1, unsupported1);
        assertEquals(unsupported1, unsupported3);
        assertNotEquals(unsupported1, new Object());
        assertNotEquals(unsupported1, unsupported2);
    }

    @Test
    public void testUnsupportedHashCode() {
        DurationFieldType type = DurationFieldType.milliseconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        int hashcode = unsupported.hashCode();
        assertEquals(hashcode, unsupported.hashCode());
    }

    @Test
    public void testUnsupportedToString() {
        DurationFieldType type = DurationFieldType.milliseconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[milliseconds]", unsupported.toString());
    }

    @Test
    public void testUnsupportedReadResolve() {
        DurationFieldType type = DurationFieldType.milliseconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertSame(unsupported, unsupported.readResolve());
    }

    @Test
    public void testUnsupportedSerialization() {
        DurationFieldType type = DurationFieldType.milliseconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        byte[] serialized = new java.io.ByteArrayOutputStream()
                .<byte[]>toByteArray(new java.io.ObjectOutputStream(new java.io.ByteArrayOutputStream())
                        .writeObject(unsupported));
        UnsupportedDurationField deserialized = (UnsupportedDurationField) new java.io.ObjectInputStream(
                new java.io.ByteArrayInputStream(serialized)).readObject();
        assertSame(unsupported, deserialized);
    }
}
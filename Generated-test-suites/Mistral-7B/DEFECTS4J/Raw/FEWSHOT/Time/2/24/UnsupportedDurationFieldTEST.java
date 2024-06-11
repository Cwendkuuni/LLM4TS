package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTimeout;

import org.joda.time.DurationFieldType;
import org.joda.time.DurationFieldTypeValidator;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testCache() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals(field1, field2);
    }

    @Test
    public void testGetInstance() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals(DurationFieldType.years(), field.getType());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertTrue(field.isPrecise());
    }

    @Test
    public void testValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertThrows(UnsupportedOperationException.class, () -> field.getValue(0L));
    }

    @Test
    public void testValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(0L));
    }

    @Test
    public void testValueAtInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertThrows(UnsupportedOperationException.class, () -> field.getValue(1234567890123L, 1234567890123L));
    }

    @Test
    public void testValueAtInstantAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(1234567890123L, 1234567890123L));
    }

    @Test
    public void testGetMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(0));
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1));
    }

    @Test
    public void testAdd() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertThrows(UnsupportedOperationException.class, () -> field.add(1234567890123L, 1));
    }

    @Test
    public void testAddLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertThrows(UnsupportedOperationException.class, () -> field.add(1234567890123L, 1L));
    }

    @Test
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertThrows(UnsupportedOperationException.class, () -> field.getDifference(1234567890123L, 1234567890123L));
    }

    @Test
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertThrows(UnsupportedOperationException.class, () -> field.getDifferenceAsLong(1234567890123L, 1234567890123L));
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals(field1, field2);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals(field1.hashCode(), field2.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals("UnsupportedDurationField[years]", field.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.years());
        assertSame(UnsupportedDurationField.getInstance(DurationFieldType.years()), field.readResolve());
    }

    @Test
    public void testUnsupported() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.years());
        assertSame(field.unsupported(), field.new UnsupportedOperationException("years field is unsupported"));
    }

    @Test
    public void testGetType() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.years());
        assertEquals(DurationFieldType.years(), field.getType());
    }

    @Test
    public void testName() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.years());
        assertEquals("years", field.getName());
    }

    @Test
    public void testSingletonSerialization() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.years());
        assertSame(field, field.readResolve());
    }

    @Test
    public void testGetSerialVersionUID() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.years());
        assertEquals(-6390301302770925357L, field.serialVersionUID);
    }

    @Test
    public void testSingletonSerialization_invalidType() {
        UnsupportedDurationField field = new UnsupportedDurationField(new DurationFieldTypeValidator.InvalidDurationFieldType("test"));
        assertNull(field.readResolve());
    }

    @Test
    public void testSerializability() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.years());
        // Serialization test
        byte[] bytes = new java.io.ByteArrayOutputStream().toByteArray(new java.io.ObjectOutputStream(new java.io.ByteArrayOutputStream()).writeObject(field));
        // Deserialization test
        UnsupportedDurationField deserializedField = (UnsupportedDurationField) new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(bytes)).readObject();
        assertEquals(DurationFieldType.years(), deserializedField.getType());
    }

    @Test
    public void testSerializability_invalidType() {
        UnsupportedDurationField field = new UnsupportedDurationField(new DurationFieldTypeValidator.InvalidDurationFieldType("test"));
        byte[] bytes = new java.io.ByteArrayOutputStream().toByteArray(new java.io.ObjectOutputStream(new java.io.ByteArrayOutputStream()).writeObject(field));
        assertThrows(ClassCastException.class, () -> new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(bytes)).readObject());
    }

    @Test
    public void testGetCache() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertNotNull(UnsupportedDurationField.cCache);
        assertEquals(1, UnsupportedDurationField.cCache.size());
        assertSame(field, UnsupportedDurationField.cCache.get(DurationFieldType.years()));
    }

    @Test
    public void testGetCache_multipleInstances() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals(field1, field2);
        assertEquals(1, UnsupportedDurationField.cCache.size());
        assertSame(field1, UnsupportedDurationField.cCache.get(DurationFieldType.years()));

        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals(field1, field3);
        assertEquals(1, UnsupportedDurationField.cCache.size());
        assertSame(field1, UnsupportedDurationField.cCache.get(DurationFieldType.years()));
    }

    @Test
    public void testGetCache_multipleTypes() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertEquals(field1, field1);
        assertEquals(field2, field2);
        assertEquals(2, UnsupportedDurationField.cCache.size());
        assertSame(field1, UnsupportedDurationField.cCache.get(DurationFieldType.years()));
        assertSame(field2, UnsupportedDurationField.cCache.get(DurationFieldType.months()));
    }

    @Test
    public void testGetCache_serialization() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        byte[] bytes = new java.io.ByteArrayOutputStream().toByteArray(new java.io.ObjectOutputStream(new java.io.ByteArrayOutputStream()).writeObject(field));
        UnsupportedDurationField deserializedField = (UnsupportedDurationField) new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(bytes)).readObject();
        assertEquals(field, deserializedField);
        assertEquals(1, UnsupportedDurationField.cCache.size());
        assertSame(field, UnsupportedDurationField.cCache.get(DurationFieldType.years()));
    }

    @Test
    public void testGetCache_serialization_multipleInstances() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        byte[] bytes = new java.io.ByteArrayOutputStream().toByteArray(new java.io.ObjectOutputStream(new java.io.ByteArrayOutputStream()).writeObject(field1));
        UnsupportedDurationField deserializedField1 = (UnsupportedDurationField) new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(bytes)).readObject();
        assertEquals(field1, deserializedField1);
        byte[] bytes2 = new java.io.ByteArrayOutputStream().toByteArray(new java.io.ObjectOutputStream(new java.io.ByteArrayOutputStream()).writeObject(field2));
        UnsupportedDurationField deserializedField2 = (UnsupportedDurationField) new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(bytes2)).readObject();
        assertEquals(field1, deserializedField1);
        assertEquals(field2, deserializedField2);
        assertEquals(2, UnsupportedDurationField.cCache.size());
        assertSame(field1, UnsupportedDurationField.cCache.get(DurationFieldType.years()));
        assertSame(field2, UnsupportedDurationField.cCache.get(DurationFieldType.months()));
    }

    @Test
    public void testGetCache_serialization_multipleTypes_singleton() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        byte[] bytes = new java.io.ByteArrayOutputStream().toByteArray(new java.io.ObjectOutputStream(new java.io.ByteArrayOutputStream()).writeObject(field1));
        UnsupportedDurationField deserializedField1 = (UnsupportedDurationField) new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(bytes)).readObject();
        assertEquals(field1, deserializedField1);
        byte[] bytes2 = new java.io.ByteArrayOutputStream().toByteArray(new java.io.ObjectOutputStream(new java.io.ByteArrayOutputStream()).writeObject(field2));
        UnsupportedDurationField deserializedField2 = (UnsupportedDurationField) new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(bytes2)).readObject();
        assertEquals(field1, deserializedField1);
        assertEquals(field2, deserializedField2);
        assertEquals(2, UnsupportedDurationField.cCache.size());
        assertSame(field1, UnsupportedDurationField.cCache.get(DurationFieldType.years()));
        assertSame(field2, UnsupportedDurationField.cCache.get(DurationFieldType.months()));

        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        deserializedField1 = null;
        bytes = new java.io.ByteArrayOutputStream().toByteArray(new java.io.ObjectOutputStream(new java.io.ByteArrayOutputStream()).writeObject(field3));
        deserializedField1 = (UnsupportedDurationField) new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(bytes)).readObject();
        assertEquals(field1, deserializedField1);
        deserializedField3 = null;
        bytes2 = new java.io.ByteArrayOutputStream().toByteArray(new java.io.ObjectOutputStream(new java.io.ByteArrayOutputStream()).writeObject(field2));
        deserializedField3 = (UnsupportedDurationField) new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(bytes2)).readObject();
        assertEquals(field2, deserializedField3);
        assertEquals(3, UnsupportedDurationField.cCache.size());
        assertSame(field1, UnsupportedDurationField.cCache.get(DurationFieldType.years()));
        assertSame(field2, UnsupportedDurationField.cCache.get(DurationFieldType.months()));
        assertSame(deserializedField1, UnsupportedDurationField.cCache.get(DurationFieldType.years()));
        assertSame(deserializedField3, UnsupportedDurationField.cCache.get(DurationFieldType.months()));
    }
}
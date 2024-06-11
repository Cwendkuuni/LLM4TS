package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertNotNull(field);
        assertEquals(type, field.getType());
    }

    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertNotNull(field);
        assertEquals(type.getName(), field.getName());
    }

    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertNotNull(field);
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertNotNull(field);
        assertTrue(field.isPrecise());
    }

    @Test
    public void testGetValue_long() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertNotNull(field);
        try {
            field.getValue(1000L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testGetValueAsLong_long() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertNotNull(field);
        try {
            field.getValueAsLong(1000L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testGetValue_long_long() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertNotNull(field);
        try {
            field.getValue(1000L, 2000L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testGetValueAsLong_long_long() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertNotNull(field);
        try {
            field.getValueAsLong(1000L, 2000L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testGetMillis_int() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertNotNull(field);
        try {
            field.getMillis(1);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testGetMillis_long() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertNotNull(field);
        try {
            field.getMillis(1000L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testGetMillis_int_long() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertNotNull(field);
        try {
            field.getMillis(1, 2000L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testGetMillis_long_long() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertNotNull(field);
        try {
            field.getMillis(1000L, 2000L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testAdd_long_int() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertNotNull(field);
        try {
            field.add(1000L, 1);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testAdd_long_long() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertNotNull(field);
        try {
            field.add(1000L, 2000L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testGetDifference_long_long() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertNotNull(field);
        try {
            field.getDifference(1000L, 2000L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testGetDifferenceAsLong_long_long() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertNotNull(field);
        try {
            field.getDifferenceAsLong(1000L, 2000L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertNotNull(field);
        assertEquals(0L, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertNotNull(field);
        assertEquals(0, field.compareTo(field));
    }

    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field1 = new UnsupportedDurationField(type);
        UnsupportedDurationField field2 = new UnsupportedDurationField(type);
        assertNotNull(field1);
        assertNotNull(field2);
        assertTrue(field1.equals(field1));
        assertTrue(field1.equals(field2));
        assertFalse(field1.equals(null));
        assertFalse(field1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertNotNull(field);
        assertEquals(type.getName().hashCode(), field.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertNotNull(field);
        assertEquals("UnsupportedDurationField[" + type.getName() + ']', field.toString());
    }

    @Test
    public void testReadResolve() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertNotNull(field);
        UnsupportedDurationField resolved = (UnsupportedDurationField) field.readResolve();
        assertNotNull(resolved);
        assertEquals(field.getType(), resolved.getType());
    }

    private UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException(iType + " field is unsupported");
    }
}
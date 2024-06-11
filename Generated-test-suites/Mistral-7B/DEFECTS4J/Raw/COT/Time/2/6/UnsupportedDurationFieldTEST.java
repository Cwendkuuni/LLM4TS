package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test class for UnsupportedDurationField.
 */
public class UnsupportedDurationFieldTest {

    /**  */

    @BeforeClass
    public static void setUpBeforeClass() {
        // No setup required
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetInstance_TypeIsNull() {
        DurationFieldType type = null;
        UnsupportedDurationField instance = UnsupportedDurationField.getInstance(type);
        assertNull(instance);
    }

    @Test
    public void testGetInstance_ValidType() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField instance = UnsupportedDurationField.getInstance(type);
        assertNotNull(instance);
        assertEquals(type, instance.getType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsSupported() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_Long() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.getValue(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_Long() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.getValueAsLong(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_LongLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.getValue(1L, 2L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_LongLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.getValueAsLong(1L, 2L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_Int() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_Long() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.getMillis(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_IntLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.getMillis(1, 2L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_LongLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.getMillis(1L, 2L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_LongLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.add(1L, 2L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_LongLongLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.add(1L, 2L, 3L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference_LongLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.getDifference(1L, 2L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong_LongLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.getDifferenceAsLong(1L, 2L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo_Null() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedDurationField field2 = null;
        assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testCompareTo_SameInstance() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedDurationField field2 = field1;
        assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testCompareTo_DifferentInstance() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.HOURS);
        assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testEquals_SameInstance() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedDurationField field2 = field1;
        assertTrue(field1.equals(field2));
    }

    @Test
    public void testEquals_Null() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertFalse(field.equals(null));
    }

    @Test
    public void testEquals_OtherClass() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertFalse(field.equals(new Object()));
    }

    @Test
    public void testEquals_DifferentName() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.HOURS);
        assertFalse(field1.equals(field2));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        int hashCode = field.hashCode();
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.HOURS);
        int hashCode2 = field2.hashCode();
        assertNotEquals(hashCode, hashCode2);
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        String toString = field.toString();
        String expected = "UnsupportedDurationField[DAYS]";
        assertEquals(expected, toString);
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedDurationField resolved = (UnsupportedDurationField) field.readResolve();
        assertEquals(field.getType(), resolved.getType());
    }

    @Test
    public void testUnsupported() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedOperationException exception = field.unsupported();
        String message = exception.getMessage();
        String expected = "DAYS field is unsupported";
        assertEquals(expected, message);
    }

    /**  */
}
package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import org.junit.Test;

/**
 * Test class for UnsupportedDurationField.
 */
public class UnsupportedDurationFieldTest {

    /**
     * Test getInstance method.
     */
    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertNotNull(field);
        assertEquals(type, field.getType());
        assertEquals(type.getName(), field.getName());
    }

    /**
     * Test isSupported method.
     */
    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertFalse(field.isSupported());
    }

    /**
     * Test getValue method.
     */
    @Test
    public void testGetValue() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.getValue(1));
    }

    /**
     * Test getValueAsLong method.
     */
    @Test
    public void testGetValueAsLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(1));
    }

    /**
     * Test getValue method with instant.
     */
    @Test
    public void testGetValueWithInstant() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.getValue(1, 1));
    }

    /**
     * Test getValueAsLong method with instant.
     */
    @Test
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(1, 1));
    }

    /**
     * Test getMillis method.
     */
    @Test
    public void testGetMillis() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1));
    }

    /**
     * Test getMillis method with value.
     */
    @Test
    public void testGetMillisWithValue() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1));
    }

    /**
     * Test getMillis method with value and instant.
     */
    @Test
    public void testGetMillisWithValueAndInstant() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1, 1));
    }

    /**
     * Test getMillis method with value and instant.
     */
    @Test
    public void testGetMillisWithValueAndInstant2() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(2, 1));
    }

    /**
     * Test add method.
     */
    @Test
    public void testAdd() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.add(1, 1));
    }

    /**
     * Test add method with instant.
     */
    @Test
    public void testAddWithInstant() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.add(1, 1, 1));
    }

    /**
     * Test getDifference method.
     */
    @Test
    public void testGetDifference() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.getDifference(1, 1));
    }

    /**
     * Test getDifferenceAsLong method.
     */
    @Test
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.getDifferenceAsLong(1, 1));
    }

    /**
     * Test getUnitMillis method.
     */
    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertEquals(0, field.getUnitMillis());
    }

    /**
     * Test compareTo method.
     */
    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertEquals(0, field1.compareTo(field2));
    }

    /**
     * Test equals method with null.
     */
    @Test
    public void testEqualsNull() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertFalse(field.equals(null));
    }

    /**
     * Test equals method with other UnsupportedDurationField.
     */
    @Test
    public void testEqualsSameType() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertTrue(field1.equals(field2));
    }

    /**
     * Test equals method with other UnsupportedDurationField
     * having different name.
     */
    @Test
    public void testEqualsDifferentName() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.HOURS);
        assertFalse(field1.equals(field2));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertEquals(DurationFieldType.DAYS.hashCode(), field.hashCode());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertEquals("UnsupportedDurationField[DAYS]", field.toString());
    }

    /**
     * Test readResolve method.
     */
    @Test
    public void testReadResolve() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedDurationField resolved = (UnsupportedDurationField) field.readResolve();
        assertEquals(field.getType(), resolved.getType());
        assertEquals(field.getName(), resolved.getName());
    }

    /**
     * Test unsupported method.
     */
    @Test
    public void testUnsupported() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedOperationException exception = field.unsupported();
        assertEquals(DurationFieldType.DAYS + " field is unsupported", exception.getMessage());
    }

    /**
     * Test getInstance method with null argument.
     */
    @Test(expected = NullPointerException.class)
    public void testGetInstanceNullArgument() {
        UnsupportedDurationField.getInstance(null);
    }

    /**
     * Test getInstance method with already cached instance.
     */
    @Test
    public void testGetInstanceCachedInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(field1, field2);
    }

    /**
     * Test getInstance method with new instance creation.
     */
    @Test
    public void testGetInstanceNewInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = new UnsupportedDurationField(type);
        assertEquals(field1, field2);
    }

    /**
     * Test getInstance method with different types.
     */
    @Test
    public void testGetInstanceDifferentTypes() {
        DurationFieldType type1 = DurationFieldType.DAYS;
        DurationFieldType type2 = DurationFieldType.HOURS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        assertFalse(field1.equals(field2));
    }

    /**
     * Test getInstance method with multiple threads.
     */
    @Test
    public void testGetInstanceMultipleThreads() {
        Map<Long, UnsupportedDurationField> fields = new HashMap<>(2);
        Runnable runnable = () -> fields.put(Thread.currentThread().getId(), UnsupportedDurationField.getInstance(DurationFieldType.DAYS));
        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        UnsupportedDurationField field1 = fields.get(thread1.getId());
        UnsupportedDurationField field2 = fields.get(thread2.getId());
        assertEquals(field1, field2);
    }

}
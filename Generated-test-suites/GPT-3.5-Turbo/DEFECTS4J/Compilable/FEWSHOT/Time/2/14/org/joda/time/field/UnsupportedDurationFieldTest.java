package org.joda.time.field;
import static org.junit.Assert.assertEquals;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {
    
    DurationFieldType type = DurationFieldType.millis();
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);

    @Test
    public void testGetType() {
        assertEquals(type, field.getType());
    }

    @Test
    public void testGetName() {
        assertEquals(type.getName(), field.getName());
    }

    @Test
    public void testIsSupported() {
        assertEquals(false, field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        assertEquals(true, field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long() {
        field.getValue(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long() {
        field.getValueAsLong(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long_long() {
        field.getValue(100, 1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long_long() {
        field.getValueAsLong(1000, 2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int() {
        field.getMillis(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long() {
        field.getMillis(2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int_long() {
        field.getMillis(100, 2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long_long() {
        field.getMillis(2000, 3000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_int() {
        field.add(1000, 10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_long() {
        field.add(2000, 5000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference_long_long() {
        field.getDifference(1000, 500);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong_long_long() {
        field.getDifferenceAsLong(1000, 500);
    }

    @Test
    public void testGetUnitMillis() {
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertEquals(0, field.compareTo(field2));
    }
    
    @Test
    public void testEquals() {
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(true, field.equals(field2));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(field2.hashCode(), field.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[" + type.getName() + ']', field.toString());
    }

}
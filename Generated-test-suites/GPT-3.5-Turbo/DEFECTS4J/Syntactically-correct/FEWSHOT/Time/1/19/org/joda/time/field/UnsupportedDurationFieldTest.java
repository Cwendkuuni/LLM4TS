package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertTrue(field != null);
        assertTrue(field.getType() == DurationFieldType.years());
    }

    @Test
    public void testGetType() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.years());
        assertEquals(DurationFieldType.years(), field.getType());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.years());
        assertEquals(DurationFieldType.years().getName(), field.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.years());
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.years());
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.years());
        field.getValue(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.years());
        field.getValueAsLong(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long_long() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.years());
        field.getValue(1000L, 2000L);
    }

   @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long_long() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.years());
        field.getValueAsLong(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.years());
        field.getMillis(10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.years());
        field.getMillis(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int_long() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.years());
        field.getMillis(10, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long_long() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.years());
        field.getMillis(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_int() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.years());
        field.add(1000L, 10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_long() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.years());
        field.add(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference_long_long() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.years());
        field.getDifference(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong_long_long() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.years());
        field.getDifferenceAsLong(1000L, 2000L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.years());
        assertEquals(0L, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.years());
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.months());
        assertTrue(field1.compareTo(field2) > 0);
        assertTrue(field2.compareTo(field1) < 0);
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.years());
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.years());
        UnsupportedDurationField field3 = new UnsupportedDurationField(DurationFieldType.months());
        assertEquals(field1, field2);
        assertFalse(field1.equals(field3));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.years());
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.years());
        UnsupportedDurationField field3 = new UnsupportedDurationField(DurationFieldType.months());
        assertEquals(field1.hashCode(), field2.hashCode());
        assertFalse(field1.hashCode() == field3.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.years());
        assertEquals("UnsupportedDurationField[years]", field.toString());
    }

    private UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException(DurationFieldType.years() + " field is unsupported");
    }

}
package org.joda.time.field;

import org.joda.time.DurationFieldType;
import org.junit.Assert;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        Assert.assertEquals(DurationFieldType.YEARS, field.getType());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        Assert.assertEquals("years", field.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        Assert.assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        Assert.assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_Long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        field.getValue(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_Long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        field.getValueAsLong(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_Long_Long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        field.getValue(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_Long_Long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        field.getValueAsLong(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_Int() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        field.getMillis(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_Long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        field.getMillis(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_Int_Long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        field.getMillis(1000, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_Long_Long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        field.getMillis(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_Long_Int() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        field.add(1000L, 5);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_Long_Long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        field.add(1000L, 500L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference_Long_Long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        field.getDifference(2000L, 1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong_Long_Long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        field.getDifferenceAsLong(2000L, 1000L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        Assert.assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.MONTHS);
        Assert.assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.MONTHS);
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        Assert.assertFalse(field1.equals(field2));
        Assert.assertFalse(field1.equals(null));
        Assert.assertTrue(field1.equals(field3));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.MONTHS);
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        Assert.assertNotEquals(field1.hashCode(), field2.hashCode());
        Assert.assertEquals(field1.hashCode(), field3.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        Assert.assertEquals("UnsupportedDurationField[years]", field.toString());
    }
}
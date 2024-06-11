package org.joda.time.field;

import org.joda.time.DurationFieldType;
import org.joda.time.field.UnsupportedDurationField;
import org.junit.Assert;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        Assert.assertNotNull(field);
        Assert.assertEquals(DurationFieldType.SECONDS, field.getType());
    }

    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        Assert.assertEquals(DurationFieldType.SECONDS, field.getType());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        Assert.assertEquals("seconds", field.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        Assert.assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        Assert.assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.getValue(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.getValueAsLong(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.getValue(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.getValueAsLong(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.getMillis(10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.getMillis(10L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.getMillis(10, 1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.getMillis(10L, 1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_int() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.add(1000L, 10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.add(1000L, 10L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.getDifference(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.getDifferenceAsLong(1000L, 2000L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        Assert.assertEquals(0L, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        UnsupportedDurationField supportedField = new UnsupportedDurationField(DurationFieldType.SECOND_OF_MINUTE);
        Assert.assertEquals(0, field.compareTo(other));
        Assert.assertEquals(1, field.compareTo(supportedField));
        Assert.assertEquals(-1, supportedField.compareTo(field));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        UnsupportedDurationField supportedField = new UnsupportedDurationField(DurationFieldType.SECOND_OF_MINUTE);
        Assert.assertTrue(field.equals(field));
        Assert.assertTrue(field.equals(other));
        Assert.assertFalse(field.equals(supportedField));
        Assert.assertFalse(field.equals(null));
        Assert.assertFalse(field.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        UnsupportedDurationField supportedField = new UnsupportedDurationField(DurationFieldType.SECOND_OF_MINUTE);
        Assert.assertEquals(field.hashCode(), field.hashCode());
        Assert.assertEquals(field.hashCode(), other.hashCode());
        Assert.assertNotEquals(field.hashCode(), supportedField.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        Assert.assertEquals("UnsupportedDurationField[seconds]", field.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        UnsupportedDurationField resolvedField = (UnsupportedDurationField) field.readResolve();
        Assert.assertSame(field, resolvedField);
    }

}
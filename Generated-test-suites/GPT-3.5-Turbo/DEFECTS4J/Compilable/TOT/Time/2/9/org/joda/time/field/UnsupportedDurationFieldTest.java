package org.joda.time.field;
import org.joda.time.DurationFieldType;
import org.junit.Assert;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        Assert.assertEquals(DurationFieldType.years(), field.getType());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        Assert.assertEquals("months", field.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        Assert.assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        Assert.assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        field.getValue(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getValueAsLong(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_withInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        field.getValue(1000, 12345);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_withInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.getValueAsLong(1000, 12345);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_withValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getMillis(10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_withValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        field.getMillis(100L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_withValue_andInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        field.getMillis(10, 123456);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_withValueAsLong_andInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        field.getMillis(100L, 123456);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_withValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        field.add(123456, 10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_withValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.add(123456L, 100L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getDifference(123456, 78910);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        field.getDifferenceAsLong(123456L, 78910L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        Assert.assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        Assert.assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        Assert.assertEquals(field1, field2);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        Assert.assertEquals("hours".hashCode(), field.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        Assert.assertEquals("UnsupportedDurationField[millis]", field.toString());
    }

}
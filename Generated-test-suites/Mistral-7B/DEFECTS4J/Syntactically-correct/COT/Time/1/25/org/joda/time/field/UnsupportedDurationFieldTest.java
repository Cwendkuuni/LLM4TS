package org.joda.time.field;

import org.joda.time.DurationFieldType;
import org.junit.Assert;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        Assert.assertNotNull(unsupportedField);
        Assert.assertEquals(type, unsupportedField.getType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long duration = 1234567890L;
        unsupportedField.getValue(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long duration = 1234567890L;
        unsupportedField.getValueAsLong(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long_long() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long duration = 1234567890L;
        long instant = 1234567890L;
        unsupportedField.getValue(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long_long() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long duration = 1234567890L;
        long instant = 1234567890L;
        unsupportedField.getValueAsLong(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        int value = 123;
        unsupportedField.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long value = 1234567890L;
        unsupportedField.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int_long() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        int value = 123;
        long instant = 1234567890L;
        unsupportedField.getMillis(value, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long_long() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long value = 1234567890L;
        long instant = 1234567890L;
        unsupportedField.getMillis(value, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_int() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long instant = 1234567890L;
        int value = 123;
        unsupportedField.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_long() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long instant = 1234567890L;
        long value = 1234567890L;
        unsupportedField.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference_long_long() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 1234567890L;
        long subtrahendInstant = 1234567890L;
        unsupportedField.getDifference(minuendInstant, subtrahendInstant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong_long_long() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 1234567890L;
        long subtrahendInstant = 1234567890L;
        unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long unitMillis = unsupportedField.getUnitMillis();
        Assert.assertEquals(0, unitMillis);
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.DAYS;
        DurationFieldType type2 = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type2);
        Assert.assertThat(unsupportedField1.compareTo(unsupportedField2), is(1));
    }

    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type);
        Assert.assertTrue(unsupportedField1.equals(unsupportedField2));
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        String name = unsupportedField.getName();
        Assert.assertEquals(name.hashCode(), unsupportedField.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        String expected = "UnsupportedDurationField[DAYS]";
        String result = unsupportedField.toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReadResolve() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(type);
        Object resolved = unsupportedField.readResolve();
        Assert.assertInstanceOf(UnsupportedDurationField.class, resolved);
    }

    

}
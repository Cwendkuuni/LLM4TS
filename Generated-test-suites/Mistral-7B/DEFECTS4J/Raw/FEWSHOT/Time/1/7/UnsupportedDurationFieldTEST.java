package org.joda.time.field;

import static org.junit.Assert.assertSame;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.create("test", 1);
        UnsupportedDurationField instance1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField instance2 = UnsupportedDurationField.getInstance(type);
        assertSame(instance1, instance2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        field.getValue(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        field.getValueAsLong(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        field.getValue(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueLongLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        field.getValue(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        field.getMillis(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLongLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        field.getMillis(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        field.add(1L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        field.add(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        field.getDifference(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        field.getDifferenceAsLong(1L, 1L);
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.HOURLY_INSTANTS);
        int result = field1.compareTo(field2);
        assert (result > 0);
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        boolean result = field1.equals(field2);
        assert (result == true);

        Object obj = new Object();
        result = field1.equals(obj);
        assert (result == false);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        int hashCode1 = field1.hashCode();
        int hashCode2 = field2.hashCode();
        assert (hashCode1 == hashCode2);

        String name = "test";
        DurationFieldType type = DurationFieldType.create(name, 1);
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        int hashCode = field.hashCode();
        assert (hashCode == name.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        String result = field.toString();
        assert (result.equals("UnsupportedDurationField[DAYS]"));
    }

}
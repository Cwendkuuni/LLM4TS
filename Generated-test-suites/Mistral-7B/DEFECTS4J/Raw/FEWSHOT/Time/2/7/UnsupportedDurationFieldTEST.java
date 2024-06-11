package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.joda.time.DurationFieldType;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testCache() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertNotNull(field1);
        assertNotNull(field2);
        assertEquals(field1, field2);
    }

    @Test
    public void testName() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("DAYS", field.getName());
    }

    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetValue() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long duration = 123456789;
        field.getValue(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetValueAsLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long duration = 123456789;
        field.getValueAsLong(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetValueAsLong_instant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long duration = 123456789;
        long instant = 1234567890L;
        field.getValueAsLong(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetMillis() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        int value = 123;
        field.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetMillis_long() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long value = 123456789;
        field.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetMillis_int_long() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        int value = 123;
        long instant = 1234567890L;
        field.getMillis(value, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetMillis_long_long() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long value = 123456789;
        long instant = 1234567890L;
        field.getMillis(value, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testadd() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long instant = 1234567890L;
        int value = 123;
        field.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testadd_long() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long instant = 1234567890L;
        long value = 123456789;
        field.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetDifference() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 1234567890L;
        long subtrahendInstant = 1234567889L;
        field.getDifference(minuendInstant, subtrahendInstant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 1234567890L;
        long subtrahendInstant = 1234567889L;
        field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @Test
    public void testgetUnitMillis() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.DAYS;
        DurationFieldType type2 = DurationFieldType.HOURS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type.hashCode(), field.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[DAYS]", field.toString());
    }

    @Test
    public void testReadResolve() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        field = (UnsupportedDurationField) field.readResolve();
        UnsupportedDurationField fieldFromCache = UnsupportedDurationField.getInstance(type);
        assertEquals(fieldFromCache, field);
    }

    @Test
    public void testUnsupportedOperationException() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> field.getValue(1));
    }
}
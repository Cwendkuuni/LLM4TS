package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link UnsupportedDurationField}.
 */
public class UnsupportedDurationFieldTest {

    

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertNotNull(unsupported);
        assertEquals(type, unsupported.getType());
        assertEquals(type.getName(), unsupported.getName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 1L;
        unsupported.getValue(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 1L;
        unsupported.getValueAsLong(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAtInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 1L;
        long instant = 1L;
        unsupported.getValue(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongAtInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 1L;
        long instant = 1L;
        unsupported.getValueAsLong(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        int value = 1;
        unsupported.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAtLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long value = 1L;
        unsupported.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAtIntLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        int value = 1;
        long instant = 1L;
        unsupported.getMillis(value, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAtLongLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long value = 1L;
        long instant = 1L;
        unsupported.getMillis(value, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long instant = 1L;
        long value = 1L;
        unsupported.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long instant = 1L;
        long value = 1L;
        unsupported.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 1L;
        long subtrahendInstant = 1L;
        unsupported.getDifference(minuendInstant, subtrahendInstant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 1L;
        long subtrahendInstant = 1L;
        unsupported.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals(0, unsupported.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.DAYS;
        DurationFieldType type2 = DurationFieldType.HOURS;
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(0, unsupported1.compareTo(unsupported2));
    }

    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported3 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertTrue(unsupported1.equals(unsupported1));
        assertTrue(unsupported1.equals(unsupported2));
        assertFalse(unsupported1.equals(unsupported3));
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals(type.hashCode(), unsupported.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        String expected = "UnsupportedDurationField[DAYS]";
        assertEquals(expected, unsupported.toString());
    }

    @Test
    public void testReadResolve() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = new UnsupportedDurationField(type);
        assertEquals(type, unsupported.getType());
    }

    private UnsupportedOperationException unsupported() {
        return new UnsupportedDurationField(DurationFieldType.DAYS).unsupported();
    }

    

}
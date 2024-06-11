package org.joda.time.field;
import org.joda.time.DurationFieldType;
import org.joda.time.field.UnsupportedDurationField;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    private UnsupportedDurationField field;

    @Before
    public void setup() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    }

    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.years(), field.getType());
    }

    @Test
    public void testGetName() {
        assertEquals("years", field.getName());
    }

    @Test
    public void testIsSupported() {
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        field.getValue(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        field.getValueAsLong(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        field.getValue(1000, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        field.getValueAsLong(1000, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        field.getMillis(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueLong() {
        field.getMillis(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstant() {
        field.getMillis(1000, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstantLong() {
        field.getMillis(1000L, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithValue() {
        field.add(0, 1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithValueLong() {
        field.add(0, 1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        field.getDifference(0, 1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        field.getDifferenceAsLong(0, 1000);
    }

    @Test
    public void testGetUnitMillis() {
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(DurationFieldType.months());

        assertEquals(1, field.compareTo(other));
        assertEquals(0, field.compareTo(field));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField different = UnsupportedDurationField.getInstance(DurationFieldType.months());

        assertTrue(field.equals(other));
        assertTrue(other.equals(field));
        assertFalse(field.equals(null));
        assertFalse(field.equals("some string"));
        assertFalse(field.equals(different));
    }

    @Test
    public void testHashCode() {
        assertEquals(field.getName().hashCode(), field.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[years]", field.toString());
    }
}
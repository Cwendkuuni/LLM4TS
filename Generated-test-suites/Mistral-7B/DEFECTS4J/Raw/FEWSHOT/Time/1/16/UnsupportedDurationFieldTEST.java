package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupported, unsupported2);
    }

    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals("minutes", unsupported.getName());
    }

    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertFalse(unsupported.isSupported());
    }

    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertTrue(unsupported.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getValue(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getValueAsLong(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAt() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getValue(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongAt() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getValueAsLong(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getMillis(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAt() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getMillis(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.add(0, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAt() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.add(0, 1, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getDifference(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getDifferenceAsLong(0, 0);
    }

    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals(0, unsupported.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.MINUTES;
        DurationFieldType type2 = DurationFieldType.SECONDS;
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(1, unsupported1.compareTo(unsupported2));
    }

    @Test
    public void testEquals() {
        DurationFieldType type1 = DurationFieldType.MINUTES;
        DurationFieldType type2 = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);
        UnsupportedDurationField unsupported3 = UnsupportedDurationField.getInstance(type1);
        assertTrue(unsupported1.equals(unsupported1));
        assertTrue(unsupported1.equals(unsupported3));
        assertTrue(unsupported1.equals(unsupported2));
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        String name = unsupported.getName();
        int expectedHashCode = name.hashCode();
        int actualHashCode = unsupported.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        String expectedString = "UnsupportedDurationField[minutes]";
        String actualString = unsupported.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testReadResolve() throws ObjectStreamException {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupported = new UnsupportedDurationField(type);
        Object output = unsupported.readResolve();
        UnsupportedDurationField unsupported2 = (UnsupportedDurationField) output;
        assertEquals(unsupported.getName(), unsupported2.getName());
    }

}
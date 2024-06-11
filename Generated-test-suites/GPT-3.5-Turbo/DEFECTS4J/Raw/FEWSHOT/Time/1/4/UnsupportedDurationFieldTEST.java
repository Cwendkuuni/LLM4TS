package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND);
        assertEquals(DurationFieldType.SECOND, field.getType());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND);
        assertEquals("second", field.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND);
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND);
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND);
        field.getValue(1000L); // Expecting UnsupportedOperationException
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND);
        field.getValueAsLong(1000L); // Expecting UnsupportedOperationException
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND);
        field.getValue(1000L, 2000L); // Expecting UnsupportedOperationException
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND);
        field.getValueAsLong(1000L, 2000L); // Expecting UnsupportedOperationException
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND);
        field.getMillis(1); // Expecting UnsupportedOperationException
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND);
        field.getMillis(1000L); // Expecting UnsupportedOperationException
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND);
        field.getMillis(1, 2000L); // Expecting UnsupportedOperationException
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND);
        field.getMillis(1000L, 2000L); // Expecting UnsupportedOperationException
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_int() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND);
        field.add(1000L, 1); // Expecting UnsupportedOperationException
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND);
        field.add(1000L, 1000L); // Expecting UnsupportedOperationException
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND);
        field.getDifference(1000L, 2000L); // Expecting UnsupportedOperationException
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND);
        field.getDifferenceAsLong(1000L, 2000L); // Expecting UnsupportedOperationException
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND);
        assertEquals(0L, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.SECOND);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE);
        assertEquals(0, field1.compareTo(field1));
        assertEquals(1, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.SECOND);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.SECOND);

        assertTrue(field1.equals(field1));
        assertTrue(field1.equals(field2));
        assertFalse(field1.equals(null));
        assertFalse(field1.equals(""));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND);
        assertEquals("second".hashCode(), field.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND);
        assertEquals("UnsupportedDurationField[second]", field.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND);
        assertEquals(field, field.readResolve());
    }

    @Test
    public void testUnsupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND);
        try {
            field.unsupported(); // Expecting UnsupportedOperationException
            fail();
        } catch (UnsupportedOperationException e) {
            assertEquals("second field is unsupported", e.getMessage());
        }
    }

}
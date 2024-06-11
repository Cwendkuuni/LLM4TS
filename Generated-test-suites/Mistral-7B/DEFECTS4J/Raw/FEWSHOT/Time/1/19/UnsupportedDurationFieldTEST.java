package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);

        assertEquals(field1, field2);
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertFalse(field.isSupported());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals("DAYS", field.getName());
    }

    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(DurationFieldType.DAYS, field.getType());
    }

    @Test
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        try {
            field.getValue(1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }
    }

    @Test
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        try {
            field.getValueAsLong(1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }
    }

    @Test
    public void testGetValueLongInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        try {
            field.getValueAsLong(1L, 0L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }
    }

    @Test
    public void testGetMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        try {
            field.getMillis(1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }
    }

    @Test
    public void testGetMillisLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        try {
            field.getMillis(1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }
    }

    @Test
    public void testGetMillisLongInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        try {
            field.getMillis(1L, 0L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }
    }

    @Test
    public void testAdd() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        try {
            field.add(0L, 1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }
    }

    @Test
    public void testAddLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        try {
            field.add(0L, 1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }
    }

    @Test
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        try {
            field.getDifference(0L, 0L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }
    }

    @Test
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        try {
            field.getDifferenceAsLong(0L, 0L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertTrue(field1.compareTo(field2) < 0);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(DurationFieldType.DAYS.hashCode(), field.hashCode());
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(field1, field2);
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals("UnsupportedDurationField[DAYS]", field.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertNotSame(field, field.readResolve());
    }

}
package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    private final DurationFieldType unsupportedType = DurationFieldType.centuries();
    private final UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(unsupportedType);

    @Test
    public void testGetType() {
        assertEquals(unsupportedType, unsupportedField.getType());
    }

    @Test
    public void testGetName() {
        assertEquals(unsupportedType.getName(), unsupportedField.getName());
    }

    @Test
    public void testIsSupported() {
        assertFalse(unsupportedField.isSupported());
    }

    @Test
    public void testIsPrecise() {
        assertTrue(unsupportedField.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long() {
        unsupportedField.getValue(123);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long() {
        unsupportedField.getValueAsLong(123L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long_long() {
        unsupportedField.getValue(123, 456L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long_long() {
        unsupportedField.getValueAsLong(123L, 456L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int() {
        unsupportedField.getMillis(123);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long() {
        unsupportedField.getMillis(123L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int_long() {
        unsupportedField.getMillis(123, 456L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long_long() {
        unsupportedField.getMillis(123L, 456L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_int() {
        unsupportedField.add(123L, 456);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_long() {
        unsupportedField.add(123L, 456L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference_long_long() {
        unsupportedField.getDifference(123L, 456L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong_long_long() {
        unsupportedField.getDifferenceAsLong(123L, 456L);
    }

    @Test
    public void testGetUnitMillis() {
        assertEquals(0, unsupportedField.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(
                DurationFieldType.eras());
        assertEquals(1, unsupportedField.compareTo(unsupportedField2));
        assertEquals(0, unsupportedField.compareTo(unsupportedField));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(
                DurationFieldType.centuries());
        assertEquals(unsupportedField, unsupportedField);
        assertEquals(unsupportedField, unsupportedField2);
        assertEquals(unsupportedField2, unsupportedField);
        assertEquals(unsupportedField.hashCode(), unsupportedField2.hashCode());
        
        DurationFieldType nullNameType = new DurationFieldType("name") {
            public DurationField getField(Chronology chronology) {
                return null;
            }
        };
        
        UnsupportedDurationField unsupportedField3 = UnsupportedDurationField.getInstance(nullNameType);
        assertFalse(unsupportedField.equals(unsupportedField3));
    }

    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[Centuries]", unsupportedField.toString());
    }

    @Test
    public void testReadResolve() {
        assertSame(unsupportedField, unsupportedField.readResolve());
    }

    @Test
    public void testUnsupported() {
        try {
            unsupportedField.unsupported();
            fail();
        } catch (UnsupportedOperationException ex) {
            assertEquals("Centuries field is unsupported", ex.getMessage());
        }
    }
}
package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import org.junit.Test;

/**
 * Test class for {@link UnsupportedDurationField}
 */
public class UnsupportedDurationFieldTest {

    /**
     * Test method for {@link UnsupportedDurationField#getInstance(DurationFieldType)}.
     */
    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type, field.getType());
        assertEquals("hours", field.getName());
        assertFalse(field.isSupported());
        assertTrue(field.isPrecise());
        assertNull(field.getValue(Long.MAX_VALUE));
        assertNull(field.getValueAsLong(Long.MAX_VALUE));
        assertNull(field.getValue(Long.MAX_VALUE, Long.MAX_VALUE));
        assertNull(field.getValueAsLong(Long.MAX_VALUE, Long.MAX_VALUE));
        assertEquals(0, field.getMillis(0));
        assertEquals(0, field.getMillis(1));
        assertEquals(0, field.getMillis(0, Long.MAX_VALUE));
        assertEquals(0, field.getMillis(1, Long.MAX_VALUE));
        assertThrows(UnsupportedOperationException.class, () -> field.add(Long.MAX_VALUE, 1));
        assertThrows(UnsupportedOperationException.class, () -> field.add(Long.MAX_VALUE, Long.MAX_VALUE));
        assertThrows(UnsupportedOperationException.class, () -> field.getDifference(Long.MAX_VALUE, Long.MAX_VALUE));
        assertThrows(UnsupportedOperationException.class, () -> field.getDifferenceAsLong(Long.MAX_VALUE, Long.MAX_VALUE));
        assertEquals(0, field.getUnitMillis());
        assertFalse(field.compareTo(null));
        assertFalse(field.compareTo(new DurationFieldType(null)));
        assertFalse(field.compareTo(FieldUtils.aSecond()));
        assertSame(field, field.readResolve());
    }

    /**
     * Test method for {@link UnsupportedDurationField#toString()}.
     */
    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[hours]", field.toString());
    }

}
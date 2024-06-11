package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

import org.junit.Test;

/**
 * Test class for UnsupportedDurationField.
 */
public class UnsupportedDurationFieldTest {

    /**
     * Test method for {@link UnsupportedDurationField#getInstance(DurationFieldType)}.
     */
    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField another = UnsupportedDurationField.getInstance(type);
        assertEquals(type, unsupported.getType());
        assertEquals(type, another.getType());
        assertSame(unsupported, another);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getInstance(DurationFieldType)}.
     * Tests the caching mechanism.
     */
    @Test
    public void testGetInstance_cache() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type);
        assertEquals(type, unsupported1.getType());
        assertEquals(type, unsupported2.getType());
        assertSame(unsupported1, unsupported2);

        UnsupportedDurationField unsupported3 = UnsupportedDurationField.getInstance(type);
        assertSame(unsupported1, unsupported3);
    }

    /**
     * Test method for {@link UnsupportedDurationField#isSupported()}.
     */
    @Test
    public void testIsSupported() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertFalse(unsupported.isSupported());
    }

    /**
     * Test method for {@link UnsupportedDurationField#getValue(long)}.
     */
    @Test
    public void testgetValue() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValue(1));
    }

    /**
     * Test method for {@link UnsupportedDurationField#getValueAsLong(long)}.
     */
    @Test
    public void testgetValueAsLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValueAsLong(1));
    }

    /**
     * Test method for {@link UnsupportedDurationField#getValue(long, long)}.
     */
    @Test
    public void testgetValueWithInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValue(1, 1));
    }

    /**
     * Test method for {@link UnsupportedDurationField#getValueAsLong(long, long)}.
     */
    @Test
    public void testgetValueAsLongWithInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValueAsLong(1, 1));
    }

    /**
     * Test method for {@link UnsupportedDurationField#getMillis(int)}.
     */
    @Test
    public void testgetMillis() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(1));
    }

    /**
     * Test method for {@link UnsupportedDurationField#getMillis(long)}.
     */
    @Test
    public void testgetMillisLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(1));
    }

    /**
     * Test method for {@link UnsupportedDurationField#getMillis(int, long)}.
     */
    @Test
    public void testgetMillisWithInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(1, 1));
    }

    /**
     * Test method for {@link UnsupportedDurationField#getMillis(long, long)}.
     */
    @Test
    public void testgetMillisLongWithInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(1, 1));
    }

    /**
     * Test method for {@link UnsupportedDurationField#add(long, int)}.
     */
    @Test
    public void testadd() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertThrows(UnsupportedOperationException.class, () -> unsupported.add(1, 1));
    }

    /**
     * Test method for {@link UnsupportedDurationField#add(long, long)}.
     */
    @Test
    public void testaddLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertThrows(UnsupportedOperationException.class, () -> unsupported.add(1, 1));
    }

    /**
     * Test method for {@link UnsupportedDurationField#getDifference(long, long)}.
     */
    @Test
    public void testgetDifference() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getDifference(1, 1));
    }

    /**
     * Test method for {@link UnsupportedDurationField#getDifferenceAsLong(long, long)}.
     */
    @Test
    public void testgetDifferenceAsLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getDifferenceAsLong(1, 1));
    }

    /**
     * Test method for {@link UnsupportedDurationField#getUnitMillis()}.
     */
    @Test
    public void testgetUnitMillis() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertEquals(0, unsupported.getUnitMillis());
    }

    /**
     * Test method for {@link UnsupportedDurationField#compareTo(DurationField)}.
     */
    @Test
    public void testcompareTo() {
        UnsupportedDurationField unsupported1 = new UnsupportedDurationField(DurationFieldType.HOURS());
        UnsupportedDurationField unsupported2 = new UnsupportedDurationField(DurationFieldType.MINUTES());
        assertEquals(0, unsupported1.compareTo(unsupported2));
    }

    /**
     * Test method for {@link UnsupportedDurationField#getName()}.
     */
    @Test
    public void testgetName() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertEquals("hours", unsupported.getName());
    }

    /**
     * Test method for {@link UnsupportedDurationField#isPrecise()}.
     */
    @Test
    public void testIsPrecise() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertTrue(unsupported.isPrecise());
    }

    /**
     * Test method for {@link UnsupportedDurationField#toString()}.
     */
    @Test
    public void testToString() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertEquals("UnsupportedDurationField[hours]", unsupported.toString());
    }

    /**
     * Test method for {@link UnsupportedDurationField#equals(Object)}.
     */
    @Test
    public void testEquals() {
        UnsupportedDurationField unsupported1 = new UnsupportedDurationField(DurationFieldType.HOURS());
        UnsupportedDurationField unsupported2 = new UnsupportedDurationField(DurationFieldType.HOURS());
        UnsupportedDurationField unsupported3 = new UnsupportedDurationField(DurationFieldType.MINUTES());
        assertNull(unsupported1.equals(null));
        assertTrue(unsupported1.equals(unsupported1));
        assertTrue(unsupported1.equals(unsupported2));
        assertFalse(unsupported1.equals(unsupported3));
    }

    /**
     * Test method for {@link UnsupportedDurationField#hashCode()}.
     */
    @Test
    public void testHashCode() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertEquals(220660631, unsupported.hashCode());
    }

    /**
     * Test method for {@link UnsupportedDurationField#readResolve()}.
     */
    @Test
    public void testReadResolve() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertSame(UnsupportedDurationField.getInstance(DurationFieldType.HOURS()), unsupported.readResolve());
    }

}
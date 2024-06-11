package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.joda.time.DurationFieldType;
import org.junit.Before;
import org.junit.Test;

public class UnsupportedDurationFieldTest {
    private UnsupportedDurationField field;

    @Before
    public void setUp() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    }

    @Test
    public void testIsSupported() {
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        assertTrue(field.isPrecise());
    }

    @Test
    public void testGetValue() {
        try {
            field.getValue(0);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetValueAsLong() {
        try {
            field.getValueAsLong(0);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetValueWithInstant() {
        try {
            field.getValue(0, 0);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetValueAsLongWithInstant() {
        try {
            field.getValueAsLong(0, 0);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetMillisWithValue() {
        try {
            field.getMillis(0);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetMillisWithValue() {
        try {
            field.getMillis(0);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testGetName() {
        assertEquals("days", field.getName());
    }

    @Test
    public void testGetUnitMillis() {
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField anotherField = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertEquals(1, field.compareTo(anotherField));
    }
}
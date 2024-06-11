package org.joda.time.field;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testUnsupportedDurationField() {
        // Test getInstance
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        assertEquals("Type should be minutes", DurationFieldType.minutes(), field.getType());
    }

    @Test
    public void testGetName() {
        // Test getName
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertEquals("Name should be seconds", "seconds", field.getName());
    }

    @Test
    public void testIsSupported() {
        // Test isSupported
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertFalse("Field should not be supported", field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        // Test isPrecise
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertTrue("Field should be precise", field.isPrecise());
    }

    @Test
    public void testUnsupportedMethods() {
        // Test unsupported methods
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        try {
            field.getValue(100);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException ignored) {
        }
    }

    @Test
    public void testGetUnitMillis() {
        // Test getUnitMillis
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertEquals("Unit millis should be 0", 0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        // Test compareTo
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        assertEquals("Fields should have equal sort order", 0, field1.compareTo(field2));
    }

}
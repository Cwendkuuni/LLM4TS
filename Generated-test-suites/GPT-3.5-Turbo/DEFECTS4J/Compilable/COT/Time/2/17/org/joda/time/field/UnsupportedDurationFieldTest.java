package org.joda.time.field;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import org.joda.time.field.UnsupportedDurationField;

import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertNotNull(field);
        assertEquals(type, field.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInstanceWithNullDurationFieldType() {
        DurationFieldType type = null;
        UnsupportedDurationField.getInstance(type);
    }

    @Test
    public void testGetInstanceWithUnknownDurationFieldType() {
        DurationFieldType type = DurationFieldType.weeks();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertNotNull(field);
        assertEquals(type, field.getType());
    }
}
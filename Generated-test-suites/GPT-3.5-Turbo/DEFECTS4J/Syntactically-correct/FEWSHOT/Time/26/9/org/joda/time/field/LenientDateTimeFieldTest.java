package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.chrono.LenientChronology;
import org.joda.time.field.DelegatedDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Test;

public class LenientDateTimeFieldTest {
    
    @Test
    public void testGetInstance_withNullField() {
        assertNull(LenientDateTimeField.getInstance(null, null));
    }
    
    @Test
    public void testGetInstance_withStrictField() {
        DateTimeField field = new StrictDateTimeField(null, null);
        assertEquals(field, LenientDateTimeField.getInstance(field, null));
    }
    
    @Test
    public void testGetInstance_withLenientField() {
        DateTimeField field = new LenientDateTimeField(null, null);
        assertEquals(field, LenientDateTimeField.getInstance(field, null));
    }
    
    @Test
    public void testGetInstance_withNonLenientField() {
        DateTimeField field = new DelegatedDateTimeField(null, null);
        DateTimeField result = LenientDateTimeField.getInstance(field, null);
        assertEquals(LenientDateTimeField.class, result.getClass());
        assertEquals(field, ((LenientDateTimeField) result).getWrappedField());
    }
    
    @Test
    public void testIsLenient() {
        DateTimeField field = new LenientDateTimeField(null, null);
        assertEquals(true, field.isLenient());
    }
    
    @Test
    public void testSet() {
        Chronology base = LenientChronology.getInstanceUTC();
        DateTimeField field = new LenientDateTimeField(null, base);
        long instant = 123456789L;
        int value = 42;
        assertEquals(instant + value, field.set(instant, value));
    }

}
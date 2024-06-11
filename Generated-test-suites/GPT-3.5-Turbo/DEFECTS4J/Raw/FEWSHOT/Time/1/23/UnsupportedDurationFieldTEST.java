package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.junit.Test;

public class UnsupportedDurationFieldTest {
    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        assertEquals(type, field.getType());
    }
    
    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        assertEquals(type.getName(), field.getName());
    }
    
    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        assertFalse(field.isSupported());
    }
    
    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        assertTrue(field.isPrecise());
    }
    
    @Test
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        try {
            field.getValue(1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // Expected
        }
    }
    
    // Continue testing for all other methods in the class...
}
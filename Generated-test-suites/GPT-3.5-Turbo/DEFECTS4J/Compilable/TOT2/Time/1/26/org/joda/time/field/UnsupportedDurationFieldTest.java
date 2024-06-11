package org.joda.time.field;
import org.joda.time.DurationFieldType;
import org.joda.time.field.UnsupportedDurationField;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {
    
    private UnsupportedDurationField unsupportedDurationField;
    
    @Before
    public void setup() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.days());
    }
    
    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.days(), unsupportedDurationField.getType());
    }
    
    @Test
    public void testGetName() {
        assertEquals("days", unsupportedDurationField.getName());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        unsupportedDurationField.getValue(10);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        unsupportedDurationField.getValueAsLong(10L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        unsupportedDurationField.getValue(10, 100);
    }
    
    // other test cases for remaining methods can be added here
    
}
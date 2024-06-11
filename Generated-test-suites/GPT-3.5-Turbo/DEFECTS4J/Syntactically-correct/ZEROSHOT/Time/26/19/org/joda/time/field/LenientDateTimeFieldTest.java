package org.joda.time.field;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {
    private LenientDateTimeField lenientField;
    private MockDateTimeField baseField;
    
    @Before
    public void setUp() {
        baseField = new MockDateTimeField();
        lenientField = new LenientDateTimeField(baseField, null);
    }
    
    @Test
    public void testIsLenient() {
        // The lenientField should always be lenient
        assertTrue(lenientField.isLenient());
    }
    
    @Test
    public void testSet() {
        // Test set method for a positive value
        long instant = 1000L;
        int value = 10;
        long expected = 1010L;
        assertEquals(expected, lenientField.set(instant, value));
        
        // Test set method for a negative value
        instant = 2000L;
        value = -20;
        expected = 1980L;
        assertEquals(expected, lenientField.set(instant, value));
    }
}
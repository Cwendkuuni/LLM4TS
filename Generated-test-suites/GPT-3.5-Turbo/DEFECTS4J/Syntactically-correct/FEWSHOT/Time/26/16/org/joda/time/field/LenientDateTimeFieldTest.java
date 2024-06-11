package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.ReadablePartial;
import org.junit.Test;

public class LenientDateTimeFieldTest {
  
    private final Chronology base = null;
    private final DateTimeField field = null;
    
    @Test
    public void testGetInstance() {
        assertNotNull(LenientDateTimeField.getInstance(field, base));
    }
    
    @Test
    public void testIsLenient() {
        assertTrue(LenientDateTimeField.getInstance(field, base).isLenient());
    }
    
    @Test
    public void testSet() {
        ReadablePartial partial = null;
        long instant = 0;
        int value = 0;
        
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        long result = lenientField.set(instant, value);
        assertEquals(result, 0);
        
        // Test with non-null base and field
        assertNotNull(LenientDateTimeField.getInstance(DateTimeFieldType.year(), base));
        assertNotNull(LenientDateTimeField.getInstance(DateTimeFieldType.year(), base).isLenient());
        assertNotNull(LenientDateTimeField.getInstance(DateTimeFieldType.year(), base).set(0, 0));
        assertNotNull(LenientDateTimeField.getInstance(DateTimeFieldType.year(), base).setExtended(partial, 0, null));
        
    }
}
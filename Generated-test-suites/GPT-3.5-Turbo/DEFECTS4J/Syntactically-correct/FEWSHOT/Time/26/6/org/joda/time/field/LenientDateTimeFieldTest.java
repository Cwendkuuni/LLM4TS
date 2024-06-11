package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.chrono.ISOChronology;
import org.junit.BeforeClass;
import org.junit.Test;

public class LenientDateTimeFieldTest {
    
    private static DateTimeField field;
    private static Chronology base;
    private static LenientDateTimeField lenientField;
    
    @BeforeClass
    public static void setup() {
        field = ISOChronology.getInstance().hourOfDay();
        base = ISOChronology.getInstance();
        lenientField = (LenientDateTimeField) LenientDateTimeField.getInstance(field, base);
    }
    
    @Test
    public void testGetInstance_NullField() {
        assertNull(LenientDateTimeField.getInstance(null, base));
    }
    
    @Test
    public void testGetInstance_StrictField() {
        DateTimeField strictField = StrictDateTimeField.getInstance(field, base);
        assertNotNull(strictField);
        
        DateTimeField result = LenientDateTimeField.getInstance(strictField, base);
        assertNotEquals(strictField, result);
        assertTrue(result instanceof LenientDateTimeField);
        assertEquals(field, result.getWrappedField());
        assertTrue(result.isLenient());
    }
    
    @Test
    public void testGetInstance_LenientField() {
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        assertNotNull(lenientField);
        
        DateTimeField result = LenientDateTimeField.getInstance(lenientField, base);
        assertEquals(lenientField, result);
    }
    
    @Test
    public void testIsLenient() {
        assertTrue(lenientField.isLenient());
    }
    
    @Test
    public void testSet() {
        long instant = DateTimeUtils.currentTimeMillis();
        
        int value1 = 10;
        long expected1 = field.set(instant, value1);
        long result1 = lenientField.set(instant, value1);
        assertEquals(expected1, result1);
        
        int value2 = 30;
        long expected2 = field.set(instant, value2);
        long result2 = lenientField.set(instant, value2);
        assertEquals(expected2, result2);
        
        int value3 = 60;
        long expected3 = field.set(instant, value3);
        long result3 = lenientField.set(instant, value3);
        assertEquals(expected3, result3);
    }

}
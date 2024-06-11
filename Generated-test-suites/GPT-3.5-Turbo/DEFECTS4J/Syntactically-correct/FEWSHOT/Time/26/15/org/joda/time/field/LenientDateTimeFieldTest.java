package org.joda.time.field;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.chrono.LenientChronology;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {
    
    private LenientDateTimeField lenientDateTimeField;
    private DateTimeField strictDateTimeField;
    private Chronology baseChronology;
    private LenientChronology lenientChronology;
    
    @Before
    public void setUp() {
        strictDateTimeField = new StrictDateTimeField();
        baseChronology = new BaseChronology();
        lenientChronology = new LenientChronology(baseChronology);
        lenientDateTimeField = new LenientDateTimeField(strictDateTimeField, baseChronology);
    }
    
    @Test
    public void testGetInstance() {
        DateTimeField field = null;
        assertNull(LenientDateTimeField.getInstance(field, baseChronology));
        
        field = strictDateTimeField;
        assertSame(field, LenientDateTimeField.getInstance(field, baseChronology));
        
        field = new LenientDateTimeField(strictDateTimeField, baseChronology);
        assertSame(field, LenientDateTimeField.getInstance(field, baseChronology));
        
        field = new StrictDateTimeField();
        assertSame(field.getWrappedField(), LenientDateTimeField.getInstance(field, baseChronology).getWrappedField());
    }
    
    @Test
    public void testIsLenient() {
        assertTrue(lenientDateTimeField.isLenient());
    }
    
    @Test
    public void testSet() {
        long instant = 0;
        int value = 1;
        long expected = 1;
        assertEquals(expected, lenientDateTimeField.set(instant, value));
        
        instant = 0;
        value = 0;
        expected = 0;
        assertEquals(expected, lenientDateTimeField.set(instant, value));
        
        instant = 0;
        value = -1;
        expected = -1;
        assertEquals(expected, lenientDateTimeField.set(instant, value));
    }
}
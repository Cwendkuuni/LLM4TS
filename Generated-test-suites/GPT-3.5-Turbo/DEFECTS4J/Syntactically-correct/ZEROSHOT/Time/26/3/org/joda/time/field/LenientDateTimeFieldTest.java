package org.joda.time.field;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.chrono.LenientChronology;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LenientDateTimeFieldTest {

    private LenientDateTimeField lenientDateTimeField;
    private DateTimeField dateTimeField;

    @Before
    public void setUp() {
        dateTimeField = mock(DateTimeField.class);
        Chronology base = new LenientChronology(null, null);
        lenientDateTimeField = new LenientDateTimeField(dateTimeField, base);
    }

    @Test
    public void testGetInstance() {
        DateTimeField field = mock(DateTimeField.class);
        Chronology base = new LenientChronology(null, null);
        DateTimeField result = LenientDateTimeField.getInstance(field, base);
        
        assertEquals(field, result);
    }
    
    @Test
    public void testGetInstanceWithNullField() {
        DateTimeField result = LenientDateTimeField.getInstance(null, null);
        
        assertEquals(null, result);
    }
    
    @Test
    public void testGetInstanceWithStrictDateTimeField() {
        StrictDateTimeField strictDateTimeField = mock(StrictDateTimeField.class);
        DateTimeField wrappedField = mock(DateTimeField.class);
        when(strictDateTimeField.getWrappedField()).thenReturn(wrappedField);
        
        DateTimeField result = LenientDateTimeField.getInstance(strictDateTimeField, null);
        
        assertEquals(wrappedField, result);
    }
    
    @Test
    public void testGetInstanceIsLenient() {
        boolean isLenient = lenientDateTimeField.isLenient();
        
        assertEquals(true, isLenient);
    }
    
    @Test
    public void testSet() {
        long instant = 1234567890L;
        int value = 10;
        long expected = 9876543210L;
        long difference = 9876543210L;
        
        when(dateTimeField.get(instant)).thenReturn(0);
        when(dateTimeField.add(instant, difference)).thenReturn(expected);
        
        long result = lenientDateTimeField.set(instant, value);
        
        assertEquals(expected, result);
    }

}
package org.joda.time.field;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.chrono.LenientChronology;
import org.joda.time.chrono.StrictChronology;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {
    
    private LenientDateTimeField lenientDateTimeField;
    private DateTimeField dateTimeField;
    private Chronology base;
    
    @Before
    public void setUp() {
        dateTimeField = mock(DateTimeField.class);
        base = mock(Chronology.class);
        lenientDateTimeField = new LenientDateTimeField(dateTimeField, base);
    }
    
    @Test
    public void testGetInstance_fieldIsNull_returnsNull() {
        assertNull(LenientDateTimeField.getInstance(null, base));
    }
    
    @Test
    public void testGetInstance_fieldIsInstanceOfStrictDateTimeField_returnsWrappedField() {
        StrictDateTimeField strictDateTimeField = mock(StrictDateTimeField.class);
        when(strictDateTimeField.getWrappedField()).thenReturn(dateTimeField);
        
        assertEquals(dateTimeField, LenientDateTimeField.getInstance(strictDateTimeField, base));
    }
    
    @Test
    public void testGetInstance_fieldIsLenient_returnsField() {
        when(dateTimeField.isLenient()).thenReturn(true);
        
        assertEquals(dateTimeField, LenientDateTimeField.getInstance(dateTimeField, base));
    }
    
    @Test
    public void testGetInstance_fieldIsNotLenient_returnsNewLenientDateTimeField() {
        when(dateTimeField.isLenient()).thenReturn(false);
        
        LenientDateTimeField result = LenientDateTimeField.getInstance(dateTimeField, base);
        
        assertNotNull(result);
        assertEquals(dateTimeField, result.getWrappedField());
        assertEquals(base, result.getBase());
    }
    
    @Test
    public void testConstructor_setsWrappedFieldAndBase() {
        assertEquals(dateTimeField, lenientDateTimeField.getWrappedField());
        assertEquals(base, lenientDateTimeField.getBase());
    }
    
    @Test
    public void testIsLenient_returnsTrue() {
        assertEquals(true, lenientDateTimeField.isLenient());
    }
    
    @Test
    public void testSet_convertsUTCToLocal_callsAddWithLocalInstant() {
        long instant = 123456789L;
        int value = 10;
        long expectedLocalInstant = 987654321L;
        long expectedUtcInstant = 357951246L;
        
        when(base.getZone()).thenReturn(mock(DateTimeZone.class));
        when(base.getZone().convertUTCToLocal(instant)).thenReturn(expectedLocalInstant);
        when(dateTimeField.get(anyLong())).thenReturn(value);
        when(dateTimeField.getType()).thenReturn(mock(DurationFieldType.class));
        when(dateTimeField.getType().getField(any(Chronology.class))).thenReturn(dateTimeField);
        when(base.withUTC()).thenReturn(mock(Chronology.class));
        when(dateTimeField.add(expectedLocalInstant, value - dateTimeField.get(instant))).thenReturn(expectedLocalInstant);
        when(base.getZone().convertLocalToUTC(expectedLocalInstant, false)).thenReturn(expectedUtcInstant);
        
        long result = lenientDateTimeField.set(instant, value);
        
        verify(base.getZone()).convertUTCToLocal(instant);
        verify(dateTimeField).get(instant);
        verify(dateTimeField.getType()).getField(any(Chronology.class));
        verify(base.withUTC()).get(anyLong());
        verify(dateTimeField).add(expectedLocalInstant, value - dateTimeField.get(instant));
        verify(base.getZone()).convertLocalToUTC(expectedLocalInstant, false);
        assertEquals(expectedUtcInstant, result);
    }
    
}
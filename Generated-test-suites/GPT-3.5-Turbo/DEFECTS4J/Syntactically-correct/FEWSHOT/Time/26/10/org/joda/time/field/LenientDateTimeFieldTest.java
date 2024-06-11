package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.LenientChronology;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LenientDateTimeFieldTest {
    
    @Mock
    private DateTimeField mockField;
    
    @Mock
    private Chronology mockBase;
    
    private LenientDateTimeField lenientField;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        when(mockField.isLenient()).thenReturn(false);
        lenientField = new LenientDateTimeField(mockField, mockBase);
    }
    
    @Test
    public void testGetInstance_withNullField() {
        assertNull(LenientDateTimeField.getInstance(null, mockBase));
    }
    
    @Test
    public void testGetInstance_withStrictField() {
        StrictDateTimeField strictField = mock(StrictDateTimeField.class);
        when(strictField.getWrappedField()).thenReturn(mockField);
        
        DateTimeField result = LenientDateTimeField.getInstance(strictField, mockBase);
        
        assertEquals(mockField, result);
    }
    
    @Test
    public void testGetInstance_withLenientField() {
        when(mockField.isLenient()).thenReturn(true);
        
        DateTimeField result = LenientDateTimeField.getInstance(mockField, mockBase);
        
        assertEquals(mockField, result);
    }
    
    @Test
    public void testGetInstance_withNonLenientField() {
        DateTimeField result = LenientDateTimeField.getInstance(mockField, mockBase);
        
        assertEquals(LenientDateTimeField.class, result.getClass());
    }
    
    @Test
    public void testIsLenient() {
        boolean result = lenientField.isLenient();
        
        assertEquals(true, result);
    }
    
    @Test
    public void testSet() {
        DateTimeZone zone = DateTimeZone.forOffsetHours(1);
        long localInstant = 1000L;
        long utcInstant = 900L;
        int difference = 2;
        
        when(mockBase.getZone()).thenReturn(zone);
        when(mockBase.withUTC()).thenReturn(mockBase);
        when(mockField.add(localInstant, difference)).thenReturn(1100L);
        when(mockBase.getZone().convertLocalToUTC(1100L, false)).thenReturn(utcInstant);
        
        DateTimeUtils.setCurrentMillisFixed(localInstant);
        long result = lenientField.set(utcInstant, difference);
        
        assertEquals(utcInstant, result);
        
        verify(mockBase).getZone();
        verify(mockZone).convertUTCToLocal(utcInstant);
        verify(mockField).add(localInstant, difference);
        verify(mockBase).getZone().convertLocalToUTC(1100L, false);
    }
}
package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class LenientDateTimeFieldTest {
    
    private Chronology mockBaseChronology;
    private DateTimeField mockWrappedField;
    private LenientDateTimeField lenientDateTimeField;
    
    @Before
    public void setup() {
        mockBaseChronology = Mockito.mock(Chronology.class);
        mockWrappedField = Mockito.mock(DateTimeField.class);
        lenientDateTimeField = new LenientDateTimeField(mockWrappedField, mockBaseChronology);
    }
    
    @Test
    public void testGetInstance() {
        DateTimeField nonLenientField = Mockito.mock(DateTimeField.class);
        LenientDateTimeField lenientNonLenientField = (LenientDateTimeField) LenientDateTimeField.getInstance(nonLenientField, mockBaseChronology);
        assertEquals(nonLenientField, lenientNonLenientField);
        
        StrictDateTimeField strictField = Mockito.mock(StrictDateTimeField.class);
        Mockito.when(strictField.getWrappedField()).thenReturn(nonLenientField);
        LenientDateTimeField lenientStrictField = (LenientDateTimeField) LenientDateTimeField.getInstance(strictField, mockBaseChronology);
        assertEquals(nonLenientField, lenientStrictField);
        
        Mockito.when(nonLenientField.isLenient()).thenReturn(false);
        LenientDateTimeField lenientNonLenientField2 = (LenientDateTimeField) LenientDateTimeField.getInstance(nonLenientField, mockBaseChronology);
        assertFalse(lenientNonLenientField2.isLenient());
        assertEquals(nonLenientField, lenientNonLenientField2.getWrappedField());
        
        LenientDateTimeField lenientField = (LenientDateTimeField) LenientDateTimeField.getInstance(mockWrappedField, mockBaseChronology);
        assertTrue(lenientField.isLenient());
        assertEquals(mockWrappedField, lenientField.getWrappedField());
    }
    
    @Test
    public void testIsLenient() {
        assertTrue(lenientDateTimeField.isLenient());
    }
    
    @Test
    public void testSet() {
        long instant = 1000L;
        int value = 1;
        long localInstant = 500L;
        long difference = 1L;
        
        Mockito.when(mockWrappedField.get(instant)).thenReturn(0);
        Mockito.when(mockBaseChronology.getZone()).thenReturn(Mockito.mock(DateTimeZone.class));
        Mockito.when(mockBaseChronology.getZone().convertUTCToLocal(instant)).thenReturn(localInstant);
        Mockito.when(mockWrappedField.getType()).thenReturn(Mockito.mock(DateTimeFieldType.class));
        Mockito.when(mockBaseChronology.withUTC()).thenReturn(Mockito.mock(Chronology.class));
        Mockito.when(mockWrappedField.getField(mockBaseChronology.withUTC())).thenReturn(Mockito.mock(DateTimeField.class));
        Mockito.when(mockWrappedField.getField(mockBaseChronology.withUTC()).add(localInstant, difference)).thenReturn(localInstant + difference);
        Mockito.when(mockBaseChronology.getZone().convertLocalToUTC(localInstant + difference, false)).thenReturn(instant);
        
        assertEquals(instant, lenientDateTimeField.set(instant, value));
        
        // Verify method calls
        Mockito.verify(mockWrappedField).get(instant);
        Mockito.verify(mockBaseChronology).getZone();
        Mockito.verify(mockBaseChronology.getZone()).convertUTCToLocal(instant);
        Mockito.verify(mockWrappedField).getType();
        Mockito.verify(mockBaseChronology).withUTC();
        Mockito.verify(mockWrappedField).getField(mockBaseChronology.withUTC());
        Mockito.verify(mockWrappedField.getField(mockBaseChronology.withUTC())).add(localInstant, difference);
        Mockito.verify(mockBaseChronology.getZone()).convertLocalToUTC(localInstant + difference, false);
    }
}
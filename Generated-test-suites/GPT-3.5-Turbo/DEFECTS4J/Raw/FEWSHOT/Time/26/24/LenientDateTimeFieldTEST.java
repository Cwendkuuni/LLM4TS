package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    private DateTimeField baseField;
    private Chronology baseChronology;
    private LenientDateTimeField lenientField;

    @Before
    public void setUp() {
        baseField = mock(DateTimeField.class);
        baseChronology = mock(Chronology.class);
        lenientField = new LenientDateTimeField(baseField, baseChronology);
    }

    @Test
    public void testGetInstance_withNullField() {
        assertNull(LenientDateTimeField.getInstance(null, baseChronology));
    }

    @Test
    public void testGetInstance_withStrictField() {
        StrictDateTimeField strictField = mock(StrictDateTimeField.class);
        when(strictField.getWrappedField()).thenReturn(baseField);
        
        DateTimeField result = LenientDateTimeField.getInstance(strictField, baseChronology);
        
        assertEquals(baseField, result);
    }

    @Test
    public void testGetInstance_withLenientField() {
        when(baseField.isLenient()).thenReturn(true);

        DateTimeField result = LenientDateTimeField.getInstance(baseField, baseChronology);

        assertEquals(baseField, result);
    }

    @Test
    public void testGetInstance_withNonLenientField() {
        when(baseField.isLenient()).thenReturn(false);

        DateTimeField result = LenientDateTimeField.getInstance(baseField, baseChronology);

        assertEquals(lenientField, result);
    }

    @Test
    public void testIsLenient() {
        boolean result = lenientField.isLenient();

        assertEquals(true, result);
    }

    @Test
    public void testSet() {
        DateTimeUtils.setCurrentMillisFixed(1000L); // Fixed current time for consistent testing

        long instant = 2000L;
        int value = 10;
        long localInstant = 1500L;
        long difference = 10L;
        long updatedLocalInstant = 1510L;
        long updatedInstant = 2500L;

        when(baseChronology.getZone()).thenReturn(mock(org.joda.time.DateTimeZone.class));
        when(baseChronology.getZone().convertUTCToLocal(instant)).thenReturn(localInstant);
        when(baseField.get(instant)).thenReturn(value);
        when(lenientField.getType()).thenReturn(mock(DateTimeFieldType.class));
        when(baseChronology.withUTC()).thenReturn(baseChronology);
        when(lenientField.getType().getField(baseChronology)).thenReturn(baseField);
        when(baseField.add(localInstant, difference)).thenReturn(updatedLocalInstant);
        when(baseChronology.getZone().convertLocalToUTC(updatedLocalInstant, false)).thenReturn(updatedInstant);

        long result = lenientField.set(instant, value);

        assertEquals(updatedInstant, result);
    }
}
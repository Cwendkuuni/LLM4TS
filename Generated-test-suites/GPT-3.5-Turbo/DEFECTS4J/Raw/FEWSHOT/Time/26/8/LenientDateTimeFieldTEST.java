
package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.chrono.LenientChronology;
import org.junit.Test;
import org.mockito.Mockito;

public class LenientDateTimeFieldTest {

    @Test
    public void testGetInstance() {
        DateTimeField field = Mockito.mock(DateTimeField.class);
        Chronology base = Mockito.mock(Chronology.class);
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        assertTrue(lenientField instanceof LenientDateTimeField);
    }

    @Test
    public void testIsLenient() {
        DateTimeField field = Mockito.mock(DateTimeField.class);
        Chronology base = Mockito.mock(Chronology.class);
        DateTimeField lenientField = new LenientDateTimeField(field, base);
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSet() {
        DateTimeField field = Mockito.mock(DateTimeField.class);
        Chronology base = Mockito.mock(Chronology.class);
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        long instant = 1234567890L;
        int value = 5;
        long expectedInstant = 987654321L;
        Mockito.when(base.getZone()).thenReturn(Mockito.mock(DateTimeZone.class));
        Mockito.when(field.get(instant)).thenReturn(2);
        Mockito.when(field.withUTC()).thenReturn(Mockito.mock(DateTimeField.class));
        Mockito.when(field.getField(Mockito.any())).thenReturn(Mockito.mock(DateTimeField.class));
        Mockito.when(field.getField(Mockito.any()).add(Mockito.anyLong(), Mockito.anyInt())).thenReturn(expectedInstant);
        long result = lenientField.set(instant, value);
        assertEquals(expectedInstant, result);
    }
}

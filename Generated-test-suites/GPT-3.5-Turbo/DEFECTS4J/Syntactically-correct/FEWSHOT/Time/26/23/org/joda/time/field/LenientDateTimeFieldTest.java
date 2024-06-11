import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.field.DelegatedDateTimeField;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Test;
import org.mockito.Mockito;

public class LenientDateTimeFieldTest {

    @Test
    public void testGetInstance() {
        DateTimeField field = Mockito.mock(DateTimeField.class);
        Chronology base = Mockito.mock(Chronology.class);
        
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        assertEquals(field, lenientField);
        
        StrictDateTimeField strictField = Mockito.mock(StrictDateTimeField.class);
        DateTimeField wrappedField = Mockito.mock(DateTimeField.class);
        Mockito.when(strictField.getWrappedField()).thenReturn(wrappedField);
        lenientField = LenientDateTimeField.getInstance(strictField, base);
        assertEquals(wrappedField, lenientField);
        
        Mockito.when(field.isLenient()).thenReturn(false);
        lenientField = LenientDateTimeField.getInstance(field, base);
        assertEquals(LenientDateTimeField.class, lenientField.getClass());
    }

    @Test
    public void testLenientDateTimeField() {
        DateTimeField field = Mockito.mock(DateTimeField.class);
        Chronology base = Mockito.mock(Chronology.class);
        
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        assertEquals(field, lenientField.getWrappedField());
        assertEquals(base, lenientField.getChronology());
    }

    @Test
    public void testIsLenient() {
        DateTimeField field = Mockito.mock(DateTimeField.class);
        Chronology base = Mockito.mock(Chronology.class);
        
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        assertEquals(true, lenientField.isLenient());
    }

    @Test
    public void testSet() {
        DateTimeField field = Mockito.mock(DateTimeField.class);
        Chronology base = Mockito.mock(Chronology.class);
        
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        
        long instant = 123456789L;
        int value = 10;
        long localInstant = 987654321L;
        long difference = 5;
        long convertedLocalInstant = 192837465L;
        long convertedInstant = 345678912L;
        
        Mockito.when(base.getZone().convertUTCToLocal(instant)).thenReturn(localInstant);
        Mockito.when(field.get(instant)).thenReturn(5);
        Mockito.when(field.getType()).thenReturn(field);
        Mockito.when(base.withUTC()).thenReturn(base);
        Mockito.when(field.add(localInstant, difference)).thenReturn(convertedLocalInstant);
        Mockito.when(base.getZone().convertLocalToUTC(convertedLocalInstant, false)).thenReturn(convertedInstant);
        
        assertEquals(convertedInstant, lenientField.set(instant, value));
    }

}
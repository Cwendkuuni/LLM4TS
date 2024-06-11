import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationFieldType;
import org.joda.time.TestDateMidnight_Basics;
import org.joda.time.chrono.ISOChronology;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {
    
    private DateTimeField field;
    private Chronology base;
    private LenientDateTimeField lenientField;
    
    @Before
    public void setUp() {
        field = Mockito.mock(DateTimeField.class);
        base = ISOChronology.getInstanceUTC();
        lenientField = new LenientDateTimeField(field, base);
    }
    
    @Test
    public void testGetInstance() {
        DateTimeField lenientInstance = LenientDateTimeField.getInstance(field, base);
        assertEquals(lenientField, lenientInstance);
    }
    
    @Test
    public void testGetInstance_withNullField() {
        DateTimeField lenientInstance = LenientDateTimeField.getInstance(null, base);
        assertNull(lenientInstance);
    }
    
    @Test
    public void testGetInstance_withStrictDateTimeField() {
        StrictDateTimeField strictField = Mockito.mock(StrictDateTimeField.class);
        Mockito.when(strictField.getWrappedField()).thenReturn(field);
        DateTimeField lenientInstance = LenientDateTimeField.getInstance(strictField, base);
        assertEquals(lenientField, lenientInstance);
    }
    
    @Test
    public void testGetInstance_withLenientField() {
        Mockito.when(field.isLenient()).thenReturn(true);
        DateTimeField lenientInstance = LenientDateTimeField.getInstance(field, base);
        assertEquals(field, lenientInstance);
    }
    
    @Test
    public void testIsLenient() {
        assertTrue(lenientField.isLenient());
    }
    
    @Test
    public void testSet() {
        long instant = DateTimeUtils.currentTimeMillis();
        int value = 10;
        long expected = base.getZone().convertLocalToUTC(10, false);
        Mockito.when(field.get(instant)).thenReturn(5);
        Mockito.when(field.getType()).thenReturn(DurationFieldType.hours());
        Mockito.when(base.withUTC()).thenReturn(ISOChronology.getInstanceUTC());
        Mockito.when(base.getZone().convertUTCToLocal(instant)).thenReturn(5L);
        Mockito.when(field.add(5L, 5)).thenReturn(10L);
        Mockito.when(base.getZone().convertLocalToUTC(10L, false)).thenReturn(expected);
        
        long result = lenientField.set(instant, value);
        
        Mockito.verify(base.getZone()).convertLocalToUTC(5L, false);
        Mockito.verify(base.getZone()).convertLocalToUTC(10L, false);
        Mockito.verify(field).add(5L, 5);
        Mockito.verify(field).get(instant);
        Mockito.verify(field).getType();
        assertEquals(expected, result);
    }
}
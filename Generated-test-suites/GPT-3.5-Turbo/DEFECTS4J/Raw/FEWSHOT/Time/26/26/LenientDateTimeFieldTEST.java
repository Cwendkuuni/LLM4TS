package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.LocalTime;
import org.joda.time.chrono.GregorianChronology;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {
    private LenientDateTimeField lenientField;
    private DateTimeField wrappedField;
    private Chronology base;
    
    @Before
    public void setUp() {
        base = GregorianChronology.getInstance();
        wrappedField = base.hourOfDay();
        lenientField = new LenientDateTimeField(wrappedField, base);
    }
    
    @Test
    public void testIsLenient() {
        assertTrue(lenientField.isLenient());
    }
    
    @Test
    public void testSet() {
        long instant = new LocalTime(12, 0).toDateTimeToday().getMillis();
        assertEquals(base.hourOfDay().set(instant, 2), lenientField.set(instant, 2));
        assertEquals(base.hourOfDay().set(instant, 25), lenientField.set(instant, 25));
    }
    
    @Test
    public void testSet_NullWrappedField() {
        lenientField = new LenientDateTimeField(null, base);
        long instant = new LocalTime(12, 0).toDateTimeToday().getMillis();
        assertNull(lenientField.set(instant, 2));
    }
    
    @Test
    public void testGetInstance() {
        lenientField = (LenientDateTimeField) LenientDateTimeField.getInstance(base.year(), base);
        assertNotNull(lenientField);
        assertTrue(lenientField.isLenient());
    }
    
    @Test
    public void testGetInstance_NullField() {
        lenientField = (LenientDateTimeField) LenientDateTimeField.getInstance(null, base);
        assertNull(lenientField);
    }
    
    @Test
    public void testGetInstance_LenientField() {
        lenientField = (LenientDateTimeField) LenientDateTimeField.getInstance(lenientField, base);
        assertNotNull(lenientField);
        assertTrue(lenientField.isLenient());
        assertEquals(wrappedField, lenientField.getWrappedField());
    }
    
    @Test
    public void testGetInstance_StrictField() {
        StrictDateTimeField strictField = new StrictDateTimeField(wrappedField, base);
        lenientField = (LenientDateTimeField) LenientDateTimeField.getInstance(strictField, base);
        assertNotNull(lenientField);
        assertTrue(lenientField.isLenient());
        assertEquals(wrappedField, lenientField.getWrappedField());
    }
    
    @Test
    public void testGetInstance_LenientField_IsLenient() {
        DateTimeField lenientDateTimeField = LenientDateTimeField.getInstance(wrappedField, base);
        lenientField = (LenientDateTimeField) LenientDateTimeField.getInstance(lenientDateTimeField, base);
        assertNotNull(lenientField);
        assertTrue(lenientField.isLenient());
        assertEquals(wrappedField, lenientField.getWrappedField());
    }
    
    @Test
    public void testGetInstance_LenientField_IsStrict() {
        StrictDateTimeField strictField = new StrictDateTimeField(wrappedField, base);
        DateTimeField lenientDateTimeField = LenientDateTimeField.getInstance(strictField, base);
        lenientField = (LenientDateTimeField) LenientDateTimeField.getInstance(lenientDateTimeField, base);
        assertNotNull(lenientField);
        assertTrue(lenientField.isLenient());
        assertEquals(wrappedField, lenientField.getWrappedField());
    }
    
    @Test
    public void testGetInstance_FieldIsNull() {
        lenientField = (LenientDateTimeField) LenientDateTimeField.getInstance(null, base);
        assertNull(lenientField);
    }
}
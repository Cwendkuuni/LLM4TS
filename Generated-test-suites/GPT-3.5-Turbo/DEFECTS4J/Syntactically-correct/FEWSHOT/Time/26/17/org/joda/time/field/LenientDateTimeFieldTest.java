package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.chrono.LenientChronology;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    private DateTimeField lenientField;
    private DateTimeField strictField;
    private LenientChronology chronology;

    @Before
    public void setUp() {
        strictField = new StubDateTimeField();
        chronology = new LenientChronology(strictField.getChronology());
        lenientField = new LenientDateTimeField(strictField, chronology);
    }

    @Test
    public void testConstructor() {
        assertEquals(strictField, lenientField.getWrappedField());
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSet() {
        long now = System.currentTimeMillis();
        int value = 10;
        long updatedNow = lenientField.set(now, value);
        assertEquals(value, lenientField.get(updatedNow));
    }

    @Test
    public void testSet_OutOfBounds() {
        long now = System.currentTimeMillis();
        int value = 100;
        long updatedNow = lenientField.set(now, value);
        assertEquals(value, lenientField.get(updatedNow));
    }
    
    @Test
    public void testSet_WithNegativeValue() {
        long now = System.currentTimeMillis();
        int value = -10;
        long updatedNow = lenientField.set(now, value);
        assertEquals(value, lenientField.get(updatedNow));
    }
    
    @Test
    public void testSet_EqualValue() {
        long now = System.currentTimeMillis();
        int value = lenientField.get(now);
        long updatedNow = lenientField.set(now, value);
        assertEquals(value, lenientField.get(updatedNow));
    }
    
    @Test
    public void testSet_NullValue() {
        long now = System.currentTimeMillis();
        long updatedNow = lenientField.set(now, null);
        assertEquals(lenientField.get(now), lenientField.get(updatedNow));
    }
    
    @Test
    public void testIsLenient() {
        assertTrue(lenientField.isLenient());
    }
    
    @Test
    public void testIsLenient_WithStrictField() {
        assertFalse(strictField.isLenient());
    }
    
    @Test
    public void testGetInstance_WithNullField() {
        DateTimeField field = null;
        DateTimeField result = LenientDateTimeField.getInstance(field, null);
        assertNull(result);
    }
    
    @Test
    public void testGetInstance_WithStrictField() {
        DateTimeField field = new StubDateTimeField();
        DateTimeField result = LenientDateTimeField.getInstance(field, null);
        assertTrue(result.isLenient());
    }
    
    @Test
    public void testGetInstance_WithLenientField() {
        DateTimeField field = new StubDateTimeField(true);
        DateTimeField result = LenientDateTimeField.getInstance(field, null);
        assertEquals(field, result);
    }
    
    private class StubDateTimeField extends BaseDateTimeField {
        
        private boolean lenient;
        
        public StubDateTimeField() {
            this(false);
        }
        
        public StubDateTimeField(boolean lenient) {
            super(null);
            this.lenient = lenient;
        }
        
        public boolean isLenient() {
            return lenient;
        }

        public int get(long instant) {
            return HOURS.convert(instant, MINUTES);
        }

        public long set(long instant, int value) {
            return MINUTES.set(instant, value);
        }
        
        public Chronology getChronology() {
            return null;
        }
    }
}
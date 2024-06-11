package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {
    
    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        assertEquals(type, field.getType());
    }
    
    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.hours();
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        
        assertEquals(type.getName(), field.getName());
    }
    
    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.weeks();
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        
        assertTrue(field.isPrecise());
    }
    
    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.eras();
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        
        assertFalse(field.isSupported());
    }
    
    @Test
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.centuries();
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        
        try {
            field.getValue(1000L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    @Test
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        
        try {
            field.getValueAsLong(1000L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    @Test
    public void testGetValueWithInstant() {
        DurationFieldType type = DurationFieldType.halfdays();
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        
        try {
            field.getValue(1000L, 2000L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    @Test
    public void testGetValueAsLongWithInstant() {
        DurationFieldType type = DurationFieldType.millisOfSecond();
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        
        try {
            field.getValueAsLong(1000L, 2000L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    @Test
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.months();
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        
        try {
            field.getMillis(10);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    @Test
    public void testGetMillisWithLong() {
        DurationFieldType type = DurationFieldType.minutes();
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        
        try {
            field.getMillis(10L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    @Test
    public void testGetMillisWithInstant() {
        DurationFieldType type = DurationFieldType.micros();
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        
        try {
            field.getMillis(10, 20L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    @Test
    public void testGetMillisWithLongAndInstant() {
        DurationFieldType type = DurationFieldType.minutes();
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        
        try {
            field.getMillis(10L, 20L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    @Test
    public void testAdd() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        
        try {
            field.add(1000L, 10);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    @Test
    public void testAddWithLong() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        
        try {
            field.add(1000L, 10L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    @Test
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        
        try {
            field.getDifference(1000L, 500L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.hours();
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        
        try {
            field.getDifferenceAsLong(1000L, 500L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.minutes();
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        
        assertEquals(0L, field.getUnitMillis());
    }
    
    @Test
    public void testCompareTo() {
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField field1 = new UnsupportedDurationField(type);
        UnsupportedDurationField field2 = new UnsupportedDurationField(type);
        
        assertEquals(0, field1.compareTo(field2));
    }
    
    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.hours();
        UnsupportedDurationField field1 = new UnsupportedDurationField(type);
        UnsupportedDurationField field2 = new UnsupportedDurationField(type);
        
        assertTrue(field1.equals(field2));
    }
    
    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.minutes();
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        
        assertEquals(type.hashCode(), field.hashCode());
    }
    
    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.months();
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        
        assertEquals("UnsupportedDurationField[months]", field.toString());
    }

}
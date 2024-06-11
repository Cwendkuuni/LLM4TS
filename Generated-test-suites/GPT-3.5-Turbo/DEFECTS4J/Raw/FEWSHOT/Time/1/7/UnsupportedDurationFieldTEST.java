/*
 * Test cases for UnsupportedDurationField class
 */ 

import org.joda.time.DurationFieldType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {
  
    @Test
    public void testGetInstance() {
        UnsupportedDurationField instance1 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_TYPE);
        UnsupportedDurationField instance2 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_TYPE);
        
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertEquals(instance1, instance2);
    }
    
    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.HOUR_TYPE);
        
        DurationFieldType type = field.getType();
        
        assertNotNull(type);
        assertEquals(DurationFieldType.HOUR_TYPE, type);
    }
    
    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND_TYPE);
        
        String name = field.getName();
        
        assertNotNull(name);
        assertEquals(DurationFieldType.SECOND_TYPE.getName(), name);
    }
    
    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_TYPE);
        
        boolean supported = field.isSupported();
        
        assertFalse(supported);
    }
    
    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MONTH_TYPE);
        
        boolean precise = field.isPrecise();
        
        assertTrue(precise);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.HOUR_TYPE);
        
        field.getValue(12345L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND_TYPE);
        
        field.getValueAsLong(54321L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_TYPE);
        
        field.getValue(12345L, 67890L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS_TYPE);
        
        field.getValueAsLong(54321L, 98765L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_TYPE);
        
        field.getMillis(54321);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.HOUR_TYPE);
        
        field.getMillis(54321L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_TYPE);
        
        field.getMillis(54321, 98765L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValueAndInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND_TYPE);
        
        field.getMillis(54321L, 98765L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND_TYPE);
        
        field.add(12345L, 54321);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithLongValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_TYPE);
        
        field.add(12345L, 54321L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.HOUR_TYPE);
        
        field.getDifference(12345L, 54321L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND_TYPE);
        
        field.getDifferenceAsLong(12345L, 54321L);
    }
    
    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_TYPE);
        
        long unitMillis = field.getUnitMillis();
        
        assertEquals(0, unitMillis);
    }
    
    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.DAY_TYPE);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.HOUR_TYPE);
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.HOUR_TYPE);
        
        assertTrue(field1.compareTo(field2) < 0);
        assertTrue(field2.compareTo(field1) > 0);
        assertEquals(0, field2.compareTo(field3));
    }
    
    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_TYPE);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_TYPE);
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.SECOND_TYPE);
        
        assertTrue(field1.equals(field2));
        assertTrue(field2.equals(field1));
        assertFalse(field1.equals(field3));
        assertFalse(field3.equals(field1));
    }
    
    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS_TYPE);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS_TYPE);
        
        assertEquals(field1.hashCode(), field1.hashCode());
        assertEquals(field1.hashCode(), field2.hashCode());
    }
    
    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND_TYPE);
        
        String expected = "UnsupportedDurationField[second]";
        String actual = field.toString();
        
        assertEquals(expected, actual);
    }
    
}
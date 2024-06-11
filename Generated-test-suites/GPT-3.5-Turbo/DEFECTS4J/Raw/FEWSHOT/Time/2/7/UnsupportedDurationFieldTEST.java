package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.joda.time.DurationFieldType;
import org.joda.time.field.UnsupportedDurationField;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
    	assertEquals(DurationFieldType.seconds(), UnsupportedDurationField.getInstance(DurationFieldType.seconds()).getType());
    }
    
    @Test
    public void testGetName() {
    	assertEquals("years", UnsupportedDurationField.getInstance(DurationFieldType.years()).getName());
    }
    
    @Test
    public void testIsSupported() {
    	assertFalse(UnsupportedDurationField.getInstance(DurationFieldType.months()).isSupported());
    }
    
    @Test
    public void testIsPrecise() {
    	assertFalse(UnsupportedDurationField.getInstance(DurationFieldType.weeks()).isPrecise());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
    	UnsupportedDurationField.getInstance(DurationFieldType.seconds()).getValue(123456789);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
    	UnsupportedDurationField.getInstance(DurationFieldType.minutes()).getValueAsLong(123456789);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
    	UnsupportedDurationField.getInstance(DurationFieldType.hours()).getValue(123456789, 987654321);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
    	UnsupportedDurationField.getInstance(DurationFieldType.days()).getValueAsLong(123456789, 987654321);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
    	UnsupportedDurationField.getInstance(DurationFieldType.hours()).getMillis(123);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueLong() {
    	UnsupportedDurationField.getInstance(DurationFieldType.minutes()).getMillis(123456789L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueInstant() {
    	UnsupportedDurationField.getInstance(DurationFieldType.days()).getMillis(123, 987654321);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueLongInstant() {
    	UnsupportedDurationField.getInstance(DurationFieldType.seconds()).getMillis(123456789L, 987654321);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantValue() {
    	UnsupportedDurationField.getInstance(DurationFieldType.hours()).add(123456789, 456);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantValueLong() {
    	UnsupportedDurationField.getInstance(DurationFieldType.minutes()).add(123456789L, 987654321L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
    	UnsupportedDurationField.getInstance(DurationFieldType.days()).getDifference(123456789L, 987654321L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
    	UnsupportedDurationField.getInstance(DurationFieldType.weeks()).getDifferenceAsLong(123456789L, 987654321L);
    }
    
    @Test
    public void testGetUnitMillis() {
    	assertEquals(0, UnsupportedDurationField.getInstance(DurationFieldType.months()).getUnitMillis());
    }
    
    @Test
    public void testCompareTo() {
    	assertEquals(0, UnsupportedDurationField.getInstance(DurationFieldType.years()).compareTo(UnsupportedDurationField.getInstance(DurationFieldType.years())));
    }
    
    @Test
    public void testEquals() {
    	UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
    	UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
    	
    	assertEquals(field1, field1);
    	assertEquals(field1, field2);
    	assertEquals(field2, field1);
    	
    	UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.days());
    	
    	assertFalse(field1.equals(field3));
    }
    
    @Test
    public void testHashCode() {
    	UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    	UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    	
    	assertEquals(field1.hashCode(), field2.hashCode());
    }
    
    @Test
    public void testToString() {
    	assertEquals("UnsupportedDurationField[days]", UnsupportedDurationField.getInstance(DurationFieldType.days()).toString());
    }
    
}
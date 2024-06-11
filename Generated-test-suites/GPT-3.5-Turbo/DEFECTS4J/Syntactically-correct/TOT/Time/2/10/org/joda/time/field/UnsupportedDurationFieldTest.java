
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest {
    
    private UnsupportedDurationField unsupportedDurationField;
    
    @Before
    public void setup() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.YEAR);
    }
    
    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.YEAR, unsupportedDurationField.getType());
    }
    
    @Test
    public void testGetName() {
        assertEquals("year", unsupportedDurationField.getName());
    }
    
    @Test
    public void testIsSupported() {
        assertFalse(unsupportedDurationField.isSupported());
    }
    
    @Test
    public void testIsPrecise() {
        assertTrue(unsupportedDurationField.isPrecise());
    }
    
    @Test (expected = UnsupportedOperationException.class)
    public void testGetValue() {
        unsupportedDurationField.getValue(1000);
    }
    
    @Test (expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        unsupportedDurationField.getValueAsLong(1000);
    }
    
    @Test (expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        unsupportedDurationField.getValue(1000, 100000);
    }
    
    @Test (expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        unsupportedDurationField.getValueAsLong(1000, 100000);
    }
    
    @Test (expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        unsupportedDurationField.getMillis(1000);
    }
    
    @Test (expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValue() {
        unsupportedDurationField.getMillis(1000L);
    }
    
    @Test (expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        unsupportedDurationField.getMillis(1000, 100000);
    }
    
    @Test (expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValueAndInstant() {
        unsupportedDurationField.getMillis(1000L, 100000L);
    }
    
    @Test (expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValue() {
        unsupportedDurationField.add(100000, 1000);
    }
    
    @Test (expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndLongValue() {
        unsupportedDurationField.add(100000L, 1000L);
    }
    
    @Test (expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        unsupportedDurationField.getDifference(100000, 1000);
    }
    
    @Test (expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLongWithInstant() {
        unsupportedDurationField.getDifferenceAsLong(100000L, 1000L);
    }
    
    @Test
    public void testGetUnitMillis() {
        assertEquals(0, unsupportedDurationField.getUnitMillis());
    }
    
    @Test
    public void testCompareTo() {
        UnsupportedDurationField otherDurationField = UnsupportedDurationField.getInstance(DurationFieldType.HOUR);
        assertEquals(0, unsupportedDurationField.compareTo(otherDurationField));
    }
    
    @Test
    public void testEquals() {
        UnsupportedDurationField sameDurationField = UnsupportedDurationField.getInstance(DurationFieldType.YEAR);
        UnsupportedDurationField differentDurationField = UnsupportedDurationField.getInstance(DurationFieldType.HOUR);
        
        assertEquals(sameDurationField, unsupportedDurationField);
        assertNotEquals(differentDurationField, unsupportedDurationField);
    }
    
    @Test
    public void testHashCode() {
        UnsupportedDurationField sameDurationField = UnsupportedDurationField.getInstance(DurationFieldType.YEAR);
        
        assertEquals(sameDurationField.hashCode(), unsupportedDurationField.hashCode());
    }
    
    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[year]", unsupportedDurationField.toString());
    }
    
}


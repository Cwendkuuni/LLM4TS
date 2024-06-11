import org.junit.Test;
import org.joda.time.DurationFieldType;

import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {
    
    private UnsupportedDurationField unsupportedDurationField;
    
    @Test
    public void testGetType() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertEquals(DurationFieldType.MILLIS, unsupportedDurationField.getType());
    }
    
    @Test
    public void testGetName() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertEquals("Millis", unsupportedDurationField.getName());
    }
    
    @Test
    public void testIsSupported() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertFalse(unsupportedDurationField.isSupported());
    }
    
    @Test
    public void testIsPrecise() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertTrue(unsupportedDurationField.isPrecise());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        unsupportedDurationField.getValue(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        unsupportedDurationField.getValueAsLong(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        unsupportedDurationField.getValue(1000, System.currentTimeMillis());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        unsupportedDurationField.getValueAsLong(1000, System.currentTimeMillis());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        unsupportedDurationField.getMillis(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueLong() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        unsupportedDurationField.getMillis(1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        unsupportedDurationField.getMillis(1000, System.currentTimeMillis());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueLongAndInstant() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        unsupportedDurationField.getMillis(1000L, System.currentTimeMillis());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValue() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        unsupportedDurationField.add(System.currentTimeMillis(), 1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndLongValue() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        unsupportedDurationField.add(System.currentTimeMillis(), 1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        unsupportedDurationField.getDifference(System.currentTimeMillis(), System.currentTimeMillis() - 1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        unsupportedDurationField.getDifferenceAsLong(System.currentTimeMillis(), System.currentTimeMillis() - 1000);
    }
    
    @Test
    public void testGetUnitMillis() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertEquals(0, unsupportedDurationField.getUnitMillis());
    }
    
    @Test
    public void testCompareTo() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        DurationField anotherDurationField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertEquals(0, unsupportedDurationField.compareTo(anotherDurationField));
    }
    
    @Test
    public void testEquals() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        DurationField anotherDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertTrue(unsupportedDurationField.equals(anotherDurationField));
    }
    
    @Test
    public void testHashCode() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        DurationField anotherDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertEquals(unsupportedDurationField.hashCode(), anotherDurationField.hashCode());
    }
    
    @Test
    public void testToString() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertEquals("UnsupportedDurationField[Millis]", unsupportedDurationField.toString());
    }
    
    @Test
    public void testReadResolve() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertSame(unsupportedDurationField, unsupportedDurationField.readResolve());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedException() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        throw unsupportedDurationField.unsupported();
    }
}
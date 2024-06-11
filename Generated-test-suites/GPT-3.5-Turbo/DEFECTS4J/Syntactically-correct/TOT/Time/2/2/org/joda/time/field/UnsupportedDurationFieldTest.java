import static org.junit.Assert.*;
import org.junit.Test;

public class UnsupportedDurationFieldTest {
    
    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        assertEquals(DurationFieldType.minutes(), field.getType());
    }
    
    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertEquals("hours", field.getName());
    }
    
    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertFalse(field.isSupported());
    }
    
    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertTrue(field.isPrecise());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        field.getValue(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.getValueAsLong(2000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_withInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        field.getValue(500, 1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_withInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        field.getValueAsLong(100, 200);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_withValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        field.getMillis(5);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_withLongValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        field.getMillis(100000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_withValueAndInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getMillis(500, 1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_withLongValueAndInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.getMillis(100, 200);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_withValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        field.add(1000, 5);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_withLongValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        field.add(2000, 10);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        field.getDifference(1000, 500);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getDifferenceAsLong(2000, 1500);
    }
    
    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertEquals(0, field.getUnitMillis());
    }
    
    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals(0, field1.compareTo(field2));
    }
    
    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertEquals(field1, field2);
    }
    
    @Test
    public void testHashCode() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        assertEquals("minutes".hashCode(), field.hashCode());
    }
    
    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertEquals("UnsupportedDurationField[seconds]", field.toString());
    }
    
    @Test
    public void testReadResolve() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        assertEquals(field1, field2);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        throw field.unsupported();
    }
}
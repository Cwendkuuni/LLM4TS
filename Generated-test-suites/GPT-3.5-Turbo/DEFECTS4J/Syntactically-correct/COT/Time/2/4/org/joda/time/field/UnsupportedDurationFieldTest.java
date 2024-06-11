import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertEquals(DurationFieldType.MILLIS, field.getType());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertEquals("millis", field.getName());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertFalse(field.isSupported());
    }
    
    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertTrue(field.isPrecise());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.getValue(10);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.getValueAsLong(10);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.getValue(10, 1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.getValueAsLong(10, 1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.getMillis(10);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.getMillis(10L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.getMillis(10, 1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstantAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.getMillis(10L, 1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.add(1000, 10);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.add(1000L, 10L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.getDifference(1000, 500);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.getDifferenceAsLong(1000L, 500L);
    }
    
    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertEquals(0, field.getUnitMillis());
    }
    
    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertEquals(field1, field2);
    }
    
    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertEquals(field1.hashCode(), field2.hashCode());
    }
    
    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertEquals("UnsupportedDurationField[millis]", field.toString());
    }
    
}
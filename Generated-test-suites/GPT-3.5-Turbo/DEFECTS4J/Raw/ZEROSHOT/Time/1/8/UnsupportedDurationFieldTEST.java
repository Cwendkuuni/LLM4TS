import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        assertSame(type, field.getType());
    }
    
    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        assertEquals("millis", field.getName());
    }
    
    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        assertFalse(field.isSupported());
    }
    
    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        assertTrue(field.isPrecise());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        field.getValue(1);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        field.getValueAsLong(1);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        field.getValue(1, 1);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        field.getValueAsLong(1, 1);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInt() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        field.getMillis(1);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLong() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        field.getMillis(1L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithIntAndInstant() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        field.getMillis(1, 1);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongAndInstant() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        field.getMillis(1L, 1);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndInt() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        field.add(1, 1);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndLong() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        field.add(1, 1L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceWithMillis() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        field.getDifference(1, 1);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceWithMillisAsLong() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        field.getDifferenceAsLong(1, 1);
    }
    
    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        assertEquals(0, field.getUnitMillis());
    }
    
    @Test
    public void testCompareTo() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        DurationField otherField = new UnsupportedDurationField(type);
        assertEquals(1, field.compareTo(otherField));
        
        otherField = duration -> 0;
        assertEquals(0, field.compareTo(otherField));
    }
    
    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        assertTrue(field.equals(field));
        
        DurationField otherField = new UnsupportedDurationField(type);
        assertTrue(field.equals(otherField));
        
        otherField = new UnsupportedDurationField(DurationFieldType.seconds());
        assertFalse(field.equals(otherField));
    }
    
    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        int expectedHashCode = type.getName().hashCode();
        assertEquals(expectedHashCode, field.hashCode());
    }
    
    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        String expectedToString = "UnsupportedDurationField[" + type.getName() + ']';
        assertEquals(expectedToString, field.toString());
    }
    
    @Test
    public void testReadResolve() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        assertSame(field, field.readResolve());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperationException() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        throw field.unsupported();
    }
}
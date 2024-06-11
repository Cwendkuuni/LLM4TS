import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {
    
    @Test
    public void testGetInstance() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertEquals(DurationFieldType.millis(), field.getType());
        
        // Ensure that getInstance returns the same instance for the same DurationFieldType
        UnsupportedDurationField sameField = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertEquals(field, sameField);
    }
    
    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type.getName(), field.getName());
    }
    
    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertFalse(field.isSupported());
    }
    
    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertTrue(field.isPrecise());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getValue(0);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getValueAsLong(0);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getValue(0, 0);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getValueAsLong(0, 0);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getMillis(0);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getMillis(0L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstantAndValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getMillis(0, 0L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstantAndValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getMillis(0L, 0L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddValueToInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.add(0, 0);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddValueAsLongToInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.add(0, 0L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getDifference(0, 0);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getDifferenceAsLong(0, 0);
    }
    
    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertEquals(0, field.getUnitMillis());
    }
    
    @Test
    public void testCompareTo() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        
        // Should return -1 when comparing with a supported duration field
        DurationFieldType supportedType = DurationFieldType.hours();
        DurationFieldType unsupportedType = DurationFieldType.seconds();
        DurationField supportedField = DurationFieldType.hours().getField(null);
        
        assertTrue(field.compareTo(supportedField) < 0);
        
        // Should return 0 when comparing with an unsupported duration field
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(unsupportedType);
        
        assertEquals(0, field.compareTo(unsupportedField));
    }
    
    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        // Should return true when comparing with itself
        assertTrue(field.equals(field));
        
        // Should return true when comparing with another instance of UnsupportedDurationField with the same type
        UnsupportedDurationField sameField = UnsupportedDurationField.getInstance(type);
        assertTrue(field.equals(sameField));
        
        // Should return true when comparing with another instance of UnsupportedDurationField with null type
        UnsupportedDurationField nullTypeField = UnsupportedDurationField.getInstance(null);
        assertTrue(field.equals(nullTypeField));
        
        // Should return false when comparing with other object types
        assertFalse(field.equals(5));
        assertFalse(field.equals("unsupported"));
    }
    
    @Test
    public void testHashCode() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertEquals(field.getName().hashCode(), field.hashCode());
    }
    
    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[" + type.getName() + "]", field.toString());
    }
    
    @Test
    public void testReadResolve() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        UnsupportedDurationField resolvedField = (UnsupportedDurationField) field.readResolve();
        assertEquals(field, resolvedField);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        throw field.unsupported();
    }
    
}
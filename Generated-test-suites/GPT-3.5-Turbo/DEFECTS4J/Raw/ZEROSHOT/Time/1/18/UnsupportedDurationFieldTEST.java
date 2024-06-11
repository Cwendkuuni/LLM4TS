import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest {
    
    private UnsupportedDurationField field;
    
    @Before
    public void setUp() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    }
    
    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.SECONDS, field.getType());
    }
    
    @Test
    public void testGetName() {
        assertEquals("seconds", field.getName());
    }
    
    @Test
    public void testIsSupported() {
        assertFalse(field.isSupported());
    }
    
    @Test
    public void testIsPrecise() {
        assertTrue(field.isPrecise());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_Unsupported() {
        field.getValue(1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_Unsupported() {
        field.getValueAsLong(1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_WithInstant_Unsupported() {
        field.getValue(1000L, 100L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_WithInstant_Unsupported() {
        field.getValueAsLong(1000L, 100L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_WithValue_Unsupported() {
        field.getMillis(1);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_WithValueLong_Unsupported() {
        field.getMillis(1L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_WithValueAndInstant_Unsupported() {
        field.getMillis(1, 100L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_WithValueLongAndInstant_Unsupported() {
        field.getMillis(1L, 100L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_WithInstantAndValue_Unsupported() {
        field.add(100L, 1);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_WithInstantAndValueLong_Unsupported() {
        field.add(100L, 1L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference_Unsupported() {
        field.getDifference(100L, 50L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong_Unsupported() {
        field.getDifferenceAsLong(100L, 50L);
    }
    
    @Test
    public void testGetUnitMillis() {
        assertEquals(0L, field.getUnitMillis());
    }
    
    @Test
    public void testCompareTo_SupportedField() {
        DummyDurationField supportedField = new DummyDurationField(DurationFieldType.MINUTES);
        assertEquals(1, field.compareTo(supportedField));
    }
    
    @Test
    public void testCompareTo_UnsupportedField() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        assertEquals(0, field.compareTo(unsupportedField));
    }
    
    @Test
    public void testEquals_SameObject() {
        assertTrue(field.equals(field));
    }
    
    @Test
    public void testEquals_SameTypeAndName() {
        UnsupportedDurationField sameField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertTrue(field.equals(sameField));
    }
    
    @Test
    public void testEquals_DifferentType() {
        UnsupportedDurationField differentField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        assertFalse(field.equals(differentField));
    }
    
    @Test
    public void testEquals_DifferentName() {
        UnsupportedDurationField differentField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS_IGNORING_OVERFLOW);
        assertFalse(field.equals(differentField));
    }
    
    @Test
    public void testHashCode_SameTypeAndName() {
        UnsupportedDurationField sameField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertEquals(sameField.hashCode(), field.hashCode());
    }
    
    @Test
    public void testHashCode_DifferentType() {
        UnsupportedDurationField differentField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        assertNotEquals(differentField.hashCode(), field.hashCode());
    }
    
    @Test
    public void testHashCode_DifferentName() {
        UnsupportedDurationField differentField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS_IGNORING_OVERFLOW);
        assertNotEquals(differentField.hashCode(), field.hashCode());
    }
    
    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[seconds]", field.toString());
    }
    
    @Test
    public void testReadResolve() {
        UnsupportedDurationField resolvedField = (UnsupportedDurationField) field.readResolve();
        assertEquals(field.getName(), resolvedField.getName());
    }
    
    private class DummyDurationField extends DurationField {

        private DurationFieldType type;
        
        public DummyDurationField(DurationFieldType type) {
            this.type = type;
        }
        
        @Override
        public DurationFieldType getType() {
            return type;
        }
        
        // Other abstract methods
        
    }
}
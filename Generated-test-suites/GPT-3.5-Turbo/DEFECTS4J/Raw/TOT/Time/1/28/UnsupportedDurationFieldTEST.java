import org.joda.time.DurationFieldType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {
    
    private UnsupportedDurationField field;
    
    @Before
    public void setUp() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
    }
    
    /**
     * Test case for getType() method.
     * Type should be DurationFieldType.millis().
     */
    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.millis(), field.getType());
    }
    
    /**
     * Test case for getName() method.
     * Name should be "millis".
     */
    @Test
    public void testGetName() {
        assertEquals("millis", field.getName());
    }
    
    /**
     * Test case for isSupported() method.
     * isSupported() should return false.
     */
    @Test
    public void testIsSupported() {
        assertFalse(field.isSupported());
    }
    
    /**
     * Test case for isPrecise() method.
     * isPrecise() should return true.
     */
    @Test
    public void testIsPrecise() {
        assertTrue(field.isPrecise());
    }
    
    /**
     * Test case for getValue() method.
     * getValue() should throw UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        field.getValue(1000);
    }
    
    /**
     * Test case for getValueAsLong() method.
     * getValueAsLong() should throw UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        field.getValueAsLong(1000);
    }
    
    /**
     * Test case for getValue() method.
     * getValue() should throw UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        field.getValue(1000, 1000);
    }
    
    /**
     * Test case for getValueAsLong() method.
     * getValueAsLong() should throw UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        field.getValueAsLong(1000, 1000);
    }
    
    /**
     * Test case for getMillis() method.
     * getMillis() should throw UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        field.getMillis(1);
    }
    
    /**
     * Test case for getMillis() method.
     * getMillis() should throw UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLong() {
        field.getMillis(1L);
    }
    
    /**
     * Test case for getMillis() method.
     * getMillis() should throw UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstant() {
        field.getMillis(1, 1000);
    }
    
    /**
     * Test case for getMillis() method.
     * getMillis() should throw UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        field.getMillis(1L, 1000);
    }
    
    /**
     * Test case for add() method.
     * add() should throw UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValue() {
        field.add(1000, 1);
    }
    
    /**
     * Test case for add() method.
     * add() should throw UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValueAsLong() {
        field.add(1000, 1L);
    }
    
    /**
     * Test case for getDifference() method.
     * getDifference() should throw UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        field.getDifference(1000, 500);
    }
    
    /**
     * Test case for getDifferenceAsLong() method.
     * getDifferenceAsLong() should throw UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        field.getDifferenceAsLong(1000, 500);
    }
    
    /**
     * Test case for getUnitMillis() method.
     * getUnitMillis() should return 0.
     */
    @Test
    public void testGetUnitMillis() {
        assertEquals(0, field.getUnitMillis());
    }
    
    /**
     * Test case for compareTo() method when durationField is supported.
     * compareTo() should return 1.
     */
    @Test
    public void testCompareToWithSupportedField() {
        DurationFieldType supportedFieldType = DurationFieldType.years();
        DurationField supportedField = UnsupportedDurationField.getInstance(supportedFieldType);
        
        assertEquals(1, field.compareTo(supportedField));
    }
    
    /**
     * Test case for compareTo() method when durationField is unsupported.
     * compareTo() should return 0.
     */
    @Test
    public void testCompareToWithUnsupportedField() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        
        assertEquals(0, field.compareTo(unsupportedField));
    }
    
    /**
     * Test case for equals() method when fields are equal.
     * equals() should return true.
     */
    @Test
    public void testEqualsWithEqualFields() {
        DurationField sameField = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        
        assertTrue(field.equals(sameField));
    }
    
    /**
     * Test case for equals() method when fields are not equal.
     * equals() should return false.
     */
    @Test
    public void testEqualsWithDifferentFields() {
        DurationField differentField = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        
        assertFalse(field.equals(differentField));
    }
    
    /**
     * Test case for hashCode() method.
     * hashCode() should return the hashCode of the name.
     */
    @Test
    public void testHashCode() {
        assertEquals("millis".hashCode(), field.hashCode());
    }
    
    /**
     * Test case for toString() method.
     * toString() should return "UnsupportedDurationField[millis]".
     */
    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[millis]", field.toString());
    }
    
    /**
     * Test case for readResolve() method.
     * readResolve() should return the cached instance of the field.
     */
    @Test
    public void testReadResolve() {
        DurationField resolvedField = field.readResolve();
        
        assertEquals(field, resolvedField);
    }
    
    /**
     * Test case for unsupported() method.
     * unsupported() should return an UnsupportedOperationException with the correct message.
     */
    @Test
    public void testUnsupported() {
        UnsupportedOperationException exception = field.unsupported();
        
        assertEquals("millis field is unsupported", exception.getMessage());
    }
    
}
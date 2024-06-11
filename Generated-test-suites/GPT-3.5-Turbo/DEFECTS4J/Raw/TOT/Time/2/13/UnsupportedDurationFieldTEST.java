import static org.junit.Assert.*;
import org.joda.time.DurationFieldType;
import org.junit.Before;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    private UnsupportedDurationField unsupportedDurationField;
    
    @Before
    public void setUp() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    }
    
    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.SECONDS, unsupportedDurationField.getType());
    }
    
    @Test
    public void testGetName() {
        assertEquals("seconds", unsupportedDurationField.getName());
    }
    
    @Test
    public void testIsSupported() {
        assertFalse(unsupportedDurationField.isSupported());
    }
    
    @Test
    public void testIsPrecise() {
        assertTrue(unsupportedDurationField.isPrecise());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        unsupportedDurationField.getValue(1234L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        unsupportedDurationField.getValueAsLong(1234L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        unsupportedDurationField.getValue(1234L, 5678L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        unsupportedDurationField.getValueAsLong(1234L, 5678L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        unsupportedDurationField.getMillis(1234);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValue() {
        unsupportedDurationField.getMillis(1234L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        unsupportedDurationField.getMillis(1234, 5678L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValueAndInstant() {
        unsupportedDurationField.getMillis(1234L, 5678L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValue() {
        unsupportedDurationField.add(5678L, 1234);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndLongValue() {
        unsupportedDurationField.add(5678L, 1234L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        unsupportedDurationField.getDifference(5678L, 1234L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        unsupportedDurationField.getDifferenceAsLong(5678L, 1234L);
    }
    
    @Test
    public void testGetUnitMillis() {
        assertEquals(0, unsupportedDurationField.getUnitMillis());
    }
    
    @Test
    public void testCompareTo() {
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertEquals(0, unsupportedDurationField.compareTo(other));
    }
    
    @Test
    public void testEquals() {
        UnsupportedDurationField other1 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        UnsupportedDurationField other2 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        assertTrue(unsupportedDurationField.equals(unsupportedDurationField));
        assertTrue(unsupportedDurationField.equals(other1));
        assertFalse(unsupportedDurationField.equals(other2));
        assertFalse(unsupportedDurationField.equals(null));
        assertFalse(unsupportedDurationField.equals(new Object()));
    }
    
    @Test
    public void testHashCode() {
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertEquals(unsupportedDurationField.hashCode(), other.hashCode());
    }
    
    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[seconds]", unsupportedDurationField.toString());
    }
  
    @Test
    public void testReadResolve() {
        UnsupportedDurationField other = unsupportedDurationField.readResolve();
        assertEquals(unsupportedDurationField, other);
    }

}
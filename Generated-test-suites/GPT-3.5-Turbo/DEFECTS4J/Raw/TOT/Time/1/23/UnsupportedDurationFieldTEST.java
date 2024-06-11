import org.joda.time.DurationFieldType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {
    
    private UnsupportedDurationField field;
    
    @Before
    public void setUp() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
    }
    
    @Test
    public void testGetInstance() {
        SupportedDurationField result = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertEquals(result.getType(), DurationFieldType.SECONDS);
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
    public void testGetValue() {
        field.getValue(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        field.getValueAsLong(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        field.getValue(1000, 123456789);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        field.getValueAsLong(1000, 123456789);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        field.getMillis(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueLong() {
        field.getMillis(1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstant() {
        field.getMillis(1000, 123456789);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstantLong() {
        field.getMillis(1000L, 123456789);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantValue() {
        field.add(123456789, 1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantValueLong() {
        field.add(123456789, 1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        field.getDifference(123456789, 987654321);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        field.getDifferenceAsLong(123456789, 987654321);
    }
    
    @Test
    public void testGetUnitMillis() {
        assertEquals(field.getUnitMillis(), 0L);
    }
    
    @Test
    public void testCompareTo_SupportedField() {
        SupportedDurationField supportedField = new SupportedDurationField(DurationFieldType.MINUTES);
        assertEquals(field.compareTo(supportedField), 1);
    }
    
    @Test
    public void testCompareTo_UnsupportedField() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.WEEKS);
        assertEquals(field.compareTo(unsupportedField), 0);
    }
    
    @Test
    public void testEquals_SameInstance() {
        assertTrue(field.equals(field));
    }
    
    @Test
    public void testEquals_SameName() {
        UnsupportedDurationField other = new UnsupportedDurationField(DurationFieldType.MILLIS);
        assertTrue(field.equals(other));
    }
    
    @Test
    public void testEquals_DifferentName() {
        UnsupportedDurationField other = new UnsupportedDurationField(DurationFieldType.SECONDS);
        assertFalse(field.equals(other));
    }
    
    @Test
    public void testHashCode_SameName() {
        UnsupportedDurationField other = new UnsupportedDurationField(DurationFieldType.MILLIS);
        assertEquals(field.hashCode(), other.hashCode());
    }
    
    @Test
    public void testHashCode_DifferentName() {
        UnsupportedDurationField other = new UnsupportedDurationField(DurationFieldType.SECONDS);
        assertNotEquals(field.hashCode(), other.hashCode());
    }
    
    @Test
    public void testToString() {
        String expected = "UnsupportedDurationField[" + DurationFieldType.MILLIS.getName() + "]";
        assertEquals(field.toString(), expected);
    }
    
    @Test
    public void testReadResolve() {
        UnsupportedDurationField resolvedField = (UnsupportedDurationField) field.readResolve();
        assertEquals(field.getType(), resolvedField.getType());
    }
    
}
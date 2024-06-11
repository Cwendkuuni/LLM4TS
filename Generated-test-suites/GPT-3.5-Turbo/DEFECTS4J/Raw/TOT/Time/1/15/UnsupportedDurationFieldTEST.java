import static org.junit.Assert.*;

import org.joda.time.DurationFieldType;
import org.joda.time.field.UnsupportedDurationField;
import org.junit.Before;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    private UnsupportedDurationField unsupportedDurationField;
    
    @Before
    public void setUp() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.days());
    }
    
    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.days(), unsupportedDurationField.getType());
    }
    
    @Test
    public void testGetName() {
        assertEquals("days", unsupportedDurationField.getName());
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
        unsupportedDurationField.getValue(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        unsupportedDurationField.getValueAsLong(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        unsupportedDurationField.getValue(1000, 2000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        unsupportedDurationField.getValueAsLong(1000, 2000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        unsupportedDurationField.getMillis(100);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLong() {
        unsupportedDurationField.getMillis(100L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        unsupportedDurationField.getMillis(100, 2000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLongAndInstant() {
        unsupportedDurationField.getMillis(100L, 2000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValue() {
        unsupportedDurationField.add(2000, 100);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValueAsLong() {
        unsupportedDurationField.add(2000, 100L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        unsupportedDurationField.getDifference(5000, 2000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        unsupportedDurationField.getDifferenceAsLong(5000, 2000);
    }
    
    @Test
    public void testGetUnitMillis() {
        assertEquals(0, unsupportedDurationField.getUnitMillis());
    }
    
    @Test
    public void testCompareToWithSupportedDurationField() {
        DurationFieldType supportedType = DurationFieldType.years();
        assertTrue(unsupportedDurationField.compareTo(UnsupportedDurationField.getInstance(supportedType)) > 0);
    }
    
    @Test
    public void testCompareToWithUnsupportedDurationField() {
        DurationFieldType unsupportedType = DurationFieldType.millis();
        assertTrue(unsupportedDurationField.compareTo(UnsupportedDurationField.getInstance(unsupportedType)) == 0);
    }
    
    @Test
    public void testEqualsWithSameInstance() {
        assertTrue(unsupportedDurationField.equals(unsupportedDurationField));
    }
    
    @Test
    public void testEqualsWithEqualInstance() {
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertTrue(unsupportedDurationField.equals(other));
    }
    
    @Test
    public void testEqualsWithDifferentType() {
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertFalse(unsupportedDurationField.equals(other));
    }
    
    @Test
    public void testEqualsWithNull() {
        assertFalse(unsupportedDurationField.equals(null));
    }
    
    @Test
    public void testHashCode() {
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertEquals(unsupportedDurationField.hashCode(), other.hashCode());
    }
    
    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[days]", unsupportedDurationField.toString());
    }
    
    @Test
    public void testReadResolve() {
        assertSame(unsupportedDurationField, unsupportedDurationField.readResolve());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testExceptionMessage() {
        throw unsupportedDurationField.unsupported();
    }
}
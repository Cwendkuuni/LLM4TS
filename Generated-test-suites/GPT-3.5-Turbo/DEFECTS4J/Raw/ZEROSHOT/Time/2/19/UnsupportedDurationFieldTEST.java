import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.DurationFieldType;
import java.util.HashMap;

public class UnsupportedDurationFieldTest {
    
    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.ERA;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertNotNull(field);
        assertEquals(type, field.getType());
    }
    
    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.ERA;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertEquals(type.getName(), field.getName());
    }
    
    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.ERA);
        assertFalse(field.isSupported());
    }
    
    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.ERA);
        assertTrue(field.isPrecise());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.ERA);
        field.getValue(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.ERA);
        field.getValueAsLong(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.ERA);
        field.getValue(1000, 2000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.ERA);
        field.getValueAsLong(1000, 2000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.ERA);
        field.getMillis(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.ERA);
        field.getMillis(1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstantAndValue() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.ERA);
        field.getMillis(1000, 2000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstantAndValueAsLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.ERA);
        field.getMillis(1000L, 2000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValue() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.ERA);
        field.add(1000, 2000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValueAsLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.ERA);
        field.add(1000L, 2000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.ERA);
        field.getDifference(1000, 2000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.ERA);
        field.getDifferenceAsLong(1000L, 2000L);
    }
    
    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.ERA);
        assertEquals(0, field.getUnitMillis());
    }
    
    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.ERA);
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.HOUR_OF_DAY);
        assertEquals(0, field1.compareTo(field1));
        assertNotEquals(0, field1.compareTo(field2));
    }
    
    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.ERA);
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.HOUR_OF_DAY);
        assertTrue(field1.equals(field1));
        assertFalse(field1.equals(field2));
        assertFalse(field1.equals(null));
    }
    
    @Test
    public void testHashCode() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.ERA);
        assertEquals(field.getName().hashCode(), field.hashCode());
    }
    
    @Test
    public void testToString() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.ERA);
        assertEquals("UnsupportedDurationField[" + field.getName() + ']', field.toString());
    }
    
    @Test
    public void testReadResolve() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.ERA);
        Object result = field.readResolve();
        assertNotNull(result);
        assertTrue(result instanceof UnsupportedDurationField);
        assertEquals(field.getName(), ((UnsupportedDurationField) result).getName());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupported() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.ERA);
        field.unsupported();
    }
    
}
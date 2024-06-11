import static org.junit.Assert.*;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        DurationFieldType type = DurationFieldType.ERAS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type, field.getType());
    }
    
    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.YEARS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type.getName(), field.getName());
    }
    
    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertFalse(field.isSupported());
    }
    
    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertTrue(field.isPrecise());
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(1000);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.MILLIS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(1000);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(1000, 2000);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        DurationFieldType type = DurationFieldType.MONTHS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(1000, 2000);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        DurationFieldType type = DurationFieldType.YEARS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(100);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testGetMillisWithValueLong() {
        DurationFieldType type = DurationFieldType.WEEKS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(100L);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(100, 200);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testGetMillisWithValueLongAndInstant() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(100L, 200L);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(1000L, 2000);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testAddLong() {
        DurationFieldType type = DurationFieldType.MILLIS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(1000L, 2000L);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifference(1000L, 2000L);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.MONTHS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifferenceAsLong(1000L, 2000L);
    }
    
    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.YEARS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(0, field.getUnitMillis());
    }
    
    @Test
    public void testCompareTo() {
        DurationFieldType type = DurationFieldType.WEEKS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(0, field1.compareTo(field2));
    }
    
    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(field1, field2);
    }
    
    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type.getName().hashCode(), field.hashCode());
    }
    
    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[seconds]", field.toString());
    }

}
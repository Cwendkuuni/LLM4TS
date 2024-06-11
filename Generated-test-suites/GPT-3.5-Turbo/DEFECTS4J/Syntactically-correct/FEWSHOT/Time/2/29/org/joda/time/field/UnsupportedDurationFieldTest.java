import org.joda.time.field.UnsupportedDurationField;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {
    
    @Test
    public void testGetInstance() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertNotNull(field1);
        
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertNotNull(field2);
        
        // test if same instance is returned for the same type
        assertSame(field1, field2);
    }
    
    @Test
    public void testGetType() {
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        assertEquals(type, field.getType());
    }
    
    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.weeks();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        assertEquals(type.getName(), field.getName());
    }
    
    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        
        assertFalse(field.isSupported());
    }
    
    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        
        assertTrue(field.isPrecise());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        
        field.getValue(1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        
        field.getValueAsLong(2000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.eras());
        
        field.getValue(1000L, 2000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        
        field.getValueAsLong(2000L, 3000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        
        field.getMillis(10);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        
        field.getMillis(5000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        
        field.getMillis(1000, 2000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        
        field.getMillis(1000L, 2000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_int() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        
        field.add(1000L, 10);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        
        field.add(5000L, 2000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        
        field.getDifference(1000L, 2000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        
        field.getDifferenceAsLong(1000L, 2000L);
    }
    
    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        
        long expected = 0L;
        long actual = field.getUnitMillis();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        
        int expected = 0;
        int actual = field1.compareTo(field2);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        
        assertTrue(field1.equals(field2));
        assertFalse(field1.equals(field3));
    }
    
    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        
        int hashcode1 = field1.hashCode();
        int hashcode2 = field2.hashCode();
        
        assertEquals(hashcode1, hashcode2);
    }
    
    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        assertEquals("UnsupportedDurationField[" + type.getName() + ']', field.toString());
    }
}
import org.joda.time.DurationFieldType;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.YEAR_TYPE);
        
        assertNotNull(field1);
        assertNotNull(field2);
        assertSame(field1, field2);
    }
    
    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MONTH_0_TYPE);
        assertSame(DurationFieldType.MONTH_0_TYPE, field.getType());
    }
    
    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_WEEK_TYPE);
        assertEquals("dayOfWeek", field.getName());
    }
    
    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_YEAR_TYPE);
        assertFalse(field.isSupported());
    }
    
    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.HOUR_OF_DAY_TYPE);
        assertTrue(field.isPrecise());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_Long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_DAY_TYPE);
        field.getValue(1000000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_Long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND_OF_DAY_TYPE);
        field.getValueAsLong(1000000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_Instant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS_OF_DAY_TYPE);
        field.getValue(1000000L, 1234567890L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_Instant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_HOUR_TYPE);
        field.getValueAsLong(1000L, 1234567890L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_Int() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND_OF_MINUTE_TYPE);
        field.getMillis(60);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_Long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS_TYPE);
        field.getMillis(1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_Int_Instant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_HOUR_TYPE);
        field.getMillis(60, 1234567890L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_Long_Instant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND_OF_DAY_TYPE);
        field.getMillis(1000000L, 1234567890L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_Long_Int() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_HOUR_TYPE);
        field.add(1234567890L, 60);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_Long_Long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECOND_OF_DAY_TYPE);
        field.add(1234567890L, 1000000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS_OF_SECOND_TYPE);
        field.getDifference(1234567890L, 1234567889L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_DAY_TYPE);
        field.getDifferenceAsLong(1234567890L, 1234567800L);
    }
    
    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.HOUR_OF_DAY_TYPE);
        assertEquals(0L, field.getUnitMillis());
    }
    
    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.YEAR_TYPE);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.YEAR_OF_ERA_TYPE);
        
        assertEquals(1, field1.compareTo(field2));
        assertEquals(0, field2.compareTo(field1));
    }
    
    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH_TYPE);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH_TYPE);
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.HOUR_TYPE);
        
        assertEquals(field1, field1);
        assertEquals(field1, field2);
        assertNotEquals(field1, field3);
        assertNotEquals(field1, null);
        assertNotEquals(field1, "invalid");
    }
    
    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS_TYPE);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS_TYPE);
        
        assertEquals(field1.hashCode(), field2.hashCode());
    }
    
    @Test
    public void testToString() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES_TYPE);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES_TYPE);
        
        assertEquals("UnsupportedDurationField[minutes]", field1.toString());
        assertEquals(field1.toString(), field2.toString());
    }
}
import static org.junit.Assert.*;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        // Test typical use case
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertNotNull(field);
    }

    @Test
    public void testGetters() {
        // Test getName method
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        assertEquals("minutes", field.getName());
        
        // Test getType method
        assertEquals(DurationFieldType.minutes(), field.getType());
        
        // Test isSupported method
        assertFalse(field.isSupported());
        
        // Test isPrecise method
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedMethods() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        
        // Test unsupported methods throw UnsupportedOperationException
        field.getValue(10);
        field.getValueAsLong(10);
        field.getValue(10, 20);
        field.getValueAsLong(10, 20);
        field.getMillis(10);
        field.getMillis(10L);
        field.getMillis(10, 20L);
        field.getMillis(10L, 20L);
        field.add(100L, 5);
        field.add(100L, 5L);
        field.getDifference(200L, 100L);
        field.getDifferenceAsLong(200L, 100L);
        field.getUnitMillis();
        field.compareTo(field);
    }

    @Test
    public void testEquals() {
        // Test equals method
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        
        assertTrue(field1.equals(field2));
        assertFalse(field1.equals(field3));
    }

    @Test
    public void testHashCode() {
        // Test hashCode method
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        
        assertEquals(field1.hashCode(), field2.hashCode());
    }

    @Test
    public void testToString() {
        // Test toString method
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals("UnsupportedDurationField[years]", field.toString());
    }

}
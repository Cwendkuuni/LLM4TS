import static org.junit.Assert.*;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetAndGetName() {
        // Test getType method
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertEquals(DurationFieldType.days(), field.getType());
        
        // Test getName method
        assertEquals("days", field.getName());
    }

    @Test
    public void testIsSupportedAndIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        
        // Test isSupported method
        assertFalse(field.isSupported());
        
        // Test isPrecise method
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedMethods() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        
        // Test all unsupported methods
        field.getValue(1000);
        field.getValueAsLong(1000);
        field.getValue(1000, 2000);
        field.getValueAsLong(1000, 2000);
        field.getMillis(10);
        field.getMillis(10L);
        field.getMillis(10, 20L);
        field.getMillis(10L, 20L);
        field.add(1000L, 5);
        field.add(1000L, 5L);
        field.getDifference(2000L, 1000L);
        field.getDifferenceAsLong(2000L, 1000L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        
        // Test getUnitMillis method
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        
        // Test compareTo method
        assertEquals(1, field1.compareTo(field2));
        assertEquals(0, field1.compareTo(new UnsupportedDurationField(DurationFieldType.days())));
    }

    @Test
    public void testEqualsAndHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        
        // Test equals method
        assertTrue(field1.equals(field2));
        assertFalse(field1.equals(field3));
        
        // Test hashCode method
        assertEquals(field1.hashCode(), field2.hashCode());
        assertNotEquals(field1.hashCode(), field3.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        
        // Test toString method
        assertEquals("UnsupportedDurationField[days]", field.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        
        // Test readResolve method
        assertSame(field, field.readResolve());
    }
}
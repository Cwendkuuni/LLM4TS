import static org.junit.Assert.*;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        // Testing the getInstance method for a specific named field
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertNotNull(field);
        assertEquals(DurationFieldType.years(), field.getType());
    }

    @Test
    public void testGetName() {
        // Testing the getName method
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertEquals("days", field.getName());
    }

    @Test
    public void testIsSupported() {
        // Testing the isSupported method
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        assertFalse(field.isSupported());
    }

    // Additional test cases for edge cases and exception handling scenarios
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        field.getValue(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        field.add(1000, 5);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertEquals(field1, field2);
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.minutes();
        UnsupportedDurationField field1 = new UnsupportedDurationField(type);
        UnsupportedDurationField field2 = new UnsupportedDurationField(type);
        assertEquals(field1.hashCode(), field2.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertEquals("UnsupportedDurationField[seconds]", field.toString());
    }
}
import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        assertEquals(DurationFieldType.minutes(), field.getType());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertEquals("seconds", field.getName());
    }

    // Add more test cases for the remaining public methods

    // Edge cases and exception handling scenarios

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.halfdays());
        field.getValue(10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        field.getValueAsLong(20);
    }

    // Add more edge cases and exception handling scenarios for the remaining methods

}
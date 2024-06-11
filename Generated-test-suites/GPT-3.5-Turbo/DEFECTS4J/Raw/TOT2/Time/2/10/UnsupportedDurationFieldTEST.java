import static org.junit.Assert.*;
import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertEquals(field1, field2);
    }

    @Test
    public void testGetType() {
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type, field.getType());
    }

    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.hours();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("hours", field.getName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField.getInstance(DurationFieldType.minutes()).getValue(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField.getInstance(DurationFieldType.seconds()).getValueAsLong(5000L);
    }

    // Add more test cases as needed for each public method

}
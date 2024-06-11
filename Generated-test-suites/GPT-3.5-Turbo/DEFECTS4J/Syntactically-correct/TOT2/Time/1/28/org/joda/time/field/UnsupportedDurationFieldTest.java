import static org.junit.Assert.*;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        // Test getting instance with a specific type
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        assertEquals(DurationFieldType.weeks(), field1.getType());
    }

    @Test
    public void testGetName() {
        // Test getting name of the field
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        assertEquals("minutes", field2.getName());
    }

    // Other test cases for public methods...

}
import static org.junit.Assert.*;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    // Test for getType method
    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertNotNull(field.getType());
    }

    // Test for getName method
    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertEquals("days", field.getName());
    }

    // Test for isSupported method
    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertFalse(field.isSupported());
    }

    // Add additional test cases for other public methods following the same structure

}
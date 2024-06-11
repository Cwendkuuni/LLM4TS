import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    // Test for getType method
    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertEquals(DurationFieldType.days(), field.getType());
    }

    // Test for getName method
    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        assertEquals("minutes", field.getName());
    }

    // Test for isSupported method
    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertFalse(field.isSupported());
    }

    // Test for isPrecise method
    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertTrue(field.isPrecise());
    }

    // Add more test cases for other methods as needed...

}
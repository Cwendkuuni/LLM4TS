import static org.junit.Assert.*;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        // Typical Use Case
        assertNotNull(UnsupportedDurationField.getInstance(DurationFieldType.seconds()));

        // Edge Case - Null Type
        assertNull(UnsupportedDurationField.getInstance(null));
    }

    @Test
    public void testGetType() {
        // Typical Use Case
        assertEquals(DurationFieldType.seconds(), UnsupportedDurationField.getInstance(DurationFieldType.seconds()).getType());
    }

    @Test
    public void testGetName() {
        // Typical Use Case
        assertEquals("seconds", UnsupportedDurationField.getInstance(DurationFieldType.seconds()).getName());
    }

    @Test
    public void testIsSupported() {
        // Error Scenario
        assertFalse(UnsupportedDurationField.getInstance(DurationFieldType.seconds()).isSupported());
    }

    @Test
    public void testIsPrecise() {
        // Typical Use Case
        assertTrue(UnsupportedDurationField.getInstance(DurationFieldType.seconds()).isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField.getInstance(DurationFieldType.seconds()).getValue(0);
    }

    // Add more test methods for other public methods in UnsupportedDurationField class

}
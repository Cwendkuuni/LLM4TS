import static org.junit.Assert.*;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    // Test getInstance method with valid argument
    @Test
    public void testGetInstance_valid() {
        assertNotNull(UnsupportedDurationField.getInstance(DurationFieldType.seconds()));
    }

    // Test getType method
    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertEquals(DurationFieldType.seconds(), field.getType());
    }

    // Test isSupported method
    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertFalse(field.isSupported());
    }

    // Test isPrecise method
    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertTrue(field.isPrecise());
    }

    // Edge case: Test getValue method with invalid duration
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_invalidDuration() {
        UnsupportedDurationField.getInstance(DurationFieldType.seconds()).getValue(-1L);
    }

    // Test compareTo method with supported duration field
    @Test
    public void testCompareTo_supportedField() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        DurationField supportedField = new PreciseDurationField(DurationFieldType.minutes());
        assertTrue(unsupportedField.compareTo(supportedField) == 1);
    }

}
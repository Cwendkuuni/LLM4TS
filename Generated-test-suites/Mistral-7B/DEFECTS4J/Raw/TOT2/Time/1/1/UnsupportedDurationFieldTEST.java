import org.junit.Test;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(DurationFieldType.DAYS, unsupportedField.getType());
    }

    @Test
    public void testgetName() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals("DAYS", unsupportedField.getName());
    }

    @Test
    public void testIsSupportedWithSupportedField() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        assertFalse(unsupportedField.isSupported());

        DurationField supportedField = DurationField.standardMinutes();
        assertTrue(supportedField.isSupported());

        try {
            unsupportedField.isSupported();
        } catch (AssertionError e) {
            throw new UnsupportedOperationException("Expected UnsupportedOperationException but got " + e.getMessage());
        }
    }

    // Add more test cases for other methods, edge cases, and exceptions here...

}
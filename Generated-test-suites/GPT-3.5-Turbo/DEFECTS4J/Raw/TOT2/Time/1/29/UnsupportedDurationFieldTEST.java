
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        // Test case for getInstance method
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertNotNull(field);
        assertEquals(type, field.getType());
    }

    @Test
    public void testGetValue_WithNegativeDuration() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        try {
            field.getValue(-10);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // Expected
        }
    }

    // Add more test cases for other methods and identified scenarios

}

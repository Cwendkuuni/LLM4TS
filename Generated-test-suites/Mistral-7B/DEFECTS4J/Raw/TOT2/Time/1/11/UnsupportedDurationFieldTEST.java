import org.junit.Test;
import org.junit.Assert.*;
import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        // Test 1
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertEquals(field1, field2);

        // Test 2
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(null);
        assertNull(field3);

        // Test edge cases
        UnsupportedDurationField field4 = UnsupportedDurationField.getInstance(DurationFieldType.Millennia());
        UnsupportedDurationField field5 = UnsupportedDurationField.getInstance(DurationFieldType.Microseconds());
        assertNotEquals(field4, field5);
    }

    // Add other tests here...

}
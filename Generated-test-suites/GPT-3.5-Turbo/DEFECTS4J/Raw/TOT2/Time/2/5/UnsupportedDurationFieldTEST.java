import static org.junit.Assert.*;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        // Typical use case
        assertNotNull(UnsupportedDurationField.getInstance(DurationFieldType.seconds()));

        // Edge case: null type
        assertNull(UnsupportedDurationField.getInstance(null));
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes);

        // Typical use case
        assertEquals("minutes", field.getName());
    }

    // Add more test cases for other methods following the same pattern

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.hours());

        // Typical use case
        assertTrue(field1.equals(field2));

        // Edge case: compare with different type
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertFalse(field1.equals(field3));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.days());

        // Typical use case
        assertEquals(field1.hashCode(), field2.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());

        // Typical use case
        assertEquals("UnsupportedDurationField[weeks]", field.toString());
    }
}
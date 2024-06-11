import org.joda.time.DurationFieldType;
import org.joda.time.field.UnsupportedDurationField;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertEquals(DurationFieldType.days(), field.getType());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertEquals("months", field.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        assertTrue(field.isPrecise());
    }

    // Add more test cases for other methods if needed

}
import org.junit.Test;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedDurationField;

import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

    @Test
    public void testUnsupportedField() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);

        // Test getters
        assertEquals("UnsupportedDurationField[Milliseconds]", field.toString());
        assertEquals(DurationFieldType.MILLISECONDS, field.getType());
        assertEquals("milliseconds", field.getName());

        // Test isSupported
        assertFalse(field.isSupported());

        // Test value methods
        assertThrows(UnsupportedOperationException.class, () -> field.getValue(1L));
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(1L));
        assertThrows(UnsupportedOperationException.class, () -> field.getValue(1L, 1L));
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(1L, 1L));

        // Test millis methods
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1));
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1L));
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1, 1L));
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1L, 1L));

        // Test add and getDifference methods
        assertThrows(UnsupportedOperationException.class, () -> field.add(1L, 1L));
        assertThrows(UnsupportedOperationException.class, () -> field.add(1L, 1));
        assertThrows(UnsupportedOperationException.class, () -> field.getDifference(1L, 1L));
        assertThrows(UnsupportedOperationException.class, () -> field.getDifferenceAsLong(1L, 1L));

        // Test compareTo method
        assertEquals(0, field.compareTo(field));
    }
}
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedDurationField;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for the UnsupportedDurationField class.
 */
public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.weeks();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type, field.getType());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.days());
        assertFalse(field.isSupported());
    }

    @Test
    public void testGetValue() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.days());
        assertThrows(UnsupportedOperationException.class, () -> field.getValue(1));
    }

    @Test
    public void testGetValueAsLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.days());
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(1));
    }

    @Test
    public void testAdd() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.days());
        assertThrows(UnsupportedOperationException.class, () -> field.add(0, 1));
    }

    @Test
    public void testGetDifference() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.days());
        assertThrows(UnsupportedOperationException.class, () -> field.getDifference(0, 1));
    }

    @Test
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.days());
        assertThrows(UnsupportedOperationException.class, () -> field.getDifferenceAsLong(0, 1));
    }
}
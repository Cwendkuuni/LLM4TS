import org.junit.Test;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedDurationField;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

    @Test
    public void testUnsupportedFieldInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);

        assertEquals(DurationFieldType.DAYS, unsupportedField.getType());
        assertEquals("days", unsupportedField.getName());
        assertFalse(unsupportedField.isSupported());
        assertTrue(unsupportedField.isPrecise());
    }

    @Test
    public void testUnsupportedValueGetters() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);

        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValue(1));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValueAsLong(1));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValue(1, 1));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValueAsLong(1, 1));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1L));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1, 1));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1L, 1));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(1, 1));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(1L, 1));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(1, 1L));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(1L, 1L));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifference(1, 1));
        assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifferenceAsLong(1, 1));
        assertEquals(0, unsupportedField.getUnitMillis());
    }

    @Test
    public void testUnsupportedCompareTo() {
        DurationFieldType type1 = DurationFieldType.HOURS;
        DurationFieldType type2 = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type2);

        assertEquals(1, unsupportedField1.compareTo(unsupportedField2));
    }

    @Test
    public void testUnsupportedEquals() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type);

        assertEquals(unsupportedField1, unsupportedField2);
    }
}
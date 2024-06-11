import org.joda.time.DurationFieldType;
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
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        assertEquals("Weeks", field.getName());
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

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getValue(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.getValueAsLong(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_withInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        field.getValue(1000, 100000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_withInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        field.getValueAsLong(1000, 100000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_withValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getMillis(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_withLongValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        field.getMillis(1000000000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_withValueAndInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        field.getMillis(5, 1000000000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_withLongValueAndInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        field.getMillis(2L, 1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_withInstantAndValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        field.add(1000000000L, 5);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_withInstantAndLongValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        field.add(1000000000L, 2L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        field.getDifference(1000000000L, 500000000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        field.getDifferenceAsLong(1000000000L, 500000000L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertEquals(0L, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testEquals_sameInstance() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertTrue(field.equals(field));
    }

    @Test
    public void testEquals_notInstanceOfUnsupportedDurationField() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertFalse(field.equals(DurationFieldType.months()));
    }

    @Test
    public void testEquals_sameName() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        assertTrue(field1.equals(field2));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertEquals("Seconds".hashCode(), field.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertEquals("UnsupportedDurationField[Seconds]", field.toString());
    }
}
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {
    @Test
    public void testGetType() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertEquals(DurationFieldType.days(), field.getType());
    }

    @Test
    public void testGetName() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertEquals("hours", field.getName());
    }

    @Test
    public void testIsSupported() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.getValue(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getValueAsLong(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        field.getValue(1000, 2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        field.getValueAsLong(1000, 2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        field.getMillis(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLong() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        field.getMillis(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        field.getMillis(100, 200);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLongAndInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getMillis(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        field.add(1000, 2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithLongValue() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.add(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        field.getDifference(1000, 2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        field.getDifferenceAsLong(1000, 2000);
    }

    @Test
    public void testGetUnitMillis() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        DurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        DurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        DurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertTrue(field1.equals(field2));
    }

    @Test
    public void testHashCode() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertEquals(field.getName().hashCode(), field.hashCode());
    }

    @Test
    public void testToString() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertEquals("UnsupportedDurationField[days]", field.toString());
    }
}
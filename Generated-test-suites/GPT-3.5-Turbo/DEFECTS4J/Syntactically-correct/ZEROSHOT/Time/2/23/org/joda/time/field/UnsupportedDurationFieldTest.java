import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertEquals(DurationFieldType.days(), field.getType());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertEquals(DurationFieldType.hours().getName(), field.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        long duration = 1000;
        field.getValue(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        long duration = 1000;
        field.getValueAsLong(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        long duration = 1000;
        long instant = 2000;
        field.getValue(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.eras());
        long duration = 1000;
        long instant = 2000;
        field.getValueAsLong(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        int value = 100;
        field.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.quarters());
        long value = 100;
        field.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        int value = 100;
        long instant = 2000;
        field.getMillis(value, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueLongAndInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        long value = 100;
        long instant = 2000;
        field.getMillis(value, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithValueAndInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        long instant = 1000;
        int value = 10;
        field.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithValueLongAndInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        long instant = 1000;
        long value = 10;
        field.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        long minuendInstant = 1000;
        long subtrahendInstant = 500;
        field.getDifference(minuendInstant, subtrahendInstant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        long minuendInstant = 1000;
        long subtrahendInstant = 500;
        field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertTrue(field1.equals(field2));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        assertEquals(field1.hashCode(), field2.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertEquals("UnsupportedDurationField[seconds]", field.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weekdays());
        assertSame(field, field.readResolve());
    }

}
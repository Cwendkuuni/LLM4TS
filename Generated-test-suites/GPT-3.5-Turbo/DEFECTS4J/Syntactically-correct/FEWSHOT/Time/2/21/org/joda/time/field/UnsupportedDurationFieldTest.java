import static org.junit.Assert.assertEquals;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.days(); // example duration field type
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("Days", field.getName());
    }

    @Test
    public void testGetType() {
        DurationFieldType type = DurationFieldType.hours(); // example duration field type
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type, field.getType());
    }

    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.minutes(); // example duration field type
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("Minutes", field.getName());
    }

    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.seconds(); // example duration field type
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(false, field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.months(); // example duration field type
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(true, field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueLong() {
        DurationFieldType type = DurationFieldType.weeks(); // example duration field type
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.years(); // example duration field type
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueLongLong() {
        DurationFieldType type = DurationFieldType.centuries(); // example duration field type
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(5000L, 10000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongLong() {
        DurationFieldType type = DurationFieldType.years(); // example duration field type
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(1234L, 5678L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisInt() {
        DurationFieldType type = DurationFieldType.months(); // example duration field type
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(123);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLong() {
        DurationFieldType type = DurationFieldType.days(); // example duration field type
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(123L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisIntLong() {
        DurationFieldType type = DurationFieldType.weeks(); // example duration field type
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1234, 5678L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLongLong() {
        DurationFieldType type = DurationFieldType.hours(); // example duration field type
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1234L, 5678L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLongInt() {
        DurationFieldType type = DurationFieldType.minutes(); // example duration field type
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(123L, 456);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLongLong() {
        DurationFieldType type = DurationFieldType.seconds(); // example duration field type
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(123L, 456L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.millis(); // example duration field type
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifference(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.millis(); // example duration field type
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifferenceAsLong(1000L, 2000L);
    }

    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.millis(); // example duration field type
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(0L, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type = DurationFieldType.millis(); // example duration field type
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.millis(); // example duration field type
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(true, field1.equals(field2));
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.millis(); // example duration field type
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type.getName().hashCode(), field.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.millis(); // example duration field type
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[millis]", field.toString());
    }

}
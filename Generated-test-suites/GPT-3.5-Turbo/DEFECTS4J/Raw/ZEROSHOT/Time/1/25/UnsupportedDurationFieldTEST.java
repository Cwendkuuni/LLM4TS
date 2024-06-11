import org.joda.time.DurationFieldType;
import org.joda.time.field.UnsupportedDurationField;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void getType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals(DurationFieldType.years(), field.getType());
    }

    @Test
    public void getName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertEquals("months", field.getName());
    }

    @Test
    public void isSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        assertFalse(field.isSupported());
    }

    @Test
    public void isPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        field.getValue(10L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        field.getValueAsLong(20L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getValueWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getValue(30L, 100L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getValueAsLongWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.getValueAsLong(40L, 200L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getMillisWithValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.getMillis(50);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getMillisWithLongValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getMillis(60L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getMillisWithValueAndInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        field.getMillis(70, 300L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getMillisWithLongValueAndInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        field.getMillis(80L, 400L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void add() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.micros());
        field.add(500L, 90);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addWithLongValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.add(600L, 100L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getDifference(700L, 800L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        field.getDifferenceAsLong(900L, 1000L);
    }

    @Test
    public void getUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void compareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertEquals(1, field1.compareTo(field2));
        assertEquals(0, field2.compareTo(field1));
    }

    @Test
    public void equals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertTrue(field1.equals(field2));
    }

    @Test
    public void hashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertEquals(field1.hashCode(), field2.hashCode());
    }

    @Test
    public void toStringTest() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.micros());
        assertEquals("UnsupportedDurationField[micros]", field.toString());
    }
}
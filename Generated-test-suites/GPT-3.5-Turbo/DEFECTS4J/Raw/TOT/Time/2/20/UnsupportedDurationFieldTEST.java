import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest {

    UnsupportedDurationField field;

    @Test
    public void testGetInstance() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertNotNull(field);
    }

    @Test
    public void testGetType() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertEquals(DurationFieldType.months(), field.getType());
    }

    @Test
    public void testGetName() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        assertEquals(DurationFieldType.weeks().getName(), field.getName());
    }

    @Test
    public void testIsSupported() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        field.getValue(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getValueAsLong(2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.getValue(3000, 1500);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.milliseconds());
        field.getValueAsLong(4000, 2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        field.getMillis(5);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueLong() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getMillis(6L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        field.getMillis(7, 3000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueLongAndInstant() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.milliseconds());
        field.getMillis(8L, 4000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValue() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        field.add(1000L, 10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValueLong() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.add(2000L, 100L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        field.getDifference(1500L, 1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        field.getDifferenceAsLong(2000L, 1000L);
    }

    @Test
    public void testGetUnitMillis() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        assertEquals(0, field.compareTo(new UnsupportedDurationField(DurationFieldType.weeks())));
    }
}
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {
    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(DurationFieldType.DAYS, field.getType());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertEquals("hours", field.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertFalse(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.getValue(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.getValueAsLong(60);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        field.getValue(24, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        field.getValueAsLong(60, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithIntValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.getMillis(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.getMillis(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithIntValueAndInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        field.getMillis(24, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValueAndInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        field.getMillis(60L, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndIntValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.add(0, 60);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndLongValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.add(0, 1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        field.getDifference(100, 50);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        field.getDifferenceAsLong(100, 50);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertEquals(0, field.getUnitMillis());
    }
  
    @Test
    public void testCompareTo() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertEquals(0, field.compareTo(other));
    }
}
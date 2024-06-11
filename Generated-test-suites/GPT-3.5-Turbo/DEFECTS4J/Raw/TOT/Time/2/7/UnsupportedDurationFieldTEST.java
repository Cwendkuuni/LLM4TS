import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        assertEquals(DurationFieldType.YEARS, field.getType());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MONTHS);
        assertEquals("Months", field.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.WEEKS);
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertTrue(field.isPrecise());
    }

    @Test
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        try {
            field.getValue(1000L);
            fail("Expected UnsupportedOperationException was not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Hours field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        try {
            field.getValueAsLong(1000L);
            fail("Expected UnsupportedOperationException was not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Minutes field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetValueWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        try {
            field.getValue(1000L, 12345L);
            fail("Expected UnsupportedOperationException was not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Seconds field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        try {
            field.getValueAsLong(1000L, 12345L);
            fail("Expected UnsupportedOperationException was not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Millis field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetMillisWithValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        try {
            field.getMillis(100);
            fail("Expected UnsupportedOperationException was not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Milliseconds field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetMillisWithValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        try {
            field.getMillis(1000L);
            fail("Expected UnsupportedOperationException was not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Seconds field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetMillisWithInstantAndValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        try {
            field.getMillis(100, 12345L);
            fail("Expected UnsupportedOperationException was not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Minutes field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetMillisWithInstantAndValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        try {
            field.getMillis(1000L, 12345L);
            fail("Expected UnsupportedOperationException was not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Hours field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testAddWithInstantAndValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        try {
            field.add(1000L, 123);
            fail("Expected UnsupportedOperationException was not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Days field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testAddWithInstantAndValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MONTHS);
        try {
            field.add(1000L, 1234L);
            fail("Expected UnsupportedOperationException was not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Months field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        try {
            field.getDifference(1000L, 500L);
            fail("Expected UnsupportedOperationException was not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Seconds field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        try {
            field.getDifferenceAsLong(1000L, 500L);
            fail("Expected UnsupportedOperationException was not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Minutes field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertEquals(0, field1.compareTo(field2));
    }

}
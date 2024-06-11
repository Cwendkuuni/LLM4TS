package org.joda.time.field;
import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest {

    private UnsupportedDurationField unsupportedDurationField;

    @Test
    public void testGetInstance() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertNotNull(unsupportedDurationField);
        assertEquals(DurationFieldType.years(), unsupportedDurationField.getType());
    }

    @Test
    public void testIsSupported() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertFalse(unsupportedDurationField.isSupported());
    }

    @Test
    public void testIsPrecise() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertTrue(unsupportedDurationField.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.years());
        unsupportedDurationField.getValue(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.years());
        unsupportedDurationField.getValueAsLong(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.years());
        unsupportedDurationField.getValue(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.years());
        unsupportedDurationField.getValueAsLong(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.years());
        unsupportedDurationField.getMillis(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstant() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.years());
        unsupportedDurationField.getMillis(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValue() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.years());
        unsupportedDurationField.add(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.years());
        unsupportedDurationField.getDifference(0, 0);
    }

    @Test
    public void testGetUnitMillis() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals(0, unsupportedDurationField.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals(1, unsupportedDurationField.compareTo(UnsupportedDurationField.getInstance(DurationFieldType.hours())));
        assertEquals(0, unsupportedDurationField.compareTo(UnsupportedDurationField.getInstance(DurationFieldType.years())));
    }

    @Test
    public void testEquals() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertTrue(unsupportedDurationField.equals(unsupportedDurationField));
        assertFalse(unsupportedDurationField.equals(null));
        assertFalse(unsupportedDurationField.equals(new Object()));
        assertFalse(unsupportedDurationField.equals(UnsupportedDurationField.getInstance(DurationFieldType.hours())));
        assertTrue(unsupportedDurationField.equals(UnsupportedDurationField.getInstance(DurationFieldType.years())));
    }

    @Test
    public void testHashCode() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals(unsupportedDurationField.getName().hashCode(), unsupportedDurationField.hashCode());
    }

    @Test
    public void testToString() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals("UnsupportedDurationField[year]", unsupportedDurationField.toString());
    }
}
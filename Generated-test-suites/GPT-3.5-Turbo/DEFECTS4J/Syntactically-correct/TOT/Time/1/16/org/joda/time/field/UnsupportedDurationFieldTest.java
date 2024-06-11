import org.junit.Before;
import org.junit.Test;
import org.joda.time.DurationFieldType;

import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    private UnsupportedDurationField unsupportedDurationField;

    @Before
    public void setUp() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.hours());
    }

    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.hours(), unsupportedDurationField.getType());
    }

    @Test
    public void testGetName() {
        assertEquals("hours", unsupportedDurationField.getName());
    }

    @Test
    public void testIsSupported() {
        assertFalse(unsupportedDurationField.isSupported());
    }

    @Test
    public void testIsPrecise() {
        assertTrue(unsupportedDurationField.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        unsupportedDurationField.getValue(10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        unsupportedDurationField.getValueAsLong(10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        unsupportedDurationField.getValue(10, 1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        unsupportedDurationField.getValueAsLong(10, 1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        unsupportedDurationField.getMillis(5);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLong() {
        unsupportedDurationField.getMillis(10L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        unsupportedDurationField.getMillis(5, 1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLongAndInstant() {
        unsupportedDurationField.getMillis(10L, 1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithValue() {
        unsupportedDurationField.add(1000, 5);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithValueAsLong() {
        unsupportedDurationField.add(1000, 5L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        unsupportedDurationField.getDifference(2000, 1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        unsupportedDurationField.getDifferenceAsLong(2000, 1000);
    }

    @Test
    public void testGetUnitMillis() {
        assertEquals(0, unsupportedDurationField.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertEquals(1, unsupportedDurationField.compareTo(other));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField other1 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        UnsupportedDurationField other2 = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        assertTrue(unsupportedDurationField.equals(other1));
        assertFalse(unsupportedDurationField.equals(other2));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertEquals(other.hashCode(), unsupportedDurationField.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[hours]", unsupportedDurationField.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField resolvedField = (UnsupportedDurationField) unsupportedDurationField.readResolve();
        assertEquals(unsupportedDurationField, resolvedField);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperationException() {
        throw unsupportedDurationField.unsupported();
    }
}
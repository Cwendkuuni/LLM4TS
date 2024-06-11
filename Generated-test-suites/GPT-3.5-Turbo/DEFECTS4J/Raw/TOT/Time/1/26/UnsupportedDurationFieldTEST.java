import org.junit.Before;
import org.junit.Test;

import org.joda.time.DurationFieldType;

import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    private UnsupportedDurationField unsupportedField;

    @Before
    public void setUp() {
        unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
    }

    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.SECONDS, unsupportedField.getType());
    }

    @Test
    public void testGetName() {
        assertEquals("seconds", unsupportedField.getName());
    }

    @Test
    public void testIsSupported() {
        assertFalse(unsupportedField.isSupported());
    }

    @Test
    public void testIsPrecise() {
        assertTrue(unsupportedField.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueForDuration() {
        unsupportedField.getValue(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongForDuration() {
        unsupportedField.getValueAsLong(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueForDurationAndInstant() {
        unsupportedField.getValue(1000, 1500000000000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongForDurationAndInstant() {
        unsupportedField.getValueAsLong(1000, 1500000000000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisForValue() {
        unsupportedField.getMillis(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisForValueLong() {
        unsupportedField.getMillis(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisForValueAndInstant() {
        unsupportedField.getMillis(1000, 1500000000000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisForValueLongAndInstant() {
        unsupportedField.getMillis(1000L, 1500000000000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddForInstantAndValue() {
        unsupportedField.add(1500000000000L, 1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddForInstantAndLongValue() {
        unsupportedField.add(1500000000000L, 1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceForMinuendAndSubtrahendInstant() {
        unsupportedField.getDifference(1500000000000L, 1400000000000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLongForMinuendAndSubtrahendInstant() {
        unsupportedField.getDifferenceAsLong(1500000000000L, 1400000000000L);
    }

    @Test
    public void testGetUnitMillis() {
        assertEquals(0L, unsupportedField.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertEquals(1, unsupportedField.compareTo(unsupportedField));
        assertEquals(0, unsupportedField.compareTo(unsupportedField2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertTrue(unsupportedField.equals(unsupportedField));
        assertFalse(unsupportedField.equals(null));
        assertFalse(unsupportedField.equals("UnsupportedDurationField"));
        assertFalse(unsupportedField.equals(unsupportedField2));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertEquals(unsupportedField.hashCode(), unsupportedField2.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[seconds]", unsupportedField.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertSame(unsupportedField, unsupportedField2.readResolve());
    }

}
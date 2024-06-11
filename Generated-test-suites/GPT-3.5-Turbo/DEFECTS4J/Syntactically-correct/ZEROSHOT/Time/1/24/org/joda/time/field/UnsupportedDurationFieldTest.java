import org.joda.time.DurationFieldType;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    private static UnsupportedDurationField unsupportedDurationField;

    @BeforeClass
    public static void setup() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    }

    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.seconds(), unsupportedDurationField.getType());
    }

    @Test
    public void testGetName() {
        assertEquals("seconds", unsupportedDurationField.getName());
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
        unsupportedDurationField.getValue(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        unsupportedDurationField.getValueAsLong(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        unsupportedDurationField.getValue(1000, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        unsupportedDurationField.getValueAsLong(1000, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        unsupportedDurationField.getMillis(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLong() {
        unsupportedDurationField.getMillis(100L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        unsupportedDurationField.getMillis(100, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLongAndInstant() {
        unsupportedDurationField.getMillis(100L, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValue() {
        unsupportedDurationField.add(1000, 100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValueAsLong() {
        unsupportedDurationField.add(1000, 100L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        unsupportedDurationField.getDifference(1000L, 500L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        unsupportedDurationField.getDifferenceAsLong(1000L, 500L);
    }

    @Test
    public void testGetUnitMillis() {
        assertEquals(0, unsupportedDurationField.getUnitMillis());
    }

    @Test
    public void testCompareToWithSupportedDurationField() {
        DurationField durationField = new UnsupportedDurationField(DurationFieldType.minutes());
        assertEquals(1, unsupportedDurationField.compareTo(durationField));
    }

    @Test
    public void testCompareToWithUnsupportedDurationField() {
        DurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        assertEquals(0, unsupportedDurationField.compareTo(durationField));
    }

    @Test
    public void testEqualsWithSameObject() {
        assertTrue(unsupportedDurationField.equals(unsupportedDurationField));
    }

    @Test
    public void testEqualsWithEqualFields() {
        DurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertTrue(unsupportedDurationField.equals(durationField));
    }

    @Test
    public void testEqualsWithDifferentFields() {
        DurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        assertFalse(unsupportedDurationField.equals(durationField));
    }

    @Test
    public void testEqualsWithNull() {
        assertFalse(unsupportedDurationField.equals(null));
    }

    @Test
    public void testHashCode() {
        DurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertEquals(durationField.hashCode(), unsupportedDurationField.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[seconds]", unsupportedDurationField.toString());
    }

    @Test
    public void testReadResolve() {
        assertSame(unsupportedDurationField, unsupportedDurationField.readResolve());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupported() {
        unsupportedDurationField.unsupported();
    }
}
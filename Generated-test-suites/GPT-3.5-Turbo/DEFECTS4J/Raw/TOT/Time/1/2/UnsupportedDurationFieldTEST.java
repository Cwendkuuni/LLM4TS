import org.joda.time.DurationFieldType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    private UnsupportedDurationField unsupportedDurationField;

    @Before
    public void setUp() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
    }

    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.YEARS, unsupportedDurationField.getType());
    }

    @Test
    public void testGetName() {
        assertEquals("years", unsupportedDurationField.getName());
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
        unsupportedDurationField.getValue(1000, System.currentTimeMillis());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        unsupportedDurationField.getValueAsLong(1000, System.currentTimeMillis());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        unsupportedDurationField.getMillis(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLong() {
        unsupportedDurationField.getMillis(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        unsupportedDurationField.getMillis(1000, System.currentTimeMillis());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLongAndInstant() {
        unsupportedDurationField.getMillis(1000L, System.currentTimeMillis());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithValue() {
        unsupportedDurationField.add(System.currentTimeMillis(), 1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithValueAsLong() {
        unsupportedDurationField.add(System.currentTimeMillis(), 1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        unsupportedDurationField.getDifference(System.currentTimeMillis(), System.currentTimeMillis() - 100000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        unsupportedDurationField.getDifferenceAsLong(System.currentTimeMillis(), System.currentTimeMillis() - 100000);
    }

    @Test
    public void testGetUnitMillis() {
        assertEquals(0, unsupportedDurationField.getUnitMillis());
    }

    @Test
    public void testCompareToWithSupportedField() {
        int result = unsupportedDurationField.compareTo(DurationFieldType.MILLIS);
        assertEquals(1, result);
    }

    @Test
    public void testCompareToWithUnsupportedField() {
        int result = unsupportedDurationField.compareTo(UnsupportedDurationField.getInstance(DurationFieldType.HOURS));
        assertEquals(0, result);
    }

    @Test
    public void testEquals() {
        assertFalse(unsupportedDurationField.equals(null));
        assertFalse(unsupportedDurationField.equals(DurationFieldType.MILLIS));
        assertTrue(unsupportedDurationField.equals(UnsupportedDurationField.getInstance(DurationFieldType.YEARS)));
    }

    @Test
    public void testHashCode() {
        assertEquals("years".hashCode(), unsupportedDurationField.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[years]", unsupportedDurationField.toString());
    }
}

import org.joda.time.DurationFieldType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    private UnsupportedDurationField unsupportedDurationField;

    @Before
    public void setUp() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MONTHS);
    }

    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.MONTHS, unsupportedDurationField.getType());
    }

    @Test
    public void testGetName() {
        assertEquals("months", unsupportedDurationField.getName());
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
        unsupportedDurationField.getValue(1000, 2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        unsupportedDurationField.getValueAsLong(1000, 2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        unsupportedDurationField.getMillis(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueLong() {
        unsupportedDurationField.getMillis(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        unsupportedDurationField.getMillis(1000, 2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueLongAndInstant() {
        unsupportedDurationField.getMillis(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValue() {
        unsupportedDurationField.add(1000, 2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValueLong() {
        unsupportedDurationField.add(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        unsupportedDurationField.getDifference(1000, 2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        unsupportedDurationField.getDifferenceAsLong(1000L, 2000L);
    }

    @Test
    public void testGetUnitMillis() {
        assertEquals(0, unsupportedDurationField.getUnitMillis());
    }

    @Test
    public void testCompareToWithSupportedDurationField() {
        DurationField supportedField = new PreciseDurationField(DurationFieldType.MINUTES, 60000);
        assertEquals(1, unsupportedDurationField.compareTo(supportedField));
    }

    @Test
    public void testCompareToWithUnsupportedDurationField() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertEquals(0, unsupportedDurationField.compareTo(unsupportedField));
    }

    @Test
    public void testEqualsWithSameInstance() {
        assertTrue(unsupportedDurationField.equals(unsupportedDurationField));
    }

    @Test
    public void testEqualsWithEqualFields() {
        DurationField equalField = UnsupportedDurationField.getInstance(DurationFieldType.MONTHS);
        assertTrue(unsupportedDurationField.equals(equalField));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        assertFalse(unsupportedDurationField.equals("unsupportedDurationField"));
    }

    @Test
    public void testEqualsWithDifferentField() {
        DurationField differentField = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        assertFalse(unsupportedDurationField.equals(differentField));
    }

    @Test
    public void testHashCode() {
        assertEquals("months".hashCode(), unsupportedDurationField.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[months]", unsupportedDurationField.toString());
    }

}

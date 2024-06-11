import org.joda.time.DurationFieldType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    private UnsupportedDurationField unsupportedDurationField;

    @Before
    public void setUp() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
    }

    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.minutes(), unsupportedDurationField.getType());
    }

    @Test
    public void testGetName() {
        assertEquals("minutes", unsupportedDurationField.getName());
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
    public void testGetMillisWithIntValue() {
        unsupportedDurationField.getMillis(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValue() {
        unsupportedDurationField.getMillis(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithIntValueAndInstant() {
        unsupportedDurationField.getMillis(1000, 2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValueAndInstant() {
        unsupportedDurationField.getMillis(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithIntValue() {
        unsupportedDurationField.add(1000, 2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithLongValue() {
        unsupportedDurationField.add(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        unsupportedDurationField.getDifference(1000L, 2000L);
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
        assertEquals(1, unsupportedDurationField.compareTo(DurationFieldType.millis()));
    }

    @Test
    public void testCompareToWithUnsupportedDurationField() {
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertEquals(0, unsupportedDurationField.compareTo(other));
    }

    @Test
    public void testEqualsWithSameObject() {
        assertTrue(unsupportedDurationField.equals(unsupportedDurationField));
    }

    @Test
    public void testEqualsWithEqualObjects() {
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        assertTrue(unsupportedDurationField.equals(other));
    }

    @Test
    public void testEqualsWithDifferentObjects() {
        assertFalse(unsupportedDurationField.equals(DurationFieldType.minutes()));
    }

    @Test
    public void testHashCode() {
        assertEquals("minutes".hashCode(), unsupportedDurationField.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[minutes]", unsupportedDurationField.toString());
    }

}
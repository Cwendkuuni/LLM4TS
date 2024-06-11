import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.joda.time.DurationFieldType;
import org.joda.time.field.UnsupportedDurationField;

public class UnsupportedDurationFieldTest {

    private UnsupportedDurationField unsupportedDurationField;

    @Before
    public void setUp() {
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
        unsupportedDurationField.getValue(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        unsupportedDurationField.getValueAsLong(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        unsupportedDurationField.getValue(1000L, 1234567890L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        unsupportedDurationField.getValueAsLong(1000L, 1234567890L);
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
    public void testGetMillisWithValueInstant() {
        unsupportedDurationField.getMillis(1000, 1234567890L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueLongInstant() {
        unsupportedDurationField.getMillis(1000L, 1234567890L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        unsupportedDurationField.add(1234567890L, 1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLong() {
        unsupportedDurationField.add(1234567890L, 1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        unsupportedDurationField.getDifference(1234567890L, 1234567890L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        unsupportedDurationField.getDifferenceAsLong(1234567890L, 1234567890L);
    }

    @Test
    public void testGetUnitMillis() {
        assertEquals(0L, unsupportedDurationField.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        assertEquals(0, unsupportedDurationField.compareTo(DurationFieldType.seconds()));
    }
}
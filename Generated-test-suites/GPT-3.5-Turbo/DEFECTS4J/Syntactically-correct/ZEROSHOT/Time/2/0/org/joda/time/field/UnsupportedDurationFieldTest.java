import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    private UnsupportedDurationField unsupportedDurationField;

    @Before
    public void setUp() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.millis());
    }

    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.millis(), unsupportedDurationField.getType());
    }

    @Test
    public void testGetName() {
        assertEquals("millis", unsupportedDurationField.getName());
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
        unsupportedDurationField.getValue(123);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        unsupportedDurationField.getValueAsLong(123);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        unsupportedDurationField.getValue(123, 123);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        unsupportedDurationField.getValueAsLong(123, 123);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        unsupportedDurationField.getMillis(123);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLong() {
        unsupportedDurationField.getMillis(123L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstant() {
        unsupportedDurationField.getMillis(123, 123);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        unsupportedDurationField.getMillis(123L, 123L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        unsupportedDurationField.add(123L, 123);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithLong() {
        unsupportedDurationField.add(123L, 123L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        unsupportedDurationField.getDifference(123L, 123L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        unsupportedDurationField.getDifferenceAsLong(123L, 123L);
    }

    @Test
    public void testGetUnitMillis() {
        assertEquals(0L, unsupportedDurationField.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        assertEquals(0, unsupportedDurationField.compareTo(unsupportedDurationField));
    }

    @Test
    public void testEquals() {
        assertEquals(unsupportedDurationField, unsupportedDurationField);
    }

    @Test
    public void testHashCode() {
        assertEquals(unsupportedDurationField.hashCode(), unsupportedDurationField.hashCode());
    }

    @Test
    public void testToString() {
        assertNotNull(unsupportedDurationField.toString());
    }

    @Test
    public void testReadResolve() {
        assertEquals(unsupportedDurationField, unsupportedDurationField.readResolve());
    }

    @Test
    public void testUnsupportedException() {
        try {
            unsupportedDurationField.getValue(123);
            fail("UnsupportedOperationException should have been thrown");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }
}
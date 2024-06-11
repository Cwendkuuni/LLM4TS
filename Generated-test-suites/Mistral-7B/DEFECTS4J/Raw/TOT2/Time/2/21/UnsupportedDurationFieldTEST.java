// UnsupportedDurationFieldTest.java

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.DurationFieldType;
import org.junit.Test;

/**
 * Test cases for UnsupportedDurationField.
 */
public class UnsupportedDurationFieldTest {

    // Test methods

    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        DurationFieldType type = field.getType();
        assertEquals(DurationFieldType.DAYS, type);
    }

    @Test
    public void testGetTypeWithNullArgument() {
        UnsupportedDurationField field = new UnsupportedDurationField(null);
        assertNull(field.getType());
        assertThrows(NullPointerException.class, () -> field.getName());
    }

    @Test
    public void testGetTypeWithNegativeArgument() {
        assertThrows(IllegalArgumentException.class, () -> UnsupportedDurationField.getInstance(-1L));
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals("DAYS", field.getName());
    }

    @Test
    public void testGetNameWithNullArgument() {
        UnsupportedDurationField field = new UnsupportedDurationField(null);
        assertNull(field.getName());
        assertThrows(NullPointerException.class, () -> field.getType());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.getValue(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.getValueAsLong(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithNullArgument() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(NullPointerException.class, () -> field.getValue(null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithNegativeArgument() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(IllegalArgumentException.class, () -> field.getValue(-1L));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.getDifference(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.getDifferenceAsLong(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceWithNullArgument() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(NullPointerException.class, () -> field.getDifference(null, 1L));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceWithNegativeArguments() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(IllegalArgumentException.class, () -> field.getDifference(-1L, 1L));
        assertThrows(IllegalArgumentException.class, () -> field.getDifference(1L, -1L));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithLongArgument() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.add(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithNullArgument() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(NullPointerException.class, () -> field.add(null, 1L));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithNegativeArgument() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(IllegalArgumentException.class, () -> field.add(-1L, 1L));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCompareToWithNullArgument() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(NullPointerException.class, () -> field.compareTo(null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetUnitMillis() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.getUnitMillis();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetUnitMillisWithNegativeArgument() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertThrows(IllegalArgumentException.class, () -> field.getUnitMillis(-1L));
    }

    @Test
    public void testGetInstance() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(field1, field2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInstanceWithNonSupportedType() {
        assertThrows(IllegalArgumentException.class, () -> UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS));
    }

    @Test
    public void testSingletonSerialization() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField field2 = (UnsupportedDurationField) field1.readResolve();
        assertEquals(field1, field2);
    }
}
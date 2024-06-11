
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class UnsupportedDurationFieldTest {

    private UnsupportedDurationField unsupportedDurationField;

    @Before
    public void setup() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.years());
    }

    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.years(), unsupportedDurationField.getType());
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
    public void testGetValue_long() {
        unsupportedDurationField.getValue(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long() {
        unsupportedDurationField.getValueAsLong(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long_long() {
        unsupportedDurationField.getValue(1000, 2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long_long() {
        unsupportedDurationField.getValueAsLong(1000, 2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int() {
        unsupportedDurationField.getMillis(10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long() {
        unsupportedDurationField.getMillis(10L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int_long() {
        unsupportedDurationField.getMillis(10, 20L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long_long() {
        unsupportedDurationField.getMillis(10L, 20L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_int() {
        unsupportedDurationField.add(1000L, 10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_long() {
        unsupportedDurationField.add(1000L, 10L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference_long_long() {
        unsupportedDurationField.getDifference(2000L, 1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong_long_long() {
        unsupportedDurationField.getDifferenceAsLong(2000L, 1000L);
    }

    @Test
    public void testGetUnitMillis() {
        assertEquals(0, unsupportedDurationField.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField otherField = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertTrue(unsupportedDurationField.compareTo(otherField) > 0);
        assertFalse(otherField.compareTo(unsupportedDurationField) > 0);
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField otherField1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField otherField2 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertTrue(unsupportedDurationField.equals(otherField1));
        assertFalse(unsupportedDurationField.equals(otherField2));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField otherField = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals(otherField.hashCode(), unsupportedDurationField.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[years]", unsupportedDurationField.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField resolvedField = (UnsupportedDurationField) unsupportedDurationField.readResolve();
        assertEquals(unsupportedDurationField.getType(), resolvedField.getType());
    }

    @Test
    public void testUnsupported() {
        try {
            unsupportedDurationField.unsupported();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
}

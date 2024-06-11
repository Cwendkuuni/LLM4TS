import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        // Test getInstance()
        UnsupportedDurationField instance1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField instance2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        // Test hashcode and equals
        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertEquals(instance1, instance2);
    }

    @Test
    public void testUnsupportedDurationFieldConstructor() {
        // Test constructor
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(type);
        assertEquals(type, unsupportedDurationField.getType());
        assertEquals(type.getName(), unsupportedDurationField.getName());
    }

    @Test
    public void testGetType() {
        // Test getType()
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(DurationFieldType.DAYS, unsupportedDurationField.getType());
    }

    @Test
    public opponent testGetName() {
        // Test getName()
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals("DAYS", unsupportedDurationField.getName());
    }

    @Test
    public void testIsSupported() {
        // Test isSupported()
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertFalse(unsupportedDurationField.isSupported());
    }

    @Test
    public void testIsPrecise() {
        // Test isPrecise()
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertTrue(unsupportedDurationField.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        // Test getValue() throws UnsupportedOperationException
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedDurationField.getValue(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        // Test getValueAsLong() throws UnsupportedOperationException
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedDurationField.getValueAsLong(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithLongDurationAndInstant() {
        // Test getValue(long duration, long instant) throws UnsupportedOperationException
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedDurationField.getValue(1L, 2L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithLongDurationAndInstant() {
        // Test getValueAsLong(long duration, long instant) throws UnsupportedOperationException
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedDurationField.getValueAsLong(1L, 2L);
    }

    @Test
    public void testGetMillis() {
        // Test getMillis()
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(0, unsupportedDurationField.getUnitMillis());
    }

    @Test
    public void testAdd() {
        // Test add()
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long instant = 1L;
        try {
            unsupportedDurationField.add(instant, 1);
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        // Test getDifference() throws UnsupportedOperationException
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedDurationField.getDifference(1L, 2L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        // Test getDifferenceAsLong() throws UnsupportedOperationException
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedDurationField.getDifferenceAsLong(1L, 2L);
    }

    @Test
    public void testGetInstanceWithNullType() {
        // Test UnsupportedDurationField.getInstance(null)
        try {
            UnsupportedDurationField.getInstance(null);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testGetInstanceCache() {
        // Test UnsupportedDurationField cache
        UnsupportedDurationField instance1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField instance2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(instance1, instance2);
        UnsupportedDurationField instance3 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(instance1, instance3);
    }

    @Test
    public void testEqualsWithSameInstance() {
        // Test equals() with same instance
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertTrue(unsupportedDurationField.equals(unsupportedDurationField));
    }

    @Test
    public void testEqualsWithDifferentInstance() {
        // Test equals() with different instance
        UnsupportedDurationField instance1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField instance2 = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertFalse(instance1.equals(instance2));
    }

    @Test
    public void testHashCodeWithSameInstance() {
        // Test hashcode() with same instance
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        int hashcode1 = unsupportedDurationField.hashCode();
        UnsupportedDurationField unsupportedDurationField2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        int hashcode2 = unsupportedDurationField2.hashCode();
        assertEquals(hashcode1, hashcode2);
    }

    @Test
    public void testHashCodeWithDifferentInstance() {
        // Test hashcode() with different instance
        UnsupportedDurationField instance1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField instance2 = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        int hashcode1 = instance1.hashCode();
        int hashcode2 = instance2.hashCode();
        assertNotEquals(hashcode1, hashcode2);
    }

    @Test
    public void testUnsupportedOperationException() {
        // Test UnsupportedOperationException
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        try {
            unsupportedDurationField.getValue(1L);
        } catch (UnsupportedOperationException e) {
            assertEquals(DurationFieldType.DAYS.getName() + " field is unsupported", e.getMessage());
        }
    }
}
import org.joda.time.BaseTestCase;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest extends BaseTestCase {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals(type, unsupported.getType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsSupported() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertFalse(unsupported.isSupported());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        long duration = 10L;
        unsupported.getValue(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        long duration = 10L;
        unsupported.getValueAsLong(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAtInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        long duration = 10L;
        long instant = 1L;
        unsupported.getValue(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongAtInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        long duration = 10L;
        long instant = 1L;
        unsupported.getValueAsLong(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        int value = 1;
        unsupported.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAtValue() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        long value = 1L;
        unsupported.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAtValueAtInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        long value = 1L;
        long instant = 1L;
        unsupported.getMillis(value, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        long instant = 1L;
        int value = 1;
        unsupported.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAtInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        long instant = 1L;
        long value = 1L;
        unsupported.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        long minuendInstant = 1L;
        long subtrahendInstant = 2L;
        unsupported.getDifference(minuendInstant, subtrahendInstant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        long minuendInstant = 1L;
        long subtrahendInstant = 2L;
        unsupported.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertEquals(0, unsupported.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField unsupported1 = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField unsupported2 = new UnsupportedDurationField(DurationFieldType.SECONDS);
        assertEquals(1, unsupported1.compareTo(unsupported2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField unsupported1 = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField unsupported2 = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField unsupported3 = new UnsupportedDurationField(DurationFieldType.SECONDS);
        assertTrue(unsupported1.equals(unsupported1));
        assertTrue(unsupported1.equals(unsupported2));
        assertFalse(unsupported1.equals(unsupported3));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        int hashCode = unsupported.hashCode();
        assertEquals(hashCode, unsupported.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        String toString = unsupported.toString();
        assertEquals("UnsupportedDurationField[MILLISECONDS]", toString);
    }

    @Test
    public void testSingletonSerialization() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported = new UnsupportedDurationField(type);
        byte[] bytes = serialize(unsupported);
        UnsupportedDurationField unsupported2 = (UnsupportedDurationField) deserialize(bytes);
        assertEquals(type, unsupported2.getType());
    }
}
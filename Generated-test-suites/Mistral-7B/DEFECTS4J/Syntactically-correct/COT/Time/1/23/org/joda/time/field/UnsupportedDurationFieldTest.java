import org.joda.time.BaseTestCase;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import org.junit.Test;

public class UnsupportedDurationFieldTest extends BaseTestCase {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupported, other);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsSupported() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        unsupported.isSupported();
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertEquals(DurationFieldType.DAYS.getName(), unsupported.getName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        unsupported.getValue(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        unsupported.getValueAsLong(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        unsupported.getValue(1L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        unsupported.getValueAsLong(1L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        unsupported.getMillis(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        unsupported.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        unsupported.getMillis(1, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        unsupported.add(0L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        unsupported.add(0L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        unsupported.getDifference(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        unsupported.getDifferenceAsLong(0, 0);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertEquals(0, unsupported.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        DurationField supported = new IntDurationField(1, DurationFieldType.DAYS);
        assertFalse(unsupported.compareTo(supported) < 0);
        assertTrue(unsupported.compareTo(supported) > 0);
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField unsupported1 = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedDurationField unsupported2 = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedDurationField unsupported3 = new UnsupportedDurationField(DurationFieldType.HOURS);
        assertEquals(unsupported1, unsupported1);
        assertEquals(unsupported1, unsupported2);
        assertNotSame(unsupported1, unsupported3);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertEquals(unsupported.hashCode(), unsupported.hashCode());
        UnsupportedDurationField other = new UnsupportedDurationField(DurationFieldType.HOURS);
        assertNotSame(unsupported.hashCode(), other.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertEquals("UnsupportedDurationField[DAYS]", unsupported.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedDurationField restored = (UnsupportedDurationField) unsupported.readResolve();
        assertEquals(unsupported, restored);
    }

}
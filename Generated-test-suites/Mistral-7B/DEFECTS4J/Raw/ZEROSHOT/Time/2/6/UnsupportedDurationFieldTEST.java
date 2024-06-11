import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertSame(type, unsupported.getType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 1L;
        unsupported.getValue(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 1L;
        unsupported.getValueAsLong(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsInt() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 1L;
        unsupported.getValue(duration);
        int value = unsupported.getValue(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongAsInt() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 1L;
        long value = unsupported.getValueAsLong(duration);
        int intValue = (int) value;
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        int value = 1;
        long millis = unsupported.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAsLong() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long value = 1L;
        long millis = unsupported.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long instant = 1L;
        long value = 1L;
        unsupported.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 1L;
        long subtrahendInstant = 1L;
        int difference = unsupported.getDifference(minuendInstant, subtrahendInstant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 1L;
        long subtrahendInstant = 1L;
        long difference = unsupported.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertFalse(unsupported.isSupported());
    }

    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertTrue(unsupported.isPrecise());
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.HOURS();
        DurationFieldType type2 = DurationFieldType.MINUTES();
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(0, unsupported1.compareTo(unsupported2));
    }

    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type);
        assertTrue(unsupported1.equals(unsupported2));

        UnsupportedDurationField unsupported3 = new UnsupportedDurationField(null);
        assertFalse(unsupported1.equals(unsupported3));
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupported1.hashCode(), unsupported2.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[HOURS]", unsupported.toString());
    }

    private static int compare(Object o1, Object o2) {
        return 0;
    }

    private static int assertEquals(int expected, int actual) {
        assertEquals(expected, actual);
        return 0;
    }
}
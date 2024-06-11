import org.junit.Test;
import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest {

    private UnsupportedDurationField unsupportedDurationField;

    @Test
    public void testGetInstance() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);

        // Check if the field type is correct
        assertEquals(DurationFieldType.YEARS, unsupportedDurationField.getType());

        // Check if the instance is the same for the same field type
        assertEquals(unsupportedDurationField, UnsupportedDurationField.getInstance(DurationFieldType.YEARS));

        // Check if the instance is not the same for different field types
        assertNotEquals(unsupportedDurationField, UnsupportedDurationField.getInstance(DurationFieldType.HOURS));
    }

    @Test
    public void testGetName() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);

        // Check if the field name is correct
        assertEquals("days", unsupportedDurationField.getName());
    }

    @Test
    public void testIsSupported() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);

        // Check if the field is not supported
        assertFalse(unsupportedDurationField.isSupported());
    }

    @Test
    public void testIsPrecise() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);

        // Check if the field is precise
        assertTrue(unsupportedDurationField.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);

        // Check if calling getValue() throws UnsupportedOperationException
        unsupportedDurationField.getValue(5);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);

        // Check if calling getValueAsLong() throws UnsupportedOperationException
        unsupportedDurationField.getValueAsLong(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);

        // Check if calling getValue() with instant throws UnsupportedOperationException
        unsupportedDurationField.getValue(10, 2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);

        // Check if calling getValueAsLong() with instant throws UnsupportedOperationException
        unsupportedDurationField.getValueAsLong(3, 5000);
    }

    @Test
    public void testGetMillisWithValue() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);

        // Check if calling getMillis() with simple value returns zero
        assertEquals(0, unsupportedDurationField.getMillis(10));
    }

    @Test
    public void testGetMillisWithValueAsLong() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);

        // Check if calling getMillis() with long value returns zero
        assertEquals(0, unsupportedDurationField.getMillis(1000L));
    }

    @Test
    public void testGetMillisWithInstant() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);

        // Check if calling getMillis() with instant returns zero
        assertEquals(0, unsupportedDurationField.getMillis(5, 5000));
    }

    @Test
    public void testGetMillisWithInstantAsLong() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);

        // Check if calling getMillis() with long instant returns zero
        assertEquals(0, unsupportedDurationField.getMillis(1L, 10000L));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValue() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);

        // Check if calling add() with instant and value throws UnsupportedOperationException
        unsupportedDurationField.add(10000L, 2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValueAsLong() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MONTHS);

        // Check if calling add() with instant and long value throws UnsupportedOperationException
        unsupportedDurationField.add(3000L, 5L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);

        // Check if calling getDifference() throws UnsupportedOperationException
        unsupportedDurationField.getDifference(2000L, 1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);

        // Check if calling getDifferenceAsLong() throws UnsupportedOperationException
        unsupportedDurationField.getDifferenceAsLong(5000L, 1000L);
    }

    @Test
    public void testGetUnitMillis() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);

        // Check if the unit millis is zero
        assertEquals(0, unsupportedDurationField.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);

        // Check if comparing with supported field returns 1
        assertEquals(1, unsupportedDurationField.compareTo(DurationFieldType.SECONDS));

        // Check if comparing with unsupported field returns 0
        assertEquals(0, unsupportedDurationField.compareTo(DurationFieldType.DAYS));
    }

    @Test
    public void testEquals() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);

        // Check if the field is equal to itself
        assertTrue(unsupportedDurationField.equals(unsupportedDurationField));

        // Check if the field is equal to another field of the same type
        assertTrue(unsupportedDurationField.equals(UnsupportedDurationField.getInstance(DurationFieldType.MINUTES)));

        // Check if the field is not equal to another field of different type
        assertFalse(unsupportedDurationField.equals(UnsupportedDurationField.getInstance(DurationFieldType.HOURS)));

        // Check if the field is not equal to another object
        assertFalse(unsupportedDurationField.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);

        // Check if the hashcode is different for different field types
        assertNotEquals(unsupportedDurationField.hashCode(), UnsupportedDurationField.getInstance(DurationFieldType.DAYS).hashCode());

        // Check if the hashcode is the same for the same field type
        assertEquals(unsupportedDurationField.hashCode(), UnsupportedDurationField.getInstance(DurationFieldType.SECONDS).hashCode());
    }

    @Test
    public void testToString() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);

        // Check if the toString() output contains the field name
        assertTrue(unsupportedDurationField.toString().contains("days"));
    }

}
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest {

    private UnsupportedDurationField unsupportedField;

    @Before
    public void setUp() {
        unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
    }

    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.ERA_TYPE, unsupportedField.getType());
    }

    @Test
    public void testGetName() {
        assertEquals("era", unsupportedField.getName());
    }

    @Test
    public void testIsSupported() {
        assertFalse(unsupportedField.isSupported());
    }

    @Test
    public void testIsPrecise() {
        assertTrue(unsupportedField.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        unsupportedField.getValue(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        unsupportedField.getValueAsLong(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        unsupportedField.getValue(1000L, 123456789L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        unsupportedField.getValueAsLong(1000L, 987654321L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        unsupportedField.getMillis(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLong() {
        unsupportedField.getMillis(500L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        unsupportedField.getMillis(1000, 123456789L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLongAndInstant() {
        unsupportedField.getMillis(500L, 987654321L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        unsupportedField.add(123456789L, 1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithLongValue() {
        unsupportedField.add(987654321L, 500L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        unsupportedField.getDifference(123456789L, 987654321L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        unsupportedField.getDifferenceAsLong(123456789L, 987654321L);
    }

    @Test
    public void testGetUnitMillis() {
        assertEquals(0L, unsupportedField.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        assertEquals(1, unsupportedField.compareTo(DurationFieldType.seconds()));
        assertEquals(0, unsupportedField.compareTo(DurationFieldType.ERA_TYPE));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.secs());
        UnsupportedDurationField field4 = UnsupportedDurationField.getInstance(null);

        assertTrue(field1.equals(field2));
        assertTrue(field2.equals(field1));
        assertFalse(field2.equals(field3));
        assertFalse(field2.equals(field4));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);

        assertEquals(field1.hashCode(), field2.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[era]", unsupportedField.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        UnsupportedDurationField field2 = (UnsupportedDurationField) field1.readResolve();

        assertSame(field1, field2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperationException() {
        throw unsupportedField.unsupported();
    }
}
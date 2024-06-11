
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type, field.getType());
    }

    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type.getName(), field.getName());
    }

    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(123);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(123);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(123, 456);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(123, 456);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(123);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLong() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(123L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(123, 456);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLongAndInstant() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(123L, 456L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValue() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(123, 456);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValueAsLong() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(123L, 456L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifference(123L, 456L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifferenceAsLong(123L, 456L);
    }

    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(0L, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.years();
        DurationFieldType type2 = DurationFieldType.months();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        assertTrue(field1.compareTo(field1) > 0);
        assertTrue(field1.compareTo(field2) > 0);
        assertEquals(0, field1.compareTo(field1));
        assertTrue(field2.compareTo(field1) < 0);
    }

    @Test
    public void testEquals() {
        DurationFieldType type1 = DurationFieldType.years();
        DurationFieldType type2 = DurationFieldType.months();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        assertTrue(field1.equals(field1));
        assertFalse(field1.equals(null));
        assertFalse(field1.equals(new Object()));
        assertFalse(field1.equals(field2));
        assertFalse(field1.equals(UnsupportedDurationField.getInstance(null)));
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type.getName().hashCode(), field.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[" + type.getName() + ']', field.toString());
    }

}

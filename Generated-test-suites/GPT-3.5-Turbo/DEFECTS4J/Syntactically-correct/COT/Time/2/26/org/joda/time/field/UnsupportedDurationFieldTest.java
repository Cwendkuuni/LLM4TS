
import static org.junit.Assert.*;
import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertNotNull(field);
        assertEquals(type, field.getType());
    }

    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.months();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertNotNull(field);
        assertEquals(type.getName(), field.getName());
    }

    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(false, field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.hours();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(true, field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.minutes();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(2000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(3000, 4000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        DurationFieldType type = DurationFieldType.halfdays();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(5000, 6000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        DurationFieldType type = DurationFieldType.quarters();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(7000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValue() {
        DurationFieldType type = DurationFieldType.weeks();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(8000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(9000, 10000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValueAndInstant() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(10000L, 11000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValue() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(12000L, 13000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndLongValue() {
        DurationFieldType type = DurationFieldType.minutes();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(14000L, 15000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifference(16000L, 17000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.hours();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifferenceAsLong(18000L, 19000L);
    }

    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.weeks();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertTrue(field1.equals(field2));
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.minutes();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type.getName().hashCode(), field.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[" + type.getName() + ']', field.toString());
    }

    @Test
    public void testReadResolve() {
        DurationFieldType type = DurationFieldType.hours();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(field, field.readResolve());
    }

}

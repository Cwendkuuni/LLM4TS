import org.joda.time.BaseTestCase;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedDurationField;
import org.junit.Test;

public class UnsupportedDurationFieldTest extends BaseTestCase {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(field1, field2);
    }

    @Test
    public void testGetType() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type, field.getType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAtInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(1L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongAtInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(1L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAtInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(0L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAtInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(0L, 1, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifference(0L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifferenceAsLong(0L, 0L);
    }

    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.DAYS;
        DurationFieldType type2 = DurationFieldType.HOURS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        DurationFieldType type1 = DurationFieldType.DAYS;
        DurationFieldType type2 = DurationFieldType.HOURS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        assertFalse(field1.equals(field2));

        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(type1);
        assertTrue(field1.equals(field3));
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        String name = field.getName();
        int hashCode = field.hashCode();
        assertEquals(hashCode, name.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        String expected = "UnsupportedDurationField[DAYS]";
        String actual = field.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testReadResolve() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        UnsupportedDurationField resolved = (UnsupportedDurationField) field.readResolve();
        assertEquals(field.getType(), resolved.getType());
    }

}
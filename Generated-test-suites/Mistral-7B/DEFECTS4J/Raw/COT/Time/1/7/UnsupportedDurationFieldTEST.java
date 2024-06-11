import org.joda.time.BaseTestCase;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import org.junit.Test;

public class UnsupportedDurationFieldTest extends BaseTestCase {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField instance1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField instance2 = UnsupportedDurationField.getInstance(type);
        assertEquals(instance1, instance2);
    }

    @Test
    public void testGetType() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.days());
        assertEquals(DurationFieldType.days(), field.getType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsSupported() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.days());
        field.isSupported();
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.days());
        assertEquals("days", field.getName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.days());
        field.getValue(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.days());
        field.getValueAsLong(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAtInstant() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.days());
        field.getValue(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongAtInstant() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.days());
        field.getValueAsLong(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.days());
        field.getMillis(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAtInstant() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.days());
        field.getMillis(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.days());
        field.add(0, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAtInstant() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.days());
        field.add(0, 1, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.days());
        field.getDifference(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.days());
        field.getDifferenceAsLong(0, 0);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.days());
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.days());
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.hours());
        assertEquals(1, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.days());
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.days());
        UnsupportedDurationField field3 = new UnsupportedDurationField(DurationFieldType.hours());
        assertEquals(field1, field1);
        assertEquals(field1, field2);
        assertNotSame(field1, field3);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.days());
        assertEquals(DurationFieldType.days().hashCode(), field.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.days());
        assertEquals("UnsupportedDurationField[days]", field.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.days());
        assertEquals(field.getType(), UnsupportedDurationField.getInstance(field.getType()));
    }
}
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertNotNull(field);
    }

    @Test
    public void testGetType() {
        DurationFieldType type = DurationFieldType.months();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type, field.getType());
    }

    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type.getName(), field.getName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.hours();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.minutes();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(5);
    }

    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.millis();
        DurationFieldType type2 = DurationFieldType.seconds();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.days();
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
        DurationFieldType type = DurationFieldType.weeks();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[" + type.getName() + ']', field.toString());
    }
}
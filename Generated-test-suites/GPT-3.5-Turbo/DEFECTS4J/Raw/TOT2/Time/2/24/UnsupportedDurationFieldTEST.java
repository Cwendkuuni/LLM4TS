import org.joda.time.DurationFieldType;
import org.joda.time.field.UnsupportedDurationField;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type, field.getType());
    }

    @Test
    public void testGetType() {
        DurationFieldType type = DurationFieldType.hours();
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertEquals(type, field.getType());
    }

    @Test
    public void testName() {
        DurationFieldType type = DurationFieldType.minutes();
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertEquals(type.getName(), field.getName());
    }

    // Edge cases and error scenarios testing

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.seconds());
        field.getValue(10L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.weeks());
        field.getMillis(5);
    }

    @Test
    public void testAdd() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.months());
        assertEquals(0, field.add(100L, 20));
    }

    // Additional test cases can be added here

}
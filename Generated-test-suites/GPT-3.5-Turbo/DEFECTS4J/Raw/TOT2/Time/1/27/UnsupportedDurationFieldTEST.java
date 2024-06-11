import org.joda.time.DurationFieldType;
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
    public void testGetName() {
        DurationFieldType type = DurationFieldType.hours();
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertEquals(type.getName(), field.getName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.weeks();
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertTrue(field.isPrecise());
    }

    // Add more test cases for other methods as needed

}
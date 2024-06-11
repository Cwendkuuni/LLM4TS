import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        DurationFieldType fieldType = DurationFieldType.SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(fieldType);
        
        assertEquals(fieldType, field.getType());
    }

    @Test
    public void testGetName() {
        DurationFieldType fieldType = DurationFieldType.SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(fieldType);

        assertEquals(fieldType.getName(), field.getName());
    }

    @Test
    public void testIsSupported() {
        DurationFieldType fieldType = DurationFieldType.SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(fieldType);

        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        DurationFieldType fieldType = DurationFieldType.SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(fieldType);

        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType fieldType = DurationFieldType.SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(fieldType);

        field.getValue(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType fieldType = DurationFieldType.SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(fieldType);

        field.getValueAsLong(1000);
    }

    // and so on for the rest of the methods...

}
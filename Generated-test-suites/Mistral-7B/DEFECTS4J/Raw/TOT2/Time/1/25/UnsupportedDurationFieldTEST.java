import static org.junit.Assert.assertEquals;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedDurationField;

public class UnsupportedDurationFieldTest {

    @org.junit.Test
    public void testGetType() {
        DurationFieldType expectedType = DurationFieldType.YEARS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(expectedType);
        DurationFieldType actualType = unsupportedField.getType();
        assertEquals(expectedType, actualType);
    }
}
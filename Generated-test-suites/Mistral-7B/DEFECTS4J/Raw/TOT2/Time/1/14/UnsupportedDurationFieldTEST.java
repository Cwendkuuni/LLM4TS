import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        DurationFieldType expectedType = DurationFieldType.DAYS;
        DurationFieldType actualType = unsupportedDurationField.getType();
        assertEquals("getType should return the correct DurationFieldType", expectedType, actualType);
    }
}
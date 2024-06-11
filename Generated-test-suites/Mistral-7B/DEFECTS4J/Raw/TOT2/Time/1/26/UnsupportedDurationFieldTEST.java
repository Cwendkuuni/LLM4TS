import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedDurationField;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField anotherUnsupportedField = UnsupportedDurationField.getInstance(type);

        assertEquals(unsupportedField, anotherUnsupportedField);
    }
}
import static org.junit.Assert.assertEquals;
import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        assertEquals(DurationFieldType.MINUTES, unsupportedDurationField.getType());
    }
}
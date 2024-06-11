import org.junit.Test;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.assertSame;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField expectedUnsupportedField = new UnsupportedDurationField(type);
        assertSame(expectedUnsupportedField, unsupportedField);
    }
}
import org.junit.Test;
import static org.junit.Assert.assertFalse;

public class UnsupportedDurationFieldTest {

    @Test
    public void testIsSupported() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertFalse(unsupportedDurationField.isSupported());
    }
}
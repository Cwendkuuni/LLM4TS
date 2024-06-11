import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        String name = field.getName();
        assertEquals("DAYS", name);
    }
}
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.assertSame;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.SECONDS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertSame(field1, field2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetInstance_EdgeCase() {
        DurationFieldType type = new DurationFieldType("TestType");
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
    }

    @Test
    public void testGetInstance_Caching() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(type);
        assertSame(field1, field2);
        assertSame(field1, field3);
    }
}

//
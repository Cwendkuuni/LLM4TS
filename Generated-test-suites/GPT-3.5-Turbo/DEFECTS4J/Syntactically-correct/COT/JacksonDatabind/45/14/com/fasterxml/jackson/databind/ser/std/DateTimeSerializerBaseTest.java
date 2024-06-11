import org.junit.Before;
import org.junit.Test;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<TestDate> dateTimeSerializerBase;

    @Before
    public void setUp() {
        dateTimeSerializerBase = new DateTimeSerializerBase<TestDate>(TestDate.class, true, null) {
            @Override
            protected long _timestamp(TestDate value) {
                return 0L;
            }

            @Override
            public void serialize(TestDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            public DateTimeSerializerBase<TestDate> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
        };
    }

    @Test
    public void testWithFormat() {
        // TODO: Implement test case
    }

    @Test
    public void testCreateContextual() {
        // TODO: Implement test case
    }

    @Test
    public void testIsEmpty() {
        // TODO: Implement test case
    }

    @Test
    public void testGetSchema() {
        // TODO: Implement test case
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        // TODO: Implement test case
    }

    @Test
    public void testSerialize() {
        // TODO: Implement test case
    }
}

import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, Boolean.FALSE, null) {
            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestObject value) {
                return 0;
            }

            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        DateTimeSerializerBase<TestObject> newSerializer = serializer.withFormat(Boolean.TRUE, null);
        assertTrue(newSerializer.getUseTimestamp());
        assertNull(newSerializer.getCustomFormat());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider serializerProvider = null;
        BeanProperty property = null;

        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, Boolean.FALSE, null) {
            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestObject value) {
                return 0;
            }

            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        JsonSerializer<?> newSerializer = serializer.createContextual(serializerProvider, property);

        assertSame(serializer, newSerializer);
    }

    @Test
    public void testIsEmpty() {
        SerializerProvider serializerProvider = null;
        TestObject value = null;

        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, Boolean.FALSE, null) {
            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestObject value) {
                return 0;
            }

            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        assertFalse(serializer.isEmpty(serializerProvider, value));
    }

    @Test
    public void testGetSchema() {
        SerializerProvider serializerProvider = null;
        Type typeHint = null;

        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, Boolean.FALSE, null) {
            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestObject value) {
                return 0;
            }

            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        JsonNode schema = serializer.getSchema(serializerProvider, typeHint);

        assertNotNull(schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;

        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, Boolean.FALSE, null) {
            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestObject value) {
                return 0;
            }

            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void testSerialize() throws IOException {
        SerializerProvider serializerProvider = null;
        JsonGenerator gen = null;
        TestObject value = null;

        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, Boolean.FALSE, null) {
            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestObject value) {
                return 0;
            }

            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        serializer.serialize(value, gen, serializerProvider);
    }
}

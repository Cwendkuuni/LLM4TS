import static org.junit.Assert.*;
import org.junit.Test;

public class DateTimeSerializerBaseTest {
    
    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }
        };
        
        DateTimeSerializerBase<Date> result = serializer.withFormat(true, null);
        assertTrue(result._useTimestamp);

        result = serializer.withFormat(false, null);
        assertFalse(result._useTimestamp);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }
        };

        SerializerProvider serializers = null;
        BeanProperty property = null;

        JsonSerializer<?> result = serializer.createContextual(serializers, property);
        assertSame(serializer, result);
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }
        };

        SerializerProvider serializers = null;
        Date value = null;

        assertFalse(serializer.isEmpty(serializers, value));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }
        };

        SerializerProvider serializers = null;
        Type typeHint = null;

        JsonNode result = serializer.getSchema(serializers, typeHint);
        assertNotNull(result);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }
        };

        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;

        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }
}
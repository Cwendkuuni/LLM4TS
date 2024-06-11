import static org.junit.Assert.*;
import org.junit.Test;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseImpl();
        DateTimeSerializerBase result = serializer.withFormat(false, new SimpleDateFormat("yyyy-MM-dd"));
        assertFalse(result._useTimestamp);
        assertNotNull(result._customFormat);
        assertEquals(new SimpleDateFormat("yyyy-MM-dd"), result._customFormat);
    }

    @Test
    public void testCreateContextual() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseImpl();
        SerializerProvider serializers = new SerializerProvider();
        BeanProperty property = new BeanPropertyImpl();
        try {
            ContextualSerializer result = serializer.createContextual(serializers, property);
            assertEquals(serializer, result);
        } catch (JsonMappingException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBaseImpl serializer = new DateTimeSerializerBaseImpl();
        assertTrue(serializer.isEmpty(null));
        assertFalse(serializer.isEmpty("value"));
    }

    @Test
    public void testIsEmptyWithProvider() {
        DateTimeSerializerBaseImpl serializer = new DateTimeSerializerBaseImpl();
        SerializerProvider serializers = new SerializerProvider();
        assertTrue(serializer.isEmpty(serializers, null));
        assertFalse(serializer.isEmpty(serializers, "value"));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBaseImpl serializer = new DateTimeSerializerBaseImpl();
        SerializerProvider serializers = new SerializerProvider();
        JsonNode result = serializer.getSchema(serializers, null);
        assertEquals("string", result.get("type").asText());
        assertTrue(result.get("required").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        DateTimeSerializerBaseImpl serializer = new DateTimeSerializerBaseImpl();
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = null;
        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
            assertTrue(visitor.visited);
        } catch (JsonMappingException e) {
            fail("Should not throw exception");
        }
    }

    private static class DateTimeSerializerBaseImpl extends DateTimeSerializerBase<String> {

        protected DateTimeSerializerBaseImpl() {
            super(String.class, false, new SimpleDateFormat("yyyy-MM-dd"));
        }

        @Override
        public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
            return new DateTimeSerializerBaseImpl();
        }

        @Override
        protected long _timestamp(String value) {
            return 0;
        }

        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {
            // Do nothing
        }
    }

    private static class SerializerProvider {
        public boolean isEnabled(SerializationFeature feature) {
            return true;
        }

        public TimeZone getTimeZone() {
            return TimeZone.getDefault();
        }

        public Locale getLocale() {
            return Locale.getDefault();
        }

        public JsonFormat.Value getAnnotationIntrospector() {
            return null;
        }
    }

    private static class BeanPropertyImpl implements BeanProperty {
        public Type getMember() {
            return String.class;
        }
    }

    private static class JsonFormatVisitorWrapper implements JsonFormatVisitorWrapper {
        public boolean visited = false;

        public Provider getProvider() {
            return new Provider();
        }

        @Override
        public void expectStringFormat(Class<?> type) throws JsonMappingException {
            visited = true;
        }
    }

    private static class Provider implements SerializerProvider {
        @Override
        public boolean isEnabled(SerializationFeature feature) {
            return true;
        }

        @Override
        public TimeZone getTimeZone() {
            return TimeZone.getDefault();
        }

        @Override
        public Locale getLocale() {
            return Locale.getDefault();
        }

        @Override
        public JsonFormat.Value getAnnotationIntrospector() {
            return new JsonFormat.Value();
        }
    }
}


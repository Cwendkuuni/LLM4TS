import static org.junit.Assert.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.SerializerProvider;

import org.junit.Before;
import org.junit.Test;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Object> serializer;

    @Before
    public void setup() {
        serializer = new DateTimeSerializerImpl(Object.class, true, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<Object> newSerializer = serializer.withFormat(false, new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"));
        assertFalse(newSerializer._useTimestamp);
        assertEquals("MM/dd/yyyy HH:mm:ss", ((SimpleDateFormat) newSerializer._customFormat).toPattern());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        DateTimeSerializerBase<Object> contextualSerializer = (DateTimeSerializerBase<Object>) serializer.createContextual(provider, null);
        assertTrue(contextualSerializer._useTimestamp);
        assertNull(contextualSerializer._customFormat);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(serializer.isEmpty(null));
        assertFalse(serializer.isEmpty(new Object()));
    }

    @Test
    public void testGetSchema() {
        assertNotNull(serializer.getSchema(new ObjectMapper().getSerializerProvider(), Object.class));
    }

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        serializer.serialize(new Object(), gen, new ObjectMapper().getSerializerProvider());
        gen.close();
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        CustomVisitor visitor = new CustomVisitor();
        serializer.acceptJsonFormatVisitor(visitor, mapper.getTypeFactory().constructType(Object.class));
        assertTrue(visitor.visited);
    }

    private static class DateTimeSerializerImpl extends DateTimeSerializerBase<Object> {
        public DateTimeSerializerImpl(Class<Object> type, Boolean useTimestamp, SimpleDateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            // custom serialization logic
        }

        @Override
        protected long _timestamp(Object value) {
            // custom timestamp logic
            return 0L;
        }
    }

    private static class CustomVisitor implements JsonFormatVisitorWrapper {
        boolean visited = false;

        @Override
        public SerializerProvider getProvider() {
            return new ObjectMapper().getSerializerProvider();
        }

        @Override
        public void setProvider(SerializerProvider provider) {}

        @Override
        public JsonFormatVisitorWrapper setSchema(SerializerProvider provider) {
            visited = true;
            return this;
        }
    }

}
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonFormat;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdDateFormat;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {
    @Test
    public void testWithContextual() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializer();
        JsonSerializer<?> result = serializer.createContextual(provider, property);
        assertEquals(serializer, result);
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializer();
        SerializerProvider provider = mock(SerializerProvider.class);
        Object value = new Object();
        assertEquals(false, serializer.isEmpty(provider, value));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializer();
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        JsonNode result = serializer.getSchema(provider, typeHint);
        assertEquals("number", result.get("type").textValue());
        assertEquals(true, result.get("format").booleanValue());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializer();
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor).expectDateFormat(any(DateFormat.class));
    }

    @Test
    public void testSerialize() throws IOException {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializer();
        Object value = new Date();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        serializer.serialize(value, gen, provider);
        verify(gen).writeString(anyString());
    }

    private static class DateTimeSerializer extends DateTimeSerializerBase<Object> {

        protected DateTimeSerializer() {
            super(Object.class, null, null);
        }

        @Override
        public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

        @Override
        protected long _timestamp(Object value) {
            return 0;
        }

        @Override
        protected void _serializeAsString(Date value, JsonGenerator g, SerializerProvider provider) throws IOException {
            
        }
        
    }
}
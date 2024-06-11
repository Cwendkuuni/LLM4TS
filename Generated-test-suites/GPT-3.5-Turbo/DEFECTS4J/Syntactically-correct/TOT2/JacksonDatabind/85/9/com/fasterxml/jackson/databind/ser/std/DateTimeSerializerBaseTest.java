import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;

@RunWith(MockitoJUnitRunner.class)
public class DateTimeSerializerBaseTest {

    @Mock
    private SerializerProvider serializers;

    @Test
    public void testWithContextual() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseImpl(String.class, true, null);
        JsonSerializer contextualSerializer = null;
        try {
            contextualSerializer = serializer.createContextual(serializers, null);
        } catch (JsonMappingException e) {
            fail("Exception thrown during createContextual");
        }
        assertNotNull(contextualSerializer);
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseImpl(String.class, true, null);
        assertTrue(serializer.isEmpty(serializers, null));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseImpl(String.class, true, null);
        JsonNode schema = serializer.getSchema(serializers, String.class);
        assertNotNull(schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseImpl(String.class, true, null);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        serializer.acceptJsonFormatVisitor(visitor, String.class);
        verify(visitor).setFormatName("number");
    }

    @Test
    public void testSerialize() throws Exception {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseImpl(String.class, true, null);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        serializer.serialize("test", generator, provider);
        verify(generator).writeString("test");
    }

    private class DateTimeSerializerBaseImpl extends DateTimeSerializerBase<String> {
        public DateTimeSerializerBaseImpl(Class<String> type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }

        @Override
        public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

        @Override
        protected long _timestamp(String value) {
            return 0;
        }
    }
}
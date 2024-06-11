import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonFormat;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithContextual() throws JsonMappingException {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseTestImpl();
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonSerializer result = serializer.createContextual(provider, property);
        assertNotNull(result);
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseTestImpl();
        assertTrue(serializer.isEmpty(null));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseTestImpl();
        SerializerProvider provider = mock(SerializerProvider.class);
        JsonNode schema = serializer.getSchema(provider, null);
        assertNotNull(schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseTestImpl();
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        serializer.acceptJsonFormatVisitor(visitor, null);
        Mockito.verify(visitor, Mockito.times(1)).getProvider();
    }

    @Test
    public void testSerialize() throws IOException {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseTestImpl();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        serializer.serialize(null, gen, provider);
        Mockito.verify(gen, Mockito.times(1)).writeString("");
    }

    private static class DateTimeSerializerBaseTestImpl extends DateTimeSerializerBase<String> {

        public DateTimeSerializerBaseTestImpl() {
            super(String.class, true, new SimpleDateFormat());
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
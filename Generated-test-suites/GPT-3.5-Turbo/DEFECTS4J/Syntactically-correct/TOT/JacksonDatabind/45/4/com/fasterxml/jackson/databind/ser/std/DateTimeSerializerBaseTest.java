import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Object> serializer;
    private SerializerProvider serializerProvider;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        serializer = new DateTimeSerializerBase<Object>(Object.class, null, null) {

            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }
        };

        serializerProvider = new ObjectMapper().getSerializerProvider();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testIsEmpty_NullValue_ReturnsTrue() {
        assertTrue(serializer.isEmpty(null));
    }

    @Test
    public void testIsEmpty_NonNullValue_ReturnsFalse() {
        assertFalse(serializer.isEmpty(12345));
    }

    @Test
    public void test_GetSchema_AsTimestamp_ReturnsNumberSchemaNode() {
        JsonNode schema = serializer.getSchema(serializerProvider, null);
        assertEquals("number", schema.asText());
    }

    @Test
    public void test_GetSchema_AsString_ReturnsStringSchemaNode() {
        serializer = new DateTimeSerializerBase<Object>(Object.class, false, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }
        };
        JsonNode schema = serializer.getSchema(serializerProvider, null);
        assertEquals("string", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_AsTimestamp_VisitIntFormat() throws Exception {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base(serializerProvider);
        serializer._acceptJsonFormatVisitor(visitor, objectMapper.constructType(Object.class), true);
        assertEquals(JsonParser.NumberType.LONG, visitor.getProvider().getType());
        assertEquals(JsonValueFormat.UTC_MILLISEC, visitor.getFormat().getValueFormat());
    }

    @Test
    public void testAcceptJsonFormatVisitor_AsString_VisitStringFormat() throws Exception {
        serializer = new DateTimeSerializerBase<Object>(Object.class, false, null) {

            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }
        };

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base(serializerProvider);
        serializer._acceptJsonFormatVisitor(visitor, objectMapper.constructType(Object.class), false);
        assertEquals(JsonValueFormat.DATE_TIME, visitor.getFormat().getValueFormat());
    }

    @Test
    public void testSerialize_ObjectToJson_Success() throws Exception {
        Object value = new Object();
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(value, gen, serializerProvider);
    }
}
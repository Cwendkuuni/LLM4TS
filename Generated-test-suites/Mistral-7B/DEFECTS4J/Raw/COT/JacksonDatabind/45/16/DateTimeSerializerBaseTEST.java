package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Long> dateTimeSerializer;
    private SerializerProvider serializers;
    private JsonGenerator jsonGenerator;
    private JsonParser jsonParser;

    @Before
    public void setup() {
        serializers = mock(SerializerProvider.class);
        jsonGenerator = mock(JsonGenerator.class);
        jsonParser = mock(JsonParser.class);
        dateTimeSerializer = new DateTimeSerializerBase<Long>(Long.class, false, null) {
            @Override
            protected long _timestamp(Long value) {
                return value;
            }

            @Override
            public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                when(jsonGenerator.writeNumber(value)).thenReturn(this);
            }
        };
    }

    @Test
    public void testCreateContextual_withFormat_numeric() throws Exception {
        // Test serializing as numeric timestamp
        JsonFormat.Value formatNumeric = new JsonFormat.Value();
        formatNumeric.setShape(JsonFormat.Shape.NUMBER);
        when(serializers.getAnnotationIntrospector().findFormat((Annotated)any())).thenReturn(formatNumeric);

        DateTimeSerializerBase<Long> serializerNumeric = dateTimeSerializer.withFormat(true, null);

        Long testValue = 1516041620000L;
        serializeAndCheck("1516041620000", serializerNumeric, testValue, serializers);
    }

    @Test
    public void testCreateContextual_withFormat_string() throws Exception {
        // Test serializing as string
        JsonFormat.Value formatString = new JsonFormat.Value();
        formatString.setShape(JsonFormat.Shape.STRING);
        formatString.setLocale(Locale.US);
        formatString.setTimeZone(TimeZone.getTimeZone("UTC"));
        formatString.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        when(serializers.getAnnotationIntrospector().findFormat((Annotated)any())).thenReturn(formatString);

        DateTimeSerializerBase<Long> serializerString = dateTimeSerializer.withFormat(false, new SimpleDateFormat(formatString.getPattern(), formatString.getLocale()));

        Long testValue = 1516041620000L;
        serializeAndCheck("2018-01-01T00:01:23.456Z", serializerString, testValue, serializers);
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateContextual_withoutFormat() throws Exception {
        // Test failing when no format is found
        when(serializers.getAnnotationIntrospector().findFormat((Annotated)any()))
                .thenReturn(null);

        dateTimeSerializer.createContextual(serializers, null);

        fail("Expected JsonMappingException to be thrown");
    }

    @Test
    public void testIsEmpty() throws Exception {
        assertTrue(dateTimeSerializer.isEmpty(null));
        assertTrue(dateTimeSerializer.isEmpty(serializers, null));
        assertTrue(dateTimeSerializer.isEmpty(serializers, 0L));
    }

    @Test
    public void testGetSchema() throws Exception {
        JsonNode expectedSchema = createSchemaNode("number", true);
        JsonNode actualSchema = dateTimeSerializer.getSchema(serializers, Long.class);

        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        when(visitor.getProvider()).thenReturn(serializers);

        dateTimeSerializer.acceptJsonFormatVisitor(visitor, Long.class);

        verify(visitor, times(1)).visitIntFormat(any(JsonFormatVisitorWrapper.class), any(JavaType.class), eq(JsonParser.NumberType.LONG), eq(JsonValueFormat.UTC_MILLISEC));
    }

    private void serializeAndCheck(String expectedJson, DateTimeSerializerBase<Long> serializer, Long testValue, SerializerProvider serializers) throws IOException {
        when(jsonGenerator.writeStartObject()).thenReturn(this);
        when(jsonGenerator.writeFieldName("value")).thenReturn(this);
        serializer.serialize(testValue, jsonGenerator, serializers);
        when(jsonGenerator.writeEndObject()).thenReturn(this);

        String actualJson = jsonParserFromString(jsonParser, jsonGenerator.getOutputStream().toString());
        assertEquals(expectedJson, actualJson);
    }

    private JsonNode createSchemaNode(String type, boolean isRequired) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("type", type);
        node.put("required", isRequired);
        return node;
    }

    private JsonParser jsonParserFromString(JsonParser jsonParser, String jsonString) {
        JsonFactory factory = new JsonFactory();
        return factory.createParser(jsonString);
    }
}
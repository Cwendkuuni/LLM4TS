package com.fasterxml.jackson.databind.ser.std; // same package as the class under test

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonMapper;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class StdSerializerTest {

    private SerializerProvider serializerProvider;
    private ObjectNode objectNode;
    private JsonMapper jsonMapper;

    @Before
    public void setup() {
        serializerProvider = mock(SerializerProvider.class);
        objectNode = JsonNodeFactory.instance.objectNode();
        jsonMapper = new JsonMapper();
        jsonMapper.setSerializerProvider(serializerProvider);
    }

    @Test
    public void testStdSerializerConstructor() {
        Class<String> handledType = String.class;
        StdSerializer<String> stdSerializer = new StdSerializer<>(handledType);
        assertEquals(String.class, stdSerializer.handledType());
    }

    @Test
    public void testSerializeMethodThrowsException() throws Exception {
        Class<Object> handledType = Object.class;
        StdSerializer<Object> stdSerializer = new StdSerializer<>(handledType);
        Object object = new Object();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        when(serializerProvider.getActiveContext()).thenReturn(null);

        try {
            stdSerializer.serialize(object, jsonGenerator, serializerProvider);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testGetSchemaMethodReturnsDefaultSchema() throws JsonMappingException {
        Class<String> handledType = String.class;
        StdSerializer<String> stdSerializer = new StdSerializer<>(handledType);

        JsonNode schema = stdSerializer.getSchema(serializerProvider, String.class);
        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testGetSchemaMethodWithOptionalFlagReturnsSchemaWithRequiredSetToFalse() throws JsonMappingException {
        Class<String> handledType = String.class;
        StdSerializer<String> stdSerializer = new StdSerializer<>(handledType);

        JsonNode schema = stdSerializer.getSchema(serializerProvider, String.class, true);
        assertEquals("string", schema.get("type").asText());
        assertFalse(schema.get("required").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitorMethod() throws JsonMappingException {
        Class<String> handledType = String.class;
        StdSerializer<String> stdSerializer = new StdSerializer<>(handledType);
        JsonFormatVisitorWrapper visitorWrapper = mock(JsonFormatVisitorWrapper.class);
        when(visitorWrapper.expectAnyFormat(String.class)).thenReturn(null);

        stdSerializer.acceptJsonFormatVisitor(visitorWrapper, String.class);
        verify(visitorWrapper).expectAnyFormat(String.class);
    }

    @Test
    public void testWrapAndThrowMethod() throws Exception {
        Class<Object> handledType = Object.class;
        StdSerializer<Object> stdSerializer = new StdSerializer<>(handledType);
        Throwable exception = new IOException("Test exception");
        Object object = new Object();
        String fieldName = "testField";

        stdSerializer.wrapAndThrow(serializerProvider, exception, object, fieldName);

        // Verify that the exception was wrapped with a JsonMappingException
        JsonMappingException jsonMappingException = throwableCaptor.getValue();
        assertEquals("Test exception", jsonMappingException.getCause().getMessage());
        assertEquals(object, jsonMappingException.getPath().get(0).asText());
    }

    @Test
    public void testFindConvertingContentSerializerMethod() throws JsonMappingException {
        Class<Object> handledType = Object.class;
        StdSerializer<Object> stdSerializer = new StdSerializer<>(handledType);

        // Configure a mock converter
        Converter<Object, Object> converter = mock(Converter.class);
        when(converter.getOutputType(JsonTypeFactory.instance)).thenReturn(Integer.class);

        // Simulate a call to findValueSerializer method with the mock converter returned
        JsonSerializer<Integer> existingSerializer = mock(JsonSerializer.class);
        when(serializerProvider.findValueSerializer(Integer.class)).thenReturn(existingSerializer);

        JsonSerializer<Object> result = stdSerializer.findConvertingContentSerializer(serializerProvider, null, objectNode);
        assertEquals(existingSerializer.getClass(), result.getClass());
        verify(serializerProvider).converterInstance(null, converter);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindPropertyFilterMethodThrowsException() throws JsonMappingException {
        FilterProvider filterProvider = mock(FilterProvider.class);
        Object filterId = new Object();
        Object valueToFilter = new Object();

        StdSerializer stdSerializer = new StdSerializer(null);
        stdSerializer.findPropertyFilter(serializerProvider, filterId, valueToFilter);

       fail("Expected JsonMappingException to be thrown");
    }

    @Test
    public void testIsDefaultSerializerMethodReturnsTrueForDefaultSerializer() {
        StdSerializer<String> stdSerializer = new StdSerializer<>(String.class);
        JsonSerializer<String> jsonSerializer = new StdSerializer<String>(String.class);

        assertTrue(stdSerializer.isDefaultSerializer(jsonSerializer));
    }

    @Test
    public void testIsDefaultSerializerMethodReturnsFalseForCustomSerializer() {
        StdSerializer<String> stdSerializer = new StdSerializer<>(String.class);
        CustomSerializer<String> customSerializer = new CustomSerializer<>();

        assertFalse(stdSerializer.isDefaultSerializer(customSerializer));
    }

    @Test
    public void testCreateSchemaNodeMethodCreatesSchemaNodeWithStringType() {
        ObjectNode result = new StdSerializer<>(String.class).createSchemaNode("string");

        assertEquals("string", result.get("type").asText());
    }

    @Test
    public void testCreateSchemaNodeMethodCreatesSchemaNodeWithStringTypeAndOptionalSetToFalse() {
        ObjectNode result = new StdSerializer<>(String.class).createSchemaNode("string", false);

        assertEquals("string", result.get("type").asText());
        assertFalse(result.get("required").asBoolean());
    }

    @Test
    public void testCreateSchemaNodeMethodCreatesSchemaNodeWithOtherType() {
        ObjectNode result = new StdSerializer<>(Integer.class).createSchemaNode("integer");

        assertEquals("integer", result.get("type").asText());
    }

    @Test
    public void testCreateSchemaNodeMethodCreatesSchemaNodeWithOtherTypeAndOptionalSetToFalse() {
        ObjectNode result = new StdSerializer<>(Integer.class).createSchemaNode("integer", false);

        assertEquals("integer", result.get("type").asText());
        assertFalse(result.get("required").asBoolean());
    }

    private Throwable throwableCaptor = new Throwable();

}

class CustomSerializer<T> implements JsonSerializer<T> {
    @Override
    public void serialize(T value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        // empty implementation
    }
}
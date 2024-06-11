package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StdSerializerTest {

    private StdSerializer<Integer> serializer;
    private SerializerProvider provider;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        provider = mock(SerializerProvider.class);
        serializer = new StdSerializer<Integer>(Integer.class) {
            @Override
            public void serialize(Integer value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                jgen.writeNumber(value);
            }
        };
    }

    // Test for constructors

    @Test
    public void testStdSerializerConstructorWithClass() {
        StdSerializer<Integer> serializer = new StdSerializer<Integer>(Integer.class);
        assertEquals(Integer.class, serializer.handledType());
    }

    @Test
    public void testStdSerializerConstructorWithJavaType() {
        StdSerializer<Integer> serializer = new StdSerializer<Integer>(JavaType.construct(Integer.class));
        assertEquals(Integer.class, serializer.handledType());
    }

    @Test
    public void testStdSerializerConstructorWithClassAndBoolean() {
        StdSerializer<Integer> serializer = new StdSerializer<Integer>(Integer.class, false);
        assertEquals(Integer.class, serializer.handledType());
    }

    // Test for abstract methods

    @Test
    public void testSerialize() throws JsonProcessingException {
        when(provider.getOutputFactory()).thenReturn(JsonNodeFactory.instance);
        JsonGenerator gen = mock(JsonGenerator.class);
        when(provider.getFactory()).thenReturn(mock(JsonFactory.class));
        when(provider.createGenerator(mock(OutputStream.class), serializer.handledType(), ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT)).thenReturn(gen);

        serializer.serialize(1, gen, provider);
        ObjectNode node = (ObjectNode) provider.getOutputFactory().getRootValue();
        assertEquals(1, node.get(0).asInt());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        assertEquals(createSchemaNode("string"), serializer.getSchema(provider, null));
    }

    @Test
    public void testGetSchemaWithOptional() throws JsonMappingException {
        assertEquals(createSchemaNode("string", false), serializer.getSchema(provider, null, false));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        serializer.acceptJsonFormatVisitor(visitor, null);
    }

    // Test for helper methods

    @Test
    public void testIsDefaultSerializer() {
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        assertTrue(StdSerializer.isDefaultSerializer(serializer));
        assertFalse(StdSerializer.isDefaultSerializer(new CustomSerializer<>()));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        Converter<Object, Object> converter = mock(Converter.class);
        ObjectNode delegateTypeNode = createSchemaNode("String");
        JsonSerializer<Object> existingSerializer = mock(JsonSerializer.class);
        when(provider.converterInstance(mock(AnnotatedMember.class), JsonType.class)).thenReturn(converter);

        JsonSerializer<Object> result = serializer.findConvertingContentSerializer(provider, JsonType.class, "test");
        assertEquals(new StdDelegatingSerializer(converter, delegateTypeNode, existingSerializer), result);
    }

    // Test for exception handling methods

    @Test
    public void testWrapAndThrow() throws JsonMappingException {
        when(provider.isEnabled(SerializationFeature.WRAP_EXCEPTIONS)).thenReturn(true);
        JsonMappingException mappingException = new JsonMappingException("Test exception");
        when(provider.getSerializationConfig().getFactory().createGenerator(mock(OutputStream.class), null, ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT)).thenThrow(mappingException);

        serializer.wrapAndThrow(provider, null, null, null);
        JsonMappingException wrappedException = expectedException.expect(JsonMappingException.class);
        assertEquals(mappingException, wrappedException);
    }

    @Test
    public void testWrapAndThrowWithoutWrapper() {
        expectedException.expect(RuntimeException.class);
        serializer.wrapAndThrow(provider, new RuntimeException("Test exception"), null, null);
    }

    @Test
    public void testWrapAndThrowWithoutWrapperWithJsonMappingException() {
        expectedException.expect(JsonMappingException.class);
        JsonMappingException jsonMappingException = new JsonMappingException("Test exception");
        when(provider.isEnabled(SerializationFeature.WRAP_EXCEPTIONS)).thenReturn(false);
        serializer.wrapAndThrow(provider, jsonMappingException, null, null);
    }

    @Test
    public void testWrapAndThrowWithJsonMappingExceptionWithIndex() throws JsonMappingException {
        when(provider.isEnabled(SerializationFeature.WRAP_EXCEPTIONS)).thenReturn(true);
        JsonMappingException mappingException = new JsonMappingException("Test exception");
        when(provider.getSerializationConfig().getFactory().createGenerator(mock(OutputStream.class), null, ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT)).thenThrow(mappingException);

        serializer.wrapAndThrow(provider, mappingException, null, 0);
        JsonMappingException wrappedException = expectedException.expect(JsonMappingException.class);
        assertEquals(mappingException, wrappedException);
    }

    // Test for other helper methods

    @Test
    public void testIsDefaultSerializerWithNullSerializer() {
        assertFalse(StdSerializer.isDefaultSerializer(null));
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        FilterProvider filterProvider = mock(FilterProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        when(filterProvider.findPropertyFilter("testId", "testValue")).thenReturn(filter);

        PropertyFilter result = serializer.findPropertyFilter(provider, "testId", "testValue");
        assertEquals(filter, result);
    }

    private JsonNode createSchemaNode(String type, boolean isOptional) {
        ObjectNode schemaNode = createObjectNode();
        schemaNode.put("type", type);
        if (!isOptional) {
            schemaNode.put("required", !isOptional);
        }
        return schemaNode;
    }

    private ObjectNode createObjectNode() {
        return JsonNodeFactory.instance.objectNode();
    }
}
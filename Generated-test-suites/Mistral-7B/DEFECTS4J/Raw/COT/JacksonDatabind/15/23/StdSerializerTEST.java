package com.fasterxml.jackson.databind.ser.std; // Import necessary packages

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * JUnit 4 test cases for StdSerializer class
 */
public class StdSerializerTest {

    private StdSerializer<Object> stdSerializer;
    private SerializerProvider provider;
    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        stdSerializer = new StdSerializer<Object>(Object.class) {};
        provider = mock(SerializerProvider.class);
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setPropertyFilter(new PropertyFilter() {
            @Override
            public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider provider, String name)
                    throws IOException, JsonProcessingException {
                jgen.writeString(name);
            }

            @Override
            public boolean shouldFilterProperty(String name) {
                return name.equals("testProperty");
            }
        });
        mapper.setFilterProvider(new FilterProvider() {
            @Override
            public PropertyFilter findPropertyFilter(String id, Object value) {
                if (id.equals("testFilter")) {
                    return new PropertyFilter() {
                        @Override
                        public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider provider, String name)
                                throws IOException, JsonProcessingException {
                            jgen.writeString("filtered value");
                        }

                        @Override
                        public boolean shouldFilterProperty(String name) {
                            return true;
                        }
                    };
                }
                return null;
            }
        });
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(Object.class, stdSerializer.handledType());
    }

    @Test
    public void testAlternateConstructor() throws Exception {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class, false) {};
        assertEquals(String.class, stringSerializer.handledType());
    }

    @Test
    public void testSerializeMethod() throws Exception {
        ObjectNode rootNode = mapper.createObjectNode();
        when(provider.isEnabled(SerializationFeature.WRAP_EXCEPTIONS)).thenReturn(true);
        when(provider.getAnnotationIntrospector()).thenReturn(mock(AnnotationIntrospector.class));

        String testValue = "hello world";
        ObjectNode node = mapper.createObjectNode();
        node.put("testProperty", testValue);

        stdSerializer.serialize(node, new JsonGenerator(mapper.getFactory()), provider);

        ArgumentCaptor<JsonNode> jsonNodeCaptor = ArgumentCaptor.forClass(JsonNode.class);
        mapper.writeValueAsString(rootNode, stdSerializer); // Should call writeValueAsString internally
        assertEquals("{\"testProperty\":\"hello world\"}", mapper.writerWithRootValue(rootNode).writeValueAsString(rootNode));

        // Test wrapping exception
        when(provider.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS)).thenReturn(false);
        stdSerializer = mock(StdSerializer.class);
        when(stdSerializer.handledType()).thenReturn(String.class);
        when(stdSerializer.serialize(any(String.class), eq(new JsonGenerator(mapper.getFactory())), eq(provider)))
                .thenThrow(new IOException("Test exception"));

        when(provider.getAnnotationIntrospector()).thenReturn(mock(AnnotationIntrospector.class));
        when(provider.isEnabled(SerializationFeature.WRAP_EXCEPTIONS)).thenReturn(true);

        when(provider.findValueSerializer(String.class)).thenReturn(stdSerializer);

        mapper.writeValueAsString(null, "test");

        // Verify that the exception was wrapped and re-thrown
        JsonMappingException mappingException = assertEquals(
                "Error writing value [null] to test (required to be an instance of java.lang.String) (for representation: " +
                "null)\n at [Source: (String)null]\n",
                mapper.readValue(mapper.writerWithRootValue(mapper.createObjectNode()).writeValueAsString("{\"test\":null}"),
                        JsonNode.class).get("exception"));
        assertEquals("Test exception", mappingException.getCause().getMessage());
    }

    @Test
    public void testGetSchemaMethod() throws Exception {
        when(provider.getAnnotationIntrospector()).thenReturn(mock(AnnotationIntrospector.class));

        ObjectNode schemaNode = stdSerializer.getSchema(provider, Object.class);
        assertEquals("string", schemaNode.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitorMethod() throws Exception {
        when(provider.getSerializationConfig().isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS)).thenReturn(false);

        ObjectNode rootNode = mapper.createObjectNode();
        JsonFormatVisitorWrapper visitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JsonFormatVisitor jsonFormatVisitor = mock(JsonFormatVisitor.class);
        when(visitorWrapper.expectAnyFormat(any(JavaType.class))).thenReturn(jsonFormatVisitor);

        stdSerializer.acceptJsonFormatVisitor(visitorWrapper, Object.class);

        verify(visitorWrapper).expectAnyFormat(Object.class);
    }

    @Test
    public void testIsDefaultSerializerMethod() throws Exception {
        JsonSerializer<String> stringSerializer = mock(JsonSerializer.class);
        when(stringSerializer.handledType()).thenReturn(String.class);
        assertEquals(true, StdSerializer.class.newInstance().isDefaultSerializer(stringSerializer));
    }

    @Test
    public void testFindConvertingContentSerializerMethod() throws Exception {
        Converter<Object,Object> converter = mock(Converter.class);
        when(converter.getOutputType(any(TypeFactory.class))).thenReturn(String.class);
        JsonSerializer<String> stringSerializer = new StdDelegatingSerializer<>(converter, String.class, null);

        assertEquals(stringSerializer, stdSerializer.findConvertingContentSerializer(provider, null, null));
    }

    @Test
    public void testFindPropertyFilterMethod() throws Exception {
        when(provider.getTypeFactory().constructType(String[].class)).thenReturn(String[].class);
        PropertyFilter propertyFilter = stdSerializer.findPropertyFilter(provider, "testFilter", new String[]{"testValue"});

        assertEquals(propertyFilter.getClass().getName(), "com.fasterxml.jackson.databind.ser.PropertyFilterMap$1");
        assertEquals("filtered value", propertyFilter.serializeAsField(mapper.createValueInstance(String[].class), new JsonGenerator(mapper.getFactory()), provider, "testProperty"));
    }
}
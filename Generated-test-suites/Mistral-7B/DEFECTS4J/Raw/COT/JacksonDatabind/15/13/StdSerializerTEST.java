package com.yourpackage.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Before;
import org.junit.Test;

public class StdSerializerTest {

    private StdSerializer<Object> serializer;
    private SerializerProvider provider;
    private ObjectMapper mapper;

    @Before
    public void setup() {
        Class<Object> handledType = Object.class;
        AnnotatedClass annotatedClass = new AnnotatedClass(StdSerializer.class, handledType);
        this.serializer = new StdSerializer<Object>(handledType) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {}
        };
        this.provider = new SerializerProvider() {
            @Override
            public ObjectMapper getFactory() { return null; }
            @Override
            public JsonNodeFactory getNodeFactory() { return JsonNodeFactory.instance; }
            @Override
            public FilterProvider getFilterProvider() { return null; }
            @Override
            public JsonSerializer<?> findValueSerializer(Class<?> type) { return null; }
            @Override
            public void setNullValueSerializer(JsonSerializer<?> nullSerializer) {}
            @Override
            public boolean isEnabled(SerializationFeature feature) { return true; }
            @Override
            public void setNullValueSerializer(JsonSerializer<Object> nullSerializer) {}
            @Override
            public JsonSerializer<Object> findValueSerializer(Class<Object> type) { return null; }
            @Override
            public JsonSerializer<Object> findValueSerializer(Class<?> type, JsonSerializer<?> nullSerializer) { return null; }
            @Override
            public void setDefaultSerializerProvider(SerializerProvider provider) {}
        };
        this.mapper = new ObjectMapper();
    }

    @Test
    public void testConstructorWithClass() {
        StdSerializer<Object> actual = new StdSerializer<Object>(Object.class);
        StdSerializer<Object> expected = new StdSerializer<Object>(Object.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testConstructorWithJavaType() {
        StdSerializer<Object> actual = new StdSerializer<Object>(JsonTypeFactory.defaultInstance.constructType(Object.class));
        StdSerializer<Object> expected = new StdSerializer<Object>(Object.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testConstructorWithClassAndBoolean() {
        StdSerializer<Object> actual = new StdSerializer<Object>(Object.class, false);
        StdSerializer<Object> expected = new StdSerializer<Object>(Object.class, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testHandledTypeGetting() {
        assertEquals(Object.class, serializer.handledType());
    }

    @Test
    public void testSerializeNull() {
        try {
            serializer.serialize(null, null, provider);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testGetSchemaEmpty() {
        ObjectNode actual = serializer.getSchema(provider, null);
        ObjectNode expected = createSchemaNode("null");
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSchemaString() {
        ObjectNode actual = serializer.getSchema(provider, String.class);
        ObjectNode expected = createSchemaNode("string");
        assertEquals(expected, actual);
    }

    @Test
    public void testAcceptJsonFormatVisitorEmpty() {
        serializer.acceptJsonFormatVisitor(null, null);
    }

    @Test
    public void testAcceptJsonFormatVisitorCustom() {
        AnnotatedClass annotatedClass = new AnnotatedClass(Object.class, Object.class);
        ObjectNode expected = createSchemaNode("number", true);
        serializer.acceptJsonFormatVisitor(provider.getJsonFormatVisitorWrapper(annotatedClass), Object.class);
        ObjectNode actual = ((JsonFormatVisitorWrapper) provider.getJsonFormatVisitorWrapper(annotatedClass)).getSchemaNode();
        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowNullProvider() {
        serializer.wrapAndThrow(null, new RuntimeException(), null, "");
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowNullBean() {
        serializer.wrapAndThrow(provider, new RuntimeException(), null, "");
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowNullFieldName() {
        serializer.wrapAndThrow(provider, new RuntimeException(), null, 0);
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowRuntimeException() {
        serializer.wrapAndThrow(provider, new RuntimeException(), new Object(), "");
    }

    @Test
    public void testIsDefaultSerializerNull() {
        assertFalse(serializer.isDefaultSerializer(null));
    }

    @Test
    public void testIsDefaultSerializerDefaultSerializer() {
        assertTrue(serializer.isDefaultSerializer(serializer));
    }

    @Test
    public void testIsDefaultSerializerCustomSerializer() {
        assertFalse(serializer.isDefaultSerializer(new CustomSerializer<Object>()));
    }

    @Test
    public void testFindConvertingContentSerializerNull() {
        String expected = "";
        JsonSerializer<Object> actual = serializer.findConvertingContentSerializer(provider, null, null);
        assertEquals(null, actual);
        assertEquals(expected, actual.getClass().getName());
    }

    @Test
    public void testFindConvertingContentSerializerJsonSerializer() {
        Converter<Object,Object> converter = mapper.getSerializerProvider().getConverterInstance(String.class, String.class);
        JsonSerializer<Object> expected = new StdDelegatingSerializer(converter, JsonTypeFactory.defaultInstance.constructType(String.class), serializer);
        JsonSerializer<Object> actual = serializer.findConvertingContentSerializer(provider, String.class, "Test");
        assertEquals(expected, actual);
    }

    @Test
    public void testFindConvertingContentSerializerNullValueSerializer() {
        Converter<Object,Object> converter = mapper.getSerializerProvider().getConverterInstance(String.class, String.class);
        JsonSerializer<Object> expected = new StdDelegatingSerializer(converter, JsonTypeFactory.defaultInstance.constructType(String.class), null);
        JsonSerializer<Object> actual = serializer.findConvertingContentSerializer(provider, null, null);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindPropertyFilterNull() {
        try {
            serializer.findPropertyFilter(provider, null, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testFindPropertyFilterStringFilterId() {
        PropertyFilter expected = new JsonStringPropertyFilter();
        PropertyFilter actual = serializer.findPropertyFilter(provider, "jsonStringFilterId", "Test");
        assertEquals(expected, actual);
    }

    @Test
    public void testFindPropertyFilterNullFilterId() {
        PropertyFilter actual = serializer.findPropertyFilter(provider, null, null);
        assertNull(actual);
    }

    private ObjectNode createSchemaNode(String type, boolean isOptional) {
        ObjectNode schema = mapper.getNodeFactory().objectNode();
        schema.put("type", type);
        if (!isOptional) {
            schema.put("required", !isOptional);
        }
        return schema;
    }
}
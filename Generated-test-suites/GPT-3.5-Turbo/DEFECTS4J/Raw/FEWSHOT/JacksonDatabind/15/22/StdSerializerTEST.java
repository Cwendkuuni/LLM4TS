package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;

public class StdSerializerTest {
    @Test
    public void testConstructor() {
        StdSerializer<Object> serializer = new TestStdSerializer<>(Object.class);
        assertNotNull(serializer);
        assertEquals(Object.class, serializer.handledType());
    }
    
    @Test
    public void testConstructorWithType() {
        JavaType type = new ObjectMapper().constructType(Object.class);
        StdSerializer<Object> serializer = new TestStdSerializer<>(type);
        assertNotNull(serializer);
        assertEquals(Object.class, serializer.handledType());
    }
    
    @Test
    public void testConstructorWithDummy() {
        StdSerializer<Object> serializer = new TestStdSerializer<>(Object.class, true);
        assertNotNull(serializer);
        assertEquals(Object.class, serializer.handledType());
    }
    
    @Test
    public void testGetSchema() throws JsonMappingException {
        StdSerializer<Object> serializer = new TestStdSerializer<>(Object.class);
        JsonNode schema = serializer.getSchema(null, null);
        assertNotNull(schema);
        assertEquals("string", schema.get("type").asText());
    }
    
    @Test
    public void testGetSchemaWithOptional() throws JsonMappingException {
        StdSerializer<Object> serializer = new TestStdSerializer<>(Object.class);
        JsonNode schema = serializer.getSchema(null, null, true);
        assertNotNull(schema);
        assertEquals("string", schema.get("type").asText());
        assertNull(schema.get("required"));
        
        schema = serializer.getSchema(null, null, false);
        assertNotNull(schema);
        assertEquals("string", schema.get("type").asText());
        assertEquals(false, schema.get("required").asBoolean());
    }
    
    @Test
    public void testCreateObjectNode() {
        StdSerializer<Object> serializer = new TestStdSerializer<>(Object.class);
        assertNotNull(serializer.createObjectNode());
    }
    
    @Test
    public void testCreateSchemaNode() {
        StdSerializer<Object> serializer = new TestStdSerializer<>(Object.class);
        ObjectNode schema = serializer.createSchemaNode("string");
        assertNotNull(schema);
        assertEquals("string", schema.get("type").asText());
    }
    
    @Test
    public void testCreateSchemaNodeWithOptional() {
        StdSerializer<Object> serializer = new TestStdSerializer<>(Object.class);
        ObjectNode schema = serializer.createSchemaNode("string", true);
        assertNotNull(schema);
        assertEquals("string", schema.get("type").asText());
        assertEquals(false, schema.get("required").asBoolean());
        
        schema = serializer.createSchemaNode("string", false);
        assertNotNull(schema);
        assertEquals("string", schema.get("type").asText());
        assertNull(schema.get("required"));
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdSerializer<Object> serializer = new TestStdSerializer<>(Object.class);
        JsonFormatVisitorWrapper visitor = new ObjectMapper().getSerializationConfig()
                .getDefaultPrettyPrinter();
        serializer.acceptJsonFormatVisitor(visitor, null);
    }
    
    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<Object> serializer = new TestStdSerializer<>(Object.class);
        assertEquals(false, serializer.isDefaultSerializer(null));
    }
    
    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        StdSerializer<Object> serializer = new TestStdSerializer<>(Object.class);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        BeanProperty prop = null;
        JsonSerializer<?> existingSerializer = null;
        assertNull(serializer.findConvertingContentSerializer(provider, prop, existingSerializer));
    }
    
    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        StdSerializer<Object> serializer = new TestStdSerializer<>(Object.class);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        Object filterId = new Object();
        Object valueToFilter = new Object();
        assertNull(serializer.findPropertyFilter(provider, filterId, valueToFilter));
    }
    
    @Test(expected=JsonMappingException.class)
    public void testWrapAndThrowWithJsonMappingException() throws IOException {
        StdSerializer<Object> serializer = new TestStdSerializer<>(Object.class);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        Throwable t = new RuntimeException();
        Object bean = new Object();
        String fieldName = "field";
        serializer.wrapAndThrow(provider, t, bean, fieldName);
    }
    
    @Test(expected=RuntimeException.class)
    public void testWrapAndThrowWithRuntimeException() throws IOException {
        StdSerializer<Object> serializer = new TestStdSerializer<>(Object.class);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        Throwable t = new RuntimeException();
        Object bean = new Object();
        int index = 0;
        serializer.wrapAndThrow(provider, t, bean, index);
    }

    private static class TestStdSerializer<T> extends StdSerializer<T> {
        private static final long serialVersionUID = -305889892142123L;

        public TestStdSerializer(Class<T> t) {
            super(t);
        }

        public TestStdSerializer(JavaType type) {
            super(type);
        }

        public TestStdSerializer(Class<?> t, boolean dummy) {
            super(t, dummy);
        }

        @Override
        public void serialize(T value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
        }
    }
}
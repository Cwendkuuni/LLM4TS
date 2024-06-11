package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;

public class StdSerializerTest {
    
    private StdSerializer<StdSerializerTest> serializer;

    @Before
    public void setup() {
        serializer = new StdSerializer<StdSerializerTest>(StdSerializerTest.class) {   
            
            @Override
            public void serialize(StdSerializerTest value, JsonGenerator jgen, SerializerProvider provider) 
                    throws IOException, JsonGenerationException {
            }
            
        };
    }

    @Test
    public void testHandledType() {
        Class<StdSerializerTest> expected = StdSerializerTest.class;
        Class<StdSerializerTest> actual = serializer.handledType();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetSchema() {
        SerializerProvider provider = null;
        Type typeHint = null;
        
        ObjectNode expected = JsonNodeFactory.instance.objectNode();
        expected.put("type", "string");
        
        JsonNode actual = serializer.getSchema(provider, typeHint);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetSchemaWithIsOptionalTrue() {
        SerializerProvider provider = null;
        Type typeHint = null;
        boolean isOptional = true;
        
        ObjectNode expected = JsonNodeFactory.instance.objectNode();
        expected.put("type", "string");
        
        JsonNode actual = serializer.getSchema(provider, typeHint, isOptional);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetSchemaWithIsOptionalFalse() {
        SerializerProvider provider = null;
        Type typeHint = null;
        boolean isOptional = false;
        
        ObjectNode expected = JsonNodeFactory.instance.objectNode();
        expected.put("type", "string");
        expected.put("required", true);
        
        JsonNode actual = serializer.getSchema(provider, typeHint, isOptional);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCreateObjectNode() {
        ObjectNode expected = JsonNodeFactory.instance.objectNode();
        
        ObjectNode actual = serializer.createObjectNode();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCreateSchemaNodeWithType() {
        String type = "string";
        
        ObjectNode expected = JsonNodeFactory.instance.objectNode();
        expected.put("type", type);
        
        ObjectNode actual = serializer.createSchemaNode(type);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCreateSchemaNodeWithTypeAndIsOptionalTrue() {
        String type = "string";
        boolean isOptional = true;
        
        ObjectNode expected = JsonNodeFactory.instance.objectNode();
        expected.put("type", type);
        
        ObjectNode actual = serializer.createSchemaNode(type, isOptional);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCreateSchemaNodeWithTypeAndIsOptionalFalse() {
        String type = "string";
        boolean isOptional = false;
        
        ObjectNode expected = JsonNodeFactory.instance.objectNode();
        expected.put("type", type);
        expected.put("required", true);
        
        ObjectNode actual = serializer.createSchemaNode(type, isOptional);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() {
        JavaType typeHint = null;
        
        JsonFormatVisitorWrapper visitor = null;
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }
    
    @Test(expected = IOException.class)
    public void testWrapAndThrowWithIOException() throws IOException {
        SerializerProvider provider = null;
        Throwable t = new IOException();
        Object bean = null;
        String fieldName = null;
        
        serializer.wrapAndThrow(provider, t, bean, fieldName);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowWithJsonMappingException() throws IOException {
        SerializerProvider provider = null;
        Throwable t = new JsonMappingException("");
        Object bean = null;
        String fieldName = null;
        
        serializer.wrapAndThrow(provider, t, bean, fieldName);
    }
    
    @Test(expected = Error.class)
    public void testWrapAndThrowWithError() throws IOException {
        SerializerProvider provider = null;
        Throwable t = new Error();
        Object bean = null;
        String fieldName = null;
        
        serializer.wrapAndThrow(provider, t, bean, fieldName);
    }
    
    @Test
    public void testWrapAndThrowWithRuntimeException() throws IOException {
        SerializerProvider provider = null;
        Throwable t = new RuntimeException();
        Object bean = null;
        String fieldName = null;
        
        try {
            serializer.wrapAndThrow(provider, t, bean, fieldName);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // expected
        }
    }
    
    @Test
    public void testIsDefaultSerializer() {
        JsonSerializer<?> serializer = new StdSerializer<>(StdSerializerTest.class);
        
        boolean actual = this.serializer.isDefaultSerializer(serializer);
        
        assertFalse(actual);
    }
    
    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        SerializerProvider provider = null;
        BeanProperty prop = null;
        JsonSerializer<?> existingSerializer = null;
        
        JsonSerializer<?> actual = this.serializer.findConvertingContentSerializer(provider, prop, existingSerializer);
        
        assertNull(actual);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testFindPropertyFilterWithMissingFilterProvider() throws JsonMappingException {
        SerializerProvider provider = null;
        Object filterId = null;
        Object valueToFilter = null;
        
        serializer.findPropertyFilter(provider, filterId, valueToFilter);
    }
    
    @Test
    public void testFindPropertyFilterWithUnknownFilterId() throws JsonMappingException {
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        Object filterId = "unknown";
        Object valueToFilter = null;
        
        PropertyFilter actual = serializer.findPropertyFilter(provider, filterId, valueToFilter);
        
        assertNull(actual);
    }
    
}
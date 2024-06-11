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
import org.junit.Test;
import static org.junit.Assert.*;

public class StdSerializerTest {
    
    @Test
    public void testGetHandledType() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {};
        assertEquals(String.class, serializer.handledType());
    }
    
    @Test
    public void testSerialize() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
                // custom implementation
            }
        };
        assertNotNull(serializer);
        // TODO: add assertion for custom implementation
    }
    
    @Test
    public void testGetSchema() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                // custom implementation
                return null;
            }
        };
        assertNotNull(serializer.getSchema(null, null));
        // TODO: add assertion for custom implementation
    }
    
    @Test
    public void testGetSchemaWithOptional() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint, boolean isOptional) throws JsonMappingException {
                // custom implementation
                return null;
            }
        };
        assertNotNull(serializer.getSchema(null, null, false));
        // TODO: add assertion for custom implementation
    }
    
    @Test
    public void testCreateObjectNode() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {};
        assertNotNull(serializer.createObjectNode());
    }
    
    @Test
    public void testCreateSchemaNode() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {};
        assertNotNull(serializer.createSchemaNode("string"));
    }
    
    @Test
    public void testCreateSchemaNodeWithOptional() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {};
        assertNotNull(serializer.createSchemaNode("string", false));
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
                // custom implementation
            }
        };
        serializer.acceptJsonFormatVisitor(null, null);
        // TODO: add assertion for custom implementation
    }
            
    @Test(expected=IOException.class)
    public void testWrapAndThrowWithIOException() throws IOException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {};
        serializer.wrapAndThrow(null, new IOException(), null, null);
    }
    
    @Test(expected=JsonMappingException.class)
    public void testWrapAndThrowWithJsonMappingException() throws IOException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {};
        serializer.wrapAndThrow(null, new JsonMappingException(""), null, null);
    }
    
    @Test(expected=Error.class)
    public void testWrapAndThrowWithError() throws IOException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {};
        serializer.wrapAndThrow(null, new Error(), null, null);
    }
    
    @Test(expected=RuntimeException.class)
    public void testWrapAndThrowWithRuntimeException() throws IOException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {};
        serializer.wrapAndThrow(null, new RuntimeException(), null, null);
    }
    
    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {};
        assertFalse(serializer.isDefaultSerializer(null));
    }
    
    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {};
        assertNull(serializer.findConvertingContentSerializer(null, null, null));
    }
    
    @Test(expected=JsonMappingException.class)
    public void testFindPropertyFilterWithNoFilterProvider() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {};
        serializer.findPropertyFilter(null, null, null);
    }
    
    @Test
    public void testFindPropertyFilterWithFilterProvider() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {};
        assertNotNull(serializer.findPropertyFilter(new SerializerProvider(null, new ObjectMapper(), null), null, null));
    }
}
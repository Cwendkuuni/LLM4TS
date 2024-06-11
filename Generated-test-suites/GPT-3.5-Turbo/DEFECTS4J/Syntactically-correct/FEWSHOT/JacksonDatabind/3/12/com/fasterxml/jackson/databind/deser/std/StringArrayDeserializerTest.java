package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.DeserializationContext;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        
        // Test with an empty array
        JsonParser parser = createParser("[ ]");
        DeserializationContext ctxt = createDeserializationContext();
        String[] result = deserializer.deserialize(parser, ctxt);
        assertEquals(0, result.length);
        
        // Test with an array with one element
        parser = createParser("[ \"foo\" ]");
        result = deserializer.deserialize(parser, ctxt);
        assertEquals(1, result.length);
        assertEquals("foo", result[0]);
        
        // Test with an array with multiple elements
        parser = createParser("[ \"foo\", \"bar\", \"baz\" ]");
        result = deserializer.deserialize(parser, ctxt);
        assertEquals(3, result.length);
        assertEquals("foo", result[0]);
        assertEquals("bar", result[1]);
        assertEquals("baz", result[2]);
    }
    
    @Test
    public void testDeserializeWithType() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        
        // Test with an empty array
        JsonParser parser = createParser("[ ]");
        DeserializationContext ctxt = createDeserializationContext();
        TypeDeserializer typeDeserializer = createTypeDeserializer();
        Object result = deserializer.deserializeWithType(parser, ctxt, typeDeserializer);
        assertNull(result);
        
        // Test with an array with one element
        parser = createParser("[ \"foo\" ]");
        result = deserializer.deserializeWithType(parser, ctxt, typeDeserializer);
        assertTrue(result instanceof String[]);
        String[] arrayResult = (String[]) result;
        assertEquals(1, arrayResult.length);
        assertEquals("foo", arrayResult[0]);
        
        // Test with an array with multiple elements
        parser = createParser("[ \"foo\", \"bar\", \"baz\" ]");
        result = deserializer.deserializeWithType(parser, ctxt, typeDeserializer);
        assertTrue(result instanceof String[]);
        arrayResult = (String[]) result;
        assertEquals(3, arrayResult.length);
        assertEquals("foo", arrayResult[0]);
        assertEquals("bar", arrayResult[1]);
        assertEquals("baz", arrayResult[2]);
    }
    
    @Test
    public void testContextualize() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        // Test when _elementDeserializer is null
        DeserializationContext ctxt = createDeserializationContext();
        JsonDeserializer<?> deserializerResult = deserializer.createContextual(ctxt, null);
        assertNull(deserializerResult);
        
        // Test when _elementDeserializer is not null
        deserializer = new StringArrayDeserializer(new StringDeserializer());
        deserializerResult = deserializer.createContextual(ctxt, null);
        assertNotNull(deserializerResult);
        assertTrue(deserializerResult instanceof StringArrayDeserializer);
    }
    
    private JsonParser createParser(String json) throws Exception {
        JsonFactory factory = new JsonFactory();
        return factory.createParser(json);
    }
    
    private DeserializationContext createDeserializationContext() {
        ObjectMapper mapper = new ObjectMapper();
        return new DefaultDeserializationContext(mapper, null);
    }
    
    private TypeDeserializer createTypeDeserializer() {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.constructType(String[].class);
        return mapper.getDeserializerProvider().findTypeDeserializer(mapper.getDeserializationConfig(), javaType);
    }
}
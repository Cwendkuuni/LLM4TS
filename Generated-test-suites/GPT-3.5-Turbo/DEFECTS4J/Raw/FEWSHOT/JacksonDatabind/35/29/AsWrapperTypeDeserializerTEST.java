package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {
    private AsWrapperTypeDeserializer deserializer;
    private JavaType baseType;
    private TypeIdResolver typeIdResolver;
    private String typePropertyName;
    private JsonFactory jsonFactory;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    
    @Before
    public void setup() throws IOException {
        baseType = new ObjectMapper().getTypeFactory().constructType(Object.class);
        typeIdResolver = null;
        typePropertyName = "type";
        jsonFactory = new JsonFactory();
        jsonParser = jsonFactory.createParser("{\"type\":\"test\",\"value\":\"testValue\"}");
        deserializationContext = null;
        deserializer = new AsWrapperTypeDeserializer(baseType, typeIdResolver, typePropertyName, true, null);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(deserializer);
    }
    
    @Test
    public void testForProperty() {
        assertNull(deserializer.forProperty(null));   
    }
    
    @Test
    public void testGetTypeInclusion() {
        assertEquals(JsonToken.START_OBJECT, deserializer.getTypeInclusion());
    }
    
    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        Object result = deserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
        assertNotNull(result);
    }
    
    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        Object result = deserializer.deserializeTypedFromArray(jsonParser, deserializationContext);
        assertNotNull(result);
    }
    
    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        Object result = deserializer.deserializeTypedFromScalar(jsonParser, deserializationContext);
        assertNotNull(result);
    }
    
    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        Object result = deserializer.deserializeTypedFromAny(jsonParser, deserializationContext);
        assertNotNull(result);
    }
    
    private JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) {
        return null;
    }
    
    private String baseTypeName() {
        return "";
    }
}
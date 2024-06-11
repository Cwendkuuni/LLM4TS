package com.fasterxml.jackson.databind.jsontype.impl;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Test;

import static org.junit.Assert.*;

public class AsPropertyTypeDeserializerTest {
    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null,
                "typePropertyName", true, null, As.PROPERTY);
        
        // Test when p can read typeId
        JsonParser p = new JsonFactory().createParser("{}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        assertNull(deserializer.deserializeTypedFromObject(p, ctxt));
        
        // Test when p cannot read typeId
        p = new JsonFactory().createParser("{\"typePropertyName\":\"typeId\", \"name\":\"value\"}");
        assertNotNull(deserializer.deserializeTypedFromObject(p, ctxt));
    }
    
    @Test
    public void testDeserializeTypedForId() throws IOException {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null,
                "typePropertyName", true, null, As.PROPERTY);
        
        JsonParser p = new JsonFactory().createParser("{\"typePropertyName\":\"typeId\", \"name\":\"value\"}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        
        assertNull(deserializer._deserializeTypedForId(p, ctxt, null));
    }
    
    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws IOException {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null,
                "typePropertyName", true, null, As.PROPERTY);
        
        JsonParser p = new JsonFactory().createParser("{\"typePropertyName\":\"typeId\", \"name\":\"value\"}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        
        assertNull(deserializer._deserializeTypedUsingDefaultImpl(p, ctxt, null));
    }
    
    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null,
                "typePropertyName", true, null, As.PROPERTY);
        
        JsonParser p = new JsonFactory().createParser("{\"typePropertyName\":\"typeId\", \"name\":\"value\"}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        
        assertNotNull(deserializer.deserializeTypedFromAny(p, ctxt));
    }
}
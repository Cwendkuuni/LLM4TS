package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer.As;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() throws JsonParseException, JsonMappingException, IOException {
        JavaType bt = null;
        TypeIdResolver idRes = null;
        String typePropertyName = null;
        boolean typeIdVisible = false;
        JavaType defaultImpl = null;
        As inclusion = As.PROPERTY;
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl, inclusion);
        DeserializationContext ctxt = null;
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"type\":\"MyClass\",\"name\":\"test\"}";
        JsonNode node = mapper.readTree(json);
        assertEquals("test", deserializer.deserializeTypedFromObject(node.traverse(), ctxt));
    }

    @Test
    public void testDeserializeTypedForId() throws JsonParseException, JsonMappingException, IOException {
        JavaType bt = null;
        TypeIdResolver idRes = null;
        String typePropertyName = null;
        boolean typeIdVisible = false;
        JavaType defaultImpl = null;
        As inclusion = As.PROPERTY;
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl, inclusion);
        DeserializationContext ctxt = null;
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"type\":\"MyClass\",\"name\":\"test\"}";
        JsonNode node = mapper.readTree(json);
        assertEquals("test", deserializer.deserializeTypedForId(node.traverse(), ctxt, null));
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws JsonParseException, JsonMappingException, IOException {
        JavaType bt = null;
        TypeIdResolver idRes = null;
        String typePropertyName = null;
        boolean typeIdVisible = false;
        JavaType defaultImpl = null;
        As inclusion = As.PROPERTY;
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl, inclusion);
        DeserializationContext ctxt = null;
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"type\":\"MyClass\",\"name\":\"test\"}";
        JsonNode node = mapper.readTree(json);
        assertEquals("test", deserializer.deserializeTypedUsingDefaultImpl(node.traverse(), ctxt, null));
    }

    @Test
    public void testDeserializeTypedFromAny() throws JsonParseException, JsonMappingException, IOException {
        JavaType bt = null;
        TypeIdResolver idRes = null;
        String typePropertyName = null;
        boolean typeIdVisible = false;
        JavaType defaultImpl = null;
        As inclusion = As.PROPERTY;
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl, inclusion);
        DeserializationContext ctxt = null;
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"type\":\"MyClass\",\"name\":\"test\"}";
        JsonNode node = mapper.readTree(json);
        assertEquals("test", deserializer.deserializeTypedFromAny(node.traverse(), ctxt));
    }

}
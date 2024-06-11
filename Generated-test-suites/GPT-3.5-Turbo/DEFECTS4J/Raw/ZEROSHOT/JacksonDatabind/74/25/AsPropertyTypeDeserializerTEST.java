package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testConstructorWithProperties() {
        JavaType bt = null;
        TypeIdResolver idRes = null;
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = null;
        As inclusion = As.PROPERTY;
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName,
                typeIdVisible, defaultImpl, inclusion);
        assertNotNull(deserializer);
        assertEquals(inclusion, deserializer.getTypeInclusion());
    }

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("{ \"type\": \"sampleType\", \"property1\": \"value1\" }");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, "type", true, null);
        Object result = deserializer.deserializeTypedFromObject(jsonParser, ctxt);
        assertNotNull(result);
        assertEquals("sampleType", result.getClass().getSimpleName());
    }

    @Test
    public void testDeserializeTypedForId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("\"sampleType\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TokenBuffer tb = new TokenBuffer(jsonParser, ctxt);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, "type", true, null);
        Object result = deserializer._deserializeTypedForId(jsonParser, ctxt, tb);
        assertNotNull(result);
        assertEquals("sampleType", result.getClass().getSimpleName());
    }
    
    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("\"sampleType\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TokenBuffer tb = new TokenBuffer(jsonParser, ctxt);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, "type", true, null);
        Object result = deserializer._deserializeTypedUsingDefaultImpl(jsonParser, ctxt, tb);
        assertNull(result);
    }
}
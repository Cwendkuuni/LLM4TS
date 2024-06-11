package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsPropertyTypeDeserializerTest {
    @Test
    public void testAsPropertyTypeDeserializer() {
        JavaType bt = null;
        TypeIdResolver idRes = null;
        String typePropertyName = null;
        boolean typeIdVisible = false;
        JavaType defaultImpl = null;
        As inclusion = As.PROPERTY;
        AsPropertyTypeDeserializer aptd = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl, inclusion);
        assertNotNull(aptd);
        assertEquals(inclusion, aptd.getTypeInclusion());
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        AsPropertyTypeDeserializer aptd = new AsPropertyTypeDeserializer(null, null, null, false, null, As.PROPERTY);
        DeserializationContext ctxt = null;
        JsonParser p = null;
        Object result = aptd.deserializeTypedFromObject(p, ctxt);
        assertNull(result);
    }
    
    @Test
    public void testDeserializeTypedForId() throws IOException {
        AsPropertyTypeDeserializer aptd = new AsPropertyTypeDeserializer(null, null, null, false, null, As.PROPERTY);
        DeserializationContext ctxt = null;
        JsonParser p = null;
        TokenBuffer tb = null;
        Object result = aptd._deserializeTypedForId(p, ctxt, tb);
        assertNull(result);
    }
    
    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws IOException {
        AsPropertyTypeDeserializer aptd = new AsPropertyTypeDeserializer(null, null, null, false, null, As.PROPERTY);
        DeserializationContext ctxt = null;
        JsonParser p = null;
        TokenBuffer tb = null;
        Object result = aptd._deserializeTypedUsingDefaultImpl(p, ctxt, tb);
        assertNull(result);
    }
    
    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        AsPropertyTypeDeserializer aptd = new AsPropertyTypeDeserializer(null, null, null, false, null, As.PROPERTY);
        DeserializationContext ctxt = null;
        JsonParser p = null;
        Object result = aptd.deserializeTypedFromAny(p, ctxt);
        assertNull(result);
    }
}
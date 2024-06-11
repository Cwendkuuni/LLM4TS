package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AsPropertyTypeDeserializerTest {

    private AsPropertyTypeDeserializer deserializer;
    private JavaType bt;
    private TypeIdResolver idRes;
    private String typePropertyName;
    private boolean typeIdVisible;
    private JavaType defaultImpl;
    private As inclusion;
    private BeanProperty property;
    
    @Before
    public void setUp() {
        bt = new JavaType();
        idRes = new TypeIdResolver();
        typePropertyName = "typePropertyName";
        typeIdVisible = true;
        defaultImpl = new JavaType();
        inclusion = As.PROPERTY;
        deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl, inclusion);
        property = new BeanProperty();
        deserializer = new AsPropertyTypeDeserializer(deserializer, property);
    }

    @Test
    public void testForProperty() {
        TypeDeserializer result = deserializer.forProperty(property);
        assertEquals(deserializer, result);
    }
    
    @Test
    public void testGetTypeInclusion() {
        As result = deserializer.getTypeInclusion();
        assertEquals(inclusion, result);
    }

    @Test
    public void testDeserializeTypedFromObject() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object result = deserializer.deserializeTypedFromObject(p, ctxt);
        assertNotNull(result);
    }

    @Test
    public void testDeserializeTypedForId() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        TokenBuffer tb = new TokenBuffer(p, ctxt);
        tb.writeString("typeId");
        String typeId = "typeId";
        JsonDeserializer<Object> deser = new JsonDeserializer<Object>();
        Object result = deserializer.deserializeTypedForId(p, ctxt, tb);
        assertNotNull(result);
    }
    
    @Test
    public void testDeserializeTypedUsingDefaultImpl() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        TokenBuffer tb = new TokenBuffer(p, ctxt);
        Object result = deserializer.deserializeTypedUsingDefaultImpl(p, ctxt, tb);
        assertNotNull(result);
    }
    
    @Test
    public void testDeserializeTypedFromAny() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object result = deserializer.deserializeTypedFromAny(p, ctxt);
        assertNotNull(result);
    }
}
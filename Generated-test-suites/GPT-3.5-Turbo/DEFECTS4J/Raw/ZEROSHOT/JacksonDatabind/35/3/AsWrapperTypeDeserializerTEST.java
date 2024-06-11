package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {

    private AsWrapperTypeDeserializer asWrapperTypeDeserializer;

    @Before
    public void setUp() {
        JavaType bt = new JavaType();
        TypeIdResolver idRes = new TypeIdResolver();
        String typePropertyName = "testType";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        
        asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test
    public void testForProperty() {
        BeanProperty prop = new BeanProperty();

        TypeDeserializer result = asWrapperTypeDeserializer.forProperty(prop);

        assertNotNull(result);
        assertNotSame(asWrapperTypeDeserializer, result);
        assertTrue(result instanceof AsWrapperTypeDeserializer);
        assertEquals(prop, result._property);
    }

    @Test
    public void testGetTypeInclusion() {
        As result = asWrapperTypeDeserializer.getTypeInclusion();

        assertEquals(As.WRAPPER_OBJECT, result);
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();

        Object result = asWrapperTypeDeserializer.deserializeTypedFromObject(jp, ctxt);

        assertNotNull(result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();

        Object result = asWrapperTypeDeserializer.deserializeTypedFromArray(jp, ctxt);

        assertNotNull(result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();

        Object result = asWrapperTypeDeserializer.deserializeTypedFromScalar(jp, ctxt);

        assertNotNull(result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();

        Object result = asWrapperTypeDeserializer.deserializeTypedFromAny(jp, ctxt);

        assertNotNull(result);
    }

    private class BeanProperty { }
    
    private class JavaType { }
    
    private class TypeIdResolver { }
}
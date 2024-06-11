package com.fasterxml.jackson.databind.jsontype.impl;

import java.io.IOException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {
    private DeserializationContext ctxtMock;
    private AsWrapperTypeDeserializer asWrapperTypeDeserializer;

    @Before
    public void setUp() {
        JavaType bt = null;
        TypeIdResolver idRes = null;
        String typePropertyName = "";
        boolean typeIdVisible = false;
        Class<?> defaultImpl = null;
        asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test
    public void testForProperty() {
        BeanProperty prop = null;
        TypeDeserializer result = asWrapperTypeDeserializer.forProperty(prop);
        Assert.assertEquals(asWrapperTypeDeserializer, result);
    }

    @Test
    public void testGetTypeInclusion() {
        As result = asWrapperTypeDeserializer.getTypeInclusion();
        Assert.assertEquals(As.WRAPPER_OBJECT, result);
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        JsonParser jp = null;
        Object result = asWrapperTypeDeserializer.deserializeTypedFromObject(jp, ctxtMock);
        // add assertions for the expected return value
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        JsonParser jp = null;
        Object result = asWrapperTypeDeserializer.deserializeTypedFromArray(jp, ctxtMock);
        // add assertions for the expected return value
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        JsonParser jp = null;
        Object result = asWrapperTypeDeserializer.deserializeTypedFromScalar(jp, ctxtMock);
        // add assertions for the expected return value
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        JsonParser jp = null;
        Object result = asWrapperTypeDeserializer.deserializeTypedFromAny(jp, ctxtMock);
        // add assertions for the expected return value
    }

    /* Internal methods */

    @Test
    public void test_deserialize() throws IOException {
        JsonParser p = null;
        Object result = asWrapperTypeDeserializer._deserialize(p, ctxtMock);
        // add assertions for the expected return value
    }
}
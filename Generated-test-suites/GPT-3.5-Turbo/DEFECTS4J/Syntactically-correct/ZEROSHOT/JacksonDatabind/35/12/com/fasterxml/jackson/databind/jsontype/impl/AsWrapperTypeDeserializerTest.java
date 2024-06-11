package com.fasterxml.jackson.databind.jsontype.impl;

import java.io.IOException;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsWrapperTypeDeserializerTest {

    private AsWrapperTypeDeserializer asWrapperTypeDeserializer;

    @Before
    public void setup() {
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
        assertNotNull(result);
    }

    @Test
    public void testGetTypeInclusion() {
        As result = asWrapperTypeDeserializer.getTypeInclusion();
        assertEquals(As.WRAPPER_OBJECT, result);
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object result = asWrapperTypeDeserializer.deserializeTypedFromObject(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object result = asWrapperTypeDeserializer.deserializeTypedFromArray(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object result = asWrapperTypeDeserializer.deserializeTypedFromScalar(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object result = asWrapperTypeDeserializer.deserializeTypedFromAny(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void test_deserialize() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object result = asWrapperTypeDeserializer._deserialize(p, ctxt);
        assertNull(result);
    }
}
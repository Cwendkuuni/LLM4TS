package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);

        // Test when type id is null
        JsonParser jp = null;
        DeserializationContext context = null;
        assertNull(deserializer.deserializeTypedFromObject(jp, context));

        // Test when type id is not null
        TypeIdResolver idRes = null;
        JavaType bt = null;
        String typePropertyName = null;
        boolean typeIdVisible = false;
        Class<?> defaultImpl = null;
        deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
        // Test when type id is present
        jp = null;
        context = null;
        assertNull(deserializer.deserializeTypedFromObject(jp, context));
    }

    @Test
    public void testDeserializeTypedFromArray() {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);

        // Test when type id is null
        JsonParser jp = null;
        DeserializationContext context = null;
        assertNull(deserializer.deserializeTypedFromArray(jp, context));

        // Test when type id is not null
        TypeIdResolver idRes = null;
        JavaType bt = null;
        String typePropertyName = null;
        boolean typeIdVisible = false;
        Class<?> defaultImpl = null;
        deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
        // Test when type id is present
        jp = null;
        context = null;
        assertNull(deserializer.deserializeTypedFromArray(jp, context));
    }

    @Test
    public void testDeserializeTypedFromScalar() {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);

        // Test when type id is null
        JsonParser jp = null;
        DeserializationContext context = null;
        assertNull(deserializer.deserializeTypedFromScalar(jp, context));

        // Test when type id is not null
        TypeIdResolver idRes = null;
        JavaType bt = null;
        String typePropertyName = null;
        boolean typeIdVisible = false;
        Class<?> defaultImpl = null;
        deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
        // Test when type id is present
        jp = null;
        context = null;
        assertNull(deserializer.deserializeTypedFromScalar(jp, context));
    }

    @Test
    public void testDeserializeTypedFromAny() {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);

        // Test when type id is null
        JsonParser jp = null;
        DeserializationContext context = null;
        assertNull(deserializer.deserializeTypedFromAny(jp, context));

        // Test when type id is not null
        TypeIdResolver idRes = null;
        JavaType bt = null;
        String typePropertyName = null;
        boolean typeIdVisible = false;
        Class<?> defaultImpl = null;
        deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
        // Test when type id is present
        jp = null;
        context = null;
        assertNull(deserializer.deserializeTypedFromAny(jp, context));
    }

    @Test
    public void test_deserialize() {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);

        // Test when type id is null
        JsonParser p = null;
        DeserializationContext ctxt = null;
        assertNull(deserializer._deserialize(p, ctxt));

        // Test when type id is not null
        TokenBuffer tb = null;
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object typeId = null;
        assertNull(deserializer._deserializeWithNativeTypeId(p, ctxt, typeId));
    }
}
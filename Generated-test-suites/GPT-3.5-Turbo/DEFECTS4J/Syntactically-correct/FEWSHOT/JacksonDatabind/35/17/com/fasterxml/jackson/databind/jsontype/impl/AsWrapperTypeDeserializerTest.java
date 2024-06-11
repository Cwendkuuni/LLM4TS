package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;

public class AsWrapperTypeDeserializerTest {
    private AsWrapperTypeDeserializer asWrapperTypeDeserializer;
    private JavaType baseType;
    private TypeIdResolver typeIdResolver;
    private String typePropertyName;
    private boolean typeIdVisible;
    private Class<?> defaultImpl;

    @Before
    public void setup() {
        baseType = TypeFactory.defaultInstance().constructType(Object.class);
        typeIdResolver = null;
        typePropertyName = "type";
        typeIdVisible = true;
        defaultImpl = null;
        asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(baseType, typeIdResolver, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test
    public void testForProperty() {
        BeanProperty property = null;
        assertEquals(asWrapperTypeDeserializer, asWrapperTypeDeserializer.forProperty(property));
    }

    @Test
    public void testGetTypeInclusion() {
        assertEquals(As.WRAPPER_OBJECT, asWrapperTypeDeserializer.getTypeInclusion());
    }

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object expectedResult = null;
        assertEquals(expectedResult, asWrapperTypeDeserializer.deserializeTypedFromObject(jp, ctxt));
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object expectedResult = null;
        assertEquals(expectedResult, asWrapperTypeDeserializer.deserializeTypedFromArray(jp, ctxt));
    }

    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object expectedResult = null;
        assertEquals(expectedResult, asWrapperTypeDeserializer.deserializeTypedFromScalar(jp, ctxt));
    }

    @Test
    public void testDeserializeTypedFromAny() throws Exception {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object expectedResult = null;
        assertEquals(expectedResult, asWrapperTypeDeserializer.deserializeTypedFromAny(jp, ctxt));
    }

    @Test
    public void testDeserialize() throws Exception {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeIdResolver typeIdResolver = null;
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = null;
        asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(baseType, typeIdResolver, typePropertyName, typeIdVisible, defaultImpl);

        Object expectedResult = null;
        assertEquals(expectedResult, asWrapperTypeDeserializer.deserialize(p, ctxt));
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object typeId = null;
        Class<?> expectedResult = null;
        assertEquals(expectedResult, asWrapperTypeDeserializer.deserializeWithNativeTypeId(p, ctxt, typeId));
    }

    @Test
    public void testDeserializeWithNativeTypeId_nullTypeId() throws Exception {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object typeId = null;
        Class<?> expectedResult = null;
        assertEquals(expectedResult, asWrapperTypeDeserializer.deserializeWithNativeTypeId(p, ctxt, typeId));
    }

    @Test
    public void testDeserializeWithNativeTypeId_notNullTypeId() throws Exception {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object typeId = new Object();
        Class<?> expectedResult = null;
        assertEquals(expectedResult, asWrapperTypeDeserializer.deserializeWithNativeTypeId(p, ctxt, typeId));
    }

    @Test
    public void testDeserializeTypedForObject() throws Exception {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String typeId = "";
        JsonDeserializer<Object> expectedResult = null;
        JsonDeserializer<Object> actualResult = asWrapperTypeDeserializer.deserializeTypedForObject(jp, ctxt, typeId);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDeserializeTypedForArray() throws Exception {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String typeId = "";
        JsonDeserializer<Object> expectedResult = null;
        JsonDeserializer<Object> actualResult = asWrapperTypeDeserializer.deserializeTypedForArray(jp, ctxt, typeId);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDeserializeTypedForScalar() throws Exception {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String typeId = "";
        JsonDeserializer<Object> expectedResult = null;
        JsonDeserializer<Object> actualResult = asWrapperTypeDeserializer.deserializeTypedForScalar(jp, ctxt, typeId);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDeserializeTyped() throws Exception {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String typeId = "";
        JsonDeserializer<Object> expectedResult = null;
        JsonDeserializer<Object> actualResult = asWrapperTypeDeserializer.deserializeTyped(jp, ctxt, typeId);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void test_findDeserializer() throws Exception {
        DeserializationContext ctxt = null;
        JsonNode node = null;
        JsonDeserializer<Object> expectedResult = null;
        String typeId = "";
        JsonDeserializer<Object> actualResult = asWrapperTypeDeserializer._findDeserializer(ctxt, node, typeId);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void test_findDeserializer_nullNode() throws Exception {
        DeserializationContext ctxt = null;
        JsonNode node = null;
        JsonDeserializer<Object> expectedResult = null;
        String typeId = "";
        JsonDeserializer<Object> actualResult = asWrapperTypeDeserializer._findDeserializer(ctxt, node, typeId);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void test_findDeserializer_objectNode() throws Exception {
        DeserializationContext ctxt = null;
        JsonNode node = null;
        JsonDeserializer<Object> expectedResult = null;
        String typeId = "";
        JsonDeserializer<Object> actualResult = asWrapperTypeDeserializer._findDeserializer(ctxt, node, typeId);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void test_findDeserializer_otherNode() throws Exception {
        DeserializationContext ctxt = null;
        JsonNode node = null;
        JsonDeserializer<Object> expectedResult = null;
        String typeId = "";
        JsonDeserializer<Object> actualResult = asWrapperTypeDeserializer._findDeserializer(ctxt, node, typeId);
        assertEquals(expectedResult, actualResult);
    }
}
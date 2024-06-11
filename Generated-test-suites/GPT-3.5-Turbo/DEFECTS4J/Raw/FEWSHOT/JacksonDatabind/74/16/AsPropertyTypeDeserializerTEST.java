package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testAsPropertyTypeDeserializer() {
        JavaType baseType = null;
        TypeIdResolver idRes = null;
        String typePropertyName = "";
        boolean typeIdVisible = true;
        JavaType defaultImpl = null;
        As inclusion = As.WRAPPER_ARRAY;
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl, inclusion);
        assertNotNull(deserializer);
    }

    @Test
    public void testForProperty() {
        JavaType baseType = null;
        TypeIdResolver idRes = null;
        String typePropertyName = "";
        boolean typeIdVisible = true;
        JavaType defaultImpl = null;
        As inclusion = As.WRAPPER_ARRAY;
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl, inclusion);
        BeanProperty prop = null;
        TypeDeserializer result = deserializer.forProperty(prop);
        assertNotNull(result);
    }

    @Test
    public void testGetTypeInclusion() {
        JavaType baseType = null;
        TypeIdResolver idRes = null;
        String typePropertyName = "";
        boolean typeIdVisible = true;
        JavaType defaultImpl = null;
        As inclusion = As.WRAPPER_ARRAY;
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl, inclusion);
        As result = deserializer.getTypeInclusion();
        assertEquals(inclusion, result);
    }

    @Test
    public void testDeserializeTypedFromAny() {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, "", true, null);

        JsonParser p = new JsonFactory().createParser("[]");
        DeserializationContext ctxt = null;
        try {
            deserializer.deserializeTypedFromAny(p, ctxt);
            fail("Exception not thrown for invalid JSON token");
        } catch (IOException e) {
            assertTrue(true);
        }
    }
}
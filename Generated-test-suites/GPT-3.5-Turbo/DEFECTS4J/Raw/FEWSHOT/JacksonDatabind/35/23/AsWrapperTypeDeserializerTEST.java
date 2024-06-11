package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {
    @Test
    public void testConstructor() {
        JavaType bt = null;
        TypeIdResolver idRes = null;
        String typePropertyName = "type";
        boolean typeIdVisible = false;
        Class<?> defaultImpl = null;

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);

        assertSame(bt, deserializer.baseType());
        assertSame(idRes, deserializer._idResolver);
        assertEquals(typePropertyName, deserializer._typePropertyName);
        assertEquals(typeIdVisible, deserializer._typeIdVisible);
        assertSame(defaultImpl, deserializer._defaultImpl);
    }

    @Test
    public void testForProperty() {
        JavaType bt = null;
        TypeIdResolver idRes = null;
        String typePropertyName = "type";
        boolean typeIdVisible = false;
        Class<?> defaultImpl = null;
        BeanProperty property = null;

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
        TypeDeserializer typeDeserializer = deserializer.forProperty(property);

        assertSame(property, typeDeserializer.getProperty());
        assertSame(deserializer, typeDeserializer);
    }

    @Test
    public void testGetTypeInclusion() {
        JavaType bt = null;
        TypeIdResolver idRes = null;
        String typePropertyName = "type";
        boolean typeIdVisible = false;
        Class<?> defaultImpl = null;

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);

        assertEquals(As.WRAPPER_OBJECT, deserializer.getTypeInclusion());
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        JavaType bt = null;
        TypeIdResolver idRes = null;
        String typePropertyName = "type";
        boolean typeIdVisible = false;
        Class<?> defaultImpl = null;
        JsonParser jp = null;
        DeserializationContext ctxt = null;

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);

        Object result = deserializer.deserializeTypedFromObject(jp, ctxt);

        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        JavaType bt = null;
        TypeIdResolver idRes = null;
        String typePropertyName = "type";
        boolean typeIdVisible = false;
        Class<?> defaultImpl = null;
        JsonParser jp = null;
        DeserializationContext ctxt = null;

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);

        Object result = deserializer.deserializeTypedFromArray(jp, ctxt);

        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        JavaType bt = null;
        TypeIdResolver idRes = null;
        String typePropertyName = "type";
        boolean typeIdVisible = false;
        Class<?> defaultImpl = null;
        JsonParser jp = null;
        DeserializationContext ctxt = null;

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);

        Object result = deserializer.deserializeTypedFromScalar(jp, ctxt);

        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        JavaType bt = null;
        TypeIdResolver idRes = null;
        String typePropertyName = "type";
        boolean typeIdVisible = false;
        Class<?> defaultImpl = null;
        JsonParser jp = null;
        DeserializationContext ctxt = null;

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);

        Object result = deserializer.deserializeTypedFromAny(jp, ctxt);

        assertNull(result);
    }

    @Test
    public void testDeserialize() throws IOException {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, "type", true, null);

        JsonParser p = new JsonFactory().createParser("{}");
        DeserializationContext ctxt = new ObjectMapper().createDeserializationContext(p);
        String typeId = "TestType";
        JsonDeserializer<Object> deser = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "testValue";
            }
        };
        Map<String, JsonDeserializer<Object>> deserializers = new HashMap<>();
        deserializers.put(typeId, deser);
        deserializer._deserializers = deserializers;
        p.nextToken(); // move to start_object
        p.nextToken(); // move to field_name
        p.nextToken(); // move to field_value

        Object result = deserializer._deserialize(p, ctxt);

        assertNotNull(result);
        assertEquals("testValue", result);
    }
}
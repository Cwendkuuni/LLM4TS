package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase base = new TypeDeserializerBaseImpl(null, null, null, true, null);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseImpl", base.baseTypeName());
    }

    @Test
    public void testGetTypeInclusion() {
        TypeDeserializerBase base = new TypeDeserializerBaseImpl(null, null, null, true, null);
        assertEquals(JsonTypeInfo.As.PROPERTY, base.getTypeInclusion());
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase base = new TypeDeserializerBaseImpl(null, null, "type", true, null);
        assertEquals("type", base.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver resolver = new TypeIdResolver() {
            @Override
            public String idFromBaseType() {
                return null;
            }
            @Override
            public String idFromValueAndType(Object value, Class<?> suggestedType) {
                return null;
            }
            @Override
            public JavaType typeFromId(String id) {
                return null;
            }
            @Override
            public String getDescForKnownTypeIds() {
                return null;
            }
        };
        TypeDeserializerBase base = new TypeDeserializerBaseImpl(null, resolver, null, true, null);
        assertEquals(resolver, base.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        JavaType defaultImpl = JavaType.construct(String.class);
        TypeDeserializerBase base = new TypeDeserializerBaseImpl(null, null, null, true, defaultImpl);
        assertEquals(String.class, base.getDefaultImpl());
    }

    @Test
    public void testBaseType() {
        JavaType baseType = JavaType.construct(String.class);
        TypeDeserializerBase base = new TypeDeserializerBaseImpl(baseType, null, null, true, null);
        assertEquals(baseType, base.baseType());
    }

    @Test
    public void testToString() {
        TypeDeserializerBase base = new TypeDeserializerBaseImpl(null, null, null, true, null);
        assertTrue(base.toString().startsWith("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseImpl"));
    }

    @Test
    public void testFindDeserializer() throws IOException {
        DeserializationContext ctxt = null; // create actual context if needed
        TypeDeserializerBase base = new TypeDeserializerBaseImpl(null, null, null, true, null);
        JsonParser jp = null; // create actual JSON parser if needed
        JsonDeserializer<Object> deser = base._findDeserializer(ctxt, "dummy");
        assertNotNull(deser);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        DeserializationContext ctxt = null; // create actual context if needed
        TypeDeserializerBase base = new TypeDeserializerBaseImpl(null, null, null, true, null);
        JsonDeserializer<Object> deser = base._findDefaultImplDeserializer(ctxt);
        assertNull(deser);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = null; // create actual context if needed
        TypeDeserializerBase base = new TypeDeserializerBaseImpl(null, null, null, true, null);
        JavaType actual = base._handleUnknownTypeId(ctxt, "unknown");
        assertNull(actual);
    }

    @Test
    public void testHandleMissingTypeId() throws IOException {
        DeserializationContext ctxt = null; // create actual context if needed
        TypeDeserializerBase base = new TypeDeserializerBaseImpl(null, null, null, true, null);
        JavaType actual = base._handleMissingTypeId(ctxt, "missing");
        assertNull(actual);
    }

    private static class TypeDeserializerBaseImpl extends TypeDeserializerBase {
        public TypeDeserializerBaseImpl(JavaType baseType, TypeIdResolver idRes, String typePropertyName,
                boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.PROPERTY;
        }
    }
}
package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setUp() {
        TypeIdResolver typeIdResolver = new TypeIdResolver() {
            @Override
            public String idFromValue(Object value) {
                return null;
            }

            @Override
            public String idFromValueAndType(Object value, Class<?> suggestedType) {
                return null;
            }

            @Override
            public JavaType typeFromId(DatabindContext context, String id) throws IOException {
                return null;
            }

            @Override
            public JavaType typeFromId(DatabindContext context, String id, Object extraInfo) throws IOException {
                return null;
            }

            @Override
            public String getDescForKnownTypeIds() {
                return null;
            }

            @Override
            public JsonTypeInfo.Id getMechanism() {
                return null;
            }
        };

        JavaType baseType = null;
        String typePropertyName = "";
        boolean typeIdVisible = false;
        JavaType defaultImpl = null;

        typeDeserializerBase = new TypeDeserializerBaseTestImplementation(baseType, typeIdResolver,
                typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As expected = null;
        JsonTypeInfo.As actual = typeDeserializerBase.getTypeInclusion();
        assertEquals(expected, actual);
    }

    @Test
    public void testBaseTypeName() {
        String expected = null;
        String actual = typeDeserializerBase.baseTypeName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyName() {
        String expected = "";
        String actual = typeDeserializerBase.getPropertyName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver expected = null;
        TypeIdResolver actual = typeDeserializerBase.getTypeIdResolver();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> expected = null;
        Class<?> actual = typeDeserializerBase.getDefaultImpl();
        assertEquals(expected, actual);
    }

    @Test
    public void testBaseType() {
        JavaType expected = null;
        JavaType actual = typeDeserializerBase.baseType();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String expected = "[]";
        String actual = typeDeserializerBase.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testFindDeserializerWithCorrectTypeId() throws IOException {
        DeserializationContext ctxt = null;
        String typeId = "someType";
        JsonDeserializer<Object> expected = null;
        JsonDeserializer<Object> actual = typeDeserializerBase._findDeserializer(ctxt, typeId);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindDeserializerWithNullTypeId() throws IOException {
        DeserializationContext ctxt = null;
        String typeId = null;
        JsonDeserializer<Object> expected = null;
        JsonDeserializer<Object> actual = typeDeserializerBase._findDeserializer(ctxt, typeId);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindDeserializerWithUnknownTypeId() throws IOException {
        DeserializationContext ctxt = null;
        String typeId = "unknownType";
        JsonDeserializer<Object> expected = null;
        JsonDeserializer<Object> actual = typeDeserializerBase._findDeserializer(ctxt, typeId);
        assertEquals(expected, actual);
    }

    @Test
    public void testHandleUnknownTypeId() {
        DeserializationContext ctxt = null;
        String typeId = "unknownType";
        String extraDesc = "";
        JavaType expected = null;
        JavaType actual = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId);
        assertEquals(expected, actual);
    }

    private static class TypeDeserializerBaseTestImplementation extends TypeDeserializerBase {

        public TypeDeserializerBaseTestImplementation(JavaType baseType, TypeIdResolver idRes,
                                                      String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }
    }
}
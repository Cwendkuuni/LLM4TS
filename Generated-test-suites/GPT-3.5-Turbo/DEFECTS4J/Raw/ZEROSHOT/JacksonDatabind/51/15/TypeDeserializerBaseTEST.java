package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setUp() {
        typeDeserializerBase = new TypeDeserializerBase(JavaType.class, new TypeIdResolver() {
            @Override
            public void init(JavaType baseType) {

            }

            @Override
            public String idFromValue(Object value) {
                return null;
            }

            @Override
            public String idFromValueAndType(Object value, Class<?> suggestedType) {
                return null;
            }

            @Override
            public JavaType typeFromId(DatabindContext context, String id) {
                return null;
            }

            @Override
            public String getDescForKnownTypeIds() {
                return null;
            }

            @Override
            public Id getMechanism() {
                return null;
            }
        }, "typePropertyName", true, JavaType.class) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
    }

    @Test
    public void testBaseTypeName() {
        assertEquals("com.fasterxml.jackson.databind.JavaType", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("typePropertyName", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertNotNull(typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        assertNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() {
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:com.fasterxml.jackson.databind.JavaType; id-resolver: null]", typeDeserializerBase.toString());
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        assertNull(typeDeserializerBase._deserializeWithNativeTypeId(null, null));
    }

    @Test
    public void testDeserializeWithNativeTypeIdWithTypeId() throws IOException {
        assertNull(typeDeserializerBase._deserializeWithNativeTypeId(null, null, "typeId"));
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        assertNull(typeDeserializerBase._handleUnknownTypeId(null, null, null, null));
    }
}
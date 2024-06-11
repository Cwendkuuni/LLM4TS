package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.BeforeClass;
import org.junit.Test;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    private static TypeDeserializerBase typeDeserializerBase;

    @BeforeClass
    public static void setUp() {
        JavaType baseType = new JavaType();
        TypeIdResolver idResolver = new TypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = new JavaType();
        typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl) {
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
        assertEquals("java.lang.Object", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("type", typeDeserializerBase.getPropertyName());
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
    public void testBaseType() {
        assertNotNull(typeDeserializerBase.baseType());
    }

    @Test
    public void testToString() {
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseTest" +
                "$1; base-type:JavaType [rawType=null]; id-resolver: com.fasterxml.jackson.databind.jsontype.TypeIdResolver@mock]";
        assertEquals(expected, typeDeserializerBase.toString());
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        assertNull(typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt));
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = new DeserializationContext();
        String typeId = "unknown";
        String extraDesc = "type ids are not statically known";
        assertNull(typeDeserializerBase._handleUnknownTypeId(ctxt, typeId));
    }

    @Test
    public void testHandleMissingTypeId() throws IOException {
        DeserializationContext ctxt = new DeserializationContext();
        String extraDesc = "type ids are not statically known";
        assertNull(typeDeserializerBase._handleMissingTypeId(ctxt, extraDesc));
    }
}
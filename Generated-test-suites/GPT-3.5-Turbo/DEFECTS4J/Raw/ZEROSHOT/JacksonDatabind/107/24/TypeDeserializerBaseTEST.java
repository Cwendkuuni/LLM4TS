package com.fasterxml.jackson.databind.jsontype.impl;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(JavaType) {};
        String result = typeDeserializerBase.baseTypeName();
        assertEquals(result, "java.lang.Object");
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(JavaType) {};
        String result = typeDeserializerBase.getPropertyName();
        assertEquals(result, "_typePropertyName");
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(JavaType) {};
        TypeIdResolver result = typeDeserializerBase.getTypeIdResolver();
        assertNotNull(result);
    }

    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(JavaType) {};
        Class result = typeDeserializerBase.getDefaultImpl();
        assertNotNull(result);
    }

    @Test
    public void testBaseType() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(JavaType) {};
        JavaType result = typeDeserializerBase.baseType();
        assertNotNull(result);
    }
}
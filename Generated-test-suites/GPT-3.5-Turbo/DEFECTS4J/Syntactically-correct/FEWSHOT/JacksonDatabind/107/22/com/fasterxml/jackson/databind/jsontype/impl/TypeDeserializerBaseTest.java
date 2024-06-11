package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;
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
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeDeserializerBaseTest {
    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase base = new TypeDeserializerBase(new JavaType(), new TypeIdResolver(), "typePropertyName", true, new JavaType()) {};
        assertEquals("java.lang.Object", base.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase base = new TypeDeserializerBase(new JavaType(), new TypeIdResolver(), "typePropertyName", true, new JavaType()) {};
        assertEquals("typePropertyName", base.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver idResolver = new TypeIdResolver();
        TypeDeserializerBase base = new TypeDeserializerBase(new JavaType(), idResolver, "typePropertyName", true, new JavaType()) {};
        assertEquals(idResolver, base.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        JavaType defaultImpl = new JavaType();
        TypeDeserializerBase base = new TypeDeserializerBase(new JavaType(), new TypeIdResolver(), "typePropertyName", true, defaultImpl) {};
        assertEquals(defaultImpl.getRawClass(), base.getDefaultImpl());
    }

    @Test
    public void testBaseType() {
        JavaType baseType = new JavaType();
        TypeDeserializerBase base = new TypeDeserializerBase(baseType, new TypeIdResolver(), "typePropertyName", true, new JavaType()) {};
        assertEquals(baseType, base.baseType());
    }

    @Test
    public void testToString() {
        JavaType baseType = new JavaType();
        TypeDeserializerBase base = new TypeDeserializerBase(baseType, new TypeIdResolver(), "typePropertyName", true, new JavaType()) {};
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:javaType; id-resolver: com.fasterxml.jackson.databind.jsontype.TypeIdResolver]", base.toString());
    }
}
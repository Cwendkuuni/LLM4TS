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
        JavaType baseType = typeFactory.constructType(String.class);
        TypeIdResolver idRes = new SomeTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = null;

        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl) {};

        String expected = String.class.getName();
        String actual = typeDeserializer.baseTypeName();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyname() {
        JavaType baseType = typeFactory.constructType(String.class);
        TypeIdResolver idRes = new SomeTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = null;

        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl) {};

        String expected = "type";
        String actual = typeDeserializer.getPropertyName();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypeIdResolver() {
        JavaType baseType = typeFactory.constructType(String.class);
        TypeIdResolver idRes = new SomeTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = null;

        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl) {};

        TypeIdResolver expected = idRes;
        TypeIdResolver actual = typeDeserializer.getTypeIdResolver();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetDefaultImpl() {
        JavaType baseType = typeFactory.constructType(String.class);
        TypeIdResolver idRes = new SomeTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = String.class;

        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl) {};

        Class<?> expected = String.class;
        Class<?> actual = typeDeserializer.getDefaultImpl();

        assertEquals(expected, actual);
    }

    private class SomeTypeIdResolver implements TypeIdResolver {
        @Override
        public void init(JavaType baseType) {}

        @Override
        public String idFromValue(Object value) { return null; }

        @Override
        public String idFromBaseType() { return null; }

        @Override
        public JavaType typeFromId(DatabindContext context, String id) { return null; }

        @Override
        public JsonTypeInfo.Id getMechanism() { return null; }
    }
}
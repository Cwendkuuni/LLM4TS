package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseType() {
        JavaType baseType = mock(JavaType.class);
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, null, null, false, null) {
			
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
			
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
			
            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType) throws IOException {
                return null;
            }
        };
		
        assertEquals(baseType, typeDeserializer.baseTypeName());
    }

    @Test
    public void testNullDefaultImpl() {
        JavaType baseType = mock(JavaType.class);
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, null, null, false, null) {
			
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
			
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
			
            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType) throws IOException {
                return null;
            }
        };
		
        assertNull(typeDeserializer.getDefaultImpl());
    }

    @Test
    public void testNonNullDefaultImpl() {
        JavaType defaultImpl = mock(JavaType.class);
        JavaType baseType = mock(JavaType.class);
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, null, null, false, defaultImpl) {
			
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
			
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
			
            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType) throws IOException {
                return null;
            }
        };
		
        assertEquals(defaultImpl, typeDeserializer.getDefaultImpl());
    }

    @Test
    public void testToString() {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, null, false, null) {
			
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
			
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
			
            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType) throws IOException {
                return null;
            }
        };
		
        assertNotNull(typeDeserializer.toString());
    }
}
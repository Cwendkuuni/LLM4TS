package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase deserializer;
    private JavaType baseType;
    private TypeIdResolver idResolver;
    private String typePropertyName;
    private boolean typeIdVisible;
    private JavaType defaultImpl;

    @Before
    public void setUp() {
        baseType = mock(JavaType.class);
        idResolver = mock(TypeIdResolver.class);
        typePropertyName = "type";
        typeIdVisible = true;
        defaultImpl = mock(JavaType.class);
        deserializer = new TypeDeserializerBaseImpl(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test
    public void testForProperty() {
        BeanProperty prop = mock(BeanProperty.class);
        TypeDeserializer result = deserializer.forProperty(prop);
        assertEquals(deserializer.getClass(), result.getClass());
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As result = deserializer.getTypeInclusion();
        assertEquals(JsonTypeInfo.As.PROPERTY, result);
    }

    @Test
    public void testBaseTypeName() {
        when(baseType.getRawClass()).thenReturn(Object.class);
        String result = deserializer.baseTypeName();
        assertEquals(Object.class.getName(), result);
    }

    @Test
    public void testGetPropertyName() {
        String result = deserializer.getPropertyName();
        assertEquals(typePropertyName, result);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver result = deserializer.getTypeIdResolver();
        assertEquals(idResolver, result);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> rawClass = Object.class;
        when(defaultImpl.getRawClass()).thenReturn(rawClass);
        Class<?> result = deserializer.getDefaultImpl();
        assertEquals(rawClass, result);
    }

    @Test
    public void testToString() {
        String result = deserializer.toString();
        assertEquals("[" + TypeDeserializerBaseImpl.class.getName() + "; base-type:" + baseType + "; id-resolver: " + idResolver + "]", result);
    }

    private static class TypeDeserializerBaseImpl extends TypeDeserializerBase {
        private static final long serialVersionUID = 1;

        protected TypeDeserializerBaseImpl(JavaType baseType, TypeIdResolver idRes, String typePropertyName,
                boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return new TypeDeserializerBaseImpl(this, prop);
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.PROPERTY;
        }

        private TypeDeserializerBaseImpl(TypeDeserializerBaseImpl src, BeanProperty property) {
            super(src, property);
        }

        @Override
        protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver,
                JavaType baseType) throws IOException {
            return null;
        }

        @Override
        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }
}
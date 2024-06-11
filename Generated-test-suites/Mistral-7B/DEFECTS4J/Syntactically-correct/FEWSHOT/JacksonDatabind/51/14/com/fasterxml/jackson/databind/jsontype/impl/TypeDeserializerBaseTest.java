package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class TypeDeserializerBaseTest {

    @Test
    public void testForProperty() throws IOException {
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        JavaType baseType = mock(JavaType.class);
        String typePropertyName = "typePropertyName";
        String typeIdVisible = "true";
        JavaType defaultImpl = mock(JavaType.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName,
                Boolean.parseBoolean(typeIdVisible), defaultImpl);

        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializer forProperty = typeDeserializerBase.forProperty(property);

        assertSame(typeDeserializerBase, forProperty);
    }

    @Test
    public void testGetTypeInclusion() throws IOException {
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        JavaType baseType = mock(JavaType.class);
        String typePropertyName = "typePropertyName";
        String typeIdVisible = "true";
        JavaType defaultImpl = mock(JavaType.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName,
                Boolean.parseBoolean(typeIdVisible), defaultImpl);

        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();

        assertSame(JsonTypeInfo.As.EXTERNAL_PROPERTY, typeInclusion);
    }

    @Test
    public void testBaseTypeName() throws IOException {
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        JavaType baseType = mock(JavaType.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);

        String baseTypeName = typeDeserializerBase.baseTypeName();

        assertSame(baseType.getRawClass().getName(), baseTypeName);
    }

    @Test
    public void testGetPropertyName() throws IOException {
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        JavaType baseType = mock(JavaType.class);
        String typePropertyName = "typePropertyName";

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName,
                false, null);

        String propertyName = typeDeserializerBase.getPropertyName();

        assertSame(typePropertyName, propertyName);
    }

    @Test
    public void testGetTypeIdResolver() throws IOException {
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        JavaType baseType = mock(JavaType.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);

        TypeIdResolver typeIdResolver = typeDeserializerBase.getTypeIdResolver();

        assertSame(idResolver, typeIdResolver);
    }

    @Test
    public void testGetDefaultImpl() throws IOException {
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        JavaType baseType = mock(JavaType.class);
        JavaType defaultImpl = mock(JavaType.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null,
                false, defaultImpl);

        Class<?> defaultClass = typeDeserializerBase.getDefaultImpl();

        assertSame(defaultImpl.getRawClass(), defaultClass);
    }

    @Test
    public void testToString() throws IOException {
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        JavaType baseType = mock(JavaType.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);

        String toString = typeDeserializerBase.toString();

        assertTrue(toString.contains("[TypeDeserializerBase"));
        assertTrue(toString.contains("; base-type:"));
        assertTrue(toString.contains("; id-resolver: "));
    }

    @Test
    public void testFindDeserializer() throws IOException {
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        JavaType baseType = mock(JavaType.class);
        String typeId = "typeId";
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);

        when(idResolver.typeFromId(any(DeserializationContext.class), eq(typeId))).thenReturn(mock(JavaType.class));
        when(idResolver.findContextualValueDeserializer(any(JavaType.class), any(BeanProperty.class)))
                .thenReturn(deserializer);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);

        JsonDeserializer<Object> findDeserializer = typeDeserializerBase._findDeserializer(mock(DeserializationContext.class),
                typeId);

        assertSame(deserializer, findDeserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType baseType = mock(JavaType.class);
        JavaType defaultImpl = mock(JavaType.class);
        JsonDeserializer<Object> defaultImplDeserializer = mock(JsonDeserializer.class);

        when(ctxt.findContextualValueDeserializer(eq(defaultImpl), any(BeanProperty.class))).thenReturn(defaultImplDeserializer);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, null, false, defaultImpl);

        JsonDeserializer<Object> findDefaultImplDeserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);

        assertSame(defaultImplDeserializer, findDefaultImplDeserializer);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType baseType = mock(JavaType.class);
        String typeId = "typeId";
        TypeIdResolver idResolver = mock(TypeIdResolver.class);

        when(idResolver.getTypeDeserializer(eq(ctxt), eq(typeId))).thenThrow(new IOException());

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);

        JavaType handleUnknownTypeId = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);

        assertNull(handleUnknownTypeId);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object typeId = "typeId";

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);

        when(jp.getTypeId()).thenReturn(typeId);

        Object deserializeWithNativeTypeId = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);

        assertNull(deserializeWithNativeTypeId);
    }
}
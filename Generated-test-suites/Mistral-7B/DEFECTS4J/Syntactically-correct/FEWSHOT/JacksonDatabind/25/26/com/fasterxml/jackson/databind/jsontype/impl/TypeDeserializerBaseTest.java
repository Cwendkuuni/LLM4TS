package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    private static final String TYPE_ID = "myTypeId";
    private static final String TYPE_ID_STR = "\"myTypeId\"";
    private static final String DEFAULT_IMPL_NAME = "MyDefaultImpl";
    private static final String DEFAULT_IMPL_TYPE_NAME = "com.example.MyDefaultImpl";
    private static final String BASE_TYPE_NAME = "com.example.MyBaseType";
    private static final String PROPERTY_NAME = "myProperty";

    @Test
    public void testTypeDeserializerBaseConstruction() {
        JavaType baseType = mock(JavaType.class);
        when(baseType.getClass()).thenReturn(Object.class);
        when(baseType.getRawClass()).thenReturn(Class.forName(BASE_TYPE_NAME));
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.typeFromId(Mockito.any(DeserializationContext.class), Mockito.eq(TYPE_ID)))
                .thenReturn(mock(JavaType.class));
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, PROPERTY_NAME, false, null);
        assertEquals(BASE_TYPE_NAME, typeDeserializerBase.baseTypeName());
        assertEquals(PROPERTY_NAME, typeDeserializerBase.getPropertyName());
        assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
        assertNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testTypeDeserializerBaseConstructionWithDefaultImpl() {
        JavaType baseType = mock(JavaType.class);
        when(baseType.getClass()).thenReturn(Object.class);
        when(baseType.getRawClass()).thenReturn(Class.forName(BASE_TYPE_NAME));
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.typeFromId(Mockito.any(DeserializationContext.class), Mockito.eq(TYPE_ID)))
                .thenReturn(mock(JavaType.class));
        JavaType defaultImplType = mock(JavaType.class);
        when(defaultImplType.getClass()).thenReturn(DefaultClassLoader.getLoadedClass(DEFAULT_IMPL_TYPE_NAME));
        when(defaultImplType.getRawClass()).thenReturn(Class.forName(DEFAULT_IMPL_TYPE_NAME));
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, PROPERTY_NAME, false,
                DEFAULT_IMPL_TYPE_NAME);
        assertEquals(BASE_TYPE_NAME, typeDeserializerBase.baseTypeName());
        assertEquals(PROPERTY_NAME, typeDeserializerBase.getPropertyName());
        assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
        assertEquals(DEFAULT_IMPL_TYPE_NAME, typeDeserializerBase.getDefaultImpl().getRawClass().getName());
    }

    @Test
    public void testTypeDeserializerBaseCopy() throws IOException {
        JavaType baseType = mock(JavaType.class);
        when(baseType.getClass()).thenReturn(Object.class);
        when(baseType.getRawClass()).thenReturn(Class.forName(BASE_TYPE_NAME));
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.typeFromId(Mockito.any(DeserializationContext.class), Mockito.eq(TYPE_ID)))
                .thenReturn(mock(JavaType.class));
        TypeDeserializerBase src = new TypeDeserializerBase(baseType, idResolver, PROPERTY_NAME, false, null);
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializerBase dest = new TypeDeserializerBase(src, property);
        assertEquals(src._baseType, dest._baseType);
        assertEquals(src._idResolver, dest._idResolver);
        assertEquals(src._typePropertyName, dest._typePropertyName);
        assertEquals(src._typeIdVisible, dest._typeIdVisible);
        assertEquals(src._deserializers, dest._deserializers);
        assertEquals(src._defaultImpl, dest._defaultImpl);
        assertEquals(src._defaultImplDeserializer, dest._defaultImplDeserializer);
        assertEquals(src._property, dest._property);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        JavaType baseType = mock(JavaType.class);
        when(baseType.getClass()).thenReturn(Object.class);
        when(baseType.getRawClass()).thenReturn(Class.forName(BASE_TYPE_NAME));
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.typeFromId(Mockito.any(DeserializationContext.class), Mockito.eq(TYPE_ID)))
                .thenReturn(mock(JavaType.class));

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, PROPERTY_NAME, false, null);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        when(ctxt.findContextualValueDeserializer(Mockito.any(JavaType.class), Mockito.eq(property))).thenReturn(mock(JsonDeserializer.class));

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, TYPE_ID_STR);
        assertEquals(mock(JsonDeserializer.class), deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        JavaType baseType = mock(JavaType.class);
        when(baseType.getClass()).thenReturn(Object.class);
        when(baseType.getRawClass()).thenReturn(Class.forName(BASE_TYPE_NAME));
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.typeFromId(Mockito.any(DeserializationContext.class), Mockito.eq(TYPE_ID)))
                .thenReturn(mock(JavaType.class));
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, PROPERTY_NAME, false, null);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerWithDefaultImpl() throws IOException {
        JavaType baseType = mock(JavaType.class);
        when(baseType.getClass()).thenReturn(Object.class);
        when(baseType.getRawClass()).thenReturn(Class.forName(BASE_TYPE_NAME));
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.typeFromId(Mockito.any(DeserializationContext.class), Mockito.eq(TYPE_ID)))
                .thenReturn(mock(JavaType.class));
        JavaType defaultImplType = mock(JavaType.class);
        when(defaultImplType.getClass()).thenReturn(DefaultClassLoader.getLoadedClass(DEFAULT_IMPL_TYPE_NAME));
        when(defaultImplType.getRawClass()).thenReturn(Class.forName(DEFAULT_IMPL_TYPE_NAME));
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, PROPERTY_NAME, false,
                DEFAULT_IMPL_TYPE_NAME);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertSame(mock(JsonDeserializer.class), deserializer);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeId() throws IOException {
        JavaType baseType = mock(JavaType.class);
        when(baseType.getClass()).thenReturn(Object.class);
        when(baseType.getRawClass()).thenReturn(Class.forName(BASE_TYPE_NAME));
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.typeFromId(Mockito.any(DeserializationContext.class), Mockito.eq(TYPE_ID)))
                .thenReturn(null);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, PROPERTY_NAME, false, null);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        typeDeserializerBase._handleUnknownTypeId(ctxt, TYPE_ID, idResolver, baseType);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JavaType baseType = mock(JavaType.class);
        when(baseType.getClass()).thenReturn(Object.class);
        when(baseType.getRawClass()).thenReturn(Class.forName(BASE_TYPE_NAME));
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.typeFromId(Mockito.any(DeserializationContext.class), Mockito.eq(TYPE_ID)))
                .thenReturn(mock(JavaType.class));
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, PROPERTY_NAME, false, null);
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        when(ctxt.findContextualValueDeserializer(Mockito.any(JavaType.class), Mockito.eq(PROPERTY_NAME)))
                .thenReturn(mock(JsonDeserializer.class));

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, TYPE_ID);
        assertSame(mock(Object.class), result);
    }
}
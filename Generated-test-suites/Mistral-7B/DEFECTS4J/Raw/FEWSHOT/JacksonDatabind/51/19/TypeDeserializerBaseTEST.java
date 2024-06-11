package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

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
import org.junit.Test;

public class TypeDeserializerBaseTest {

    private static final TypeIdResolver idResolverMock = mock(TypeIdResolver.class);
    private static final DeserializationContext contextMock = mock(DeserializationContext.class);
    private static final Object typeId = "testTypeId";
    private static final JavaType baseTypeMock = mock(JavaType.class);
    private static final BeanProperty propertyMock = mock(BeanProperty.class);
    private static final JsonDeserializer<Object> defaultImplDeserializerMock = mock(JsonDeserializer.class);
    private static final Map<String, JsonDeserializer<Object>> deserializers = new ConcurrentHashMap<>();

    @Test
    public void testConstructor() throws IOException {
        when(baseTypeMock.getRawClass()).thenReturn(Object.class);
        when(idResolverMock.typeFromId(contextMock, typeId)).thenReturn(null);

        new TypeDeserializerBase(baseTypeMock, idResolverMock, null, false, null);
        verify(baseTypeMock).getRawClass();
        verify(idResolverMock).typeFromId(contextMock, typeId);
        verify(idResolverMock, never()).getTypeFactory();
    }

    @Test
    public void testConstructorWithTypePropertyName() throws IOException {
        when(baseTypeMock.getRawClass()).thenReturn(Object.class);
        when(idResolverMock.typeFromId(contextMock, typeId)).thenReturn(null);

        String typePropertyName = "testPropertyName";
        new TypeDeserializerBase(baseTypeMock, idResolverMock, typePropertyName, false, null);
        verify(baseTypeMock).getRawClass();
        verify(idResolverMock).typeFromId(contextMock, typeId);
        verify(idResolverMock).getTypeFactory();
        verify(idResolverMock).getTypeFactory().constructPropertyType(typePropertyName, Object.class);
        verifyNoMoreInteractions(idResolverMock);
    }

    @Test
    public void testConstructorWithTypeIdVisible() throws IOException {
        when(baseTypeMock.getRawClass()).thenReturn(Object.class);
        when(idResolverMock.typeFromId(contextMock, typeId)).thenReturn(null);

        new TypeDeserializerBase(baseTypeMock, idResolverMock, null, true, null);
        verify(baseTypeMock).getRawClass();
        verify(idResolverMock).typeFromId(contextMock, typeId);
        verify(idResolverMock).getTypeFactory();
        verify(idResolverMock).getTypeFactory().constructPropertyType(null, Object.class);
        verify(idResolverMock).getTypeIdResolver();
        verifyNoMoreInteractions(idResolverMock);
    }

    @Test
    public void testConstructorWithDefaultImpl() throws IOException {
        when(baseTypeMock.getRawClass()).thenReturn(Object.class);
        when(idResolverMock.typeFromId(contextMock, typeId)).thenReturn(null);
        when(baseTypeMock.getTypeFactory()).thenReturn(mock(TypeFactory.class));

        JavaType defaultImplTypeMock = mock(JavaType.class);
        when(baseTypeMock.getTypeFactory().constructType(eq("java.lang.String"), anyListOf(Type.class))).thenReturn(defaultImplTypeMock);

        new TypeDeserializerBase(baseTypeMock, idResolverMock, null, false, defaultImplTypeMock);
        verify(baseTypeMock).getRawClass();
        verify(idResolverMock).typeFromId(contextMock, typeId);
        verify(baseTypeMock).getTypeFactory();
        verify(baseTypeMock).getTypeFactory().constructType(eq("java.lang.String"), anyListOf(Type.class));
        verifyNoMoreInteractions(baseTypeMock);
    }

    @Test
    public void testConstructorWithProperty() throws IOException {
        when(baseTypeMock.getRawClass()).thenReturn(Object.class);
        when(idResolverMock.typeFromId(contextMock, typeId)).thenReturn(null);
        when(propertyMock.getName()).thenReturn("testPropertyName");

        new TypeDeserializerBase(baseTypeMock, idResolverMock, propertyMock);
        verify(baseTypeMock).getRawClass();
        verify(idResolverMock).typeFromId(contextMock, typeId);
        verify(propertyMock).getName();
        verifyNoMoreInteractions(baseTypeMock);
        verifyNoMoreInteractions(idResolverMock);
    }

    @Test
    public void testForProperty() throws IOException {
        when(baseTypeMock.getRawClass()).thenReturn(Object.class);
        when(idResolverMock.typeFromId(contextMock, typeId)).thenReturn(null);
        when(baseTypeMock.getTypeFactory()).thenReturn(mock(TypeFactory.class));

        JavaType defaultImplTypeMock = mock(JavaType.class);
        when(baseTypeMock.getTypeFactory().constructType(eq("java.lang.String"), anyListOf(Type.class))).thenReturn(defaultImplTypeMock);

        when(propertyMock.getName()).thenReturn("testPropertyName");

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                baseTypeMock, idResolverMock, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }
        };
        TypeDeserializer result = typeDeserializerBase.forProperty(propertyMock);

        verify(baseTypeMock).getRawClass();
        verify(idResolverMock).typeFromId(contextMock, typeId);
        verify(baseTypeMock).getTypeFactory();
        verify(propertyMock).getName();
        verify(this).forProperty(propertyMock);

        assertSame(typeDeserializerBase, result);
    }

    @Test
    public void testGetTypeInclusion() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mock(JavaType.class), mock(TypeIdResolver.class), null, false, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.PROPERTY;
            }
        };

        JsonTypeInfo.As result = typeDeserializerBase.getTypeInclusion();

        assertSame(JsonTypeInfo.As.PROPERTY, result);
    }

    @Test
    public void testBaseTypeName() {
        String expectedBaseTypeName = Object.class.getName();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mock(JavaType.class), mock(TypeIdResolver.class), null, false, null);

        String result = typeDeserializerBase.baseTypeName();

        assertSame(expectedBaseTypeName, result);
    }

    @Test
    public void testGetPropertyName() {
        String expectedPropertyName = "testPropertyName";
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mock(JavaType.class), mock(TypeIdResolver.class), expectedPropertyName, false, null);

        String result = typeDeserializerBase.getPropertyName();

        assertSame(expectedPropertyName, result);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mock(JavaType.class), mock(TypeIdResolver.class), null, false, null);

        TypeIdResolver result = typeDeserializerBase.getTypeIdResolver();

        assertSame(idResolverMock, result);
    }

    @Test
    public void testGetDefaultImpl() throws IOException {
        when(baseTypeMock.getRawClass()).thenReturn(Object.class);
        when(idResolverMock.typeFromId(contextMock, typeId)).thenReturn(null);
        when(baseTypeMock.getTypeFactory()).thenReturn(mock(TypeFactory.class));

        JavaType defaultImplTypeMock = mock(JavaType.class);
        when(baseTypeMock.getTypeFactory().constructType(eq("java.lang.String"), anyListOf(Type.class))).thenReturn(defaultImplTypeMock);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                baseTypeMock, idResolverMock, null, false, null) {
            @Override
            public JavaType getDefaultImpl() {
                return defaultImplTypeMock;
            }
        };

        JavaType result = typeDeserializerBase.getDefaultImpl();

        assertSame(defaultImplTypeMock, result);
    }

    @Test
    public void testToString() {
        String expectedClassName = TypeDeserializerBase.class.getName();
        String expectedBaseType = Object.class.getName();
        String expectedIdResolverClass = TypeIdResolver.class.getName();

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mock(JavaType.class), mock(TypeIdResolver.class), null, false, null);

        String result = typeDeserializerBase.toString();

        String expectedResult = String.format("[%s; base-type:%s; id-resolver: %s]",
                expectedClassName, expectedBaseType, expectedIdResolverClass);
        assertSame(expectedResult, result);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        when(idResolverMock.typeFromId(contextMock, typeId)).thenReturn(null);
        when(baseTypeMock.getTypeFactory()).thenReturn(mock(TypeFactory.class));

        JavaType typeMock = mock(JavaType.class);
        when(baseTypeMock.getTypeFactory().constructSpecializedType(_baseTypeMock.getTypeFactory().constructType(eq("java.lang.String"), anyListOf(Type.class)), typeMock.getRawClass())).thenReturn(typeMock);

        when(idResolverMock.findContextualValueDeserializer(typeMock, propertyMock)).thenReturn(defaultImplDeserializerMock);

        JsonDeserializer<Object> result = _typeDeserializerBase.findDeserializer(contextMock, typeId);

        assertSame(defaultImplDeserializerMock, result);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        when(baseTypeMock.getTypeFactory()).thenReturn(mock(TypeFactory.class));

        when(baseTypeMock.getRawClass()).thenReturn(Object.class);
        when(idResolverMock.typeFromId(contextMock, typeId)).thenReturn(null);

        when(baseTypeMock.getTypeFactory().constructType(eq("java.lang.Void"), anyListOf(Type.class))).thenReturn(mock(JavaType.class));
        when(baseTypeMock.getTypeFactory().constructType(eq("java.lang.String"), anyListOf(Type.class))).thenReturn(mock(JavaType.class));

        when(defaultImplDeserializerMock.getClass()).thenReturn(Object.class);

        JsonDeserializer<Object> result = _typeDeserializerBase.findDefaultImplDeserializer(contextMock);

        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        when(baseTypeMock.getTypeFactory()).thenReturn(mock(TypeFactory.class));
        when(idResolverMock.getTypeIdResolver()).thenReturn(mock(TypeIdResolver.class));

        when(idResolverMock.typeFromId(contextMock, typeId)).thenReturn(null);

        when(baseTypeMock.getTypeFactory().constructPropertyType(null, Object.class)).thenReturn(mock(JavaType.class));
        when(baseTypeMock.getTypeFactory().constructType(eq("java.lang.String"), anyListOf(Type.class))).thenReturn(mock(JavaType.class));

        when(idResolverMock.getTypeFactory()).thenReturn(mock(TypeFactory.class));
        when(idResolverMock.getTypeFactory().constructType(eq("java.util.Map"), anyListOf(Class.class))).thenReturn(mock(JavaType.class));

        when(idResolverMock.getTypeIdResolver().getTypeIdResolver(eq(contextMock), any(Class.class), any(Class.class))).thenReturn("testMapTypeId");

        JavaType result = _typeDeserializerBase.handleUnknownTypeId(contextMock, typeId, idResolverMock, baseTypeMock);

        assertNull(result);
    }

    private TypeDeserializerBase _typeDeserializerBase = new TypeDeserializerBase(baseTypeMock, idResolverMock, null, false, null);
}
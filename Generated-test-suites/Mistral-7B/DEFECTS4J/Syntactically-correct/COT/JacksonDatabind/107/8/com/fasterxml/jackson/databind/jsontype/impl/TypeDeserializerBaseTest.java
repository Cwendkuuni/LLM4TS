package com.example.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TypeDeserializerBaseTest {

    private JsonDeserializer<Object> defaultImplDeserializerMock;
    private TypeDeserializerBase typeDeserializerBase;
    private TypeIdResolver typeIdResolverMock;
    private DeserializationContext contextMock;
    private JavaType baseTypeMock;
    private BeanProperty propertyMock;
    private JavaType defaultImplMock;

    @Before
    public void setup() throws IOException {
        typeIdResolverMock = mock(TypeIdResolver.class);
        contextMock = mock(DeserializationContext.class);
        baseTypeMock = mock(JavaType.class);
        propertyMock = mock(BeanProperty.class);
        defaultImplMock = mock(JavaType.class);
        defaultImplDeserializerMock = mock(JsonDeserializer.class);

        typeDeserializerBase = new TypeDeserializerBase(baseTypeMock, typeIdResolverMock, "typePropertyName", true, defaultImplMock) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return defaultImplDeserializerMock;
            }
        };
        typeDeserializerBase._deserializers = new ConcurrentHashMap<>();
    }

    @Test
    public void testForProperty() throws IOException, JsonProcessingException {
        JavaType javaType = mock(JavaType.class);
        when(typeDeserializerBase._findDeserializer(contextMock, "typeId")).thenReturn(mock(JsonDeserializer.class));
        when(typeDeserializerBase.forProperty(propertyMock)).thenReturn(typeDeserializerBase);
        assertEquals(typeDeserializerBase, typeDeserializerBase.forProperty(propertyMock));
    }

    @Test(expected = IllegalStateException.class)
    public void testForPropertyNullProperty() {
        when(typeDeserializerBase._findDeserializer(contextMock, "typeId")).thenReturn(mock(JsonDeserializer.class));
        typeDeserializerBase.forProperty(null);
    }

    @Test(expected = NullPointerException.class)
    public void testForPropertyNullTypeIdResolver() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseTypeMock, null, "typePropertyName", true, defaultImplMock);
        typeDeserializerBase.forProperty(propertyMock);
    }

    @Test
    public void testGetTypeInclusion() {
        assertEquals(JsonTypeInfo.As.WRAP_INCLUDE_PROPERTY, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() throws IOException {
        when(baseTypeMock.getRawClass()).thenReturn(String.class);
        assertEquals("java.lang.String", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("typePropertyName", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertEquals(typeIdResolverMock, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() throws IOException {
        when(defaultImplMock.getRawClass()).thenReturn(String.class);
        assertEquals("java.lang.String", typeDeserializerBase.getDefaultImpl().getName());
    }

    @Test
    public void testBaseType() throws IOException {
        when(baseTypeMock.getRawClass()).thenReturn(String.class);
        assertEquals(String.class, typeDeserializerBase.baseType());
    }

    @Test
    public void testToString() {
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:java.lang.String; id-resolver: MockFor(com.fasterxml.jackson.databind.TypeIdResolver)];", typeDeserializerBase.toString());
    }

    @Test
    public void testFindDeserializerNullContext() throws IOException {
        when(typeDeserializerBase._idResolver.typeFromId(contextMock, "typeId")).thenReturn(defaultImplMock);
        when(defaultImplMock.getRawClass()).thenReturn(String.class);

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(null, "typeId");
        verify(contextMock, never()).findContextualValueDeserializer(any(), any());
        verify(contextMock, never()).handleMissingTypeId(any(), anyString());
        verifyNoMoreInteractions(contextMock);
        assertEquals(null, deserializer);
    }

    @Test
    public void testFindDeserializerWithTypeId() throws IOException {
        when(typeDeserializerBase._idResolver.typeFromId(contextMock, "typeId")).thenReturn(typeDeserializerBase);
        when(typeDeserializerBase._typeIdVisible).thenReturn(false);

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(contextMock, "typeId");
        verify(typeDeserializerBase).forProperty(propertyMock);
        assertEquals(typeDeserializerBase, deserializer);
    }

    @Test(expected = NullPointerException.class)
    public void testFindDeserializerNullTypeId() {
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(contextMock, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDeserializerNonStringTypeId() {
        when(typeDeserializerBase._idResolver.typeFromId(contextMock, "typeId")).thenReturn(null);

        typeDeserializerBase._findDeserializer(contextMock, new Object());
    }

    @Test
    public void testFindDefaultImplDeserializerNullContext() throws IOException {
        when(defaultImplMock.getRawClass()).thenReturn(String.class);

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(null);
        verify(contextMock, never()).findContextualValueDeserializer(any(), any());
        verify(contextMock, never()).handleMissingTypeId(any(), anyString());
        verifyNoMoreInteractions(contextMock);
        assertEquals(NullifyingDeserializer.instance, deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerContext() throws IOException {
        when(defaultImplMock.getRawClass()).thenReturn(String.class);
        when(propertyMock.hasName("propertyName")).thenReturn(true);

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(contextMock);
        verify(contextMock).findContextualValueDeserializer(defaultImplMock, propertyMock);
        assertEquals(NullifyingDeserializer.instance, deserializer);
    }

    @Test(expected = NullPointerException.class)
    public void testHandleUnknownTypeIdNullContext() throws IOException {
        typeDeserializerBase._handleUnknownTypeId(null, "extraDesc");
    }

    @Test
    public void testHandleUnknownTypeIdContext() throws IOException {
        when(baseTypeMock.getClass()).thenReturn(Map.class);

        JavaType javaType = typeDeserializerBase._handleUnknownTypeId(contextMock, "extraDesc");
        verify(contextMock).handleMissingTypeId(baseTypeMock, "extraDesc");
        assertEquals(javaType, contextMock.handleMissingTypeId(baseTypeMock, "extraDesc"));
    }

    @Test(expected = NullPointerException.class)
    public void testHandleMissingTypeIdNullContext() throws IOException {
        typeDeserializerBase._handleMissingTypeId(null, "extraDesc");
    }

    @Test
    public void testHandleMissingTypeIdContextWithDefaultImpl() throws IOException {
        when(baseTypeMock.getClass()).thenReturn(Map.class);
        when(contextMock.handleMissingTypeId(baseTypeMock, "extraDesc")).thenReturn(defaultImplMock);

        JavaType javaType = typeDeserializerBase._handleMissingTypeId(contextMock, "extraDesc");
        assertEquals(defaultImplMock, javaType);
    }
}
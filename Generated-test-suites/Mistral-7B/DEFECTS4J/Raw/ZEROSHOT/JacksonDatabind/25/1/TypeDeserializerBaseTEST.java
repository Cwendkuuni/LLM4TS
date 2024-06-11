package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test class for TypeDeserializerBase.
 */
public class TypeDeserializerBaseTest {

    @InjectMocks
    private TypeDeserializerBase typeDeserializerBase;

    @Mock
    private JsonDeserializer<Object> jsonDeserializerMock;

    @Mock
    private TypeIdResolver typeIdResolverMock;

    @Mock
    private DeserializationContext deserializationContextMock;

    @Mock
    private JsonParser jsonParserMock;

    private JavaType javaTypeMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        javaTypeMock = mock(JavaType.class);
    }

    @Test
    public void testForProperty() throws IOException {
        when(typeIdResolverMock.typeFromId(deserializationContextMock, anyString())).thenReturn(javaTypeMock);

        BeanProperty beanPropertyMock = mock(BeanProperty.class);
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(beanPropertyMock);

        assertEquals(TypeDeserializer.class, typeDeserializer.getClass());
        verify(typeDeserializerBase).<TypeDeserializer>newInstance(typeDeserializerBase, beanPropertyMock);
        verify(typeDeserializerBase)._idResolver = typeIdResolverMock;
        verify(typeDeserializerBase)._baseType = any();
        verify(typeDeserializerBase)._property = beanPropertyMock;
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertEquals(JsonTypeInfo.As.WRAP_ONLY, typeInclusion);
    }

    @Test
    public void testBaseTypeName() throws IOException {
        String result = typeDeserializerBase.baseTypeName();
        assertNotNull(result);
    }

    @Test
    public void testGetPropertyName() {
        String result = typeDeserializerBase.getPropertyName();
        assertNull(result);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase typeDeserializerBaseSpy = spy(TypeDeserializerBase.class);
        assertEquals(typeIdResolverMock, typeDeserializerBaseSpy.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> clazz = Object.class;
        when(javaTypeMock.getRawClass()).thenReturn(clazz);

        assertEquals(clazz, typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() {
        String result = typeDeserializerBase.toString();
        assertNotNull(result);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        when(typeDeserializerBase._idResolver.typeFromId(deserializationContextMock, anyString())).thenReturn(javaTypeMock);
        when(deserializationContextMock.findContextualValueDeserializer(javaTypeMock, any())).thenReturn(jsonDeserializerMock);

        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(deserializationContextMock, "testTypeId");

        assertEquals(jsonDeserializerMock, result);
        verify(typeDeserializerBase._deserializers).put("testTypeId", jsonDeserializerMock);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        when(deserializationContextMock.findContextualValueDeserializer(eq(typeDeserializerBase._defaultImpl), any()))
                .thenReturn(jsonDeserializerMock);

        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(deserializationContextMock);

        assertEquals(jsonDeserializerMock, result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        when(typeIdResolverMock.typeFromId(deserializationContextMock, anyString())).thenReturn(javaTypeMock);

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParserMock, deserializationContextMock, null);

        assertNull(result);
        verify(typeDeserializerBase)._findDefaultImplDeserializer(deserializationContextMock);
    }

    @Test
    public void testDeserializeWithNativeTypeIdWithTypeId() throws IOException {
        String typeId = "testTypeId";

        when(typeIdResolverMock.typeFromId(deserializationContextMock, anyString())).thenReturn(javaTypeMock);
        when(deserializationContextMock.findContextualValueDeserializer(javaTypeMock, any())).thenReturn(jsonDeserializerMock);

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParserMock, deserializationContextMock, typeId);

        assertEquals(jsonDeserializerMock, result);
        verify(typeDeserializerBase)._findDeserializer(deserializationContextMock, typeId);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeId() throws IOException {
        String typeId = "testTypeId";

        when(typeIdResolverMock.typeFromId(deserializationContextMock, anyString())).thenReturn(null);

        typeDeserializerBase._handleUnknownTypeId(deserializationContextMock, typeId, typeIdResolverMock, null);

        fail("Expected JsonMappingException to be thrown");
    }
}
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ConcurrentHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

/**
 * Test cases for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest extends TestBase {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mock(JavaType.class), mock(TypeIdResolver.class), "", true, null);
        assertNotNull(typeDeserializerBase);
        verify(typeDeserializerBase._idResolver).getClass();
        verifyNoMoreInteractions(typeDeserializerBase._idResolver);
        verifyNoMoreInteractions(typeDeserializerBase._deserializers);
    }

    /**
     * Test constructor with property.
     */
    @Test
    public void testConstructorWithProperty() {
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mock(TypeDeserializerBase.class), property);
        assertNotNull(typeDeserializerBase);
        verify(typeDeserializerBase._property).getClass();
        verifyNoMoreInteractions(typeDeserializerBase._property);
        verifyNoMoreInteractions(typeDeserializerBase._deserializers);
    }

    /**
     * Test {@link #forProperty(BeanProperty)}.
     */
    @Test
    public void testForProperty() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mock(JavaType.class), mock(TypeIdResolver.class), "", true, null);
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializerBase copy = typeDeserializerBase.forProperty(property);
        verify(typeDeserializerBase).forProperty(property);
        assertNotSame(typeDeserializerBase, copy);
        verifyNoMoreInteractions(typeDeserializerBase);
        verifyNoMoreInteractions(copy);
    }

    /**
     * Test {@link #getTypeInclusion()}.
     */
    @Test
    public void testGetTypeInclusion() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mock(JavaType.class), mock(TypeIdResolver.class), "", true, null);
        TypeDeserializerBase spy = spy(typeDeserializerBase);
        assertSame(JsonTypeInfo.As.WRAP_ONLY, spy.getTypeInclusion());
        verifyNoMoreInteractions(spy);
    }

    /**
     * Test {@link #baseTypeName()}.
     */
    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mock(JavaType.class), mock(TypeIdResolver.class), "", true, null);
        String result = typeDeserializerBase.baseTypeName();
        assertEquals("class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", result);
        verifyNoMoreInteractions(typeDeserializerBase);
    }

    /**
     * Test {@link #getPropertyName()}.
     */
    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mock(JavaType.class), mock(TypeIdResolver.class), "", true, null);
        String result = typeDeserializerBase.getPropertyName();
        assertEquals("", result);
        verifyNoMoreInteractions(typeDeserializerBase);
    }

    /**
     * Test {@link #getTypeIdResolver()}.
     */
    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mock(JavaType.class), mock(TypeIdResolver.class), "", true, null);
        TypeIdResolver result = typeDeserializerBase.getTypeIdResolver();
        assertEquals(mock(TypeIdResolver.class), result);
        verifyNoMoreInteractions(typeDeserializerBase);
    }

    /**
     * Test {@link #getDefaultImpl()}.
     */
    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mock(JavaType.class), mock(TypeIdResolver.class), "", true, null);
        Class<?> result = typeDeserializerBase.getDefaultImpl();
        assertNull(result);
        verifyNoMoreInteractions(typeDeserializerBase);
    }

    /**
     * Test {@link #toString()}.
     */
    @Test
    public void testToString() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mock(JavaType.class), mock(TypeIdResolver.class), "", true, null);
        String result = typeDeserializerBase.toString();
        assertEquals("[class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; id-resolver:class com.fasterxml.jackson.databind.JsonDeserializer$1]", result);
        verifyNoMoreInteractions(typeDeserializerBase);
    }

    /**
     * Test {@link #_findDeserializer(DeserializationContext, String)}.
     */
    @Test
    public void testFindDeserializer() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mock(JavaType.class), mock(TypeIdResolver.class), "", true, null);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(typeDeserializerBase._idResolver.typeFromId(ctxt, "typeId")).thenReturn(mock(JavaType.class));
        when(objectMapper.findContextualValueDeserializer(mock(JavaType.class), any())).thenReturn(deserializer);
        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(ctxt, "typeId");
        verify(typeDeserializerBase)._deserializers.put("typeId", deserializer);
        assertEquals(deserializer, result);
        verifyNoMoreInteractions(typeDeserializerBase);
    }

    /**
     * Test {@link #_findDefaultImplDeserializer(DeserializationContext)}.
     */
    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mock(JavaType.class), mock(TypeIdResolver.class), "", true, null);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(ctxt.findContextualValueDeserializer(any(), any())).thenReturn(deserializer);
        when(typeDeserializerBase._defaultImpl).thenReturn(null);
        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        verify(ctxt).findContextualValueDeserializer(mock(JavaType.class), typeDeserializerBase._property);
        assertEquals(deserializer, result);
        verifyNoMoreInteractions(typeDeserializerBase);
    }

    /**
     * Test {@link #_deserializeWithNativeTypeId(JsonParser, DeserializationContext, Object)}.
     */
    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mock(JavaType.class), mock(TypeIdResolver.class), "", true, null);
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, null);
        assertNull(result);
        verifyNoMoreInteractions(typeDeserializerBase);
    }

    /**
     * Test {@link #_deserializeWithNativeTypeId(JsonParser, DeserializationContext, Object)}.
     */
    @Test
    public void testDeserializeWithNativeTypeIdWithTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mock(JavaType.class), mock(TypeIdResolver.class), "", true, null);
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object typeId = mock(Object.class);
        when(typeId).thenReturn("typeId");
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(typeDeserializerBase._findDeserializer(ctxt, "typeId")).thenReturn(deserializer);
        when(jp.getTypeId()).thenReturn(typeId);
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId);
        verify(typeDeserializerBase)._deserializers.put("typeId", deserializer);
        assertEquals(deserializer.deserialize(jp, ctxt), result);
        verifyNoMoreInteractions(typeDeserializerBase);
    }

    /**
     * Test {@link #_handleUnknownTypeId(DeserializationContext, String, TypeIdResolver, JavaType)}.
     */
    @Test(expected = IOException.class)
    public void testHandleUnknownTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mock(JavaType.class), mock(TypeIdResolver.class), "", true, null);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String typeId = "typeId";
        when(typeDeserializerBase._idResolver.typeFromId(ctxt, typeId)).thenReturn(null);
        TypeIdResolver typeIdResolverBase = mock(TypeIdResolver.class);
        when(typeDeserializerBase._idResolver).thenReturn(typeIdResolverBase);
        when(typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, typeIdResolverBase, null)).thenCallRealMethod();
        typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, typeIdResolverBase, null);
    }
}
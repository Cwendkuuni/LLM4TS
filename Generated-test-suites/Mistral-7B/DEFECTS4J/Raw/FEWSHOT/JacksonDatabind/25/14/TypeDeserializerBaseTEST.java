package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

public class TypeDeserializerBaseTest {
    private DeserializationContext ctxt;
    private TypeIdResolver idResolver;
    private TypeDeserializerBase typeDeserializer;

    @Before
    public void setUp() {
        ctxt = mock(DeserializationContext.class);
        idResolver = mock(TypeIdResolver.class);
        typeDeserializer = new TypeDeserializerBase(Object.class, idResolver, null, false, null);
    }

    @Test
    public void testTypeDeserializerBaseConstruction() {
        assertEquals(Object.class, typeDeserializer._baseType);
        assertEquals(idResolver, typeDeserializer._idResolver);
        assertEquals(null, typeDeserializer._typePropertyName);
        assertFalse(typeDeserializer._typeIdVisible);
        assertNull(typeDeserializer._defaultImpl);
        assertNull(typeDeserializer._property);
    }

    @Test
    public void testForProperty() {
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializerBase newTypeDeserializer = typeDeserializer.forProperty(property);
        assertEquals(typeDeserializer, newTypeDeserializer);
        assertEquals(property, newTypeDeserializer._property);
    }

    @Test
    public void testGetTypeInclusion() {
        assertNull(typeDeserializer.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() {
        assertEquals(Object.class.getName(), typeDeserializer.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        assertNull(typeDeserializer.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertEquals(idResolver, typeDeserializer.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        assertNull(typeDeserializer.getDefaultImpl());
    }

    @Test
    public void testToString() {
        String expectedToString = "[TypeDeserializerBase; base-type:class java.lang.Object; id-resolver:mockTypeIdResolver]";
        assertEquals(expectedToString, typeDeserializer.toString());
    }

    @Test
    public void testFindDeserializer() throws IOException {
        String typeId = "MyTypeId";
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(idResolver.typeFromId(ctxt, typeId)).thenReturn(getJavaTypeMock());
        when(idResolver.typeFromId(ctxt, "AnotherTypeId")).thenReturn(getJavaTypeMock());
        JsonDeserializer<Object> returnedDeserializer = typeDeserializer._findDeserializer(ctxt, typeId);
        verify(idResolver).typeFromId(ctxt, typeId);
        assertEquals(deserializer, returnedDeserializer);
    }

    @Test
    public void testFindDeserializer_NullTypeId() throws IOException {
        JsonDeserializer<Object> returnedDeserializer = typeDeserializer._findDeserializer(ctxt, null);
        assertNull(returnedDeserializer);
    }

    @Test
    public void testFindDeserializer_UnknownTypeId() throws IOException {
        String unknownTypeId = "UnknownTypeId";
        when(idResolver.typeFromId(ctxt, unknownTypeId)).thenReturn(null);
        JsonDeserializer<Object> returnedDeserializer = typeDeserializer._findDeserializer(ctxt, unknownTypeId);
        assertNull(returnedDeserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        when(ctxt.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        JsonDeserializer<Object> defaultImplDeserializer = mock(JsonDeserializer.class);
        when(ctxt.findContextualValueDeserializer(getJavaTypeMock(), any())).thenReturn(defaultImplDeserializer);

        JsonDeserializer<Object> returnedDeserializer = typeDeserializer._findDefaultImplDeserializer(ctxt);
        verify(ctxt).findContextualValueDeserializer(getJavaTypeMock(), any());
        assertEquals(defaultImplDeserializer, returnedDeserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer_DisabledFailOnInvalidSubtype() throws IOException {
        when(ctxt.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(true);
        JsonDeserializer<Object> returnedDeserializer = typeDeserializer._findDefaultImplDeserializer(ctxt);
        assertNull(returnedDeserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer_NullDefaultImpl() {
        JsonDeserializer<Object> returnedDeserializer = typeDeserializer._findDefaultImplDeserializer(ctxt);
        assertNull(returnedDeserializer);
    }

    @Test
    public void testDeserializeWithNativeTypeId_NullTypeId() throws IOException {
        Object result = typeDeserializer._deserializeWithNativeTypeId(mock(JsonParser.class), ctxt, null);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeId_UnknownTypeId() throws IOException {
        String unknownTypeId = "UnknownTypeId";
        when(ctxt.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(true);

        try {
            typeDeserializer._deserializeWithNativeTypeId(mock(JsonParser.class), ctxt, unknownTypeId);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeWithNativeTypeId_KnownTypeId() throws IOException {
        String knownTypeId = "MyTypeId";
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(idResolver.typeFromId(ctxt, knownTypeId)).thenReturn(getJavaTypeMock());
        when(ctxt.findContextualValueDeserializer(getJavaTypeMock(), any())).thenReturn(deserializer);

        Object result = typeDeserializer._deserializeWithNativeTypeId(mock(JsonParser.class), ctxt, knownTypeId);
        verify(idResolver).typeFromId(ctxt, knownTypeId);
        verify(ctxt).findContextualValueDeserializer(getJavaTypeMock(), any());
        assertEquals(result, deserializer.deserialize(mock(JsonParser.class), ctxt));
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        String unknownTypeId = "UnknownTypeId";
        TypeIdResolverBase idResolverBaseMock = mock(TypeIdResolverBase.class);
        when(idResolverBaseMock.getDescForKnownTypeIds()).thenReturn("known type ids = MyTypeId, AnotherTypeId");
        when(idResolver.getClass()).thenReturn(idResolverBaseMock.getClass());

        try {
            typeDeserializer._handleUnknownTypeId(ctxt, unknownTypeId, idResolver, getJavaTypeMock());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            String expectedMessage = "Unknown type id 'UnknownTypeId' for class java.lang.Object";
            String actualMessage = e.getMessage();
            assertTrue(actualMessage.startsWith(expectedMessage));
        }
    }

    private JavaType getJavaTypeMock() {
        return mock(JavaType.class);
    }
}
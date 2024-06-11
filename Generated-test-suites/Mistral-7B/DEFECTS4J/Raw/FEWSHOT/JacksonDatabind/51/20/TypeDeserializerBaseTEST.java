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

/**
 * Test cases for {@link TypeDeserializerBase} class.
 */
public class TypeDeserializerBaseTest {

    @Test
    public void testFindDeserializerWithNullId() throws IOException {
        TypeDeserializerBase typeDeserializer = createTypeDeserializerBaseMock();
        DeserializationContext context = createContextMock();
        JsonDeserializer<Object> result = typeDeserializer._findDeserializer(context, null);
        assertNull(result);
    }

    @Test
    public void testFindDeserializerWithExistingId() throws IOException {
        TypeDeserializerBase typeDeserializer = createTypeDeserializerBaseMock();
        DeserializationContext context = createContextMock();
        String typeId = "existingId";
        JsonDeserializer<Object> expectedDeserializer = mock(JsonDeserializer.class);
        when(typeDeserializer._deserializers.get(typeId)).thenReturn(expectedDeserializer);

        JsonDeserializer<Object> result = typeDeserializer._findDeserializer(context, typeId);
        assertSame(expectedDeserializer, result);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        TypeDeserializerBase typeDeserializer = createTypeDeserializerBaseMock();
        DeserializationContext context = createContextMock();

        JsonDeserializer<Object> result = typeDeserializer._findDefaultImplDeserializer(context);
        assertSame(NullifyingDeserializer.instance, result);

        when(typeDeserializer._defaultImplDeserializer).thenReturn(new JsonDeserializer<Object>() {});
        result = typeDeserializer._findDefaultImplDeserializer(context);
        assertSame(typeDeserializer._defaultImplDeserializer, result);
    }

    @Test
    public void testHandleUnknownTypeIdReturnsNullWithNullIdResolver() throws IOException {
        TypeDeserializerBase typeDeserializer = createTypeDeserializerBaseMock();
        DeserializationContext context = createContextMock();
        JavaType result = typeDeserializer._handleUnknownTypeId(context, null, null, null);
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeIdReturnsNullWithNullTypeIdAndIdResolver() throws IOException {
        TypeDeserializerBase typeDeserializer = createTypeDeserializerBaseMock();
        DeserializationContext context = createContextMock();
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        JavaType result = typeDeserializer._handleUnknownTypeId(context, null, idResolver, null);
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeIdReturnsNullWithNullBaseType() throws IOException {
        TypeDeserializerBase typeDeserializer = createTypeDeserializerBaseMock();
        DeserializationContext context = createContextMock();
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        JavaType result = typeDeserializer._handleUnknownTypeId(context, "", idResolver, null);
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeIdReturnsNullWithEmptyTypeId() throws IOException {
        TypeDeserializerBase typeDeserializer = createTypeDeserializerBaseMock();
        DeserializationContext context = createContextMock();
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        JavaType result = typeDeserializer._handleUnknownTypeId(context, "", idResolver, null);
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeIdReturnsTypeDeserializerBaseIfTypeIdMatchesBaseType() throws IOException {
        TypeDeserializerBase typeDeserializer = createTypeDeserializerBaseMock();
        DeserializationContext context = createContextMock();
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.typeFromId(context, "TypeDeserializerBase")).thenReturn(typeDeserializer._baseType);
        JavaType result = typeDeserializer._handleUnknownTypeId(context, "TypeDeserializerBase", idResolver, null);
        assertSame(typeDeserializer._baseType, result);
    }

    @Test
    public void testHandleUnknownTypeIdReturnsDefaultImplIfNoTypeIdAvailable() throws IOException {
        TypeDeserializerBase typeDeserializer = createTypeDeserializerBaseMock();
        DeserializationContext context = createContextMock();
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        JavaType result = typeDeserializer._handleUnknownTypeId(context, null, idResolver, null);
        assertSame(typeDeserializer._defaultImpl, result);
    }

    @Test
    public void testHandleUnknownTypeIdReturnsTypeIdResolverTypeIfTypeIdIsKnownToTypeIdResolver() throws IOException {
        TypeDeserializerBase typeDeserializer = createTypeDeserializerBaseMock();
        DeserializationContext context = createContextMock();
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.typeFromId(context, "knownTypeId")).thenReturn(new SimpleType("knownTypeId"));
        JavaType result = typeDeserializer._handleUnknownTypeId(context, "knownTypeId", idResolver, null);
        assertSame(new SimpleType("knownTypeId"), result);
    }

    @Test
    public void testHandleUnknownTypeIdReturnsNullIfTypeIdCannotBeResolvedByTypeIdResolver() throws IOException {
        TypeDeserializerBase typeDeserializer = createTypeDeserializerBaseMock();
        DeserializationContext context = createContextMock();
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.typeFromId(context, "unknownTypeId")).thenReturn(null);
        JavaType result = typeDeserializer._handleUnknownTypeId(context, "unknownTypeId", idResolver, null);
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeIdReturnsContextualDeserializerIfAvailable() throws IOException {
        TypeDeserializerBase typeDeserializer = createTypeDeserializerBaseMock();
        DeserializationContext context = createContextMock();
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        JavaType baseType = new SimpleType("java.util.List");
        when(idResolver.typeFromId(context, "contextualTypeId")).thenReturn(new SimpleType("java.util.ArrayList"));
        JsonDeserializer<Object> contextualDeserializer = mock(JsonDeserializer.class);
        when(context.findContextualValueDeserializer(baseType, null)).thenReturn(contextualDeserializer);

        JavaType result = typeDeserializer._handleUnknownTypeId(context, "contextualTypeId", idResolver, baseType);
        assertSame(contextualDeserializer, result);
    }

    @Test
    public void testHandleUnknownTypeIdReturnsNullIfContextualDeserializerIsNotAvailable() throws IOException {
        TypeDeserializerBase typeDeserializer = createTypeDeserializerBaseMock();
        DeserializationContext context = createContextMock();
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        JavaType baseType = new SimpleType("java.util.List");
        when(idResolver.typeFromId(context, "contextualTypeId")).thenReturn(new SimpleType("java.util.ArrayList"));
        when(context.findContextualValueDeserializer(baseType, null)).thenReturn(null);

        JavaType result = typeDeserializer._handleUnknownTypeId(context, "contextualTypeId", idResolver, baseType);
        assertNull(result);
    }

    private DeserializationContext createContextMock() {
        return mock(DeserializationContext.class);
    }

    private TypeDeserializerBase createTypeDeserializerBaseMock() {
        return mock(TypeDeserializerBase.class);
    }
}
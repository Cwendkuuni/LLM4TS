package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.fasterxml.jackson.databind.util.Converter;

public class TypeDeserializerBaseTest {

    @Test
    public void testFindDeserializerWithNullTypeId() throws IOException {
        TypeDeserializerBase typeDeserializer = createTypeDeserializerBaseMock();
        DeserializationContext context = createMockContext();
        assertNull(typeDeserializer._findDeserializer(context, null));
    }

    @Test
    public void testFindDeserializerWithValidTypeId() throws IOException {
        TypeDeserializerBase typeDeserializer = createTypeDeserializerBaseMock();
        String typeId = "com.example.MyType";
        DeserializationContext context = createMockContext();
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(typeDeserializer._deserializers.get(typeId)).thenReturn(deserializer);

        JsonDeserializer<Object> result = typeDeserializer._findDeserializer(context, typeId);
        assertSame(deserializer, result);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        TypeDeserializerBase typeDeserializer = createTypeDeserializerBaseMock();
        DeserializationContext context = createMockContext();

        JsonDeserializer<Object> result = typeDeserializer._findDefaultImplDeserializer(context);
        assertNull(result);
    }

    @Test
    public void testFindDefaultImplDeserializerWithDeserializer() throws IOException {
        TypeDeserializerBase typeDeserializer = createTypeDeserializerBaseMock();
        DeserializationContext context = createMockContext();
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);

        when(typeDeserializer._defaultImplDeserializer).thenReturn(deserializer);

        JsonDeserializer<Object> result = typeDeserializer._findDefaultImplDeserializer(context);
        assertSame(deserializer, result);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        TypeDeserializerBase typeDeserializer = createTypeDeserializerBaseMock();
        DeserializationContext context = createMockContext();

        JavaType result = typeDeserializer._handleUnknownTypeId(context, "unknown_type_id");
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeIdWithCustomTypeIdResolver() throws IOException {
        TypeDeserializerBase typeDeserializer = createTypeDeserializerBaseMock();
        DeserializationContext context = createMockContext();
        TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
        when(typeIdResolver.typeFromId(context, "custom_type_id")).thenReturn(mock(JavaType.class));
        typeDeserializer._idResolver = typeIdResolver;

        JavaType result = typeDeserializer._handleUnknownTypeId(context, "custom_type_id");
        // This test assumes that the returned JavaType is not null, as the mock implementation does not
        // throw an exception. If the implementation changes, this test needs to be updated accordingly.
        assertNotNull(result);
    }

    @Test
    public void testHandleMissingTypeId() throws IOException {
        TypeDeserializerBase typeDeserializer = createTypeDeserializerBaseMock();
        DeserializationContext context = createMockContext();

        JavaType result = typeDeserializer._handleMissingTypeId(context, "missing_type_id");
        assertNull(result);
    }

    private TypeDeserializerBase createTypeDeserializerBaseMock() {
        TypeDeserializerBase typeDeserializer = mock(TypeDeserializerBase.class);
        when(typeDeserializer._deserializers).thenReturn(new ConcurrentHashMap<>());
        return typeDeserializer;
    }

    private DeserializationContext createMockContext() {
        DeserializationContext context = mock(DeserializationContext.class);
        ObjectCodec objectMapper = mock(ObjectCodec.class);
        when(context.getObjectCodec()).thenReturn(objectMapper);
        return context;
    }
}
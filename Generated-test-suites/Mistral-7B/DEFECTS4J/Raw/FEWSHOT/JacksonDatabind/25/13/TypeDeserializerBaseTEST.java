package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
 * Unit tests for {@link TypeDeserializerBase} class.
 */
public class TypeDeserializerBaseTest {

    @Test(expected = IOException.class)
    public void testDefaultImplDeserializerNullWhenFailOnInvalidSubtypeEnabled() throws IOException {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);
        when(ctxt.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(true);

        typeDeserializerBase._findDefaultImplDeserializer(ctxt);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);

        when(idResolver.typeFromId(ctxt, "typeId1")).thenReturn(mock(JavaType.class));
        when(idResolver.typeFromId(ctxt, "typeId2")).thenReturn(null);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);

        JsonDeserializer<Object> foundDeserializer = typeDeserializerBase._findDeserializer(ctxt, "typeId1");
        assertNotNull(foundDeserializer);
        assertNotNull(foundDeserializer.getClass());

        foundDeserializer = typeDeserializerBase._findDeserializer(ctxt, "typeId2");
        assertNull(foundDeserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        JavaType baseType = mock(JavaType.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<Object> defaultImplDeserializer = mock(JsonDeserializer.class);

        when(ctxt.findContextualValueDeserializer(baseType, null)).thenReturn(defaultImplDeserializer);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, null, false, null);
        JsonDeserializer<Object> foundDeserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertNotNull(foundDeserializer);
        assertNotNull(foundDeserializer.getClass());
        assertSame(defaultImplDeserializer, foundDeserializer);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(idResolver.getTypeIdResolverBase()).thenReturn(null);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);

        typeDeserializerBase._handleUnknownTypeId(ctxt, "unknownTypeId", idResolver, baseType);
    }

    @Test
    public void testHandleUnknownTypeIdWithTypeIdResolverBase() throws IOException {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String knownTypeIds = "String,Integer,List<String>";

        when(idResolver.getTypeIdResolverBase()).thenReturn(new TypeIdResolverBase() {
            @Override
            public String getDescForKnownTypeIds() {
                return knownTypeIds;
            }
        });

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);

        typeDeserializerBase._handleUnknownTypeId(ctxt, "unknownTypeId", idResolver, baseType);
    }
}
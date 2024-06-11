package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
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
import com.fasterxml.jackson.databind.util.JsonTestUtils;

public class TypeDeserializerBaseTest {

    @Test
    public void testTypeDeserializerBaseConstruction() throws Exception {
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType baseType = JsonTestUtils.createSimpleType(String.class);
        JavaType defaultImpl = JsonTestUtils.createSimpleType(Integer.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        assertNotNull(typeDeserializerBase);
        assertEquals(baseType, typeDeserializerBase.getBaseType());
        assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
        assertEquals(typePropertyName, typeDeserializerBase.getPropertyName());
        assertEquals(typeIdVisible, typeDeserializerBase.isTypeIdVisible());
        assertEquals(defaultImpl, typeDeserializerBase.getDefaultImpl());
        assertEquals(16, typeDeserializerBase.getDeserializers().size());
        assertNull(typeDeserializerBase.getDefaultImplDeserializer());

        TypeDeserializerBase copy = new TypeDeserializerBase(typeDeserializerBase, null);

        assertNotNull(copy);
        assertEquals(baseType, copy.getBaseType());
        assertEquals(idResolver, copy.getTypeIdResolver());
        assertEquals(typePropertyName, copy.getPropertyName());
        assertEquals(typeIdVisible, copy.isTypeIdVisible());
        assertEquals(defaultImpl, copy.getDefaultImpl());
        assertEquals(typeDeserializerBase.getDeserializers(), copy.getDeserializers());
        assertEquals(typeDeserializerBase.getDefaultImplDeserializer(), copy.getDefaultImplDeserializer());
    }

    @Test
    public void testForProperty() throws Exception {
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType baseType = JsonTestUtils.createSimpleType(String.class);
        JavaType defaultImpl = JsonTestUtils.createSimpleType(Integer.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        BeanProperty prop = mock(BeanProperty.class);
        TypeDeserializerBase copy = typeDeserializerBase.forProperty(prop);

        assertNotNull(copy);
        assertNotNull(copy.getProperty());
        assertEquals(prop, copy.getProperty());
        assertEquals(baseType, copy.getBaseType());
        assertEquals(idResolver, copy.getTypeIdResolver());
        assertEquals(typePropertyName, copy.getPropertyName());
        assertEquals(typeIdVisible, copy.isTypeIdVisible());
        assertEquals(defaultImpl, copy.getDefaultImpl());
        assertEquals(typeDeserializerBase.getDeserializers(), copy.getDeserializers());
        assertEquals(typeDeserializerBase.getDefaultImplDeserializer(), copy.getDefaultImplDeserializer());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typeId = "someTypeId";
        JavaType type = JsonTestUtils.createSimpleType(String.class);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, idResolver, null, false, null);

        when(idResolver.typeFromId(any(DeserializationContext.class), eq(typeId))).thenReturn(type);
        when(idResolver.findValueDeserializer(type, any(DeserializationContext.class))).thenReturn(deserializer);

        JsonDeserializer<Object> result = typeDeserializerBase.findDeserializer(mock(DeserializationContext.class), typeId);

        assertNotNull(result);
        assertEquals(deserializer, result);
        verify(idResolver).typeFromId(any(DeserializationContext.class), eq(typeId));
        verify(idResolver).findValueDeserializer(type, any(DeserializationContext.class));
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        JavaType defaultImpl = JsonTestUtils.createSimpleType(Integer.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, idResolver, null, false, defaultImpl);

        JsonDeserializer<Object> result = typeDeserializerBase.findDefaultImplDeserializer(mock(DeserializationContext.class));

        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String typeId = "unknownTypeId";

        when(idResolver.getTypeIdResolverBase()).thenReturn(new TypeIdResolverBase() {
            @Override
            public String getDescForKnownTypeIds() {
                return "someDesc";
            }
        });

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, idResolver, null, false, null);

        JavaType result = typeDeserializerBase.handleUnknownTypeId(ctxt, typeId, idResolver, null);

        assertNotNull(result);
        assertEquals(ctxt.handleUnknownTypeId(null, typeId, idResolver, "someDesc").getType(), result.getType());
    }
}
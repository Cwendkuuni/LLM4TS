package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testTypeDeserializerBaseConstruction() {
        JavaType baseType = null;
        TypeIdResolver idResolver = null;
        String typePropertyName = null;
        boolean typeIdVisible = false;
        JavaType defaultImpl = null;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertNull(typeDeserializerBase.getPropertyName());
        assertNull(typeDeserializerBase.getDefaultImplDeserializer());

        BeanProperty property = null;
        TypeDeserializerBase typeDeserializerBase2 = new TypeDeserializerBase(typeDeserializerBase, property);
        assertEquals("TypeDeserializerBase", typeDeserializerBase2.getClass().getName());
        assertEquals(typeDeserializerBase.baseType, typeDeserializerBase2.baseType);
        assertEquals(typeDeserializerBase.idResolver, typeDeserializerBase2.idResolver);
        assertEquals(typeDeserializerBase.typePropertyName, typeDeserializerBase2.typePropertyName);
        assertEquals(typeDeserializerBase.typeIdVisible, typeDeserializerBase2.typeIdVisible);
        assertEquals(typeDeserializerBase.deserializers, typeDeserializerBase2.deserializers);
        assertEquals(typeDeserializerBase.defaultImpl, typeDeserializerBase2.defaultImpl);
        assertEquals(typeDeserializerBase.property, typeDeserializerBase2.property);
        assertEquals(typeDeserializerBase.defaultImplDeserializer, typeDeserializerBase2.defaultImplDeserializer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeDeserializerBaseIllegalArgument() {
        JavaType baseType = null;
        TypeIdResolver idResolver = null;
        String typePropertyName = null;
        boolean typeIdVisible = false;
        JavaType defaultImpl = null;

        new TypeDeserializerBase(baseType, idResolver, null, typeIdVisible, defaultImpl);
    }

    @Test
    public void testTypeDeserializerBaseForProperty() {
        JavaType baseType = null;
        TypeIdResolver idResolver = Mockito.mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = null;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        BeanProperty property = Mockito.mock(BeanProperty.class);
        TypeDeserializerBase typeDeserializerBase2 = typeDeserializerBase.forProperty(property);

        assertEquals("TypeDeserializerBase", typeDeserializerBase2.getClass().getName());
        assertEquals(typeDeserializerBase.baseType, typeDeserializerBase2.baseType);
        assertEquals(typeDeserializerBase.idResolver, typeDeserializerBase2.idResolver);
        assertEquals(typeDeserializerBase.typePropertyName, typeDeserializerBase2.typePropertyName);
        assertEquals(typeDeserializerBase.typeIdVisible, typeDeserializerBase2.typeIdVisible);
        assertEquals(typeDeserializerBase.deserializers, typeDeserializerBase2.deserializers);
        assertEquals(typeDeserializerBase.defaultImpl, typeDeserializerBase2.defaultImpl);
        assertEquals(typeDeserializerBase.property, typeDeserializerBase2.property);
        assertEquals(typeDeserializerBase.defaultImplDeserializer, typeDeserializerBase2.defaultImplDeserializer);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        JavaType baseType = Mockito.mock(JavaType.class);
        TypeIdResolver idResolver = Mockito.mock(TypeIdResolver.class);
        String typeId = "typeId";
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = null;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        JsonDeserializer<Object> deserializer = Mockito.mock(JsonDeserializer.class);
        when(idResolver.typeFromId(ctxt, typeId)).thenReturn(Mockito.mock(JavaType.class));
        when(ctxt.findContextualValueDeserializer(Mockito.any(JavaType.class), Mockito.eq(typePropertyName))).thenReturn(deserializer);

        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(ctxt, typeId);
        assertEquals(deserializer, result);
        verify(idResolver, times(1)).typeFromId(ctxt, typeId);
        verify(ctxt, times(1)).findContextualValueDeserializer(Mockito.any(JavaType.class), Mockito.eq(typePropertyName));
    }

    @Test
    public void testFindDeserializerNullTypeId() throws IOException {
        JavaType baseType = Mockito.mock(JavaType.class);
        TypeIdResolver idResolver = Mockito.mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = null;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(ctxt, null);
        assertNull(result);
        verifyNoInteractions(idResolver, ctxt);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        JavaType baseType = Mockito.mock(JavaType.class);
        TypeIdResolver idResolver = Mockito.mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = Mockito.mock(JavaType.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        JsonDeserializer<Object> deserializer = Mockito.mock(JsonDeserializer.class);
        when(ctxt.findContextualValueDeserializer(defaultImpl, typePropertyName)).thenReturn(deserializer);

        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertEquals(deserializer, result);
        verify(ctxt, times(1)).findContextualValueDeserializer(defaultImpl, typePropertyName);
    }

    @Test
    public void testFindDefaultImplDeserializerNull() throws IOException {
        JavaType baseType = Mockito.mock(JavaType.class);
        TypeIdResolver idResolver = Mockito.mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = null;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertNull(result);
        verifyNoInteractions(ctxt);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        JavaType baseType = Mockito.mock(JavaType.class);
        TypeIdResolver idResolver = Mockito.mock(TypeIdResolver.class);
        String typeId = "typeId";
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = null;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        JsonMappingException result = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId);
        assertNotNull(result);
        verify(ctxt, times(1)).handleUnknownTypeId(baseType, typeId, idResolver, "type ids are not statically known (for POJO property 'typePropertyName')");
    }

    @Test
    public void testHandleMissingTypeId() throws IOException {
        JavaType baseType = Mockito.mock(JavaType.class);
        TypeIdResolver idResolver = Mockito.mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = null;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        JsonMappingException result = typeDeserializerBase._handleMissingTypeId(ctxt, "");
        assertNotNull(result);
        verify(ctxt, times(1)).handleMissingTypeId(baseType, idResolver, "");
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JavaType baseType = Mockito.mock(JavaType.class);
        TypeIdResolver idResolver = Mockito.mock(TypeIdResolver.class);
        String typeId = "typeId";
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = null;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        JsonDeserializer<Object> deserializer = Mockito.mock(JsonDeserializer.class);
        when(idResolver.typeFromId(ctxt, typeId)).thenReturn(Mockito.mock(JavaType.class));
        when(ctxt.findContextualValueDeserializer(Mockito.any(JavaType.class), Mockito.eq(typePropertyName))).thenReturn(deserializer);

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId);
        assertEquals(result, deserializer.deserialize(jp, ctxt));
        verify(idResolver, times(1)).typeFromId(ctxt, typeId);
        verify(ctxt, times(1)).findContextualValueDeserializer(Mockito.any(JavaType.class), Mockito.eq(typePropertyName));
    }

    @Test
    public void testDeserializeWithNativeTypeIdNullTypeId() throws IOException {
        JavaType baseType = Mockito.mock(JavaType.class);
        TypeIdResolver idResolver = Mockito.mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = null;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, null);
        assertNull(result);
        verifyNoInteractions(idResolver, ctxt);
    }
}
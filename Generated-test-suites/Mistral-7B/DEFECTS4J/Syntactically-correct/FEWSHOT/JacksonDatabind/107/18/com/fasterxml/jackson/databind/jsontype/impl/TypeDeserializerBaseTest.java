package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

/**
 * Test cases for {@link TypeDeserializerBase} class.
 */
public class TypeDeserializerBaseTest {

    @Test
    public void testTypeDeserializerBase() {
        // Test empty constructor
        TypeDeserializerBase td = new TypeDeserializerBase(null, null, null, false, null);
        assertNotNull(td);

        // Test constructor with arguments
        Class<?> baseType = Object.class;
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = Object.class;
        TypeDeserializerBase td2 = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals("TypeDeserializerBase", td2.getClass().getName());
        assertEquals(baseType, td2.baseType());
        assertEquals(idResolver, td2.getTypeIdResolver());
        assertEquals(typePropertyName, td2.getPropertyName());
        assertEquals(typeIdVisible, td2.isTypeIdVisible());
        assertEquals(defaultImpl, td2.getDefaultImpl());
    }

    @Test
    public void testForProperty() {
        TypeDeserializerBase td = new TypeDeserializerBase(null, null, null, false, null);
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializerBase td2 = td.forProperty(property);
        assertNotNull(td2);
        assertEquals(td, td2._baseType);
        assertEquals(td._idResolver, td2._idResolver);
        assertEquals(td._typePropertyName, td2._typePropertyName);
        assertEquals(td._typeIdVisible, td2._typeIdVisible);
        assertEquals(td._deserializers, td2._deserializers);
        assertNull(td2._defaultImpl);
        assertNull(td2._property);
    }

    @Test
    public void testGetTypeInclusion() {
        TypeDeserializerBase td = new TypeDeserializerBase(null, null, null, false, null);
        JsonTypeInfo.As inclusion = td.getTypeInclusion();
        assertNull(inclusion);
    }

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase td = new TypeDeserializerBase(null, null, null, false, null);
        assertEquals("class java.lang.Object", td.baseTypeName());
    }

    @Test
    public void testFindDeserializer() throws IOException {
        TypeDeserializerBase td = new TypeDeserializerBase(null, null, null, false, null);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String typeId = "typeId";
        JavaType type = Object.class;
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(td._deserializers.get(typeId)).thenReturn(deserializer);
        Object result = td._findDeserializer(ctxt, typeId);
        assertEquals(deserializer, result);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        TypeDeserializerBase td = new TypeDeserializerBase(null, null, null, false, null);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Class<?> defaultImpl = Object.class;
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(td._defaultImplDeserializer).thenReturn(deserializer);
        Object result = td._findDefaultImplDeserializer(ctxt);
        assertEquals(deserializer, result);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        TypeDeserializerBase td = new TypeDeserializerBase(null, null, null, false, null);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String typeId = "typeId";
        JsonMappingException exception = mock(JsonMappingException.class);
        when(ctxt.handleUnknownTypeId(null, typeId, null, "")).thenReturn(exception);
        Object result = td._handleUnknownTypeId(ctxt, typeId);
        assertNull(result);
        verify(ctxt).handleUnknownTypeId(td.baseType(), typeId, td.getTypeIdResolver(), "");
    }

    @Test
    public void testHandleMissingTypeId() throws IOException {
        TypeDeserializerBase td = new TypeDeserializerBase(null, null, null, false, null);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonMappingException exception = mock(JsonMappingException.class);
        when(ctxt.handleMissingTypeId(td.baseType(), td.getTypeIdResolver(), "")).thenReturn(exception);
        Object result = td._handleMissingTypeId(ctxt, "");
        assertNull(result);
        verify(ctxt).handleMissingTypeId(td.baseType(), td.getTypeIdResolver(), "");
    }
}
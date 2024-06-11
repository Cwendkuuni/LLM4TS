package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test class for TypeDeserializerBase.
 */
@RunWith(MockitoJUnitRunner.class)
public class TypeDeserializerBaseTest {

    @InjectMocks
    private TypeDeserializerBase typeDeserializerBase;

    @Test
    public void testForProperty() throws IOException {
        BeanProperty beanProperty = Mockito.mock(BeanProperty.class);
        TypeDeserializer forProperty = typeDeserializerBase.forProperty(beanProperty);
        assertNotNull(forProperty);
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertNull(typeInclusion);
    }

    @Test
    public void testBaseTypeName() {
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertNull(baseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        String propertyName = typeDeserializerBase.getPropertyName();
        assertEquals("", propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver typeIdResolver = typeDeserializerBase.getTypeIdResolver();
        assertNotNull(typeIdResolver);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
        assertNull(defaultImpl);
    }

    @Test
    public void testToString() {
        String toString = typeDeserializerBase.toString();
        assertTrue(toString.contains("[TypeDeserializerBase"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testForPropertyThrowException() {
        typeDeserializerBase.forProperty(null);
    }

    @Test
    public void testFindDeserializerNullTypeId() throws IOException {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, null);
        assertEquals(NullifyingDeserializer.instance, deserializer);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerUnknownTypeId() throws IOException {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        String typeId = "unknownTypeId";
        typeDeserializerBase._findDeserializer(context, typeId);
    }

    @Test
    public void testFindDefaultImplDeserializerNullContext() throws IOException {
        DeserializationContext context = null;
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerWithContext() throws IOException {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        JsonDeserializer<Object> deserializer = Mockito.mock(JsonDeserializer.class);
        when(typeDeserializerBase._property).thenReturn(Mockito.mock(BeanProperty.class));
        when(context.findContextualValueDeserializer(Mockito.any(JavaType.class), Mockito.any(BeanProperty.class)))
                .thenReturn(deserializer);
        JsonDeserializer<Object> findDefaultImplDeserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertEquals(deserializer, findDefaultImplDeserializer);
    }

    @Test
    public void testDeserializeWithNativeTypeIdNullContext() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = null;
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context, null);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeIdNullTypeId() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context, null);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeIdUnknownTypeId() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        String typeId = "unknownTypeId";
        when(context.handleUnknownTypeId(Mockito.any(JavaType.class), Mockito.eq(typeId), Mockito.any(TypeIdResolver.class), Mockito.anyString()))
                .thenThrow(new IOException("Unknown type id"));
        try {
            typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context, typeId);
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("Unknown type id", e.getMessage());
        }
    }

    @Test
    public void testDeserializeWithNativeTypeIdValidTypeId() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        String typeId = "validTypeId";
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context, typeId);
        assertNotNull(result);
    }
}
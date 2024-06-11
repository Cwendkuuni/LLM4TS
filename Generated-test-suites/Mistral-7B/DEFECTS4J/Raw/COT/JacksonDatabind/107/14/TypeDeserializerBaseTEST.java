package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test class for TypeDeserializerBase.
 */
public class TypeDeserializerBaseTest {

    @Test
    public void testForProperty() throws IOException {
        // Given
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mock(JavaType.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        BeanProperty property = mock(BeanProperty.class);

        // When
        TypeDeserializer result = typeDeserializerBase.forProperty(property);

        // Then
        assertEquals("Unexpected typeDeserializerBase type for property", TypeDeserializer.class, result.getClass());
    }

    @Test
    public void testGetTypeInclusion() {
        // Given
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);

        // When
        JsonTypeInfo.As result = typeDeserializerBase.getTypeInclusion();

        // Then
        assertNull("Unexpected typeInclusion", result);
    }

    @Test
    public void testBaseTypeName() throws IOException {
        // Given
        JavaType baseType = mock(JavaType.class);
        when(baseType.getRawClass()).thenReturn(Object.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, null, false, null);

        // When
        String result = typeDeserializerBase.baseTypeName();

        // Then
        assertEquals("Unexpected baseTypeName", "java.lang.Object", result);
    }

    @Test
    public void testToString() throws IOException {
        // Given
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, null, false, null);

        // When
        String result = typeDeserializerBase.toString();

        // Then
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        verify(idRes).toString(stringCaptor.capture());
        String idResString = stringCaptor.getValue();
        String expectedToString = String.format("[%s; base-type:%s; id-resolver: %s]",
                TypeDeserializerBase.class.getName(), baseType, idRes);
        assertEquals("Unexpected toString", expectedToString, result);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithNullContext() throws IOException {
        // Given
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);

        // When
        typeDeserializerBase._findDeserializer(null, "typeId");

        // Then
        // IOException should be thrown
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithNullTypeId() throws IOException {
        // Given
        DeserializationContext context = mock(DeserializationContext.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);

        // When
        typeDeserializerBase._findDeserializer(context, null);

        // Then
        // IOException should be thrown
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithNullTypeIdResolver() throws IOException {
        // Given
        JavaType baseType = mock(JavaType.class);
        DeserializationContext context = mock(DeserializationContext.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, null, false, null);

        // When
        typeDeserializerBase._findDeserializer(context, "typeId");

        // Then
        // IOException should be thrown
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithNullTypeIdAndContext() throws IOException {
        // Given

        // When
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);

        // Then
        // IOException should be thrown
        typeDeserializerBase._findDeserializer(null, null);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithNullTypeIdAndContextAndTypeId() throws IOException {
        // Given

        // When
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);

        // Then
        // IOException should be thrown
        typeDeserializerBase._findDeserializer(null, null, "typeId");
    }

    @Test
    public void testFindDeserializerWithEmptyMap() throws IOException {
        // Given
        JavaType baseType = mock(JavaType.class);
        DeserializationContext context = mock(DeserializationContext.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, null, false, null);

        // When
        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(context, "typeId");

        // Then
        assertNull("Unexpected result", result);
    }

    @Test
    public void testFindDeserializerWithDefaultImpl() throws IOException {
        // Given
        JavaType baseType = mock(JavaType.class);
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType defaultImpl = mock(JavaType.class);

        when(defaultImpl.getRawClass()).thenReturn(String.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, null, false, defaultImpl);

        // When
        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(context, "typeId");

        // Then
        assertEquals("Unexpected result", NullifyingDeserializer.instance.getClass(), result.getClass());
    }

    @Test
    public void testFindDeserializerWithTypeIdNotFound() throws IOException {
        // Given
        JavaType baseType = mock(JavaType.class);
        DeserializationContext context = mock(DeserializationContext.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, null, false, null);

        // When
        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(context, "unknownTypeId");

        // Then
        assertNull("Unexpected result", result);
    }

    @Test
    public void testFindDeserializerWithTypeIdResolved() throws IOException {
        // Given
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);

        when(idRes.typeFromId(context, "typeId")).thenReturn(type);
        when(context.findContextualValueDeserializer(type, null)).thenReturn(deserializer);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, null, false, null);

        // When
        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(context, "typeId");

        // Then
        assertEquals("Unexpected result", deserializer, result);
    }

    @Test(expected = IOException.class)
    public void testFindDefaultImplDeserializerWithNullContext() throws IOException {
        // Given
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);

        // When
        typeDeserializerBase._findDefaultImplDeserializer(null);

        // Then
        // IOException should be thrown
    }

    @Test(expected = IOException.class)
    public void testFindDefaultImplDeserializerWithNullType() throws IOException {
        // Given
        DeserializationContext context = mock(DeserializationContext.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);

        // When
        typeDeserializerBase._findDefaultImplDeserializer(context);

        // Then
        // IOException should be thrown
    }

    @Test
    public void testFindDefaultImplDeserializerWithDefaultImpl() throws IOException {
        // Given
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType defaultImpl = mock(JavaType.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, defaultImpl);

        // When
        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(context);

        // Then
        assertNull("Unexpected result", result);
    }

    @Test
    public void testFindDefaultImplDeserializerWithDefaultImplDeserializer() throws IOException {
        // Given
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType defaultImpl = mock(JavaType.class);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);

        when(defaultImpl.getRawClass()).thenReturn(Object.class);
        when(context.findContextualValueDeserializer(defaultImpl, null)).thenReturn(deserializer);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, defaultImpl);

        // When
        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(context);

        // Then
        assertEquals("Unexpected result", deserializer, result);
    }
}
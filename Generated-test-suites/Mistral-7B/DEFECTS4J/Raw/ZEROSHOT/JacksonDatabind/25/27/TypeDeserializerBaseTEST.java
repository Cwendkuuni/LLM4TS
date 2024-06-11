package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ConcurrentHashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.TestBase;

public class TypeDeserializerBaseTest extends TestBase {

    @Test
    public void testTypeDeserializerBase() throws IOException, JsonProcessingException {
        String typePropertyName = "type";
        Class<?> defaultImpl = String.class;
        TypeDeserializerBase typeDeserializerBase = createTypeDeserializerBase();

        // Test constructor
        assertNotNull(typeDeserializerBase);
        assertEquals(TypeDeserializerBase.class, typeDeserializerBase.getClass());
        assertEquals(TypeDeserializer.class, typeDeserializerBase.getClass().getSuperclass());
        assertEquals(_defaultImpl, typeDeserializerBase._defaultImpl);
        assertEquals(typePropertyName, typeDeserializerBase._typePropertyName);
        assertEquals(_typeIdVisible, typeDeserializerBase._typeIdVisible);
        assertEquals(_deserializers, typeDeserializerBase._deserializers);
        assertEquals(_defaultImplDeserializer, typeDeserializerBase._defaultImplDeserializer);

        BeanProperty property = new BeanProperty("property", String.class);
        TypeDeserializerBase copy = typeDeserializerBase.forProperty(property);

        // Test copy constructor
        assertEquals(typeDeserializerBase._baseType, copy._baseType);
        assertEquals(typeDeserializerBase._idResolver, copy._idResolver);
        assertEquals(typeDeserializerBase._typePropertyName, copy._typePropertyName);
        assertEquals(typeDeserializerBase._typeIdVisible, copy._typeIdVisible);
        assertEquals(typeDeserializerBase._deserializers, copy._deserializers);
        assertEquals(typeDeserializerBase._defaultImpl, copy._defaultImpl);
        assertEquals(typeDeserializerBase._defaultImplDeserializer, copy._defaultImplDeserializer);
        assertNotNull(copy._property);
        assertEquals("property", copy._property.getName());
        assertEquals(String.class, copy._property.getType());

        // Test getTypeInclusion
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertEquals(JsonTypeInfo.As.WRAP_EXISTING_ARRAY_TYPES, typeInclusion);

        // Test baseTypeName
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertEquals(defaultImpl.getName(), baseTypeName);

        // Test getPropertyName
        String getPropertyName = typeDeserializerBase.getPropertyName();
        assertEquals(typePropertyName, getPropertyName);

        // Test getTypeIdResolver
        TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
        typeDeserializerBase.getTypeIdResolver();
        verify(typeIdResolver).getClass();

        // Test getDefaultImpl
        Class<?> getDefaultImpl = typeDeserializerBase.getDefaultImpl();
        assertEquals(defaultImpl, getDefaultImpl);

        // Test toString
        String toString = typeDeserializerBase.toString();
        assertTrue(toString.contains("[TypeDeserializerBase"));
        assertTrue(toString.contains("base-type:" + defaultImpl.getName()));
        assertTrue(toString.contains("id-resolver: " + typeIdResolver.getClass().getName()));

        // Test _findDeserializer
        DeserializationContext context = mock(DeserializationContext.class);
        JsonDeserializer<Object> jsonDeserializer = mock(JsonDeserializer.class);
        when(context.findContextualValueDeserializer(eq(String.class), any())).thenReturn(jsonDeserializer);
        String typeId = "TestType";
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, typeId);
        assertEquals(jsonDeserializer, deserializer);

        // Test _findDefaultImplDeserializer
        when(context.findContextualValueDeserializer(_defaultImpl, any())).thenReturn(jsonDeserializer);
        JsonDeserializer<Object> defaultImplDeserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertEquals(jsonDeserializer, defaultImplDeserializer);

        // Test _deserializeWithNativeTypeId
        JsonParser jsonParser = createContextualParser();
        JsonDeserializer<Object> result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context, null);
        assertNotNull(result);

        // Test _deserializeWithNativeTypeId with known type id
        typeId = "TestType";
        JsonDeserializer<Object> expectedDeserializer = mock(JsonDeserializer.class);
        when(context.findContextualValueDeserializer(eq(String.class), any())).thenReturn(expectedDeserializer);
        result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context, typeId);
        assertEquals(expectedDeserializer, result);

        // Test _deserializeWithNativeTypeId with unknown type id
        typeId = "UnknownType";
        when(context.findContextualValueDeserializer(eq(String.class), any())).thenReturn(null);
        try {
            typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context, typeId);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }

        // Test _handleUnknownTypeId
        JsonMappingException jsonMappingException = new JsonMappingException("Test");
        when(context.unknownTypeException(_defaultImpl, typeId, anyString())).thenThrow(jsonMappingException);
        try {
            typeDeserializerBase._handleUnknownTypeId(context, typeId, typeIdResolver, _defaultImpl);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals(jsonMappingException, e);
        }

        // Test findDeserializer
        Converter<String, JsonDeserializer<Object>> converter = mock(Converter.class);
        when(context.findValueDeserializer(eq(String.class), any())).thenReturn(converter);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, typeId);
        assertEquals(converter, deserializer);

        // Test findDeserializer when default implementation is present
        when(context.findValueDeserializer(eq(String.class), any())).thenReturn(null);
        when(context.findContextualValueDeserializer(_defaultImpl, any())).thenReturn(jsonDeserializer);
        deserializer = typeDeserializerBase._findDeserializer(context, typeId);
        assertEquals(jsonDeserializer, deserializer);

        // Test findDeserializer when default implementation is not present and no deserializer is found
        when(context.findValueDeserializer(eq(String.class), any())).thenReturn(null);
        when(context.findContextualValueDeserializer(_defaultImpl, any())).thenReturn(null);
        try {
            typeDeserializerBase._findDeserializer(context, typeId);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }

        // Test findDeserializer when default implementation is not present and type is null
        when(context.findValueDeserializer(eq(String.class), any())).thenReturn(null);
        when(context.findContextualValueDeserializer(_defaultImpl, any())).thenReturn(null);
        try {
            typeDeserializerBase._findDeserializer(context, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    private TypeDeserializerBase createTypeDeserializerBase() {
        JavaType baseType = new TypeReference<Object>() {}.getType();
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDesserazlizerBase(baseType, idResolver, typePropertyName, _typeIdVisible, defaultImpl) {
            private static final long serialVersionUID = 1;
            private static final Map<String, JsonDeserializer<Object>> _deserializers = new ConcurrentHashMap<>(16, 0.75f, 4);
            private static final JsonDeserializer<Object> _defaultImplDeserializer = mock(JsonDeserializer.class);
        };
        return typeDeserializerBase;
    }

    private static final boolean _typeIdVisible = true;
    private static final JavaType _defaultImpl = new TypeReference<Object>() {}.getType();
    private static final String typePropertyName = "type";
}
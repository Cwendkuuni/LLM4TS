
package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

/**
 * Test class for TypeDeserializerBase.
 */
public class TypeDeserializerBaseTest {

    @Mock
    private TypeIdResolver typeIdResolver;

    @Mock
    private JavaType baseType;

    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        typeDeserializerBase = new TypeDeserializerBaseMock(baseType, typeIdResolver);
    }

    @Test
    public void testForProperty() {
        // Given
        BeanProperty prop = mock(BeanProperty.class);
        TypeDeserializer expectedTypeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializerBase.forProperty(prop)).thenReturn(expectedTypeDeserializer);

        // When
        TypeDeserializer actualTypeDeserializer = typeDeserializerBase.forProperty(prop);

        // Then
        assertEquals(expectedTypeDeserializer, actualTypeDeserializer);
    }

    @Test
    public void testGetTypeInclusion() {
        // Given
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseMock(baseType, typeIdResolver) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.WRAPPER_ARRAY;
            }
        };

        // When
        JsonTypeInfo.As actualInclude = typeDeserializerBase.getTypeInclusion();

        // Then
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, actualInclude);
    }

    @Test
    public void testBaseTypeName() {
        // Given
        Class<?> rawClass = String.class;
        when(baseType.getRawClass()).thenReturn(rawClass);

        // When
        String actualTypeName = typeDeserializerBase.baseTypeName();

        // Then
        assertEquals(rawClass.getName(), actualTypeName);
    }

    @Test
    public void testGetPropertyName() {
        // Given
        String expectedPropertyName = "type";
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseMock(baseType, typeIdResolver) {
            @Override
            public String getPropertyName() {
                return expectedPropertyName;
            }
        };

        // When
        String actualPropertyName = typeDeserializerBase.getPropertyName();

        // Then
        assertEquals(expectedPropertyName, actualPropertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        // When
        TypeIdResolver actualTypeIdResolver = typeDeserializerBase.getTypeIdResolver();

        // Then
        assertEquals(typeIdResolver, actualTypeIdResolver);
    }

    @Test
    public void testGetDefaultImpl() {
        // Given
        Class<?> rawClass = Object.class;
        JavaType defaultImpl = mock(JavaType.class);
        when(typeDeserializerBase.getDefaultImpl()).thenReturn(defaultImpl);
        when(defaultImpl.getRawClass()).thenReturn(rawClass);

        // When
        Class<?> actualDefaultImpl = typeDeserializerBase.getDefaultImpl();

        // Then
        assertEquals(rawClass, actualDefaultImpl);
    }

    @Test
    public void testToString() {
        // Given
        String expectedToString = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseMock;" +
                " base-type:mockJavaType; id-resolver: mockTypeIdResolver]";
        when(baseType.toString()).thenReturn("mockJavaType");
        when(typeIdResolver.toString()).thenReturn("mockTypeIdResolver");

        // When
        String actualToString = typeDeserializerBase.toString();

        // Then
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        // Given
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseMock(baseType, typeIdResolver) {
            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) {
                return "mockDeserializedObject";
            }
        };
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // When
        Object actualDeserializedObject = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);

        // Then
        assertEquals("mockDeserializedObject", actualDeserializedObject);
        verify(typeDeserializerBase).deserialize(jp, ctxt);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdWhenTypeIsNotFound() throws IOException {
        // Given
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseMock(baseType, typeIdResolver) {
            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
                throw new IOException("Type not found");
            }
        };
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // When
        typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        // Given
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseMock(baseType, typeIdResolver);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String typeId = "unknownTypeId";

        // When
        JsonDeserializer<Object> actualDeserializer = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, typeIdResolver, baseType);

        // Then
        assertNull(actualDeserializer);
        verify(ctxt).unknownTypeException(baseType, typeId, null);
    }

    private class TypeDeserializerBaseMock extends TypeDeserializerBase {

        protected TypeDeserializerBaseMock(JavaType baseType, TypeIdResolver idRes) {
            super(baseType, idRes, null, false, null);
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
            return null;
        }

        @Override
        protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
            return null;
        }

        @Override
        protected JsonDeserializer<Object> _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType) throws IOException {
            return null;
        }

        @Override
        public String getPropertyName() {
            return null;
        }

        @Override
        public Class<?> getDefaultImpl() {
            return null;
        }

        @Override
        protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
            return null;
        }
    }
}


package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;
    private BeanProperty beanPropertyMock;
    private DeserializationContext deserializationContextMock;
    private TypeIdResolver typeIdResolverMock;
    private JavaType baseTypeMock;
    private JavaType defaultImplMock;

    @Before
    public void setUp() {
        baseTypeMock = mock(JavaType.class);
        typeIdResolverMock = mock(TypeIdResolver.class);
        defaultImplMock = mock(JavaType.class);

        typeDeserializerBase = new TypeDeserializerBaseTestImpl(baseTypeMock, typeIdResolverMock, "typeProperty", true,
                defaultImplMock);
    }

    @Test
    public void testForProperty() {
        typeDeserializerBase.forProperty(beanPropertyMock);
        verify(baseTypeMock, times(1)).getClass();
    }

    @Test
    public void testGetTypeInclusion() {
        typeDeserializerBase.getTypeInclusion();
    }

    @Test
    public void testGetTypeIdResolver() {
        typeDeserializerBase.getTypeIdResolver();
    }

    @Test
    public void testGetDefaultImpl() {
        typeDeserializerBase.getDefaultImpl();
        verify(defaultImplMock, times(1)).getRawClass();
    }

    @Test
    public void testToString() {
        typeDeserializerBase.toString();
    }

    @Test
    public void testDeserializeWithNativeTypeId() {
        typeDeserializerBase._deserializeWithNativeTypeId(null, deserializationContextMock, "type");
        verify(typeIdResolverMock, times(1)).typeFromId(deserializationContextMock, "type");
    }

    @Test
    public void testDeserializeWithNativeTypeId_NullTypeId() {
        typeDeserializerBase._deserializeWithNativeTypeId(null, deserializationContextMock, null);
        verify(typeDeserializerBase, times(1))._findDefaultImplDeserializer(deserializationContextMock);
    }

    @Test
    public void testHandleUnknownTypeId() {
        typeDeserializerBase._handleUnknownTypeId(deserializationContextMock, "type", typeIdResolverMock, baseTypeMock);
    }

    class TypeDeserializerBaseTestImpl extends TypeDeserializerBase {

        public TypeDeserializerBaseTestImpl(JavaType baseType, TypeIdResolver idRes, String typePropertyName,
                boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }

    }
}
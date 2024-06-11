package com.fasterxml.jackson.databind.jsontype.impl;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializer;
    private DeserializationContext deserializationContext;
    private JavaType baseType;
    private TypeIdResolver idResolver;
    private String typePropertyName;
    private boolean typeIdVisible;
    private JavaType defaultImpl;
    private BeanProperty property;

    @Before
    public void setUp() {
        baseType = mock(JavaType.class);
        idResolver = mock(TypeIdResolver.class);
        typePropertyName = "type";
        typeIdVisible = true;
        defaultImpl = mock(JavaType.class);
        property = mock(BeanProperty.class);
        typeDeserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            protected JavaType _handleMissingTypeId(DeserializationContext ctxt, String extraDesc) throws IOException {
                return null;
            }
        };
        deserializationContext = mock(DeserializationContext.class);
    }

    @Test
    public void testBaseTypeName() {
        typeDeserializer.baseTypeName();
        verify(baseType).getRawClass();
        verifyNoMoreInteractions(baseType);
    }

    @Test
    public void testGetPropertyName() {
        typeDeserializer.getPropertyName();
        verifyNoMoreInteractions(baseType);
    }

    @Test
    public void testGetTypeIdResolver() {
        typeDeserializer.getTypeIdResolver();
        verifyNoMoreInteractions(baseType);
        verify(idResolver).typeFromId(deserializationContext, typeId);
        verifyNoMoreInteractions(idResolver);
    }

    @Test
    public void testGetDefaultImpl() {
        typeDeserializer.getDefaultImpl();
        verify(defaultImpl).getRawClass();
        verifyNoMoreInteractions(defaultImpl);
    }

    @Test
    public void testBaseType() {
        typeDeserializer.baseType();
        verifyNoMoreInteractions(baseType);
    }

    @Test
    public void testToString() {
        typeDeserializer.toString();
        verify(baseType).getRawClass();
        verifyNoMoreInteractions(baseType);
        verify(idResolver).toString();
        verifyNoMoreInteractions(idResolver);
    }

    @Test
    public void testHandleUnknownTypeId() {
        typeDeserializer._handleUnknownTypeId(deserializationContext, typeId);
        verify(idResolver).getDescForKnownTypeIds();
        verifyNoMoreInteractions(idResolver);
        verify(property).getName();
        verifyNoMoreInteractions(property);
        verify(deserializationContext).handleUnknownTypeId(baseType, typeId, idResolver, "type ids are not statically known");
        verifyNoMoreInteractions(deserializationContext);
    }

    @Test
    public void testHandleMissingTypeId() {
        typeDeserializer._handleMissingTypeId(deserializationContext, extraDesc);
        verify(idResolver).getDescForKnownTypeIds();
        verifyNoMoreInteractions(idResolver);
        verify(deserializationContext).handleMissingTypeId(baseType, idResolver, "known type ids = type ids are not statically known");
        verifyNoMoreInteractions(deserializationContext);
    }
}
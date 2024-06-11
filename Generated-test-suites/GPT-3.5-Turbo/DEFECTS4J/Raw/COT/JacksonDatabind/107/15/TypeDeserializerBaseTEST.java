package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TypeDeserializerBaseTest {

    @Mock
    private TypeIdResolver _idResolver;

    @Mock
    private JavaType _baseType;

    @Mock
    private BeanProperty _property;

    @Mock
    private JavaType _defaultImpl;

    private TypeDeserializerBase _typeDeserializerBase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        _typeDeserializerBase = new TypeDeserializerBaseStub(_baseType, _idResolver, "", false, _defaultImpl);
    }

    @Test
    public void testToString() {
        String expectedString = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseTest.TypeDeserializerBaseStub; base-type:Mock for JavaType; id-resolver: com.fasterxml.jackson.databind.jsontype.TypeIdResolver]";
        String result = _typeDeserializerBase.toString();
        Assert.assertEquals(expectedString, result);
    }

    private class TypeDeserializerBaseStub extends TypeDeserializerBase {

        public TypeDeserializerBaseStub(JavaType baseType, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return new TypeDeserializerBaseStub(_baseType, _idResolver, "", false, _defaultImpl);
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }

        @Override
        protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
            return null;
        }
    }
}
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.ObjectMapperUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TypeDeserializerBaseTest {
    private static final String DEFAULT_BASE_TYPE = "java.lang.String";
    private static final String DEFAULT_TYPE_PROPERTY_NAME = "type";

    private TypeDeserializerBase _typeDeserializer;
    private TypeIdResolver _idResolver;
    private DeserializationContext _ctxt;
    private JsonParser _parser;

    @Before
    public void setup() throws Exception {
        _idResolver = mock(TypeIdResolver.class);
        _ctxt = new TestContext(mock(DeserializationConfig.class));
        _parser = mock(JsonParser.class);
        when(_ctxt.getFactory()).thenReturn(ObjectMapperUtil.getTestObjectMapper());

        _typeDeserializer = new TypeDeserializerBase(
                TypeFactory.defaultInstance().constructType(DEFAULT_BASE_TYPE), _idResolver,
                DEFAULT_TYPE_PROPERTY_NAME, true, null) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return NullifyingDeserializer.instance;
            }

            @Override
            public String toString() {
                return "TypeDeserializerBaseTest";
            }
        };
    }

    @After
    public void teardown() throws Exception {
        _idResolver = null;
        _ctxt = null;
        _parser = null;
    }

    @Test
    public void forProperty_shouldReturnNewInstance() {
        _typeDeserializer._property = new BeanProperty("name", String.class);
        TypeDeserializer newInstance = _typeDeserializer.forProperty(_typeDeserializer._property);
        assertSame(_typeDeserializer.getClass(), newInstance.getClass());
    }

    @Test
    public void getTypeInclusion_shouldReturnAs_DESERIALIZED_PROPERTY_ONLY() {
        assertEquals(JsonTypeInfo.As.PROPERTY, _typeDeserializer.getTypeInclusion());
    }

    @Test
    public void getBaseTypeName_shouldReturnBaseTypeName() {
        assertEquals(DEFAULT_BASE_TYPE, _typeDeserializer.baseTypeName());
    }

    @Test
    public void getPropertyName_shouldReturnPropertyName() {
        assertEquals(DEFAULT_TYPE_PROPERTY_NAME, _typeDeserializer.getPropertyName());
    }

    @Test
    public void getTypeIdResolver_shouldReturnIdResolver() {
        assertEquals(_idResolver, _typeDeserializer.getTypeIdResolver());
    }

    @Test
    public void getDefaultImpl_shouldReturnDefaultImpl() {
        assertNull(_typeDeserializer.getDefaultImpl());
    }

    @Test
    public void toString_shouldReturnToStringRepresentation() throws IOException {
        assertEquals("TypeDeserializerBaseTest; base-type:java.lang.String; id-resolver: null]", _typeDeserializer.toString());
    }

    @Test
    public void _findDeserializer_shouldReturnDeserializer() throws IOException {
        String typeId = "typeId";
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);

        when(_idResolver.typeFromId(_ctxt, typeId)).thenReturn(TypeFactory.defaultInstance().constructType(String.class));
        when(_ctxt.findContextualValueDeserializer(TypeFactory.defaultInstance().constructType(String.class), _typeDeserializer._property)).thenReturn(deserializer);

        JsonDeserializer<Object> result = _typeDeserializer._findDeserializer(_ctxt, typeId);
        assertEquals(deserializer, result);
    }

    @Test
    public void _findDefaultImplDeserializer_shouldReturnDefaultImplDeserializer() throws IOException {
        JsonDeserializer<Object> defaultImplDeserializer = mock(JsonDeserializer.class);

        when(_typeDeserializer._defaultImplDeserializer).thenReturn(null);
        when(_typeDeserializer._ctxt.findContextualValueDeserializer(_typeDeserializer._defaultImpl, _typeDeserializer._property)).thenReturn(defaultImplDeserializer);

        JsonDeserializer<Object> result = _typeDeserializer._findDefaultImplDeserializer(_ctxt);
        assertSame(defaultImplDeserializer, result);
    }

    @Test
    public void _handleUnknownTypeId_shouldReturnNull() throws IOException {
        when(_ctxt.handleUnknownTypeId(_typeDeserializer._baseType, "typeId", _idResolver, null)).thenReturn(null);

        assertNull(_typeDeserializer._handleUnknownTypeId(_ctxt, "typeId", _idResolver, _typeDeserializer._baseType));
    }

    @Test
    public void _deserializeWithNativeTypeId_shouldThrowException() throws IOException {
        when(_typeDeserializer._deserializeWithNativeTypeId(_parser, _ctxt, null)).thenThrow(JsonMappingException.class);

        try {
            _typeDeserializer._deserializeWithNativeTypeId(_parser, _ctxt, null);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // Expected
        }
    }
}

class TestContext extends DeserializationContext {
    private DeserializationConfig _config;

    public TestContext(DeserializationConfig config) {
        super(config);
        _config = config;
    }

    @Override
    public DeserializationConfig getConfig() {
        return _config;
    }
}
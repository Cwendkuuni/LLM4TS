package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolutionContext;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class TypeDeserializerBaseTest {

    private static final String TYPE_ID = "testTypeId";

    private DeserializationContext context;
    private TypeIdResolver idResolver;
    private JavaType baseType;
    private BeanProperty property;
    private TypeDeserializerBase deserializer;

    @Before
    public void setUp() throws Exception {
        idResolver = mock(TypeIdResolver.class);
        context = mock(DeserializationContext.class);
        baseType = mock(JavaType.class);
        property = mock(BeanProperty.class);
        when(idResolver.typeFromId(context, TYPE_ID)).thenReturn(baseType);
        deserializer = new TypeDeserializerBase(baseType, idResolver, "", true, null) {
            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId)
                    throws IOException {
                return mock(JsonDeserializer.class);
            }

            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt)
                    throws IOException {
                return null;
            }

            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId)
                    throws IOException, JsonMappingException {
                return null;
            }

            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver,
                    JavaType baseType) throws IOException {
                return null;
            }
        };
    }

    @After
    public void tearDown() throws Exception {
        deserializer = null;
        context = null;
        idResolver = null;
        baseType = null;
        property = null;
    }

    @Test
    public void testForProperty() throws Exception {
        TypeDeserializerBase newDeserializer = deserializer.forProperty(property);
        assertNotNull(newDeserializer);
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        JsonTypeInfo.As inclusion = deserializer.getTypeInclusion();
        assertNotNull(inclusion);
    }

    @Test
    public void testBaseTypeName() throws Exception {
        String result = deserializer.baseTypeName();
        assertNotNull(result);
    }

    @Test
    public void testGetPropertyName() throws Exception {
        String result = deserializer.getPropertyName();
        assertNotNull(result);
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        TypeIdResolver result = deserializer.getTypeIdResolver();
        assertNotNull(result);
    }

    @Test
    public void testGetDefaultImpl() throws Exception {
        Class<?> result = deserializer.getDefaultImpl();
        assertNotNull(result);
    }

    @Test
    public void testToString() throws Exception {
        String result = deserializer.toString();
        assertNotNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindDeserializerNullTypeId() throws Exception {
        when(context.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        JsonDeserializer<Object> result = deserializer._findDeserializer(context, null);
        assertNotNull(result);
    }

    @Test
    public void testFindDeserializerTypeId() throws Exception {
        when(context.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(true);
        String typeId = "testTypeId";
        JsonDeserializer<Object> result = deserializer._findDeserializer(context, typeId);
        assertNotNull(result);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        when(context.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        JsonDeserializer<Object> result = deserializer._findDefaultImplDeserializer(context);
        assertNotNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        when(context.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        JsonParser parser = mock(JsonParser.class);
        Object result = deserializer._deserializeWithNativeTypeId(parser, context, TYPE_ID);
        assertNotNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNativeTypeIdNoTypeId() throws Exception {
        when(context.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        JsonParser parser = mock(JsonParser.class);
        Object result = deserializer._deserializeWithNativeTypeId(parser, context, null);
        assertNotNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNativeTypeIdInvalidTypeId() throws Exception {
        when(context.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(true);
        JsonParser parser = mock(JsonParser.class);
        Object result = deserializer._deserializeWithNativeTypeId(parser, context, "invalidTypeId");
        assertNotNull(result);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        when(context.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(true);
        TypeResolutionContext resolutionContext = mock(TypeResolutionContext.class);
        JavaType result = deserializer._handleUnknownTypeId(context, TYPE_ID, idResolver, baseType);
        assertNotNull(result);
    }

    @Test
    public void testHandleUnknownTypeIdNoIdResolver() throws Exception {
        when(context.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(true);
        TypeResolutionContext resolutionContext = mock(TypeResolutionContext.class);
        JavaType result = deserializer._handleUnknownTypeId(context, null, null, baseType);
        assertNotNull(result);
    }

    @Test
    public void testHandleUnknownTypeIdNoBaseType() throws Exception {
        when(context.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(true);
        TypeResolutionContext resolutionContext = mock(TypeResolutionContext.class);
        JavaType result = deserializer._handleUnknownTypeId(context, TYPE_ID, idResolver, null);
        assertNotNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeIdNullTypeId() throws Exception {
        when(context.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(true);
        JavaType result = deserializer._handleUnknownTypeId(context, null, idResolver, baseType);
        assertNotNull(result);
    }

    @Test
    public void testHandleUnknownTypeIdNoIdResolverOrBaseType() throws Exception {
        when(context.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(true);
        JavaType result = deserializer._handleUnknownTypeId(context, TYPE_ID, null, baseType);
        assertNotNull(result);
    }
}
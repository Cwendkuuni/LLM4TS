package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.TypeUtil;

public class TypeDeserializerBaseTest {

    private DeserializationContext ctxt;
    private TypeIdResolver idResolver;
    private JavaType baseType;
    private TypeDeserializerBase typeDeserializer;

    @Before
    public void setUp() throws Exception {
        idResolver = mock(TypeIdResolver.class);
        ctxt = mock(DeserializationContext.class);
        baseType = TypeFactory.defaultInstance().constructType(String.class);
        typeDeserializer = new TypeDeserializerBase(baseType, idResolver, "_typePropertyName", true, String.class) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };
    }

    @Test
    public void testBaseTypeName() throws Exception {
        assertNotNull(typeDeserializer.baseTypeName());
    }

    @Test
    public void testTypeIdResolver() throws Exception {
        assertNotNull(typeDeserializer.getTypeIdResolver());
    }

    @Test
    public void testDefaultImpl() throws Exception {
        assertNotNull(typeDeserializer.getDefaultImpl());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        String typeId = "typeId";
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(idResolver.typeFromId(ctxt, typeId)).thenReturn(mock(JavaType.class));
        when(ctxt.findContextualValueDeserializer(mock(JavaType.class), null)).thenReturn(deserializer);

        JsonDeserializer<Object> result = typeDeserializer._findDeserializer(ctxt, typeId);
        assertNotNull(result);
        assertEquals(deserializer, result);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializer_UnknownTypeId() throws Exception {
        String typeId = "unknownTypeId";
        when(idResolver.typeFromId(ctxt, typeId)).thenReturn(null);

        typeDeserializer._findDeserializer(ctxt, typeId);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        Class<?> defaultImpl = Object.class;
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(ctxt.findContextualValueDeserializer(baseType, null)).thenReturn(deserializer);

        JsonDeserializer<Object> result = typeDeserializer._findDefaultImplDeserializer(ctxt);
        assertNotNull(result);
        assertEquals(deserializer, result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        String json = "\"someString\"";
        ObjectReader objectReader = mock(ObjectReader.class);
        JsonParser jsonParser = mock(JsonParser.class);
        when(objectReader.readValueAsTree(new ByteArrayInputStream(json.getBytes()))).thenReturn(mock(JsonNode.class));
        when(jsonParser.getCodec()).thenReturn(mock(ObjectMapper.class));
        when(jsonParser.getObjectReader()).thenReturn(objectReader);
        when(jsonParser.getValueAsString()).thenReturn(json);
        when(jsonParser.getTypeId()).thenReturn("typeId");

        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(idResolver.typeFromId(ctxt, "typeId")).thenReturn(mock(JavaType.class));
        when(ctxt.findContextualValueDeserializer(mock(JavaType.class), null)).thenReturn(deserializer);

        Object result = typeDeserializer._deserializeWithNativeTypeId(jsonParser, ctxt);
        assertNotNull(result);
        assertEquals("someString", result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeId_UnknownTypeId() throws Exception {
        String json = "\"someString\"";
        ObjectReader objectReader = mock(ObjectReader.class);
        JsonParser jsonParser = mock(JsonParser.class);
        when(objectReader.readValueAsTree(new ByteArrayInputStream(json.getBytes()))).thenReturn(mock(JsonNode.class));
        when(jsonParser.getCodec()).thenReturn(mock(ObjectMapper.class));
        when(jsonParser.getObjectReader()).thenReturn(objectReader);
        when(jsonParser.getValueAsString()).thenReturn(json);
        when(jsonParser.getTypeId()).thenReturn("unknownTypeId");

        typeDeserializer._deserializeWithNativeTypeId(jsonParser, ctxt);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeId() throws Exception {
        String typeId = "unknownTypeId";
        when(idResolver.typeFromId(ctxt, typeId)).thenReturn(null);

        typeDeserializer._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
    }
}
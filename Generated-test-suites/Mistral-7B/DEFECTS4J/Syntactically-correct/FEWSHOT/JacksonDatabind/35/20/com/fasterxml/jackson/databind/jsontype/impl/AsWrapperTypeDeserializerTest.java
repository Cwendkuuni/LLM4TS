package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.test.BaseTest;

public class AsWrapperTypeDeserializerTest extends BaseTest {

    private static final String TYPE_ID = "MyTypeId";

    @Test
    public void testDeserializeTypedFromObjectWithNativeTypeId() throws IOException {
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("typeId", TYPE_ID);

        JsonParser jsonParser = objectMapper.treeAsTokens(jsonNode);
        TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
        when(typeIdResolver.readTypeId(jsonParser, null)).thenReturn(TYPE_ID);

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mock(JavaType.class), typeIdResolver, null, false, null);

        Object result = deserializer.deserializeTypedFromObject(jsonParser, mock(DeserializationContext.class));
        assertInstanceOf(JavaType.class, result);
    }

    @Test
    public void testDeserializeTypedFromObjectWithoutNativeTypeId() throws IOException {
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("typeId", null);

        JsonParser jsonParser = objectMapper.treeAsTokens(jsonNode);
        TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
        when(typeIdResolver.readTypeId(jsonParser, null)).thenReturn(null);

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mock(JavaType.class), typeIdResolver, null, false, null);

        Object result = deserializer.deserializeTypedFromObject(jsonParser, mock(DeserializationContext.class));
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("typeId", TYPE_ID);

        JsonParser jsonParser = objectMapper.treeAsTokens(jsonNode);

        TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
        when(typeIdResolver.readTypeId(jsonParser, null)).thenReturn(TYPE_ID);

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mock(JavaType.class), typeIdResolver, null, false, null);

        Object result = deserializer.deserializeTypedFromArray(jsonParser, mock(DeserializationContext.class));
        assertInstanceOf(JavaType.class, result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("typeId", TYPE_ID);

        JsonParser jsonParser = objectMapper.treeAsTokens(jsonNode);

        TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
        when(typeIdResolver.readTypeId(jsonParser, null)).thenReturn(TYPE_ID);

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mock(JavaType.class), typeIdResolver, null, false, null);

        Object result = deserializer.deserializeTypedFromScalar(jsonParser, mock(DeserializationContext.class));
        assertInstanceOf(JavaType.class, result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("typeId", TYPE_ID);

        JsonParser jsonParser = objectMapper.treeAsTokens(jsonNode);

        TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
        when(typeIdResolver.readTypeId(jsonParser, null)).thenReturn(TYPE_ID);

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mock(JavaType.class), typeIdResolver, null, false, null);

        Object result = deserializer.deserializeTypedFromAny(jsonParser, mock(DeserializationContext.class));
        assertInstanceOf(JavaType.class, result);
    }

    @Test
    public void testDeserializeWithMergedTypeId() throws IOException {
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("typeId", TYPE_ID);

        JsonParser jsonParser = objectMapper.treeAsTokens(jsonNode);

        TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
        when(typeIdResolver.readTypeId(jsonParser, null)).thenReturn(TYPE_ID);

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mock(JavaType.class), typeIdResolver, null, true, null);

        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(jsonParser, jsonParser);

        Object result = deserializer.deserializeTypedFromObject(jsonParserSequence, mock(DeserializationContext.class));
        assertInstanceOf(JavaType.class, result);
    }
}
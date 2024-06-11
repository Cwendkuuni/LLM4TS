package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.JsonParserSequence;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;

public class AsWrapperTypeDeserializerTest {
    private TypeDeserializer testee;
    private ObjectMapper objectMapper;
    private TypeIdResolver typeIdResolver;

    @Before
    public void setUp() {
        typeIdResolver = mock(TypeIdResolver.class);
        objectMapper = new ObjectMapper();
        objectMapper.setTypeIdResolver(typeIdResolver);
        testee = new AsWrapperTypeDeserializer(
                mock(JavaType.class), typeIdResolver, "_typePropertyName", true, Object.class);
    }

    @Test
    public void testDeserializeTypedFromObject_WithTypeId() throws Exception {
        String typeId = "com.test.TypeA";
        when(typeIdResolver.resolveTypeId(any(DeserializationContext.class), anyString(), any())).thenReturn(typeId);

        JsonParser parser = createMockJsonParser();
        Object result = testee.deserializeTypedFromObject(parser, createMockDeserializationContext());

        assertNotNull(result);
        verify(typeIdResolver).resolveTypeId(any(DeserializationContext.class), anyString(), any());
        verify(parser).nextToken();
        verify(parser).nextToken();
        verify(parser).getText();
        verify(parser).nextToken();
        verify(parser).nextToken();
    }

    @Test
    public void testDeserializeTypedFromObject_WithoutTypeId() throws Exception {
        JsonParser parser = createMockJsonParser();
        Object result = testee.deserializeTypedFromObject(parser, createMockDeserializationContext());

        assertNull(result);
        verify(parser, times(5)).nextToken();
    }

    @Test(expected = IOException.class)
    public void testDeserializeTypedFromObject_InvalidJson_MissingStartObject() throws Exception {
        JsonParser parser = createMockJsonParser();
        parser.nextToken(JsonToken.VALUE_STRING);

        testee.deserializeTypedFromObject(parser, createMockDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testDeserializeTypedFromObject_InvalidJson_MissingFieldName() throws Exception {
        JsonParser parser = createMockJsonParser();
        parser.nextToken(JsonToken.START_OBJECT);

        testee.deserializeTypedFromObject(parser, createMockDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testDeserializeTypedFromObject_InvalidJson_MissingTypeId() throws Exception {
        JsonParser parser = createMockJsonParser();
        parser.nextToken(JsonToken.START_OBJECT);
        parser.nextToken(JsonToken.FIELD_NAME);
        parser.nextToken(JsonToken.VALUE_STRING);
        parser.nextToken(JsonToken.START_OBJECT);

        testee.deserializeTypedFromObject(parser, createMockDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testDeserializeTypedFromObject_InvalidJson_MissingEndObject() throws Exception {
        JsonParser parser = createMockJsonParser();
        parser.nextToken(JsonToken.START_OBJECT);
        parser.nextToken(JsonToken.FIELD_NAME);
        parser.nextToken(JsonToken.VALUE_STRING);
        parser.nextToken(JsonToken.VALUE_NUMBER_INT);

        testee.deserializeTypedFromObject(parser, createMockDeserializationContext());
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        String typeId = "com.test.TypeA";
        when(typeIdResolver.resolveTypeId(any(DeserializationContext.class), anyString(), any())).thenReturn(typeId);

        JsonParser parser = createMockJsonParser();
        Object[] results = testee.deserializeTypedFromArray(parser, createMockDeserializationContext());

        assertNotNull(results);
        assertNotNull(results[0]);
        verify(typeIdResolver, times(1)).resolveTypeId(any(DeserializationContext.class), anyString(), any());
        verify(parser, times(1)).nextToken();
        verify(parser, times(3)).nextToken();
        verify(parser, times(1)).getText();
        verify(parser, times(1)).nextToken();
        verify(parser, times(1)).nextToken();
    }

    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        JsonParser parser = createMockJsonParser();

        Object result = testee.deserializeTypedFromScalar(parser, createMockDeserializationContext());

        assertNull(result);
        verify(parser, times(1)).nextToken();
    }

    @Test
    public void testDeserializeTypedFromAny() throws Exception {
        JsonParser parser = createMockJsonParser();

        Object result = testee.deserializeTypedFromAny(parser, createMockDeserializationContext());

        assertNull(result);
        verify(parser, times(1)).nextToken();
    }

    private JsonParser createMockJsonParser() throws IOException {
        return mock(JsonParser.class);
    }

    private DeserializationContext createMockDeserializationContext() {
        return mock(DeserializationContext.class);
    }
}
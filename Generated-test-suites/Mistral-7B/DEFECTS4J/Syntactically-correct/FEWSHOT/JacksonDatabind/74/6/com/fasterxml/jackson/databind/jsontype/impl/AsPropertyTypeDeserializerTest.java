package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ObjectMapperUtil;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;

public class AsPropertyTypeDeserializerTest {

    private ObjectMapper objectMapper;
    private JavaType javaType;
    private TypeIdResolver typeIdResolver;

    @Before
    public void setup() {
        this.objectMapper = new ObjectMapper();
        this.javaType = this.objectMapper.getTypeFactory().constructType(String.class);
        this.typeIdResolver = objectMapper.getTypeFactory();
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        String json = "{\"type\":\"StringType\",\"value\":\"TestString\"}";
        JsonParser jsonParser = this.objectMapper.getFactory().createParser(json);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                this.javaType, this.typeIdResolver, "type", true, null, As.PROPERTY);

        Object result = deserializer.deserializeTypedFromObject(jsonParser, this.objectMapper.getDeserializationContext());

        assertEquals("TestString", result);
    }

    @Test
    public void testDeserializeTypedFromObject_NoTypeProperty() throws IOException {
        String json = "{\"value\":\"TestString\"}";
        JsonParser jsonParser = this.objectMapper.getFactory().createParser(json);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                this.javaType, this.typeIdResolver, "type", true, null, As.PROPERTY);

        Object result = deserializer.deserializeTypedFromObject(jsonParser, this.objectMapper.getDeserializationContext());

        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObject_WrongTypeProperty() throws IOException {
        String json = "{\"invalidType\":\"TestString\",\"value\":\"TestString\"}";
        JsonParser jsonParser = this.objectMapper.getFactory().createParser(json);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                this.javaType, this.typeIdResolver, "type", true, null, As.PROPERTY);

        Object result = deserializer.deserializeTypedFromObject(jsonParser, this.objectMapper.getDeserializationContext());

        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObject_TokenBuffer() throws IOException {
        String json = "{\"type\":\"StringType\",\"value\":\"TestString\"}";
        JsonParser jsonParser = this.objectMapper.getFactory().createParser(json);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                this.javaType, this.typeIdResolver, "type", true, null, As.PROPERTY);

        Object result = deserializer.deserializeTypedFromObject(jsonParser, this.objectMapper.getDeserializationContext());

        assertEquals("TestString", result);

        JsonParser jsonParserWithTokenBuffer = this.objectMapper.getFactory().createParser(json);
        TokenBuffer tokenBuffer = new TokenBuffer(jsonParserWithTokenBuffer, this.objectMapper.getDeserializationContext());

        for (; jsonParserWithTokenBuffer.nextToken() != JsonToken.END_OBJECT; ) {
            tokenBuffer.nextToken();
        }

        deserializer = new AsPropertyTypeDeserializer(deserializer, null);

        JsonParser jsonParserWithNewDeserializer = JsonParserSequence.createFlattened(false, jsonParserWithTokenBuffer, deserializer);

        Object resultWithTokenBuffer = deserializer.deserializeTypedFromObject(jsonParserWithNewDeserializer, this.objectMapper.getDeserializationContext());

        assertEquals("TestString", resultWithTokenBuffer);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws IOException {
        String json = "[\"TestString\"]";
        JsonParser jsonParser = this.objectMapper.getFactory().createParser(json);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                this.javaType, this.typeIdResolver, "type", true, null, As.PROPERTY);

        Object result = deserializer.deserializeTypedUsingDefaultImpl(jsonParser, this.objectMapper.getDeserializationContext(), null);

        assertEquals("[TestString]", ObjectMapperUtil.writeValueAsString(result));
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_TokenBuffer() throws IOException {
        String json = "[\"TestString\"]";
        JsonParser jsonParser = this.objectMapper.getFactory().createParser(json);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                this.javaType, this.typeIdResolver, "type", true, null, As.PROPERTY);

        Object result = deserializer.deserializeTypedUsingDefaultImpl(jsonParser, this.objectMapper.getDeserializationContext(), null);

        assertEquals("[TestString]", ObjectMapperUtil.writeValueAsString(result));

        JsonParser jsonParserWithTokenBuffer = this.objectMapper.getFactory().createParser(json);
        TokenBuffer tokenBuffer = new TokenBuffer(jsonParserWithTokenBuffer, this.objectMapper.getDeserializationContext());

        for (; jsonParserWithTokenBuffer.nextToken() != JsonToken.END_ARRAY; ) {
            tokenBuffer.nextToken();
        }

        deserializer = new AsPropertyTypeDeserializer(deserializer, null);

        JsonParser jsonParserWithNewDeserializer = JsonParserSequence.createFlattened(false, jsonParserWithTokenBuffer, deserializer);

        Object resultWithTokenBuffer = deserializer.deserializeTypedUsingDefaultImpl(jsonParserWithNewDeserializer, this.objectMapper.getDeserializationContext(), null);

        assertEquals("[TestString]", ObjectMapperUtil.writeValueAsString(resultWithTokenBuffer));
    }

    @Test
    public void testDeserializeTypedForId() throws IOException {
        String json = "{\"type\":\"StringType\",\"value\":\"TestString\"}";
        JsonParser jsonParser = this.objectMapper.getFactory().createParser(json);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                this.javaType, this.typeIdResolver, "type", true, null, As.PROPERTY);

        Object result = deserializer.deserializeTypedForId(jsonParser, this.objectMapper.getDeserializationContext(), null);

        assertEquals("TestString", result);

        JsonParser jsonParserWithTokenBuffer = this.objectMapper.getFactory().createParser(json);
        TokenBuffer tokenBuffer = new TokenBuffer(jsonParserWithTokenBuffer, this.objectMapper.getDeserializationContext());

        for (; jsonParserWithTokenBuffer.nextToken() != JsonToken.END_OBJECT; ) {
            tokenBuffer.nextToken();
        }

        deserializer = new AsPropertyTypeDeserializer(deserializer, null);

        JsonParser jsonParserWithNewDeserializer = JsonParserSequence.createFlattened(false, jsonParserWithTokenBuffer, deserializer);

        Object resultWithTokenBuffer = deserializer.deserializeTypedForId(jsonParserWithNewDeserializer, this.objectMapper.getDeserializationContext(), tokenBuffer);

        assertEquals("TestString", resultWithTokenBuffer);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_NoDefaultImpl() throws IOException {
        String json = "[\"TestString\"]";
        JsonParser jsonParser = this.objectMapper.getFactory().createParser(json);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                this.javaType, this.typeIdResolver, "type", true, null, As.PROPERTY);

        Object result = deserializer.deserializeTypedUsingDefaultImpl(jsonParser, this.objectMapper.getDeserializationContext(), null);

        assertNull(result);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_TokenBuffer() throws IOException {
        String json = "[\"TestString\"]";
        JsonParser jsonParser = this.objectMapper.getFactory().createParser(json);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                this.javaType, this.typeIdResolver, "type", true, null, As.PROPERTY);

        Object result = deserializer.deserializeTypedUsingDefaultImpl(jsonParser, this.objectMapper.getDeserializationContext(), null);

        assertNull(result);

        JsonParser jsonParserWithTokenBuffer = this.objectMapper.getFactory().createParser(json);
        TokenBuffer tokenBuffer = new TokenBuffer(jsonParserWithTokenBuffer, this.objectMapper.getDeserializationContext());

        for (; jsonParserWithTokenBuffer.nextToken() != JsonToken.END_ARRAY; ) {
            tokenBuffer.nextToken();
        }

        deserializer = new AsPropertyTypeDeserializer(deserializer, null);

        JsonParser jsonParserWithNewDeserializer = JsonParserSequence.createFlattened(false, jsonParserWithTokenBuffer, deserializer);

        Object resultWithTokenBuffer = deserializer.deserializeTypedUsingDefaultImpl(jsonParserWithNewDeserializer, this.objectMapper.getDeserializationContext(), null);

        assertNull(resultWithTokenBuffer);
    }

    @Test
    public void testSerializeDeserializedObject() throws IOException {
        String jsonInput = "{\"type\":\"StringType\",\"value\":\"TestString\"}";
        Object deserializedObject = this.objectMapper.readValue(jsonInput, AsPropertyTypeDeserializer.class);

        String jsonOutput = this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(deserializedObject);

        assertEquals(jsonInput, jsonOutput);
    }

    @Test
    public void testSerializeDeserializedObject_WithNullTokenBuffer() throws IOException {
        String jsonInput = "{\"type\":\"StringType\",\"value\":\"TestString\"}";
        Object deserializedObject = this.objectMapper.readValue(jsonInput, AsPropertyTypeDeserializer.class);

        String jsonOutput = this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(deserializedObject);

        assertEquals(jsonInput, jsonOutput);

        JsonParser jsonParser = this.objectMapper.getFactory().createParser(jsonInput);
        TokenBuffer tokenBuffer = new TokenBuffer(jsonParser, this.objectMapper.getDeserializationContext());

        for (; jsonParser.nextToken() != JsonToken.END_OBJECT; ) {
            tokenBuffer.nextToken();
        }

        deserializedObject = this.objectMapper.readerWithTypeIdResolver(this.typeIdResolver).readTree(tokenBuffer);

        jsonOutput = this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(deserializedObject);

        assertEquals(jsonInput, jsonOutput);
    }
}
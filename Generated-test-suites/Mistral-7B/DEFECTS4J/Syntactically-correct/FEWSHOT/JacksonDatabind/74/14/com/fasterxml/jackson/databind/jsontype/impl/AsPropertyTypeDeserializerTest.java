package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Test;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(Object.class),
                mapper.getTypeIdResolver(), "type", true,
                mapper.getTypeFactory().constructType(String.class), As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser("{\"type\":\"String\"}");
        JsonDeserializer<Object> deser = mapper.findValueDeserializer(Object.class, parser, null);
        Object result = deserializer.deserializeTypedFromObject(parser, parser.getDeserializationContext());

        assertEquals(String.class, result.getClass());

        parser.nextToken(); // consume next token to move to the end of the JSON object

        result = deserializer.deserializeTypedFromObject(parser, parser.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObjectWithNativeTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(Object.class),
                mapper.getTypeIdResolver(), "type", true,
                mapper.getTypeFactory().constructType(String.class), As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser("{\"type\":\"String\", \"data\":\"foo\"}");
        Object result = deserializer.deserializeTypedFromObject(parser, parser.getDeserializationContext());

        assertEquals("foo", result);
    }

    @Test
    public void testDeserializeTypedFromObjectWithWrongJsonStructure() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(Object.class),
                mapper.getTypeIdResolver(), "type", true,
                mapper.getTypeFactory().constructType(String.class), As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser("{\"data\":\"foo\"}");

        try {
            deserializer.deserializeTypedFromObject(parser, parser.getDeserializationContext());
            fail();
        } catch (JsonProcessingException e) {
            //Expected
        }
    }

    @Test
    public void testDeserializeTypedFromObjectWithNullJsonParser() {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                null, null, null, true, null, As.PROPERTY);

        assertNull(deserializer.deserializeTypedFromObject(null, null));
    }

    @Test
    public void testDeserializeTypedForId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(Object.class),
                mapper.getTypeIdResolver(), "type", true,
                mapper.getTypeFactory().constructType(String.class), As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser("{\"type\":\"String\"}");

        Object result = deserializer._deserializeTypedForId(parser, parser.getDeserializationContext(), null);

        assertEquals(String.class, result.getClass());

        parser = mapper.getFactory().createParser("{\"type\":\"String\", \"data\":\"foo\"}");
        TokenBuffer tokenBuffer = new TokenBuffer(parser, parser.getDeserializationContext());
        result = deserializer._deserializeTypedForId(parser, parser.getDeserializationContext(), tokenBuffer);

        assertEquals("foo", result);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(Object.class),
                mapper.getTypeIdResolver(), "type", true,
                mapper.getTypeFactory().constructType(String.class), As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser("{\"data\":\"foo\"}");

        Object result = deserializer._deserializeTypedUsingDefaultImpl(parser, parser.getDeserializationContext(), null);
        assertNull(result);

        result = deserializer._deserializeTypedUsingDefaultImpl(parser, parser.getDeserializationContext(), new TokenBuffer(parser, parser.getDeserializationContext()));
        assertNull(result);

        JsonDeserializer<Object> defaultImplDeserializer = mapper.findValueDeserializer(Object.class, parser, null);
        result = deserializer._deserializeTypedUsingDefaultImpl(parser, parser.getDeserializationContext(), new TokenBuffer(parser, parser.getDeserializationContext()));
        assertInstanceOf(defaultImplDeserializer.getClass(), result.getClass());
    }

    @Test
    public void testDeserializeTypedUsingDefaultImplWithNullJsonParser() {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                null, null, null, true, null, As.PROPERTY);

        assertNull(deserializer._deserializeTypedUsingDefaultImpl(null, null, null));
    }
}
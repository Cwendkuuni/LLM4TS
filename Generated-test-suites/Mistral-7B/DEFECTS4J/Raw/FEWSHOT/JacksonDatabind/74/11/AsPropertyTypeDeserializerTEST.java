package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        TypeIdResolver idResolver = mapper.getPolymorphicTypeIdResolver();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);

        // Test with null input
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, idResolver, "type", false, null, As.PROPERTY);
        JsonParser parser = factory.createParser("{}");
        assertNull(deserializer.deserializeTypedFromObject(parser, new TestContext()));

        // Test with empty JSON object
        parser = factory.createParser("{}");
        String expectedType = "String";
        parser.nextToken(); // Move to the next token
        parser.nextToken(); // Move to the next token
        Object actualType = deserializer.deserializeTypedFromObject(parser, new TestContext());
        assertEquals(expectedType, actualType.getClass().getName());

        // Test with JSON object containing type property
        String jsonWithTypeProperty = "{\"type\":\"String\"}";
        parser = factory.createParser(jsonWithTypeProperty);
        expectedType = "String";
        Object actualTypeFromObject = deserializer.deserializeTypedFromObject(parser, new TestContext());
        assertEquals(expectedType, actualTypeFromObject.getClass().getName());

        // Test with JSON object containing token buffer
        String jsonWithTokenBuffer = "{\"type\":\"String\"}";
        parser = factory.createParser(jsonWithTypeProperty);
        TokenBuffer tokenBuffer = new TokenBuffer(parser, new TestContext());
        parser.nextToken(); // Move to the next token (skip START_OBJECT)
        parser.nextToken(); // Move to the next token (skip FIELD_NAME)
        String actualTypeFromTokenBuffer = deserializer.deserializeTypedForId(parser, new TestContext(), tokenBuffer).getClass().getName();
        assertEquals(expectedType, actualTypeFromTokenBuffer);

        // Test with JSON array
        String jsonWithArray = "[\"String\"]";
        parser = factory.createParser(jsonWithArray);
        assertNull(deserializer.deserializeTypedFromObject(parser, new TestContext()));

        // Test with JSON object without type property
        String jsonWithoutTypeProperty = "{}";
        parser = factory.createParser(jsonWithoutTypeProperty);
        assertNull(deserializer.deserializeTypedFromObject(parser, new TestContext()));

        // Test with JSON object with invalid type property name
        String jsonWithInvalidTypePropertyName = "{\"invalidTypePropertyName\":\"String\"}";
        parser = factory.createParser(jsonWithInvalidTypePropertyName);
        assertNull(deserializer.deserializeTypedFromObject(parser, new TestContext()));
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        TypeIdResolver idResolver = mapper.getPolymorphicTypeIdResolver();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);

        // Test with null input
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, idResolver, "type", false, null, As.PROPERTY);
        JsonParser parser = factory.createParser("{}");
        assertNull(deserializer.deserializeTypedUsingDefaultImpl(parser, new TestContext(), null));

        // Test with empty JSON object
        parser = factory.createParser("{}");
        String expectedType = "String";
        parser.nextToken(); // Move to the next token
        parser.nextToken(); // Move to the next token
        Object actualTypeFromObject = deserializer.deserializeTypedUsingDefaultImpl(parser, new TestContext(), null);
        assertEquals(expectedType, actualTypeFromObject.getClass().getName());

        // Test with JSON object containing type property
        String jsonWithTypeProperty = "{\"type\":\"String\"}";
        parser = factory.createParser(jsonWithTypeProperty);
        expectedType = "String";
        Object actualTypeFromObjectWithTypeProperty = deserializer.deserializeTypedUsingDefaultImpl(parser, new TestContext(), null);
        assertEquals(expectedType, actualTypeFromObjectWithTypeProperty.getClass().getName());

        // Test with JSON object containing token buffer
        String jsonWithTokenBuffer = "{\"type\":\"String\"}";
        parser = factory.createParser(jsonWithTypeProperty);
        TokenBuffer tokenBuffer = new TokenBuffer(parser, new TestContext());
        parser.nextToken(); // Move to the next token (skip START_OBJECT)
        parser.nextToken(); // Move to the next token (skip FIELD_NAME)
        String actualTypeFromTokenBufferWithDefaultImpl = deserializer.deserializeTypedUsingDefaultImpl(parser, new TestContext(), tokenBuffer).getClass().getName();
        assertEquals(expectedType, actualTypeFromTokenBufferWithDefaultImpl);

        // Test with JSON array
        String jsonWithArray = "[\"String\"]";
        parser = factory.createParser(jsonWithArray);
        Object actualTypeFromArrayWithDefaultImpl = deserializer.deserializeTypedUsingDefaultImpl(parser, new TestContext(), null);
        assertEquals(javaType.getRawClass(), actualTypeFromArrayWithDefaultImpl.getClass());

        // Test with JSON object without type property
        String jsonWithoutTypeProperty = "{}";
        parser = factory.createParser(jsonWithoutTypeProperty);
        Object actualTypeFromObjectWithoutTypeProperty = deserializer.deserializeTypedUsingDefaultImpl(parser, new TestContext(), null);
        assertEquals(javaType.getRawClass(), actualTypeFromObjectWithoutTypeProperty.getClass());

        // Test with JSON object with invalid type property name
        String jsonWithInvalidTypePropertyName = "{\"invalidTypePropertyName\":\"String\"}";
        parser = factory.createParser(jsonWithInvalidTypePropertyName);
        Object actualTypeFromObjectWithInvalidTypePropertyName = deserializer.deserializeTypedUsingDefaultImpl(parser, new TestContext(), null);
        assertEquals(javaType.getRawClass(), actualTypeFromObjectWithInvalidTypePropertyName.getClass());
    }
}

class TestContext extends DeserializationContext {
    @Override
    public final boolean isEmptyTokenBuffer() {
        return false;
    }
}
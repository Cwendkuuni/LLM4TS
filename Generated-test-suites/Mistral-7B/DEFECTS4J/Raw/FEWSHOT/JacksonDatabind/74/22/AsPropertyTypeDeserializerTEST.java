package com.fasterxml.jackson.databind.jsontype.impl;

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

/**
 * Test cases for {@link AsPropertyTypeDeserializer} class
 */
public class AsPropertyTypeDeserializerTest {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final TypeFactory typeFactory = mapper.getTypeFactory();

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        JavaType javaType = typeFactory.constructType(String.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, idResolver, "type", false, null, As.PROPERTY);

        // Test with JSON Object
        String jsonObject = "{\"type\": \"String\"}";
        JsonParser parser = mapper.getFactory().createParser(jsonObject);
        assertEquals("String", deserializer.deserializeTypedFromObject(parser, new TestContext()).getClass().getName());

        // Test with null input
        parser.close();
        parser = mapper.getFactory().createParser(null);
        assertNull(deserializer.deserializeTypedFromObject(parser, new TestContext()));

        // Test with invalid JSON input
        parser.nextToken();
        deserializer.deserializeTypedFromObject(parser, new TestContext());
    }

    @Test
    public void testDeserializeTypedFromObjectWithDefaultImpl() throws IOException {
        JavaType javaType = typeFactory.constructType(String.class);
        JavaType defaultImplType = typeFactory.constructType(Integer.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, idResolver, "type", false, defaultImplType, As.PROPERTY);

        // Test with JSON Object with type id
        String jsonObject = "{\"type\": \"String\"}";
        JsonParser parser = mapper.getFactory().createParser(jsonObject);
        assertEquals("String", deserializer.deserializeTypedFromObject(parser, new TestContext()).getClass().getName());

        // Test with JSON Object without type id
        jsonObject = "{\"type\": \"Integer\"}";
        parser = mapper.getFactory().createParser(jsonObject);
        assertEquals(Integer.class, deserializer.deserializeTypedFromObject(parser, new TestContext()).getClass());

        // Test with JSON Array
        String jsonArray = "[\"String\"]";
        parser = mapper.getFactory().createParser(jsonArray);
        assertEquals("String", deserializer.deserializeTypedFromObject(parser, new TestContext()).getClass().getName());

        // Test with null input
        parser.close();
        parser = mapper.getFactory().createParser(null);
        assertNull(deserializer.deserializeTypedFromObject(parser, new TestContext()));

        // Test with invalid JSON input
        parser.nextToken();
        deserializer.deserializeTypedFromObject(parser, new TestContext());
    }

    @Test
    public void testDeserializeTypedFromObjectWithNativeTypeId() throws IOException {
        JavaType javaType = typeFactory.constructType(String.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, idResolver, "type", false, null, As.PROPERTY);

        // Test with JSON Object with native type id
        String jsonObject = "{\"@type\": \"java.lang.String\"}";
        JsonParser parser = mapper.getFactory().createParser(jsonObject);
        assertEquals("String", deserializer.deserializeTypedFromObject(parser, new TestContext()).getClass().getName());

        // Test with JSON Object without type id
        String jsonNoTypeId = "{\"type\": \"String\"}";
        parser = mapper.getFactory().createParser(jsonNoTypeId);
        assertNull(deserializer.deserializeTypedFromObject(parser, new TestContext()));

        // Test with JSON Array
        String jsonArray = "[\"String\"]";
        parser = mapper.getFactory().createParser(jsonArray);
        assertEquals("String", deserializer.deserializeTypedFromObject(parser, new TestContext()).getClass().getName());

        // Test with null input
        parser.close();
        parser = mapper.getFactory().createParser(null);
        assertNull(deserializer.deserializeTypedFromObject(parser, new TestContext()));

        // Test with invalid JSON input
        parser.nextToken();
        deserializer.deserializeTypedFromObject(parser, new TestContext());
    }

    @Test
    public void testDeserializeWithUnknownTypeId() throws IOException {
        JavaType javaType = typeFactory.constructType(String.class);
        JavaType defaultImplType = typeFactory.constructType(Integer.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, idResolver, "type", false, defaultImplType, As.PROPERTY);

        // Test with JSON Object with unknown type id
        String jsonObject = "{\"type\": \"UnknownType\"}";
        JsonParser parser = mapper.getFactory().createParser(jsonObject);
        assertEquals(deserializer.getDefaultImplType().getClass(), deserializer.deserializeTypedFromObject(parser, new TestContext()).getClass());

        // Test with JSON Object without type id
        String jsonNoTypeId = "{\"type\": null}";
        parser = mapper.getFactory().createParser(jsonNoTypeId);
        assertNull(deserializer.deserializeTypedFromObject(parser, new TestContext()));

        // Test with JSON Array
        String jsonArray = "[\"UnknownType\"]";
        parser = mapper.getFactory().createParser(jsonArray);
        assertEquals(deserializer.getDefaultImplType().getClass(), deserializer.deserializeTypedFromObject(parser, new TestContext()).getClass());

        // Test with null input
        parser.close();
        parser = mapper.getFactory().createParser(null);
        assertNull(deserializer.deserializeTypedFromObject(parser, new TestContext()));

        // Test with invalid JSON input
        parser.nextToken();
        deserializer.deserializeTypedFromObject(parser, new TestContext());
    }

    /**
     * Test context for deserialization tests
     */
    static class TestContext extends DeserializationContext {
        public TestContext() {
            super(new AnnotationIntrospector(), new StandardDeserializationProblemHandler(), new JacksonAnnotationIntrospector());
        }
    }
}
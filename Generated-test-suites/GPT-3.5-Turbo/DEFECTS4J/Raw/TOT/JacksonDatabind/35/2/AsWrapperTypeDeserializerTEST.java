package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.*;
import static org.junit.Assert.*;

public class AsWrapperTypeDeserializerTest {

    private AsWrapperTypeDeserializer deserializer;

    @Before
    public void setUp() {
        deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
    }

    @Test
    public void testDeserializeTypedFromObject() {
        // typical use case
        // prepare input JSON
        String inputJson = "{\"type\":\"wrapper\", \"data\":\"test data\"}";

        // create a parser
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(inputJson);

        // set up DeserializationContext
        DeserializationContext context = new DeserializationContext(null, null);

        // call the method to be tested
        Object result = deserializer.deserializeTypedFromObject(parser, context);

        // verify the result
        assertNotNull(result);
        assertEquals("test data", result);
    }

    @Test
    public void testDeserializeTypedFromArray() {
        // typical use case
        // prepare input JSON
        String inputJson = "[\"type\", \"data\"]";

        // create a parser
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(inputJson);

        // set up DeserializationContext
        DeserializationContext context = new DeserializationContext(null, null);

        // call the method to be tested
        Object result = deserializer.deserializeTypedFromArray(parser, context);

        // verify the result
        assertNotNull(result);
        assertEquals("data", result);
    }

    @Test
    public void testDeserializeTypedFromScalar() {
        // typical use case
        // prepare input JSON
        String inputJson = "data";

        // create a parser
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(inputJson);

        // set up DeserializationContext
        DeserializationContext context = new DeserializationContext(null, null);

        // call the method to be tested
        Object result = deserializer.deserializeTypedFromScalar(parser, context);

        // verify the result
        assertNotNull(result);
        assertEquals("data", result);
    }

    @Test
    public void testDeserializeTypedFromAny() {
        // typical use case
        // prepare input JSON
        String inputJson = "{\"type\":\"wrapper\", \"data\":\"test data\"}";

        // create a parser
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(inputJson);

        // set up DeserializationContext
        DeserializationContext context = new DeserializationContext(null, null);

        // call the method to be tested
        Object result = deserializer.deserializeTypedFromAny(parser, context);

        // verify the result
        assertNotNull(result);
        assertEquals("test data", result);
    }

    @Test(expected = Exception.class)
    public void testDeserializeTypedFromObjectWithInvalidJson() {
        // edge case: invalid JSON input

        // prepare input JSON
        String inputJson = "{\"type\":\"wrapper\", \"data\":\"test data\"";

        // create a parser
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(inputJson);

        // set up DeserializationContext
        DeserializationContext context = new DeserializationContext(null, null);

        // call the method to be tested
        deserializer.deserializeTypedFromAny(parser, context);
    }

    @Test(expected = Exception.class)
    public void testDeserializeTypedFromArrayWithInvalidJson() {
        // edge case: invalid JSON input

        // prepare input JSON
        String inputJson = "[\"type\", \"data\"";

        // create a parser
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(inputJson);

        // set up DeserializationContext
        DeserializationContext context = new DeserializationContext(null, null);

        // call the method to be tested
        deserializer.deserializeTypedFromAny(parser, context);
    }

    @Test(expected = Exception.class)
    public void testDeserializeTypedFromScalarWithInvalidJson() {
        // edge case: invalid JSON input

        // prepare input JSON
        String inputJson = "data";

        // create a parser
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(inputJson);

        // set up DeserializationContext
        DeserializationContext context = new DeserializationContext(null, null);

        // call the method to be tested
        deserializer.deserializeTypedFromAny(parser, context);
    }

    @Test(expected = Exception.class)
    public void testDeserializeTypedFromAnyWithInvalidJson() {
        // edge case: invalid JSON input

        // prepare input JSON
        String inputJson = "{\"type\":\"wrapper\", \"data\":\"test data\"";

        // create a parser
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(inputJson);

        // set up DeserializationContext
        DeserializationContext context = new DeserializationContext(null, null);

        // call the method to be tested
        deserializer.deserializeTypedFromAny(parser, context);
    }

}
package com.fasterxml.jackson.databind.deser.std;

import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {

    // Test Method 1: Testing deserialize() method when input is a valid JSON array of strings
    @Test
    public void testDeserialize_ValidArray() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jsonParser = new JsonFactory().createParser("[\"string1\", \"string2\", \"string3\"]");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        String[] result = deserializer.deserialize(jsonParser, ctxt);

        Assert.assertArrayEquals(new String[] {"string1", "string2", "string3"}, result);
    }

    // Test Method 2: Testing deserialize() method when input is an empty JSON array
    @Test
    public void testDeserialize_EmptyArray() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jsonParser = new JsonFactory().createParser("[]");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        String[] result = deserializer.deserialize(jsonParser, ctxt);

        Assert.assertArrayEquals(new String[] {}, result);
    }

    // Test Method 3: Testing deserialize() method when input is a JSON array with null values
    @Test
    public void testDeserialize_ArrayWithNullValues() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jsonParser = new JsonFactory().createParser("[null, null, null]");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        String[] result = deserializer.deserialize(jsonParser, ctxt);

        Assert.assertArrayEquals(new String[] {null, null, null}, result);
    }

    // Test Method 4: Testing deserialize() method when input is a valid JSON array with a mix of strings and null values
    @Test
    public void testDeserialize_ArrayWithMixedValues() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jsonParser = new JsonFactory().createParser("[null, \"string1\", null, \"string2\"]");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        String[] result = deserializer.deserialize(jsonParser, ctxt);

        Assert.assertArrayEquals(new String[] {null, "string1", null, "string2"}, result);
    }

    // Test Method 5: Testing deserialize() method when input is not a JSON array
    @Test(expected = IOException.class)
    public void testDeserialize_NonArrayInput() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jsonParser = new JsonFactory().createParser("{\"key\": \"value\"}");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        deserializer.deserialize(jsonParser, ctxt);
    }

    // Test Method 6: Testing deserialize() method when there is an invalid string value in JSON array
    @Test(expected = IOException.class)
    public void testDeserialize_InvalidStringValue() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jsonParser = new JsonFactory().createParser("[\"string1\", 123, \"string2\"]");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        deserializer.deserialize(jsonParser, ctxt);
    }

    // Test Method 7: Testing deserializeWithType() method
    @Test
    public void testDeserializeWithType() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jsonParser = new JsonFactory().createParser("[\"string1\", \"string2\", \"string3\"]");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        TypeDeserializer typeDeserializer = null;
        Object result = deserializer.deserializeWithType(jsonParser, ctxt, typeDeserializer);

        Assert.assertArrayEquals(new String[] {"string1", "string2", "string3"}, (String[])result);
    }

    // Test Method 8: Testing handleNonArray() method when JSON input is not an array and ACCEPT_SINGLE_VALUE_AS_ARRAY is disabled
    @Test(expected = JsonMappingException.class)
    public void testHandleNonArray_DisabledAcceptSingleValueAsArray() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jsonParser = new JsonFactory().createParser("\"string\"");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        deserializer.handleNonArray(jsonParser, ctxt);
    }

    // Test Method 9: Testing handleNonArray() method when JSON input is not an array and ACCEPT_SINGLE_VALUE_AS_ARRAY is enabled
    @Test
    public void testHandleNonArray_EnabledAcceptSingleValueAsArray() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jsonParser = new JsonFactory().createParser("\"string\"");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        ctxt.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        String[] result = deserializer.handleNonArray(jsonParser, ctxt);

        Assert.assertArrayEquals(new String[] {"string"}, result);
    }

    // Test Method 10: Testing createContextual() method when _elementDeserializer is null
    @Test
    public void testCreateContextual_NullElementDeserializer() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        BeanProperty property = null;
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);

        Assert.assertNull(result);
    }

    // Test Method 11: Testing createContextual() method when _elementDeserializer is not null
    @Test
    public void testCreateContextual_ElementDeserializerNotNull() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        BeanProperty property = null;
        JsonDeserializer<?> deser = new JsonDeserializer<>() {
            @Override
            public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
                return null;
            }
        };
        deserializer._elementDeserializer = (JsonDeserializer<String>) deser;
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);

        Assert.assertEquals(deser, result);
    }

    // Test Method 12: Testing createContextual() method when _elementDeserializer is the default deserializer
    @Test
    public void testCreateContextual_SameElementDeserializer() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        BeanProperty property = null;
        deserializer._elementDeserializer = new StdDeserializer<>(String.class);
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);

        Assert.assertEquals(deserializer, result);
    }

    // Test Method 13: Testing createContextual() method when _elementDeserializer is not the default deserializer
    @Test
    public void testCreateContextual_DifferentElementDeserializer() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        BeanProperty property = null;
        deserializer._elementDeserializer = new JsonDeserializer<>() {
            @Override
            public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
                return null;
            }
        };
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);

        Assert.assertNotEquals(deserializer, result);
    }

}
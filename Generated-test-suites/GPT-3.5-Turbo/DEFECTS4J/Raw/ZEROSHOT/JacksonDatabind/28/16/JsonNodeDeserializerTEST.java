package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.RawValue;

import org.junit.Test;
import org.junit.Assert;

public class JsonNodeDeserializerTest {

    @Test
    public void testGetNullValue() {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = null;
        JsonNode result = jsonNodeDeserializer.getNullValue(ctxt);
        Assert.assertEquals(NullNode.getInstance(), result);
    }

    @Test
    public void testGetNullValueDeprecated() {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode result = jsonNodeDeserializer.getNullValue();
        Assert.assertEquals(NullNode.getInstance(), result);
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonParser p = null;
        DeserializationContext ctxt = null;
        JsonNode result = jsonNodeDeserializer.deserialize(p, ctxt);
        Assert.assertNotNull(result);
    }

    @Test
    public void testObjectDeserializerDeserialize() throws IOException {
        JsonNodeDeserializer.ObjectDeserializer objectDeserializer = new JsonNodeDeserializer.ObjectDeserializer();
        JsonParser p = null;
        DeserializationContext ctxt = null;
        ObjectNode result = objectDeserializer.deserialize(p, ctxt);
        Assert.assertNotNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testObjectDeserializerDeserializeException() throws IOException {
        JsonNodeDeserializer.ObjectDeserializer objectDeserializer = new JsonNodeDeserializer.ObjectDeserializer();
        JsonParser p = null;
        DeserializationContext ctxt = null;
        objectDeserializer.deserialize(p, ctxt);
    }

    @Test
    public void testArrayDeserializerDeserialize() throws IOException {
        JsonNodeDeserializer.ArrayDeserializer arrayDeserializer = new JsonNodeDeserializer.ArrayDeserializer();
        JsonParser p = null;
        DeserializationContext ctxt = null;
        ArrayNode result = arrayDeserializer.deserialize(p, ctxt);
        Assert.assertNotNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testArrayDeserializerDeserializeException() throws IOException {
        JsonNodeDeserializer.ArrayDeserializer arrayDeserializer = new JsonNodeDeserializer.ArrayDeserializer();
        JsonParser p = null;
        DeserializationContext ctxt = null;
        arrayDeserializer.deserialize(p, ctxt);
    }
}
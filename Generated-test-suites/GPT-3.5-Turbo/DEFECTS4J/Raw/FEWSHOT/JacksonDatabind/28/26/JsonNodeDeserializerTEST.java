package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.util.RawValue;

//imports for test cases
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Assert;
import org.junit.Test;

public class JsonNodeDeserializerTest {
    //test getNullValue(DeserializationContext ctxt) method
    @Test
    public void testGetNullValue() {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = null;
        JsonNode result = jsonNodeDeserializer.getNullValue(ctxt);
        Assert.assertNotNull(result);
    }

    //test deserialize(JsonParser p, DeserializationContext ctxt) method
    @Test
    public void testDeserialize() throws Exception {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonParser p = null;
        DeserializationContext ctxt = null;
        JsonNode result = jsonNodeDeserializer.deserialize(p, ctxt);
        Assert.assertNotNull(result);
    }

    //test deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) method
    @Test
    public void testDeserializeWithType() throws Exception {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer(JsonNode.class);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        TypeDeserializer typeDeserializer = null;
        Object result = jsonNodeDeserializer.deserializeWithType(p, ctxt, typeDeserializer);
        Assert.assertNull(result);
    }
}
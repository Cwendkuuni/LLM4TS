package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.RawValue;

import org.junit.Assert;
import org.junit.Test;

public class JsonNodeDeserializerTest {

    @Test
    public void testGetNullValue() {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        DeserializationContext deserializationContext = null;
        JsonNode result = jsonNodeDeserializer.getNullValue(deserializationContext);
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
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        JsonNode result = jsonNodeDeserializer.deserialize(jsonParser, deserializationContext);
        Assert.assertNull(result);
    }

    @Test
    public void testGetDeserializer() {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        Class<?> nodeClass = null;
        JsonDeserializer<? extends JsonNode> result = jsonNodeDeserializer.getDeserializer(nodeClass);
        Assert.assertEquals(JsonNodeDeserializer.instance, result);
    }

}
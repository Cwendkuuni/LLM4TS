package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.RawValue;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

    @Test
    public void testGetNullValue() {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = null;
        JsonNode result = deserializer.getNullValue(ctxt);
        assertEquals(NullNode.getInstance(), result);
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonParser parser = null;
        DeserializationContext ctxt = null;
        JsonNodeFactory nodeFactory = null;
        JsonNode result = deserializer.deserialize(parser, ctxt);
        switch (parser.getCurrentTokenId()) {
            case JsonTokenId.ID_START_OBJECT:
                assertEquals(deserializer.deserializeObject(p, ctxt, nodeFactory), result);
                break;
            case JsonTokenId.ID_START_ARRAY:
                assertEquals(deserializer.deserializeArray(p, ctxt, nodeFactory), result);
                break;
            default:
                assertEquals(deserializer.deserializeAny(p, ctxt, nodeFactory), result);
                break;
        }
    }

}
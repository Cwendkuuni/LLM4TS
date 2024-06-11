package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        JsonParser parser = new JsonFactory().createParser("");
        DeserializationContext ctxt = new DeserializationContext(null, null);
        NullifyingDeserializer deserializer = new NullifyingDeserializer();

        assertNull(deserializer.deserialize(parser, ctxt));

        parser.close();
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser parser = new JsonFactory().createParser("");
        DeserializationContext ctxt = new DeserializationContext(null, null);
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        TypeDeserializer typeDeserializer = new TypeDeserializer() {
            @Override
            public Object deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };

        assertNull(deserializer.deserializeWithType(parser, ctxt, typeDeserializer));

        parser.close();
    }
}
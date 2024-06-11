package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = null;
        DeserializationContext context = null;
        
        try {
            assertNull(deserializer.deserialize(parser, context));
        } catch (Exception e) {
            fail("Exception thrown during deserialization");
        }
    }

    @Test
    public void testDeserializeWithType() {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = null;
        DeserializationContext context = null;
        TypeDeserializer typeDeserializer = null;
        
        try {
            assertNull(deserializer.deserializeWithType(parser, context, typeDeserializer));
        } catch (Exception e) {
            fail("Exception thrown during deserialization with type");
        }
    }

}
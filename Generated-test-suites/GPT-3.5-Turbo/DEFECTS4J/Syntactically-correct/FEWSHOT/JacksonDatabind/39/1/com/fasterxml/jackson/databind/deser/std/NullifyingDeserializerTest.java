package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NullifyingDeserializerTest {
    @Test
    public void testDeserialize() {
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        assertNull(NullifyingDeserializer.instance.deserialize(parser, context));
    }

    @Test
    public void testDeserializeWithType() {
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        TypeDeserializer typeDeserializer = new TypeDeserializer();

        parser.nextToken(JsonToken.START_ARRAY);
        assertEquals(null, NullifyingDeserializer.instance.deserializeWithType(parser, context, typeDeserializer));

        parser.nextToken(JsonToken.START_OBJECT);
        assertEquals(null, NullifyingDeserializer.instance.deserializeWithType(parser, context, typeDeserializer));

        parser.nextToken(JsonToken.FIELD_NAME);
        assertEquals(null, NullifyingDeserializer.instance.deserializeWithType(parser, context, typeDeserializer));

        parser.nextToken(JsonToken.VALUE_STRING);
        assertEquals(null, NullifyingDeserializer.instance.deserializeWithType(parser, context, typeDeserializer));

        parser.nextToken(JsonToken.VALUE_NUMBER_INT);
        assertEquals(null, NullifyingDeserializer.instance.deserializeWithType(parser, context, typeDeserializer));

        parser.nextToken(JsonToken.END_OBJECT);
        assertEquals(null, NullifyingDeserializer.instance.deserializeWithType(parser, context, typeDeserializer));
    }
}
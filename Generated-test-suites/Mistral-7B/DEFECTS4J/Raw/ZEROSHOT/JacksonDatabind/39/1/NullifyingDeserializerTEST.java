package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

/**
 * Test class for NullifyingDeserializer
 */
public class NullifyingDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDeserialize() throws Exception {
        String json = "{\"name\":\"John\", \"age\":30}";
        StdTypeDeserializer typeDeserializer = new StdTypeDeserializer();
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;

        JsonParser parser = MAPPER.getFactory().createParser(json);
        Object result = deserializer.deserialize(parser, new DeserializationContext(MAPPER.getFactory(), null, null));

        assertNull(result);

        // Test with empty JSON
        String emptyJson = "{}";
        result = deserializer.deserialize(MAPPER.getFactory().createParser(emptyJson), new DeserializationContext(MAPPER.getFactory(), null, null));
        assertNull(result);

        // Test with empty string
        String emptyString = "";
        parser = MAPPER.getFactory().createParser(emptyString);
        result = deserializer.deserialize(parser, new DeserializationContext(MAPPER.getFactory(), null, null));
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        String json = "[1, 2, 3]";
        StdTypeDeserializer typeDeserializer = new StdTypeDeserializer();
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;

        JsonParser parser = MAPPER.getFactory().createParser(json);
        Object result = deserializer.deserializeWithType(parser, new DeserializationContext(MAPPER.getFactory(), null, typeDeserializer), typeDeserializer);

        assertNull(result);

        json = "{\"name\":\"John\", \"age\":30}";
        result = deserializer.deserializeWithType(parser, new DeserializationContext(MAPPER.getFactory(), null, typeDeserializer), typeDeserializer);

        assertNull(result);

        json = "null";
        parser = MAPPER.getFactory().createParser(json);
        result = deserializer.deserializeWithType(parser, new DeserializationContext(MAPPER.getFactory(), null, typeDeserializer), typeDeserializer);

        assertNull(result);

        // Test with arrays
        json = "[1, 2, [3, 4]]";
        ObjectNode rootNode = (ObjectNode) MAPPER.readTree(json);
        result = deserializer.deserializeWithType(parser, new DeserializationContext(MAPPER.getFactory(), null, typeDeserializer), typeDeserializer);

        assertNull(result);

        // Test with objects
        json = "{\"name\":\"John\", \"age\":30, \"hobbies\":[\"reading\", \"swimming\"]}";
        ObjectNode rootNodeObj = (ObjectNode) MAPPER.readTree(json);
        Object node = rootNodeObj.get("hobbies");
        JsonParser hobbiesParser = MAPPER.getFactory().createParser(node.toString());

        result = deserializer.deserializeWithType(hobbiesParser, new DeserializationContext(MAPPER.getFactory(), null, typeDeserializer), typeDeserializer);

        assertNull(result);
    }
}
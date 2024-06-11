package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        assertNull(deserializer.deserialize(null, null)); // Test deserialize method with null inputs
    }

    @Test
    public void testDeserializeWithType() {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        NullNode nullNode = NullNode.getInstance();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.valueToTree(nullNode);

        assertEquals(null, deserializer.deserializeWithType(null, null, null)); // Test deserializeWithType method with null inputs
        assertEquals(null, deserializer.deserializeWithType(jsonNode.traverse(), null, null)); // Test with JsonNode

    }

}
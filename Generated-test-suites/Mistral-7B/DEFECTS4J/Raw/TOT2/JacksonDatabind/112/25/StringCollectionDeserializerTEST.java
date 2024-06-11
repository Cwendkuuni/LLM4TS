package com.example.junit;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class StringCollectionDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @After
    public void tearDown() throws IOException {
        objectMapper = null;
    }

    @Test
    public void testDeserializeEmptyCollection() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        Collection<String> result = deserializer.deserialize(jsonParser, new DeserializationContext(), new ArrayList<>());
        assertEquals(0, result.size());
    }

    @Test
    public void testDeserializeSingleElementCollection() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("['one']");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        Collection<String> result = deserializer.deserialize(jsonParser, new DeserializationContext(), new ArrayList<>());
        assertEquals(1, result.size());
        assertEquals("one", result.iterator().next());
    }

    @Test
    public void testDeserializeMultipleElementCollection() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("['one', 'two', 'three']");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        Collection<String> result = deserializer.deserialize(jsonParser, new DeserializationContext(), new ArrayList<>());
        assertEquals(3, result.size());
        List<String> expected = new ArrayList<>();
        expected.add("one");
        expected.add("two");
        expected.add("three");
        assertEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJson() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("{invalid_json}");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        deserializer.deserialize(jsonParser, new DeserializationContext(), new ArrayList<>());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNullJson() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser(null);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        deserializer.deserialize(jsonParser, new DeserializationContext(), new ArrayList<>());
    }

    @Test
    public void testDeserializeWithCustomDeserializer() throws IOException {
        ObjectNode node = objectMapper.createObjectNode();
        ArrayNode arrayNode = node.set("values", objectMapper.createArrayNode());
        String testString = "test";
        StdSerializer<String> customSerializer = new StdSerializer<String>(String.class) {};
        JsonDeserializer<String> customDeserializer = new JsonDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return customSerializer.serializeWithType(p, ctxt, null);
            }
        };

        arrayNode.add(ObjectMapper.Factory.instance.getFactory().valueToTree(testString));
        JsonParser jsonParser = objectMapper.getFactory().createParser(node.toString());

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        deserializer._valueDeserializer = customDeserializer;
        Collection<String> result = deserializer.deserialize(jsonParser, new DeserializationContext(), new ArrayList<>());
        assertEquals(1, result.size());
        assertEquals(testString, result.iterator().next());
    }
}
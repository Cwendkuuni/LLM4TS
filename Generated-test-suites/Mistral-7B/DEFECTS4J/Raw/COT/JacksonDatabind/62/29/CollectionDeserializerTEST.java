package com.example.test; // replace with your test package

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;

public class CollectionDeserializerTest {

    @Test
    public void testCollectionDeserializerWithDefaults() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[]");

        CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        Collection<Object> collection = deserializer.deserialize(mapper.treeToValues(jsonNode, Object.class), null);
        assertNotNull(collection);
        assertEquals(0, collection.size());
    }

    @Test
    public void testCollectionDeserializerWithValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[1, 2, 3]");

        JsonDeserializer<Integer> integerDeserializer = mapper.findValueDeserializer(Integer.class, null);
        CollectionDeserializer<Collection<Integer>> deserializer = mapper.findValueDeserializer(Collection.class, Collection.class);
        deserializer = deserializer.withResolved(null, integerDeserializer, null, null);

        Collection<Integer> collection = deserializer.deserialize(mapper.treeToValues(jsonNode, Object.class), null);
        assertNotNull(collection);
        assertEquals(3, collection.size());
        assertEquals(1, collection.contains(1));
        assertEquals(2, collection.contains(2));
        assertEquals(3, collection.contains(3));
    }

    @Test
    public void testCollectionDeserializerWithTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[{\"name\": \"John\", \"age\": 30}, {\"name\": \"Jane\", \"age\": 25}]");

        SimpleType type = mapper.getTypeFactory().constructType(List.class, Map.class);
        JsonDeserializer<Map<String, Integer>> mapDeserializer = mapper.findValueDeserializer(Map.class, Map.class);
        JsonDeserializer<Map.Entry<String, Integer>> entryDeserializer = mapper.findValueDeserializer(Map.Entry.class, Map.Entry.class);
        TypeDeserializer typeDeserializer = mapper.findTypeDeserializer(type, null);

        CollectionDeserializer<Collection<Map<String, Integer>>> deserializer = mapper.findValueDeserializer(Collection.class, Collection.class);
        deserializer = deserializer.withResolved(null, mapDeserializer, typeDeserializer, null);

        Collection<Map<String, Integer>> collection = deserializer.deserialize(mapper.treeToValues(jsonNode, Object.class), null);
        assertNotNull(collection);
        assertEquals(2, collection.size());
        assertEquals(Arrays.asList(
                Map.of("name", "John", "age", 30),
                Map.of("name", "Jane", "age", 25)
        ), collection);
    }

    @Test
    public void testCollectionDeserializerWithEmptyString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[]");
        jsonNode.setToken(JsonParser.CurrentToken.VALUE_STRING);
        jsonNode.setText("");

        CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, Collection.class);
        Collection<Object> collection = deserializer.deserialize(mapper.treeToValues(jsonNode, Object.class), null);
        assertNotNull(collection);
        assertEquals(0, collection.size());
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializerWithInvalidJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{ \"name\": \"John\" }");

        CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, Collection.class);
        deserializer.deserialize(mapper.treeToValues(jsonNode, Object.class), null);
    }
}
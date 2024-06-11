package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CollectionDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);

        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        JsonNode node = mapper.readTree(json);

        Collection<Object> result = deserializer.deserialize(node.traverse(mapper.getNodeFactory()), mapper.getDeserializationContext());

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(1));
        assertTrue(result.contains(2));
        assertTrue(result.contains(3));
    }

    @Test
    public void testDeserializeWithNull() throws Exception {
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);

        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, null, 3]";
        JsonNode node = mapper.readTree(json);

        Collection<Object> result = deserializer.deserialize(node.traverse(mapper.getNodeFactory()), mapper.getDeserializationContext());

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(1));
        assertTrue(result.contains(null));
        assertTrue(result.contains(3));
    }

    @Test
    public void testDeserializeEmptyArray() throws Exception {
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);

        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        JsonNode node = mapper.readTree(json);

        Collection<Object> result = deserializer.deserialize(node.traverse(mapper.getNodeFactory()), mapper.getDeserializationContext());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserializeSingleValue() throws Exception {
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);

        ObjectMapper mapper = new ObjectMapper();
        String json = "1";
        JsonNode node = mapper.readTree(json);

        Collection<Object> result = deserializer.deserialize(node.traverse(mapper.getNodeFactory()), mapper.getDeserializationContext());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(1));
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);

        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, \"two\", 3.0]";
        JsonNode node = mapper.readTree(json);

        List<Object> result = (List<Object>) deserializer.deserializeWithType(node.traverse(mapper.getNodeFactory()), mapper.getDeserializationContext());

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(1));
        assertTrue(result.contains("two"));
        assertTrue(result.contains(3.0));
    }

    @Test
    public void testDeserializeEmptyString() throws Exception {
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);

        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        JsonNode node = mapper.readTree(json);

        Collection<Object> result = deserializer.deserialize(node.traverse(mapper.getNodeFactory()), mapper.getDeserializationContext());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}
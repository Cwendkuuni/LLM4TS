package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CollectionDeserializerTest {

    private ObjectMapper mapper;
    private CollectionDeserializer collectionDeserializer;

    @BeforeMethod
    public void setup() {
        mapper = new ObjectMapper();
        collectionDeserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                mock(JsonDeserializer.class), null, null, null, null);
    }

    @Test
    public void testEmptyCollectionDeserialization() throws IOException {
        ObjectNode jsonNode = mapper.createObjectNode();
        List<String> actual = collectionDeserializer.deserialize(mapper.getFactory().createParser(jsonNode), mapper);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testSingleElementCollectionDeserialization() throws IOException {
        ObjectNode jsonNode = mapper.createObjectNode();
        ObjectNode elementNode = mapper.createObjectNode();
        elementNode.put("name", "John");
        jsonNode.put("value", elementNode);

        List<String> actual = collectionDeserializer.deserialize(mapper.getFactory().createParser(jsonNode), mapper);
        assertEquals(actual.size(), 1);
        assertEquals(actual.get(0), "John");
    }

    @Test
    public void testMultipleElementsCollectionDeserialization() throws IOException {
        ObjectNode jsonNode = mapper.createObjectNode();
        ObjectNode elementNode1 = mapper.createObjectNode();
        ObjectNode elementNode2 = mapper.createObjectNode();
        elementNode1.put("name", "John");
        elementNode2.put("name", "Mike");
        jsonNode.put("value", mapper.createValueFactory().arrayValue().add(elementNode1).add(elementNode2));

        List<String> actual = collectionDeserializer.deserialize(mapper.getFactory().createParser(jsonNode), mapper);
        assertEquals(actual.size(), 2);
        assertEquals(actual.get(0), "John");
        assertEquals(actual.get(1), "Mike");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidJson() throws IOException, JsonMappingException {
        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.put("invalid", "not an array");
        collectionDeserializer.deserialize(mapper.getFactory().createParser(jsonNode), mapper);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithNull() throws IOException, JsonMappingException {
        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.putNull("value");
        collectionDeserializer.deserialize(mapper.getFactory().createParser(jsonNode), mapper);
    }

    @Test
    public void testDeserializationWithEmptyString() throws IOException {
        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.put("value", "");
        List<String> actual = collectionDeserializer.deserialize(mapper.getFactory().createParser(jsonNode), mapper);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testDeserializationWithUnresolvedForwardReference() throws IOException, JsonProcessingException {
        ObjectNode jsonNode = mapper.createObjectNode();
        ObjectNode referencedNode = mapper.createObjectNode();
        referencedNode.put("name", "John");
        jsonNode.put("value", mapper.createReferenceNode(referencedNode));

        CollectionDeserializer deserializerWithResolvedReference = collectionDeserializer.withResolved(
                mapper.findRootValueDeserializer(String.class), null, null, true);
        List<String> actual = deserializerWithResolvedReference.deserialize(
                mapper.getFactory().createParser(jsonNode), mapper);
        assertEquals(actual.size(), 1);
        assertEquals(actual.get(0), "John");
    }
}
package com.example.test; // replace with your test package

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class CollectionDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testCollectionDeserializer_EmptyArray() throws IOException {
        JsonParser jsonParser = mapper.getFactory().createParser("[]");
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(
                mapper.constructType(List.class),
                mapper.getFactory().getPolymorphicValueDeserializer(Integer.class),
                null,
                null,
                null,
                null);
        List<Integer> list = collectionDeserializer.deserialize(jsonParser, mapper.getDeserializationContext());
        assertEquals(0, list.size());
    }

    @Test
    public void testCollectionDeserializer_EmptyString() throws IOException {
        JsonParser jsonParser = mapper.getFactory().createParser("[]");
        jsonParser.nextToken(); // consume [
        String emptyString = jsonParser.getValueAsString();
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(
                mapper.constructType(List.class),
                mapper.getFactory().getPolymorphicValueDeserializer(Integer.class),
                null,
                null,
                null,
                null);
        List<Integer> list = collectionDeserializer.deserializeWithType(jsonParser, mapper.getDeserializationContext(), mapper.getTypeFactory().constructType(List.class));
        assertEquals(0, list.size());
    }

    @Test
    public void testCollectionDeserializer_SingleValue() throws IOException {
        JsonParser jsonParser = mapper.getFactory().createParser("[]");
        jsonParser.nextToken(); // consume [
        jsonParser.nextToken(); // consume null
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(
                mapper.constructType(List.class),
                mapper.getFactory().getPolymorphicValueDeserializer(Integer.class),
                null,
                null,
                null,
                null);
        List<Integer> list = collectionDeserializer.deserializeWithType(jsonParser, mapper.getDeserializationContext(), mapper.getTypeFactory().constructType(List.class));
        assertEquals(0, list.size());

        jsonParser.reset();
        jsonParser.nextToken(); // consume [
        jsonParser.nextToken(); // consume null
        jsonParser.nextToken(); // consume Integer value
        JsonNode node = jsonParser.readValueAsTree();
        list = mapper.readValue(node.toString(), List.class);
        assertEquals(1, list.size());
        assertEquals(42, list.get(0));
    }

    @Test
    public void testCollectionDeserializer_SingleValueArray() throws IOException {
        JsonParser jsonParser = mapper.getFactory().createParser("[-1, 0, 1]");
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(
                mapper.constructType(List.class),
                mapper.getFactory().getPolymorphicValueDeserializer(Integer.class),
                null,
                null,
                null,
                null);
        List<Integer> list = collectionDeserializer.deserialize(jsonParser, mapper.getDeserializationContext());
        assertEquals(3, list.size());
        assertEquals(-1, list.get(0));
        assertEquals(0, list.get(1));
        assertEquals(1, list.get(2));
    }

    @Test
    public void testCollectionDeserializer_Nested() throws IOException {
        JsonParser jsonParser = mapper.getFactory().createParser("{" +
                "\"list\": {" +
                "\"elements\": [" +
                "{" +
                "\"id\": 1," +
                "\"value\": 1" +
                "}," +
                "{" +
                "\"id\": 2," +
                "\"value\": 2" +
                "}" +
                "]," +
                "\"type\": \"List\"" +
                "}" +
                "}");
        JsonNode node = jsonParser.readValueAsTree();
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(
                mapper.constructType(Map.class),
                mapper.getFactory().getPolymorphicValueDeserializer(Map.Entry.class),
                null,
                null,
                null,
                null);
        Map<String, Object> map = collectionDeserializer.deserialize(jsonParser, mapper.getDeserializationContext());
        Map<String, List<Object>> nestedMap = (Map<String, List<Object>>) map.get("list");
        List<Object> list = nestedMap.get("elements");
        assertEquals(2, list.size());
        Map.Entry<String, Object> entry = list.get(0);
        assertEquals("id", entry.getKey());
        assertEquals(1, entry.getValue().getClass().getComponentType().getComponentCount());
        assertEquals(1, ((List<Object>) entry.getValue()).get(0).getClass().getComponentType().getComponentCount());
        assertEquals(1, ((List<Object>) entry.getValue()).get(0).get(0).intValue());

        entry = list.get(1);
        assertEquals("id", entry.getKey());
        assertEquals(2, entry.getValue().getClass().getComponentType().getComponentCount());
        assertEquals(2, ((List<Object>) entry.getValue()).size());
        assertEquals(2, ((List<Object>) entry.getValue()).get(0).getClass().getComponentType().getComponentCount());
        assertEquals(2, ((List<Object>) entry.getValue()).get(0).get(0).intValue());
        assertEquals(2, ((List<Object>) entry.getValue()).get(1).getClass().getComponentType().getComponentCount());
        assertEquals(2, ((List<Object>) entry.getValue()).get(1).get(0).intValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializer_InvalidJson() throws IOException {
        JsonParser jsonParser = mapper.getFactory().createParser("{invalidJson}");
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(
                mapper.constructType(List.class),
                mapper.getFactory().getPolymorphicValueDeserializer(Integer.class),
                null,
                null,
                null,
                null);
        collectionDeserializer.deserialize(jsonParser, mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializer_MissingEndArray() throws IOException {
        JsonParser jsonParser = mapper.getFactory().createParser("[-1,");
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(
                mapper.constructType(List.class),
                mapper.getFactory().getPolymorphicValueDeserializer(Integer.class),
                null,
                null,
                null,
                null);
        collectionDeserializer.deserialize(jsonParser, mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializer_InvalidType() throws IOException {
        JsonParser jsonParser = mapper.getFactory().createParser("[-1, 0, 1]");
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(
                mapper.constructType(Map.class),
                mapper.getFactory().getPolymorphicValueDeserializer(Integer.class),
                null,
                null,
                null,
                null);
        collectionDeserializer.deserialize(jsonParser, mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializer_NullValueDeserializer() throws IOException {
        JsonParser jsonParser = mapper.getFactory().createParser("[-1, null, 1]");
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(
                mapper.constructType(List.class),
                null,
                null,
                null,
                null,
                null);
        collectionDeserializer.deserialize(jsonParser, mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializer_NullValueTypeDeserializer() throws IOException {
        JsonParser jsonParser = mapper.getFactory().createParser("[-1, null, 1]");
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(
                mapper.constructType(List.class),
                mapper.getFactory().getPolymorphicValueDeserializer(Integer.class),
                null,
                null,
                null,
                null);
        collectionDeserializer = collectionDeserializer.withResolved(null, null, null);
        collectionDeserializer.deserialize(jsonParser, mapper.getDeserializationContext());
    }
}
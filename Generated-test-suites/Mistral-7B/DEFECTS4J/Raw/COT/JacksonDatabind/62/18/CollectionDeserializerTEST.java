package com.example; // replace with your test package

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Test for CollectionDeserializer class.
 */
public class CollectionDeserializerTest {

    @Test
    public void testCollectionDeserializer() throws IOException, JsonProcessingException {
        // Test empty collection deserialization
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(
                ObjectMapper.getDefaultInstance().constructType(List.class),
                null, null, null, null, null);
        JsonParser jsonParser = ObjectMapper.getDefaultInstance().getFactory()
                .createParser("[ ]");
        List<Object> result = collectionDeserializer.deserialize(jsonParser, null);
        assertEquals(0, result.size());

        // Test serialization and deserialization of a simple list
        ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
        List<Integer> list = Arrays.asList(1, 2, 3);
        String jsonString = objectMapper.writeValueAsString(list);
        jsonParser = objectMapper.getFactory().createParser(jsonString);
        result = collectionDeserializer.deserialize(jsonParser, null);
        assertEquals(list, result);

        // Test deserialization of a list with null values
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        collectionDeserializer = new CollectionDeserializer(
                ObjectMapper.getDefaultInstance().constructType(List.class),
                null, null, null, null, null, stringDeserializer);
        jsonParser = ObjectMapper.getDefaultInstance().getFactory()
                .createParser("[ null, null ]");
        result = collectionDeserializer.deserialize(jsonParser, null);
        List<Object> expectedList = Arrays.asList(null, null);
        assertEquals(expectedList, result);

        // Test deserialization of a list with empty strings
        collectionDeserializer = new CollectionDeserializer(
                ObjectMapper.getDefaultInstance().constructType(List.class),
                null, null, null, null, null, StringDeserializer.instance);
        jsonParser = ObjectMapper.getDefaultInstance().getFactory()
                .createParser("[ \"\", \"\" ]");
        result = collectionDeserializer.deserialize(jsonParser, null);
        expectedList = Arrays.asList("", "");
        assertEquals(expectedList, result);

        // Test deserialization of a list with different types
        collectionDeserializer = new CollectionDeserializer(
                ObjectMapper.getDefaultInstance().constructType(List.class),
                null, null, null, null, null);
        jsonParser = ObjectMapper.getDefaultInstance().getFactory()
                .createParser("[ 1, \"two\", true ]");
        result = collectionDeserializer.deserialize(jsonParser, null);
        assertEquals(3, result.size());
        assertTrue(result.get(0) instanceof Number);
        assertEquals(1, result.get(0));
        assertTrue(result.get(1) instanceof String);
        assertEquals("two", result.get(1));
        assertTrue(result.get(2) instanceof Boolean);
        assertEquals(true, result.get(2));

        // Test deserialization of a list with single value wrap
        collectionDeserializer = new CollectionDeserializer(
                ObjectMapper.getDefaultInstance().constructType(List.class),
                null, null, null, null, true);
        jsonParser = ObjectMapper.getDefaultInstance().getFactory()
                .createParser("[ 1 ]");
        result = collectionDeserializer.deserialize(jsonParser, null);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof Number);
        assertEquals(1, result.get(0));

        // Test deserialization of a list with single value unwrap
        collectionDeserializer = new CollectionDeserializer(
                ObjectMapper.getDefaultInstance().constructType(List.class),
                null, null, null, null, false);
        jsonParser = ObjectMapper.getDefaultInstance().getFactory()
                .createParser("[ 1 ]");
        result = collectionDeserializer.deserialize(jsonParser, null);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof Number);
        assertEquals(1, result.get(0));

        // Test deserialization of an empty list with a non-empty list as a fallback
        collectionDeserializer = new CollectionDeserializer(
                ObjectMapper.getDefaultInstance().constructType(List.class),
                null, null, null, null, null);
        jsonParser = ObjectMapper.getDefaultInstance().getFactory()
                .createParser("[ ]");
        ObjectMapper objectMapperForFallback = ObjectMapper.getDefaultInstance();
        ObjectMapper fallbackMapper = new ObjectMapper();
        fallbackMapper.registerModule(new SimpleModule()
                .addSerializer(List.class, new ListSerializer<>()));
        JsonDeserializer<List<Object>> fallbackDeserializer = fallbackMapper
                .findValueDeserializer(List.class, null);
        collectionDeserializer.setValueDeserializer(fallbackDeserializer);
        result = collectionDeserializer.deserialize(jsonParser, null);
        assertEquals(0, result.size());

        // Test deserialization of a list with an undefined type
        collectionDeserializer = new CollectionDeserializer(
                ObjectMapper.getDefaultInstance().constructType(List.class),
                null, null, null, null, null);
        jsonParser = ObjectMapper.getDefaultInstance().getFactory()
                .createParser("[ undefined ]");
        try {
            result = collectionDeserializer.deserialize(jsonParser, null);
            fail();
        } catch (JsonParseException e) {
            // expected
        }

        // Test deserialization of a list with an unresolved reference
        collectionDeserializer = new CollectionDeserializer(
                ObjectMapper.getDefaultInstance().constructType(List.class),
                null, null, null, null, null);
        JsonFactory jsonFactory = ObjectMapper.getDefaultInstance().getFactory();
        JsonParser creatorParser = jsonFactory.createParser("[ { \"$ref\": \"#/properties/property1\" } ]");
        JsonParser propertyParser = jsonFactory.createParser("[ { \"$id\": \"prop1\" } ]");
        creatorParser.nextToken(); // consume [
        Creator creator = collectionDeserializer.createContextual(
                ObjectMapper.getDefaultInstance().getDeserializationContext(),
                null);
        creator.handleSecondaryContextualization(null, null,
                ObjectMapper.getDefaultInstance().constructType(Map.class));
        JsonNode propertyNode = propertyParser.readValueAs(JsonNode.class);
        UnresolvedForwardReference reference = new UnresolvedForwardReference(propertyNode, creator.getClass(),
                null, null);
        try {
            result = collectionDeserializer.deserialize(jsonParser, null, null);
            fail();
        } catch (JsonMappingException e) {
            // expected
            assertEquals(JsonMappingException.ClassId.UNRESOLVED_FORWARD_REFERENCE, e.getId());
        }
    }
}
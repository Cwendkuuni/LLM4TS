package com.example.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testCollectionDeserializer() throws IOException {
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser jsonParser = jsonFactory.getParser();

        // Test deserializing a Collection with default settings
        ArrayNode jsonArray = mapper.createArrayNode();
        jsonArray.add(1);
        jsonArray.add(2);
        jsonArray.add(3);
        Collection<Integer> collection = mapper.readValue(jsonArray.getValueAsString(), CollectionDeserializer.class);
        assertEquals(3, collection.size());
        assertTrue(collection.contains(1));
        assertTrue(collection.contains(2));
        assertTrue(collection.contains(3));

        // Test deserializing a Collection with a custom ValueDeserializer
        JsonDeserializer<String> stringDeserializer = mapper.getFactory().getDeserializerFactory()
                .findValueDeserializer(String.class, null);
        CollectionDeserializer<String> stringCollectionDeserializer = new CollectionDeserializer<>(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                stringDeserializer, null, null, null, null);
        collection = mapper.readValue(jsonArray.getValueAsString(), stringCollectionDeserializer.getClass());
        assertEquals(3, collection.size());
        assertTrue(collection.contains("1"));
        assertTrue(collection.contains("2"));
        assertTrue(collection.contains("3"));

        // Test deserializing a Collection with a custom TypeDeserializer
        JsonDeserializer<Integer> intDeserializer = mapper.getFactory().getDeserializerFactory()
                .findValueDeserializer(Integer.class, null);
        CollectionDeserializer<List<Integer>> intListDeserializer = new CollectionDeserializer<>(
                mapper.getTypeFactory().constructCollectionType(List.class, Integer.class),
                intDeserializer, new IntTypeDeserializer(), null, null, null);
        collection = mapper.readValue(jsonArray.getValueAsString(), intListDeserializer.getClass());
        assertEquals(3, collection.size());
        assertEquals(1, ((Integer) collection.get(0)).intValue());
        assertEquals(2, ((Integer) collection.get(1)).intValue());
        assertEquals(3, ((Integer) collection.get(2)).intValue());

        // Test deserializing an empty Collection
        jsonParser.setValue("[]");
        collection = mapper.readValue(jsonParser, CollectionDeserializer.class);
        assertTrue(collection.isEmpty());

        // Test deserializing a Collection with a single-value element
        jsonArray.add(new Object());
        collection = mapper.readValue(jsonArray.getValueAsString(), CollectionDeserializer.class);
        assertEquals(1, collection.size());

        // Test deserializing a Collection with a null value
        jsonArray.set(0, JsonNull.INSTANCE);
        collection = mapper.readValue(jsonArray.getValueAsString(), CollectionDeserializer.class);
        assertTrue(collection.isEmpty());

        // Test deserializing a Collection with an invalid JSON structure
        jsonParser.setValue("{ \"key\": 1 }");
        try {
            collection = mapper.readValue(jsonParser, CollectionDeserializer.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    private static class IntTypeDeserializer extends StdTypeDeserializer<Integer> {
        public IntTypeDeserializer() {
            this(null);
        }

        protected IntTypeDeserializer(DeserializationConfig config) {
            super(Integer.class, config);
        }

        @Override
        public Integer deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            return p.getValueAsInt();
        }
    }
}
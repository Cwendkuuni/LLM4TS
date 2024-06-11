package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactoryBuilder;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class MappingIteratorTest {

    private ObjectMapper mapper;
    private ObjectReader reader;
    private JsonFactory jsonFactory;

    @Before
    public void setUp() {
        jsonFactory = new JsonFactoryBuilder().build();
        mapper = new ObjectMapper(jsonFactory);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        reader = mapper.readerForValueType(ArrayNode.class);
    }

    @Test
    public void testEmptyIterator() throws IOException {
        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();

        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testBasicUsage() throws IOException {
        ArrayNode json = jsonFactory.createArrayNode();
        json.add(ObjectNode.parse("{\"name\": \"John\"}"));
        json.add(ObjectNode.parse("{\"name\": \"Jane\"}"));
        JsonParser parser = json.getParser();

        MappingIterator<ObjectNode> iterator = reader.readValues(parser);

        assertEquals(ObjectNode.class, iterator.next().getClass());
        assertEquals(ObjectNode.class, iterator.next().getClass());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testThrowIOException() throws IOException {
        ArrayNode json = jsonFactory.createArrayNode();
        json.add(ObjectNode.parse("{\"name\": \"John\"}"));
        json.add(ObjectNode.parse("{\"name\": \"Jane\"}"));
        JsonParser parser = json.getParser();

        // Create a deserializer that throws an IOException
        JsonDeserializer<ObjectNode> deserializer = new JsonDeserializer<ObjectNode>() {
            @Override
            public ObjectNode deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                throw new IOException("Test IOException");
            }
        };

        MappingIterator<ObjectNode> iterator = reader.readValues(parser, deserializer);

        assertTrue(iterator.hasNext());
        ObjectNode first = iterator.next();
        assertFalse(iterator.hasNext());

        assertThrows(IOException.class, () -> iterator.next());
    }

    @Test
    public void testCloseParser() throws IOException {
        ArrayNode json = jsonFactory.createArrayNode();
        json.add(ObjectNode.parse("{\"name\": \"John\"}"));
        json.add(ObjectNode.parse("{\"name\": \"Jane\"}"));
        JsonParser parser = json.getParser();

        MappingIterator<ObjectNode> iterator = reader.readValues(parser, null, true);

        assertEquals(ObjectNode.class, iterator.next().getClass());
        assertEquals(ObjectNode.class, iterator.next().getClass());
        assertFalse(iterator.hasNext());
        parser.close();
    }

    @Test
    public void testUpdateValue() throws IOException {
        ArrayNode json = jsonFactory.createArrayNode();
        json.add(ObjectNode.parse("{\"name\": \"John\"}"));
        json.add(ObjectNode.parse("{\"name\": \"Jane\"}"));
        JsonParser parser = json.getParser();

        String initialValue = "initial";
        ObjectNode updatedValue = ObjectNode.parser().readValue(TypeFactory.defaultInstance().constructType(String.class));
        updatedValue.put("updated", initialValue);

        MappingIterator<ObjectNode> iterator = reader.readValues(parser, null, false, null, updatedValue);

        assertEquals(ObjectNode.class, iterator.next().getClass());
        assertEquals(ObjectNode.class, iterator.next().getClass());

        ObjectNode first = iterator.next();
        assertEquals("initial", first.get("updated").asText());

        ObjectNode second = iterator.next();
        assertEquals("initial", second.get("updated").asText());

        // Update the value for the next iteration
        first.put("updated", "updated");

        ObjectNode next = iterator.next();
        assertEquals("updated", next.get("updated").asText());

        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws IOException {
        ArrayNode json = jsonFactory.createArrayNode();
        json.add(ObjectNode.parse("{\"name\": \"John\"}"));
        json.add(ObjectNode.parse("{\"name\": \"Jane\"}"));
        JsonParser parser = json.getParser();

        MappingIterator<ObjectNode> iterator = reader.readValues(parser);

        iterator.remove();
    }
}
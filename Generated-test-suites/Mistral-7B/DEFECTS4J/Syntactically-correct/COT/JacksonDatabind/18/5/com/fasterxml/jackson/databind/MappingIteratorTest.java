package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class MappingIteratorTest extends BaseMapTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ObjectReader READER = MAPPER.readerForMap();
    private static final TypeFactory TYPE_FACTORY = MAPPER.getTypeFactory();

    private InputStream inputStream;
    private JsonParser parser;
    private DeserializationContext context;
    private JsonDeserializer<Map<String, Integer>> deserializer;
    private Iterator<Map.Entry<String, Integer>> iterator;

    @Before
    public void setUp() throws Exception {
        deserializer = new JsonDeserializer<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                Map<String, Integer> map = new HashMap<>();
                while (p.nextToken() != null) {
                    String key = p.getText();
                    int value = p.nextIntValue();
                    map.put(key, value);
                }
                return map;
            }
        };

        ObjectReader reader = MAPPER.readerForMap(TYPE_FACTORY.constructMapType(Map.class, String.class, Integer.class))
                .with(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .with(SerializationFeature.UNWRAP_ROOT_VALUE, true);
        inputStream = new ByteArrayInputStream(createJsonArrayWithMaps().getBytes());
        parser = new JsonFactory().createParser(inputStream);
        context = reader.getDeserializationContext();
    }

    @Test
    public void testHasNext() throws Exception {
        iterator = MAPPER.readerForMap(TYPE_FACTORY.constructMapType(Iterator.class, Map.Entry.class))
                .readValues(parser);

        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNext() throws Exception {
        iterator = MAPPER.readerForMap(TYPE_FACTORY.constructMapType(Iterator.class, Map.Entry.class))
                .readValues(parser);

        Map.Entry<String, Integer> entry = iterator.next();
        assertNotNull(entry);
        assertEquals("key1", entry.getKey());
        assertEquals(1, entry.getValue().intValue());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testRemove() throws Exception {
        throw new UnsupportedOperationException("Method not supported.");
    }

    @Test
    public void testClose() throws Exception {
        iterator = MAPPER.readerForMap(TYPE_FACTORY.constructMapType(Iterator.class, Map.Entry.class))
                .readValues(parser);

        iterator.close();
        assertNull(parser);

        iterator = MAPPER.readerForMap(TYPE_FACTORY.constructMapType(Iterator.class, Map.Entry.class))
                .readValues(parser);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNextValue() throws Exception {
        iterator = MAPPER.readerForMap(TYPE_FACTORY.constructMapType(Iterator.class, Map.Entry.class))
                .readValues(parser);

        assertTrue(iterator.hasNextValue());
        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testNextValue() throws Exception {
        iterator = MAPPER.readerForMap(TYPE_FACTORY.constructMapType(Iterator.class, Map.Entry.class))
                .readValues(parser);

        Map.Entry<String, Integer> entry = iterator.nextValue();
        assertNotNull(entry);
        assertEquals("key1", entry.getKey());
        assertEquals(1, entry.getValue().intValue());

        assertTrue(iterator.hasNextValue());

        entry = iterator.nextValue();
        assertEquals("key2", entry.getKey());
        assertEquals(2, entry.getValue().intValue());

        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testReadAll() throws Exception {
        List<Map<String, Integer>> list = new ArrayList<>();
        iterator = MAPPER.readerForMap(TYPE_FACTORY.constructCollectionType(List.class, Map.class))
                .readValues(parser);

        while (iterator.hasNext()) {
            list.add((Map<String, Integer>) iterator.next());
        }

        assertEquals(3, list.size());
        assertTrue(list.contains(Map.entry("key1", 1)));
        assertTrue(list.contains(Map.entry("key2", 2)));
        assertTrue(list.contains(Map.entry("key3", 3)));
    }

    @Test
    public void testReadAllCollection() throws Exception {
        Collection<Map<String, Integer>> collection = new ArrayList<>();
        iterator = MAPPER.readerForMap(TYPE_FACTORY.constructCollectionType(Collection.class, Map.class))
                .readValues(parser);

        while (iterator.hasNext()) {
            collection.add((Map<String, Integer>) iterator.next());
        }

        assertEquals(3, collection.size());
        assertTrue(collection.contains(Map.entry("key1", 1)));
        assertTrue(collection.contains(Map.entry("key2", 2)));
        assertTrue(collection.contains(Map.entry("key3", 3)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testHasNextAfterClose() throws Exception {
        iterator = MAPPER.readerForMap(TYPE_FACTORY.constructMapType(Iterator.class, Map.Entry.class))
                .readValues(parser);
        iterator.close();
        iterator.hasNext();
    }

    @Test(expected = JsonMappingException.class)
    public void testHasNextWithMappingException() throws Exception {
        parser.nextToken();
        parser.nextToken(); // Intentionally leave current token as VALUE_STRING
        iterator = MAPPER.readerForMap(TYPE_FACTORY.constructMapType(Iterator.class, Map.Entry.class))
                .readValues(parser);
        iterator.hasNext();
    }

    @Test(expected = JsonMappingException.class)
    public void testNextWithMappingException() throws Exception {
        parser.nextToken();
        parser.nextToken(); // Intentionally leave current token as VALUE_STRING
        iterator = MAPPER.readerForMap(TYPE_FACTORY.constructMapType(Iterator.class, Map.Entry.class))
                .readValues(parser);
        iterator.next();
    }

    @Test(expected = IOException.class)
    public void testHasNextWithIOException() throws Exception {
        parser.close();
        iterator = MAPPER.readerForMap(TYPE_FACTORY.constructMapType(Iterator.class, Map.Entry.class))
                .readValues(parser);
        iterator.hasNext();
    }

    @Test(expected = IOException.class)
    public void testNextWithIOException() throws Exception {
        parser.close();
        iterator = MAPPER.readerForMap(TYPE_FACTORY.constructMapType(Iterator.class, Map.Entry.class))
                .readValues(parser);
        iterator.next();
    }

    private byte[] createJsonArrayWithMaps() throws IOException {
        ObjectWriter writer = MAPPER.writerWithRootValue(new ArrayList<>());
        ObjectWriter arrayWriter = writer.with(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        List<Object> list = new ArrayList<>();
        list.add(Map.of("key1", 1));
        list.add(Map.of("key2", 2));
        list.add(Map.of("key3", 3));
        return arrayWriter.writeValueAsBytes(list);
    }
}
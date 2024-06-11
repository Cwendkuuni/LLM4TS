package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class MappingIteratorTest {

    

    @SuppressWarnings("unchecked")
    private static <T> MappingIterator<T> createMappingIterator(ObjectMapper mapper,
            String json, Class<T> valueType) throws IOException {
        JsonFactory jsonFactory = mapper.getFactory();
        InputStream jsonInput = new ByteArrayInputStream(json.getBytes());
        JsonParser parser = jsonFactory.createParser(jsonInput);
        ObjectReader reader = mapper.readerFor(valueType);
        JsonDeserializer<T> deserializer = reader.getDeserializer();
        DeserializationContext context = mapper.getDeserializationContext();
        MappingIterator<T> mappingIterator = new MappingIterator<>(
                TypeFactory.defaultInstance().constructType(valueType), parser, context, deserializer,
                true, null);
        return mappingIterator;
    }

    @org.junit.Test
    public void testHasNextAndNextWithNoInitialValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        MappingIterator<Object> mappingIterator = createMappingIterator(mapper, json, Object.class);
        assertFalse(mappingIterator.hasNext());
        assertNull(mappingIterator.next());
        mappingIterator.close();
    }

    @org.junit.Test
    public void testHasNextAndNextWithInitialValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"foo\"]";
        MappingIterator<String> mappingIterator = createMappingIterator(mapper, json, String.class);
        assertTrue(mappingIterator.hasNext());
        assertEquals("foo", mappingIterator.next());
        assertTrue(mappingIterator.hasNext());
        assertEquals("foo", mappingIterator.next());
        mappingIterator.close();
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"foo\", \"bar\"]";
        MappingIterator<String> mappingIterator = createMappingIterator(mapper, json, String.class);
        mappingIterator.next();
        mappingIterator.remove();
    }

    @org.junit.Test
    public void testClose() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"foo\", \"bar\"]";
        MappingIterator<String> mappingIterator = createMappingIterator(mapper, json, String.class);
        mappingIterator.close();
        mappingIterator.next(); // should throw exception
    }

    @org.junit.Test
    public void testHasNextValueWithNoInitialValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        MappingIterator<Object> mappingIterator = createMappingIterator(mapper, json, Object.class);
        assertFalse(mappingIterator.hasNextValue());
        mappingIterator.close();
    }

    @org.junit.Test
    public void testHasNextValueWithInitialValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[null]";
        MappingIterator<Object> mappingIterator = createMappingIterator(mapper, json, Object.class);
        assertTrue(mappingIterator.hasNextValue());
        mappingIterator.nextValue();
        assertTrue(mappingIterator.hasNextValue());
        mappingIterator.close();
    }

    @org.junit.Test
    public void testNextValueWithNoInitialValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        MappingIterator<Object> mappingIterator = createMappingIterator(mapper, json, Object.class);
        assertNull(mappingIterator.nextValue());
        mappingIterator.close();
    }

    @org.junit.Test
    public void testNextValueWithInitialValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"foo\"]";
        MappingIterator<String> mappingIterator = createMappingIterator(mapper, json, String.class);
        assertEquals("foo", mappingIterator.nextValue());
        mappingIterator.close();
    }

    @org.junit.Test(expected = NoSuchElementException.class)
    public void testNoSuchElement() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        MappingIterator<Object> mappingIterator = createMappingIterator(mapper, json, Object.class);
        mappingIterator.next();
        mappingIterator.next();
        mappingIterator.next(); // should throw exception
        mappingIterator.close();
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testJsonMappingException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"foo\":[\"bar\", \"baz\"]}"; // invalid JSON
        MappingIterator<Object> mappingIterator = createMappingIterator(mapper, json, Object.class);
        mappingIterator.nextValue(); // should throw exception
        mappingIterator.close();
    }

    @org.junit.Test(expected = IOException.class)
    public void testIOException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"foo\", \"bar\"]"; // truncated JSON
        MappingIterator<Object> mappingIterator = createMappingIterator(mapper, json, Object.class);
        mappingIterator.nextValue();
        mappingIterator.nextValue();
        mappingIterator.nextValue(); // should throw exception
        mappingIterator.close();
    }

    @org.junit.Test
    public void testReadAll() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"foo\", \"bar\", \"baz\"]";
        List<Object> resultList = new ArrayList<>();
        MappingIterator<Object> mappingIterator = createMappingIterator(mapper, json, Object.class);
        mappingIterator.readAll(resultList);
        assertEquals(3, resultList.size());
        mappingIterator.close();
    }

    @org.junit.Test
    public void testReadAllWithCustomCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"foo\", \"bar\", \"baz\"]";
        Collection<Object> results = new ArrayList<>();
        MappingIterator<Object> mappingIterator = createMappingIterator(mapper, json, Object.class);
        mappingIterator.readAll(results);
        assertEquals(3, results.size());
        mappingIterator.close();
    }

    
}
package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class MappingIteratorTest {

    private JsonFactory jsonFactory;
    private ObjectMapper objectMapper;
    private JsonParser jsonParser;

    @Before
    public void setup() throws IOException {
        jsonFactory = new JsonFactory();
        jsonMapper = new ObjectMapper();
        jsonMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        jsonParser = jsonFactory.createParser("[]");
    }

    @Test
    public void testEmptyIterator() {
        MappingIterator<Integer> emptyIterator = MappingIterator.emptyIterator();
        assertFalse(emptyIterator.hasNext());
    }

    @Test
    public void testBasicFunctionality() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[1,2,3]");
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(
                TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class), parser, mapper, null, true, null);

        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(3, iterator.next());
        assertFalse(iterator.hasNext());
        iterator.close();
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoSuchElement() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[1,2,3]");
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(
                TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class), parser, mapper, null, true, null);
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{'foo': 'bar'}");
        MappingIterator<Foo> iterator = new MappingIterator<Foo>(
                TypeFactory.defaultInstance().constructType(Foo.class), parser, mapper, new FooDeserializer(), true, null);

        // Should throw JsonMappingException
        iterator.next();
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{'foo': 'bar'}");
        MappingIterator<Foo> iterator = new MappingIterator<Foo>(
                TypeFactory.defaultInstance().constructType(Foo.class), parser, mapper, new FooDeserializer(), true, null);

        parser.getCodec().getParserConfig().setProperty(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);

        // Should throw IOException
        iterator.next();
        parser.close();
    }

    @Test
    public void testReadAll() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[1,2,3]");
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(
                TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class), parser, mapper, null, true, null);

        List<Integer> result = iterator.readAll(Collections.synchronizedList(new ArrayList<Integer>()));
        assertEquals(3, result.size());
        assertEquals(1, result.get(0));
        assertEquals(2, result.get(1));
        assertEquals(3, result.get(2));
    }

    @Test
    public void testReadAllWithList() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[1,2,3]");
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(
                TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class), parser, mapper, null, true, null);

        List<Integer> result = iterator.readAll(new ArrayList<Integer>());
        assertEquals(3, result.size());
        assertEquals(1, result.get(0));
        assertEquals(2, result.get(1));
        assertEquals(3, result.get(2));
    }

    @Test
    public void testReadAllWithCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[1,2,3]");
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(
                TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class), parser, mapper, null, true, null);

        Collection<Integer> result = iterator.readAll(Collections.synchronizedList(new ArrayList<Integer>()));
        assertEquals(3, result.size());
        assertEquals(1, result.iterator().next());
        assertEquals(2, result.iterator().next());
        assertEquals(3, result.iterator().next());
    }

    @Test
    public void testGetParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[1,2,3]");
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(
                TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class), parser, mapper, null, true, null);

        JsonParser parserFromIterator = iterator.getParser();
        assertNotNull(parserFromIterator);
        assertEquals(parser, parserFromIterator);
    }

    @Test
    public void testGetParserSchema() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[1,2,3]");
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(
                TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class), parser, mapper, null, true, null);

        FormatSchema parserSchema = iterator.getParserSchema();
        assertNotNull(parserSchema);
    }

    @Test
    public void testGetCurrentLocation() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[1,2,3]");
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(
                TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class), parser, mapper, null, true, null);

        JsonLocation currentLocation = iterator.getCurrentLocation();
        assertNotNull(currentLocation);
    }

    @After
    public void cleanUp() throws IOException {
        jsonFactory.close();
        jsonMapper.close();
        if (jsonParser != null) {
            jsonParser.close();
        }
    }

    public static class Foo {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class FooDeserializer extends StdDeserializer<Foo> {
        public FooDeserializer() {
            this(null);
        }

        public FooDeserializer(Class<Foo> t) {
            super(t);
        }

        @Override
        public Foo deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            throw new JsonMappingException("Foo can not be deserialized from JsonParser");
        }
    }
}
package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MappingIteratorTest {

    private ObjectMapper objectMapper;
    private JsonParser jsonParser;
    private ObjectReader objectReader;
    private MappingIterator<String> mappingIterator;

    @Before
    public void setUp() throws IOException {
        objectMapper = new ObjectMapper();
        jsonParser = objectMapper.getFactory().createParser("[\"apple\",\"banana\",\"cherry\"]");
        objectReader = objectMapper.readerForListOf(String.class);
    }

    @After
    public void tearDown() throws IOException {
        if (jsonParser != null) {
            jsonParser.close();
        }
    }

    @Test
    public void testBasicHasNextAndNext() {
        mappingIterator = objectReader.readValues(jsonParser);
        assertTrue(mappingIterator.hasNext());
        assertEquals("apple", mappingIterator.next());
        assertTrue(mappingIterator.hasNext());
        assertEquals("banana", mappingIterator.next());
        assertTrue(mappingIterator.hasNext());
        assertEquals("cherry", mappingIterator.next());
        assertFalse(mappingIterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoElements() {
        mappingIterator = MappingIterator.emptyIterator();
        assertFalse(mappingIterator.hasNext());
        mappingIterator.next();
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidJson() throws IOException {
        jsonParser = objectMapper.getFactory().createParser("invalidJson");
        mappingIterator = objectReader.readValues(jsonParser);
        mappingIterator.next();
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationException() throws IOException {
        JsonDeserializer<String> deserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                throw new IOException("Deserialization Exception");
            }
        };
        mappingIterator = objectReader.readValues(jsonParser, deserializer);
        mappingIterator.next();
    }

    @Test
    public void testReadAll() throws IOException {
        List<String> resultList = new ArrayList<>();
        mappingIterator = objectReader.readValues(jsonParser, resultList);
        while (mappingIterator.hasNext()) {
            mappingIterator.next();
        }
        List<String> expectedList = Arrays.asList("apple", "banana", "cherry");
        assertEquals(expectedList, resultList);
    }

    @Test
    public void testReadAllCollection() throws IOException {
        List<String> resultList = new ArrayList<>();
        mappingIterator = objectReader.readValues(jsonParser, resultList);
        while (mappingIterator.hasNext()) {
            mappingIterator.next();
        }
        Collection<String> expectedCollection = Arrays.asList("apple", "banana", "cherry");
        assertEquals(expectedCollection, resultList);
    }
}
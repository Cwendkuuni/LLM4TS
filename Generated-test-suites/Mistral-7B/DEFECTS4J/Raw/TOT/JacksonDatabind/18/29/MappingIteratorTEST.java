package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class MappingIteratorTest {

    private JsonFactory jsonFactory;
    private ObjectMapper objectMapper;
    private JsonParser jsonParser;

    @Before
    public void setup() throws IOException {
        jsonFactory = new JsonFactory();
        objectMapper = new ObjectMapper();
        jsonParser = jsonFactory.createParser("[]");
    }

    @Test
    public void testEmptyMappingIterator() {
        MappingIterator<Object> emptyIterator = MappingIterator.emptyIterator();
        assertFalse(emptyIterator.hasNext());
        emptyIterator.close();
    }

    @Test
    public void testMappingIteratorWithNullType() {
        JavaType nullType = null;
        try {
            MappingIterator<Object> iterator = new MappingIterator<>(nullType, jsonParser, null, null, false, null);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testMappingIteratorWithNullContext() {
        DeserializationContext context = null;
        try {
            MappingIterator<Object> iterator = new MappingIterator<>(null, jsonParser, context, null, false, null);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testMappingIteratorWithNullDeserializer() {
        JsonDeserializer<Object> deserializer = null;
        try {
            MappingIterator<Object> iterator = new MappingIterator<>(null, jsonParser, null, deserializer, false, null);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testMappingIteratorWithNullParser() {
        try {
            MappingIterator<Object> iterator = new MappingIterator<>(null, null, null, null, false, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testMappingIteratorWithNullClosableParser() {
        try {
            MappingIterator<Object> iterator = new MappingIterator<>(null, null, null, null, false, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testMappingIteratorWithValidConstructor() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper, null, true, null);
        // test basic functionality
        assertTrue(iterator.hasNext());
        assertEquals("", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("", iterator.next());
        // test reading all elements
        List<String> resultList = new ArrayList<>();
        iterator.readAll(resultList);
        assertEquals(2, resultList.size());
        assertEquals("", resultList.get(0));
        assertEquals("", resultList.get(1));
        // test closing the parser
        iterator.close();
        assertFalse(iterator.hasNext());
        assertFalse(jsonParser.hasNextToken());
    }

    @Test
    public void testMappingIteratorWithClosableParser() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper, null, true, null);
        // test reading all elements
        List<String> resultList = new ArrayList<>();
        iterator.readAll(resultList);
        assertEquals(2, resultList.size());
        assertEquals("", resultList.get(0));
        assertEquals("", resultList.get(1));
        // test closing the parser
        iterator.close();
        assertFalse(iterator.hasNext());
        assertFalse(jsonParser.hasNextToken());
        // test reading more elements after closing the parser
        iterator = new MappingIterator<>(javaType, jsonParser, objectMapper, null, true, null);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMappingIteratorWithNonClosableParser() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonParser jsonParser2 = jsonFactory.createParser("[]");
        MappingIterator<String> iterator = new MappingIterator<>(javaType, jsonParser2, objectMapper, null, false, null);
        // test reading all elements
        List<String> resultList = new ArrayList<>();
        iterator.readAll(resultList);
        assertEquals(2, resultList.size());
        assertEquals("", resultList.get(0));
        assertEquals("", resultList.get(1));
        // test closing the parser
        iterator.close();
        assertFalse(iterator.hasNext());
        assertFalse(jsonParser2.hasNextToken());
        // test reading more elements after closing the parser
        iterator = new MappingIterator<>(javaType, jsonParser2, objectMapper, null, false, null);
        assertTrue(iterator.hasNext());
        assertEquals("", iterator.next());
    }

    @Test
    public void testMappingIteratorWithValueToUpdate() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        String initialValue = "initialValue";
        String updatedValue = "updatedValue";
        MappingIterator<String> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper, null, true, initialValue);
        // test updating value
        iterator.next();
        iterator.next();
        iterator.nextValue();
        iterator.getDeserializer().deserialize(jsonParser, objectMapper, updatedValue);
        assertEquals(updatedValue, iterator.nextValue());
        // test closing the parser
        iterator.close();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMappingIteratorWithInvalidInput() throws IOException {
        JsonParser jsonParserInvalidInput = jsonFactory.createParser("invalidInput");
        try {
            MappingIterator<Object> iterator = new MappingIterator<>(null, jsonParserInvalidInput, null, null, false, null);
            fail("Expected JsonMappingException or IOException");
        } catch (JsonMappingException | IOException e) {
            // expected
        }
    }

    @Test
    public void testMappingIteratorWithEOF() throws IOException {
        JsonParser jsonParserEOF = jsonFactory.createParser("");
        MappingIterator<Object> iterator = new MappingIterator<>(null, jsonParserEOF, null, null, false, null);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMappingIteratorWithEndArray() throws IOException {
        JsonParser jsonParserEndArray = jsonFactory.createParser("[]");
        MappingIterator<Object> iterator = new MappingIterator<>(null, jsonParserEndArray, null, null, false, null);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMappingIteratorWithInvalidDeserializer() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        JsonDeserializer<String> deserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                throw new IOException("Invalid deserializer");
            }
        };
        try {
            MappingIterator<String> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper, deserializer, true, null);
            fail("Expected JsonMappingException or IOException");
        } catch (JsonMappingException | IOException e) {
            // expected
        }
    }
}
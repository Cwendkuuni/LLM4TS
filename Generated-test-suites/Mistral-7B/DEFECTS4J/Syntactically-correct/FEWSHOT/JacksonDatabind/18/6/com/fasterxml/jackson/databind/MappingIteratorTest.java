package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class MappingIteratorTest {

    @Test
    public void testEmptyIterator() {
        MappingIterator<Object> iterator = MappingIterator.emptyIterator();
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
        iterator.close();
    }

    @Test
    public void testHasNextValue() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, jsonParser, null, null, false, null);
        iterator.nextValue(); // consume first value
        assertTrue(iterator.hasNextValue());
        assertFalse(iterator.hasNext());
        iterator.close();
        jsonParser.close();
    }

    @Test
    public void testHasNextValueWithNextThrowException() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, jsonParser, null, null, false, null);
        iterator.nextValue(); // consume first value
        iterator.nextValue(); // consume second value
        assertFalse(iterator.hasNextValue());
        assertFalse(iterator.hasNext());
        try {
            iterator.nextValue();
            fail();
        } catch (NoSuchElementException e) {
            // expected
        }
        iterator.close();
        jsonParser.close();
    }

    @Test
    public void testHasNextValueWithDeserializeThrowException() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, jsonParser, null, null, false, null);
        iterator.nextValue(); // consume first value
        JsonDeserializer<Object> deserializer = objectMapper.deserializerForClass(String.class);
        iterator._deserializer = deserializer;
        try {
            iterator.nextValue();
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
        iterator.close();
        jsonParser.close();
    }

    @Test
    public void testHasNextValueWithDeserializeThrowIOException() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, jsonParser, null, null, false, null);
        iterator.nextValue(); // consume first value
        JsonDeserializer<Object> deserializer = objectMapper.deserializerForClass(String.class);
        iterator._deserializer = deserializer;
        try {
            iterator.nextValue();
            fail();
        } catch (IOException e) {
            // expected
        }
        iterator.close();
        jsonParser.close();
    }

    @Test
    public void testNextValue() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, jsonParser, null, null, false, null);
        iterator.nextValue(); // consume first value
        Object firstValue = iterator.nextValue();
        assertEquals(null, firstValue);
        iterator.nextValue(); // consume second value
        Object secondValue = iterator.nextValue();
        assertEquals(null, secondValue);
        iterator.close();
        jsonParser.close();
    }

    @Test
    public void testNextValueWithDeserialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("['foo', 'bar']");
        MappingIterator<String> iterator = new MappingIterator<String>(null, jsonParser, null, null, false, null);
        String firstValue = iterator.nextValue();
        assertEquals("foo", firstValue);
        String secondValue = iterator.nextValue();
        assertEquals("bar", secondValue);
        iterator.close();
        jsonParser.close();
    }

    @Test
    public void testNextValueWithDeserializeAndCloseParser() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("['foo', 'bar']");
        MappingIterator<String> iterator = new MappingIterator<String>(null, jsonParser, null, null, true, null);
        String firstValue = iterator.nextValue();
        assertEquals("foo", firstValue);
        String secondValue = iterator.nextValue();
        assertEquals("bar", secondValue);
        iterator.close();
        JsonParser actualJsonParser = objectMapper.getFactory().createParser("['foo', 'bar']");
        assertNotEquals(jsonParser, actualJsonParser);
        jsonParser.close();
    }

    @Test
    public void testNextValueWithDeserializeNullInput() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("");
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, jsonParser, null, null, false, null);
        iterator.nextValue(); // consume first value
        Object firstValue = iterator.nextValue();
        assertNull(firstValue);
        iterator.close();
        jsonParser.close();
    }

    @Test
    public void testReadAll() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("['foo', 'bar']");
        MappingIterator<String> iterator = new MappingIterator<String>(null, jsonParser, null, null, false, null);
        List<String> resultList = new ArrayList<>();
        List<String> expectedList = Collections.singletonList("foo");
        iterator.readAll(resultList);
        assertEquals(expectedList, resultList);
        resultList.clear();
        expectedList = Collections.singletonList("bar");
        iterator.nextValue();
        iterator.readAll(resultList);
        assertEquals(expectedList, resultList);
        resultList.clear();
        iterator.close();
        jsonParser.close();
    }

    @Test
    public void testReadAllWithNullInput() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("['foo', 'bar']");
        MappingIterator<String> iterator = new MappingIterator<String>(null, jsonParser, null, null, false, null);
        List<String> resultList = null;
        List<String> expectedList = Collections.singletonList("foo");
        iterator.readAll(resultList);
        assertNull(resultList);
        resultList = new ArrayList<>();
        iterator.nextValue();
        iterator.readAll(resultList);
        assertEquals(Collections.singletonList(null), resultList);
        resultList.clear();
        iterator.close();
        jsonParser.close();
    }

    @Test
    public void testReadAllWithNullInputAndDeserialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("['foo', 'bar']");
        MappingIterator<String> iterator = new MappingIterator<String>(null, jsonParser, null, null, false, null);
        List<String> resultList = null;
        List<String> expectedList = Collections.singletonList("foo");
        iterator.readAll(resultList);
        assertNull(resultList);
        resultList = new ArrayList<>();
        iterator.nextValue();
        JsonDeserializer<String> deserializer = objectMapper.deserializerForClass(String.class);
        iterator._deserializer = deserializer;
        iterator.readAll(resultList);
        assertEquals(Collections.singletonList(null), resultList);
        resultList.clear();
        iterator.close();
        jsonParser.close();
    }

    @Test
    public void testReadAllWithEmptyListAsInput() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("['foo', 'bar']");
        MappingIterator<String> iterator = new MappingIterator<String>(null, jsonParser, null, null, false, null);
        List<String> resultList = Collections.emptyList();
        List<String> expectedList = Collections.emptyList();
        iterator.readAll(resultList);
        assertEquals(expectedList, resultList);
        resultList.clear();
        iterator.nextValue();
        iterator.readAll(resultList);
        assertEquals(Collections.singletonList(null), resultList);
        resultList.clear();
        iterator.close();
        jsonParser.close();
    }

    @Test
    public void testReadAllWithCollectionInput() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("['foo', 'bar']");
        MappingIterator<String> iterator = new MappingIterator<String>(null, jsonParser, null, null, false, null);
        List<String> resultList = new ArrayList<>();
        List<String> expectedList = Collections.singletonList("foo");
        iterator.readAll(resultList);
        assertEquals(expectedList, resultList);
        resultList.clear();
        expectedList = new ArrayList<>();
        expectedList.add("foo");
        expectedList.add("bar");
        iterator.nextValue();
        iterator.readAll(resultList);
        assertEquals(expectedList, resultList);
        resultList.clear();
        iterator.close();
        jsonParser.close();
    }

    @Test
    public void testReadAllWithCollectionInputAndDeserialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("['foo', 'bar']");
        MappingIterator<String> iterator = new MappingIterator<String>(null, jsonParser, null, null, false, null);
        List<String> resultList = new ArrayList<>();
        List<String> expectedList = Collections.singletonList("foo");
        iterator.readAll(resultList);
        assertEquals(expectedList, resultList);
        resultList.clear();
        JsonDeserializer<List<String>> deserializer = objectMapper.deserializerForClass(List.class);
        deserializer = objectMapper.builder().defaultTyping(DefaultTyping.NON_FINAL, JsonType.ARRAY_TYPE).build();
        iterator._deserializer = deserializer;
        iterator.nextValue();
        iterator.readAll(resultList);
        List<String> actualList = new ArrayList<>();
        actualList.add("foo");
        actualList.add("bar");
        assertEquals(actualList, resultList);
        resultList.clear();
        iterator.close();
        jsonParser.close();
    }

    @Test
    public void testGetParser() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("['foo', 'bar']");
        MappingIterator<String> iterator = new MappingIterator<String>(null, jsonParser, null, null, false, null);
        JsonParser actualParser = iterator.getParser();
        assertEquals(jsonParser, actualParser);
        iterator.nextValue();
        actualParser = iterator.getParser();
        assertEquals(null, actualParser);
        iterator.close();
        jsonParser.close();
    }

    @Test
    public void testGetParserSchema() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("['foo', 'bar']");
        MappingIterator<String> iterator = new MappingIterator<String>(null, jsonParser, null, null, false, null);
        JsonParser actualParser = iterator.getParser();
        FormatSchema actualParserSchema = actualParser.getSchema();
        JsonParser jsonParser2 = objectMapper.getFactory().createParser("{'foo': {'type': 'number'}, 'bar': {'type': 'string'}}");
        JsonParser expectedParser = jsonParser2;
        FormatSchema expectedParserSchema = expectedParser.getSchema();
        iterator._parser = jsonParser2;
        FormatSchema iteratorParserSchema = iterator.getParserSchema();
        assertEquals(expectedParserSchema, iteratorParserSchema);
        iterator.close();
        jsonParser.close();
        jsonParser2.close();
    }

    @Test
    public void testGetCurrentLocation() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("['foo', 'bar']");
        MappingIterator<String> iterator = new MappingIterator<String>(null, jsonParser, null, null, false, null);
        JsonLocation actualLocation = iterator.getCurrentLocation();
        JsonLocation expectedLocation = jsonParser.getCurrentLocation();
        assertEquals(expectedLocation.getLineNr(), actualLocation.getLineNr());
        assertEquals(expectedLocation.getColumnNr(), actualLocation.getColumnNr());
        iterator.nextValue();
        actualLocation = iterator.getCurrentLocation();
        jsonParser.nextToken();
        assertEquals(expectedLocation.getLineNr(), actualLocation.getLineNr());
        assertEquals(expectedLocation.getColumnNr(), actualLocation.getColumnNr());
        iterator.close();
        jsonParser.close();
    }
}
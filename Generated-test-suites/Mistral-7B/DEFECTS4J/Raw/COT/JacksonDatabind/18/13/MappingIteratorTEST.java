package com.fasterxml.jackson.databind.test; // Assuming you have a test package for this project

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPreprendableByteArrayBuilder;
import com.fasterxml.jackson.core.util.DefaultPrependableCharArrayBuilder;
import com.fasterxml.jackson.core.util.Version;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class MappingIteratorTest {

    private JavaType javaType;
    private DeserializationContext context;
    private JsonDeserializer<String> deserializer;
    private MappingIterator<String> mappingIterator;

    @Before
    public void setUp() throws JsonMappingException, IOException {
        ObjectNode factoryNode = ObjectMapperFactory.getJsonObjectMapper().createObjectNode();
        ObjectMapper mapper = ObjectMapperFactory.getJsonObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        jsonFactory.setCodecVersion(Version.VERSION_2_10_1);

        javaType = mapper.getTypeFactory().constructParametricType(MappingIterator.class, String.class);
        context = mapper.getDeserializationContext();
        deserializer = new StdDeserializer<String>(String.class);
    }

    @Test
    public void testHasNext() {
        String[] input = {"one", "two", "three"};
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(getJsonString(input).getBytes()));

        mappingIterator = new MappingIterator<>(javaType, jsonParser, context, deserializer, true, null);

        assertTrue(mappingIterator.hasNext());
        assertTrue(mappingIterator.hasNext());
        assertTrue(mappingIterator.hasNext());
        assertFalse(mappingIterator.hasNext());
    }

    @Test(expected = JsonMappingException.class)
    public void testHasNextWithMappingException() throws JsonProcessingException, JsonMappingException, IOException {
        String[] input = {"one", "two", "three"};
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(getJsonString(input).getBytes()));

        mappingIterator = new MappingIterator<>(javaType, jsonParser, context, deserializer, true, null);

        mappingIterator.next(); // consume first value

        mappingIterator.hasNext(); // should throw JsonMappingException
    }

    @Test(expected = IOException.class)
    public void testHasNextWithIOException() throws JsonProcessingException, JsonMappingException, IOException {
        String[] input = {"one", "two", "three"};
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(getJsonString(input).getBytes()));

        mappingIterator = new MappingIterator<>(javaType, jsonParser, context, deserializer, true, null);
        mappingIterator.next(); // consume first value

        jsonParser.close(); // close parser to simulate IOException

        mappingIterator.hasNext(); // should throw IOException
    }

    @Test
    public void testNext() {
        String[] input = {"one", "two", "three"};
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(getJsonString(input).getBytes()));

        mappingIterator = new MappingIterator<>(javaType, jsonParser, context, deserializer, true, null);

        assertEquals("one", mappingIterator.next());
        assertEquals("two", mappingIterator.next());
        assertEquals("three", mappingIterator.next());
        assertNull(mappingIterator.next());
    }

    @Test(expected = JsonMappingException.class)
    public void testNextWithMappingException() throws JsonProcessingException, JsonMappingException, IOException {
        String[] input = {"one", "two", "three"};
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(getJsonString(input).getBytes()));

        mappingIterator = new MappingIterator<>(javaType, jsonParser, context, deserializer, true, null);

        mappingIterator.next(); // consume first value

        mappingIterator.next(); // should throw JsonMappingException
    }

    @Test(expected = IOException.class)
    public void testNextWithIOException() throws JsonProcessingException, JsonMappingException, IOException {
        String[] input = {"one", "two", "three"};
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(getJsonString(input).getBytes()));

        mappingIterator = new MappingIterator<>(javaType, jsonParser, context, deserializer, true, null);

        mappingIterator.next(); // consume first value

        jsonParser.close(); // close parser to simulate IOException

        mappingIterator.next(); // should throw IOException
    }

    @Test
    public void testRemove() {
        String[] input = {"one", "two", "three"};
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(getJsonString(input).getBytes()));

        mappingIterator = new MappingIterator<>(javaType, jsonParser, context, deserializer, true, null);

        mappingIterator.next(); // consume first value

        mappingIterator.remove();

        assertEquals("two", mappingIterator.next());
        assertEquals("three", mappingIterator.next());
        assertNull(mappingIterator.next());
    }

    @Test
    public void testClose() throws IOException {
        String[] input = {"one", "two", "three"};
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(getJsonString(input).getBytes()));

        mappingIterator = new MappingIterator<>(javaType, jsonParser, context, deserializer, true, null);

        mappingIterator.next(); // consume first value

        mappingIterator.close();

        assertFalse(mappingIterator.hasNext());
    }

    @Test
    public void testHasNextValue() {
        String[] input = {"one", "two", "three"};
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(getJsonString(input).getBytes()));

        mappingIterator = new MappingIterator<>(javaType, jsonParser, context, deserializer, true, null);

        assertTrue(mappingIterator.hasNextValue());
        assertTrue(mappingIterator.hasNextValue());
        assertTrue(mappingIterator.hasNextValue());
        assertFalse(mappingIterator.hasNextValue());
    }

    @Test(expected = IOException.class)
    public void testHasNextValueWithIOException() throws JsonProcessingException, JsonMappingException, IOException {
        String[] input = {"one", "two", "three"};
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(getJsonString(input).getBytes()));

        mappingIterator = new MappingIterator<>(javaType, jsonParser, context, deserializer, true, null);

        mappingIterator.next(); // consume first value

        jsonParser.close(); // close parser to simulate IOException

        mappingIterator.hasNextValue(); // should throw IOException
    }

    @Test
    public void testNextValue() {
        String[] input = {"one", "two", "three"};
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(getJsonString(input).getBytes()));

        mappingIterator = new MappingIterator<>(javaType, jsonParser, context, deserializer, true, null);

        assertEquals("one", mappingIterator.nextValue());
        assertEquals("two", mappingIterator.nextValue());
        assertEquals("three", mappingIterator.nextValue());
        assertNull(mappingIterator.nextValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testNextValueWithMappingException() throws JsonProcessingException, JsonMappingException, IOException {
        String[] input = {"one", "two", "three"};
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(getJsonString(input).getBytes()));

        mappingIterator = new MappingIterator<>(javaType, jsonParser, context, deserializer, true, null);

        mappingIterator.next(); // consume first value

        mappingIterator.nextValue(); // should throw JsonMappingException
    }

    @Test(expected = IOException.class)
    public void testNextValueWithIOException() throws JsonProcessingException, JsonMappingException, IOException {
        String[] input = {"one", "two", "three"};
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(getJsonString(input).getBytes()));

        mappingIterator = new MappingIterator<>(javaType, jsonParser, context, deserializer, true, null);

        mappingIterator.next(); // consume first value

        jsonParser.close(); // close parser to simulate IOException

        mappingIterator.nextValue(); // should throw IOException
    }

    @Test
    public void testReadAll() throws IOException {
        String[] input = {"one", "two", "three"};
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(getJsonString(input).getBytes()));

        mappingIterator = new MappingIterator<>(javaType, jsonParser, context, deserializer, true, null);

        List<String> list = mappingIterator.readAll();

        assertEquals(3, list.size());
        assertEquals("one", list.get(0));
        assertEquals("two", list.get(1));
        assertEquals("three", list.get(2));
    }

    @Test
    public void testReadAllWithCollectionType() throws IOException {
        String[] input = {"one", "two", "three"};
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(getJsonString(input).getBytes()));

        mappingIterator = new MappingIterator<>(javaType, jsonParser, context, deserializer, true, null);

        ArrayList<String> list = new ArrayList<>();
        mappingIterator.readAll(list);

        assertEquals(3, list.size());
        assertEquals("one", list.get(0));
        assertEquals("two", list.get(1));
        assertEquals("three", list.get(2));
    }

    @Test
    public void testGetParser() {
        String[] input = {"one", "two", "three"};
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(getJsonString(input).getBytes()));

        mappingIterator = new MappingIterator<>(javaType, jsonParser, context, deserializer, true, null);

        assertEquals(jsonParser, mappingIterator.getParser());
    }

    @Test
    public void testGetParserSchema() {
        String[] input = {"one", "two", "three"};
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(getJsonString(input).getBytes()));

        mappingIterator = new MappingIterator<>(javaType, jsonParser, context, deserializer, true, null);

        assertNull(mappingIterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() {
        String[] input = {"one", "two", "three"};
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(getJsonString(input).getBytes()));

        mappingIterator = new MappingIterator<>(javaType, jsonParser, context, deserializer, true, null);

        JsonLocation location = mappingIterator.getCurrentLocation();

        assertEquals(0, location.getLineNumber());
        assertEquals(0, location.getColumnNumber());
    }

    private String getJsonString(Object... values) {
        DefaultPrependableCharArrayBuilder charBuilder = new DefaultPrependableCharArrayBuilder();
        DefaultPrependableByteArrayBuilder byteBuilder = new DefaultPrependableByteArrayBuilder();
        ObjectWriter writer = ObjectMapperFactory.getJsonObjectMapper().writer();

        try {
            writer.writeValueAsString(values);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return charBuilder.toString();
    }
}
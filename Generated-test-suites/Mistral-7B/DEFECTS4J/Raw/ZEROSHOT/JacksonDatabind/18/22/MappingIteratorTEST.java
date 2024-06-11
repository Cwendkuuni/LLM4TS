package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class MappingIteratorTest {

    private DeserializationContext context;
    private ObjectMapper objectMapper;
    private JsonParser jsonParser;

    @Before
    public void setup() throws IOException {
        context = new TestDeserializationContext();
        objectMapper = new ObjectMapper();
        jsonParser = objectMapper.getFactory().createParser(new ByteArrayInputStream(new byte[0]));
    }

    @After
    public void tearDown() throws IOException {
        jsonParser.close();
    }

    @Test
    public void testEmptyIterator() {
        MappingIterator<Object> emptyIterator = MappingIterator.emptyIterator();

        assertFalse(emptyIterator.hasNext());
    }

    @Test
    public void testHasNextValue() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                context.constructValueType(String.class), jsonParser, context, stringDeserializer, false, null);

        assertTrue(mappingIterator.hasNextValue());
    }

    @Test(expected = IOException.class)
    public void testHasNextValueWithIOException() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                context.constructValueType(String.class), jsonParser, context, stringDeserializer, false, null);

        jsonParser.nextToken(); // Skip START_ARRAY
        mappingIterator.hasNextValue();
        jsonParser.nextToken(); // Expect END_ARRAY
        mappingIterator.hasNextValue();
        mappingParser.nextToken(); // Throw an exception
    }

    @Test(expected = JsonMappingException.class)
    public void testHasNextValueWithMappingException() throws IOException {
        JsonDeserializer<String> stringDeserializer = new FailingStringDeserializer();
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                context.constructValueType(String.class), jsonParser, context, stringDeserializer, false, null);

        mappingIterator.hasNextValue();
    }

    @Test
    public void nextValue() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                context.constructValueType(String.class), jsonParser, context, stringDeserializer, false, null);

        String value = mappingIterator.nextValue();
        assertEquals("", value);
    }

    @Test(expected = IOException.class)
    public void nextValueWithIOException() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                context.constructValueType(String.class), jsonParser, context, stringDeserializer, false, null);

        mappingIterator.nextValue();
        jsonParser.nextToken(); // Expect END_ARRAY
        mappingIterator.nextValue();
        mappingParser.nextToken(); // Throw an exception
    }

    @Test(expected = JsonMappingException.class)
    public void nextValueWithMappingException() throws IOException {
        JsonDeserializer<String> stringDeserializer = new FailingStringDeserializer();
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                context.constructValueType(String.class), jsonParser, context, stringDeserializer, false, null);

        mappingIterator.nextValue();
        mappingIterator.nextValue();
        mappingIterator.nextValue();
    }

    @Test
    public void testReadAll() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                context.constructValueType(String.class), jsonParser, context, stringDeserializer, false, null);

        List<String> strings = mappingIterator.readAll();
        assertEquals(3, strings.size());
        assertEquals("", strings.get(0));
        assertEquals("one", strings.get(1));
        assertEquals("two", strings.get(2));
    }

    @Test
    public void testReadAllWithEmptyList() throws IOException {
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                context.constructValueType(String.class), jsonParser, context, null, false, null);

        List<String> strings = mappingIterator.readAll();
        assertTrue(strings.isEmpty());
    }

    @Test
    public void testReadAllWithCollection() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                context.constructValueType(String.class), jsonParser, context, stringDeserializer, false, null);

        Collection<String> strings = new ArrayList<>();
        mappingIterator.readAll(strings);
        assertEquals(3, strings.size());
        assertEquals("", strings.iterator().next());
        assertEquals("one", strings.iterator().next());
        assertEquals("two", strings.iterator().next());
    }

    @Test
    public void testGetParser() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                context.constructValueType(String.class), jsonParser, context, stringDeserializer, false, null);

        JsonParser parser = mappingIterator.getParser();
        assertSame(jsonParser, parser);
    }

    @Test
    public void testGetParserSchema() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                context.constructValueType(String.class), jsonParser, context, stringDeserializer, false, null);

        FormatSchema schema = mappingIterator.getParserSchema();
        assertNull(schema);
    }

    @Test
    public void testGetCurrentLocation() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                context.constructValueType(String.class), jsonParser, context, stringDeserializer, false, null);

        JsonLocation location = mappingIterator.getCurrentLocation();
        assertSame(jsonParser.getCurrentLocation(), location);
    }
}

class FailingStringDeserializer extends StdDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        throw new JsonMappingException("Test MappingIterator deserialization failure");
    }
}

class StringDeserializer extends StdDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        return value == null ? "" : value;
    }
}

class TestDeserializationContext extends DeserializationContext {
    @Override
    public JsonFactory _rootValueFactory() {
        return objectMapper.getFactory();
    }

    @Override
    public ObjectFactory createContextualObjectFactory() {
        return null;
    }

    @Override
    public DeserializationConfig getConfig() {
        return null;
    }

    @Override
    public JsonParser _currentLocationParser() {
        return jsonParser;
    }
}
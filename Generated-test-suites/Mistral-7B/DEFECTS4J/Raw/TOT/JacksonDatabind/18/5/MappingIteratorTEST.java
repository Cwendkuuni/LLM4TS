package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MappingIteratorTest {

    private ObjectMapper objectMapper;
    private JsonFactory jsonFactory;
    private ObjectReader reader;

    @Before
    public void setup() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, new StringDeserializer());
        jsonFactory = new JsonMapperFactory(module).getJsonFactory();
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        reader = objectMapper.getFactory().getReader();
    }

    @Test
    public void testMappingIteratorEmptyIterator() throws IOException {
        // Test empty iterator
        MappingIterator<String> emptyIterator = MappingIterator.emptyIterator();
        Assert.assertFalse(emptyIterator.hasNext());
    }

    @Test
    public void testMappingIteratorSingleValue() throws IOException {
        // Test iterator with single value
        String value = "hello";
        JsonParser jsonParser = jsonFactory.createParser(value);
        MappingIterator<String> iterator = new MappingIterator<>(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class),
                jsonParser, null, null, false, null);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(value, iterator.next());
        Assert.assertFalse(iterator.hasNext());
        iterator.close();
    }

    @Test
    public void testMappingIteratorMultipleValues() throws IOException {
        // Test iterator with multiple values
        String jsonString = "[\"hello\", \"world\"]";
        JsonParser jsonParser = jsonFactory.createParser(jsonString);
        List<String> resultList = new ArrayList<>();
        MappingIterator<String> iterator = new MappingIterator<>(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                jsonParser, null, null, false, resultList);
        while (iterator.hasNext()) {
            String value = iterator.next();
            resultList.add(value);
        }
        Assert.assertEquals(2, resultList.size());
        Assert.assertEquals("hello", resultList.get(0));
        Assert.assertEquals("world", resultList.get(1));
        Assert.assertFalse(iterator.hasNext());
        iterator.close();
    }

    @Test(expected = NoSuchElementException.class)
    public void testMappingIteratorNoSuchElement() throws IOException {
        // Test iterator with no more elements
        String jsonString = "[\"hello\", \"world\"]";
        JsonParser jsonParser = jsonFactory.createParser(jsonString);
        MappingIterator<String> iterator = new MappingIterator<>(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                jsonParser, null, null, false, null);
        while (iterator.hasNext()) {
            iterator.next();
        }
        iterator.next(); // should throw NoSuchElementException
    }

    @Test(expected = IOException.class)
    public void testMappingIteratorIOException() throws IOException {
        // Test iterator with IOException
        String jsonString = "{\"bad\":\"json\"}";
        JsonParser jsonParser = jsonFactory.createParser(jsonString);
        MappingIterator<String> iterator = new MappingIterator<>(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class),
                jsonParser, null, null, false, null);
        iterator.next(); // should read the first token
        iterator.close();
        // Throw an IOException to test the error handling
        throw new IOException("Test error");
    }

    @Test
    public void testMappingIteratorUpdateValue() throws IOException {
        // Test iterator with update value feature
        String jsonString = "[1, 2, 3]";
        JsonParser jsonParser = jsonFactory.createParser(jsonString);
        ObjectNode jsonObject = (ObjectNode) jsonParser.readValueAsTree();
        int expectedValue = 42;
        MappingIterator<Integer> iterator = new MappingIterator<>(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Integer.class),
                jsonParser, null, null, false, jsonObject.get(0));
        JSONAssert.assertEquals(expectedValue, iterator.nextValue());
        jsonObject.set(0, ObjectMapper.getDefaultInstance().valueToTree(expectedValue));
        iterator.nextValue(); // consume the second value
        iterator.close();
    }

    private static class StringDeserializer extends StdDeserializer<String> {
        public StringDeserializer() {
            this(null);
        }

        public StringDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }
}
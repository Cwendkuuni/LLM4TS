package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.ReferenceType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MappingIteratorTest {

    @Test
    public void testMappingIteratorEmpty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        MappingIterator<Object> emptyIterator = MappingIterator.emptyIterator();

        Assert.assertFalse(emptyIterator.hasNext());
        emptyIterator.close();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMappingIteratorRemove() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("values", Collections.singletonList(mapper.valueToTree("testValue")));

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.createParser(rootNode);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader reader = mapper.readerForListOf(String.class);
        MappingIterator<String> iterator = reader.iterator(parser, ctxt);

        iterator.next();
        iterator.remove();
    }

    @Test
    public void testMappingIteratorReadAll() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("values", mapper.valueToTree(Arrays.asList("test1", "test2", "test3")));

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.createParser(rootNode);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader reader = mapper.readerForValueType(ArrayList.class);

        MappingIterator<Object> iterator = reader.iterator(parser, ctxt);
        List<Object> resultList = new ArrayList<>();
        iterator.readAll(resultList);

        Assert.assertEquals(3, resultList.size());
        Assert.assertEquals("test1", resultList.get(0));
        Assert.assertEquals("test2", resultList.get(1));
        Assert.assertEquals("test3", resultList.get(2));

        iterator.close();
    }

    @Test
    public void testMappingIteratorReadAllWithCustomCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("values", mapper.valueToTree(Arrays.asList("test1", "test2", "test3")));

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.createParser(rootNode);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader reader = mapper.readerForValueType(LinkedList.class);

        MappingIterator<Object> iterator = reader.iterator(parser, ctxt);
        LinkedList<Object> resultList = new LinkedList<>();
        iterator.readAll(resultList);

        Assert.assertEquals(3, resultList.size());
        Assert.assertEquals("test1", resultList.getFirst());
        Assert.assertEquals("test2", resultList.get(1));
        Assert.assertEquals("test3", resultList.getLast());

        iterator.close();
    }

    @Test
    public void testMappingIteratorHasNextValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("values", mapper.valueToTree(Arrays.asList("test1", "test2", "test3")));

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.createParser(rootNode);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader reader = mapper.readerForListOf(String.class);

        MappingIterator<String> iterator = reader.iterator(parser, ctxt);

        // First check
        Assert.assertTrue(iterator.hasNextValue());

        // Next call should return the same value
        String firstValue = iterator.nextValue();
        Assert.assertEquals("test1", firstValue);
        Assert.assertTrue(iterator.hasNextValue());

        // Last call should return the last value
        String lastValue = iterator.nextValue();
        Assert.assertEquals("test3", lastValue);
        Assert.assertFalse(iterator.hasNextValue());

        iterator.close();
    }

    @Test(expected = JsonMappingException.class)
    public void testMappingIteratorJsonMappingException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("values", mapper.valueToTree(Collections.singletonList("testValue")));
        rootNode.put("error", mapper.valueToTree("Invalid value"));

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.createParser(rootNode);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader reader = mapper.readerForListOf(String.class);

        MappingIterator<String> iterator = reader.iterator(parser, ctxt);

        // Should throw JsonMappingException
        iterator.nextValue();
    }

    @Test(expected = IOException.class)
    public void testMappingIteratorIOException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("error", mapper.valueToTree("IOException Test"));

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.createParser(rootNode);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader reader = mapper.readerForListOf(String.class);

        MappingIterator<String> iterator = reader.iterator(parser, ctxt);

        // Should throw IOException
        iterator.nextValue();
    }

    @Test
    public void testMappingIteratorClosesParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("values", mapper.valueToTree(Arrays.asList("test1", "test2", "test3")));

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.createParser(rootNode);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader reader = mapper.readerForListOf(String.class);

        MappingIterator<String> iterator = reader.iterator(parser, ctxt, true, null);

        Assert.assertTrue(iterator.hasNextValue());
        String firstValue = iterator.nextValue();
        Assert.assertEquals("test1", firstValue);

        iterator.close();

        // Parser should be closed
        Assert.assertFalse(parser.hasNextToken());
    }

    @Test
    public void testMappingIteratorReadAllWithDeserializedValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("values", mapper.valueToTree(Arrays.asList(
                mapper.valueToTree("testValue1"),
                mapper.valueToTree("testValue2")
        )));

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.createParser(rootNode);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader reader = mapper.readerForListOf(String.class);

        JsonDeserializer<String> deserializer = new JsonStringDeserializer();
        MappingIterator<String> iterator = reader.iterator(parser, ctxt, null, deserializer, true, null);

        List<String> resultList = new ArrayList<>();
        iterator.readAll(resultList);

        Assert.assertEquals(2, resultList.size());
        Assert.assertEquals("testValue1", resultList.get(0));
        Assert.assertEquals("testValue2", resultList.get(1));

        iterator.close();
    }

    @Test
    public void testMappingIteratorReadAllWithDeserializedValueAndCustomValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("values", mapper.valueToTree(Arrays.asList(
                mapper.valueToTree("testValue1"),
                mapper.valueToTree("testValue2")
        )));

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.createParser(rootNode);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader reader = mapper.readerForListOf(String.class);

        String customValue = "custom";

        JsonDeserializer<String> deserializer = new JsonStringDeserializer() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String value = super.deserialize(p, ctxt);
                return customValue + "_" + value;
            }
        };

        MappingIterator<String> iterator = reader.iterator(parser, ctxt, null, deserializer, true, customValue);

        List<String> resultList = new ArrayList<>();
        iterator.readAll(resultList);

        Assert.assertEquals(2, resultList.size());
        Assert.assertEquals("custom_testValue1", resultList.get(0));
        Assert.assertEquals("custom_testValue2", resultList.get(1));

        iterator.close();
    }

    @Test
    public void testMappingIteratorGetParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("values", mapper.valueToTree(Arrays.asList("test1", "test2", "test3")));

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.createParser(rootNode);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader reader = mapper.readerForListOf(String.class);

        MappingIterator<String> iterator = reader.iterator(parser, ctxt);

        JsonParser parserFromIterator = iterator.getParser();

        // Test that both parsers point to the same position
        Assert.assertEquals(parser.getCurrentLocation(), parserFromIterator.getCurrentLocation());

        iterator.nextValue();

        // Test that both parsers point to different positions
        Assert.assertNotEquals(parser.getCurrentLocation(), parserFromIterator.getCurrentLocation());

        iterator.close();
    }

    @Test
    public void testMappingIteratorGetParserSchema() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("schema", mapper.valueToTree(new ObjectMapper().getFactory().getSchemaProvider()
                .getDefaultSchema()));
        rootNode.put("values", mapper.valueToTree(Arrays.asList("test1", "test2", "test3")));

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.createParser(rootNode);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader reader = mapper.readerForListOf(String.class);

        MappingIterator<String> iterator = reader.iterator(parser, ctxt);

        FormatSchema schema = iterator.getParserSchema();

        Assert.assertNotNull(schema);

        iterator.close();
    }

    @Test
    public void testMappingIteratorGetCurrentLocation() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("values", mapper.valueToTree(Arrays.asList("test1", "test2", "test3")));

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.createParser(rootNode);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader reader = mapper.readerForListOf(String.class);

        MappingIterator<String> iterator = reader.iterator(parser, ctxt);

        JsonLocation locationFromIterator = iterator.getCurrentLocation();

        // Test that both locations are equal
        Assert.assertEquals(parser.getCurrentLocation(), locationFromIterator);

        iterator.nextValue();

        // Test that both locations are not equal
        Assert.assertNotEquals(parser.getCurrentLocation(), locationFromIterator);

        iterator.close();
    }

    private static class JsonStringDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String value = p.getValueAsString();
            if (value.equals("null")) {
                return null;
            }
            return value;
        }

        @Override
        public Class<String> handledType() {
            return String.class;
        }
    }
}
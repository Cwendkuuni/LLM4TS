package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;

public class CollectionDeserializerTest {

    private static ObjectMapper mapper;

    @Test
    public void testEmptyArray() throws IOException {
        mapper = new ObjectMapper();
        JsonDeserializer<Collection<Object>> deserializer = mapper.deserializerForClass(Collection.class);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("[]");
        Collection<Object> actual = deserializer.deserialize(parser, null);
        Collection<Object> expected = new ArrayList<>();
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testSingleElementArray() throws IOException {
        mapper = new ObjectMapper();
        JsonDeserializer<Collection<Object>> deserializer = mapper.deserializerForClass(Collection.class);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("[1]");
        Collection<Object> actual = deserializer.deserialize(parser, null);
        Collection<Object> expected = new ArrayList<>();
        expected.add(1);
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testSingleElementArrayWithoutWrapper() throws IOException {
        mapper = new ObjectMapper();
        JsonDeserializer<Collection<Object>> deserializer = mapper.deserializerForClass(Collection.class);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("1");
        Collection<Object> actual = deserializer.deserialize(parser, null);
        Collection<Object> expected = new ArrayList<>();
        expected.add(1);
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testNullValue() throws IOException {
        mapper = new ObjectMapper();
        JsonDeserializer<Collection<Object>> deserializer = mapper.deserializerForClass(Collection.class);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("[]");
        parser.nextToken(); // Consume START_ARRAY token
        parser.nextToken(); // Consume null value
        Collection<Object> actual = deserializer.deserialize(parser, null);
        Collection<Object> expected = new ArrayList<>();
        Assert.assertNull(actual);
        Assert.assertNull(expected);
    }

    @Test
    public void testComplexTypeDeserializer() throws IOException {
        mapper = new ObjectMapper();
        JsonDeserializer<Collection<Object>> deserializer = mapper.deserializerForClass(Collection.class);
        JsonDeserializer<String> stringDeserializer = mapper.deserializerForClass(String.class);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("[\"Hello\", \"World\"]");
        Collection<Object> actual = deserializer.deserialize(parser, null);
        Collection<Object> expected = new ArrayList<>();
        expected.add("Hello");
        expected.add("World");
        Assert.assertEquals(actual, expected);

        TypeDeserializer stringTypeDeserializer = mapper.getTypeDeserializerForAnnotation(String.class);
        actual = deserializer.deserializeWithType(parser, null, stringTypeDeserializer);
        expected = new ArrayList<>();
        expected.add("Hello");
        expected.add("World");
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testReferencing() throws IOException {
        mapper = new ObjectMapper();
        JsonDeserializer<Collection<Object>> deserializer = mapper.deserializerForClass(Collection.class);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("[1, ${ref '2'}]");
        parser.findAndHandleValue("1");
        parser.nextToken(); // Consume START_OBJECT token
        parser.findAndHandleValue("2");
        parser.nextToken(); // Consume START_ARRAY token
        parser.nextToken(); // Consume null value
        parser.nextToken(); // Consume END_ARRAY token
        parser.nextToken(); // Consume END_OBJECT token
        JsonParser innerParser = parser;
        Collection<Object> actual = new ArrayList<>();
        while (innerParser.nextToken() != JsonToken.END_ARRAY) {
            Object value = deserializer.deserialize(innerParser, null);
            actual.add(value);
        }
        Collection<Object> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testInvalidValue() throws IOException {
        mapper = new ObjectMapper();
        JsonDeserializer<Collection<Object>> deserializer = mapper.deserializerForClass(Collection.class);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("invalid");
        try {
            deserializer.deserialize(parser, null);
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals(e.getMessage(), "Cannot deserialize value of type [simple type, class java.util.Collection] from String \"invalid\": Unrecognized token: invalid (note: 'invalid' is not a valid JSON String): unexpected character ('i' (code 105)): was expecting START_OBJECT at [Source: (StringReader); line: 1, column: 1]");
        }
    }

    @Test
    public void testUnresolvedForwardReference() throws IOException {
        mapper = new ObjectMapper();
        JsonDeserializer<Collection<Object>> deserializer = mapper.deserializerForClass(Collection.class);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("[1, ${ref '2'}]");
        CollectionReferringAccumulator accumulator = new CollectionReferringAccumulator(Object.class, new ArrayList<>());
        deserializer.handleUnresolvedForwardReference(new UnresolvedForwardReference("2", Object.class, null), accumulator);
        Object resolvedValue = accumulator.resolveForwardReference("2", new Integer(2));
        Collection<Object> actual = deserializer.deserialize(parser, null);
        Collection<Object> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testArrayOfArrays() throws IOException {
        mapper = new ObjectMapper();
        JsonDeserializer<Collection<Object>> deserializer = mapper.deserializerForClass(Collection.class);
        JsonDeserializer<Collection<Object>> nestedDeserializer = mapper.deserializerForClass(Collection.class);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("[ [1, 2], [3, 4] ]");
        Collection<Object> actual = deserializer.deserialize(parser, null);
        Collection<Object> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>());
        ((List<Collection<Object>>) expected).get(0).add(1);
        ((List<Collection<Object>>) expected).get(0).add(2);
        ((List<Collection<Object>>) expected).get(1).add(3);
        ((List<Collection<Object>>) expected).get(1).add(4);
        Assert.assertEquals(actual, expected);
    }

    static class CollectionReferringAccumulator extends CollectionDeserializer.CollectionReferringAccumulator {

        public CollectionReferringAccumulator(Class<?> elementType, Collection<Object> result) {
            super(elementType, result);
        }

        @Override
        public void resolveForwardReference(Object id, Object value) throws IOException {
            super.resolveForwardReference(id, value);
            _result.clear();
            for (CollectionReferring ref : _accumulator) {
                _result.addAll(ref.next);
            }
        }
    }
}
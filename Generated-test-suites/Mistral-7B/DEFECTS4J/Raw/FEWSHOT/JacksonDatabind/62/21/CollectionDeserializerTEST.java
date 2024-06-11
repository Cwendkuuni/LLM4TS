package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.jsontype.*;

@JacksonStdImpl
public class CollectionDeserializerTest {

    @Test
    public void testCollectionDeserializer() throws IOException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<String> valueDeserializer = new StringDeserializer();
        JsonDeserializer<String> valueTypeDeserializer = new StringDeserializer();
        ValueInstantiator valueInstantiator = new StdValueInstantiator();
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer, valueInstantiator);

        JsonParser parser = new ObjectMapper().getFactory().createParser("[\"foo\",\"bar\",\"baz\"]");
        List<String> result = deserializer.deserialize(parser, new TestDeserializationContext());
        assertEquals(Arrays.asList("foo", "bar", "baz"), result);
    }

    @Test
    public void testCollectionDeserializerWithDelegateDeserializer() throws IOException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<List<String>> delegateDeserializer = new StringListDeserializer();
        JsonDeserializer<String> valueDeserializer = new StringDeserializer();
        JsonDeserializer<String> valueTypeDeserializer = new StringDeserializer();
        ValueInstantiator valueInstantiator = new StdValueInstantiator();
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, null);

        JsonParser parser = new ObjectMapper().getFactory().createParser("[\"foo\",\"bar\",\"baz\"]");
        List<String> result = deserializer.deserialize(parser, new TestDeserializationContext());
        assertEquals(Arrays.asList("foo", "bar", "baz"), result);
    }

    @Test
    public void testCollectionDeserializerWithValueDeserializer() throws IOException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<String> valueDeserializer = new StringDeserializer();
        JsonDeserializer<String> valueTypeDeserializer = new StringDeserializer();
        ValueInstantiator valueInstantiator = new StdValueInstantiator();
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer, valueInstantiator, null, null);

        JsonParser parser = new ObjectMapper().getFactory().createParser("[\"foo\",\"bar\",\"baz\"]");
        List<String> result = deserializer.deserialize(parser, new TestDeserializationContext());
        assertEquals(Arrays.asList("foo", "bar", "baz"), result);

        parser = new ObjectMapper().getFactory().createParser("\"foo\"");
        result = deserializer.deserialize(parser, new TestDeserializationContext());
        assertEquals(Arrays.asList("foo"), result);
    }

    @Test
    public void testCollectionDeserializerWithTypeDeserializer() throws IOException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, Number.class);
        JsonDeserializer<Number> valueDeserializer = new NumberDeserializer();
        JsonDeserializer<Number> valueTypeDeserializer = new NumberDeserializer();
        ValueInstantiator valueInstantiator = new StdValueInstantiator();
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer, valueInstantiator, null, null);

        JsonParser parser = new ObjectMapper().getFactory().createParser("[\"1\",\"2\",\"3\"]");
        List<Number> result = deserializer.deserialize(parser, new TestDeserializationContext());
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializerWithNonArray() throws IOException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<String> valueDeserializer = new StringDeserializer();
        JsonDeserializer<String> valueTypeDeserializer = new StringDeserializer();
        ValueInstantiator valueInstantiator = new StdValueInstantiator();
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer, valueInstantiator);

        JsonParser parser = new ObjectMapper().getFactory().createParser("{\"foo\": \"bar\"}");
        deserializer.deserialize(parser, new TestDeserializationContext(), new ArrayList<>());
        fail();
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializerWithNull() throws IOException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<String> valueDeserializer = new StringDeserializer();
        JsonDeserializer<String> valueTypeDeserializer = new StringDeserializer();
        ValueInstantiator valueInstantiator = new StdValueInstantiator();
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer, valueInstantiator);

        JsonParser parser = new ObjectMapper().getFactory().createParser("[]");
        deserializer.deserialize(parser, new TestDeserializationContext(), null);
        fail();
    }

    @Test
    public void testCollectionDeserializerWithSingleValue() throws IOException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<String> valueDeserializer = new StringDeserializer();
        JsonDeserializer<String> valueTypeDeserializer = new StringDeserializer();
        ValueInstantiator valueInstantiator = new StdValueInstantiator();
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer, valueInstantiator, null, null);

        JsonParser parser = new ObjectMapper().getFactory().createParser("\"foo\"");
        List<String> result = deserializer.deserialize(parser, new TestDeserializationContext(), new ArrayList<>());
        assertEquals(Arrays.asList("foo"), result);
    }

    @Test
    public void testCollectionDeserializerWithUnwrappedSingleValue() throws IOException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<String> valueDeserializer = new StringDeserializer();
        JsonDeserializer<String> valueTypeDeserializer = new StringDeserializer();
        ValueInstantiator valueInstantiator = new StdValueInstantiator();
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer, valueInstantiator, null, Boolean.TRUE);

        JsonParser parser = new ObjectMapper().getFactory().createParser("\"foo\"");
        List<String> result = deserializer.deserialize(parser, new TestDeserializationContext(), new ArrayList<>());
        assertEquals(Arrays.asList("foo"), result);

        parser = new ObjectMapper().getFactory().createParser("[\"foo\"]");
        result = deserializer.deserialize(parser, new TestDeserializationContext(), new ArrayList<>());
        assertEquals(Arrays.asList("foo"), result);

        parser = new ObjectMapper().getFactory().createParser("{\"foo\": \"bar\"}");
        result = deserializer.deserialize(parser, new TestDeserializationContext(), new ArrayList<>());
        assertEquals(Arrays.asList("foo"), result);
    }

    @Test
    public void testCollectionDeserializerWithReferringObjects() throws IOException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<String> valueDeserializer = new StringDeserializer();
        JsonDeserializer<String> valueTypeDeserializer = new StringDeserializer();
        ValueInstantiator valueInstantiator = new StdValueInstantiator();
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer, valueInstantiator);

        JsonParser parser = new ObjectMapper().getFactory().createParser("[]");
        List<String> result = deserializer.deserialize(parser, new TestDeserializationContext(), new ArrayList<>());
        assertEquals(new ArrayList<>(), result);

        JsonParser parser2 = new ObjectMapper().getFactory().createParser("[\"foo\",\"bar\"]");
        List<String> result2 = deserializer.deserialize(parser2, new TestDeserializationContext(), new ArrayList<>());
        assertEquals(Arrays.asList("foo", "bar"), result2);

        JsonParser parser3 = new ObjectMapper().getFactory().createParser("[{\"id\": 1, \"name\": \"foo\"}, {\"id\": 2, \"name\": \"bar\"}]");
        List<Map<String, Object>> result3 = deserializer.deserialize(parser3, new TestDeserializationContext(), new ArrayList<>());
        assertEquals(Arrays.asList(
                Map.of("id", 1, "name", "foo"),
                Map.of("id", 2, "name", "bar")
        ), result3);

        List<Map<String, Object>> expectedResult4 = new ArrayList<>();
        expectedResult4.add(Map.of("id", 1, "name", "foo"));
        expectedResult4.add(Map.of("id", 2, "name", "bar"));
        expectedResult4.add(Map.of("id", 3, "name", "baz"));

        JsonParser parser4 = new ObjectMapper().getFactory().createParser("[{\"id\": 1, \"name\": \"foo\"}, {\"id\": 2, \"name\": \"bar\"}, {\"id\": 3}]");
        List<Map<String, Object>> result4 = deserializer.deserialize(parser4, new TestDeserializationContext(), new ArrayList<>());
        assertEquals(expectedResult4, result4);
    }
}

class StringDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.getValueAsString();
    }
}

class NumberDeserializer extends JsonDeserializer<Number> {
    @Override
    public Number deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return NumberUtils.parseNumber(p.getValueAsString(), Number.class);
    }
}

class TestDeserializationContext extends DeserializationContext {
}
package com.example.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CollectionDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testEmptyCollectionDeserialization() throws IOException {
        String json = "[]";
        Collection<Object> result = objectMapper.readValue(json, CollectionDeserializer.class);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testSimpleCollectionDeserialization() throws IOException {
        String json = "[1, 2, 3]";
        Collection<Object> result = objectMapper.readValue(json, CollectionDeserializer.class);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1, result.iterator().next());
        assertEquals(2, result.iterator().next());
        assertEquals(3, result.iterator().next());
    }

    @Test
    public void testNullCollectionDeserialization() throws IOException {
        String json = "null";
        Collection<Object> result = objectMapper.readValue(json, CollectionDeserializer.class);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test(expected = JsonMappingException.class)
    public void testNonArrayDeserialization() throws IOException {
        String json = "1";
        objectMapper.readValue(json, CollectionDeserializer.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingValueDeserializer() throws IOException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, null, null, null, null, null);
        String json = "[1, 2, 3]";
        deserializer.deserialize(objectMapper.getFactory().createParser(json), null);
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingTypeDeserializer() throws IOException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, null, null, null, null, null);
        TypeDeserializer valueTypeDeserializer = objectMapper.getTypeFactory().constructValueTypeDeserializer(String.class);
        deserializer = deserializer.withResolved(null, null, valueTypeDeserializer, null);
        String json = "[\"hello\", \"world\"]";
        deserializer.deserialize(objectMapper.getFactory().createParser(json), null);
    }

    @Test
    public void testDelegatedCollectionDeserialization() throws IOException {
        String json = "[java.util.ArrayList@123]";
        Collection<Object> result = objectMapper.readValue(json, CollectionDeserializer.class);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.iterator().next());
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        String json = "";
        Collection<Object> result = objectMapper.readValue(json, CollectionDeserializer.class);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testSingleValueDeserialization() throws IOException {
        String json = "1";
        Collection<Object> result = objectMapper.readValue(json, CollectionDeserializer.class);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.iterator().next());
    }

    @Test
    public void testSingleValueDeserializationWithDelegateCreator() throws IOException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public Object createUsingDelegate(DeserializationContext ctxt, BeanProperty property, Object value) throws IOException, JsonProcessingException {
                return new ArrayList<Object>();
            }
        };
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, null, null, valueInstantiator, null, null);
        String json = "1";
        Collection<Object> result = deserializer.deserialize(objectMapper.getFactory().createParser(json), null);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.iterator().next());
    }
}
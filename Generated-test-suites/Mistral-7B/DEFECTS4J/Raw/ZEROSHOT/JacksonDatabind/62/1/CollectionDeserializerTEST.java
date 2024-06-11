package com.example.test;

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

@SuppressWarnings("unchecked")
public class CollectionDeserializerTest {

    @Test
    public void testEmptyCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        CollectionDeserializer deserializer = mapper.findContextualValueDeserializer(Collection.class);
        Collection<Object> collection = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertEquals(Collections.emptyList(), collection);
    }

    @Test
    public void testSingleElementCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"element\"]";
        JsonDeserializer<String> valueDeserializer = mapper.findValueDeserializer(String.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                valueDeserializer, null, null, null, null);
        Collection<Object> collection = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        List<String> list = (List<String>) collection;
        assertEquals(1, list.size());
        assertEquals("element", list.get(0));
    }

    @Test
    public void testCollectionWithMultipleElementsDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"element1\", \"element2\", \"element3\"]";
        JsonDeserializer<String> valueDeserializer = mapper.findValueDeserializer(String.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                valueDeserializer, null, null, null, null);
        Collection<Object> collection = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        List<String> list = (List<String>) collection;
        assertEquals(3, list.size());
        assertEquals("element1", list.get(0));
        assertEquals("element2", list.get(1));
        assertEquals("element3", list.get(2));
    }

    @Test
    public void testCollectionWithNullElementDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[null]";
        JsonDeserializer<Object> nullDeserializer = mapper.findValueDeserializer(Object.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, Object.class),
                nullDeserializer, null, null, null, null);
        Collection<Object> collection = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        List<Object> list = (List<Object>) collection;
        assertEquals(1, list.size());
        assertNull(list.get(0));
    }

    @Test
    public void testCollectionWithCustomTypeDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Integer> intDeserializer = mapper.findValueDeserializer(Integer.class);
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);
        TypeDeserializer intTypeDeserializer = mapper.findValueTypeDeserializer(Integer.class);

        String json = "[1, \"element2\", 3]";
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, Integer.class),
                intDeserializer, intTypeDeserializer, null, null, null);
        Collection<Object> collection = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        List<Integer> list = (List<Integer>) collection;
        assertEquals(3, list.size());
        assertEquals(1, list.get(0).intValue());
        assertEquals(3, list.get(2).intValue());
        assertEquals("element2", list.get(1).toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidJsonCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"invalid\": [\"element\"]}";
        CollectionDeserializer deserializer = mapper.findContextualValueDeserializer(Collection.class);
        deserializer.deserialize(mapper.getFactory().createParser(json), null);
    }

    @Test
    public void testCollectionWithForwardReference() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[{\"$id\":\"1\",\"name\":\"element1\"}, {\"$ref\":\"1\"}]";
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                stringDeserializer, null, null, null, null);
        Collection<Object> collection = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        List<String> list = (List<String>) collection;
        assertEquals(2, list.size());
        assertEquals("element1", list.get(0));
        assertEquals("element1", list.get(1));

        // Resolve forward reference
        Object id = list.get(0);
        String resolvedValue = "resolved_element1";
        deserializer.handleResolvedForwardReference(id, resolvedValue);

        assertEquals(2, list.size());
        assertEquals("element1", list.get(0));
        assertEquals("resolved_element1", list.get(1));
    }
}
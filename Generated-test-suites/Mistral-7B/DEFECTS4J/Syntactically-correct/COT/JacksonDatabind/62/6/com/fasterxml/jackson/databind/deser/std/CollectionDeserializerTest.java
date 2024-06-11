package com.example; // Replace with your test package name

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class CollectionDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCollectionDeserializerEmptyArray() throws IOException {
        String json = "[]";
        Collection<Object> result = objectMapper.readValue(json, CollectionDeserializer.class);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCollectionDeserializerSingleElement() throws IOException {
        String json = "[\"hello\"]";
        Collection<Object> result = objectMapper.readValue(json, CollectionDeserializer.class);
        assertEquals(1, result.size());
        assertTrue(result.contains("hello"));
    }

    @Test
    public void testCollectionDeserializerMultipleElements() throws IOException {
        String json = "[\"hello\", \"world\"]";
        Collection<Object> result = objectMapper.readValue(json, CollectionDeserializer.class);
        assertEquals(2, result.size());
        assertTrue(result.contains("hello"));
        assertTrue(result.contains("world"));
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializerNullValue() throws IOException {
        String json = "[null]";
        CollectionDeserializer deserializer = new CollectionDeserializer(
                JavaType.constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, null);
        Collection<Object> result = deserializer.deserialize(new JsonParser(new StringReader(json)), null);
        fail(); // The test should fail due to JsonMappingException
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializerEmptyString() throws IOException {
        String json = "[\"\"]";
        CollectionDeserializer deserializer = new CollectionDeserializer(
                JavaType.constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, null);
        Collection<Object> result = deserializer.deserialize(new JsonParser(new StringReader(json)), null);
        fail(); // The test should fail due to JsonMappingException
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializerMissingStartArrayToken() throws IOException {
        String json = "{\"hello\":\"world\"}";
        CollectionDeserializer deserializer = new CollectionDeserializer(
                JavaType.constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, null);
        Collection<Object> result = deserializer.deserialize(new JsonParser(new StringReader(json)), null);
        fail(); // The test should fail due to JsonMappingException
    }

    @Test
    public void testCollectionDeserializerWithValueDeserializer() throws IOException {
        String json = "[{\"name\":\"Alice\"}]";
        JsonDeserializer<Person> personDeserializer = new JsonDeserializer<Person>(Person.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(
                JavaType.constructCollectionType(ArrayList.class, Person.class),
                personDeserializer, null, null, null, null);
        Collection<Object> result = deserializer.deserialize(new JsonParser(new StringReader(json)), null);
        assertEquals(1, result.size());
        assertTrue(result.contains(new Person("Alice")));
    }

    @Test
    public void testCollectionDeserializerWithTypeDeserializer() throws IOException {
        JsonDeserializer<Person> personDeserializer = new JsonDeserializer<Person>(Person.class);
        TypeDeserializer personTypeDeserializer = new StdTypeDeserializer(Person.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(
                JavaType.constructCollectionType(ArrayList.class, Person.class),
                personDeserializer, personTypeDeserializer, null, null, null);
        String json = "[{\"name\":\"Alice\",\"age\":18}]";
        Collection<Object> result = deserializer.deserialize(new JsonParser(new StringReader(json)), null);
        assertEquals(1, result.size());
        assertTrue(result.contains(new Person("Alice", 18)));
    }

    @Test
    public void testCollectionDeserializerWithDelegateDeserializer() throws IOException {
        String json = "[\"hello\", \"world\"]";
        JsonDeserializer<String> stringDeserializer = new JsonDeserializer<String>(String.class);
        JsonDeserializer<List<String>> listDeserializer = new CollectionDeserializer(
                JavaType.constructCollectionType(ArrayList.class, String.class),
                null, null, null, stringDeserializer, null);
        Collection<Object> result = listDeserializer.deserialize(new JsonParser(new StringReader(json)), null);
        assertEquals(2, result.size());
        assertTrue(result.contains("hello"));
        assertTrue(result.contains("world"));
    }

    @Test
    public void testCollectionDeserializerWithUnwrappedSingleValue() throws IOException {
        JsonDeserializer<List<String>> listDeserializer = new CollectionDeserializer(
                JavaType.constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, Boolean.TRUE);
        String json = "\"hello\"";
        Collection<Object> result = listDeserializer.deserialize(new JsonParser(new StringReader(json)), null);
        assertEquals(1, result.size());
        assertTrue(result.contains("hello"));
    }

    @Test
    public void testCollectionDeserializerWithAcceptSingleValueAsArray() throws IOException {
        JsonDeserializer<List<String>> listDeserializer = new CollectionDeserializer(
                JavaType.constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, null);
        String json = "\"hello\"";
        Collection<Object> result = listDeserializer.deserialize(new JsonParser(new StringReader(json)), null);
        assertEquals(1, result.size());
        assertTrue(result.contains("hello"));
    }

    @Test
    public void testCollectionDeserializerWithReferencing() throws IOException {
        JsonDeserializer<Person> personDeserializer = new JsonDeserializer<Person>(Person.class);
        TypeDeserializer personTypeDeserializer = new StdTypeDeserializer(Person.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(
                JavaType.constructCollectionType(ArrayList.class, Person.class),
                personDeserializer, personTypeDeserializer, null, null, null);
        String json = "[{\"name\":\"Alice\",\"id\":\"1\"},{\"name\":\"Bob\",\"id\":\"2\"},{\"name\":\"Alice\",\"id\":\"1\"}]";
        Collection<Object> result = deserializer.deserialize(new JsonParser(new StringReader(json)), null);
        assertEquals(2, result.size());
        List<Object> list = (List<Object>) result;
        Person alice = list.get(0);
        assertEquals("Alice", alice.getName());
        assertEquals(1, alice.getId());
        Person bob = list.get(1);
        assertEquals("Bob", bob.getName());
        assertEquals(2, bob.getId());
        // The test should not throw JsonMappingException when resolving forward reference
        deserializer.resolveForwardReference("1", new Person("Alice", 1));
    }
}

class Person {
    private final String name;
    private final int id;

    public Person(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
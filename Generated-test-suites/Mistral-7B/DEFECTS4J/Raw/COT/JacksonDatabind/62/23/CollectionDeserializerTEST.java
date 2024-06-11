package com.example.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.type.TypeReference;
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
    public void testCollectionDeserializer() throws IOException {
        JavaType javaType = new TypeReference<Collection<Integer>>() {}.getType();
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(
                javaType, null, null, null, null, null);

        Collection<Integer> collection = collectionDeserializer.deserialize(
                objectMapper.getFactory().createParser("[]"), objectMapper.getDeserializationContext());
        assertEquals(Collections.emptyList(), collection);
    }

    @Test
    public void testSingleElementArrayDeserialization() throws IOException {
        JavaType javaType = new TypeReference<Collection<Integer>>() {}.getType();
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(
                javaType, null, null, null, null, null);

        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("1");
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode.toString());

        Collection<Integer> collection = collectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(1, collection.size());
        assertEquals(1, collection.get(0).intValue());
    }

    @Test
    public void testDeserializeWithValueDeserializer() throws IOException {
        JavaType javaType = new TypeReference<Collection<String>>() {}.getType();

        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(
                javaType, stringDeserializer, null, null, null, null);

        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("elements", "[\"apple\", \"banana\"]");
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode.toString());

        Collection<String> collection = collectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(2, collection.size());
        assertEquals("apple", collection.get(0));
        assertEquals("banana", collection.get(1));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNullValueDeserializer() throws IOException {
        JavaType javaType = new TypeReference<Collection<String>>() {}.getType();

        JsonDeserializer<String> stringDeserializer = null;
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(
                javaType, null, null, null, null, null);

        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("elements", "[null]");
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode.toString());

        collectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithTypeDeserializer() throws IOException {
        JavaType javaType = new TypeReference<Collection<Person>>() {}.getType();
        TypeDeserializer personTypeDeserializer = new PersonTypeDeserializer();

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(
                javaType, null, personTypeDeserializer, null, null, null);

        ObjectNode jsonNode = objectMapper.createObjectNode();
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode.toString());

        jsonParser.nextToken(); // Consume [
        jsonParser.nextToken(); // Consume null as type id
        jsonParser.nextToken(); // Consume {

        jsonParser.nextToken(); // Consume "name"
        jsonParser.nextToken(); // Consume ": "
        jsonParser.nextToken(); // Consume String
        jsonParser.nextToken(); // Consume null
        jsonParser.nextToken(); // Consume "age"
        jsonParser.nextToken(); // Consume ": "
        jsonParser.nextToken(); // Consume Integer
        jsonParser.nextToken(); // Consume null
        jsonParser.nextToken(); // Consume "]"

        collectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeWithTypeDeserializerAndValueDeserializer() throws IOException {
        JavaType javaType = new TypeReference<Collection<Person>>() {}.getType();
        TypeDeserializer personTypeDeserializer = new PersonTypeDeserializer();
        JsonDeserializer<Person> personDeserializer = new PersonDeserializer();

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(
                javaType, null, personTypeDeserializer, null, personDeserializer, null);

        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("elements", "[{},{}]");
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode.toString());

        Collection<Person> collection = collectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(2, collection.size());
        assertNull(collection.get(0).getName());
        assertNull(collection.get(0).getAge());
        assertNull(collection.get(1).getName());
        assertNull(collection.get(1).getAge());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNullValueDeserializerAndTypeDeserializer() throws IOException {
        JavaType javaType = new TypeReference<Collection<Person>>() {}.getType();
        TypeDeserializer personTypeDeserializer = new PersonTypeDeserializer();

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(
                javaType, null, personTypeDeserializer, null, null, null);

        ObjectNode jsonNode = objectMapper.createObjectNode();
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode.toString());

        jsonParser.nextToken(); // Consume [
        jsonParser.nextToken(); // Consume null as type id
        jsonParser.nextToken(); // Consume {
        jsonParser.nextToken(); // Consume "name"
        jsonParser.nextToken(); // Consume ": "
        jsonParser.nextToken(); // Consume null
        jsonParser.nextToken(); // Consume "age"
        jsonParser.nextToken(); // Consume ": "
        jsonParser.nextToken(); // Consume null
        jsonParser.nextToken(); // Consume }
        jsonParser.nextToken(); // Consume null
        jsonParser.nextToken(); // Consume ]

        collectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
    }
}
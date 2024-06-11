package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.*;

@JacksonStdImpl
public class CollectionDeserializerTest {

    @Test
    public void testEmptyCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        Collection<Object> collection = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(0, collection.size());
    }

    @Test
    public void testSingleElementCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"foo\"]";
        Collection<Object> collection = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(1, collection.size());
        assertEquals("foo", collection.iterator().next());
    }

    @Test
    public void testCollectionWithValuesDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        Collection<Object> collection = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(3, collection.size());
        assertEquals(1, collection.iterator().next());
        assertEquals(2, collection.iterator().next());
        assertEquals(3, collection.iterator().next());
    }

    @Test
    public void testCollectionWithValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[{\"name\": \"John\"}]";
        Collection<Object> collection = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(1, collection.size());
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        when(valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn(new Object());
        CollectionDeserializer deserializer = new CollectionDeserializer(
                new TypeFactory().constructCollectionType(List.class, String.class),
                valueDeserializer, null, null, null, null);
        Collection<Object> collectionDeserialized = deserializer.deserialize(mock(JsonParser.class), mock(DeserializationContext.class));
        assertEquals(1, collectionDeserialized.size());
        assertEquals(mock(Object.class), collectionDeserialized.iterator().next());
        verify(valueDeserializer).deserialize(any(JsonParser.class), any(DeserializationContext.class));
    }

    @Test
    public void testCollectionWithValueTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[{\"name\": \"John\", \"age\": 30}]";
        Collection<Object> collection = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(1, collection.size());
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);
        when(valueTypeDeserializer.deserializeTypedFromArray(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn(new Object());
        when(valueDeserializer.getContentDeserializer())
                .thenReturn(valueTypeDeserializer);
        CollectionDeserializer deserializer = new CollectionDeserializer(
                new TypeFactory().constructCollectionType(List.class, Person.class),
                valueDeserializer, valueTypeDeserializer, null, null, null);
        Collection<Object> collectionDeserialized = deserializer.deserialize(mock(JsonParser.class), mock(DeserializationContext.class));
        assertEquals(1, collectionDeserialized.size());
        assertEquals(mock(Person.class), collectionDeserialized.iterator().next());
        verify(valueDeserializer).deserializeTypedFromArray(any(JsonParser.class), any(DeserializationContext.class));
    }

    @Test
    public void testCollectionWithUnresolvedForwardReference() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[{$_ref: \"/foo\"}]";
        Collection<Object> collection = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(1, collection.size());
        Object id = new Object();
        Object value = new Object();
        CollectionDeserializer deserializer = (CollectionDeserializer) collection.iterator().next();
        deserializer.handleNonArray(mock(JsonParser.class), mock(DeserializationContext.class), mock(Collection.class));
        deserializer.resolveForwardReference(id, value);
        assertTrue(collection.contains(value));
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionWithNonArrayJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\": \"foo\"}";
        mapper.readValue(json, CollectionDeserializer.class);
    }
}

class Person {
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
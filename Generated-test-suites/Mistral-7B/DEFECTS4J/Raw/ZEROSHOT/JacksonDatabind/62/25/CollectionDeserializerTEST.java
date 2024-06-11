package com.example.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.*;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CollectionDeserializerTest {

    @Test
    public void testEmptyCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        Collection<Integer> result = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testSingleElementCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1]";
        Collection<Integer> result = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(Collections.singletonList(1), result);
    }

    @Test
    public void testCollectionDeserializationWithValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        JsonDeserializer<Integer> valueDeserializer = new JsonDeserializer<Integer>() {
            @Override
            public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsInt();
            }
        };
        CollectionDeserializer<Collection<Integer>> collectionDeserializer = new CollectionDeserializer(
                TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class),
                valueDeserializer, null, null);
        Collection<Integer> result = mapper.readerForUpdating(new ArrayList<>())
                .readValue(json, collectionDeserializer);
        List<Integer> expected = Arrays.asList(1, 2, 3);
        assertEquals(expected, result);
    }

    @Test
    public void testCollectionDeserializationWithValueDeserializerAndTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        JsonDeserializer<Integer> valueDeserializer = new JsonDeserializer<Integer>() {
            @Override
            public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsInt();
            }
        };
        TypeDeserializer valueTypeDeserializer = new TypeDeserializer() {
            @Override
            public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt, Type DeserializationType)
                    throws IOException {
                return p.getValueAsInt();
            }

            @Override
            public JavaType handleUnknownProperty(DeserializationContext ctxt, PropertyName name) throws IOException {
                return null;
            }
        };
        CollectionDeserializer<Collection<Integer>> collectionDeserializer = new CollectionDeserializer(
                TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class),
                valueDeserializer, valueTypeDeserializer, null);
        Collection<Integer> result = mapper.readerForUpdating(new ArrayList<>())
                .readValue(json, collectionDeserializer);
        List<Integer> expected = Arrays.asList(1, 2, 3);
        assertEquals(expected, result);
    }

    @Test
    public void testCollectionDeserializationWithDelegateDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"Hello\", \"World\"]";
        JsonDeserializer<String> delegateDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsString();
            }
        };
        CollectionDeserializer<Collection<String>> collectionDeserializer = new CollectionDeserializer(
                TypeFactory.defaultInstance().constructCollectionType(List.class, String.class),
                null, null, delegateDeserializer);
        Collection<String> result = mapper.readValue(json, collectionDeserializer);
        List<String> expected = Arrays.asList("Hello", "World");
        assertEquals(expected, result);
    }

    @Test
    public void testCollectionDeserializationWithWrappedSingleValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "42";
        CollectionDeserializer<Collection<Integer>> collectionDeserializer = new CollectionDeserializer(
                TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class),
                null, null, null, null, Boolean.TRUE);
        Collection<Integer> result = mapper.readValue(json, collectionDeserializer);
        assertEquals(Collections.singletonList(42), result);
    }

    @Test
    public void testCollectionDeserializationWithUnresolvedForwardReference() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, $ref]";
        CollectionDeserializer<Collection<Integer>> collectionDeserializer = new CollectionDeserializer(
                TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class),
                null, null, null, null, null);
        Collection<Integer> result = mapper.readValue(json, collectionDeserializer);
        assertEquals(Collections.singletonList(1), result);
        Object nextValue = mapper.readValue("2", JsonDeserializer.None.instance());
        collectionDeserializer.handleUnresolvedReference(new UnresolvedForwardReference(nextValue, null), result);
        assertEquals(Arrays.asList(1, 2), result);
    }

    @Test
    public void testCollectionDeserializationWithValueInstantiator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Integer> valueDeserializer = new JsonDeserializer<Integer>() {
            @Override
            public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsInt();
            }
        };
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public Object createFromString(DeserializationContext ctxt, String valueAsString) throws IOException {
                return Integer.valueOf(valueAsString);
            }
        };
        CollectionDeserializer<Collection<Integer>> collectionDeserializer = new CollectionDeserializer(
                TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class),
                valueDeserializer, null, null, null, null, valueInstantiator);
        String json = "[1, 2, 3]";
        Collection<Integer> result = mapper.readValue(json, collectionDeserializer);
        List<Integer> expected = Arrays.asList(1, 2, 3);
        assertEquals(expected, result);
    }
}
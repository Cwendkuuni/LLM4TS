package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

/**
 * Test cases for {@link CollectionDeserializer}.
 */
public class CollectionDeserializerTest {

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.getFactory().findDeserializer(Collection.class);
        Collection<Object> result = deserializer.deserialize(mapper.getFactory().createParser("[]"), new DeserializationContext());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserializeSingleElementArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.getFactory().findDeserializer(Collection.class);
        JsonNode node = mapper.readTree("['foo']");
        Collection<Object> result = deserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext());
        assertEquals(1, result.size());
        assertEquals("foo", result.iterator().next());
    }

    @Test
    public void testDeserializeWithValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = mapper.getFactory().createValueDeserializer(String.class);
        CollectionDeserializer deserializer = mapper.getFactory().findDeserializer(Collection.class);
        deserializer = deserializer.withResolved(stringDeserializer, null, null, null);
        JsonNode node = mapper.readTree("[\"foo\", \"bar\"]");
        Collection<Object> result = deserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext());
        assertEquals(2, result.size());
        assertEquals("foo", result.iterator().next());
        assertEquals("bar", result.iterator().next());
    }

    @Test
    public void testDeserializeWithValueTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Integer> intDeserializer = mapper.getFactory().createValueDeserializer(Integer.class);
        TypeDeserializer intTypeDeserializer = mapper.getFactory().findTypeDeserializer(Integer[].class);
        CollectionDeserializer deserializer = mapper.getFactory().findDeserializer(Collection.class);
        deserializer = deserializer.withResolved(null, null, intTypeDeserializer, null);
        JsonNode node = mapper.readTree("[1, 2, 3]");
        Collection<Object> result = deserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext());
        assertEquals(3, result.size());
        assertTrue(result.contains(1));
        assertTrue(result.contains(2));
        assertTrue(result.contains(3));
    }

    @Test
    public void testDeserializeWithDelegateDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<List<String>> listDeserializer = mapper.getFactory().findDeserializer(List.class);
        CollectionDeserializer deserializer = mapper.getFactory().findDeserializer(Collection.class);
        deserializer = deserializer.withResolved(null, listDeserializer, null, null);
        JsonNode node = mapper.readTree("[\"foo\", \"bar\"]");
        Collection<Object> result = deserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext());
        assertEquals(2, result.size());
        assertEquals("foo", result.iterator().next());
        assertEquals("bar", result.iterator().next());
    }

    @Test
    public void testDeserializeWithWrapSingleValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = mapper.getFactory().createValueDeserializer(String.class);
        CollectionDeserializer deserializer = mapper.getFactory().findDeserializer(Collection.class);
        deserializer = deserializer.withResolved(stringDeserializer, null, null, Boolean.TRUE);
        JsonNode node = mapper.readTree("\"foo\"");
        Collection<Object> result = deserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext());
        assertEquals(1, result.size());
        assertEquals("foo", result.iterator().next());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithUnresolvableSingleValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = mapper.getFactory().createValueDeserializer(String.class);
        CollectionDeserializer deserializer = mapper.getFactory().findDeserializer(Collection.class);
        deserializer = deserializer.withResolved(stringDeserializer, null, null, null);
        JsonNode node = mapper.readTree("\"foo\"");
        Collection<Object> result = deserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext());
        assertEquals(1, result.size());
        assertEquals("foo", result.iterator().next());
        JsonNode subNode = mapper.readTree("{@type=java.lang.String}");
        result = deserializer.deserialize(mapper.getFactory().createParser(subNode), new DeserializationContext());
        // should throw an exception
    }

    @Test
    public void testDeserializeWithTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = mapper.getFactory().createValueDeserializer(String.class);
        TypeDeserializer stringTypeDeserializer = mapper.getFactory().findTypeDeserializer(String[].class);
        CollectionDeserializer deserializer = mapper.getFactory().findDeserializer(Collection.class);
        deserializer = deserializer.withResolved(stringDeserializer, null, stringTypeDeserializer, null);
        JsonNode node = mapper.readTree("[\"foo\", \"bar\"]");
        Collection<Object> result = deserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext());
        assertEquals(2, result.size());
        assertTrue(result.contains("foo"));
        assertTrue(result.contains("bar"));
    }

    @Test
    public void testDeserializeWithValueDeserializerAndTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = mapper.getFactory().createValueDeserializer(String.class);
        TypeDeserializer stringTypeDeserializer = mapper.getFactory().findTypeDeserializer(String[].class);
        CollectionDeserializer deserializer = mapper.getFactory().findDeserializer(Collection.class);
        deserializer = deserializer.withResolved(stringDeserializer, null, stringTypeDeserializer, null);
        JsonNode node = mapper.readTree("[\"foo\", \"bar\"]");
        Collection<Object> result = deserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext());
        assertEquals(2, result.size());
        assertEquals("foo", result.iterator().next());
        assertEquals("bar", result.iterator().next());
    }
}
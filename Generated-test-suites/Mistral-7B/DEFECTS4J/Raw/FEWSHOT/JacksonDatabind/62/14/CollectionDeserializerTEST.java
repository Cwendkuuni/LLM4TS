package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;
import java.util.*;

@JacksonStdImpl
public class CollectionDeserializer
        extends ContainerDeserializerBase<Collection<Object>>
        implements ContextualDeserializer {

    // Test Cases for CollectionDeserializer

    @Test
    public void testEmptyCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[]");

        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        Collection<String> result = deserializer.deserialize(mapper.getFactory().createParser(jsonNode), null);
        assertEquals(0, result.size());
    }

    @Test
    public void testCollectionDeserializationWithSingleElement() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[\"foo\"]");

        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        Collection<String> result = deserializer.deserialize(mapper.getFactory().createParser(jsonNode), null);
        assertEquals(1, result.size());
        assertEquals("foo", result.get(0));
    }

    @Test
    public void testCollectionDeserializationWithMultipleElements() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[\"foo\", \"bar\", 1, 2.0]");

        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, Object.class),
                null, null, null, null, null);

        Collection<Object> result = deserializer.deserialize(mapper.getFactory().createParser(jsonNode), null);
        assertEquals(4, result.size());
        assertEquals("foo", result.get(0));
        assertEquals("bar", result.get(1));
        assertEquals(1, result.get(2));
        assertEquals(2.0, result.get(3));
    }

    @Test
    public void testCollectionDeserializationWithTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[1, 2, 3, 4]");

        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, Integer.class),
                null, new IntTypeDeserializer(), null, null, null);

        Collection<Integer> result = deserializer.deserialize(mapper.getFactory().createParser(jsonNode), null);
        assertEquals(4, result.size());
        assertEquals(1, result.get(0).intValue());
        assertEquals(2, result.get(1).intValue());
        assertEquals(3, result.get(2).intValue());
        assertEquals(4, result.get(3).intValue());
    }

    @Test
    public void testCollectionDeserializationWithDelegateDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[\"foo\", \"bar\"]");

        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, stringDeserializer, null);

        Collection<String> result = deserializer.deserialize(mapper.getFactory().createParser(jsonNode), null);
        assertEquals(2, result.size());
        assertEquals("foo", result.get(0));
        assertEquals("bar", result.get(1));
    }

    @Test
    public void testCollectionDeserializationWithValueInstantiator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[null, true, false]");

        ValueInstantiator valueInstantiator = new StdValueInstantiator();
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, Object.class),
                null, null, valueInstantiator, null, null);

        Collection<Object> result = deserializer.deserialize(mapper.getFactory().createParser(jsonNode), null);
        assertEquals(3, result.size());
        assertNull(result.get(0));
        assertTrue(result.get(1) instanceof Boolean);
        assertFalse((Boolean) result.get(1));
        assertTrue(result.get(2) instanceof Boolean);
        assertFalse((Boolean) result.get(2));
    }

    private static class IntTypeDeserializer extends TypeDeserializer {
        @Override
        public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            return p.nextIntValue();
        }
    }
}
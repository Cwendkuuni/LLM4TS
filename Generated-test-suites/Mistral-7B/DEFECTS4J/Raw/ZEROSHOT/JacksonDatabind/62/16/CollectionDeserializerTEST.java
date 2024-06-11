package com.example.junit;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class CollectionDeserializerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        JsonParser parser = objectMapper.getFactory().createParser("[ ]");
        CollectionDeserializer deserializer = new CollectionDeserializer(
                JavaType.constructCollectionType(ArrayList.class, Object.class),
                null, null, null, null, null);

        Collection<Object> result = deserializer.deserialize(parser, null, Collections.emptyList());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserializeSingleValueArray() throws IOException {
        JsonParser parser = objectMapper.getFactory().createParser("[1]");
        CollectionDeserializer deserializer = new CollectionDeserializer(
                JavaType.constructCollectionType(ArrayList.class, Integer.class),
                new StandardSerializers.IntegerDeserializer(), null, null, null, null);

        Collection<Integer> result = deserializer.deserialize(parser, null, Collections.emptyList());
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).intValue());
    }

    @Test
    public void testDeserializeArrayWithNullValue() throws IOException {
        JsonParser parser = objectMapper.getFactory().createParser("[null]");
        CollectionDeserializer deserializer = new CollectionDeserializer(
                JavaType.constructCollectionType(ArrayList.class, Object.class),
                null, null, null, null, null);

        Collection<Object> result = deserializer.deserialize(parser, null, Collections.emptyList());
        assertEquals(1, result.size());
        assertNull(result.get(0));
    }

    @Test
    public void testDeserializeArrayWithMultipleValues() throws IOException {
        JsonParser parser = objectMapper.getFactory().createParser("[1, 2, 3]");
        CollectionDeserializer deserializer = new CollectionDeserializer(
                JavaType.constructCollectionType(ArrayList.class, Integer.class),
                new StandardSerializers.IntegerDeserializer(), null, null, null, null);

        Collection<Integer> result = deserializer.deserialize(parser, null, Collections.emptyList());
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).intValue());
        assertEquals(2, result.get(1).intValue());
        assertEquals(3, result.get(2).intValue());
    }

    @Test
    public void testDeserializeArrayWithTypeDeserializer() throws IOException {
        JsonParser parser = objectMapper.getFactory().createParser("[1, 2, 3.5]");
        TypeDeserializer typeDeserializer = new StandardDeserializers.FloatTypeDeserializer();
        CollectionDeserializer deserializer = new CollectionDeserializer(
                JavaType.constructCollectionType(ArrayList.class, Number.class),
                null, typeDeserializer, null, null, null);

        Collection<Number> result = deserializer.deserialize(parser, null, Collections.emptyList());
        assertEquals(3, result.size());
        assertEquals(1.0, result.get(0).floatValue(), 0.0);
        assertEquals(2.0, result.get(1).floatValue(), 0.0);
        assertEquals(3.5f, result.get(2).floatValue(), 0.0);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNonArray() throws IOException {
        JsonParser parser = objectMapper.getFactory().createParser("1");
        CollectionDeserializer deserializer = new CollectionDeserializer(
                JavaType.constructCollectionType(ArrayList.class, Integer.class),
                null, null, null, null, null);

        deserializer.deserialize(parser, null, Collections.emptyList());
        fail("Should have thrown a JsonMappingException");
    }
}
package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.StandardDeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Test for {@link StringCollectionDeserializer} class.
 */
public class StringCollectionDeserializerTest {

    private ObjectMapper objectMapper;
    private StandardDeserializationContext context;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        context = new StandardDeserializationContext(objectMapper.getConfiguration());
    }

    @Test
    public void testEmptyCollectionDeserialization() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ArrayType.construct(Collection.class, String.class), null, null);

        JsonParser parser = objectMapper.getFactory().createParser("[ ]");
        Collection<String> result = deserializer.deserialize(parser, context);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testStringCollectionDeserialization() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ArrayType.construct(Collection.class, String.class), null, null);

        JsonParser parser = objectMapper.getFactory().createParser("[\"apple\", \"banana\", \"cherry\"]");
        Collection<String> result = deserializer.deserialize(parser, context);
        List<String> expected = Arrays.asList("apple", "banana", "cherry");
        assertEquals(expected, result);
    }

    @Test
    public void testNullCollectionDeserialization() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ArrayType.construct(Collection.class, String.class), null, null);

        JsonParser parser = objectMapper.getFactory().createParser("[null]");
        Collection<String> result = deserializer.deserialize(parser, context);
        assertNull(result);
    }

    @Test
    public void testDeserializationWithCustomValueDeserializer() throws IOException {
        JsonDeserializer<String> customValueDeserializer = new StdDeserializer<String>() {};
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ArrayType.construct(Collection.class, String.class), null, null, null, null, null);

        JsonParser parser = objectMapper.getFactory().createParser("[\"apple\", null, \"banana\"]");
        Collection<String> result = deserializer.deserialize(parser, context);
        List<String> expected = Arrays.asList("apple", null, "banana");
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializationWithDelegateDeserializer() throws IOException {
        JsonDeserializer<Object> delegateDeserializer = new StdDeserializer<Object>() {};
        JsonDeserializer<String> valueDeserializer = new StdDeserializer<String>() {};
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ArrayType.construct(Collection.class, String.class), null, delegateDeserializer, null, null, null);

        JsonParser parser = objectMapper.getFactory().createParser("[\"apple\", null, \"banana\"]");
        Object delegate = new Object() {};
        Collection<String> result = deserializer.deserialize(parser, context);
        List<String> expected = Arrays.asList("apple", null, "banana");
        assertEquals(expected, result);
        assertSame(delegate, deserializer.getDelegateDeserializer().deserialize(parser, context));
    }

    @Test
    public void testDeserializationWithTypeDeserializer() throws IOException {
        TypeDeserializer typeDeserializer = new StdTypeDeserializer<Collection<String>>() {};
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ArrayType.construct(Collection.class, String.class), null, null, null, null, typeDeserializer);

        JsonParser parser = objectMapper.getFactory().createParser("[\"apple\", \"banana\", \"cherry\"]");
        Collection<String> result = deserializer.deserializeWithType(parser, context, typeDeserializer);
        List<String> expected = Arrays.asList("apple", "banana", "cherry");
        assertEquals(expected, result);
    }
}
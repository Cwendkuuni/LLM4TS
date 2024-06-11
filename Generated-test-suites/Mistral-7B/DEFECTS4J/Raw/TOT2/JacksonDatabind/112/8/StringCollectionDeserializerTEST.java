package com.example.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StringCollectionDeserializerTest {

    private static ObjectMapper mapper;

    @BeforeClass
    public static void setup() {
        mapper = new ObjectMapper();
    }

    // Test case for the constructor with no arguments
    @Test
    public void testStringCollectionDeserializerEmpty() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class);
        JsonDeserializer<String> valueDeserializer = null;
        ValueInstantiator valueInstantiator = null;
        JsonDeserializer<Object> delegateDeserializer = null;

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(javaType, valueDeserializer, valueInstantiator, delegateDeserializer);

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("[]");

        Collection<String> result = deserializer.deserialize(parser, null);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    // Test case for the constructor with arguments
    @Test
    public void testStringCollectionDeserializerWithArguments() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class);
        JsonDeserializer<String> valueDeserializer = mapper.deserializerFactory.findValueDeserializer(String.class, null);
        ValueInstantiator valueInstantiator = mapper.valueInstantiatorFactory.findValueInstantiator(ArrayList.class, null);
        JsonDeserializer<Object> delegateDeserializer = mapper.deserializerFactory.findDeserializer(Object.class, null);

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(javaType, valueInstantiator, delegateDeserializer, valueDeserializer);

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("[\"test\", \"string\"1, \"test2\"]");

        Collection<String> result = deserializer.deserialize(parser, null);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("test", result.get(0));
        assertEquals("string", result.get(1));
        assertEquals("test2", result.get(2));
    }

    // Test case for deserialization with null provider
    @Test
    public void testStringCollectionDeserializerWithNullProvider() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class);
        JsonDeserializer<String> valueDeserializer = null;
        ValueInstantiator valueInstantiator = null;
        JsonDeserializer<Object> delegateDeserializer = null;
        NullValueProvider nullValueProvider = mapper.nullValueProviderFactory.findNullValueProvider(null, null);

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(javaType, valueDeserializer, valueInstantiator, delegateDeserializer, nullValueProvider);

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("[]");

        Collection<String> result = deserializer.deserialize(parser, null);

        assertNotNull(result);
        assertEquals(0, result.size());
        assertNull(result.get(0));
    }

    // Test case for deserialization with valueDeserializer
    @Test
    public void testStringCollectionDeserializerWithValueDeserializer() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class);
        JsonDeserializer<String> valueDeserializer = mapper.deserializerFactory.findValueDeserializer(String.class, null);
        ValueInstantiator valueInstantiator = null;
        JsonDeserializer<Object> delegateDeserializer = null;

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(javaType, valueInstantiator, delegateDeserializer, valueDeserializer);

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("[\"test\", \"string\", null]");

        Collection<String> result = deserializer.deserialize(parser, null);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("test", result.get(0));
        assertEquals("string", result.get(1));
        assertNull(result.get(2));
    }

    // Test case for deserialization with delegateDeserializer
    @Test
    public void testStringCollectionDeserializerWithDelegateDeserializer() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class);
        JsonDeserializer<String> valueDeserializer = null;
        ValueInstantiator valueInstantiator = null;
        JsonDeserializer<Object> delegateDeserializer = mapper.deserializerFactory.findDeserializer(Object[].class, null);

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(javaType, valueDeserializer, delegateDeserializer);

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("[1, 2, 3]");

        Collection<String> result = deserializer.deserialize(parser, null);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertNull(result.get(0));
        assertNull(result.get(1));
        assertNull(result.get(2));

        Object[] array = result.toArray();
        assertEquals(3, array.length);
        assertNotNull(array[0]);
        assertNotNull(array[1]);
        assertNotNull(array[2]);
    }

    // Test case for deserialization with isCachable() method
    @Test
    public void testStringCollectionDeserializerWithIsCachable() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class);
        JsonDeserializer<String> valueDeserializer = null;
        ValueInstantiator valueInstantiator = null;
        JsonDeserializer<Object> delegateDeserializer = null;

        StringCollectionDeserializer deserializer1 = new StringCollectionDeserializer(javaType, valueDeserializer, valueInstantiator, delegateDeserializer);
        StringCollectionDeserializer deserializer2 = deserializer1.withResolved(delegateDeserializer, valueDeserializer, null, null);

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("[\"test\", \"string\"]");

        Collection<String> result1 = deserializer1.deserialize(parser, null);
        Collection<String> result2 = deserializer2.deserialize(parser, null);

        assertEquals(result1, result2);
    }
}
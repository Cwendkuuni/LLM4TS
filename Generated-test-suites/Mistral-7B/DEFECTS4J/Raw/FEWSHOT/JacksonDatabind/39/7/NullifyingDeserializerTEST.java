package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for NullifyingDeserializer class
 */
public class NullifyingDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserializeNullifyingDeserializerWithBasicJson() throws IOException {
        String json = "{\"name\":\"John\",\"age\":30}";
        StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        Object readValue = mapper.readValue(json, deserializer.getType());
        Assert.assertNull(readValue);
    }

    @Test
    public void testDeserializeNullifyingDeserializerWithEmptyJson() throws IOException {
        String json = "{}";
        StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        Object readValue = mapper.readValue(json, deserializer.getType());
        Assert.assertNull(readValue);
    }

    @Test
    public void testDeserializeNullifyingDeserializerWithArrayJson() throws IOException {
        String json = "[1, 2, 3]";
        StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        Object readValue = mapper.readValue(json, deserializer.getType());
        Assert.assertNull(readValue);
    }

    @Test
    public void testDeserializeNullifyingDeserializerWithNullJson() throws IOException {
        String json = "null";
        StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        Object readValue = mapper.readValue(json, deserializer.getType());
        Assert.assertNull(readValue);
    }

    @Test
    public void testDeserializeNullifyingDeserializerWithCustomToken() throws IOException {
        String json = "{\"token\":\"CUSTOM_TOKEN\"}";
        StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        Object readValue = mapper.readValue(json, deserializer.getType());
        Assert.assertNull(readValue);
    }

    @Test
    public void testDeserializeNullifyingDeserializerWithComplexJson() throws IOException {
        String json = "{\"name\":{\"firstName\":\"John\", \"lastName\":\"Doe\"}, \"age\":30, \"status\":\"active\"}";
        StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        Object readValue = mapper.readValue(json, deserializer.getType());
        Assert.assertNull(readValue);
    }

    @Test
    public void testDeserializeNullifyingDeserializerWithType() throws IOException {
        String json = "{\"name\":\"John\",\"age\":30}";
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(String.class);
        StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        Object readValue = deserializer.deserializeWithType(mapper.parser(), null, typeDeserializer);
        Assert.assertNull(readValue);
    }

    @Test
    public void testDeserializeNullifyingDeserializerWithArrayType() throws IOException {
        String json = "[1, 2, 3]";
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(int[].class);
        StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        Object readValue = deserializer.deserializeWithType(mapper.parser(), null, typeDeserializer);
        Assert.assertNull(readValue);
    }

    @Test
    public void testDeserializeNullifyingDeserializerWithNullType() throws IOException {
        String json = "{\"name\":\"John\",\"age\":30}";
        TypeDeserializer typeDeserializer = null;
        StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        Object readValue = deserializer.deserializeWithType(mapper.parser(), null, typeDeserializer);
        Assert.assertNull(readValue);
    }
}
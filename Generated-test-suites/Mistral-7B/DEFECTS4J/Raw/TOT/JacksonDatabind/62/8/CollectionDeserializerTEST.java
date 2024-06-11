package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;

public class CollectionDeserializerTest {

    private ObjectMapper mapper;
    private CollectionDeserializer deserializer;

    @BeforeMethod
    public void setup() {
        mapper = new ObjectMapper();
        deserializer = mapper.deserializerForClass(Collection.class);
    }

    @Test
    public void testEmptyArrayDeserialization() throws IOException {
        String json = "[]";
        Collection<Object> result = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        Assert.assertEquals(result.size(), 0);
    }

    @Test
    public void testSingleValueDeserialization() throws IOException {
        String json = "1";
        Collection<Object> result = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0), 1);
    }

    @Test
    public void testSingleValueDeserializationWithType() throws IOException, JsonProcessingException {
        String json = "1";
        TypeDeserializer valueTypeDeserializer = mapper.getTypeDeserializerForAnnotation(JsonFormat.Value.class);
        Collection<Integer> result = deserializer.deserializeWithType(mapper.getFactory().createParser(json), null, valueTypeDeserializer);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0), 1);
    }

    @Test
    public void testNullValueDeserialization() throws IOException {
        String json = "null";
        Collection<Object> result = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        Assert.assertEquals(result.size(), 1);
        Assert.assertNull(result.get(0));
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        String json = "\"\"";
        Collection<Object> result = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        Assert.assertEquals(result.size(), 0);
    }

    @Test
    public void testArrayWithNullValuesDeserialization() throws IOException {
        String json = "[null, null]";
        Collection<Object> result = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        Assert.assertEquals(result.size(), 2);
        Assert.assertNull(result.get(0));
        Assert.assertNull(result.get(1));
    }

    @Test
    public void testArrayWithSimpleValuesDeserialization() throws IOException {
        String json = "[1, 2, 3]";
        Collection<Object> result = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        Assert.assertEquals(result.size(), 3);
        Assert.assertEquals(result.get(0), 1);
        Assert.assertEquals(result.get(1), 2);
        Assert.assertEquals(result.get(2), 3);
    }

    @Test
    public void testArrayWithSimpleValuesAndNullDeserializationWithType() throws IOException, JsonProcessingException {
        String json = "[1, 2, null, 3]";
        TypeDeserializer valueTypeDeserializer = mapper.getTypeDeserializerForAnnotation(JsonFormat.Value.class);
        Collection<Integer> result = deserializer.deserializeWithType(mapper.getFactory().createParser(json), null, valueTypeDeserializer);
        Assert.assertEquals(result.size(), 4);
        Assert.assertEquals(result.get(0), 1);
        Assert.assertEquals(result.get(1), 2);
        Assert.assertNull(result.get(2));
        Assert.assertEquals(result.get(3), 3);
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testInvalidJsonDeserialization() throws IOException {
        String json = "1, 2";
        deserializer.deserialize(mapper.getFactory().createParser(json), null);
    }
}
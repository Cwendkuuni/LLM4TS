package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class CollectionDeserializerTest {

    @Test
    public void testEmptyCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        Collection<Integer> result = mapper.readValue(json, new TypeReference<Collection<Integer>>() {});
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testSingleElementCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1]";
        Collection<Integer> result = mapper.readValue(json, new TypeReference<Collection<Integer>>() {});
        Assert.assertEquals(Collections.singletonList(1), result);
    }

    @Test
    public void testMultipleElementCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        Collection<Integer> result = mapper.readValue(json, new TypeReference<Collection<Integer>>() {});
        Assert.assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @Test
    public void testNullCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[null]";
        Collection<Integer> result = mapper.readValue(json, new TypeReference<Collection<Integer>>() {});
        Assert.assertEquals(Collections.singleton(null), result);
    }

    @Test
    public void testStringCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"one\", \"two\", \"three\"]";
        Collection<String> result = mapper.readValue(json, new TypeReference<Collection<String>>() {});
        Assert.assertEquals(Arrays.asList("one", "two", "three"), result);
    }

    @Test
    public void testEmptyStringCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        Collection<String> result = mapper.readValue(json, new TypeReference<Collection<String>>() {});
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testEmptyMapCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[{},{},{}]";
        Collection<Map<String, Object>> result = mapper.readValue(json, new TypeReference<Collection<Map<String, Object>>>() {});
        Assert.assertEquals(new ArrayList<>(), result);
    }

    @Test
    public void testSingleMapElementCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[{\"key\": \"value\"}]";
        Collection<Map<String, Object>> result = mapper.readValue(json, new TypeReference<Collection<Map<String, Object>>>() {});
        Map<String, Object> expected = new HashMap<>();
        expected.put("key", "value");
        Assert.assertEquals(Collections.singletonList(expected), result);
    }

    @Test
    public void testMultipleMapElementCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[{\"key1\": \"value1\"}, {\"key2\": \"value2\"}]";
        Collection<Map<String, Object>> result = mapper.readValue(json, new TypeReference<Collection<Map<String, Object>>>() {});
        Map<String, Object> map1 = new HashMap<>();
        map1.put("key1", "value1");
        Map<String, Object> map2 = new HashMap<>();
        map2.put("key2", "value2");
        Assert.assertEquals(Arrays.asList(map1, map2), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{invalid: json}";
        mapper.readValue(json, new TypeReference<Collection<Integer>>() {});
    }

    @Test(expected = JsonMappingException.class)
    public void testNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[null]";
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.readValue(json, new TypeReference<Collection<String>>() {});
    }

    @Test(expected = JsonMappingException.class)
    public void testNullValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> nullValueDeserializer = null;
        String json = "[null]";
        mapper.setDeserializationConfig(new DeserializationConfig().withValueDeserializer(nullValueDeserializer));
        mapper.readValue(json, new TypeReference<Collection<String>>() {});
    }

    @Test(expected = JsonMappingException.class)
    public void testValueDeserializerFailure() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> failingDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                throw new IOException("Simulated deserialization failure");
            }
        };
        String json = "[\"value\"]";
        mapper.setDeserializationConfig(new DeserializationConfig().withValueDeserializer(failingDeserializer));
        mapper.readValue(json, new TypeReference<Collection<String>>() {});
    }

    @Test(expected = JsonMappingException.class)
    public void testValueTypeDeserializerFailure() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer failingTypeDeserializer = new BaseTypeDeserializer() {
            @Override
            public Object deserializeTypedFromObject(Object bean, JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                throw new IOException("Simulated deserialization failure");
            }
        };
        String json = "[1]";
        mapper.setDeserializationConfig(new DeserializationConfig().withValueTypeDeserializer(failingTypeDeserializer));
        mapper.readValue(json, new TypeReference<Collection<Integer>>() {});
    }
}
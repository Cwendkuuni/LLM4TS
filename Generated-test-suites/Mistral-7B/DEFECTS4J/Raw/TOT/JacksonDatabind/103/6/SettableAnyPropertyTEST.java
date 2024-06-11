package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettableAnyPropertyTest {

    @Test
    public void testSettableAnyProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();

        JavaType javaType = mapper.getTypeFactory().constructType(Map.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(new AnnotatedField(MyPojo.class, "anyMap")),
                new AnnotatedField(MyPojo.class, "anyMap"),
                javaType,
                null,
                mapper.findValueDeserializer(String.class),
                null);

        MyPojo myPojo = new MyPojo();

        JsonParser parser = factory.createParser("{\"anyMap\":[1,\"two\",3,\"four\":4]}");

        settableAnyProperty.deserializeAndSet(parser, mapper.getDeserializationContext(), myPojo, "anyMap");

        Map<Object, Object> actualMap = myPojo.getAnyMap();
        Map<Object, Object> expectedMap = new HashMap<>();
        expectedMap.put(1, 1);
        expectedMap.put("two", "two");
        expectedMap.put(3, 3);
        expectedMap.put("four", 4);

        Assert.assertEquals(actualMap, expectedMap);

        parser = factory.createParser("{\"anyMap\":[null]}");
        settableAnyProperty.deserializeAndSet(parser, mapper.getDeserializationContext(), myPojo, "anyMap");

        actualMap = myPojo.getAnyMap();
        Assert.assertTrue(actualMap.isEmpty());

        parser = factory.createParser("{\"anyMap\":[\"invalid\",null]}");

        try {
            settableAnyProperty.deserializeAndSet(parser, mapper.getDeserializationContext(), myPojo, "anyMap");
            Assert.fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }

        parser = factory.createParser("{\"anyMap\":[{\"key\":1,\"value\":\"one\"}]}");

        try {
            settableAnyProperty.deserializeAndSet(parser, mapper.getDeserializationContext(), myPojo, "anyMap");
            Assert.fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            Assert.assertEquals(e.getCause().getMessage(), "Unrecognized field 'key' (Class MyPojo)");
        }

        parser = factory.createParser("{\"anyMap\":[{\"key\":1,\"value\":[1,\"two\"]}]}");

        try {
            settableAnyProperty.deserializeAndSet(parser, mapper.getDeserializationContext(), myPojo, "anyMap");
            Assert.fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            Assert.assertEquals(e.getCause().getMessage(), "Not a serializable type: class [Ljava.lang.Object;");
        }
    }

    @Test
    public void testSettableAnyPropertyWithValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();

        JavaType javaType = mapper.getTypeFactory().constructType(Map.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(new AnnotatedField(MyPojo.class, "anyMap")),
                new AnnotatedField(MyPojo.class, "anyMap"),
                javaType,
                null,
                mapper.findValueDeserializer(MyPojoValue.class),
                null);

        MyPojo myPojo = new MyPojo();

        JsonParser parser = factory.createParser("{\"anyMap\":[{\"key\":1,\"value\":1}]}");

        settableAnyProperty.deserializeAndSet(parser, mapper.getDeserializationContext(), myPojo, "anyMap");

        Map<Object, Object> actualMap = myPojo.getAnyMap();
        Map<Object, MyPojoValue> expectedMap = new HashMap<>();
        expectedMap.put(1, new MyPojoValue(1));

        Assert.assertEquals(actualMap, expectedMap);
    }

    static class MyPojo {
        private Map<Object, Object> anyMap;

        public Map<Object, Object> getAnyMap() {
            return anyMap;
        }

        // Getters and setters omitted for brevity
    }

    static class MyPojoValue {
        private int value;

        public MyPojoValue(int value) {
            this.value = value;
        }

        // Getters and setters omitted for brevity
    }
}
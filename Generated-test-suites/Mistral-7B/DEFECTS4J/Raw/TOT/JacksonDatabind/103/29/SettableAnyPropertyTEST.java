package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.SettableAnyProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Test for {@link SettableAnyProperty} class.
 */
public class SettableAnyPropertyTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testSettableAnyProperty() throws IOException {
        BeanProperty property = MAPPER.findBeanProperty("anyProperty", TestPojo.class);
        AnnotatedMember setter = MAPPER.findExecutable(TestPojo.class, "setAnyProperty");
        JavaType type = MAPPER.getTypeFactory().constructMapType(Map.class, String.class, Integer.class);
        KeyDeserializer keyDeserializer = MAPPER.getDeserializationConfig().getKeyDeserializerFactory().findKeyDeserializer(String.class, null);
        JsonDeserializer<Object> valueDeserializer = MAPPER.getDeserializationConfig().findValueDeserializer(Integer.class, null);
        TypeDeserializer valueTypeDeserializer = MAPPER.getTypeFactory().constructTypeDeserializer(Integer.class);

        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        TestPojo pojo = new TestPojo();
        anyProperty.fixAccess(MAPPER.getDeserializationConfig());

        Map<String, Integer> map = new HashMap<>();
        map.put("key1", 1);
        map.put("key2", 2);

        ObjectWriter writer = MAPPER.writer().withRootValue(map);
        JsonNode node = writer.writeValueAsTree(map);

        anyProperty.deserializeAndSet(node.asToken(), MAPPER.getDeserializationConfig(), pojo, "anyProperty");

        Map<String, Integer> actualMap = pojo.getAnyProperty();

        assert map.equals(actualMap);
    }

    @Test
    public void testSettableAnyPropertyNullValue() throws IOException {
        BeanProperty property = MAPPER.findBeanProperty("anyProperty", TestPojo.class);
        AnnotatedMember setter = MAPPER.findExecutable(TestPojo.class, "setAnyProperty");
        JavaType type = MAPPER.getTypeFactory().constructMapType(Map.class, String.class, Integer.class);
        KeyDeserializer keyDeserializer = MAPPER.getDeserializationConfig().getKeyDeserializerFactory().findKeyDeserializer(String.class, null);
        JsonDeserializer<Object> valueDeserializer = MAPPER.getDeserializationConfig().findValueDeserializer(Integer.class, null);
        TypeDeserializer valueTypeDeserializer = MAPPER.getTypeFactory().constructTypeDeserializer(Integer.class);

        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        TestPojo pojo = new TestPojo();
        anyProperty.fixAccess(MAPPER.getDeserializationConfig());

        ObjectWriter writer = MAPPER.writer().withRootValue(null);
        JsonNode node = writer.writeValueAsTree(null);

        try {
            anyProperty.deserializeAndSet(node.asToken(), MAPPER.getDeserializationConfig(), pojo, "anyProperty");
            fail("Expected JsonMappingException with the message: 'Problem deserializing \"any\" property 'anyProperty' of class [any property on class com.test.TestPojo] (expected type: java.util.Map<java.lang.String, java.lang.Integer>; actual type: null)'\n");
        } catch (JsonMappingException e) {
            // Expected
        }

        Map<String, Integer> actualMap = pojo.getAnyProperty();

        assert actualMap == null;
    }

    @Test
    public void testSettableAnyPropertyWrongTypeValue() throws IOException {
        BeanProperty property = MAPPER.findBeanProperty("anyProperty", TestPojo.class);
        AnnotatedMember setter = MAPPER.findExecutable(TestPojo.class, "setAnyProperty");
        JavaType type = MAPPER.getTypeFactory().constructMapType(Map.class, String.class, Integer.class);
        KeyDeserializer keyDeserializer = MAPPER.getDeserializationConfig().getKeyDeserializerFactory().findKeyDeserializer(String.class, null);
        JsonDeserializer<Object> valueDeserializer = MAPPER.getDeserializationConfig().findValueDeserializer(Double.class, null);
        TypeDeserializer valueTypeDeserializer = MAPPER.getTypeFactory().constructTypeDeserializer(Integer.class);

        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        TestPojo pojo = new TestPojo();
        anyProperty.fixAccess(MAPPER.getDeserializationConfig());

        ObjectWriter writer = MAPPER.writer().withRootValue(1);
        JsonNode node = writer.writeValueAsTree(1);

        try {
            anyProperty.deserializeAndSet(node.asToken(), MAPPER.getDeserializationConfig(), pojo, "anyProperty");
            fail("Expected JsonMappingException with the message: 'Problem deserializing \"any\" property 'anyProperty' of class [any property on class com.test.TestPojo] (expected type: java.util.Map<java.lang.String, java.lang.Integer>; actual type: java.lang.Integer)'\n");
        } catch (JsonMappingException e) {
            // Expected
        }

        Map<String, Integer> actualMap = pojo.getAnyProperty();

        assert actualMap == null;
    }

    @Test
    public void testSettableAnyPropertyEmptyMap() throws IOException {
        BeanProperty property = MAPPER.findBeanProperty("anyProperty", TestPojo.class);
        AnnotatedMember setter = MAPPER.findExecutable(TestPojo.class, "setAnyProperty");
        JavaType type = MAPPER.getTypeFactory().constructMapType(Map.class, String.class, Integer.class);
        KeyDeserializer keyDeserializer = MAPPER.getDeserializationConfig().getKeyDeserializerFactory().findKeyDeserializer(String.class, null);
        JsonDeserializer<Object> valueDeserializer = MAPPER.getDeserializationConfig().findValueDeserializer(Integer.class, null);
        TypeDeserializer valueTypeDeserializer = MAPPER.getTypeFactory().constructTypeDeserializer(Integer.class);

        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        TestPojo pojo = new TestPojo();
        anyProperty.fixAccess(MAPPER.getDeserializationConfig());

        ObjectWriter writer = MAPPER.writer().withRootValue(new Object());
        JsonNode node = writer.writeValueAsTree(new Object());

        anyProperty.deserializeAndSet(node.asToken(), MAPPER.getDeserializationConfig(), pojo, "anyProperty");

        Map<String, Integer> actualMap = pojo.getAnyProperty();

        assert actualMap.isEmpty();
    }

    @Test
    public void testSettableAnyPropertyValueDeserializationException() throws IOException {
        BeanProperty property = MAPPER.findBeanProperty("anyProperty", TestPojo.class);
        AnnotatedMember setter = MAPPER.findExecutable(TestPojo.class, "setAnyProperty");
        JavaType type = MAPPER.getTypeFactory().constructMapType(Map.class, String.class, Integer.class);
        KeyDeserializer keyDeserializer = MAPPER.getDeserializationConfig().getKeyDeserializerFactory().findKeyDeserializer(String.class, null);
        JsonDeserializer<Object> valueDeserializer = new MyIntegerDeserializer();
        TypeDeserializer valueTypeDeserializer = MAPPER.getTypeFactory().constructTypeDeserializer(Integer.class);

        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        TestPojo pojo = new TestPojo();
        anyProperty.fixAccess(MAPPER.getDeserializationConfig());

        ObjectWriter writer = MAPPER.writer().withRootValue("invalidJson");
        JsonNode node = writer.writeValueAsTree("invalidJson");

        try {
            anyProperty.deserializeAndSet(node.asToken(), MAPPER.getDeserializationConfig(), pojo, "anyProperty");
            fail("Expected JsonMappingException with the message: 'com.fasterxml.jackson.databind.JsonMappingException: MyIntegerDeserializer: Cannot deserialize value of type java.lang.Integer from JsonNode: invalidJson\n'");
        } catch (JsonMappingException e) {
            // Expected
        }

        Map<String, Integer> actualMap = pojo.getAnyProperty();

        assert actualMap == null;
    }

    @Test
    public void testSettableAnyPropertyKeyDeserializationException() throws IOException {
        BeanProperty property = MAPPER.findBeanProperty("anyProperty", TestPojo.class);
        AnnotatedMember setter = MAPPER.findExecutable(TestPojo.class, "setAnyProperty");
        JavaType type = MAPPER.getTypeFactory().constructMapType(Map.class, String.class, Integer.class);
        KeyDeserializer keyDeserializer = new MyStringKeyDeserializer();
        JsonDeserializer<Object> valueDeserializer = MAPPER.getDeserializationConfig().findValueDeserializer(Integer.class, null);
        TypeDeserializer valueTypeDeserializer = MAPPER.getTypeFactory().constructTypeDeserializer(Integer.class);

        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        TestPojo pojo = new TestPojo();
        anyProperty.fixAccess(MAPPER.getDeserializationConfig());

        ObjectWriter writer = MAPPER.writer().withRootValue("{\"invalidKey\": 1}");
        JsonNode node = writer.writeValueAsTree("{\"invalidKey\": 1}");

        try {
            anyProperty.deserializeAndSet(node.asToken(), MAPPER.getDeserializationConfig(), pojo, "anyProperty");
            fail("Expected JsonMappingException with the message: 'com.fasterxml.jackson.databind.JsonMappingException: MyStringKeyDeserializer: Cannot deserialize key 'invalidKey' from JsonNode: {\"invalidKey\":1}\n'");
        } catch (JsonMappingException e) {
            // Expected
        }

        Map<String, Integer> actualMap = pojo.getAnyProperty();

        assert actualMap == null;
    }

    static class TestPojo {
        private Map<String, Integer> anyProperty;

        public Map<String, Integer> getAnyProperty() {
            return anyProperty;
        }

        public void setAnyProperty(Map<String, Integer> anyProperty) {
            this.anyProperty = anyProperty;
        }
    }

    static class MyIntegerDeserializer extends JsonDeserializer<Integer> {
        @Override
        public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            throw new IOException("MyIntegerDeserializer: Cannot deserialize value of type java.lang.Integer from JsonNode: " + p.getValueAsString());
        }
    }

    static class MyStringKeyDeserializer extends KeyDeserializer {
        @Override
        public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
            throw new IOException("MyStringKeyDeserializer: Cannot deserialize key '" + key + "' from String: " + key);
        }
    }
}
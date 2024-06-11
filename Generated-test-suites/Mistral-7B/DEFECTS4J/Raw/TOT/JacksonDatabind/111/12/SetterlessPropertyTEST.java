package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

public class SetterlessPropertyTest {

    @Test
    public void testSerializerDeserializer() throws JsonProcessingException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ObjectMapper mapper = new ObjectMapper();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("listProperty", ArrayList.class, null);
        JavaType listType = mapper.getTypeFactory().constructType(ArrayList.class);
        SettableBeanProperty prop = new SetterlessProperty(propDef, listType, null, null, new AnnotatedMethod(ArrayList.class.getMethod("get", (Class[]) null)));

        ObjectNode json = mapper.createObjectNode();
        json.put("listProperty", "[1, 2, 3]");
        ObjectNode result = mapper.readValue(json.toString(), MapperFactoryBuilder.getInstance().buildType(ObjectNode.class));

        assertEquals(Arrays.asList(1, 2, 3), result.get("listProperty"));

        Object instance = mapper.readValue(json.toString(), Object.class);
        Object list = prop.getGetter().invoke(instance);
        assertEquals(Arrays.asList(1, 2, 3), list);

        prop.getValueDeserializer().deserialize(mapper.getFactory().createParser(json.toString()), new TestContext(), list);
        assertEquals(Arrays.asList(1, 2, 3), list);
    }

    @Test
    public void testNullValue() throws JsonProcessingException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ObjectMapper mapper = new ObjectMapper();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("listProperty", ArrayList.class, null);
        JavaType listType = mapper.getTypeFactory().constructType(ArrayList.class);
        SettableBeanProperty prop = new SetterlessProperty(propDef, listType, null, null, new AnnotatedMethod(ArrayList.class.getMethod("get", (Class[]) null)));

        ObjectNode json = mapper.createObjectNode();
        json.put("listProperty", null);
        ObjectNode result = mapper.readValue(json.toString(), MapperFactoryBuilder.getInstance().buildType(ObjectNode.class));

        assertNull(result.get("listProperty"));

        Object instance = mapper.readValue(json.toString(), Object.class);
        Object list = prop.getGetter().invoke(instance);
        assertNull(list);

        Object nullValue = prop.getNullProvider().getValueProvider().getNullValue();
        prop.getValueDeserializer().deserialize(mapper.getFactory().createParser(json.toString()), new TestContext(), list);
        assertEquals(nullValue, list);
    }

    @Test
    public void testEmptyCollection() throws JsonProcessingException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ObjectMapper mapper = new ObjectMapper();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("listProperty", ArrayList.class, null);
        JavaType listType = mapper.getTypeFactory().constructType(ArrayList.class);
        SettableBeanProperty prop = new SetterlessProperty(propDef, listType, null, null, new AnnotatedMethod(ArrayList.class.getMethod("get", (Class[]) null)));

        ObjectNode json = mapper.createObjectNode();
        json.put("listProperty", "[]");
        ObjectNode result = mapper.readValue(json.toString(), MapperFactoryBuilder.getInstance().buildType(ObjectNode.class));

        assertNotNull(result.get("listProperty"));
        assertTrue(result.get("listProperty").isMissingNode());

        Object instance = mapper.readValue(json.toString(), Object.class);
        Object list = prop.getGetter().invoke(instance);
        assertTrue(list instanceof Collection);
        assertTrue(list.isEmpty());

        prop.getValueDeserializer().deserialize(mapper.getFactory().createParser(json.toString()), new TestContext(), list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testEmptyMap() throws JsonProcessingException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ObjectMapper mapper = new ObjectMapper();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("mapProperty", HashMap.class, null);
        JavaType mapType = mapper.getTypeFactory().constructType(HashMap.class);
        SettableBeanProperty prop = new SetterlessProperty(propDef, mapType, null, null, new AnnotatedMethod(HashMap.class.getMethod("get", (Class[]) null)));

        ObjectNode json = mapper.createObjectNode();
        json.put("mapProperty", "{}");
        ObjectNode result = mapper.readValue(json.toString(), MapperFactoryBuilder.getInstance().buildType(ObjectNode.class));

        assertNotNull(result.get("mapProperty"));
        assertTrue(result.get("mapProperty").isObject());
        assertTrue(result.get("mapProperty").isMissingNode());

        Object instance = mapper.readValue(json.toString(), Object.class);
        Object map = prop.getGetter().invoke(instance);
        assertTrue(map instanceof Map);
        assertTrue(map.isEmpty());

        prop.getValueDeserializer().deserialize(mapper.getFactory().createParser(json.toString()), new TestContext(), map);
        assertTrue(map.isEmpty());
    }

    @Test
    public void testSerializedException() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonDeserializer<Integer> deserializer = new JsonDeserializer<Integer>() {
            @Override
            public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                throw new IOException("Expected exception");
            }
        };

        BeanPropertyDefinition propDef = new BeanPropertyDefinition("integerProperty", Integer.class, null);
        JavaType intType = mapper.getTypeFactory().constructType(Integer.class);
        SettableBeanProperty prop = new SetterlessProperty(propDef, intType, null, null, new AnnotatedMethod(Integer.class.getMethod("getValue", (Class[]) null)));
        prop.setValueDeserializer(deserializer);

        ObjectNode json = mapper.createObjectNode();
        json.put("integerProperty", 42);

        try {
            mapper.readValue(json.toString(), MapperFactoryBuilder.getInstance().buildType(ObjectNode.class));
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }

        Object instance = mapper.readValue(json.toString(), Object.class);
        Object integer = prop.getGetter().invoke(instance);
        assertEquals(null, integer);
    }

    @JsonDeserialize(using = TestValueDeserializer.class)
    static class TestValue {
        private int value;

        public int getValue() {
            return value;
        }
    }

    static class TestValueDeserializer extends StdDeserializer<TestValue> {
        @Override
        public TestValue deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            throw new IOException("Expected deserialization exception");
        }
    }

    static class TestContext extends DeserializationContext {
        // Empty
    }
}
package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetterlessPropertyTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(List.class, new ListDeserializer());
        module.addDeserializer(Map.class, new MapDeserializer());
        objectMapper = new ObjectMapper(module);
    }

    @Test
    public void testSetterlessProperty_Collection() throws IOException {
        String json = "{\"name\": \"test\", \"list\": []}";
        MyClass myClass = new MyClass();

        objectMapper.readValue(json, MyClass.class, MyClass.SETTERLESS_LIST_PROPERTY);

        Assert.assertEquals(new ArrayList<>(), myClass.getList());

        String jsonWithValues = "{\"name\": \"test\", \"list\": [1, 2, 3]}";
        Object result = objectMapper.readValue(jsonWithValues, MyClass.class, MyClass.SETTERLESS_LIST_PROPERTY);

        MyClass expectedResult = new MyClass();
        expectedResult.setList(new ArrayList<>(List.of(1, 2, 3)));

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testSetterlessProperty_Map() throws IOException {
        String json = "{\"name\": \"test\", \"map\": {}}";
        MyClass myClass = new MyClass();

        objectMapper.readValue(json, MyClass.class, MyClass.SETTERLESS_MAP_PROPERTY);

        Assert.assertEquals(new HashMap<>(), myClass.getMap());

        String jsonWithValues = "{\"name\": \"test\", \"map\": {\"key1\": 1, \"key2\": 2}}";
        Object result = objectMapper.readValue(jsonWithValues, MyClass.class, MyClass.SETTERLESS_MAP_PROPERTY);

        MyClass expectedResult = new MyClass();
        expectedResult.setMap(new HashMap<>(Map.of("key1", 1, "key2", 2)));

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testSetterlessProperty_NullValue() throws IOException {
        String json = "{\"name\": \"test\", \"list\": null}";
        MyClass myClass = new MyClass();

        objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.readValue(json, MyClass.class, MyClass.SETTERLESS_LIST_PROPERTY);

        Assert.assertNull(myClass.getList());

        String jsonWithValues = "{\"name\": \"test\", \"map\": null}";
        Object result = objectMapper.readValue(jsonWithValues, MyClass.class, MyClass.SETTERLESS_MAP_PROPERTY);

        Assert.assertNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetterlessProperty_SetMethod() throws IOException {
        MyClass myClass = new MyClass();

        myClass.setList(new ArrayList<>());

        // Should not be able to call set() on setterless property
        myClass.setList(new ArrayList<>(List.of(1, 2, 3)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetterlessProperty_SetAndReturn() throws IOException {
        MyClass myClass = new MyClass();

        MyClass result = myClass.setList(new ArrayList<>());

        // Should not be able to call setAndReturn() on setterless property
        result = myClass.setList(new ArrayList<>(List.of(1, 2, 3)));
    }

    static class MyClass {
        private static final SettableBeanProperty<MyClass, List<Integer>> SETTERLESS_LIST_PROPERTY;
        private static final SettableBeanProperty<MyClass, Map<String, Integer>> SETTERLESS_MAP_PROPERTY;

        List<Integer> list;
        Map<String, Integer> map;

        static {
            try {
                BeanPropertyDefinition listPropertyDefinition = BeanDescription.StandardBuilder.start()
                        .withName("list")
                        .withType(List.class)
                        .withVisibility(PropertyAccessor.SETTER_VISIBILITY_NONE)
                        .withValueDeserializer(new ListDeserializer())
                        .withNullValueProvider(NullValueProvider.noOpValue())
                        .build();

                SETTERLESS_LIST_PROPERTY = new SetterlessProperty(
                        listPropertyDefinition,
                        TypeFactory.defaultInstance().constructType(List.class),
                        null,
                        null,
                        new AnnotatedMethod(MyClass.class, "getList", new Class[0])
                );
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            try {
                BeanPropertyDefinition mapPropertyDefinition = BeanDescription.StandardBuilder.start()
                        .withName("map")
                        .withType(Map.class)
                        .withVisibility(PropertyAccessor.SETTER_VISIBILITY_NONE)
                        .withValueDeserializer(new MapDeserializer())
                        .withNullValueProvider(NullValueProvider.noOpValue())
                        .build();

                SETTERLESS_MAP_PROPERTY = new SetterlessProperty(
                        mapPropertyDefinition,
                        TypeFactory.defaultInstance().constructType(Map.class),
                        null,
                        null,
                        new AnnotatedMethod(MyClass.class, "getMap", new Class[0])
                );
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        public List<Integer> getList() {
            return list;
        }

        public Map<String, Integer> getMap() {
            return map;
        }
    }

    static class ListDeserializer extends StdDeserializer<List<Integer>> {
        public ListDeserializer() {
            this(null);
        }

        public ListDeserializer(Class<List<Integer>> vc) {
            super(vc);
        }

        @Override
        public List<Integer> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonToken t = p.getCurrentToken();
            if (t.isArrayToken()) {
                List<Integer> list = new ArrayList<>();
                while (p.nextToken() != JsonToken.END_ARRAY) {
                    list.add(p.getValueAsInt());
                }
                return list;
            }
            throw new JsonMappingException(p, "Not an array token");
        }
    }

    static class MapDeserializer extends StdDeserializer<Map<String, Integer>> {
        public MapDeserializer() {
            this(null);
        }

        public MapDeserializer(Class<Map<String, Integer>> vc) {
            super(vc);
        }

        @Override
        public Map<String, Integer> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.readValueAsTree();
            Map<String, Integer> map = new HashMap<>();
            for (Map.Entry<String, JsonNode> entry : node.fields()) {
                map.put(entry.getKey(), entry.getValue().asInt());
            }
            return map;
        }
    }
}
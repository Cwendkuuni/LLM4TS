package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ClassNameIdResolverTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(EnumSet.class, new EnumSetSerializer());
        module.addSerializer(HashMap.class, new HashMapSerializer());
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
    }

    @Test
    public void testIdFromValue() throws IOException {
        String json = "{\"value\":1,\"type\":\"java.lang.Integer\"}";
        MyClass intType = new MyClass(1);
        assertEquals(intType.getClass().getName(), objectMapper.readerForMapAndValue(json).readValueAs(String.class));
    }

    @Test
    public void testIdFromValueAndType() throws IOException {
        String json = "{\"value\":1,\"type\":\"java.lang.Integer\"}";
        MyClass intType = new MyClass(1);
        assertEquals(intType.getClass().getName(), objectMapper
                .readerForValue(json)
                .readValueAs(MyClass.class)
                .getClass()
                .getName());
    }

    @Test
    public void testTypeFromId() throws IOException {
        String json = "{\"type\":\"java.lang.Integer\"}";
        Integer intValue = 1;
        assertEquals(intValue.getClass(), objectMapper.readValue(json, Integer.class));

        json = "{\"type\":\"java.util.EnumSet\"}";
        EnumSet<MyEnum> enumSet = EnumSet.allOf(MyEnum.class);
        assertEquals(enumSet.getClass(), objectMapper.readValue(json, EnumSet.class));

        json = "{\"type\":\"java.util.HashMap\"}";
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        assertEquals(map.getClass(), objectMapper.readValue(json, Map.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTypeId() throws IOException {
        String json = "{\"type\":\"invalidId\"}";
        objectMapper.readValue(json, Integer.class);
    }

    @Test
    public void testIdFromValueForEnum() throws IOException {
        String json = "{\"value\":MY_ENUM_1,\"type\":\"com.example.MyEnum\"}";
        MyClass enumWrapper = objectMapper.readerForMapAndValue(json).readValueAs(MyClass.class);
        assertEquals(MY_ENUM_1.getClass(), enumWrapper.getValue().getClass());
    }

    @Test
    public void testIdFromValueForInnerClass() throws IOException {
        String json = "{\"value\":1,\"type\":\"com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver$1\"}";
        MyClass innerClassWrapper = objectMapper.readerForMapAndValue(json).readValueAs(MyClass.class);
        assertEquals(InnerClass.class, innerClassWrapper.getValue().getClass());
    }

    @Test
    public void testIdFromValueForArray() throws IOException {
        String json = "{\"value\":[1, 2],\"type\":\"java.lang.Integer[]\"}";
        Integer[] intArray = objectMapper.readerForMapAndValue(json).readValueAs(Integer[].class);
        assertEquals(Arrays.asList(1, 2).toArray(new Integer[0]), intArray);
    }

    private static class MyEnum {
        MyEnum MY_ENUM_1;
        MyEnum MY_ENUM_2;
    }

    private static class InnerClass {
    }

    private static class MyClass {
        Object value;

        public MyClass(Object value) {
            this.value = value;
        }
    }

    private static class EnumSetSerializer extends StdSerializer<EnumSet<?>> {
        public EnumSetSerializer() {
            this(null);
        }

        public EnumSetSerializer(Class<EnumSet<?>> t) {
            this(null);
        }

        public EnumSetSerializer(Class<EnumSet<?>> t, JsonSerializer<Object> valueSerializer) {
            super(t, valueSerializer);
        }

        @Override
        public void serializeValues(JsonGenerator jg, Map<EnumSet<?>, Object> valueMap) throws IOException {
            for (Map.Entry<EnumSet<?>, Object> entry : valueMap.entrySet()) {
                JsonEncodingContext ctxt = jg.getCodec().contextInstance();
                ObjectWriter writer = objectMapper.writer().withContext(ctxt);
                jg.writeStartObject();
                jg.writeStringField("type", getValueAsString(entry.getKey().getClass()));
                writer.writeValue(jg, entry.getValue());
                jg.writeEndObject();
            }
        }

        @Override
        public void serializeValue(JsonGenerator jg, EnumSet<?> value, SerializerProvider sp) throws IOException {
            JsonEncodingContext ctxt = jg.getCodec().contextInstance();
            ObjectWriter writer = objectMapper.writer().withContext(ctxt);
            jg.writeStartObject();
            jg.writeStringField("type", getValueAsString(value.getClass()));
            writer.writeValue(jg, value);
            jg.writeEndObject();
        }

        private String getValueAsString(Class<?> clazz) {
            return clazz.getName().replace('.', '/').replaceAll("$", "");
        }
    }

    private static class HashMapSerializer extends StdSerializer<Map<String, String>> {
        public HashMapSerializer() {
            this(null);
        }

        public HashMapSerializer(Class<Map<String, String>> t) {
            this(null);
        }

        public HashMapSerializer(Class<Map<String, String>> t, JsonSerializer<Object> valueSerializer) {
            super(t, valueSerializer);
        }

        @Override
        public void serializeValues(JsonGenerator jg, Map<String, String> valueMap) throws IOException {
            for (Map.Entry<String, String> entry : valueMap.entrySet()) {
                JsonEncodingContext ctxt = jg.getCodec().contextInstance();
                ObjectWriter writer = objectMapper.writer().withContext(ctxt);
                jg.writeStartObject();
                jg.writeStringField("key", entry.getKey());
                jg.writeStringField("value", entry.getValue());
                jg.writeStringField("type", getValueAsString(valueMap.getClass()));
                writer.writeValue(jg, entry.getValue());
                jg.writeEndObject();
            }
        }

        @Override
        public void serializeValue(JsonGenerator jg, Map.Entry<String, String> value, SerializerProvider sp) throws IOException {
            JsonEncodingContext ctxt = jg.getCodec().contextInstance();
            ObjectWriter writer = objectMapper.writer().withContext(ctxt);
            jg.writeStartObject();
            jg.writeStringField("key", value.getKey());
            jg.writeStringField("value", value.getValue());
            jg.writeStringField("type", getValueAsString(value.getValue().getClass()));
            writer.writeValue(jg, value.getValue());
            jg.writeEndObject();
        }

        private String getValueAsString(Class<?> clazz) {
            return clazz.getName().replace('.', '/').replaceAll("$", "");
        }
    }
}
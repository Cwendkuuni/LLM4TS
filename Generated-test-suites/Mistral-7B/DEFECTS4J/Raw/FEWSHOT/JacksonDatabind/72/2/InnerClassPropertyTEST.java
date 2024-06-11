package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.deser.impl.InnerClassProperty;

public class InnerClassPropertyTest {

    static class TestBean {
        @JsonProperty("inner")
        private Inner inner;

        @JsonCreator
        public TestBean(@JsonProperty("inner") Inner inner) {
            this.inner = inner;
        }
    }

    static class Inner {
        private int value;

        @JsonCreator
        public Inner() {
            this(0);
        }

        @JsonCreator
        public Inner(@JsonProperty("value") int value) {
            this.value = value;
        }
    }

    @Test
    public void testInnerClassPropertyDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"inner\":{\"value\":123}}";
        TestBean bean = mapper.readValue(json, TestBean.class);
        assertEquals(123, bean.inner.value);
    }

    @Test
    public void testInnerClassPropertySerialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = new TestBean(new Inner(456));
        String json = mapper.writeValueAsString(bean);
        TestBean deserialized = mapper.readValue(json, TestBean.class);
        assertEquals(456, deserialized.inner.value);
    }

    @Test
    public void testInnerClassPropertyWithValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Inner> deserializer = new JsonDeserializer<Inner>() {
            @Override
            public Inner deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return new Inner(jp.getValueAsInt());
            }
        };
        mapper.registerDeserializer(Inner.class, deserializer);
        String json = "{\"inner\":{\"value\":789}}";
        TestBean bean = mapper.readValue(json, TestBean.class);
        assertEquals(789, bean.inner.value);
    }

    @Test
    public void testInnerClassPropertyWithCustomCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Class<Inner> innerClass = Inner.class;
        Constructor<Inner> constructor = innerClass.getDeclaredConstructor(Integer.class);
        constructor.setAccessible(true);
        InnerPropertyTestUtils.setupInnerClassProperty(InnerPropertyTestUtils.createPropertyContext(mapper), innerClass, constructor);
        String json = "{\"inner\":{}}";
        TestBean bean = mapper.readValue(json, TestBean.class);
        assertNull(bean.inner);

        json = "{\"inner\":{ \"value\":234 }}";
        TestBean deserialized = mapper.readValue(json, TestBean.class);
        Inner inner = deserialized.inner;
        assertEquals(234, inner.value);
        assertEquals(Inner.class, inner.getClass());
        assertEquals(constructor, inner.getClass().getDeclaredConstructor(Integer.class));
    }

    @Test
    public void testInnerClassPropertyWithCustomCreatorAndValueDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Class<Inner> innerClass = Inner.class;
        Constructor<Inner> constructor = innerClass.getDeclaredConstructor(Integer.class);
        constructor.setAccessible(true);
        JsonDeserializer<Inner> valueDeserializer = new JsonDeserializer<Inner>() {
            @Override
            public Inner deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return new Inner(jp.getValueAsInt() * 2);
            }
        };
        mapper.registerDeserializer(Inner.class, valueDeserializer);
        InnerPropertyTestUtils.setupInnerClassProperty(InnerPropertyTestUtils.createPropertyContext(mapper), innerClass, constructor, valueDeserializer);

        String json = "{\"inner\":{\"value\":3}}";
        TestBean bean = mapper.readValue(json, TestBean.class);
        Inner inner = bean.inner;
        assertEquals(6, inner.value);
        assertEquals(Inner.class, inner.getClass());
        assertEquals(constructor, inner.getClass().getDeclaredConstructor(Integer.class));
    }

    @Test
    public void testInnerClassPropertyWithCustomCreatorAndValueSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Class<Inner> innerClass = Inner.class;
        Constructor<Inner> constructor = innerClass.getDeclaredConstructor(Integer.class);
        constructor.setAccessible(true);
        JsonSerializer<Inner> valueSerializer = new JsonSerializer<Inner>() {
            @Override
            public void serialize(Inner value, JsonGenerator jg, SerializerProvider provider) throws IOException {
                jg.writeNumber(value.value * 2);
            }
        };
        mapper.registerSerializer(Inner.class, valueSerializer);
        InnerPropertyTestUtils.setupInnerClassProperty(InnerPropertyTestUtils.createPropertyContext(mapper), innerClass, constructor, valueSerializer);

        TestBean bean = new TestBean();
        bean.inner = new Inner(5);
        String json = mapper.writeValueAsString(bean);
        String expectedJson = "{\"inner\":{\"value\":20}}";
        assertEquals(expectedJson, json);
    }

    @Test
    public void testInnerClassPropertyWithCustomCreatorAndValueDeserializerAndSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Class<Inner> innerClass = Inner.class;
        Constructor<Inner> constructor = innerClass.getDeclaredConstructor(Integer.class);
        constructor.setAccessible(true);
        JsonDeserializer<Inner> valueDeserializer = new JsonDeserializer<Inner>() {
            @Override
            public Inner deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return new Inner(jp.getValueAsInt() * 2);
            }
        };
        JsonSerializer<Inner> valueSerializer = new JsonSerializer<Inner>() {
            @Override
            public void serialize(Inner value, JsonGenerator jg, SerializerProvider provider) throws IOException {
                jg.writeNumber(value.value * 4);
            }
        };
        mapper.registerDeserializer(Inner.class, valueDeserializer);
        mapper.registerSerializer(Inner.class, valueSerializer);
        InnerPropertyTestUtils.setupInnerClassProperty(InnerPropertyTestUtils.createPropertyContext(mapper), innerClass, constructor, valueDeserializer, valueSerializer);

        TestBean bean = new TestBean();
        bean.inner = new Inner(5);
        String json = mapper.writeValueAsString(bean);
        String expectedJson = "{\"inner\":{\"value\":40}}";
        assertEquals(expectedJson, json);

        TestBean deserialized = mapper.readValue(json, TestBean.class);
        Inner inner = deserialized.inner;
        assertEquals(20, inner.value);
        assertEquals(Inner.class, inner.getClass());
        assertEquals(constructor, inner.getClass().getDeclaredConstructor(Integer.class));
    }
}
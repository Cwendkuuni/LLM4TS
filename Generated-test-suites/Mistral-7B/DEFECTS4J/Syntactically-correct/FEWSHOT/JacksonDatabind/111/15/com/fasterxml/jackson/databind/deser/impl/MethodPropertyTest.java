package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class MethodPropertyTest {

    static class TestBean {
        @JsonProperty
        private String property;

        public String getProperty() {
            return property;
        }

        public void setProperty(String value) {
            this.property = value;
        }
    }

    @Test
    public void testMethodProperty() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeDeserializer noOpTypeDeserializer = new StdTypeDeserializer();
        NullValueProvider nullValueProvider = new StandardSerializer(String.class) {
            @Override
            public String getNullValue(DeserializationContext ctxt) {
                return null;
            }
        }.withoutFeature(MapperFeature.AUTO_DETECT_GETTERS);

        MethodProperty property = new MethodProperty(
                new BeanPropertyDefinition(TestBean.class.getDeclaredField("property"), null),
                Object.class, noOpTypeDeserializer, Annotations.empty(),
                TestBean.class.getMethod("setProperty", String.class));

        Object testInstance = new TestBean();

        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"property\": null}");
        property.deserializeAndSet(jsonParser, new DeserializationContext(), testInstance);
        assertEquals(null, testInstance.getProperty());

        jsonParser = objectMapper.getFactory().createParser("{\"property\": \"foo\"}");
        property.deserializeAndSet(jsonParser, new DeserializationContext(), testInstance);
        assertEquals("foo", testInstance.getProperty());

        jsonParser = objectMapper.getFactory().createParser("{\"property\": null}");
        assertEquals(null, property.deserializeSetAndReturn(jsonParser, new DeserializationContext(), testInstance));
        assertEquals(null, testInstance.getProperty());

        jsonParser = objectMapper.getFactory().createParser("{\"property\": \"foo\"}");
        Object deserializedValue = property.deserializeSetAndReturn(jsonParser, new DeserializationContext(), testInstance);
        assertEquals("foo", deserializedValue);
        assertEquals("foo", testInstance.getProperty());

        jsonParser = objectMapper.getFactory().createParser("{\"property\": null}");
        property.set(testInstance, null);
        assertEquals(null, testInstance.getProperty());
        assertEquals(null, property.deserializeSetAndReturn(jsonParser, new DeserializationContext(), testInstance));

        jsonParser = objectMapper.getFactory().createParser("{\"property\": \"foo\"}");
        deserializedValue = property.setAndReturn(testInstance, "bar");
        assertEquals("bar", deserializedValue);
        assertEquals("bar", testInstance.getProperty());

        property = new MethodProperty(property, TestBean.class.getMethod("getProperty", new Class[0]));
        assertEquals("bar", property.get(testInstance));
    }

    @Test
    public void testSkipNulls() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeDeserializer noOpTypeDeserializer = new StdTypeDeserializer();
        NullValueProvider nullValueProvider = new StandardSerializer(String.class) {
            @Override
            public String getNullValue(DeserializationContext ctxt) {
                return null;
            }
        }.withoutFeature(MapperFeature.AUTO_DETECT_GETTERS);

        MethodProperty property = new MethodProperty(
                new BeanPropertyDefinition(TestBean.class.getDeclaredField("property"), null),
                Object.class, noOpTypeDeserializer, Annotations.empty(),
                TestBean.class.getMethod("setProperty", String.class));

        Object testInstance = new TestBean();

        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"property\": null}");
        property.deserializeAndSet(jsonParser, new DeserializationContext(), testInstance);
        // Should not set property to null
        assertEquals(null, testInstance.getProperty());

        jsonParser = objectMapper.getFactory().createParser("{\"property\": null}");
        assertEquals(testInstance, property.deserializeSetAndReturn(jsonParser, new DeserializationContext(), testInstance));
        assertEquals(null, testInstance.getProperty());
    }

    @Test(expected = IllegalAccessException.class)
    public void testAccessDenied() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeDeserializer noOpTypeDeserializer = new StdTypeDeserializer();
        NullValueProvider nullValueProvider = new StandardSerializer(String.class) {
            @Override
            public String getNullValue(DeserializationContext ctxt) {
                return null;
            }
        }.withoutFeature(MapperFeature.AUTO_DETECT_GETTERS);

        MethodProperty property = new MethodProperty(
                new BeanPropertyDefinition(TestBean.class.getDeclaredField("property"), null),
                Object.class, noOpTypeDeserializer, Annotations.empty(),
                TestBean.class.getDeclaredMethod("setProperty", String.class));

        Object testInstance = new TestBean();

        property.deserializeAndSet(new JsonParser(new StringReader("{\"property\": \"foo\"}")), new DeserializationContext(), testInstance);
    }
}
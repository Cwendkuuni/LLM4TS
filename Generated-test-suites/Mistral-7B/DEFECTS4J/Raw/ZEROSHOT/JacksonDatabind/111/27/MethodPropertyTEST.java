package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class MethodPropertyTest {

    @Test
    public void testMethodProperty() throws Exception {
        MyClass obj = new MyClass();
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\"property\":\"value\"}";
        MyClass deserializedObj = mapper.readValue(json, MyClass.class);

        assertEquals(deserializedObj.getProperty(), obj.getProperty());

        MyClass propertySetter = new MyClass("test");
        AnnotatedMethod method = MyClass.class.getMethod("setProperty", String.class);
        MethodProperty property = new MethodProperty(new BeanPropertyDefinition(PropertyName.construct("property"), MyClass.class, null),
                JavaType.of(MyClass.class), null, Annotations.empty(), method);

        ObjectMapper mapperForTest = new ObjectMapper();
        mapperForTest.registerPropertyAccessor(PropertyAccess.FIELD, new DefaultPropertyAccessor(Map.of()));

        JsonParser testParser = mapperForTest.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
        deserializeAndSet(testParser, mapperForTest, property, obj);
        assertEquals(deserializedObj.getProperty(), obj.getProperty());

        // Test deserializeSetAndReturn
        Object result = property.deserializeSetAndReturn(testParser, mapperForTest, obj);
        assertEquals(deserializedObj, result);

        // Test set and setAndReturn
        property.set(obj, "testValue");
        assertEquals("testValue", obj.getProperty());

        result = property.setAndReturn(obj, "testValue");
        assertEquals(obj, result);
        assertEquals("testValue", obj.getProperty());
    }

    private void deserializeAndSet(JsonParser parser, DeserializationContext ctxt, SettableBeanProperty property, Object instance) throws IOException {
        property.deserializeAndSet(parser, ctxt, instance);
    }

    @SuppressWarnings("unused")
    private static class MyClass {
        private String property;

        @JsonProperty
        public String getProperty() {
            return property;
        }

        @JsonProperty
        public void setProperty(String value) {
            this.property = value;
        }

        public MyClass() {
        }

        public MyClass(String property) {
            this.property = property;
        }
    }
}
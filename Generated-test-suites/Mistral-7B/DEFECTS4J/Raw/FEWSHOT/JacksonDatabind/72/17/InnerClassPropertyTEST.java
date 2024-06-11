package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class InnerClassPropertyTest {

    @Test
    public void testConstructor() {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> creator = mock(Constructor.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);
        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(creator, innerClassProperty._creator);
    }

    @Test
    public void testSerialization() throws Exception {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> creator = mock(Constructor.class);
        AnnotatedConstructor annotated = mock(AnnotatedConstructor.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator, annotated);
        Object innerObject = mock(Object.class);
        when(creator.newInstance(innerObject)).thenReturn(innerObject);
        when(annotated.getAnnotated()).thenReturn(innerClassProperty);

        ObjectWriter objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(innerClassProperty);
        Object deserialized = objectMapper.readValue(json, InnerClassProperty.class);
        assertEquals(innerClassProperty, deserialized);
    }

    @Test
    public void testDeserialization() throws Exception {
        Object bean = mock(Object.class);
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object value = new Object();
        when(jp.getText()).thenReturn("{\"value\": \"Test\"}");
        when(_creator.newInstance(bean)).thenReturn(new Object());

        InnerClassProperty property = new InnerClassProperty(null, _creator) {
            @Override
            public JsonDeserializer<?> _valueDeserializer = mock(JsonDeserializer.class);
        };

        property.deserializeAndSet(jp, ctxt, bean);
        verify(bean).setValue(value);
    }

    @Test
    public void testSet() throws Exception {
        Object instance = mock(Object.class);
        Object value = new Object();
        InnerClassProperty property = new InnerClassProperty(null, _creator) {
            @Override
            public void set(Object instance, Object value) {
                assertEquals(value, instance.getValue());
            }
        };
        property.set(instance, value);
        assertEquals(value, instance.getValue());
    }

    @JsonCreator
    static class TestClass {
        @JsonProperty
        private InnerClassProperty innerClassProperty;

        public InnerClassProperty getInnerClassProperty() {
            return innerClassProperty;
        }

        public void setInnerClassProperty(InnerClassProperty innerClassProperty) {
            this.innerClassProperty = innerClassProperty;
        }
    }

    @JsonDeserialize(using = InnerClassProperty.class)
    static class InnerClass {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    @Test
    public void testDeserializationWithJsonDeserializer() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Object bean = new Object();
        InnerClass innerClass = new InnerClass();
        InnerClassProperty property = new InnerClassProperty(null, _creator) {
            @Override
            public JsonDeserializer<?> _valueDeserializer = new JsonDeserializer<InnerClass>() {};
        };

        JsonParser jp = objectMapper.getFactory().createParser("{\"innerClass\":{\"value\":\"Test\"}}");

        property.deserializeAndSet(jp, new TestContext(), bean);
        TestClass testClass = objectMapper.readValue(jp, TestClass.class);
        assertEquals("Test", testClass.getInnerClassProperty().getValue().getValue());
    }

    @JsonDeserialize(using = InnerClassProperty.class)
    static class TestClassWithJsonDeserializer {
        @JsonProperty
        private InnerClassProperty innerClassProperty;

        public InnerClassProperty getInnerClassProperty() {
            return innerClassProperty;
        }

        public void setInnerClassProperty(InnerClassProperty innerClassProperty) {
            this.innerClassProperty = innerClassProperty;
        }
    }

    static class TestContext extends DeserializationContext {
    }
}
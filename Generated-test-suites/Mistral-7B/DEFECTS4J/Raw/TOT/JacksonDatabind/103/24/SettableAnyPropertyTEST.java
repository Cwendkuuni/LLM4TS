package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SettableAnyPropertyTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSettableAnyPropertySimple() throws JsonProcessingException {
        BeanProperty property = new BeanProperty(null, "testProperty", String.class, null);
        AnnotatedMember setter = new AnnotatedMethod(null, "setTestProperty", String.class, null);
        JavaType type = objectMapper.getTypeFactory().constructType(TestClass.class);
        KeyDeserializer keyDeserializer = new StandardKeyDeserializer();
        JsonDeserializer<Object> valueDeserializer = new StringDeserializer();
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        TestClass testClass = new TestClass();
        String testValue = "testValue";

        settableAnyProperty.deserializeAndSet(objectMapper.getFactory().createParser("{\"testProperty\":\"" + testValue + "\"}"), objectMapper.getDeserializationContext(), testClass, "testProperty");

        assertEquals(testValue, testClass.getTestProperty());
    }

    @Test
    public void testSettableAnyPropertyNullValue() throws JsonProcessingException {
        BeanProperty property = new BeanProperty(null, "testProperty", String.class, null);
        AnnotatedMember setter = new AnnotatedMethod(null, "setTestProperty", String.class, null);
        JavaType type = objectMapper.getTypeFactory().constructType(TestClass.class);
        KeyDeserializer keyDeserializer = new StandardKeyDeserializer();
        JsonDeserializer<Object> valueDeserializer = new StringDeserializer();
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        TestClass testClass = new TestClass();

        settableAnyProperty.deserializeAndSet(objectMapper.getFactory().createParser("{\"testProperty\":null}"), objectMapper.getDeserializationContext(), testClass, "testProperty");

        assertNull(testClass.getTestProperty());
    }

    @Test(expected = JsonMappingException.class)
    public void testSettableAnyPropertyInvalidType() throws JsonProcessingException {
        BeanProperty property = new BeanProperty(null, "testProperty", String.class, null);
        AnnotatedMember setter = new AnnotatedMethod(null, "setTestProperty", Integer.class, null);
        JavaType type = objectMapper.getTypeFactory().constructType(TestClass.class);
        KeyDeserializer keyDeserializer = new StandardKeyDeserializer();
        JsonDeserializer<Object> valueDeserializer = new StringDeserializer();
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        TestClass testClass = new TestClass();
        String testValue = "testValue";

        objectMapper.getFactory().createParser("{\"testProperty\":\"" + testValue + "\"}").nextToken(); // consume the first token

        settableAnyProperty.deserializeAndSet(objectMapper.getFactory().createParser("{\"testProperty\":\"" + testValue + "\"}"), objectMapper.getDeserializationContext(), testClass, "testProperty");
    }
}

class TestClass {
    private String testProperty;

    public String getTestProperty() {
        return testProperty;
    }
}
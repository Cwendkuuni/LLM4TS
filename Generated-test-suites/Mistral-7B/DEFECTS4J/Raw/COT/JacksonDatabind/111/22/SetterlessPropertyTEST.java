package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SetterlessProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SetterlessPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDefinition = null;
        JavaType type = null;
        TypeDeserializer typeDeserializer = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = null;

        SetterlessProperty setterlessProperty = new SetterlessProperty(propDefinition, type, typeDeserializer, contextAnnotations, method);
        assertNotNull(setterlessProperty);
    }

    @Test
    public void testWithName() {
        SetterlessProperty setterlessProperty = new SetterlessProperty(new PropertyName("testProperty"));
        SetterlessProperty newSetterlessProperty = setterlessProperty.withName(new PropertyName("newName"));
        assertEquals("newName", newSetterlessProperty.getName().asString());
    }

    @Test
    public void testWithValueDeserializer() {
        SetterlessProperty setterlessProperty = new SetterlessProperty(new PropertyName("testProperty"), new JsonDeserializer<>(), null);
        SetterlessProperty newSetterlessProperty = setterlessProperty.withValueDeserializer(new JsonDeserializer<>());
        assertEquals(new JsonDeserializer<>(), newSetterlessProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        SetterlessProperty setterlessProperty = new SetterlessProperty(new PropertyName("testProperty"), null, new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) {
                return null;
            }
        });
        SetterlessProperty newSetterlessProperty = setterlessProperty.withNullProvider(new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) {
                return null;
            }
        });
        assertEquals(new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) {
                return null;
            }
        }, newSetterlessProperty.getNullProvider());
    }

    @Test
    public void testFixAccess() {
        SetterlessProperty setterlessProperty = new SetterlessProperty(new PropertyName("testProperty"), null, null, null, null);
        setterlessProperty.fixAccess(null);
        // Check if fixAccess method doesn't throw any exception
        // It's not possible to test this method directly as it depends on the DeserializationConfig
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetMethod() throws IOException {
        Object instance = new Object();
        SetterlessProperty setterlessProperty = new SetterlessProperty(new PropertyName("testProperty"), null, null, null, null);
        setterlessProperty.set(instance, new Object());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode testObject = objectMapper.createObjectNode();
        JsonParser jsonParser = objectMapper.parser();
        jsonParser.setCodecConfig(new ObjectMapper.DefaultCodecConfig());

        SetterlessProperty setterlessProperty = new SetterlessProperty(new PropertyName("testProperty"), null, null, null, null);
        Object instance = new Object();

        testObject.put("testProperty", 1);
        jsonParser.readValueAs(Object.class, testObject);

        setterlessProperty.deserializeAndSet(jsonParser, null, instance);

        // Check if the instance has been modified
        Object value = setterlessProperty.getGetter().invoke(instance);
        assertEquals(1, value);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode testObject = objectMapper.createObjectNode();
        JsonParser jsonParser = objectMapper.parser();

        SetterlessProperty setterlessProperty = new SetterlessProperty(new PropertyName("testProperty"), null, null, null, null);
        Object instance = new Object();

        testObject.put("testProperty", 1);

        Object returnedInstance = setterlessProperty.deserializeSetAndReturn(jsonParser, null, instance);
        assertEquals(instance, returnedInstance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        SetterlessProperty setterlessProperty = new SetterlessProperty(new PropertyName("testProperty"), null, null, null, null);
        setterlessProperty.setAndReturn(instance, new Object());
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithTypeDeserializer() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode testObject = objectMapper.createObjectNode();
        JsonParser jsonParser = objectMapper.parser();
        JsonDeserializer<?> valueDeserializer = new StdScalarDeserializer();
        TypeDeserializer typeDeserializer = new JsonDeserializer.None(Object.class);

        SetterlessProperty setterlessProperty = new SetterlessProperty(new PropertyName("testProperty"), null, typeDeserializer, null, null);

        testObject.put("testProperty", "testString");

        // Throws IOException as per the code in SetterlessProperty class
        setterlessProperty.deserializeAndSet(jsonParser, null, new Object());
    }

    @Test(expected = InvocationTargetException.class)
    public void testInvokeGetterWithNullInstance() throws IllegalAccessException, InvocationTargetException {
        Object getter = new SetterlessProperty(new PropertyName("testProperty"), null, null, null, null).getGetter();
        getter.invoke(null, (Object[]) null);
    }

    @Test
    public void testGetAnnotation() {
        SetterlessProperty setterlessProperty = new SetterlessProperty(new PropertyName("testProperty"), null, null, null, new AnnotatedMethod(new Method("testMethod"), MyAnnotation.class));
        assertTrue(setterlessProperty.getAnnotation(MyAnnotation.class) instanceof MyAnnotation);
    }
}

class MyAnnotation {}
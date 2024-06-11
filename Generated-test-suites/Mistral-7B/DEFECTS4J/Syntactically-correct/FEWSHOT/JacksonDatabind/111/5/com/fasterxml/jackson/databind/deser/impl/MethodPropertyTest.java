package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public final class MethodPropertyTest {

    @Test
    public void testMethodProperty() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", String.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructParametricType(SettableBeanProperty.class, String.class);
        AnnotatedMethod annotatedMethod = Mockito.mock(AnnotatedMethod.class);
        Method method = Mockito.mock(Method.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeserializer, contextAnnotations, annotatedMethod, method);

        assertEquals(String.class, methodProperty.getType());
        assertEquals(method, methodProperty.getSetter());
        assertEquals(propDef, methodProperty.getBeanPropertyDefinition());
        assertEquals(type, methodProperty.getTypeHandler());
        assertEquals(contextAnnotations, methodProperty.getContextAnnotations());
        assertEquals(annotatedMethod, methodProperty.getAnnotated());

        BeanPropertyDefinition newPropDef = new BeanPropertyDefinition("newTestProperty", Integer.class);
        SettableBeanProperty newMethodProperty = methodProperty.withName(new PropName("newTestProperty"));
        assertEquals(newPropDef, newMethodProperty.getBeanPropertyDefinition());

        JsonDeserializer<String> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        NullValueProvider nullValueProvider = Mockito.mock(NullValueProvider.class);
        MethodProperty newMethodPropertyWithDeserializer = new MethodProperty(methodProperty, valueDeserializer, nullValueProvider);
        assertEquals(methodProperty, newMethodPropertyWithDeserializer.getPropertyBase());
        assertEquals(valueDeserializer, newMethodPropertyWithDeserializer.getValueDeserializer());
        assertEquals(nullValueProvider, newMethodPropertyWithDeserializer.getNullProvider());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        MethodProperty methodProperty = mapper.findProperty("testProperty", String.class);
        String testValue = "testValue";

        ObjectMapper testMapper = new ObjectMapper();
        JsonParser testParser = testMapper.getFactory().createParser("{\"testProperty\": \"testValue\"}");

        methodProperty.deserializeAndSet(testParser, testMapper.getDeserializationContext(), null);

        assertEquals(testValue, methodProperty.get(null));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        MethodProperty methodProperty = mapper.findProperty("testProperty", String.class);
        String testValue = "testValue";

        ObjectMapper testMapper = new ObjectMapper();
        JsonParser testParser = testMapper.getFactory().createParser("{\"testProperty\": \"testValue\"}");

        assertEquals(null, methodProperty.deserializeSetAndReturn(testParser, testMapper.getDeserializationContext(), null));
        assertEquals(testValue, methodProperty.get(null));

        testParser = testMapper.getFactory().createParser("null");

        assertEquals(null, methodProperty.deserializeSetAndReturn(testParser, testMapper.getDeserializationContext(), null));
        assertEquals(null, methodProperty.get(null));

        testParser = testMapper.getFactory().createParser("{\"testProperty\": null}");

        assertEquals(null, methodProperty.deserializeSetAndReturn(testParser, testMapper.getDeserializationContext(), null));
        assertEquals(null, methodProperty.get(null));
    }

    @Test
    public void testSet() throws IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        MethodProperty methodProperty = mapper.findProperty("testProperty", String.class);
        String testValue = "testValue";

        Object testInstance = new Object();
        methodProperty.set(testInstance, testValue);

        assertEquals(testValue, methodProperty.get(testInstance));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        MethodProperty methodProperty = mapper.findProperty("testProperty", String.class);
        String testValue = "testValue";

        Object testInstance = new Object();
        assertEquals(testInstance, methodProperty.setAndReturn(testInstance, testValue));
        assertEquals(testValue, methodProperty.get(testInstance));
    }

    @Test
    public void testReadResolve() throws IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        MethodProperty methodProperty = mapper.findProperty("testProperty", String.class);

        MethodProperty newMethodProperty = (MethodProperty) methodProperty.readResolve();

        assertEquals(methodProperty, newMethodProperty);
    }
}
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.test.BaseTest;

/**
 * This class tests all methods in MethodProperty class
 */
public class MethodPropertyTest extends BaseTest {

    @Test
    public void testMethodProperty() throws Exception {
        BeanPropertyDefinition propDef = createBeanPropertyDefinition("TestClass", "testProperty");
        JavaType type = mapper().getTypeFactory().constructType(Object.class);
        TypeDeserializer typeDeser = mapper().getConfig().getTypeDeserializers().findValueDeserializer(type, null);
        Annotations contextAnnotations = mapper().getAnnotationIntrospector().findAnnotation(TestClass.class);
        AnnotatedMethod method = new AnnotatedMethod(TestClass.class.getMethod("setTestProperty", Object.class), contextAnnotations);

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        // Test constructor with Name
        MethodProperty withName = methodProperty.withName(propertyName("TestProperty"));
        assertEquals(propertyName("TestProperty"), withName.getName());

        // Test constructor with ValueDeserializer
        JsonDeserializer<Object> valueDeserializer = mapper().findValueDeserializer(Object.class, null);
        MethodProperty withValueDeserializer = methodProperty.withValueDeserializer(valueDeserializer);
        assertEquals(valueDeserializer, withValueDeserializer.getValueDeserializer());

        // Test constructor with NullValueProvider
        NullValueProvider nullValueProvider = mapper().getConfig().nullValueProvider();
        MethodProperty withNullValueProvider = methodProperty.withNullValueProvider(nullValueProvider);
        assertEquals(nullValueProvider, withNullValueProvider.getNullProvider());

        // Test fixAccess
        AnnotatedMember member = methodProperty.getMember();
        member.fixAccess(mapper().getConfig());
        assertTrue(member.isAccessible());

        // Test getter
        assertEquals(methodProperty.getMember(), methodProperty.getMember());

        // Test deserializeAndSet
        JsonParser mockParser = mockParser("{\"testProperty\": 1}");
        Object instance = new TestClass();
        methodProperty.deserializeAndSet(mockParser, context(), instance);
        assertEquals(1, instance.getTestProperty());

        // Test deserializeSetAndReturn
        assertEquals(instance, methodProperty.deserializeSetAndReturn(mockParser, context(), instance));

        // Test set
        Object value = new Object();
        methodProperty.set(instance, value);
        assertEquals(value, instance.getTestProperty());

        // Test setAndReturn
        Object result = methodProperty.setAndReturn(instance, value);
        assertEquals(value, result);

        // Test readResolve
        Object readResolve = methodProperty.readResolve();
        assertTrue(readResolve instanceof MethodProperty);
    }

    @Test
    public void testMethodPropertyWithAnnotation() throws Exception {
        BeanPropertyDefinition propDef = createBeanPropertyDefinition("TestClass", "testPropertyWithAnnotation");
        JavaType type = mapper().getTypeFactory().constructType(String.class);
        TypeDeserializer typeDeser = mapper().getConfig().getTypeDeserializers().findValueDeserializer(type, null);
        Annotations contextAnnotations = mapper().getAnnotationIntrospector().findAnnotation(TestClass.class);
        AnnotatedMethod method = new AnnotatedMethod(TestClass.class.getMethod("setTestPropertyWithAnnotation", String.class), contextAnnotations);
        method.addAnnotation(new MyAnnotation());

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        // Test getAnnotation
        MyAnnotation myAnnotation = methodProperty.getAnnotation(MyAnnotation.class);
        assertNotNull(myAnnotation);

        // Test deserializeAndSet with annotation
        JsonParser mockParser = mockParser("{\"testPropertyWithAnnotation\": \"test\"}");
        TestClass instance = new TestClass();
        methodProperty.deserializeAndSet(mockParser, context(), instance);
        assertEquals("test", instance.getTestPropertyWithAnnotation());

        // Test deserializeSetAndReturn with annotation
        assertEquals(instance, methodProperty.deserializeSetAndReturn(mockParser, context(), instance));

        // Test set with annotation
        String value = "test";
        methodProperty.set(instance, value);
        assertEquals(value, instance.getTestPropertyWithAnnotation());

        // Test setAndReturn with annotation
        Object result = methodProperty.setAndReturn(instance, value);
        assertEquals(value, result);
    }

    @Test
    public void testMethodPropertyWithTypeDeserializer() throws Exception {
        BeanPropertyDefinition propDef = createBeanPropertyDefinition("TestClass", "testPropertyWithTypeDeserializer");
        JavaType type = mapper().getTypeFactory().constructType(TestClassWithTypeDeserializer.class);
        TypeDeserializer typeDeser = new CustomTypeDeserializer();

        Annotations contextAnnotations = mapper().getAnnotationIntrospector().findAnnotation(TestClassWithTypeDeserializer.class);
        AnnotatedMethod method = new AnnotatedMethod(TestClassWithTypeDeserializer.class.getMethod("setTestPropertyWithTypeDeserializer", Object.class), contextAnnotations);

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        // Test deserializeAndSet with type deserializer
        JsonParser mockParser = mockParser("{\"testPropertyWithTypeDeserializer\": 1}");
        TestClassWithTypeDeserializer instance = new TestClassWithTypeDeserializer();
        methodProperty.deserializeAndSet(mockParser, context(), instance);
        assertEquals(1, instance.getTestPropertyWithTypeDeserializer());

        // Test deserializeSetAndReturn with type deserializer
        assertEquals(instance, methodProperty.deserializeSetAndReturn(mockParser, context(), instance));

        // Test set with type deserializer
        Object value = new Object();
        methodProperty.set(instance, value);
        assertEquals(value, instance.getTestPropertyWithTypeDeserializer());

        // Test setAndReturn with type deserializer
        Object result = methodProperty.setAndReturn(instance, value);
        assertEquals(value, result);
    }
}

class TestClass {
    private int testProperty;
    private String testPropertyWithAnnotation;

    @MyAnnotation
    public void setTestPropertyWithAnnotation(String value) {
        this.testPropertyWithAnnotation = value;
    }

    public int getTestProperty() {
        return testProperty;
    }

    public void setTestProperty(int value) {
        this.testProperty = value;
    }

    public String getTestPropertyWithAnnotation() {
        return testPropertyWithAnnotation;
    }
}

class TestClassWithTypeDeserializer {
    private int testPropertyWithTypeDeserializer;

    public void setTestPropertyWithTypeDeserializer(Object value) {
        this.testPropertyWithTypeDeserializer = ((Number) value).intValue();
    }

    public int getTestPropertyWithTypeDeserializer() {
        return testPropertyWithTypeDeserializer;
    }
}

class CustomTypeDeserializer extends TypeDeserializer {
    @Override
    public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer vd) throws IOException {
        Object deserialized = vd.deserializeWithType(p, ctxt, null);
        return deserialized instanceof Number ? ((Number) deserialized).intValue() : deserialized;
    }
}

class MyAnnotation extends Annenotation {}
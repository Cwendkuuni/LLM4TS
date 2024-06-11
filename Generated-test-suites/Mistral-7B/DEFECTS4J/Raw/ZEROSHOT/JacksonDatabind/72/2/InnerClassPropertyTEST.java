package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseTest;

public class InnerClassPropertyTest extends BaseTest {

    public static class TestInnerClass {
        private String name;

        public TestInnerClass() {}

        public TestInnerClass(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @SuppressWarnings("unchecked")
    public static class TestJsonDeserializer extends JsonDeserializer<TestInnerClass> {
        @Override
        public TestInnerClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String name = p.getValueAsString();
            return new TestInnerClass(name);
        }
    }

    public void testInnerClassProperty() throws IOException {
        Class<?> clazz = TestInnerClass.class;
        AnnotatedMember member = new AnnotatedMember(TestInnerClass.class.getDeclaredField("name"), null, null);
        PropertyName propertyName = new PropertyName("name");
        ObjectMapper objectMapper = new ObjectMapper();

        SettableBeanProperty delegate = objectMapper.findProperty("name", clazz);
        Constructor<?> constructor = TestInnerClass.class.getDeclaredConstructor(String.class);
        AnnotatedConstructor annotatedConstructor = objectMapper.getAnnotationIntrospector().findPropertyConstructor(member, constructor);

        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, constructor);
        JsonDeserializer<TestInnerClass> jsonDeserializer = new TestJsonDeserializer();

        // Test with value instantiation using default constructor
        JsonParser jsonParser = createParser(objectMapper, "[\"John\"]");
        Object testInstance = new Object();
        innerClassProperty.deserializeAndSet(jsonParser, context, testInstance);
        TestInnerClass expectedValue = new TestInnerClass("John");
        TestInnerClass actualValue = (TestInnerClass) testInstance;
        assertEquals(expectedValue.getName(), actualValue.getName());

        // Test with value instantiated using single-arg constructor
        JsonDeserializer<TestInnerClass> deserializer = new TestJsonDeserializer();
        innerClassProperty = new InnerClassProperty(delegate, annotatedConstructor, jsonDeserializer);
        jsonParser = createParser(objectMapper, "[\"Jane\"]");
        testInstance = new Object();
        innerClassProperty.deserializeAndSet(jsonParser, context, testInstance);
        actualValue = (TestInnerClass) testInstance;
        assertEquals(expectedValue.getName(), actualValue.getName());

        // Test with JDK Serialization
        Object testInstanceSerialized = innerClassProperty.readResolve();
        TestInnerClass actualValueSerialized = (TestInnerClass) testInstanceSerialized;
        assertEquals(expectedValue.getName(), actualValueSerialized.getName());

        // Test with writing and reading back
        Object testInstanceWritten = innerClassProperty.writeReplace();
        TestInnerClass actualValueWritten = (TestInnerClass) testInstanceWritten;
        assertEquals(expectedValue.getName(), actualValueWritten.getName());

        // Test with setting value using setter
        TestInnerClass testInstanceWithSetter = new TestInnerClass("Bob");
        innerClassProperty = new InnerClassProperty(innerClassProperty, propertyName);
        innerClassProperty.set(testInstanceWithSetter, expectedValue);
        assertEquals(expectedValue.getName(), testInstanceWithSetter.getName());
    }

    @Test
    public void testInnerClassPropertyWithAnnotation() throws IOException {
        Class<?> clazz = TestAnnotationInnerClass.class;
        AnnotatedMember member = new AnnotatedMember(TestAnnotationInnerClass.class.getDeclaredField("name"), null, null);
        PropertyName propertyName = new PropertyName("name");
        ObjectMapper objectMapper = new ObjectMapper();

        SettableBeanProperty delegate = objectMapper.findProperty("name", clazz);
        Constructor<?> constructor = TestAnnotationInnerClass.class.getDeclaredConstructor(String.class);
        AnnotatedConstructor annotatedConstructor = objectMapper.getAnnotationIntrospector().findPropertyConstructor(member, constructor);

        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, constructor);
        JsonDeserializer<TestAnnotationInnerClass> jsonDeserializer = new TestJsonDeserializerWithAnnotation();

        // Test with value instantiation using default constructor
        JsonParser jsonParser = createParser(objectMapper, "[\"John\"]");
        Object testInstance = new Object();
        innerClassProperty.deserializeAndSet(jsonParser, context, testInstance);
        TestAnnotationInnerClass expectedValue = new TestAnnotationInnerClass("John");
        TestAnnotationInnerClass actualValue = (TestAnnotationInnerClass) testInstance;
        assertEquals(expectedValue.getName(), actualValue.getName());
        assertEquals("John", actualValue.getAnnotatedAnnotation(MyAnnotation.class).value());

        // Test with value instantiated using single-arg constructor
        JsonDeserializer<TestAnnotationInnerClass> deserializer = new TestJsonDeserializerWithAnnotation();
        innerClassProperty = new InnerClassProperty(delegate, annotatedConstructor, jsonDeserializer);
        jsonParser = createParser(objectMapper, "[\"Jane\"]");
        testInstance = new Object();
        innerClassProperty.deserializeAndSet(jsonParser, context, testInstance);
        actualValue = (TestAnnotationInnerClass) testInstance;
        assertEquals(expectedValue.getName(), actualValue.getName());
        assertEquals("Jane", actualValue.getAnnotatedAnnotation(MyAnnotation.class).value());

        // Test with JDK Serialization
        Object testInstanceSerialized = innerClassProperty.readResolve();
        TestAnnotationInnerClass actualValueSerialized = (TestAnnotationInnerClass) testInstanceSerialized;
        assertEquals(expectedValue.getName(), actualValueSerialized.getName());
        assertEquals("John", actualValueSerialized.getAnnotatedAnnotation(MyAnnotation.class).value());

        // Test with writing and reading back
        Object testInstanceWritten = innerClassProperty.writeReplace();
        TestAnnotationInnerClass actualValueWritten = (TestAnnotationInnerClass) testInstanceWritten;
        assertEquals(expectedValue.getName(), actualValueWritten.getName());
        assertEquals("John", actualValueWritten.getAnnotatedAnnotation(MyAnnotation.class).value());

        // Test with setting value using setter
        TestAnnotationInnerClass testInstanceWithSetter = new TestAnnotationInnerClass("Bob");
        innerClassProperty = new InnerClassProperty(innerClassProperty, propertyName);
        innerClassProperty.set(testInstanceWithSetter, expectedValue);
        assertEquals(expectedValue.getName(), testInstanceWithSetter.getName());
        assertEquals("Bob", testInstanceWithSetter.getAnnotatedAnnotation(MyAnnotation.class).value());
    }

    @Test
    public void testInnerClassPropertyWithNullValue() throws IOException {
        Class<?> clazz = TestNullInnerClass.class;
        AnnotatedMember member = new AnnotatedMember(TestNullInnerClass.class.getDeclaredField("name"), null, null);
        PropertyName propertyName = new PropertyName("name");
        ObjectMapper objectMapper = new ObjectMapper();

        SettableBeanProperty delegate = objectMapper.findProperty("name", clazz);
        Constructor<?> constructor = TestNullInnerClass.class.getDeclaredConstructor(String.class);
        AnnotatedConstructor annotatedConstructor = objectMapper.getAnnotationIntrospector().findPropertyConstructor(member, constructor);

        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, constructor);
        JsonDeserializer<TestNullInnerClass> jsonDeserializer = new TestJsonDeserializerWithNullValue();

        // Test with value instantiation using default constructor
        JsonParser jsonParser = createParser(objectMapper, "null");
        Object testInstance = new Object();
        innerClassProperty.deserializeAndSet(jsonParser, context, testInstance);
        TestNullInnerClass expectedValue = null;
        TestNullInnerClass actualValue = (TestNullInnerClass) testInstance;
        assertNull(actualValue);

        // Test with value instantiated using single-arg constructor
        JsonDeserializer<TestNullInnerClass> deserializer = new TestJsonDeserializerWithNullValue();
        innerClassProperty = new InnerClassProperty(delegate, annotatedConstructor, jsonDeserializer);
        jsonParser = createParser(objectMapper, "null");
        testInstance = new Object();
        innerClassProperty.deserializeAndSet(jsonParser, context, testInstance);
        actualValue = (TestNullInnerClass) testInstance;
        assertNull(actualValue);

        // Test with JDK Serialization
        Object testInstanceSerialized = innerClassProperty.readResolve();
        TestNullInnerClass actualValueSerialized = (TestNullInnerClass) testInstanceSerialized;
        assertNull(actualValueSerialized);

        // Test with writing and reading back
        Object testInstanceWritten = innerClassProperty.writeReplace();
        TestNullInnerClass actualValueWritten = (TestNullInnerClass) testInstanceWritten;
        assertNull(actualValueWritten);
    }

    @Test
    public void testInnerClassPropertyWithIllegalArgumentException() throws IOException {
        Class<?> clazz = TestNullInnerClass.class;
        AnnotatedMember member = new AnnotatedMember(TestNullInnerClass.class.getDeclaredField("name"), null, null);
        PropertyName propertyName = new PropertyName("name");
        ObjectMapper objectMapper = new ObjectMapper();

        SettableBeanProperty delegate = objectMapper.findProperty("name", clazz);
        Constructor<?> constructor = TestNullInnerClass.class.getDeclaredConstructor(String.class);
        AnnotatedConstructor annotatedConstructor = objectMapper.getAnnotationIntrospector().findPropertyConstructor(member, constructor);

        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, constructor);
        JsonDeserializer<TestNullInnerClass> jsonDeserializer = new TestJsonDeserializerWithIllegalArgumentException();

        // Test with value instantiation using default constructor
        JsonParser jsonParser = createParser(objectMapper, "[\"John\"]");
        Object testInstance = new Object();
        try {
            innerClassProperty.deserializeAndSet(jsonParser, context, testInstance);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test with value instantiated using single-arg constructor
        JsonDeserializer<TestNullInnerClass> deserializer = new TestJsonDeserializerWithIllegalArgumentException();
        innerClassProperty = new InnerClassProperty(delegate, annotatedConstructor, jsonDeserializer);
        jsonParser = createParser(objectMapper, "[\"Jane\"]");
        testInstance = new Object();
        try {
            innerClassProperty.deserializeAndSet(jsonParser, context, testInstance);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test with JDK Serialization
        Object testInstanceSerialized = innerClassProperty.readResolve();
        try {
            TestNullInnerClass actualValueSerialized = (TestNullInnerClass) testInstanceSerialized;
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test with writing and reading back
        Object testInstanceWritten = innerClassProperty.writeReplace();
        try {
            TestNullInnerClass actualValueWritten = (TestNullInnerClass) testInstanceWritten;
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    private static JsonParser createParser(ObjectMapper objectMapper, String json) throws IOException {
        return objectMapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
    }

    static class TestJsonDeserializerWithAnnotation extends JsonDeserializer<TestAnnotationInnerClass> {
        @Override
        public TestAnnotationInnerClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String name = p.getValueAsString();
            MyAnnotation annotation = p.getCodec().treeAsTokens(p.getCodec().readTree(p.readValueAsCharArray())).nextValue().getAnnotation(MyAnnotation.class);
            return new TestAnnotationInnerClass(name, annotation);
        }
    }

    static class TestJsonDeserializerWithNullValue extends JsonDeserializer<TestNullInnerClass> {
        @Override
        public TestNullInnerClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }

    static class TestJsonDeserializerWithIllegalArgumentException extends JsonDeserializer<TestNullInnerClass> {
        @Override
        public TestNullInnerClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            throw new IllegalArgumentException("Invalid input");
        }
    }

    static class TestAnnotationInnerClass {
        private String name;

        public TestAnnotationInnerClass(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public MyAnnotation getAnnotatedAnnotation(Class<? extends Annotation> annotationType) {
            return (MyAnnotation) this.getClass().getAnnotation(annotationType);
        }
    }

    static class TestNullInnerClass {
        // Empty class
    }

    @MyAnnotation(value = "John")
    static class MyAnnotation {
        private String value;

        MyAnnotation(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }
}
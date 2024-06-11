package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedParameter;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class CreatorCollectorTest {

    @Test
    public void testCreatorCollector() {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(TestClass.class);

        // When
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        // Then
        assertTrue(collector instanceof CreatorCollector);

        // Method: setDefaultCreator
        Method defaultCreatorMethod = TestClass.class.getDeclaredMethod("defaultConstructor");
        AnnotatedWithParams defaultConstructor = new AnnotatedWithParams(defaultCreatorMethod, null, null);
        collector.setDefaultCreator(defaultConstructor);
        AnnotatedWithParams defaultCreator = collector.getCreatorForProperty("defaultConstructor");
        assertEquals(defaultConstructor, defaultCreator);

        // Method: addStringCreator
        Method stringCreatorMethod = TestClass.class.getDeclaredMethod("stringCreator");
        AnnotatedElement stringCreatorElement = stringCreatorMethod.getAnnotatedDeclaringClass();
        AnnotatedType stringCreatorAnnotatedType = stringCreatorElement.getAnnotation(JsonCreator.class);
        AnnotatedWithParams stringCreator = new AnnotatedWithParams(stringCreatorMethod, stringCreatorAnnotatedType, null);
        collector.addStringCreator(stringCreator, false);
        AnnotatedWithParams stringCreatorFromCollector = collector.getCreatorForProperty("stringCreator");
        assertEquals(stringCreator, stringCreatorFromCollector);

        // Method: addIntCreator
        Method intCreatorMethod = TestClass.class.getDeclaredMethod("intCreator");
        AnnotatedElement intCreatorElement = intCreatorMethod.getAnnotatedDeclaringClass();
        AnnotatedType intCreatorAnnotatedType = intCreatorElement.getAnnotation(JsonCreator.class);
        AnnotatedWithParams intCreator = new AnnotatedWithParams(intCreatorMethod, intCreatorAnnotatedType, null);
        collector.addIntCreator(intCreator, false);
        AnnotatedWithParams intCreatorFromCollector = collector.getCreatorForProperty("intCreator");
        assertEquals(intCreator, intCreatorFromCollector);

        // Method: addLongCreator
        Method longCreatorMethod = TestClass.class.getDeclaredMethod("longCreator");
        AnnotatedElement longCreatorElement = longCreatorMethod.getAnnotatedDeclaringClass();
        AnnotatedType longCreatorAnnotatedType = longCreatorElement.getAnnotation(JsonCreator.class);
        AnnotatedWithParams longCreator = new AnnotatedWithParams(longCreatorMethod, longCreatorAnnotatedType, null);
        collector.addLongCreator(longCreator, false);
        AnnotatedWithParams longCreatorFromCollector = collector.getCreatorForProperty("longCreator");
        assertEquals(longCreator, longCreatorFromCollector);

        // Method: addDoubleCreator
        Method doubleCreatorMethod = TestClass.class.getDeclaredMethod("doubleCreator");
        AnnotatedElement doubleCreatorElement = doubleCreatorMethod.getAnnotatedDeclaringClass();
        AnnotatedType doubleCreatorAnnotatedType = doubleCreatorElement.getAnnotation(JsonCreator.class);
        AnnotatedWithParams doubleCreator = new AnnotatedWithParams(doubleCreatorMethod, doubleCreatorAnnotatedType, null);
        collector.addDoubleCreator(doubleCreator, false);
        AnnotatedWithParams doubleCreatorFromCollector = collector.getCreatorForProperty("doubleCreator");
        assertEquals(doubleCreator, doubleCreatorFromCollector);

        // Method: addBooleanCreator
        Method booleanCreatorMethod = TestClass.class.getDeclaredMethod("booleanCreator");
        AnnotatedElement booleanCreatorElement = booleanCreatorMethod.getAnnotatedDeclaringClass();
        AnnotatedType booleanCreatorAnnotatedType = booleanCreatorElement.getAnnotation(JsonCreator.class);
        AnnotatedWithParams booleanCreator = new AnnotatedWithParams(booleanCreatorMethod, booleanCreatorAnnotatedType, null);
        collector.addBooleanCreator(booleanCreator, false);
        AnnotatedWithParams booleanCreatorFromCollector = collector.getCreatorForProperty("booleanCreator");
        assertEquals(booleanCreator, booleanCreatorFromCollector);

        // Method: addDelegatingCreator
        TestClassDelegate testClassDelegate = new TestClassDelegate();
        Method delegatingCreatorMethod = TestClass.class.getDeclaredMethod("delegatingConstructor", TestClassDelegate.class);
        AnnotatedElement delegatingCreatorElement = delegatingCreatorMethod.getAnnotatedDeclaringClass();
        AnnotatedType delegatingCreatorAnnotatedType = null;
        if (delegatingCreatorElement.isAnnotated()) {
            delegatingCreatorAnnotatedType = delegatingCreatorElement.getAnnotation(JsonCreator.class);
        }
        AnnotatedWithParams delegatingCreator = new AnnotatedWithParams(delegatingCreatorMethod, delegatingCreatorAnnotatedType, testClassDelegate);
        AnnotatedParameter[] delegatingCreatorParameters = delegatingCreatorMethod.getParameters();
        CreatorProperty[] delegatingCreatorInjectables = new CreatorProperty[delegatingCreatorParameters.length];
        for (int i = 0; i < delegatingCreatorParameters.length; i++) {
            AnnotatedParameter parameter = delegatingCreatorParameters[i];
            CreatorProperty creatorProperty = new CreatorProperty(parameter.getType(), parameter.getName());
            delegatingCreatorInjectables[i] = creatorProperty;
        }
        collector.addDelegatingCreator(delegatingCreator, false, delegatingCreatorInjectables);
        AnnotatedWithParams delegatingCreatorFromCollector = collector.getCreatorForProperty("delegatingConstructor");
        assertEquals(delegatingCreator, delegatingCreatorFromCollector);

        // Method: addPropertyCreator
        TestClassProperty testClassProperty = new TestClassProperty();
        Class<?> testClassPropertyClass = testClassProperty.getClass();
        TestClassProperty testClassPropertyAnnotated = testClassPropertyClass.getAnnotation(JsonPropertyOrder.class);
        Method propertyCreatorMethod = TestClass.class.getDeclaredMethod("propertyConstructor", TestClassProperty.class);
        AnnotatedElement propertyCreatorElement = propertyCreatorMethod.getAnnotatedDeclaringClass();
        AnnotatedType propertyCreatorAnnotatedType = propertyCreatorElement.getAnnotation(JsonCreator.class);
        AnnotatedWithParams propertyCreator = new AnnotatedWithParams(propertyCreatorMethod, propertyCreatorAnnotatedType, testClassProperty);
        AnnotatedParameter propertyCreatorParameter = propertyCreatorMethod.getParameters()[0];
        CreatorProperty creatorProperty = new CreatorProperty(propertyCreatorParameter.getType(), propertyCreatorParameter.getName());
        CreatorProperty[] propertyCreatorInjectables = new CreatorProperty[]{creatorProperty};
        collector.addPropertyCreator(propertyCreator, false, propertyCreatorInjectables);
        AnnotatedWithParams propertyCreatorFromCollector = collector.getCreatorForProperty("propertyConstructor");
        assertEquals(propertyCreator, propertyCreatorFromCollector);

        // Edge case: null input
        try {
            collector.constructValueInstantiator(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Edge case: missing default constructor
        try {
            JavaType type = mapper.getTypeFactory().constructType(MissingDefaultConstructor.class);
            CreatorCollector collectorForMissingDefaultConstructor = new CreatorCollector(type.getBeanDescription(), true);
            ValueInstantiator inst = collectorForMissingDefaultConstructor.constructValueInstantiator(mapper.getConfig());
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        // Edge case: multiple default constructors
        try {
            JavaType type = mapper.getTypeFactory().constructType(MultipleDefaultConstructors.class);
            CreatorCollector collectorForMultipleDefaultConstructors = new CreatorCollector(type.getBeanDescription(), true);
            ValueInstantiator inst = collectorForMultipleDefaultConstructors.constructValueInstantiator(mapper.getConfig());
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    static class TestClass {
        TestClass() {}

        @JsonCreator(mode = Mode.PROCESS_ANNOTATIONS)
        TestClass(String string) {}

        @JsonCreator(mode = Mode.PROCESS_ANNOTATIONS)
        TestClass(int intValue) {}

        @JsonCreator(mode = Mode.PROCESS_ANNOTATIONS)
        TestClass(long longValue) {}

        @JsonCreator(mode = Mode.PROCESS_ANNOTATIONS)
        TestClass(double doubleValue) {}

        @JsonCreator(mode = Mode.PROCESS_ANNOTATIONS)
        TestClass(boolean booleanValue) {}

        @JsonCreator(mode = Mode.PROCESS_ANNOTATIONS)
        static TestClass defaultConstructor() { return new TestClass(); }

        @JsonCreator(mode = Mode.PROCESS_ANNOTATIONS)
        static TestClass stringCreator(@JsonProperty("string") String value) { return new TestClass(value); }

        @JsonCreator(mode = Mode.PROCESS_ANNOTATIONS)
        static TestClass intCreator(@JsonProperty("int") int value) { return new TestClass(value); }

        @JsonCreator(mode = Mode.PROCESS_ANNOTATIONS)
        static TestClass longCreator(@JsonProperty("long") long value) { return new TestClass(value); }

        @JsonCreator(mode = Mode.PROCESS_ANNOTATIONS)
        static TestClass doubleCreator(@JsonProperty("double") double value) { return new TestClass(value); }

        @JsonCreator(mode = Mode.PROCESS_ANNOTATIONS)
        static TestClass booleanCreator(@JsonProperty("boolean") boolean value) { return new TestClass(value); }

        @JsonCreator(mode = Mode.PROCESS_ANNOTATIONS)
        static TestClass delegatingConstructor(TestClassDelegate delegate) { return new TestClass(); }

        @JsonCreator(mode = Mode.PROCESS_ANNOTATIONS)
        static TestClass propertyConstructor(TestClassProperty property) { return new TestClass(); }

        // Delegate class
        static class TestClassDelegate {}

        // Property class
        static class TestClassProperty {
            @JsonPropertyOrder(value = 1)
            String propertyName;
        }
    }

    static class MissingDefaultConstructor {
        public MissingDefaultConstructor() {} // deliberate omission
    }

    static class MultipleDefaultConstructors {
        public MissingDefaultConstructor() {} // one default constructor

        public MultipleDefaultConstructors() {} // another default constructor
    }
}
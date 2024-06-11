package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.BeanPropertyMapper;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test class for CreatorCollector.java
 */
public class CreatorCollectorTest {

    @Test
    public void testCreatorCollector() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test with a simple class with default and string constructor
        Class<?> simpleClass = SimpleClass.class;

        BeanDescription beanDesc = mapper.getFactory().findBeanDescription(simpleClass, null);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        // Set default constructor
        AnnotatedConstructor constructor = simpleClass.getDeclaredConstructor();
        AnnotatedWithParams defaultConstructor = new AnnotatedWithParams(constructor, null);
        collector.setDefaultCreator(defaultConstructor);

        // Set string constructor
        Method stringMethod = simpleClass.getDeclaredMethod("stringCreator");
        AnnotatedMethod stringAnnotatedMethod = new AnnotatedMethod(stringMethod, null);
        AnnotatedWithParams stringCreator = new AnnotatedWithParams(stringAnnotatedMethod, new JsonCreator());
        collector.addStringCreator(stringCreator, true);

        // Test getValueInstantiator
        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        assertNotNull(instantiator);

        // Test with a class with multiple constructors
        Class<?> complexClass = ComplexClass.class;

        beanDesc = mapper.getFactory().findBeanDescription(complexClass, null);
        collector = new CreatorCollector(beanDesc, true);

        // Set default constructor
        constructor = complexClass.getDeclaredConstructor();
        defaultConstructor = new AnnotatedWithParams(constructor, null);
        collector.setDefaultCreator(defaultConstructor);

        // Set string constructor
        stringMethod = complexClass.getDeclaredMethod("stringConstructor");
        AnnotatedMethod stringAnnotatedMethod2 = new AnnotatedMethod(stringMethod, new Class[] { String.class });
        AnnotatedWithParams stringCreator2 = new AnnotatedWithParams(stringAnnotatedMethod2, new JsonCreator(), String.class);
        collector.addStringCreator(stringCreator2, true);

        // Set int constructor
        Method intMethod = complexClass.getDeclaredMethod("intConstructor");
        AnnotatedMethod intAnnotatedMethod = new AnnotatedMethod(intMethod, new Class[] { int.class });
        AnnotatedWithParams intCreator = new AnnotatedWithParams(intAnnotatedMethod, new JsonCreator(), int.class);
        collector.addIntCreator(intCreator, true);

        // Set long constructor
        Method longMethod = complexClass.getDeclaredMethod("longConstructor");
        AnnotatedMethod longAnnotatedMethod = new AnnotatedMethod(longMethod, new Class[] { long.class });
        AnnotatedWithParams longCreator = new AnnotatedWithParams(longAnnotatedMethod, new JsonCreator(), long.class);
        collector.addLongCreator(longCreator, true);

        // Set double constructor
        Method doubleMethod = complexClass.getDeclaredMethod("doubleConstructor");
        AnnotatedMethod doubleAnnotatedMethod = new AnnotatedMethod(doubleMethod, new Class[] { double.class });
        AnnotatedWithParams doubleCreator = new AnnotatedWithParams(doubleAnnotatedMethod, new JsonCreator(), double.class);
        collector.addDoubleCreator(doubleCreator, true);

        // Set boolean constructor
        Method booleanMethod = complexClass.getDeclaredMethod("booleanConstructor");
        AnnotatedMethod booleanAnnotatedMethod = new AnnotatedMethod(booleanMethod, new Class[] { boolean.class });
        AnnotatedWithParams booleanCreator2 = new AnnotatedWithParams(booleanAnnotatedMethod, new JsonCreator(), boolean.class);
        collector.addBooleanCreator(booleanCreator2, true);

        // Set delegating constructor
        Class<?> delegateClass = DelegateClass.class;
        AnnotatedConstructor delegateConstructor = delegateClass.getDeclaredConstructor(SimpleClass.class);
        AnnotatedWithParams delegateConstructorParams = new AnnotatedWithParams(delegateConstructor, null);
        CreatorProperty[] delegateArgs = new CreatorProperty[] { new CreatorProperty(SimpleClass.class, "simple") };
        collector.addDelegatingCreator(delegateConstructorParams, true, delegateArgs);

        // Set property-based constructor
        AnnotatedClass complexClassAnnotated = mapper.getAnnotatedClass(complexClass);
        BeanPropertyMapper propertyMapper = BeanPropertyMapper.getInstance();
        AnnotatedMethod propertyMethod = propertyMapper.findMethod(complexClassAnnotated, "propertyMethod");
        AnnotatedWithParams propertyConstructor = new AnnotatedWithParams(propertyMethod, null);
        CreatorProperty[] propertyBasedArgs = new CreatorProperty[] {
                new CreatorProperty("property1", String.class),
                new CreatorProperty("property2", Integer.class),
                new CreatorProperty("property3", Long.class)
        };
        collector.addPropertyCreator(propertyConstructor, true, propertyBasedArgs);

        // Test getValueInstantiator
        instantiator = collector.constructValueInstantiator(null);
        assertNotNull(instantiator);
    }

    /**
     * Helper class for AnnotatedWithParams.
     */
    static class AnnotatedWithParams {

        private final AnnotatedMember _member;
        private final Object[] _args;

        public AnnotatedWithParams(AnnotatedMember member, Object... args) {
            _member = member;
            _args = args;
        }

        public AnnotatedMember getAnnotated() {
            return _member;
        }

        public Class<?>[] getParameterTypes() {
            return _member.getParameterTypes();
        }

        public Object[] getArguments() {
            return _args;
        }

        public AnnotatedParameter[] getParameters() {
            return _member.getParameters();
        }

        public Class<?> getGenericParameterType(int index) {
            return _member.getGenericParameterTypes()[index];
        }
    }
}

class SimpleClass {

    public SimpleClass() {
    }

    public SimpleClass(String str) {
    }
}

class ComplexClass {

    public ComplexClass() {
    }

    public ComplexClass(String str) {
        this.str = str;
    }

    public ComplexClass(String str, int intVal) {
        this.str = str;
        this.intVal = intVal;
    }

    public ComplexClass(String str, int intVal, long longVal) {
        this.str = str;
        this.intVal = intVal;
        this.longVal = longVal;
    }

    public ComplexClass(String str, int intVal, boolean booleanVal) {
        this.str = str;
        this.intVal = intVal;
        this.booleanVal = booleanVal;
    }

    public ComplexClass(SimpleClass simple) {
        this.simple = simple;
    }

    public String propertyMethod(String property1, int property2, long property3) {
        return null;
    }

    private String str;
    private int intVal;
    private long longVal;
    private boolean booleanVal;
    private SimpleClass simple;
}

class DelegateClass {

    public DelegateClass(SimpleClass simple) {
        this.simple = simple;
    }

    private SimpleClass simple;
}
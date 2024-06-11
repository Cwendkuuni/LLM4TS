package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

    BeanDescription beanDesc;
    CreatorCollector collector;
    DeserializationConfig config;

    @Before
    public void setUp() throws Exception {
        config = new DeserializationConfig();
        Class<MyClass> clazz = MyClass.class;
        beanDesc = new BeanDescription(clazz);
        collector = new CreatorCollector(beanDesc, true);
    }

    @Test
    public void testDefaultCreator() throws Exception {
        Method defaultMethod = clazz.getDeclaredMethod("defaultConstructor");
        AnnotatedWithParams params = AnnotatedWithParams.standard(defaultMethod);
        collector.setDefaultCreator(params);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst);
    }

    @Test
    public void testStringCreator() throws Exception {
        Method stringMethod = clazz.getDeclaredMethod("stringConstructor", String.class);
        AnnotatedWithParams params = AnnotatedWithParams.standard(stringMethod);
        collector.addStringCreator(params, true);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst);
    }

    @Test
    public void testIntCreator() throws Exception {
        Method intMethod = clazz.getDeclaredMethod("intConstructor", int.class);
        AnnotatedWithParams params = AnnotatedWithParams.standard(intMethod);
        collector.addIntCreator(params, true);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst);
    }

    @Test
    public void testLongCreator() throws Exception {
        Method longMethod = clazz.getDeclaredMethod("longConstructor", long.class);
        AnnotatedWithParams params = AnnotatedWithParams.standard(longMethod);
        collector.addLongCreator(params, true);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst);
    }

    @Test
    public void testDoubleCreator() throws Exception {
        Method doubleMethod = clazz.getDeclaredMethod("doubleConstructor", double.class);
        AnnotatedWithParams params = AnnotatedWithParams.standard(doubleMethod);
        collector.addDoubleCreator(params, true);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst);
    }

    @Test
    public void testBooleanCreator() throws Exception {
        Method booleanMethod = clazz.getDeclaredMethod("booleanConstructor", boolean.class);
        AnnotatedWithParams params = AnnotatedWithParams.standard(booleanMethod);
        collector.addBooleanCreator(params, true);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst);
    }

    @Test
    public void testDelegateCreator() throws Exception {
        Method delegateMethod = clazz.getDeclaredMethod("delegateConstructor", String.class);
        AnnotatedWithParams params = AnnotatedWithParams.standard(delegateMethod);
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = new CreatorProperty(null, "injectable");
        collector.addDelegatingCreator(params, true, injectables);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst);
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        Method propertyBasedMethod = clazz.getDeclaredMethod("propertyBasedConstructor", String.class);
        AnnotatedWithParams params = AnnotatedWithParams.standard(propertyBasedMethod);
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = new CreatorProperty(null, "property1");
        properties[1] = new CreatorProperty(null, "property2");
        collector.addPropertyCreator(params, true, properties);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreator() throws Exception {
        Method stringMethod = clazz.getDeclaredMethod("stringConstructor", String.class);
        Method stringMethod2 = clazz.getDeclaredMethod("stringConstructor2", String.class);
        AnnotatedWithParams params = AnnotatedWithParams.standard(stringMethod);
        collector.addStringCreator(params, true);
        collector.addStringCreator(AnnotatedWithParams.standard(stringMethod2), true);
        collector.constructValueInstantiator(config);
    }
}

class MyClass {
    // Class under test, with various constructors and methods for testing.
}
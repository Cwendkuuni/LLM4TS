package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedParameter;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.deser.AnnotatedWithParams;
import com.fasterxml.jackson.databind.deser.StdValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator.ValueType;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;
    private BeanDescription beanDescription;
    private DeserializationConfig config;

    @Before
    public void setUp() throws Exception {
        beanDescription = new BeanDescription(new Class<?>[] { ArrayList.class }, null);
        config = new DeserializationConfig(new AnnotationIntrospector(), null);
        config.disable(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS);
        creatorCollector = new CreatorCollector(beanDescription, config);
    }

    @Test
    public void testDefaultCreator() {
        Method defaultCreator = ArrayList.class.getDefaultConstructor();
        AnnotatedWithParams annotatedDefaultCreator = new AnnotatedWithParams(defaultCreator, null);
        creatorCollector.setDefaultCreator(annotatedDefaultCreator);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(config);
        assertSame(ValueType.COLLECTION, instantiator.getValueType());
    }

    @Test
    public void testStringCreator() throws Exception {
        Method stringCreator = ArrayList.class.getDeclaredMethod("String constructor", String.class);
        AnnotatedWithParams annotatedStringCreator = new AnnotatedWithParams(stringCreator, null);
        creatorCollector.addStringCreator(annotatedStringCreator, false);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(config);
        assertSame(ValueType.STRING, instantiator.getValueType());
    }

    @Test
    public void testIntCreator() throws Exception {
        Method intCreator = ArrayList.class.getDeclaredMethod("Int constructor", int.class);
        AnnotatedWithParams annotatedIntCreator = new AnnotatedWithParams(intCreator, null);
        creatorCollector.addIntCreator(annotatedIntCreator, false);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(config);
        assertSame(ValueType.INT, instantiator.getValueType());
    }

    @Test
    public void testLongCreator() throws Exception {
        Method longCreator = ArrayList.class.getDeclaredMethod("Long constructor", long.class);
        AnnotatedWithParams annotatedLongCreator = new AnnotatedWithParams(longCreator, null);
        creatorCollector.addLongCreator(annotatedLongCreator, false);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(config);
        assertSame(ValueType.LONG, instantiator.getValueType());
    }

    @Test
    public void testDoubleCreator() throws Exception {
        Method doubleCreator = ArrayList.class.getDeclaredMethod("Double constructor", double.class);
        AnnotatedWithParams annotatedDoubleCreator = new AnnotatedWithParams(doubleCreator, null);
        creatorCollector.addDoubleCreator(annotatedDoubleCreator, false);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(config);
        assertSame(ValueType.DOUBLE, instantiator.getValueType());
    }

    @Test
    public void testBooleanCreator() throws Exception {
        Method booleanCreator = ArrayList.class.getDeclaredMethod("Boolean constructor", boolean.class);
        AnnotatedWithParams annotatedBooleanCreator = new AnnotatedWithParams(booleanCreator, null);
        creatorCollector.addBooleanCreator(annotatedBooleanCreator, false);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(config);
        assertSame(ValueType.BOOLEAN, instantiator.getValueType());
    }

    @Test
    public void testDelegatingCreator() throws Exception {
        Class<?> delegateType = Collection.class;
        Method delegatingCreator = ArrayList.class.getDeclaredMethod("Collection constructor", Collection.class);
        AnnotatedParameter[] delegateArgs = { new AnnotatedParameter(Collection.class.getDeclaredMethod("add", Object.class), null) };
        creatorCollector.addDelegatingCreator(delegatingCreator, false, delegateArgs);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(config);
        assertSame(ValueType.COLLECTION, instantiator.getValueType());
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        Class<?> propertyClass = String.class;
        Method propertyCreatorMethod = ArrayList.class.getDeclaredMethod("String property constructor", String.class);
        AnnotatedParameter[] propertyBasedArgs = { new AnnotatedParameter(propertyClass.getDeclaredField("size"), null) };
        creatorCollector.addPropertyCreator(propertyCreatorMethod, false, propertyBasedArgs);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(config);
        assertSame(ValueType.STRING, instantiator.getValueType());
    }

    @Test
    public void testNullDefaultCreator() {
        creatorCollector = new CreatorCollector(beanDescription, config);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(config);
        assertNull(instantiator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreator() throws Exception {
        Method stringCreator = ArrayList.class.getDeclaredMethod("String constructor", String.class);
        AnnotatedWithParams annotatedStringCreator = new AnnotatedWithParams(stringCreator, null);
        creatorCollector.addStringCreator(annotatedStringCreator, false);
        creatorCollector.addStringCreator(annotatedStringCreator, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncompatibleCreatorTypes() throws Exception {
        Method intCreator = ArrayList.class.getDeclaredMethod("Int constructor", int.class);
        Method stringCreator = ArrayList.class.getDeclaredMethod("String constructor", String.class);
        creatorCollector.addIntCreator(new AnnotatedWithParams(intCreator, null), false);
        creatorCollector.addStringCreator(new AnnotatedWithParams(stringCreator, null), false);
        creatorCollector.constructValueInstantiator(config);
    }
}
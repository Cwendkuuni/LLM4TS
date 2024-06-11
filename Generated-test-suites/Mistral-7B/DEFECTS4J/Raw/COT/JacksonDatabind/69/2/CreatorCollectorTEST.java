package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

public class CreatorCollectorTest {

    @SuppressWarnings("unchecked")
    private static <T> AnnotatedWithParams createTestCreator(Class<T> clazz, Object... args) {
        AnnotatedClass ac = new AnnotatedClass(clazz);
        Method m = findCreatorMethod(ac, args);
        return new AnnotatedWithParams(m, null, args);
    }

    private static Method findCreatorMethod(AnnotatedClass ac, Object... args) {
        Method[] ms = ac.getDeclaredMethods();
        for (Method m : ms) {
            if (m.isAnnotationPresent(JsonCreator.class)) {
                Class<?>[] ptypes = m.getParameterTypes();
                if (ptypes.length == args.length) {
                    for (int i = 0; i < args.length; i++) {
                        if (!ptypes[i].isAssignableFrom(args[i].getClass())) {
                            return null;
                        }
                    }
                    return m;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("result")
    private static Object createTestInstance(Class<?> clazz, Object... args) {
        ObjectMapper mapper = new ObjectMapper();
        MapperConfig<?> config = mapper.getConfig();
        BeanDescription beanDesc = BeanDescription.forDeserialization(clazz);
        CreatorCollector collector = new CreatorCollector(beanDesc, config);
        for (Object arg : args) {
            collector.addArgValue(arg);
        }
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        return inst.createValueInstance(null);
    }

    @org.junit.Test
    public void testDefaultCreator() {
        CreatorCollector collector = new CreatorCollector(null, null);
        Object result = collector.constructValueInstantiator(null).createValueInstance(null);
        assertNotNull(result);
    }

    @org.junit.Test
    public void testStringCreator() {
        String str = "test";
        Class<?> clazz = String.class;
        CreatorCollector collector = new CreatorCollector(null, null);
        AnnotatedWithParams creator = createTestCreator(clazz, str.getClass(), str);
        collector.addStringCreator(creator, false);
        Object result = createTestInstance(clazz, str);
        assertEquals(str, result);
    }

    @org.junit.Test
    public void testIntCreator() {
        int value = 1;
        Class<?> clazz = Integer.class;
        CreatorCollector collector = new CreatorCollector(null, null);
        AnnotatedWithParams creator = createTestCreator(clazz, value);
        collector.addIntCreator(creator, false);
        Object result = createTestInstance(clazz, value);
        assertEquals(value, result);
    }

    @org.junit.Test
    public void testLongCreator() {
        long value = 1;
        Class<?> clazz = Long.class;
        CreatorCollector collector = new CreatorCollector(null, null);
        AnnotatedWithParams creator = createTestCreator(clazz, value);
        collector.addLongCreator(creator, false);
        Object result = createTestInstance(clazz, value);
        assertEquals(value, result);
    }

    @org.junit.Test
    public void testDoubleCreator() {
        double value = 1.0;
        Class<?> clazz = Double.class;
        CreatorCollector collector = new CreatorCollector(null, null);
        AnnotatedWithParams creator = createTestCreator(clazz, value);
        collector.addDoubleCreator(creator, false);
        Object result = createTestInstance(clazz, value);
        assertEquals(value, result, 0.0);
    }

    @org.junit.Test
    public void testBooleanCreator() {
        boolean value = true;
        Class<?> clazz = Boolean.class;
        CreatorCollector collector = new CreatorCollector(null, null);
        AnnotatedWithParams creator = createTestCreator(clazz, value);
        collector.addBooleanCreator(creator, false);
        Object result = createTestInstance(clazz, value);
        assertEquals(value, result);
    }

    @org.junit.Test
    public void testDelegateCreator() {
        Class<?> delegateClass = String.class;
        Class<?> clazz = CreatorCollector.class;
        CreatorCollector collector = new CreatorCollector(null, null);
        AnnotatedWithParams creator = createTestCreator(clazz, delegateClass);
        SettableBeanProperty arg = new SettableBeanProperty("delegate", String.class);
        collector.addDelegatingCreator(creator, false, new SettableBeanProperty[]{arg});
        Object result = createTestInstance(clazz, arg.getValue());
        assertNotNull(result);
    }

    @org.junit.Test
    public void testPropertyBasedCreator() {
        Class<?> clazz = CreatorCollector.class;
        CreatorCollector collector = new CreatorCollector(null, null);
        SettableBeanProperty arg1 = new SettableBeanProperty("name", String.class);
        SettableBeanProperty arg2 = new SettableBeanProperty("value", Integer.class);
        CreatorProperty[] properties = new CreatorProperty[]{new CreatorProperty(arg1), new CreatorProperty(arg2)};
        collector.addPropertyCreator(null, false, properties);
        Object result = createTestInstance(clazz, arg1.getValue(), arg2.getValue());
        assertNotNull(result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testDuplicateStringCreator() {
        String str = "test";
        Class<?> clazz = String.class;
        CreatorCollector collector = new CreatorCollector(null, null);
        AnnotatedWithParams creator1 = createTestCreator(clazz, str.getClass(), str);
        collector.addStringCreator(creator1, false);
        AnnotatedWithParams creator2 = createTestCreator(clazz, str.getClass(), str);
        collector.addStringCreator(creator2, false);
        createTestInstance(clazz, str);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testDuplicateIntCreator() {
        int value = 1;
        Class<?> clazz = Integer.class;
        CreatorCollector collector = new CreatorCollector(null, null);
        AnnotatedWithParams creator1 = createTestCreator(clazz, value);
        collector.addIntCreator(creator1, false);
        AnnotatedWithParams creator2 = createTestCreator(clazz, value);
        collector.addIntCreator(creator2, false);
        createTestInstance(clazz, value);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testDuplicateLongCreator() {
        long value = 1;
        Class<?> clazz = Long.class;
        CreatorCollector collector = new CreatorCollector(null, null);
        AnnotatedWithParams creator1 = createTestCreator(clazz, value);
        collector.addLongCreator(creator1, false);
        AnnotatedWithParams creator2 = createTestCreator(clazz, value);
        collector.addLongCreator(creator2, false);
        createTestInstance(clazz, value);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testDuplicateDoubleCreator() {
        double value = 1.0;
        Class<?> clazz = Double.class;
        CreatorCollector collector = new CreatorCollector(null, null);
        AnnotatedWithParams creator1 = createTestCreator(clazz, value);
        collector.addDoubleCreator(creator1, false);
        AnnotatedWithParams creator2 = createTestCreator(clazz, value);
        collector.addDoubleCreator(creator2, false);
        createTestInstance(clazz, value);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testDuplicateBooleanCreator() {
        boolean value = true;
        Class<?> clazz = Boolean.class;
        CreatorCollector collector = new CreatorCollector(null, null);
        AnnotatedWithParams creator1 = createTestCreator(clazz, value);
        collector.addBooleanCreator(creator1, false);
        AnnotatedWithParams creator2 = createTestCreator(clazz, value);
        collector.addBooleanCreator(creator2, false);
        createTestInstance(clazz, value);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testDuplicateDelegateCreator() {
        Class<?> delegateClass = String.class;
        Class<?> clazz = CreatorCollector.class;
        CreatorCollector collector = new CreatorCollector(null, null);
        AnnotatedWithParams creator1 = createTestCreator(clazz, delegateClass);
        SettableBeanProperty arg = new SettableBeanProperty("delegate", String.class);
        collector.addDelegatingCreator(creator1, false, new SettableBeanProperty[]{arg});
        AnnotatedWithParams creator2 = createTestCreator(clazz, delegateClass);
        SettableBeanProperty arg2 = new SettableBeanProperty("delegate", String.class);
        collector.addDelegatingCreator(creator2, false, new SettableBeanProperty[]{arg2});
        createTestInstance(clazz, arg.getValue());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testDuplicatePropertyBasedCreator() {
        Class<?> clazz = CreatorCollector.class;
        CreatorCollector collector = new CreatorCollector(null, null);
        SettableBeanProperty arg1 = new SettableBeanProperty("name", String.class);
        SettableBeanProperty arg2 = new SettableBeanProperty("name", String.class);
        CreatorProperty[] properties = new CreatorProperty[]{new CreatorProperty(arg1), new CreatorProperty(arg2)};
        collector.addPropertyCreator(null, false, properties);
        createTestInstance(clazz, arg1.getValue(), arg2.getValue());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testIncompatibleDelegateTypes() {
        Class<?> delegateClass1 = String.class;
        Class<?> delegateClass2 = List.class;
        Class<?> clazz = CreatorCollector.class;
        CreatorCollector collector = new CreatorCollector(null, null);
        AnnotatedWithParams creator = createTestCreator(clazz, delegateClass1);
        SettableBeanProperty arg = new SettableBeanProperty("delegate", delegateClass1.getClass());
        collector.addDelegatingCreator(creator, false, new SettableBeanProperty[]{arg});
        createTestInstance(clazz, arg.getValue());
        AnnotatedWithParams creator2 = createTestCreator(clazz, delegateClass2);
        SettableBeanProperty arg2 = new SettableBeanProperty("delegate", delegateClass2.getClass());
        collector.addDelegatingCreator(creator2, false, new SettableBeanProperty[]{arg2});
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testIncompleteParameter() {
        Class<?> clazz = CreatorCollector.class;
        CreatorCollector collector = new CreatorCollector(null, null);
        AnnotatedParameter param = new AnnotatedParameter(new Method("test", Integer.TYPE), 0);
        collector.addIncompeteParameter(param);
        createTestInstance(clazz, 1);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testMissingCreator() {
        Class<?> clazz = CreatorCollector.class;
        CreatorCollector collector = new CreatorCollector(null, null);
        Object result = collector.constructValueInstantiator(null).createValueInstance(null);
        assertNull(result);
    }

    @org.junit.Test
    public void testCustomCreator() {
        Class<?> clazz = CreatorCollectorCustom.class;
        CreatorCollector collector = new CreatorCollector(null, null);
        String value = "test";
        CreatorProperty arg1 = new CreatorProperty("arg1", String.class);
        CreatorProperty arg2 = new CreatorProperty("arg2", Integer.class);
        Object[] args = {value, 1};
        AnnotatedWithParams creator = new AnnotatedWithParams(CreatorCollectorCustom.class.getMethod("customCreator", AnnotatedClass.class, CreatorProperty[].class), null, args);
        collector.addPropertyCreator(creator, true, new CreatorProperty[]{arg1, arg2});
        Object result = createTestInstance(clazz, value, 1);
        assertEquals(value, result);
        assertEquals(1, result);
    }
}

class CreatorCollectorCustom {
    @JsonCreator
    public static CreatorCollectorCustom customCreator(AnnotatedClass ac, CreatorProperty[] properties) {
        CreatorCollectorCustom inst = new CreatorCollectorCustom();
        inst.setArg1(ac.getShortClassName());
        inst.setArg2(properties[0].getAnnotationInstance().getValue());
        inst.setArg3(properties[1].getAnnotationInstance().getValue());
        return inst;
    }

    private String arg1;
    private String arg2;
    private int arg3;

    public void setArg1(String value) {
        this.arg1 = value;
    }

    public void setArg2(String value) {
        this.arg2 = value;
    }

    public void setArg3(int value) {
        this.arg3 = value;
    }
}
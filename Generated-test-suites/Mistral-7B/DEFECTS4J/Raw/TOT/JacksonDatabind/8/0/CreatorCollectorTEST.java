package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

    @SuppressWarnings("unchecked")
    static class TestBean {
        private TestBean() {}

        @JsonCreator
        public static TestBean createWithString(String s) {
            return new TestBean();
        }

        @JsonCreator
        public static TestBean createWithInt(int i) {
            return new TestBean();
        }

        @JsonCreator
        public static TestBean createWithDefault() {
            return new TestBean();
        }

        @JsonCreator
        public static TestBean createWithBoolean(boolean b) {
            return new TestBean();
        }
    }

    @SuppressWarnings("unchecked")
    static class TestBeanWithDelegate {
        private int delegate;

        @JsonCreator
        public static TestBeanWithDelegate createWithDelegateAndInt(int delegate, int i) {
            TestBeanWithDelegate result = new TestBeanWithDelegate();
            result.delegate = delegate;
            return result;
        }
    }

    @SuppressWarnings("unchecked")
    static class TestBeanWithProperties {
        private String name;
        private int age;

        @JsonCreator
        public static TestBeanWithProperties createWithProperties(@JsonProperty("name") String name,
                @JsonProperty("age") int age) {
            TestBeanWithProperties result = new TestBeanWithProperties();
            result.name = name;
            result.age = age;
            return result;
        }
    }

    @Test
    public void testCreatorCollectorDefault() throws IOException {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        Object value = instantiator.createValue(new DeserializationContext(new TestCaseBase()), null);
        assertEquals(TestBean.class, value.getClass());
    }

    @Test
    public void testCreatorCollectorString() throws IOException {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        collector.addStringCreator(TestBean.class.getDeclaredMethod("createWithString"), true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        Object value = instantiator.createValue(new DeserializationContext(new TestCaseBase()), new String[]{"Test"});
        assertEquals(TestBean.class, value.getClass());
    }

    @Test
    public void testCreatorCollectorInt() throws IOException {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        collector.addIntCreator(TestBean.class.getDeclaredMethod("createWithInt"), true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        Object value = instantiator.createValue(new DeserializationContext(new TestCaseBase()), new Integer(42));
        assertEquals(TestBean.class, value.getClass());
    }

    @Test
    public void testCreatorCollectorBoolean() throws IOException {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        collector.addBooleanCreator(TestBean.class.getDeclaredMethod("createWithBoolean"), true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        Object value = instantiator.createValue(new DeserializationContext(new TestCaseBase()), new Boolean(true));
        assertEquals(TestBean.class, value.getClass());
    }

    @Test
    public void testCreatorCollectorDelegate() throws IOException {
        BeanDescription beanDesc = new BeanDescription(TestBeanWithDelegate.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        Method createWithDelegate = TestBeanWithDelegate.class.getDeclaredMethod("createWithDelegateAndInt");
        collector.addDelegatingCreator(createWithDelegate, new CreatorProperty[0], (CreatorProperty[])null);
        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        Object value = instantiator.createValue(new DeserializationContext(new TestCaseBase()), new Object[]{1});
        assertEquals(TestBeanWithDelegate.class, value.getClass());
        assertEquals(1, ((TestBeanWithDelegate)value).delegate);
    }

    @Test
    public void testCreatorCollectorProperties() throws IOException {
        BeanDescription beanDesc = new BeanDescription(TestBeanWithProperties.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        Method createWithProperties = TestBeanWithProperties.class.getDeclaredMethod("createWithProperties");
        collector.addPropertyCreator(createWithProperties, new CreatorProperty[] {new CreatorProperty("name", String.class), new CreatorProperty("age", Integer.class)}, (CreatorProperty[])null);
        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        Object value = instantiator.createValue(new DeserializationContext(new TestCaseBase()), new Object[]{"TestName", 42});
        assertEquals(TestBeanWithProperties.class, value.getClass());
        assertEquals("TestName", ((TestBeanWithProperties)value).name);
        assertEquals(42, ((TestBeanWithProperties)value).age);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatorCollectorDuplicateIntCreator() throws IOException {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        collector.addIntCreator(TestBean.class.getDeclaredMethod("createWithInt"), true);
        collector.addIntCreator(TestBean.class.getDeclaredMethod("createWithInt"), false);
        collector.constructValueInstantiator(new DeserializationConfig());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatorCollectorDuplicateStringCreator() throws IOException {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        collector.addStringCreator(TestBean.class.getDeclaredMethod("createWithString"), true);
        collector.addStringCreator(TestBean.class.getDeclaredMethod("createWithString"), false);
        collector.constructValueInstantiator(new DeserializationConfig());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatorCollectorDuplicateDelegateCreator() throws IOException {
        BeanDescription beanDesc = new BeanDescription(TestBeanWithDelegate.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        Method createWithDelegate = TestBeanWithDelegate.class.getDeclaredMethod("createWithDelegateAndInt");
        collector.addDelegatingCreator(createWithDelegate, new CreatorProperty[0], (CreatorProperty[])null);
        collector.addDelegatingCreator(createWithDelegate, new CreatorProperty[0], (CreatorProperty[])null);
        collector.constructValueInstantiator(new DeserializationConfig());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatorCollectorDuplicatePropertiesCreator() throws IOException {
        BeanDescription beanDesc = new BeanDescription(TestBeanWithProperties.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        Method createWithProperties = TestBeanWithProperties.class.getDeclaredMethod("createWithProperties");
        collector.addPropertyCreator(createWithProperties, new CreatorProperty[] {new CreatorProperty("name", String.class), new CreatorProperty("age", Integer.class)}, (CreatorProperty[])null);
        collector.addPropertyCreator(createWithProperties, new CreatorProperty[] {new CreatorProperty("name", String.class), new CreatorProperty("age", Integer.class)}, (CreatorProperty[])null);
        collector.constructValueInstantiator(new DeserializationConfig());
    }
}
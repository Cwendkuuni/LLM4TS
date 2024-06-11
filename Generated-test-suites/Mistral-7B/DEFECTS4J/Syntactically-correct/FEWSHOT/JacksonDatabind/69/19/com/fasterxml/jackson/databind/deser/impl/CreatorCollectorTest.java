package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CreatorCollectorTest {
    private static class TestBean {
        private int i;
        private boolean b;
        private String str;
        private List<String> list;
        private Map<String, Integer> map;
        private Map<String, Map<String, Integer>> nestedMap;
    }

    @Test
    public void testDefaultCreator() {
        BeanDescription beanDesc = Introspector.getBeanInfo(TestBean.class).getBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.StandardConfig());
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        Object result = inst.createValue(new DeserializationContext(), null);
        assertEquals(TestBean.class, result.getClass());
        assertNotNull(result);
    }

    @Test
    public void testStringCreator() {
        BeanDescription beanDesc = Introspector.getBeanInfo(TestBean.class).getBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.StandardConfig());

        Method m = TestBean.class.getMethod("TestBean", String.class);
        AnnotatedWithParams creator = m.getAnnotations()[0];

        collector.addStringCreator(creator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        Object result = inst.createValue(new DeserializationContext(), "foo");
        assertEquals(TestBean.class, result.getClass());
        assertNotNull(result);
        TestBean tb = (TestBean) result;
        assertEquals("foo", tb.str);
    }

    @Test
    public void testIntCreator() {
        BeanDescription beanDesc = Introspector.getBeanInfo(TestBean.class).getBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.StandardConfig());

        Method m = TestBean.class.getMethod("TestBean", int.class);
        AnnotatedWithParams creator = m.getAnnotations()[0];

        collector.addIntCreator(creator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        Object result = inst.createValue(new DeserializationContext(), 1);
        assertEquals(TestBean.class, result.getClass());
        assertNotNull(result);
        TestBean tb = (TestBean) result;
        assertEquals(1, tb.i);
    }

    @Test
    public void testLongCreator() {
        BeanDescription beanDesc = Introspector.getBeanInfo(TestBean.class).getBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.StandardConfig());

        Method m = TestBean.class.getMethod("TestBean", long.class);
        AnnotatedWithParams creator = m.getAnnotations()[0];

        collector.addLongCreator(creator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        Object result = inst.createValue(new DeserializationContext(), 1L);
        assertEquals(TestBean.class, result.getClass());
        assertNotNull(result);
        TestBean tb = (TestBean) result;
        assertEquals(1, tb.i);
    }

    @Test
    public void testDoubleCreator() {
        BeanDescription beanDesc = Introspector.getBeanInfo(TestBean.class).getBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.StandardConfig());

        Method m = TestBean.class.getMethod("TestBean", double.class);
        AnnotatedWithParams creator = m.getAnnotations()[0];

        collector.addDoubleCreator(creator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        Object result = inst.createValue(new DeserializationContext(), 1.0);
        assertEquals(TestBean.class, result.getClass());
        assertNotNull(result);
        TestBean tb = (TestBean) result;
        assertEquals(1, tb.i);
    }

    @Test
    public void testBooleanCreator() {
        BeanDescription beanDesc = Introspector.getBeanInfo(TestBean.class).getBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.StandardConfig());

        Method m = TestBean.class.getMethod("TestBean", boolean.class);
        AnnotatedWithParams creator = m.getAnnotations()[0];

        collector.addBooleanCreator(creator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        Object result = inst.createValue(new DeserializationContext(), Boolean.TRUE);
        assertEquals(TestBean.class, result.getClass());
        assertNotNull(result);
        TestBean tb = (TestBean) result;
        assertEquals(true, tb.b);
    }

    @Test
    public void testDelegateCreator() {
        BeanDescription beanDesc = Introspector.getBeanInfo(TestBean.class).getBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.StandardConfig());

        Method m = TestBean.class.getMethod("TestBean", TestBean.class);
        AnnotatedWithParams creator = m.getAnnotations()[0];

        SettableBeanProperty[] args = new SettableBeanProperty[1];
        args[0] = new SettableBeanProperty("test", TestBean.class);

        collector.addDelegatingCreator(creator, true, args);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        TestBean delegate = new TestBean();
        Object result = inst.createValue(new DeserializationContext(), delegate);
        assertEquals(TestBean.class, result.getClass());
        assertNotNull(result);
        TestBean tb = (TestBean) result;
        assertEquals(delegate, tb);
    }

    @Test
    public void testArrayDelegateCreator() {
        BeanDescription beanDesc = Introspector.getBeanInfo(TestBean.class).getBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.StandardConfig());

        Method m = TestBean.class.getMethod("TestBean", String[].class);
        AnnotatedWithParams creator = m.getAnnotations()[0];

        SettableBeanProperty[] args = new SettableBeanProperty[1];
        args[0] = new SettableBeanProperty("test", String[].class);

        collector.addDelegatingCreator(creator, true, args);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        String[] arr = new String[]{"foo"};
        Object result = inst.createValue(new DeserializationContext(), arr);
        assertEquals(TestBean.class, result.getClass());
        assertNotNull(result);
        TestBean tb = (TestBean) result;
        assertEquals(Arrays.asList(arr), tb.list);
    }

    @Test
    public void testPropertyBasedCreator() {
        BeanDescription beanDesc = Introspector.getBeanInfo(TestBean.class).getBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.StandardConfig());

        Method m = TestBean.class.getMethod("TestBean", CreatorProperty[].class);
        AnnotatedWithParams creator = m.getAnnotations()[0];

        List<CreatorProperty> properties = Arrays.asList(
                new CreatorProperty("i", int.class),
                new CreatorProperty("str", String.class),
                new CreatorProperty("list", new TypeReference<List<String>>(){}.getType()),
                new CreatorProperty("map", new TypeReference<Map<String, Integer>>(){}.getType())
        );

        collector.addPropertyCreator(creator, true, properties.toArray(new CreatorProperty[0]));
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        Object[] args = new Object[]{1, "foo", Arrays.asList("bar", "baz"), new HashMap<String, Integer>(){{
            put("key", 1);
        }}};
        Object result = inst.createValue(new DeserializationContext(), args);
        assertEquals(TestBean.class, result.getClass());
        assertNotNull(result);
        TestBean tb = (TestBean) result;
        assertEquals(1, tb.i);
        assertEquals("foo", tb.str);
        assertEquals(Arrays.asList("bar", "baz"), tb.list);
        assertEquals(1, tb.map.size());
        assertEquals(1, tb.map.get("key"));
    }

    @Test
    public void testNestedMapCreator() {
        BeanDescription beanDesc = Introspector.getBeanInfo(TestBean.class).getBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.StandardConfig());

        Method m = TestBean.class.getMethod("TestBean", CreatorProperty[].class);
        AnnotatedWithParams creator = m.getAnnotations()[0];

        List<CreatorProperty> properties = Arrays.asList(
                new CreatorProperty("str", String.class),
                new CreatorProperty("map", new TypeReference<Map<String, CreatorProperty>>(){}.getType())
        );

        Map<String, CreatorProperty> nestedProperties = new HashMap<>();
        nestedProperties.put("key1", new CreatorProperty("i1", int.class));
        nestedProperties.put("key2", new CreatorProperty("i2", int.class));

        collector.addPropertyCreator(creator, true, properties.toArray(new CreatorProperty[0]));
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        Object[] args = new Object[]{"foo", nestedProperties};
        Object result = inst.createValue(new DeserializationContext(), args);
        assertEquals(TestBean.class, result.getClass());
        assertNotNull(result);
        TestBean tb = (TestBean) result;
        assertEquals("foo", tb.str);
        Map<String, Map<String, Integer>> nestedMap = tb.nestedMap;
        assertEquals(2, nestedMap.size());
        Map<String, Integer> map1 = nestedMap.get("key1");
        assertEquals(1, map1.size());
        assertEquals(1, map1.get("i1"));
        Map<String, Integer> map2 = nestedMap.get("key2");
        assertEquals(1, map2.size());
        assertEquals(1, map2.get("i2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConflictingCreators() {
        BeanDescription beanDesc = Introspector.getBeanInfo(TestBean.class).getBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.StandardConfig());

        Method m1 = TestBean.class.getMethod("TestBean", int.class);
        Method m2 = TestBean.class.getMethod("TestBean", long.class);
        AnnotatedWithParams creator1 = m1.getAnnotations()[0];
        AnnotatedWithParams creator2 = m2.getAnnotations()[0];

        collector.addIntCreator(creator1, true);
        collector.addLongCreator(creator2, true);
    }
}
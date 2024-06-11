package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatorCollectorTest
{
    private ObjectMapper mapper;
    private BeanDescription beanDesc;

    @Before
    public void setup() throws Exception
    {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        beanDesc = mapper.findBeanDescription(MyTestClass.class);
    }

    @Test
    public void testDefaultCreator() throws IOException
    {
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        CreatorCollector.ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
        MyTestClass obj = instantiator.createValueInstance(mapper);
        assertEquals(0, obj.list.size());
        assertEquals(new HashMap<String, Object>(), obj.map);
    }

    @Test
    public void testStringCreator() throws IOException
    {
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams stringCreator = mapper.findConstructor(MyTestClass.class, String.class);
        collector.addStringCreator(stringCreator, true);
        CreatorCollector.ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
        MyTestClass obj = instantiator.createValueInstance(mapper, "test");
        assertEquals("test", obj.stringField);
        assertEquals(0, obj.list.size());
        assertEquals(new HashMap<String, Object>(), obj.map);
    }

    @Test
    public void testIntCreator() throws IOException
    {
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams intCreator = mapper.findConstructor(MyTestClass.class, int.class);
        collector.addIntCreator(intCreator, true);
        CreatorCollector.ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
        MyTestClass obj = instantiator.createValueInstance(mapper, 42);
        assertEquals(42, obj.intField);
        assertEquals(0, obj.list.size());
        assertEquals(new HashMap<String, Object>(), obj.map);
    }

    @Test
    public void testLongCreator() throws IOException
    {
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams longCreator = mapper.findConstructor(MyTestClass.class, long.class);
        collector.addLongCreator(longCreator, true);
        CreatorCollector.ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
        MyTestClass obj = instantiator.createValueInstance(mapper, 42L);
        assertEquals(42L, obj.longField);
        assertEquals(0, obj.list.size());
        assertEquals(new HashMap<String, Object>(), obj.map);
    }

    @Test
    public void testDoubleCreator() throws IOException
    {
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams doubleCreator = mapper.findConstructor(MyTestClass.class, double.class);
        collector.addDoubleCreator(doubleCreator, true);
        CreatorCollector.ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
        MyTestClass obj = instantiator.createValueInstance(mapper, 3.14);
        assertEquals(3.14, obj.doubleField, 0.001);
        assertEquals(0, obj.list.size());
        assertEquals(new HashMap<String, Object>(), obj.map);
    }

    @Test
    public void testBooleanCreator() throws IOException
    {
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams booleanCreator = mapper.findConstructor(MyTestClass.class, boolean.class);
        collector.addBooleanCreator(booleanCreator, true);
        CreatorCollector.ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
        MyTestClass obj = instantiator.createValueInstance(mapper, true);
        assertEquals(true, obj.booleanField);
        assertEquals(0, obj.list.size());
        assertEquals(new HashMap<String, Object>(), obj.map);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidIntCreator() throws IOException
    {
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams intCreator = mapper.findConstructor(MyTestClass.class, String.class);
        collector.addIntCreator(intCreator, true);
        CreatorCollector.ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
        instantiator.createValueInstance(mapper, "invalid");
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidStringCreator() throws IOException
    {
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams stringCreator = mapper.findConstructor(MyTestClass.class, int.class);
        collector.addStringCreator(stringCreator, true);
        CreatorCollector.ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
        instantiator.createValueInstance(mapper, 42);
    }

    @Test
    public void testDelegateCreator() throws IOException
    {
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams delegateCreator = mapper.findConstructor(MyDelegatingClass.class, MyDelegateClass.class);
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = mapper.findProperty(MyDelegatingClass.class, "delegate");
        collector.addDelegatingCreator(delegateCreator, true, injectables);
        CreatorCollector.ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
        MyTestClass obj = (MyTestClass) instantiator.createValueInstance(mapper, new MyDelegateClass());
        assertEquals(new MyDelegateClass(), obj.delegate);
        assertEquals(0, obj.list.size());
        assertEquals(new HashMap<String, Object>(), obj.map);
    }

    @Test
    public void testPropertyBasedCreator() throws IOException
    {
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams propertyCreator = mapper.findConstructor(MyTestClassWithProperties.class,
                MyTestClassWithProperties.class, String.class, String.class);
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = mapper.findProperty(MyTestClassWithProperties.class, "name1");
        properties[1] = mapper.findProperty(MyTestClassWithProperties.class, "name2");
        collector.addPropertyCreator(propertyCreator, true, properties);
        CreatorCollector.ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("name1", "test1");
        args.put("name2", "test2");
        MyTestClass obj = (MyTestClass) instantiator.createValueInstance(mapper, MyTestClassWithProperties.class, args);
        assertEquals("test1", obj.name1);
        assertEquals("test2", obj.name2);
        assertEquals(0, obj.list.size());
        assertEquals(new HashMap<String, Object>(), obj.map);
    }

    public static class MyTestClass
    {
        private int intField;
        private long longField;
        private double doubleField;
        private boolean booleanField;
        private String stringField;
        private List<Object> list = new ArrayList<Object>();
        private Map<String, Object> map = new HashMap<String, Object>();

        public MyTestClass() {}

        public MyTestClass(String s)
        {
            this();
            stringField = s;
        }

        public MyTestClass(int i)
        {
            this();
            intField = i;
        }

        public MyTestClass(long l)
        {
            this();
            longField = l;
        }

        public MyTestClass(double d)
        {
            this();
            doubleField = d;
        }

        public MyTestClass(boolean b)
        {
            this();
            booleanField = b;
        }

        public MyTestClass(MyDelegateClass delegate)
        {
            this();
            delegate = delegate;
        }

        public MyTestClass(String name1, String name2)
        {
            this();
            name1 = name1;
            name2 = name2;
        }

        public static class NestedClass { }
    }

    public static class MyTestClassWithProperties extends MyTestClass
    {
        private String name1;
        private String name2;

        public String getName1() { return name1; }
        public void setName1(String name1) { this.name1 = name1; }

        public String getName2() { return name2; }
        public void setName2(String name2) { this.name2 = name2; }

        public MyTestClassWithProperties() { }

        public MyTestClassWithProperties(String name1, String name2)
        {
            super(name1, name2);
        }

        public static class NestedClass { }
    }

    public static class MyDelegateClass
    {
        private MyTestClass delegate;

        public MyDelegateClass() { }

        public MyTestClass getDelegate() { return delegate; }
        public void setDelegate(MyTestClass delegate) { this.delegate = delegate; }
    }
}
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;

public class CreatorCollectorTest {

    private BeanDescription beanDesc;
    private DeserializationConfig config;
    private ObjectMapper mapper;
    private DeserializationContext ctxt;
    private CreatorCollector collector;

    @Before
    public void setup() throws Exception {
        beanDesc = new BeanDescription(MyClass.class, null, null);
        config = new DeserializationConfig();
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ctxt = mapper.getConfig().contextInstance();
        collector = new CreatorCollector(beanDesc, true);
    }

    @Test
    public void testDefaultConstructor() throws IOException {
        // Set default constructor
        Method defaultConstructor = MyClass.class.getDeclaredConstructor();
        collector.setDefaultCreator(new StandardMethodAnnotatedWithParams(defaultConstructor));

        // Validate instantiation
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        MyClass obj = (MyClass) instantiator.createUsingDefault(ctxt);
        assertEquals(0, obj.getValue());
        assertNotNull(obj.getId());
    }

    @Test
    public void testStringCreator() throws IOException {
        // Set String constructor
        MyClass obj1 = new MyClass("Hello");
        Method stringConstructor = MyClass.class.getDeclaredConstructor(String.class);
        collector.addStringCreator(new StandardMethodAnnotatedWithParams(stringConstructor), true);

        // Validate instantiation
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        MyClass obj2 = (MyClass) instantiator.createValueInstance(ctxt, "World");
        assertEquals("World", obj2.getValue());
        assertNotNull(obj2.getId());
    }

    @Test
    public void testIntCreator() throws IOException {
        // Set int constructor
        MyClass obj1 = new MyClass(42);
        Method intConstructor = MyClass.class.getDeclaredConstructor(int.class);
        collector.addIntCreator(new StandardMethodAnnotatedWithParams(intConstructor), true);

        // Validate instantiation
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        MyClass obj2 = (MyClass) instantiator.createValueInstance(ctxt, 7);
        assertEquals(7, obj2.getValue());
        assertNotNull(obj2.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateConstructors() throws IOException {
        // Set string constructor
        Method stringConstructor = MyClass.class.getDeclaredConstructor(String.class);
        collector.addStringCreator(new StandardMethodAnnotatedWithParams(stringConstructor), true);

        // Set int constructor
        Method intConstructor = MyClass.class.getDeclaredConstructor(int.class);
        collector.addIntCreator(new StandardMethodAnnotatedWithParams(intConstructor), true);

        // Validate exception
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
    }

    @Test
    public void testDelegateCreator() throws IOException {
        // Set delegate constructor
        MyDelegateClass delegate = new MyDelegateClass();
        Method delegateConstructor = MyClass.class.getDeclaredConstructor(MyDelegateClass.class);
        collector.addDelegatingCreator(new StandardMethodAnnotatedWithParams(delegateConstructor), true, new CreatorProperty[0]);

        // Validate instantiation
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        MyClass obj = (MyClass) instantiator.createValueInstance(ctxt, delegate);
        assertEquals(delegate.getValue(), obj.getValue());
        assertNotNull(obj.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateDelegateCreator() throws IOException {
        // Set delegate constructor
        MyDelegateClass delegate = new MyDelegateClass();
        Method delegateConstructor = MyClass.class.getDeclaredConstructor(MyDelegateClass.class);
        collector.addDelegatingCreator(new StandardMethodAnnotatedWithParams(delegateConstructor), true, new CreatorProperty[0]);

        // Set delegate constructor again
        collector.addDelegatingCreator(new StandardMethodAnnotatedWithParams(delegateConstructor), true, new CreatorProperty[0]);

        // Validate exception
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
    }
}

class MyClass {
    private int value;
    private String id;

    public MyClass() {
        this.value = 0;
    }

    public MyClass(String str) {
        this.value = 0;
        this.id = str;
    }

    public MyClass(int i) {
        this.value = i;
    }

    public MyClass(MyDelegateClass delegate) {
        this.value = delegate.getValue();
    }

    @JsonCreator
    public static MyClass create(@JsonProperty("value") int value) {
        return new MyClass(value);
    }

    @JsonCreator
    public static MyClass create(@JsonProperty("id") String id) {
        return new MyClass(id);
    }
}

class MyDelegateClass {
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

class StandardMethodAnnotatedWithParams extends AnnotatedWithParams {
    public StandardMethodAnnotatedWithParams(Method method) {
        super(method, new Annotation[0], new Parameter[] {});
    }
}

class CreatorProperty extends AnnotatedParameter {
    public CreatorProperty(Method method, int index) {
        super(method, index);
    }
}
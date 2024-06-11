package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.util.*;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testDefaultCreator() throws IOException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(HashMap.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, false);

        AnnotatedWithParams defaultCreator = new AnnotatedWithParams(HashMap.class.getDeclaredConstructor());
        collector.setDefaultCreator(defaultCreator);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
        JavaType javaType = mapper.getTypeFactory().constructType(HashMap.class);
        assertEquals(HashMap.class, instantiator.getValueType(javaType));
        assertTrue(instantiator.canCreateUsingDefault());
        assertNotNull(instantiator.createUsingDefault(mapper.getDeserializationContext()));
    }

    @Test
    public void testStringCreator() throws IOException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(StringBuilder.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, false);

        AnnotatedWithParams stringCreator = new AnnotatedWithParams(StringBuilder.class.getConstructor(String.class));

        collector.addStringCreator(stringCreator, true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());

        JavaType javaType = mapper.getTypeFactory().constructType(StringBuilder.class);
        assertEquals(StringBuilder.class, instantiator.getValueType(javaType));
        assertTrue(instantiator.canCreateUsingString());
        String initialValue = "test";
        ObjectNode node = mapper.createObjectNode();
        node.put("className", StringBuilder.class.getName());
        node.put("value", initialValue);
        assertEquals(new StringBuilder(initialValue), instantiator.createUsingString(node, mapper.getDeserializationContext()));
    }

    @Test
    public void testIntCreator() throws IOException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Integer.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, false);

        AnnotatedWithParams intCreator = new AnnotatedWithParams(Integer.class.getDeclaredConstructor(int.class));

        collector.addIntCreator(intCreator, true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());

        JavaType javaType = mapper.getTypeFactory().constructType(Integer.class);
        assertEquals(Integer.class, instantiator.getValueType(javaType));
        assertTrue(instantiator.canCreateUsingInt());
        ObjectNode node = mapper.createObjectNode();
        node.put("className", Integer.class.getName());
        node.put("value", 42);
        assertEquals(Integer.valueOf(42), instantiator.createUsingInt(node, mapper.getDeserializationContext()));
    }

    @Test
    public void testLongCreator() throws IOException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Long.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, false);

        AnnotatedWithParams longCreator = new AnnotatedWithParams(Long.class.getDeclaredConstructor(long.class));

        collector.addLongCreator(longCreator, true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());

        JavaType javaType = mapper.getTypeFactory().constructType(Long.class);
        assertEquals(Long.class, instantiator.getValueType(javaType));
        assertTrue(instantiator.canCreateUsingLong());
        ObjectNode node = mapper.createObjectNode();
        node.put("className", Long.class.getName());
        node.put("value", 42L);
        assertEquals(Long.valueOf(42L), instantiator.createUsingLong(node, mapper.getDeserializationContext()));
    }

    @Test
    public void testDoubleCreator() throws IOException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Double.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, false);

        AnnotatedWithParams doubleCreator = new AnnotatedWithParams(Double.class.getDeclaredConstructor(double.class));

        collector.addDoubleCreator(doubleCreator, true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());

        JavaType javaType = mapper.getTypeFactory().constructType(Double.class);
        assertEquals(Double.class, instantiator.getValueType(javaType));
        assertTrue(instantiator.canCreateUsingDouble());
        ObjectNode node = mapper.createObjectNode();
        node.put("className", Double.class.getName());
        node.put("value", 42.0);
        assertEquals(Double.valueOf(42.0), instantiator.createUsingDouble(node, mapper.getDeserializationContext()));
    }

    @Test
    public void testBooleanCreator() throws IOException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Boolean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, false);

        AnnotatedWithParams booleanCreator = new AnnotatedWithParams(Boolean.class.getDeclaredConstructor(boolean.class));

        collector.addBooleanCreator(booleanCreator, true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());

        JavaType javaType = mapper.getTypeFactory().constructType(Boolean.class);
        assertEquals(Boolean.class, instantiator.getValueType(javaType));
        assertTrue(instantiator.canCreateUsingBoolean());
        ObjectNode node = mapper.createObjectNode();
        node.put("className", Boolean.class.getName());
        node.put("value", true);
        assertEquals(Boolean.TRUE, instantiator.createUsingBoolean(node, mapper.getDeserializationContext()));
    }

    @Test
    public void testDelegatingCreator() throws IOException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(StringBuilder.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, false);

        AnnotatedWithParams stringCreator = new AnnotatedWithParams(StringBuilder.class.getConstructor(String.class));
        CreatorProperty stringProperty = new CreatorProperty("string", String.class);

        collector.addStringCreator(stringCreator, true);
        collector.addDelegatingCreator(stringCreator, true, new CreatorProperty[] {stringProperty});
        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());

        JavaType javaType = mapper.getTypeFactory().constructType(StringBuilder.class);
        assertEquals(StringBuilder.class, instantiator.getValueType(javaType));
        assertTrue(instantiator.canCreateUsingString());
        String initialValue = "test";
        ObjectNode node = mapper.createObjectNode();
        node.put("className", StringBuilder.class.getName());
        node.put("value", initialValue);
        assertEquals(new StringBuilder(initialValue), instantiator.createUsingString(node, mapper.getDeserializationContext()));
    }

    @Test
    public void testPropertyBasedCreator() throws IOException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Person.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, false);

        AnnotatedWithParams creator = new AnnotatedWithParams(Person.class.getDeclaredConstructor(String.class, int.class));
        CreatorProperty stringProperty = new CreatorProperty("name", String.class);
        CreatorProperty intProperty = new CreatorProperty("age", int.class);

        collector.addPropertyCreator(creator, true, new CreatorProperty[] {stringProperty, intProperty});
        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());

        TypeReference<Person> typeReference = new TypeReference<Person>() {};
        assertEquals(Person.class, instantiator.getValueType(typeReference));

        String name = "John Doe";
        int age = 30;
        ObjectNode node = mapper.createObjectNode();
        node.put("className", Person.class.getName());
        node.put("name", name);
        node.put("age", age);
        assertEquals(new Person(name, age), instantiator.createUsingTextNode(node, mapper.getDeserializationContext()));
    }

    static class Person {
        private final String name;
        private final int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }
}
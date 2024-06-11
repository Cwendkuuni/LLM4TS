package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ValueInstantiatorTest {

    /**
     * Test for getValueClass() method
     */
    @org.junit.Test
    public void testgetValueClass() {
        ValueInstantiator instantiator = new StdValueInstantiator(String.class);
        assertEquals(String.class, instantiator.getValueClass());
    }

    /**
     * Test for getValueTypeDesc() method
     */
    @org.junit.Test
    public void testgetValueTypeDesc() {
        ValueInstantiator instantiator = new StdValueInstantiator(String.class);
        assertEquals("java.lang.String", instantiator.getValueTypeDesc());
    }

    /**
     * Test for canInstantiate() method
     */
    @org.junit.Test
    public void testCanInstantiate() {
        ValueInstantiator instantiator = new StdValueInstantiator(String.class);
        assertTrue(instantiator.canInstantiate());
    }

    /**
     * Test for canCreateFromString() method
     */
    @org.junit.Test
    public void testCanCreateFromString() {
        ValueInstantiator instantiator = new StdValueInstantiator(String.class);
        assertTrue(instantiator.canCreateFromString());
    }

    /**
     * Test for createFromString() method
     * @throws JsonProcessingException
     */
    @org.junit.Test
    public void testCreateFromString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = new StdValueInstantiator(String.class);
        String jsonString = "\"hello world\"";
        Object instance = instantiator.createFromString(mapper.getDeserializationContext(), jsonString);
        assertEquals("hello world", instance);
    }

    /**
     * Test for canCreateFromInt() method
     */
    @org.junit.Test
    public void testCanCreateFromInt() {
        ValueInstantiator instantiator = new StdValueInstantiator(Integer.class);
        assertFalse(instantiator.canCreateFromInt());
    }

    /**
     * Test for createFromInt() method with an exception
     * @throws IOException
     */
    @org.junit.Test(expected = IOException.class)
    public void testCreateFromIntWithException() throws IOException {
        ValueInstantiator instantiator = new StdValueInstantiator(Integer.class);
        instantiator.createFromInt(new DeserializationContext(), 123);
    }

    /**
     * Test for canCreateFromLong() method
     */
    @org.junit.Test
    public void testCanCreateFromLong() {
        ValueInstantiator instantiator = new StdValueInstantiator(Long.class);
        assertFalse(instantiator.canCreateFromLong());
    }

    /**
     * Test for createFromLong() method with an exception
     * @throws IOException
     */
    @org.junit.Test(expected = IOException.class)
    public void testCreateFromLongWithException() throws IOException {
        ValueInstantiator instantiator = new StdValueInstantiator(Long.class);
        instantiator.createFromLong(new DeserializationContext(), 123L);
    }

    /**
     * Test for canCreateFromDouble() method
     */
    @org.junit.Test
    public void testCanCreateFromDouble() {
        ValueInstantiator instantiator = new StdValueInstantiator(Double.class);
        assertFalse(instantiator.canCreateFromDouble());
    }

    /**
     * Test for createFromDouble() method with an exception
     * @throws IOException
     */
    @org.junit.Test(expected = IOException.class)
    public void testCreateFromDoubleWithException() throws IOException {
        ValueInstantiator instantiator = new StdValueInstantiator(Double.class);
        instantiator.createFromDouble(new DeserializationContext(), 123.45);
    }

    /**
     * Test for canCreateFromBoolean() method
     */
    @org.junit.Test
    public void testCanCreateFromBoolean() {
        ValueInstantiator instantiator = new StdValueInstantiator(Boolean.class);
        assertTrue(instantiator.canCreateFromBoolean());
    }

    /**
     * Test for createFromBoolean() method
     * @throws JsonMappingException
     */
    @org.junit.Test
    public void testCreateFromBoolean() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = new StdValueInstantiator(Boolean.class);
        String jsonBoolean = "true";
        Object instance = instantiator.createFromString(mapper.getDeserializationContext(), jsonBoolean);
        assertTrue((Boolean) instance);

        String jsonFalse = "false";
        instance = instantiator.createFromString(mapper.getDeserializationContext(), jsonFalse);
        assertFalse((Boolean) instance);
    }

    /**
     * Test for canCreateUsingDefault() method
     */
    @org.junit.Test
    public void testCanCreateUsingDefault() {
        ValueInstantiator instantiator = new StdValueInstantiator(String.class);
        assertTrue(instantiator.canCreateUsingDefault());
    }

    /**
     * Test for createUsingDefault() method
     * @throws JsonProcessingException
     */
    @org.junit.Test
    public void testCreateUsingDefault() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = new StdValueInstantiator(String.class);
        Object instance = instantiator.createUsingDefault(mapper.getDeserializationContext());
        assertNull(instance);
    }

    /**
     * Test for canCreateUsingDelegate() method
     */
    @org.junit.Test
    public void testCanCreateUsingDelegate() {
        ValueInstantiator instantiator = new StdValueInstantiator(String.class);
        assertFalse(instantiator.canCreateUsingDelegate());
    }

    /**
     * Test for createUsingDelegate() method with an exception
     * @throws IOException
     */
    @org.junit.Test(expected = IOException.class)
    public void testCreateUsingDelegateWithException() throws IOException {
        ValueInstantiator instantiator = new StdValueInstantiator(String.class);
        instantiator.createUsingDelegate(new DeserializationContext(), new Object());
    }

    /**
     * Test for canCreateUsingArrayDelegate() method
     */
    @org.junit.Test
    public void testCanCreateUsingArrayDelegate() {
        ValueInstantiator instantiator = new StdValueInstantiator(String[].class);
        assertFalse(instantiator.canCreateUsingArrayDelegate());
    }

    /**
     * Test for createUsingArrayDelegate() method with an exception
     * @throws IOException
     */
    @org.junit.Test(expected = IOException.class)
    public void testCreateUsingArrayDelegateWithException() throws IOException {
        ValueInstantiator instantiator = new StdValueInstantiator(String[].class);
        instantiator.createUsingArrayDelegate(new DeserializationContext(), new Object[0]);
    }

    /**
     * Test for createFromObjectWith() method
     * @throws JsonMappingException
     */
    @org.junit.Test
    public void testCreateFromObjectWith() throws JsonMappingException {
        String json = "{\"name\":\"John\",\"age\":30}";
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = new StdValueInstantiator(Person.class);
        Object instance = instantiator.createFromObjectWith(mapper.getDeserializationContext(), null);
        assertNull(instance);

        Person person = mapper.readValue(JsonFactory.instance.setText(json), Person.class);
        instance = instantiator.createFromObjectWith(mapper.getDeserializationContext(), person.getFields());
        assertSame(person, instance);
    }

    /**
     * Test for getFromObjectArguments() method
     * @throws JsonProcessingException
     */
    @org.junit.Test
    public void testGetFromObjectArguments() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = new StdValueInstantiator(Person.class);
        Object person = mapper.readValue(JsonFactory.instance.setText("{\"name\":\"John\",\"age\":30}"), Person.class);
        SettableBeanProperty[] properties = instantiator.getFromObjectArguments(mapper.getConfig());
        assertEquals(2, properties.length);
        assertEquals("name", properties[0].getName());
        assertEquals("age", properties[1].getName());
    }

    /**
     * Test for getDelegateType() method
     */
    @org.junit.Test
    public void testGetDelegateType() {
        ValueInstantiator instantiator = new StdValueInstantiator(Person.class);
        assertNull(instantiator.getDelegateType(null));
    }

    /**
     * Test for getArrayDelegateType() method
     */
    @org.junit.Test
    public void testGetArrayDelegateType() {
        ValueInstantiator instantiator = new StdValueInstantiator(Person[].class);
        assertNull(instantiator.getArrayDelegateType(null));
    }

    static class Person {
        @JsonCreator
        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @JsonValue
        public String toString() {
            return String.format("{\"name\":\"%s\",\"age\":%d}", name, age);
        }

        private String name;
        private int age;

        public SettableBeanProperty nameProperty() {
            return new BeanProperty("name", String.class, this, null);
        }

        public SettableBeanProperty ageProperty() {
            return new BeanProperty("age", Integer.TYPE, this, null);
        }

        public Object[] getFields() {
            return new Object[] {nameProperty, ageProperty};
        }
    }

    static class BeanProperty extends PropertyName {
        private final Class<?> _class;

        public BeanProperty(String name, Class<?> clazz) {
            super(name);
            _class = clazz;
        }

        @Override
        public Class<?> getType() {
            return _class;
        }
    }
}
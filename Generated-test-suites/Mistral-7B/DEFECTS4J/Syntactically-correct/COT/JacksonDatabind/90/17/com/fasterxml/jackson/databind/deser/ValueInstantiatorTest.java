package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;

public class ValueInstantiatorTest {

    @Test
    public void testGetValueClass() throws Exception {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        assertEquals("java.lang.String", instantiator.getValueClass().getName());
    }

    @Test
    public void testGetValueTypeDesc() throws Exception {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        assertEquals("java.lang.String", instantiator.getValueTypeDesc());
    }

    @Test
    public void testCanInstantiate() throws Exception {
        ValueInstantiator instantiator = new ValueInstantiator.Base(ValueInstantiatorTest.class);
        assertFalse(instantiator.canInstantiate());
    }

    @Test
    public void testCanCreateUsingDefault() throws Exception {
        ValueInstantiator instantiator = new ValueInstantiator.Base(ValueInstantiatorTest.class);
        assertFalse(instantiator.canCreateUsingDefault());
    }

    @Test(expected = AssertionError.class)
    public void testCreateUsingDefault() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(ValueInstantiatorTest.class);
        instantiator.createUsingDefault(null);
    }

    @Test
    public void testCanCreateFromObjectWith() throws Exception {
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        node.putPOJO("property1", new PropertyValueInstantiatorTest());
        ValueInstantiator instantiator = new ValueInstantiator.Base(PropertyValueInstantiatorTest.class);
        assertTrue(instantiator.canCreateFromObjectWith());
    }

    @Test(expected = AssertionError.class)
    public void testCreateFromObjectWithNull() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(ValueInstantiatorTest.class);
        instantiator.createFromObjectWith(null, new Object[0]);
    }

    @Test
    public void testCreateFromObjectWithEmptyArgs() throws IOException {
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        ValueInstantiator instantiator = new ValueInstantiator.Base(EmptyObject.class);
        EmptyObject emptyObject = (EmptyObject) instantiator.createFromObjectWith(null, new Object[0]);
        assertNull(emptyObject);
    }

    @Test
    public void testCreateFromObjectWithValidArgs() throws IOException {
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        node.put("property1", "value1");
        ValueInstantiator instantiator = new ValueInstantiator.Base(PropertyValueInstantiatorTest.class);
        PropertyValueInstantiatorTest propertyValueInstantiatorTest = (PropertyValueInstantiatorTest) instantiator.createFromObjectWith(null, new Object[]{"value1"});
        assertEquals("value1", propertyValueInstantiatorTest.propertyValue);
    }

    @Test
    public void testCanCreateUsingDelegate() throws Exception {
        ValueInstantiator instantiator = new ValueInstantiator.Base(Object.class);
        assertFalse(instantiator.canCreateUsingDelegate());
    }

    @Test(expected = AssertionError.class)
    public void testCreateUsingDelegateNull() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(Object.class);
        instantiator.createUsingDelegate(null, new Object[0]);
    }

    @Test
    public void testCreateFromString() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        String value = instantiator.createFromString(null, "test");
        assertEquals("test", value);
    }

    @Test(expected = AssertionError.class)
    public void testCreateFromInt() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(Integer.class);
        instantiator.createFromInt(null, 5);
    }

    @Test
    public void testCreateFromLong() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(Long.class);
        Long value = instantiator.createFromLong(null, 5L);
        assertEquals(5L, value);
    }

    @Test
    public void testCreateFromDouble() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(Double.class);
        Double value = instantiator.createFromDouble(null, 5.0);
        assertEquals(5.0, value, 0.0);
    }

    @Test
    public void testCreateFromBoolean() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(Boolean.class);
        Boolean value = instantiator.createFromBoolean(null, true);
        assertTrue(value);
    }

    public static class PropertyValueInstantiatorTest {
        String propertyValue;
    }

    public static class EmptyObject {}

    @JsonDeserialize(using = EmptyObjectDeserializer.class)
    static class EmptyObjectDeserializer extends StdDeserializer<EmptyObject> {
        public EmptyObjectDeserializer() {
            this(EmptyObject.class);
        }

        public EmptyObjectDeserializer(Class<EmptyObject> c) {
            this(c, new SimpleBeanPropertyFilter.FilterProvider() {
                @Override
                public SimpleBeanPropertyFilter getFilterFor(DeserializationConfig config, BeanDescription beanDesc) {
                    return SimpleBeanPropertyFilter.filterOutAllExcept("property1");
                }
            });
        }

        @Override
        public EmptyObject deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new EmptyObject();
        }

        @Override
        public void acceptJsonFilter(DeserializationConfig config, BeanDescription beanDesc, JsonFilterProvider filterProvider) throws IOException {
            super.acceptJsonFilter(config, beanDesc, filterProvider);
        }
    }
}
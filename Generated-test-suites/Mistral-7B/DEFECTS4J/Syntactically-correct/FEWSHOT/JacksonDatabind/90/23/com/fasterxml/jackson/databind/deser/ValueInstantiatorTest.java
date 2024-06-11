package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ValueInstantiatorTest {

    @Test
    public void testCanInstantiate() throws IOException {
        ValueInstantiator valueInstantiator = new BaseValueInstantiator(String.class);
        assertTrue(valueInstantiator.canInstantiate());

        valueInstantiator = new BaseValueInstantiator(int[].class);
        assertFalse(valueInstantiator.canInstantiate());
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        ValueInstantiator valueInstantiator = new BaseValueInstantiator(MyClassWithConstructor.class);

        ObjectNode json = ObjectMapper.getDefaultInstance().createObjectNode();
        json.put("property1", "value1");
        json.put("property2", 2L);

        Object createdObject = valueInstantiator.createFromObjectWith(new DeserializationContext(new ObjectMapper()), json.fields());

        assertEquals(MyClassWithConstructor.class, createdObject.getClass());
        assertEquals("value1", ((MyClassWithConstructor) createdObject).getProperty1());
        assertEquals(2L, ((MyClassWithConstructor) createdObject).getProperty2());
    }

    @Test
    public void testCreateFromString() throws IOException {
        ValueInstantiator valueInstantiator = new BaseValueInstantiator(String.class);

        assertEquals("test", valueInstantiator.createFromString(new DeserializationContext(new ObjectMapper()), "test"));

        valueInstantiator = new BaseValueInstantiator(MyClassFromString.class);

        ObjectNode json = ObjectMapper.getDefaultInstance().createObjectNode();
        json.put("property1", "test");

        Object createdObject = valueInstantiator.createFromObjectWith(new DeserializationContext(new ObjectMapper()), json.fields());

        assertEquals(MyClassFromString.class, createdObject.getClass());
        assertEquals("test", ((MyClassFromString) createdObject).getProperty1());
    }

    // Add more test methods here for other methods in the ValueInstantiator class

    private static class BaseValueInstantiator extends ValueInstantiator.Base {
        public BaseValueInstantiator(Class<?> valueType) {
            super(valueType);
        }

        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
            return super.createFromObjectWith(ctxt, args);
        }

        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, SettableBeanProperty[] props, PropertyValueBuffer buffer)
                throws IOException {
            return super.createFromObjectWith(ctxt, props, buffer);
        }

        @Override
        public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
            return super.createUsingDelegate(ctxt, delegate);
        }

        @Override
        public Object createUsingArrayDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
            return super.createUsingArrayDelegate(ctxt, delegate);
        }

        @Override
        public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
            return super.createFromInt(ctxt, value);
        }

        @Override
        public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
            return super.createFromLong(ctxt, value);
        }

        @Override
        public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
            return super.createFromDouble(ctxt, value);
        }

        @Override
        public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
            return super.createFromBoolean(ctxt, value);
        }

        @Override
        public AnnotatedWithParams getDefaultCreator() {
            return super.getDefaultCreator();
        }

        @Override
        public AnnotatedWithParams getDelegateCreator() {
            return super.getDelegateCreator();
        }

        @Override
        public AnnotatedWithParams getArrayDelegateCreator() {
            return super.getArrayDelegateCreator();
        }

        @Override
        public AnnotatedWithParams getWithArgsCreator() {
            return super.getWithArgsCreator();
        }

        @Override
        public AnnotatedParameter getIncompleteParameter() {
            return super.getIncompleteParameter();
        }
    }

    private static class MyClassWithConstructor {
        private String property1;
        private long property2;

        public MyClassWithConstructor(String property1, long property2) {
            this.property1 = property1;
            this.property2 = property2;
        }

        public String getProperty1() {
            return property1;
        }

        public long getProperty2() {
            return property2;
        }
    }

    private static class MyClassFromString {
        private String property1;

        public MyClassFromString(String property1) {
            this.property1 = property1;
        }

        public String getProperty1() {
            return property1;
        }
    }
}
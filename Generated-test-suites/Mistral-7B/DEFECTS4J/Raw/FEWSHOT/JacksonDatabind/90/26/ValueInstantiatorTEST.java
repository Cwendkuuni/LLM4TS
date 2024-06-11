package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.base.ParsingObserver;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class ValueInstantiatorTest {

    private JavaContext javaContext;
    private DeserializationConfig config;

    @Before
    public void setup() {
        javaContext = new JavaContext();
        config = new DeserializationConfig(javaContext);
    }

    @Test
    public void testBaseClass() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        assertEquals("java.lang.String", instantiator.getValueTypeDesc());
        assertEquals(String.class, instantiator.getValueClass());
    }

    @Test
    public void testGetFromObjectArguments() throws IOException {
        ValueInstantiator instantiator = new MyValueInstantiator();
        SettableBeanProperty[] props = { new SettableBeanProperty("prop1", Integer.class), new SettableBeanProperty("prop2", String.class) };
        PropertyValueBuffer buffer = new PropertyValueBuffer(javaContext, config, props);

        assertEquals(2, instantiator.getFromObjectArguments(config).length);
        assertEquals(props[0], instantiator.getFromObjectArguments(config)[0]);
        assertEquals(props[1], instantiator.getFromObjectArguments(config)[1]);
    }

    @Test
    public void testCreateUsingDefault() throws IOException {
        ValueInstantiator instantiator = new MyValueInstantiator();
        Object result = instantiator.createUsingDefault(new TestContext());
        assertNull(result);
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        Object[] args = { 1, "hello" };
        ValueInstantiator instantiator = new MyValueInstantiator();

        Object result = instantiator.createFromObjectWith(new TestContext(), args);
        assertEquals(args[0], result);
    }

    @Test
    public void testCreateUsingDelegate() throws IOException {
        MyDelegateCreator delegateCreator = new MyDelegateCreator();
        ValueInstantiator instantiator = new MyValueInstantiator();
        Object delegate = new Object();

        Object result = instantiator.createUsingDelegate(new TestContext(), delegate);
        assertSame(delegateCreator.create(delegate), result);
    }

    @Test
    public void testCreateUsingArrayDelegate() throws IOException {
        MyArrayDelegateCreator arrayDelegateCreator = new MyArrayDelegateCreator();
        ValueInstantiator instantiator = new MyValueInstantiator();
        Object delegate = new Object[2];

        Object result = instantiator.createUsingArrayDelegate(new TestContext(), delegate);
        assertSame(arrayDelegateCreator.create(delegate), result);
    }

    @Test
    public void testCreateFromString() throws IOException {
        ValueInstantiator instantiator = new MyValueInstantiator();
        String value = "hello";

        Object result = instantiator.createFromString(new TestContext(), value);
        assertEquals("hello", result);
    }

    @Test
    public void testCreateFromInt() throws IOException {
        ValueInstantiator instantiator = new MyValueInstantiator();
        int value = 1;

        Object result = instantiator.createFromInt(new TestContext(), value);
        assertEquals(Integer.valueOf(value), result);
    }

    @Test
    public void testCreateFromLong() throws IOException {
        ValueInstantiator instantiator = new MyValueInstantiator();
        long value = 1L;

        Object result = instantiator.createFromLong(new TestContext(), value);
        assertEquals(Long.valueOf(value), result);
    }

    @Test
    public void testCreateFromDouble() throws IOException {
        ValueInstantiator instantiator = new MyValueInstantiator();
        double value = 1.0;

        Object result = instantiator.createFromDouble(new TestContext(), value);
        assertEquals(Double.valueOf(value), result);
    }

    @Test
    public void testCreateFromBoolean() throws IOException {
        ValueInstantiator instantiator = new MyValueInstantiator();
        boolean value = true;

        Object result = instantiator.createFromBoolean(new TestContext(), value);
        assertEquals(Boolean.valueOf(value), result);
    }

    @Test(expected = IOException.class)
    public void testCreateUsingDefaultException() throws IOException {
        ValueInstantiator instantiator = new MyValueInstantiator();
        instantiator.createUsingDefault(new TestContext());
    }

    @Test(expected = IOException.class)
    public void testCreateFromObjectWithException() throws IOException {
        ValueInstantiator instantiator = new MyValueInstantiator();
        Object[] args = { null };
        instantiator.createFromObjectWith(new TestContext(), args);
    }

    @Test(expected = IOException.class)
    public void testCreateUsingDelegateException() throws IOException {
        ValueInstantiator instantiator = new MyValueInstantiator();
        Object delegate = null;
        instantiator.createUsingDelegate(new TestContext(), delegate);
    }

    @Test(expected = IOException.class)
    public void testCreateUsingArrayDelegateException() throws IOException {
        ValueInstantiator instantiator = new MyValueInstantiator();
        Object[] delegate = null;
        instantiator.createUsingArrayDelegate(new TestContext(), delegate);
    }

    @Test(expected = IOException.class)
    public void testCreateFromStringException() throws IOException {
        ValueInstantiator instantiator = new MyValueInstantiator();
        instantiator.createFromString(new TestContext(), null);
    }

    @Test(expected = IOException.class)
    public void testCreateFromIntException() throws IOException {
        ValueInstantiator instantiator = new MyValueInstantiator();
        instantiator.createFromInt(new TestContext(), Integer.MIN_VALUE);
    }

    @Test(expected = IOException.class)
    public void testCreateFromLongException() throws IOException {
        ValueInstantiator instantiator = new MyValueInstantiator();
        instantiator.createFromLong(new TestContext(), Long.MIN_VALUE);
    }

    @Test(expected = IOException.class)
    public void testCreateFromDoubleException() throws IOException {
        ValueInstantiator instantiator = new MyValueInstantiator();
        instantiator.createFromDouble(new TestContext(), Double.NEGATIVE_INFINITY);
    }

    @Test(expected = IOException.class)
    public void testCreateFromBooleanException() throws IOException {
        ValueInstantiator instantiator = new MyValueInstantiator();
        instantiator.createFromBoolean(new TestContext(), false);
    }

    private static class MyValueInstantiator extends ValueInstantiator {
        @Override
        public boolean canCreateUsingDefault() {
            return false;
        }

        @Override
        public boolean canCreateUsingDelegate() {
            return false;
        }

        @Override
        public boolean canCreateUsingArrayDelegate() {
            return false;
        }

        @Override
        public boolean canCreateFromObjectWith() {
            return true;
        }

        @Override
        public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
            return new SettableBeanProperty[0];
        }

        @Override
        public Object createFromObjectWith(DeserializationContext context, Object[] args) throws IOException {
            throw new IOException("Should not be called");
        }

        @Override
        public AnnotatedWithParams getDefaultCreator() {
            return null;
        }

        @Override
        public AnnotatedWithParams getDelegateCreator() {
            return new MyDelegateCreator();
        }

        @Override
        public AnnotatedWithParams getArrayDelegateCreator() {
            return new MyArrayDelegateCreator();
        }

        @Override
        public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
            throw new IOException("Should not be called");
        }

        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
            return createFromObjectWith(ctxt, args);
        }

        @Override
        public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
            return createUsingDelegate(ctxt, delegate);
        }

        @Override
        public Object createUsingArrayDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
            return createUsingArrayDelegate(ctxt, delegate);
        }

        @Override
        public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
            return createFromString(ctxt, value);
        }

        @Override
        public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
            return createFromInt(ctxt, value);
        }

        @Override
        public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
            return createFromLong(ctxt, value);
        }

        @Override
        public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
            return createFromDouble(ctxt, value);
        }

        @Override
        public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
            return createFromBoolean(ctxt, value);
        }

        @Override
        public JavaType getDelegateType(DeserializationConfig config) {
            return null;
        }

        @Override
        public JavaType getArrayDelegateType(DeserializationConfig config) {
            return null;
        }

        private static class MyDelegateCreator implements AnnotatedWithParams {
            public Object create(Object delegate) {
                throw new UnsupportedOperationException("Delegate creator not supported");
            }
        }

        private static class MyArrayDelegateCreator implements AnnotatedWithParams {
            public Object create(Object delegate) {
                throw new UnsupportedOperationException("Array delegate creator not supported");
            }
        }

        private static class TestContext extends DeserializationContext {
            public TestContext() {
                super(new ParsingObserver(), Collections.emptyList(), null);
            }
        }

        private Object createFromObjectWith(DeserializationContext context, Object[] args) throws IOException {
            return context.handleMissingInstantiator(getValueClass(), context.getParser(), "Missing instantiation method");
        }

        private Object createUsingDelegate(DeserializationContext context, Object delegate) throws IOException {
            return context.handleMissingInstantiator(getValueClass(), context.getParser(), "Missing delegate creator");
        }

        private Object createUsingArrayDelegate(DeserializationContext context, Object delegate) throws IOException {
            return context.handleMissingInstantiator(getValueClass(), context.getParser(), "Missing array delegate creator");
        }

        private Object createFromString(DeserializationContext context, String value) throws IOException {
            return context.handleMissingInstantiator(getValueClass(), context.getParser(), "Missing String creator");
        }

        private Object createFromInt(DeserializationContext context, int value) throws IOException {
            return context.handleMissingInstantiator(getValueClass(), context.getParser(), "Missing Int creator");
        }

        private Object createFromLong(DeserializationContext context, long value) throws IOException {
            return context.handleMissingInstantiator(getValueClass(), context.getParser(), "Missing Long creator");
        }

        private Object createFromDouble(DeserializationContext context, double value) throws IOException {
            return context.handleMissingInstantiator(getValueClass(), context.getParser(), "Missing Double creator");
        }

        private Object createFromBoolean(DeserializationContext context, boolean value) throws IOException {
            return context.handleMissingInstantiator(getValueClass(), context.getParser(), "Missing Boolean creator");
        }
    }
}

class MyValueInstantiator extends ValueInstantiator {
    // ...
}

class MyDelegateCreator implements AnnotatedWithParams {
    // ...
}

class MyArrayDelegateCreator implements AnnotatedWithParams {
    // ...
}
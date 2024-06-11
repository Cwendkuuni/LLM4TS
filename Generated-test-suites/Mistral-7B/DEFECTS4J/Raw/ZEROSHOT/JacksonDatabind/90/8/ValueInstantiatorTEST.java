package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.ser.std.StdValueInstantiator;

/**
 * Test class for ValueInstantiator.
 */
public class ValueInstantiatorTest {

    private static final Class<Object> OBJECT_TYPE = Object.class;
    private static final String VALUE_TYPE_DESC = OBJECT_TYPE.getName();

    /**
     * Test method for canInstantiate.
     */
    @org.junit.Test
    public void testCanInstantiate() throws Exception {
        ValueInstantiator instantiator = new Base(OBJECT_TYPE);
        assertTrue(instantiator.canInstantiate());
    }

    /**
     * Test method for getValueClass.
     */
    @org.junit.Test
    public void testGetValueClass() {
        ValueInstantiator instantiator = new Base(OBJECT_TYPE);
        assertEquals(OBJECT_TYPE, instantiator.getValueClass());
    }

    /**
     * Test method for getValueTypeDesc.
     */
    @org.junit.Test
    public void testGetValueTypeDesc() {
        ValueInstantiator instantiator = new Base(OBJECT_TYPE);
        assertEquals(VALUE_TYPE_DESC, instantiator.getValueTypeDesc());
    }

    /**
     * Test method for createUsingDefault.
     */
    @org.junit.Test
    public void testCreateUsingDefault() throws Exception {
        ValueInstantiator instantiator = new Base(Object.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object result = instantiator.createUsingDefault(context);

        // since there is no default constructor, this should throw exception
        fail("Expected exception: java.lang.IllegalArgumentException");
    }

    /**
     * Test method for createFromObjectWith.
     */
    @org.junit.Test
    public void testCreateFromObjectWith() throws Exception {
        ValueInstantiator instantiator = new Base(Object.class) {
            @Override
            public Object createFromObjectWith(DeserializationContext ctxt, Object[] args)
                    throws IOException {
                return ctxt.handleMissingInstantiator(Object.class, ctxt.getParser(), "test");
            }
        };

        DeserializationContext context = mock(DeserializationContext.class);
        Object[] arguments = new Object[1];
        arguments[0] = Integer.valueOf(1);

        fail("Expected exception: java.lang.IllegalArgumentException");
    }

    /**
     * Test method for createFromObjectWith with null arguments.
     */
    @org.junit.Test
    public void testCreateFromObjectWithWithNullArguments() throws Exception {
        ValueInstantiator instantiator = new Base(Object.class) {
            @Override
            public Object createFromObjectWith(DeserializationContext ctxt, Object[] args)
                    throws IOException {
                return new Object();
            }
        };

        DeserializationContext context = mock(DeserializationContext.class);
        Object[] arguments = null;

        fail("Expected exception: java.lang.IllegalArgumentException");
    }

    /**
     * Test method for createFromObjectWith with valid arguments.
     */
    @org.junit.Test
    public void testCreateFromObjectWithWithValidArguments() throws Exception {
        ValueInstantiator instantiator = new Base(Object.class) {
            @Override
            public Object createFromObjectWith(DeserializationContext ctxt, Object[] args)
                    throws IOException {
                return new Object();
            }
        };

        DeserializationContext context = mock(DeserializationContext.class);
        Object[] arguments = new Object[1];
        arguments[0] = Integer.valueOf(1);

        Object result = instantiator.createFromObjectWith(context, arguments);
        assertEquals(Object.class, result.getClass());
    }

    /**
     * Test method for createUsingDelegate.
     */
    @org.junit.Test
    public void testCreateUsingDelegate() throws Exception {
        ValueInstantiator instantiator = new Base(Object.class) {
            @Override
            public AnnotatedWithParams getDelegateCreator() {
                return mock(AnnotatedWithParams.class);
            }
        };
        DeserializationContext context = mock(DeserializationContext.class);
        Object delegate = new Object();

        fail("Expected exception: java.lang.IllegalArgumentException");
    }

    /**
     * Test method for createUsingArrayDelegate.
     */
    @org.junit.Test
    public void testCreateUsingArrayDelegate() throws Exception {
        ValueInstantiator instantiator = new Base(Object[].class) {
            @Override
            public AnnotatedWithParams getArrayDelegateCreator() {
                return mock(AnnotatedWithParams.class);
            }
        };
        DeserializationContext context = mock(DeserializationContext.class);
        Object delegate = new Object[1];

        fail("Expected exception: java.lang.IllegalArgumentException");
    }

    /**
     * Test method for createFromString.
     */
    @org.junit.Test
    public void testCreateFromString() throws Exception {
        ValueInstantiator instantiator = new Base(String.class) {
            @Override
            public Object createFromString(DeserializationContext ctxt, String value)
                    throws IOException {
                return value;
            }
        };

        DeserializationContext context = mock(DeserializationContext.class);
        String value = "test";

        Object result = instantiator.createFromString(context, value);
        assertEquals(String.class, result.getClass());
        assertEquals("test", result);
    }

    /**
     * Test method for createFromInt.
     */
    @org.junit.Test
    public void testCreateFromInt() throws Exception {
        ValueInstantiator instantiator = new Base(Integer.class) {
            @Override
            public Object createFromInt(DeserializationContext ctxt, int value)
                    throws IOException {
                return value;
            }
        };

        DeserializationContext context = mock(DeserializationContext.class);
        int value = 1;

        Object result = instantiator.createFromInt(context, value);
        assertEquals(Integer.class, result.getClass());
        assertEquals(Integer.valueOf(1), result);
    }

    /**
     * Test method for createFromLong.
     */
    @org.junit.Test
    public void testCreateFromLong() throws Exception {
        ValueInstantiator instantiator = new Base(Long.class) {
            @Override
            public Object createFromLong(DeserializationContext ctxt, long value)
                    throws IOException {
                return value;
            }
        };

        DeserializationContext context = mock(DeserializationContext.class);
        long value = 1L;

        Object result = instantiator.createFromLong(context, value);
        assertEquals(Long.class, result.getClass());
        assertEquals(Long.valueOf(1), result);
    }

    /**
     * Test method for createFromDouble.
     */
    @org.junit.Test
    public void testCreateFromDouble() throws Exception {
        ValueInstantiator instantiator = new Base(Double.class) {
            @Override
            public Object createFromDouble(DeserializationContext ctxt, double value)
                    throws IOException {
                return value;
            }
        };

        DeserializationContext context = mock(DeserializationContext.class);
        double value = 1.0;

        Object result = instantiator.createFromDouble(context, value);
        assertEquals(Double.class, result.getClass());
        assertEquals(Double.valueOf(1.0), result);
    }

    /**
     * Test method for createFromBoolean.
     */
    @org.junit.Test
    public void testCreateFromBoolean() throws Exception {
        ValueInstantiator instantiator = new Base(Boolean.class) {
            @Override
            public Object createFromBoolean(DeserializationContext ctxt, boolean value)
                    throws IOException {
                return value;
            }
        };

        DeserializationContext context = mock(DeserializationContext.class);
        boolean value = true;

        Object result = instantiator.createFromBoolean(context, value);
        assertEquals(Boolean.class, result.getClass());
        assertEquals(Boolean.valueOf(true), result);
    }

    /**
     * Test method for getDefaultCreator.
     */
    @org.junit.Test
    public void testGetDefaultCreator() throws Exception {
        ValueInstantiator instantiator = new Base(Object.class) {
            @Override
            public AnnotatedWithParams getDefaultCreator() {
                return mock(AnnotatedWithParams.class);
            }
        };

        Object result = instantiator.getDefaultCreator();
        fail("Expected null");
    }

    /**
     * Test method for getDelegateCreator.
     */
    @org.junit.Test
    public void testGetDelegateCreator() throws Exception {
        ValueInstantiator instantiator = new Base(Object.class) {
            @Override
            public AnnotatedWithParams getDelegateCreator() {
                return mock(AnnotatedWithParams.class);
            }
        };

        AnnotatedWithParams result = instantiator.getDelegateCreator();
        fail("Expected null");
    }

    /**
     * Test method for getArrayDelegateCreator.
     */
    @org.junit.Test
    public void testGetArrayDelegateCreator() throws Exception {
        ValueInstantiator instantiator = new Base(Object[].class) {
            @Override
            public AnnotatedWithParams getArrayDelegateCreator() {
                return mock(AnnotatedWithParams.class);
            }
        };

        AnnotatedWithParams result = instantiator.getArrayDelegateCreator();
        fail("Expected null");
    }

    /**
     * Test method for getWithArgsCreator.
     */
    @org.junit.Test
    public void testGetWithArgsCreator() throws Exception {
        ValueInstantiator instantiator = new Base(Object[].class) {
            @Override
            public AnnotatedWithParams getWithArgsCreator() {
                return mock(AnnotatedWithParams.class);
            }
        };

        AnnotatedWithParams result = instantiator.getWithArgsCreator();
        fail("Expected null");
    }

    /**
     * Test method for getIncompleteParameter.
     */
    @org.junit.Test
    public void testGetIncompleteParameter() throws Exception {
        ValueInstantiator instantiator = new Base(Object[].class) {
            @Override
            public AnnotatedParameter getIncompleteParameter() {
                return mock(AnnotatedParameter.class);
            }
        };

        AnnotatedParameter result = instantiator.getIncompleteParameter();
        fail("Expected null");
    }

    /**
     * Test method for getFromObjectArguments.
     */
    @org.junit.Test
    public void testGetFromObjectArguments() throws Exception {
        ValueInstantiator instantiator = new Base(Object[].class) {
            @Override
            public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
                return new SettableBeanProperty[0];
            }
        };

        DeserializationConfig config = mock(DeserializationConfig.class);
        SettableBeanProperty[] result = instantiator.getFromObjectArguments(config);
        assertNull("Expected empty array", result);
    }

    /**
     * Test method for getDelegateType.
     */
    @org.junit.Test
    public void testGetDelegateType() throws Exception {
        ValueInstantiator instantiator = new Base(Object.class) {
            @Override
            public JavaType getDelegateType(DeserializationConfig config) {
                return null;
            }
        };

        DeserializationConfig config = mock(DeserializationConfig.class);
        JavaType result = instantiator.getDelegateType(config);
        assertNull("Expected null");
    }

    /**
     * Test method for getArrayDelegateType.
     */
    @org.junit.Test
    public void testGetArrayDelegateType() throws Exception {
        ValueInstantiator instantiator = new Base(Object[].class) {
            @Override
            public JavaType getArrayDelegateType(DeserializationConfig config) {
                return null;
            }
        };

        DeserializationConfig config = mock(DeserializationConfig.class);
        JavaType result = instantiator.getArrayDelegateType(config);
        assertNull("Expected null");
    }
}
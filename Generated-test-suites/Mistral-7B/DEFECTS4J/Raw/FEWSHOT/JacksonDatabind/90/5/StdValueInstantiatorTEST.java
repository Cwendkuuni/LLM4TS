package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.base.JsonMappingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;

public class StdValueInstantiatorTest {

    @Test
    public void testStdValueInstantiator() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null);
        assertEquals("UNKNOWN TYPE", instantiator.getValueTypeDesc());
        assertEquals(Object.class, instantiator.getValueClass());
        assertFalse(instantiator.canCreateUsingDefault());
        assertFalse(instantiator.canCreateUsingDelegate());
        assertFalse(instantiator.canCreateFromObjectWith());
        assertFalse(instantiator.canCreateUsingArrayDelegate());
        assertFalse(instantiator.canCreateFromString());
        assertFalse(instantiator.canCreateFromInt());
        assertFalse(instantiator.canCreateFromLong());
        assertFalse(instantiator.canCreateFromDouble());
        assertFalse(instantiator.canCreateFromBoolean());

        JavaType javaType = null;
        StdValueInstantiator instantiator1 = new StdValueInstantiator(null, javaType);
        assertEquals("UNKNOWN TYPE", instantiator1.getValueTypeDesc());
        assertEquals(Object.class, instantiator1.getValueClass());
    }

    @Test
    public void testConfigureFromObjectSettings() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null);

        AnnotatedWithParams defaultCreator = mock(AnnotatedWithParams.class);
        AnnotatedWithParams delegateCreator = mock(AnnotatedWithParams.class);
        JavaType delegateType = mock(JavaType.class);
        SettableBeanProperty[] delegateArguments = new SettableBeanProperty[]{mock(SettableBeanProperty.class), mock(SettableBeanProperty.class)};
        AnnotatedWithParams withArgsCreator = mock(AnnotatedWithParams.class);
        SettableBeanProperty[] constructorArguments = new SettableBeanProperty[]{mock(SettableBeanProperty.class), mock(SettableBeanProperty.class)};

        instantiator.configureFromObjectSettings(defaultCreator, delegateCreator, delegateType, delegateArguments, withArgsCreator, constructorArguments);

        assertEquals(defaultCreator, instantiator._defaultCreator);
        assertEquals(delegateCreator, instantiator._delegateCreator);
        assertEquals(delegateType, instantiator._delegateType);
        assertArrayEquals(delegateArguments, instantiator._delegateArguments);
        assertEquals(withArgsCreator, instantiator._withArgsCreator);
        assertArrayEquals(constructorArguments, instantiator._constructorArguments);
    }

    @Test
    public void testCreateUsingDefault() throws IOException {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null) {
            @Override
            protected AnnotatedWithParams _defaultCreator;

            @Override
            public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
                return new Object() {};
            }
        };

        AnnotatedWithParams defaultCreator = mock(AnnotatedWithParams.class);

        instantiator._defaultCreator = defaultCreator;

        DeserializationContext context = mock(DeserializationContext.class);

        Object result = instantiator.createUsingDefault(context);

        verify(defaultCreator).call();
        assertEquals(result.getClass(), instantiator.getValueClass());
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null) {
            @Override
            protected AnnotatedWithParams _withArgsCreator;

            @Override
            public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
                return new Object() {};
            }
        };

        AnnotatedWithParams withArgsCreator = mock(AnnotatedWithParams.class);

        instantiator._withArgsCreator = withArgsCreator;

        DeserializationContext context = mock(DeserializationContext.class);
        Object[] args = new Object[]{new Object(){}};

        Object result = instantiator.createFromObjectWith(context, args);

        verify(withArgsCreator).call(args);
        assertEquals(result.getClass(), instantiator.getValueClass());
    }

    @Test
    public void testCreateUsingDelegate() throws IOException {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null) {
            @Override
            protected AnnotatedWithParams _delegateCreator;
            protected JavaType _delegateType;

            @Override
            public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
                return new Object() {};
            }
        };

        AnnotatedWithParams delegateCreator = mock(AnnotatedWithParams.class);
        JavaType delegateType = mock(JavaType.class);

        instantiator._delegateCreator = delegateCreator;
        instantiator._delegateType = delegateType;

        DeserializationContext context = mock(DeserializationContext.class);
        Object delegate = new Object(){};

        Object result = instantiator.createUsingDelegate(context, delegate);

        verify(delegateCreator).call();
        assertEquals(result.getClass(), instantiator.getValueClass());
    }

    @Test
    public void testCreateUsingArrayDelegate() throws IOException {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null) {
            @Override
            protected AnnotatedWithParams _arrayDelegateCreator;
            protected JavaType _arrayDelegateType;

            @Override
            public Object createUsingArrayDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
                return new Object() {};
            }
        };

        AnnotatedWithParams arrayDelegateCreator = mock(AnnotatedWithParams.class);
        JavaType arrayDelegateType = mock(JavaType.class);

        instantiator._arrayDelegateCreator = arrayDelegateCreator;
        instantiator._arrayDelegateType = arrayDelegateType;

        DeserializationContext context = mock(DeserializationContext.class);
        Object delegate = new Object(){};

        Object result = instantiator.createUsingArrayDelegate(context, delegate);

        verify(arrayDelegateCreator).call();
        assertEquals(result.getClass(), instantiator.getValueClass());
    }

    @Test
    public void testCreateFromString() throws IOException {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null) {
            @Override
            protected AnnotatedWithParams _fromStringCreator;

            @Override
            public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
                return new Object() {};
            }
        };

        AnnotatedWithParams fromStringCreator = mock(AnnotatedWithParams.class);

        instantiator._fromStringCreator = fromStringCreator;

        DeserializationContext context = mock(DeserializationContext.class);
        String value = "value";

        Object result = instantiator.createFromString(context, value);

        verify(fromStringCreator).call1(value);
        assertEquals(result.getClass(), instantiator.getValueClass());
    }

    @Test
    public void testCreateFromInt() throws IOException {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null) {
            @Override
            protected AnnotatedWithParams _fromIntCreator;

            @Override
            public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
                return new Object() {};
            }
        };

        AnnotatedWithParams fromIntCreator = mock(AnnotatedWithParams.class);

        instantiator._fromIntCreator = fromIntCreator;

        DeserializationContext context = mock(DeserializationContext.class);
        int value = 0;

        Object result = instantiator.createFromInt(context, value);

        verify(fromIntCreator).call1(Integer.valueOf(value));
        assertEquals(result.getClass(), instantiator.getValueClass());
    }

    @Test
    public void testCreateFromLong() throws IOException {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null) {
            @Override
            protected AnnotatedWithParams _fromLongCreator;

            @Override
            public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
                return new Object() {};
            }
        };

        AnnotatedWithParams fromLongCreator = mock(AnnotatedWithParams.class);

        instantiator._fromLongCreator = fromLongCreator;

        DeserializationContext context = mock(DeserializationContext.class);
        long value = 0L;

        Object result = instantiator.createFromLong(context, value);

        verify(fromLongCreator).call1(Long.valueOf(value));
        assertEquals(result.getClass(), instantiator.getValueClass());
    }

    @Test
    public void testCreateFromDouble() throws IOException {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null) {
            @Override
            protected AnnotatedWithParams _fromDoubleCreator;

            @Override
            public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
                return new Object() {};
            }
        };

        AnnotatedWithParams fromDoubleCreator = mock(AnnotatedWithParams.class);

        instantiator._fromDoubleCreator = fromDoubleCreator;

        DeserializationContext context = mock(DeserializationContext.class);
        double value = 0.0;

        Object result = instantiator.createFromDouble(context, value);

        verify(fromDoubleCreator).call1(Double.valueOf(value));
        assertEquals(result.getClass(), instantiator.getValueClass());
    }

    @Test
    public void testCreateFromBoolean() throws IOException {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null) {
            @Override
            protected AnnotatedWithParams _fromBooleanCreator;

            @Override
            public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
                return new Object() {};
            }
        };

        AnnotatedWithParams fromBooleanCreator = mock(AnnotatedWithParams.class);

        instantiator._fromBooleanCreator = fromBooleanCreator;

        DeserializationContext context = mock(DeserializationContext.class);
        boolean value = false;

        Object result = instantiator.createFromBoolean(context, value);

        verify(fromBooleanCreator).call1(Boolean.valueOf(value));
        assertEquals(result.getClass(), instantiator.getValueClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateWithoutSupportedMethod() throws IOException {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null);

        DeserializationContext context = mock(DeserializationContext.class);

        instantiator.createUsingDefault(context);
    }
}
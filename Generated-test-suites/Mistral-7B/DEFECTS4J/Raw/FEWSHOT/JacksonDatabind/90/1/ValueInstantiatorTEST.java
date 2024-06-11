package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class ValueInstantiatorTest {

    private static class TestValueInstantiator extends ValueInstantiator {
        @Override
        public boolean canCreateUsingDefault() {
            return true;
        }

        @Override
        public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
            return new Object();
        }
    }

    @Test
    public void testCanInstantiate() throws Exception {
        ValueInstantiator instantiator = new TestValueInstantiator(Object.class);
        assertTrue(instantiator.canInstantiate());
    }

    @Test
    public void testCannotInstantiate() throws Exception {
        ValueInstantiator instantiator = new TestValueInstantiator(String.class);
        assertFalse(instantiator.canInstantiate());
    }

    @Test
    public void testCreateUsingDefault() throws Exception {
        ValueInstantiator instantiator = new TestValueInstantiator(Object.class);
        Object result = instantiator.createUsingDefault(null);
        assertNotNull(result);
    }

    @Test(expected = IOException.class)
    public void testCreateUsingDefaultWithError() throws Exception {
        TestValueInstantiator instantiator = new TestValueInstantiator(String.class);
        instantiator.createUsingDefault(null);
    }

    @Test
    public void testGetStringArgumentCreator() throws Exception {
        ValueInstantiator instantiator = new TestStringArgumentValueInstantiator("test");
        Object result = instantiator.createUsingDefault(null);
        assertEquals("test", result);
    }

    @Test
    public void testGetIntArgumentCreator() throws Exception {
        ValueInstantiator instantiator = new TestIntArgumentValueInstantiator(42);
        Object result = instantiator.createUsingDefault(null);
        assertEquals(42, result.hashCode());
    }

    @Test(expected = IOException.class)
    public void testMissingCreatorWithArgs() throws Exception {
        TestValueInstantiator instantiator = new TestValueInstantiator(String[].class) {
            @Override
            public boolean canCreateFromObjectWith() {
                return true;
            }
        };
        instantiator.createFromObjectWith(null, null);
    }

    @Test
    public void testGetDelegateType() throws Exception {
        ValueInstantiator instantiator = new TestDelegateValueInstantiator(String.class, String[].class);
        JavaType delegateType = instantiator.getDelegateType(null);
        assertNotNull(delegateType);
        assertEquals(String[].class, delegateType.getRawClass());
    }

    @Test(expected = NullPointerException.class)
    public void testGetDelegateTypeNull() throws Exception {
        TestValueInstantiator instantiator = new TestValueInstantiator(Object.class);
        instantiator.getDelegateType(null);
    }

    private static class TestStringArgumentValueInstantiator extends ValueInstantiator {
        private final String _argument;

        public TestStringArgumentValueInstantiator(String argument) {
            _argument = argument;
        }

        @Override
        public boolean canCreateFromString() {
            return true;
        }

        @Override
        public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
            return new Object() {
                @Override
                public String toString() {
                    return _argument;
                }
            };
        }
    }

    private static class TestIntArgumentValueInstantiator extends ValueInstantiator {
        private final int _argument;

        public TestIntArgumentValueInstantiator(int argument) {
            _argument = argument;
        }

        @Override
        public boolean canCreateFromInt() {
            return true;
        }

        @Override
        public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
            return new Object() {
                @Override
                public int hashCode() {
                    return _argument;
                }
            };
        }
    }

    private static class TestDelegateValueInstantiator extends ValueInstantiator {
        private final Class<?> _valueClass;
        private final Class<?> _delegateClass;

        public TestDelegateValueInstantiator(Class<?> valueClass, Class<?> delegateClass) {
            _valueClass = valueClass;
            _delegateClass = delegateClass;
        }

        @Override
        public boolean canCreateUsingDelegate() {
            return true;
        }

        @Override
        public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
            Constructor<ValueInstantiator> constructor = _delegateClass.getDeclaredConstructor(ValueInstantiator.class);
            constructor.setAccessible(true);
            ValueInstantiator instantiator = (ValueInstantiator) constructor.newInstance(delegate);
            return instantiator.createUsingDefault(ctxt);
        }

        @Override
        public JavaType getDelegateType(DeserializationConfig config) {
            try {
                Method method = _delegateClass.getDeclaredMethod("getDelegateType");
                method.setAccessible(true);
                return (JavaType) method.invoke(null);
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
        }
    }
}
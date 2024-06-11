package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.base.ParserArgs;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class ValueInstantiatorTest {

    @Test
    public void testValueInstantiatorAbstractClass() {
        ValueInstantiator instantiator = new StdValueInstantiator();
        assertNotNull(instantiator);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAbstractMethods() {
        ValueInstantiator instantiator = new StdValueInstantiator();
        instantiator.getDefaultCreator();
    }

    @Test
    public void testCanInstantiate() throws IOException {
        ValueInstantiator instantiator = new StdValueInstantiator();
        assertTrue(instantiator.canInstantiate());
    }

    @Test
    public void testCreateUsingDefault() throws IOException {
        ValueInstantiator instantiator = new StdValueInstantiator();
        Object obj = instantiator.createUsingDefault(createContext());
        assertNotNull(obj);
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        ValueInstantiator instantiator = new StdValueInstantiator();
        Object[] args = new Object[]{new Integer(1), new String("test")};
        Object obj = instantiator.createFromObjectWith(createContext(), args);
        assertNotNull(obj);
    }

    @Test
    public void testCreateUsingDelegate() throws IOException {
        ValueInstantiator instantiator = new StdValueInstantiator() {
            @Override
            public AnnotatedWithParams getDelegateCreator() {
                return new AnnotatedWithParams(new Constructor<Object>() {
                    @Override
                    public Object newInstance(Object... initargs) throws InvocationTargetException, InstantiationException {
                        return new Object() {
                            @Override
                            public String toString() {
                                return "DelegateInstance";
                            }
                        };
                    }
                }.getDeclaringClass(), null);
            }
        };
        Object obj = instantiator.createUsingDelegate(createContext(), new Object());
        assertNotNull(obj);
        assertEquals("DelegateInstance", obj.toString());
    }

    @Test
    public void testCreateFromString() throws IOException {
        ValueInstantiator instantiator = new StdValueInstantiator() {
            @Override
            public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
                return "StringValue";
            }
        };
        Object obj = instantiator.createFromString(createContext(), "TestString");
        assertEquals("StringValue", obj);
    }

    @Test
    public void testCreateFromInt() throws IOException {
        ValueInstantiator instantiator = new StdValueInstantiator() {
            @Override
            public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
                return value;
            }
        };
        Object obj = instantiator.createFromInt(createContext(), 1);
        assertEquals(1, obj);
    }

    @Test
    public void testCreateFromLong() throws IOException {
        ValueInstantiator instantiator = new StdValueInstantiator() {
            @Override
            public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
                return value;
            }
        };
        Object obj = instantiator.createFromLong(createContext(), 1L);
        assertEquals(1L, obj);
    }

    @Test
    public void testCreateFromDouble() throws IOException {
        ValueInstantiator instantiator = new StdValueInstantiator() {
            @Override
            public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
                return value;
            }
        };
        Object obj = instantiator.createFromDouble(createContext(), 1.0);
        assertEquals(1.0, obj, 0.0);
    }

    @Test
    public void testCreateFromBoolean() throws IOException {
        ValueInstantiator instantiator = new StdValueInstantiator() {
            @Override
            public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
                return value
                        ? Boolean.TRUE
                        : Boolean.FALSE;
            }
        };
        Object obj = instantiator.createFromBoolean(createContext(), true);
        assertTrue(obj instanceof Boolean);
        assertEquals(Boolean.TRUE, obj);

        obj = instantiator.createFromBoolean(createContext(), false);
        assertTrue(obj instanceof Boolean);
        assertEquals(Boolean.FALSE, obj);
    }

    @Test
    public void testCanCreateFromObjectWithEmptyArguments() throws IOException {
        ValueInstantiator instantiator = new StdValueInstantiator();
        SettableBeanProperty[] props = new SettableBeanProperty[]{new SettableBeanProperty(){}};
        assertFalse(instantiator.canCreateFromObjectWith(props));
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testCreateFromObjectWithNullArguments() throws IOException {
        ValueInstantiator instantiator = new StdValueInstantiator();
        Object[] args = new Object[]{null};
        instantiator.createFromObjectWith(createContext(), args);
    }

    @Test
    public void testCreateFromObjectWithMissingArguments() throws IOException {
        ValueInstantiator instantiator = new StdValueInstantiator() {
            @Override
            public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
                return new SettableBeanProperty[]{new SettableBeanProperty(){}};
            }
        };
        SettableBeanProperty[] props = new SettableBeanProperty[]{new SettableBeanProperty(){}};
        assertThrows(IllegalArgumentException.class, () -> instantiator.createFromObjectWith(createContext(), props));
    }

    @Test
    public void testCreateUsingArrayDelegate() throws IOException {
        ValueInstantiator instantiator = new StdValueInstantiator() {
            @Override
            public AnnotatedWithParams getArrayDelegateCreator() {
                return new AnnotatedWithParams(new Constructor<Object[]>() {
                    @Override
                    public Object[] newInstance(Object... initargs) throws InvocationTargetException, InstantiationException {
                        return new Object[0];
                    }
                }.getDeclaringClass(), null);
            }

            @Override
            public Object createUsingArrayDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
                return new Object();
            }
        };
        Object[] args = new Object[0];
        Object obj = instantiator.createUsingArrayDelegate(createContext(), args);
        assertNotNull(obj);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateUsingArrayDelegateWithNoArrayDelegateCreator() throws IOException {
        ValueInstantiator instantiator = new StdValueInstantiator();
        Object[] args = new Object[0];
        instantiator.createUsingArrayDelegate(createContext(), args);
    }

    private DeserializationContext createContext() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.getFactory().createParser("{}", new ParserArgs());
        return mapper.getDeserializationContext().withParser(reader);
    }
}
package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;

import java.io.IOException;

public class ValueInstantiatorTest {

    @SuppressWarnings("unchecked")
    private static final ValueInstantiator INSTANTIATOR = new StdValueInstantiator() {
        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
            // custom implementation for testing purposes
            return new TestObject((String) args[0], (Integer) args[1]);
        }
    };

    @Test
    public void testCanCreateUsingDefault() throws Exception {
        ValueInstantiator valueInstantiator = INSTANTIATOR;
        assertTrue(valueInstantiator.canCreateUsingDefault());
    }

    @Test
    public void testCannotCreateUsingDefault() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public Class<?> getValueClass() {
                return String.class;
            }

            @Override
            public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
                fail();
                return null;
            }
        };

        assertFalse(valueInstantiator.canCreateUsingDefault());
    }

    @Test
    public void testCanCreateFromObjectWith() throws Exception {
        ValueInstantiator valueInstantiator = INSTANTIATOR;
        Object[] args = new Object[]{"testString", 1};
        SettableBeanProperty[] properties = new SettableBeanProperty[2];
        properties[0] = new SettableBeanProperty.Std("stringProperty", String.class);
        properties[1] = new SettableBeanProperty.Std("integerProperty", Integer.class);

        assertTrue(valueInstantiator.canCreateFromObjectWith());
        Object obj = valueInstantiator.createFromObjectWith(mock(DeserializationContext.class), properties);
        assertEquals(new TestObject("testString", 1), obj);
    }

    @Test
    public void testCannotCreateFromObjectWith() throws Exception {
        ValueInstantiator valueInstantiator = INSTANTIATOR;
        assertFalse(valueInstantiator.canCreateFromObjectWith());
    }

    @Test
    public void testCanCreateFromString() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
                return new TestObject(value);
            }
        };

        assertTrue(valueInstantiator.canCreateFromString());
    }

    @Test
    public void testCannotCreateFromString() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
                throw new IOException("Cannot create from string");
            }
        };

        assertFalse(valueInstantiator.canCreateFromString());
    }

    @Test
    public void testCanCreateFromInt() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
                return new TestObject(value);
            }
        };

        assertTrue(valueInstantiator.canCreateFromInt());
    }

    @Test
    public void testCannotCreateFromInt() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
                throw new IOException("Cannot create from int");
            }
        };

        assertFalse(valueInstantiator.canCreateFromInt());
    }

    @Test
    public void testGetValueClass() throws Exception {
        ValueInstantiator valueInstantiator = INSTANTIATOR;
        assertEquals(Object.class, valueInstantiator.getValueClass());
    }

    @Test
    public void testGetValueTypeDesc() throws Exception {
        ValueInstantiator valueInstantiator = INSTANTIATOR;
        assertEquals("java.lang.Object", valueInstantiator.getValueTypeDesc());
    }

    @Test
    public void testCanInstantiate() throws Exception {
        ValueInstantiator valueInstantiator = INSTANTIATOR;
        assertTrue(valueInstantiator.canInstantiate());
    }

    @Test
    public void testCannotInstantiate() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
                return null;
            }
        };

        assertFalse(valueInstantiator.canInstantiate());
    }

    private static class TestObject {
        private final String stringProperty;
        private final int integerProperty;

        public TestObject(String stringProperty, int integerProperty) {
            this.stringProperty = stringProperty;
            this.integerProperty = integerProperty;
        }

        public TestObject(String stringProperty) {
            this.stringProperty = stringProperty;
            this.integerProperty = 0;
        }

        public TestObject(int integerProperty) {
            this.stringProperty = "";
            this.integerProperty = integerProperty;
        }

        public String getStringProperty() {
            return stringProperty;
        }

        public int getIntegerProperty() {
            return integerProperty;
        }
    }
}
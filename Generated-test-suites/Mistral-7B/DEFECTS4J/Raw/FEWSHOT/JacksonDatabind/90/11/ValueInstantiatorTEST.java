package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ValueInstantiatorTest {
    @Test
    public void testCreateFromObjectWith() throws Exception {
        JavaType javaType = new ValueInstantiator.Base(String.class);
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(javaType) {
            @Override
            public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
                assertEquals("arg1", args[0]);
                assertEquals("arg2", args[1]);
                return "created value";
            }
        };

        Object createdValue = valueInstantiator.createFromObjectWith(new DeserializationContext(), new Object[]{"arg1", "arg2"});
        assertEquals("created value", createdValue);
    }

    @Test
    public void testCanCreateFromObjectWith() throws Exception {
        JavaType javaType = new ValueInstantiator.Base(String.class);
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(javaType) {
            @Override
            public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
                return new SettableBeanProperty[]{new SettableBeanProperty("arg1", String.class), new SettableBeanProperty("arg2", String.class)};
            }

            @Override
            public boolean canCreateFromObjectWith() {
                return true;
            }
        };

        assertTrue(valueInstantiator.canCreateFromObjectWith());
    }

    @Test
    public void testCreateUsingDefault() throws Exception {
        JavaType javaType = new ValueInstantiator.Base(String.class);
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(javaType) {
            @Override
            public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
                return "default value";
            }
        };

        Object createdValue = valueInstantiator.createUsingDefault(new DeserializationContext());
        assertEquals("default value", createdValue);
    }

    @Test
    public void testCanCreateUsingDefault() throws Exception {
        JavaType javaType = new ValueInstantiator.Base(String.class);
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(javaType) {
            @Override
            public AnnotatedWithParams getDefaultCreator() {
                return new AnnotatedWithParams(this, createAnnotation());
            }

            @Override
            public boolean canCreateUsingDefault() {
                return true;
            }
        };

        assertTrue(valueInstantiator.canCreateUsingDefault());
    }

    @Test
    public void testCreateUsingDelegate() throws Exception {
        JavaType javaType = new ValueInstantiator.Base(String.class);
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(javaType) {
            @Override
            public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
                assertEquals("delegate", delegate);
                return "created value";
            }

            @Override
            public boolean canCreateUsingDelegate() {
                return true;
            }
        };

        Object createdValue = valueInstantiator.createUsingDelegate(new DeserializationContext(), "delegate");
        assertEquals("created value", createdValue);
    }

    @Test
    public void testCanCreateUsingDelegate_nullDelegate() throws Exception {
        JavaType javaType = new ValueInstantiator.Base(String.class);
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(javaType) {
            @Override
            public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
                fail("should not be called with null delegate");
                return null;
            }

            @Override
            public boolean canCreateUsingDelegate() {
                return true;
            }
        };

        assertNull(valueInstantiator.createUsingDelegate(new DeserializationContext(), null));
    }

    @Test
    public void testCanCreateFromString() throws Exception {
        JavaType javaType = new ValueInstantiator.Base(String.class);
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(javaType) {
            @Override
            public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
                assertEquals("value", value);
                return "created value";
            }

            @Override
            public boolean canCreateFromString() {
                return true;
            }
        };

        Object createdValue = valueInstantiator.createFromString(new DeserializationContext(), "value");
        assertEquals("created value", createdValue);
    }

    @Test
    public void testCanCreateFromInt() throws Exception {
        JavaType javaType = new ValueInstantiator.Base(Integer.class);
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(javaType) {
            @Override
            public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
                assertEquals(42, value);
                return 43;
            }

            @Override
            public boolean canCreateFromInt() {
                return true;
            }
        };

        Object createdValue = valueInstantiator.createFromInt(new DeserializationContext(), 42);
        assertEquals(43, createdValue);
    }

    @Test
    public void testCanCreateFromLong() throws Exception {
        JavaType javaType = new ValueInstantiator.Base(Long.class);
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(javaType) {
            @Override
            public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
                assertEquals(42L, value);
                return 43L;
            }

            @Override
            public boolean canCreateFromLong() {
                return true;
            }
        };

        Object createdValue = valueInstantiator.createFromLong(new DeserializationContext(), 42L);
        assertEquals(43L, createdValue);
    }

    @Test
    public void testCanCreateFromDouble() throws Exception {
        JavaType javaType = new ValueInstantiator.Base(Double.class);
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(javaType) {
            @Override
            public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
                assertEquals(42.0, value, 0.0);
                return 43.0;
            }

            @Override
            public boolean canCreateFromDouble() {
                return true;
            }
        };

        Object createdValue = valueInstantiator.createFromDouble(new DeserializationContext(), 42.0);
        assertEquals(43.0, createdValue);
    }

    @Test
    public void testCanCreateFromBoolean() throws Exception {
        JavaType javaType = new ValueInstantiator.Base(Boolean.class);
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(javaType) {
            @Override
            public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
                assertEquals(true, value);
                return false;
            }

            @Override
            public boolean canCreateFromBoolean() {
                return true;
            }
        };

        Object createdValue = valueInstantiator.createFromBoolean(new DeserializationContext(), true);
        assertFalse(createdValue);
    }

    private static Annotation createAnnotation() {
        return new Annotation() {};
    }
}
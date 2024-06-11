package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Set;

public class ValueInstantiatorTest {

    @Test
    public void testCanCreateDefault() throws IOException, JsonMappingException {
        ValueInstantiator instantiator = new Base(String.class);
        Assert.assertTrue(instantiator.canCreateUsingDefault());
    }

    @Test
    public void testCannotCreateDefaultWhenNoDefaultConstructor() throws IOException, JsonMappingException {
        ValueInstantiator instantiator = new Base(new Class[0].getClass()) {
            @Override
            public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
                throw new UnsupportedOperationException();
            }
        };
        Assert.assertFalse(instantiator.canCreateUsingDefault());
    }

    @Test
    public void testCanCreateFromObjectWith() throws IOException, JsonMappingException {
        ValueInstantiator instantiator = new Base(MyClassWithConstructor.class) {
            @Override
            public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
                return new MyClassWithConstructor(args[0]);
            }

            @Override
            public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
                return new SettableBeanProperty[] { new SettableBeanProperty.Std("arg1") };
            }
        };
        Assert.assertTrue(instantiator.canCreateFromObjectWith());
    }

    @Test
    public void testCannotCreateFromObjectWithWhenNoConstructorArg() throws IOException, JsonMappingException {
        ValueInstantiator instantiator = new Base(MyClassWithConstructor.class) {
            @Override
            public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
                throw new UnsupportedOperationException();
            }

            @Override
            public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
                return Collections.emptyList().toArray(new SettableBeanProperty[0]);
            }
        };
        Assert.assertFalse(instantiator.canCreateFromObjectWith());
    }

    @Test
    public void testCanCreateUsingDelegate() throws IOException, JsonMappingException {
        ValueInstantiator instantiator = new Base(MyClassWithDelegate.class) {
            @Override
            public AnnotatedWithParams getDelegateCreator() {
                return new AnnotatedWithParams(MyClassWithDelegate.class.getDeclaredConstructor());
            }
        };
        Assert.assertTrue(instantiator.canCreateUsingDelegate());
    }

    @Test
    public void testCannotCreateUsingDelegateWhenNoDelegate() throws IOException, JsonMappingException {
        ValueInstantiator instantiator = new Base(MyClassWithDelegate.class) {
            @Override
            public AnnotatedWithParams getDelegateCreator() {
                return null;
            }
        };
        Assert.assertFalse(instantiator.canCreateUsingDelegate());
    }

    @Test
    public void testCanCreateFromString() throws IOException, JsonMappingException {
        ValueInstantiator instantiator = new Base(MyStringClass.class) {
            @Override
            public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
                return new MyStringClass(value);
            }
        };
        Assert.assertTrue(instantiator.canCreateFromString());
    }

    @Test
    public void testCannotCreateFromStringWhenNoStringConstructor() throws IOException, JsonMappingException {
        ValueInstantiator instantiator = new Base(MyClassWithNoStringConstructor.class) {
            @Override
            public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
                throw new UnsupportedOperationException();
            }
        };
        Assert.assertFalse(instantiator.canCreateFromString());
    }

    @Test
    public void testCanCreateFromInt() throws IOException, JsonMappingException {
        ValueInstantiator instantiator = new Base(MyIntClass.class) {
            @Override
            public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
                return new MyIntClass(value);
            }

            @Override
            public Class<?> getValueClass() {
                return Integer.TYPE;
            }
        };
        Assert.assertTrue(instantiator.canCreateFromInt());
    }

    @Test
    public void testCannotCreateFromIntWhenNoIntConstructor() throws IOException, JsonMappingException {
        ValueInstantiator instantiator = new Base(MyClassWithNoIntConstructor.class) {
            @Override
            public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
                throw new UnsupportedOperationException();
            }
        };
        Assert.assertFalse(instantiator.canCreateFromInt());
    }

    @Test
    public void testCanCreateFromLong() throws IOException, JsonMappingException {
        ValueInstantiator instantiator = new Base(MyLongClass.class) {
            @Override
            public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
                return new MyLongClass(value);
            }

            @Override
            public Class<?> getValueClass() {
                return Long.TYPE;
            }
        };
        Assert.assertTrue(instantiator.canCreateFromLong());
    }

    @Test
    public void testCannotCreateFromLongWhenNoLongConstructor() throws IOException, JsonMappingException {
        ValueInstantiator instantiator = new Base(MyClassWithNoLongConstructor.class) {
            @Override
            public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
                throw new UnsupportedOperationException();
            }
        };
        Assert.assertFalse(instantiator.canCreateFromLong());
    }

    @Test
    public void testCanCreateFromDouble() throws IOException, JsonMappingException {
        ValueInstantiator instantiator = new Base(MyDoubleClass.class) {
            @Override
            public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
                return new MyDoubleClass(value);
            }

            @Override
            public Class<?> getValueClass() {
                return Double.TYPE;
            }
        };
        Assert.assertTrue(instantiator.canCreateFromDouble());
    }

    @Test
    public void testCannotCreateFromDoubleWhenNoDoubleConstructor() throws IOException, JsonMappingException {
        ValueInstantiator instantiator = new Base(MyClassWithNoDoubleConstructor.class) {
            @Override
            public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
                throw new UnsupportedOperationException();
            }
        };
        Assert.assertFalse(instantiator.canCreateFromDouble());
    }

    @Test
    public void testCanCreateFromBoolean() throws IOException, JsonMappingException {
        ValueInstantiator instantiator = new Base(MyBooleanClass.class) {
            @Override
            public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
                return new MyBooleanClass(value);
            }

            @Override
            public Class<?> getValueClass() {
                return Boolean.TYPE;
            }
        };
        Assert.assertTrue(instantiator.canCreateFromBoolean());
    }

    @Test
    public void testCannotCreateFromBooleanWhenNoBooleanConstructor() throws IOException, JsonMappingException {
        ValueInstantiator instantiator = new Base(MyClassWithNoBooleanConstructor.class) {
            @Override
            public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
                throw new UnsupportedOperationException();
            }
        };
        Assert.assertFalse(instantiator.canCreateFromBoolean());
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        ValueInstantiator instantiator = new Base(MyClassWithConstructor.class) {
            @Override
            public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
                return new MyClassWithConstructor(args[0]);
            }

            @Override
            public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
                return new SettableBeanProperty[] { new SettableBeanProperty.Std("arg1") };
            }
        };

        Object instance = instantiator.createFromObjectWith(new DeserializationContext(), new Object[] { "arg1" });
        Assert.assertEquals("created instance", "arg1", instance.toString());
    }

    @Test
    public void testCreateUsingDelegate() throws IOException {
        ValueInstantiator instantiator = new Base(MyClassWithDelegate.class) {
            @Override
            public AnnotatedWithParams getDelegateCreator() {
                return new AnnotatedWithParams(MyClassWithDelegate.class.getDeclaredConstructor());
            }
        };

        Object instance = instantiator.createUsingDelegate(new DeserializationContext(), new MyClassWithDelegate());
        Assert.assertEquals("created instance", "delegate", instance.toString());
    }

    @Test
    public void testCreateFromString() throws IOException {
        ValueInstantiator instantiator = new Base(MyStringClass.class) {
            @Override
            public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
                return new MyStringClass(value);
            }
        };

        Object instance = instantiator.createFromString(new DeserializationContext(), "string");
        Assert.assertEquals("created instance", "string", instance.toString());
    }

    @Test
    public void testCreateFromInt() throws IOException {
        ValueInstantiator instantiator = new Base(MyIntClass.class) {
            @Override
            public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
                return new MyIntClass(value);
            }
        };

        Object instance = instantiator.createFromInt(new DeserializationContext(), 42);
        Assert.assertEquals("created instance", 42, instance.intValue());
    }

    @Test
    public void testCreateFromLong() throws IOException {
        ValueInstantiator instantiator = new Base(MyLongClass.class) {
            @Override
            public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
                return new MyLongClass(value);
            }
        };

        Object instance = instantiator.createFromLong(new DeserializationContext(), 42L);
        Assert.assertEquals("created instance", 42, instance.longValue());
    }

    @Test
    public void testCreateFromDouble() throws IOException {
        ValueInstantiator instantiator = new Base(MyDoubleClass.class) {
            @Override
            public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
                return new MyDoubleClass(value);
            }
        };

        Object instance = instantiator.createFromDouble(new DeserializationContext(), 42.0);
        Assert.assertEquals("created instance", 42.0, instance.doubleValue(), 0.0);
    }

    @Test
    public void testCreateFromBoolean() throws IOException {
        ValueInstantiator instantiator = new Base(MyBooleanClass.class) {
            @Override
            public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
                return new MyBooleanClass(value);
            }
        };

        Object instance = instantiator.createFromBoolean(new DeserializationContext(), true);
        Assert.assertEquals("created instance", true, instance.booleanValue());
    }

    private static class MyClassWithConstructor {
        MyClassWithConstructor(String arg) {
            this.arg = arg;
        }

        private String arg;

        @Override
        public String toString() {
            return "created instance";
        }
    }

    private static class MyClassWithDelegate {
        @JsonCreator
        public MyClassWithDelegate(String delegate) {
            this.delegate = delegate;
        }

        private String delegate;

        @Override
        public String toString() {
            return "delegate";
        }

        @JsonValue
        public String value() {
            return delegate;
        }
    }

    private static class MyStringClass {
        MyStringClass(String value) {
            this.value = value;
        }

        private String value;

        @Override
        public String toString() {
            return "string";
        }
    }

    private static class MyIntClass {
        MyIntClass(int value) {
            this.value = value;
        }

        private int value;

        @Override
        public String toString() {
            return "int instance";
        }

        @JsonValue
        public int intValue() {
            return value;
        }
    }

    private static class MyLongClass {
        MyLongClass(long value) {
            this.value = value;
        }

        private long value;

        @Override
        public String toString() {
            return "long instance";
        }

        @JsonValue
        public long longValue() {
            return value;
        }
    }

    private static class MyDoubleClass {
        MyDoubleClass(double value) {
            this.value = value;
        }

        private double value;

        @Override
        public String toString() {
            return "double instance";
        }

        @JsonValue
        public double doubleValue() {
            return value;
        }
    }

    private static class MyBooleanClass {
        MyBooleanClass(boolean value) {
            this.value = value;
        }

        private boolean value;

        @Override
        public String toString() {
            return "boolean instance";
        }

        @JsonValue
        public boolean booleanValue() {
            return value;
        }
    }

    private static class MyClassWithNoStringConstructor {
        // no constructor that takes a String argument
    }

    private static class MyClassWithNoIntConstructor {
        // no constructor that takes an int argument
    }

    private static class MyClassWithNoLongConstructor {
        // no constructor that takes a long argument
    }

    private static class MyClassWithNoDoubleConstructor {
        // no constructor that takes a double argument
    }

    private static class MyClassWithNoBooleanConstructor {
        // no constructor that takes a boolean argument
    }
}

class Base extends ValueInstantiator {
    private final Class<?> _valueType;

    public Base(Class<?> type) {
        _valueType = type;
    }

    public Base(JavaType type) {
        _valueType = type.getRawClass();
    }

    @Override
    public String getValueTypeDesc() {
        return _valueType.getName();
    }

    @Override
    public Class<?> getValueClass() {
        return _valueType;
    }
}
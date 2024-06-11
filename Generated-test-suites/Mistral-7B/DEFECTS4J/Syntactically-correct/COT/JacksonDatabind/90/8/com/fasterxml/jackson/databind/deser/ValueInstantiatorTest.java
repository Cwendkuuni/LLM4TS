package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;

import org.junit.Before;
import org.junit.Test;

public class ValueInstantiatorTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testValueInstantiatorBasicFunctionality() {
        ValueInstantiator valueInstantiator = new StdValueInstantiator(String.class);
        Object createdObject = valueInstantiator.createFromObjectWith(objectMapper, new Object[]{"test"});
        assertEquals("test", createdObject);
    }

    @Test(expected = IllegalStateException.class)
    public void testThrowsIllegalStateExceptionWhenCanInstantiateReturnsFalse() {
        ValueInstantiator valueInstantiator = new StdValueInstantiator(String.class) {
            @Override
            public boolean canCreateUsingDefault() {
                return false;
            }
        };
        valueInstantiator.createUsingDefault(objectMapper);
    }

    @Test
    public void testCanCreateFromString() {
        ValueInstantiator valueInstantiator = new StdValueInstantiator(String.class) {
            @Override
            public boolean canCreateFromString() {
                return true;
            }

            @Override
            public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
                return "string created";
            }
        };

        Object createdObject = valueInstantiator.createFromString(objectMapper, "test");
        assertEquals("string created", createdObject);
    }

    @Test(expected = IllegalStateException.class)
    public void testThrowsIllegalStateExceptionWhenCanCreateFromStringReturnsFalse() {
        ValueInstantiator valueInstantiator = new StdValueInstantiator(String.class) {
            @Override
            public boolean canCreateFromString() {
                return false;
            }
        };

        valueInstantiator.createFromString(objectMapper, "test");
    }

    @Test
    public void testCanCreateFromInt() {
        ValueInstantiator valueInstantiator = new StdValueInstantiator(Integer.class) {
            @Override
            public boolean canCreateFromInt() {
                return true;
            }

            @Override
            public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
                return value;
            }
        };

        Object createdObject = valueInstantiator.createFromInt(objectMapper, 10);
        assertEquals(10, createdObject);
    }

    @Test(expected = IllegalStateException.class)
    public void testThrowsIllegalStateExceptionWhenCanCreateFromIntReturnsFalse() {
        ValueInstantiator valueInstantiator = new StdValueInstantiator(Integer.class) {
            @Override
            public boolean canCreateFromInt() {
                return false;
            }
        };

        valueInstantiator.createFromInt(objectMapper, 10);
    }

    @Test
    public void testCanCreateFromLong() {
        ValueInstantiator valueInstantiator = new StdValueInstantiator(Long.class) {
            @Override
            public boolean canCreateFromLong() {
                return true;
            }

            @Override
            public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
                return value;
            }
        };

        Object createdObject = valueInstantiator.createFromLong(objectMapper, 10L);
        assertEquals(10L, createdObject);
    }

    @Test(expected = IllegalStateException.class)
    public void testThrowsIllegalStateExceptionWhenCanCreateFromLongReturnsFalse() {
        ValueInstantiator valueInstantiator = new StdValueInstantiator(Long.class) {
            @Override
            public boolean canCreateFromLong() {
                return false;
            }
        };

        valueInstantiator.createFromLong(objectMapper, 10L);
    }

    @Test
    public void testCanCreateFromDouble() {
        ValueInstantiator valueInstantiator = new StdValueInstantiator(Double.class) {
            @Override
            public boolean canCreateFromDouble() {
                return true;
            }

            @Override
            public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
                return value;
            }
        };

        Object createdObject = valueInstantiator.createFromDouble(objectMapper, 10.0);
        assertEquals(10.0, createdObject);
    }

    @Test(expected = IllegalStateException.class)
    public void testThrowsIllegalStateExceptionWhenCanCreateFromDoubleReturnsFalse() {
        ValueInstantiator valueInstantiator = new StdValueInstantiator(Double.class) {
            @Override
            public boolean canCreateFromDouble() {
                return false;
            }
        };

        valueInstantiator.createFromDouble(objectMapper, 10.0);
    }

    @Test
    public void testCanCreateUsingDefault() {
        ValueInstantiator valueInstantiator = new StdValueInstantiator(String.class) {
            @Override
            public boolean canCreateUsingDefault() {
                return true;
            }

            @Override
            public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
                return "default created";
            }
        };

        Object createdObject = valueInstantiator.createUsingDefault(objectMapper);
        assertEquals("default created", createdObject);
    }

    @Test(expected = IllegalStateException.class)
    public void testThrowsIllegalStateExceptionWhenCanCreateUsingDefaultReturnsFalse() {
        ValueInstantiator valueInstantiator = new StdValueInstantiator(String.class) {
            @Override
            public boolean canCreateUsingDefault() {
                return false;
            }
        };

        valueInstantiator.createUsingDefault(objectMapper);
    }

    @Test
    public void testCanCreateFromObjectWith() {
        ValueInstantiator valueInstantiator = new StdValueInstantiator(MyClass.class) {
            @Override
            public boolean canCreateFromObjectWith() {
                return true;
            }

            @Override
            public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
                return new MyClass("arg1", "arg2");
            }
        };

        Object[] args = new Object[]{"arg1", "arg2"};
        Object createdObject = valueInstantiator.createFromObjectWith(objectMapper, args);
        assertEquals("arg1", createdObject.getProperty1());
        assertEquals("arg2", createdObject.getProperty2());
    }

    @Test(expected = IllegalStateException.class)
    public void testThrowsIllegalStateExceptionWhenCanCreateFromObjectWithReturnsFalse() {
        ValueInstantiator valueInstantiator = new StdValueInstantiator(MyClass.class) {
            @Override
            public boolean canCreateFromObjectWith() {
                return false;
            }
        };

        valueInstantiator.createFromObjectWith(objectMapper, new Object[0]);
    }

    @Test
    public void testCanCreateUsingDelegate() {
        JavaType javaType = objectMapper.getTypeFactory().constructType(MyDelegateClass.class);
        ValueInstantiator valueInstantiator = new StdValueInstantiator(MyDelegateClass.class) {
            @Override
            public boolean canCreateUsingDelegate() {
                return true;
            }

            @Override
            public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
                return new MyDelegateClass(delegate);
            }

            @Override
            public JavaType getDelegateType(DeserializationConfig config) {
                return javaType;
            }
        };

        Object delegate = new MyDelegateClass("delegate");
        Object createdObject = valueInstantiator.createUsingDelegate(objectMapper, delegate);
        assertEquals("delegate", createdObject.getDelegate());
    }

    @Test(expected = IllegalStateException.class)
    public void testThrowsIllegalStateExceptionWhenCanCreateUsingDelegateReturnsFalse() {
        ValueInstantiator valueInstantiator = new StdValueInstantiator(MyDelegateClass.class) {
            @Override
            public boolean canCreateUsingDelegate() {
                return false;
            }
        };

        valueInstantiator.createUsingDelegate(objectMapper, new Object());
    }

    static class MyClass {
        private String property1;
        private String property2;

        public MyClass(String property1, String property2) {
            this.property1 = property1;
            this.property2 = property2;
        }

        public String getProperty1() {
            return property1;
        }

        public String getProperty2() {
            return property2;
        }
    }

    static class MyDelegateClass {
        private Object delegate;

        public MyDelegateClass(Object delegate) {
            this.delegate = delegate;
        }

        public Object getDelegate() {
            return delegate;
        }
    }

}
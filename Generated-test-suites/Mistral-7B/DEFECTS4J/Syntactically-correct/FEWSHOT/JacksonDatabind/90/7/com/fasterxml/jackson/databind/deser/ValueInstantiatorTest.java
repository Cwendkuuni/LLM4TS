package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class ValueInstantiatorTest {

    @Test
    public void testBase() throws IOException {
        ValueInstantiator inst = new ValueInstantiator.Base(String.class);
        Object result = inst.createFromObjectWith(new DeserializationContext(null, new ObjectMapper()), null);
        assertEquals("result should be null", null, result);
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        ValueInstantiator inst = new ValueInstantiator.Base(MyClass.class) {
            @Override
            public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
                return ctxt.handleMissingInstantiator(getValueClass(), ctxt.getParser(), "error message");
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"arg1\":1, \"arg2\":\"foo\"}");
        Object result = inst.createFromObjectWith(new DeserializationContext(parser, mapper), new Object[] {1, "foo"});

        assertNull("Result should be null", result);
        parser.close();
    }

    @Test
    public void testCreateUsingDefault() throws IOException {
        ValueInstantiator inst = new ValueInstantiator.Base(MyClass.class) {
            @Override
            public Object getDefaultCreator() {
                return new AnnotatedWithParams(MyClass.class.getDeclaredConstructor());
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        Object result = inst.createUsingDefault(new DeserializationContext(new ObjectMapper(), null));
        assertNotNull("Result should not be null", result);
        assertEquals("Result should be instance of MyClass", MyClass.class, result.getClass());
    }

    @Test
    public void testCanInstantiate() throws IOException {
        ValueInstantiator inst = new ValueInstantiator.Base(MyClass.class) {
            @Override
            public boolean canCreateUsingDelegate() {
                return true;
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"arg1\":1, \"arg2\":\"foo\"}");
        Object result = inst.createUsingDelegate(new DeserializationContext(parser, mapper), new Object());

        assertNull("Result should be null", result);
        parser.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromObjectWithNullContext() throws IOException {
        ValueInstantiator inst = new ValueInstantiator.Base(MyClass.class) {
            @Override
            public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
                return null;
            }
        };
        inst.createFromObjectWith(null, new Object[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromObjectWithNullArgs() throws IOException {
        ValueInstantiator inst = new ValueInstantiator.Base(MyClass.class) {
            @Override
            public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
                return null;
            }
        };
        inst.createFromObjectWith(new DeserializationContext(new ObjectMapper(), null), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUsingDefaultNullContext() throws IOException {
        ValueInstantiator inst = new ValueInstantiator.Base(MyClass.class) {
            @Override
            public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        inst.createUsingDefault(null);
    }

    static class MyClass {
        public MyClass() {}

        public MyClass(int i, String s) {}
    }

    interface MyTypeDeserializer extends TypeDeserializer {
        MyClass createUsingDelegate(DeserializationContext ctxt, Object v);
    }

    @java.lang.SuppressWarnings("unchecked")
    static class MyValueInstantiator extends ValueInstantiator {
        private final MyTypeDeserializer _delegate;

        public MyValueInstantiator(MyTypeDeserializer delegate) {
            _delegate = delegate;
        }

        @Override
        public boolean canCreateUsingDelegate() {
            return true;
        }

        @Override
        public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
            return _delegate.createUsingDelegate(ctxt, delegate);
        }
    }
}

class TestValueInstantiator extends ValueInstantiator {
    @Override
    public boolean canCreateFromString() {
        return true;
    }

    @Override
    public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
        return new Object();
    }
}
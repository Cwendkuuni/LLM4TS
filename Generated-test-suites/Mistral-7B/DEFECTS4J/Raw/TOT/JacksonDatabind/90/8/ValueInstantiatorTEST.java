package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.type.ClassIdResolver;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.std.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValueInstantiatorTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDefaultCreator() throws Exception {
        // Test default creator (no arguments)
        ValueInstantiator inst = mapper.getFactory().findValueInstantiator(String.class);
        assertEquals("default creator: test", inst.createUsingDefault(mapper.getDeserializationContext()));
    }

    @Test(expected = IOException.class)
    public void testMissingCreator() throws Exception {
        ValueInstantiator inst = mapper.getFactory().findValueInstantiator(MyClass.class);
        inst.createUsingDefault(mapper.getDeserializationContext());
    }

    @Test
    public void testFromObjectWith() throws Exception {
        ValueInstantiator inst = new MyClassFromObjectWith(Map.of("p1", "v1", "p2", "v2"));
        Object result = inst.createFromObjectWith(mapper.getDeserializationContext(), new Object[]{"v1", "v2"});
        MyClass expected = new MyClass("v1", "v2");
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testFromString() throws Exception {
        ValueInstantiator inst = mapper.getFactory().findValueInstantiator(String.class);
        assertEquals("String creator: test", inst.createFromString(mapper.getDeserializationContext(), "test"));
    }

    @Test
    public void testFromInt() throws Exception {
        ValueInstantiator inst = mapper.getFactory().findValueInstantiator(Integer.class);
        assertEquals(42, inst.createFromInt(mapper.getDeserializationContext(), 42));
    }

    @Test
    public void testFromLong() throws Exception {
        ValueInstantiator inst = mapper.getFactory().findValueInstantiator(Long.class);
        assertEquals(42L, inst.createFromLong(mapper.getDeserializationContext(), 42L));
    }

    @Test
    public void testFromDouble() throws Exception {
        ValueInstantiator inst = mapper.getFactory().findValueInstantiator(Double.class);
        assertEquals(3.14, inst.createFromDouble(mapper.getDeserializationContext(), 3.14), 0.001);
    }

    @Test
    public void testFromBoolean() throws Exception {
        ValueInstantiator inst = mapper.getFactory().findValueInstantiator(Boolean.class);
        assertTrue(inst.createFromBoolean(mapper.getDeserializationContext(), true));
        assertFalse(inst.createFromBoolean(mapper.getDeserializationContext(), false));
    }

    @Test(expected = IOException.class)
    public void testMissingProperties() throws Exception {
        ValueInstantiator inst = new MyClassFromObjectWith(Map.of("p1", "v1"));
        inst.createFromObjectWith(mapper.getDeserializationContext(), new Object[]{"v1", null});
    }

    @Test
    public void testMissingDelegate() throws Exception {
        ValueInstantiator inst = new MyClassWithDelegate(new MyDelegate());
        inst.createUsingDelegate(mapper.getDeserializationContext(), new Object());
    }

    @Test
    public void testMissingArrayDelegate() throws Exception {
        ValueInstantiator inst = new MyClassWithArrayDelegate(new MyArrayDelegate());
        inst.createUsingArrayDelegate(mapper.getDeserializationContext(), new Object[]{new Object[]{}});
    }

    static class MyClass {
        final String p1;
        final String p2;

        MyClass(String p1, String p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        @Override
        public String toString() {
            return "MyClass{" + "p1='" + p1 + '\'' + ", p2='" + p2 + '\'}';
        }
    }

    static class MyClassFromObjectWith extends BaseValueInstantiator<MyClass> {
        MyClassFromObjectWith(Map<String, String> properties) {
            super(MyClass.class, new AnnotatedClass(MyClass.class));
            _valueId = ClassIdResolver.instance.getClassId(MyClass.class);
            setObjectMapper(mapper);
            setProperties(properties);
        }

        @Override
        public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
            ObjectParser p = mapper.getFactory()
                    .findValueSerializerProvider().getObjectParser(MyClass.class);
            return p.getParsedValues();
        }

        @Override
        public MyClass createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
            ObjectParser p = mapper.getFactory()
                    .findValueSerializerProvider().getObjectParser(MyClass.class);
            return p.readValue(p.getStream(), MyClass.class, ctxt.getContextValue("MyClass"));
        }
    }

    static class MyClassWithDelegate extends BaseValueInstantiator<MyClass> {
        MyClassWithDelegate(MyDelegate delegate) {
            super(MyClass.class, new AnnotatedClass(MyClass.class));
            _delegate = delegate;
        }

        @Override
        public MyClass createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
            return (MyClass) _delegate.createUsingDelegate(ctxt, this, delegate);
        }

        private Object _delegate;
    }

    static class MyClassWithArrayDelegate extends BaseValueInstantiator<MyClass> {
        MyClassWithArrayDelegate(MyArrayDelegate delegate) {
            super(MyClass.class, new AnnotatedClass(MyClass.class));
            _delegate = delegate;
        }

        @Override
        public MyClass createUsingArrayDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
            return (MyClass) _delegate.createUsingArrayDelegate(ctxt, this, delegate);
        }

        private Object _delegate;
    }

    static class MyDelegate extends StdValueInstantiator {
        @Override
        public Object createUsingDelegate(DeserializationContext ctxt, ValueInstantiator inst, Object delegate)
                throws IOException {
            return inst.createUsingDefault(ctxt);
        }

        @Override
        public Object createUsingArrayDelegate(DeserializationContext ctxt, ValueInstantiator inst, Object delegate)
                throws IOException {
            return inst.createUsingDefault(ctxt);
        }
    }

    static class MyArrayDelegate extends StdValueInstantiator {
        @Override
        public Object createUsingArrayDelegate(DeserializationContext ctxt, ValueInstantiator inst, Object delegate)
                throws IOException {
            return inst.createUsingDefault(ctxt);
        }
    }
}
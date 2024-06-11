package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import org.junit.Test;

import static org.junit.Assert.*;

public class ValueInstantiatorTest {

    @Test
    public void testCanInstantiate() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(String.class);
        assertTrue(valueInstantiator.canInstantiate());
    }

    @Test
    public void testGetValueTypeDesc() {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(String.class);
        assertEquals("java.lang.String", valueInstantiator.getValueTypeDesc());
    }

    @Test
    public void testGetValueClass() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(String.class);
        assertEquals(String.class, valueInstantiator.getValueClass());
    }

    @Test
    public void testCanCreateFromString() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(String.class);
        assertFalse(valueInstantiator.canCreateFromString());
    }

    @Test
    public void testCanCreateFromInt() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(Integer.class);
        assertFalse(valueInstantiator.canCreateFromInt());
    }

    @Test
    public void testCanCreateFromLong() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(Long.class);
        assertFalse(valueInstantiator.canCreateFromLong());
    }

    @Test
    public void testCanCreateFromDouble() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(Double.class);
        assertFalse(valueInstantiator.canCreateFromDouble());
    }

    @Test
    public void testCanCreateFromBoolean() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(Boolean.class);
        assertFalse(valueInstantiator.canCreateFromBoolean());
    }

    @Test
    public void testCanCreateUsingDefault() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(String.class);
        assertFalse(valueInstantiator.canCreateUsingDefault());
    }

    @Test
    public void testCanCreateUsingDelegate() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(String.class);
        assertFalse(valueInstantiator.canCreateUsingDelegate());
    }

    @Test
    public void testCanCreateFromObjectWith() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(String.class);
        assertFalse(valueInstantiator.canCreateFromObjectWith());
    }

    @Test
    public void testGetFromObjectArguments() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(MyClassWithConstructor.class);
        MyClassWithConstructor obj = new MyClassWithConstructor("foo", 123);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.deserializationContext();
        SettableBeanProperty[] props = new SettableBeanProperty[]{
                mapper.findProperty("fieldOne", MyClassWithConstructor.class),
                mapper.findProperty("fieldTwo", MyClassWithConstructor.class)
        };
        Object result = valueInstantiator.createFromObjectWith(ctxt, props);
        assertEquals(obj, result);
    }

    @Test
    public void testCreateFromObjectWith() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(MyClassWithConstructor.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.deserializationContext();
        MyClassWithConstructor obj = new MyClassWithConstructor("foo", 123);
        Object result = valueInstantiator.createFromObjectWith(ctxt, obj);
        assertEquals(obj, result);
    }

    @Test
    public void testCreateUsingDelegate() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(MyClassWithDelegate.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.deserializationContext();
        MyClassWithDelegate delegate = new MyClassWithDelegate();
        Object result = valueInstantiator.createUsingDelegate(ctxt, delegate);
        assertEquals(delegate, result);
    }

    @Test
    public void testCreateUsingArrayDelegate() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(MyClassWithArrayDelegate.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.deserializationContext();
        MyClassWithArrayDelegate[] arrayDelegate = new MyClassWithArrayDelegate[]{new MyClassWithArrayDelegate()};
        Object result = valueInstantiator.createUsingArrayDelegate(ctxt, arrayDelegate);
        assertEquals(arrayDelegate[0], result);
    }

    @Test
    public void testCreateFromString() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(String.class) {
            @Override
            public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
                return "default";
            }
        };
        ObjectMapper mapper = new ObjectMapper();
        String value = "test";
        Object result = valueInstantiator.createFromString(mapper.deserializationContext(), value);
        assertEquals("default", result);
    }

    @Test
    public void testCreateFromInt() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(Integer.class) {
            @Override
            public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
                return value;
            }
        };
        ObjectMapper mapper = new ObjectMapper();
        int value = 123;
        Object result = valueInstantiator.createFromInt(mapper.deserializationContext(), value);
        assertEquals(value, result);
    }

    @Test
    public void testCreateFromLong() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(Long.class) {
            @Override
            public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
                return value;
            }
        };
        ObjectMapper mapper = new ObjectMapper();
        long value = 123L;
        Object result = valueInstantiator.createFromLong(mapper.deserializationContext(), value);
        assertEquals(value, result);
    }

    @Test
    public void testCreateFromDouble() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(Double.class) {
            @Override
            public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
                return value;
            }
        };
        ObjectMapper mapper = new ObjectMapper();
        double value = 123.0;
        Object result = valueInstantiator.createFromDouble(mapper.deserializationContext(), value);
        assertEquals(value, result);
    }

    @Test
    public void testCreateFromBoolean() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(Boolean.class) {
            @Override
            public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
                return value;
            }
        };
        ObjectMapper mapper = new ObjectMapper();
        boolean value = true;
        Object result = valueInstantiator.createFromBoolean(mapper.deserializationContext(), value);
        assertEquals(value, result);
    }

    @Test
    public void testGetDelegateCreator() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(MyClassWithDelegate.class) {
            @Override
            public AnnotatedWithParams getDelegateCreator() {
                return new AnnotatedMethod(MyClassWithDelegate.class.getDeclaredMethod("methodWithDelegate"));
            }
        };
        assertEquals(MyClassWithDelegate.class.getDeclaredMethod("methodWithDelegate"), valueInstantiator.getDelegateCreator());
    }
}

class MyClassWithConstructor {
    private final String fieldOne;
    private final int fieldTwo;

    public MyClassWithConstructor(String fieldOne, int fieldTwo) {
        this.fieldOne = fieldOne;
        this.fieldTwo = fieldTwo;
    }

    @Override
    public String toString() {
        return "MyClassWithConstructor{" +
                "fieldOne='" + fieldOne + '\'' +
                ", fieldTwo=" + fieldTwo +
                '}';
    }
}

class MyClassWithDelegate {
    private int field;

    public MyClassWithDelegate() {
    }

    public MyClassWithDelegate(int field) {
        this.field = field;
    }

    @JsonCreator
    public static MyClassWithDelegate fromInt(int field) {
        return new MyClassWithDelegate(field);
    }

    public int getField() {
        return field;
    }

    public void setField(int field) {
        this.field = field;
    }

    public String methodWithDelegate() {
        return "delegate method";
    }

    @Override
    public String toString() {
        return "MyClassWithDelegate{" +
                "field=" + field +
                '}';
    }
}

class MyClassWithArrayDelegate {
    private int[] fields;

    public MyClassWithArrayDelegate() {
    }

    public MyClassWithArrayDelegate(int[] fields) {
        this.fields = fields;
    }

    public int[] getFields() {
        return fields;
    }

    public void setFields(int[] fields) {
        this.fields = fields;
    }
}
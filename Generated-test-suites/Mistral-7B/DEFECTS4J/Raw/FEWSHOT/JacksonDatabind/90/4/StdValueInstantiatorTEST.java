package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.deser.*;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StdValueInstantiatorTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testGetValueTypeDesc() {
        StdValueInstantiator inst = new StdValueInstantiator(null, null);
        assertEquals("UNKNOWN TYPE", inst.getValueTypeDesc());

        inst = new StdValueInstantiator(null, Object.class);
        assertEquals("java.lang.Object", inst.getValueTypeDesc());

        inst = new StdValueInstantiator(null, String.class);
        assertEquals("java.lang.String", inst.getValueTypeDesc());
    }

    @Test
    public void testGetValueClass() {
        StdValueInstantiator inst = new StdValueInstantiator(null, null);
        assertEquals(Object.class, inst.getValueClass());

        inst = new StdValueInstantiator(null, Object.class);
        assertEquals(Object.class, inst.getValueClass());

        inst = new StdValueInstantiator(null, String.class);
        assertEquals(String.class, inst.getValueClass());
    }

    @Test
    public void testCanCreateFromString() {
        StdValueInstantiator inst = new StdValueInstantiator(null, null);
        assertFalse(inst.canCreateFromString());

        inst = new StdValueInstantiator(null, String.class);
        assertTrue(inst.canCreateFromString());

        inst = new StdValueInstantiator(null, Number.class);
        assertFalse(inst.canCreateFromString());
    }

    @Test
    public void testCanCreateFromInt() {
        StdValueInstantiator inst = new StdValueInstantiator(null, null);
        assertFalse(inst.canCreateFromInt());

        inst = new StdValueInstantiator(null, Number.class);
        assertTrue(inst.canCreateFromInt());

        inst = new StdValueInstantiator(null, String.class);
        assertFalse(inst.canCreateFromInt());
    }

    @Test
    public void testCanCreateFromLong() {
        StdValueInstantiator inst = new StdValueInstantiator(null, null);
        assertFalse(inst.canCreateFromLong());

        inst = new StdValueInstantiator(null, Number.class);
        assertTrue(inst.canCreateFromLong());

        inst = new StdValueInstantiator(null, String.class);
        assertFalse(inst.canCreateFromLong());
    }

    @Test
    public void testCanCreateFromDouble() {
        StdValueInstantiator inst = new StdValueInstantiator(null, null);
        assertFalse(inst.canCreateFromDouble());

        inst = new StdValueInstantiator(null, Number.class);
        assertTrue(inst.canCreateFromDouble());

        inst = new StdValueInstantiator(null, String.class);
        assertFalse(inst.canCreateFromDouble());
    }

    @Test
    public void testCanCreateFromBoolean() {
        StdValueInstantiator inst = new StdValueInstantiator(null, null);
        assertFalse(inst.canCreateFromBoolean());

        inst = new StdValueInstantiator(null, Boolean.class);
        assertTrue(inst.canCreateFromBoolean());

        inst = new StdValueInstantiator(null, String.class);
        assertFalse(inst.canCreateFromBoolean());
    }

    @Test
    public void testCanCreateUsingDefault() {
        StdValueInstantiator inst = new StdValueInstantiator(null, null);
        assertFalse(inst.canCreateUsingDefault());

        inst = new StdValueInstantiator(null, Object.class);
        assertTrue(inst.canCreateUsingDefault());

        inst = new StdValueInstantiator(null, String.class);
        assertFalse(inst.canCreateUsingDefault());
    }

    @Test
    public void testCanCreateUsingDelegate() {
        StdValueInstantiator inst = new StdValueInstantiator(null, null);
        assertFalse(inst.canCreateUsingDelegate());

        inst = new StdValueInstantiator(null, Object.class);
        assertFalse(inst.canCreateUsingDelegate());

        AnnotatedClass ac = mapper.getTypeFactory().constructType(MyDelegate.class);
        inst = new StdValueInstantiator(null, ac.getRawClass());
        assertTrue(inst.canCreateUsingDelegate());
    }

    @Test
    public void testCanCreateUsingArrayDelegate() {
        StdValueInstantiator inst = new StdValueInstantiator(null, null);
        assertFalse(inst.canCreateUsingArrayDelegate());

        inst = new StdValueInstantiator(null, Object[].class);
        assertTrue(inst.canCreateUsingArrayDelegate());

        inst = new StdValueInstantiator(null, String.class);
        assertFalse(inst.canCreateUsingArrayDelegate());
    }

    @Test
    public void testCanCreateFromObjectWith() {
        StdValueInstantiator inst = new StdValueInstantiator(null, null);
        assertFalse(inst.canCreateFromObjectWith());

        inst = new StdValueInstantiator(null, Object[].class);
        assertFalse(inst.canCreateFromObjectWith());

        inst = new StdValueInstantiator(null, MyWithArgsConstructor.class);
        assertTrue(inst.canCreateFromObjectWith());
    }

    @Test
    public void testGetDelegateType() {
        StdValueInstantiator inst = new StdValueInstantiator(null, null);
        assertNull(inst.getDelegateType(null));

        AnnotatedClass ac = mapper.getTypeFactory().constructType(MyDelegate.class);
        inst = new StdValueInstantiator(null, ac.getRawClass());
        assertEquals(MyDelegate.class, inst.getDelegateType(null));
    }

    @Test
    public void testGetArrayDelegateType() {
        StdValueInstantiator inst = new StdValueInstantiator(null, null);
        assertNull(inst.getArrayDelegateType(null));

        inst = new StdValueInstantiator(null, Object[].class);
        assertEquals(Object[].class, inst.getArrayDelegateType(null));
    }

    @Test
    public void testGetFromObjectArguments() {
        StdValueInstantiator inst = new StdValueInstantiator(null, null);
        assertNull(inst.getFromObjectArguments(null));

        AnnotatedClass ac = mapper.getTypeFactory().constructType(MyWithArgsConstructor.class);
        inst = new StdValueInstantiator(null, ac.getRawClass());
        AnnotatedMethod am = ac.findMethod("myMethod", Integer.TYPE, Integer.TYPE);
        Set<SettableBeanProperty> args = new HashSet<>();
        args.add(new SettableBeanProperty(am, 0));
        args.add(new SettableBeanProperty(am, 1));
        inst.configureFromObjectSettings(null, null, null, null, null, args.toArray(new SettableBeanProperty[0]));
        assertEquals(args.toArray(new SettableBeanProperty[0]), inst.getFromObjectArguments(null));
    }

    @Test
    public void testCreateUsingDefault() throws IOException {
        StdValueInstantiator inst = new StdValueInstantiator(null, Object.class);
        assertEquals(new Object(), inst.createUsingDefault(null));

        inst = new StdValueInstantiator(null, String.class);
        assertEquals("", inst.createUsingDefault(null));

        inst = new StdValueInstantiator(null, MyDefaultConstructor.class);
        assertEquals("myDefault", inst.createUsingDefault(null));
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        AnnotatedClass ac = mapper.getTypeFactory().constructType(MyWithArgsConstructor.class);
        StdValueInstantiator inst = new StdValueInstantiator(null, ac.getRawClass());
        inst.configureFromObjectSettings(null, null, null, null, null, new SettableBeanProperty[] {
                new SettableBeanProperty(ac.findMethod("myMethod", Integer.TYPE, Integer.TYPE), 0),
                new SettableBeanProperty(ac.findMethod("myMethod", Integer.TYPE, Integer.TYPE), 1)
        });
        Integer intArg1 = 1;
        Integer intArg2 = 2;
        Object[] args = {intArg1, intArg2};
        assertEquals(new MyWithArgsConstructor(intArg1, intArg2), inst.createFromObjectWith(null, args));
    }

    @Test
    public void testCreateUsingDelegate() throws IOException {
        AnnotatedClass ac = mapper.getTypeFactory().constructType(MyDelegate.class);
        StdValueInstantiator inst = new StdValueInstantiator(null, ac.getRawClass());
        inst.configureFromDelegateSettings(null, null, null);
        MyDelegate delegate = new MyDelegate();
        assertEquals(delegate, inst.createUsingDelegate(null, delegate));
    }

    @Test
    public void testCreateUsingArrayDelegate() throws IOException {
        StdValueInstantiator inst = new StdValueInstantiator(null, Object[].class);
        inst.configureFromArrayDelegateSettings(null, null, null);
        Object[] args = new Object[]{1, 2, 3};
        assertEquals(new Integer[3], inst.createUsingArrayDelegate(null, args));
    }

    @Test
    public void testCreateFromString() throws IOException {
        StdValueInstantiator inst = new StdValueInstantiator(null, String.class);
        assertEquals("", inst.createFromString(null, ""));

        inst = new StdValueInstantiator(null, MyFromStringConstructor.class);
        assertEquals("myFromString", inst.createFromString(null, "myFromString"));
    }

    @Test
    public void testCreateFromInt() throws IOException {
        StdValueInstantiator inst = new StdValueInstantiator(null, Number.class);
        assertEquals(0, inst.createFromInt(null, 0));

        inst = new StdValueInstantiator(null, MyFromIntConstructor.class);
        assertEquals("myFromInt_0", inst.createFromInt(null, 0));
    }

    @Test
    public void testCreateFromLong() throws IOException {
        StdValueInstantiator inst = new StdValueInstantiator(null, Number.class);
        assertEquals(0L, inst.createFromLong(null, 0L));

        inst = new StdValueInstantiator(null, MyFromLongConstructor.class);
        assertEquals("myFromLong_0", inst.createFromLong(null, 0L));
    }

    @Test
    public void testCreateFromDouble() throws IOException {
        StdValueInstantiator inst = new StdValueInstantiator(null, Number.class);
        assertEquals(0d, inst.createFromDouble(null, 0d));

        inst = new StdValueInstantiator(null, MyFromDoubleConstructor.class);
        assertEquals("myFromDouble_0", inst.createFromDouble(null, 0d));
    }

    @Test
    public void testCreateFromBoolean() throws IOException {
        StdValueInstantiator inst = new StdValueInstantiator(null, Boolean.class);
        assertEquals(false, inst.createFromBoolean(null, false));

        inst = new StdValueInstantiator(null, MyFromBooleanConstructor.class);
        assertEquals("myFromBoolean_false", inst.createFromBoolean(null, false));
        assertEquals("myFromBoolean_true", inst.createFromBoolean(null, true));
    }

    static class MyDelegate {
        // Empty class with no-args constructor
    }

    static class MyWithArgsConstructor {
        private final int arg1;
        private final int arg2;

        public MyWithArgsConstructor(int arg1, int arg2) {
            this.arg1 = arg1;
            this.arg2 = arg2;
        }

        @JsonCreator
        public static MyWithArgsConstructor myMethod(@JsonProperty("arg1") int arg1,
                @JsonProperty("arg2") int arg2) {
            return new MyWithArgsConstructor(arg1, arg2);
        }
    }

    static class MyDefaultConstructor {
        private final String name;

        @JsonCreator
        public MyDefaultConstructor() {
            this.name = "myDefault";
        }

        public String getName() {
            return name;
        }
    }

    static class MyFromStringConstructor {
        private final String name;

        @JsonCreator
        public MyFromStringConstructor(@JsonProperty("name") String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    static class MyFromIntConstructor {
        private final String name;

        @JsonCreator
        public MyFromIntConstructor(@JsonProperty("name") int value) {
            this.name = "myFromInt_" + value;
        }

        public String getName() {
            return name;
        }
    }

    static class MyFromLongConstructor {
        private final String name;

        @JsonCreator
        public MyFromLongConstructor(@JsonProperty("name") long value) {
            this.name = "myFromLong_" + value;
        }

        public String getName() {
            return name;
        }
    }

    static class MyFromDoubleConstructor {
        private final String name;

        @JsonCreator
        public MyFromDoubleConstructor(@JsonProperty("name") double value) {
            this.name = "myFromDouble_" + value;
        }

        public String getName() {
            return name;
        }
    }

    static class MyFromBooleanConstructor {
        private final String name;

        @JsonCreator
        public MyFromBooleanConstructor(@JsonProperty("name") boolean value) {
            this.name = value ? "myFromBoolean_true" : "myFromBoolean_false";
        }

        public String getName() {
            return name;
        }
    }
}
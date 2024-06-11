package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import static org.junit.Assert.*;

public class ValueInstantiatorTest {

    private static class TestValueInstantiator extends ValueInstantiator {
        @Override
        public Class<?> getValueClass() {
            return TestClass.class;
        }

        @Override
        public boolean canCreateUsingDefault() {
            return false;
        }

        @Override
        public boolean canCreateFromObjectWith() {
            return true;
        }

        @Override
        public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
            return new SettableBeanProperty[]{new SettableBeanProperty.Std("field1", String.class), new SettableBeanProperty.Std("field2", Integer.class)};
        }

        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
            TestClass testClass = new TestClass();
            setProperties(testClass, args);
            return testClass;
        }

        private void setProperties(TestClass testClass, Object[] args) {
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                SettableBeanProperty property = getFromObjectArguments(new DeserializationConfig()).get(i);
                Object value = property.getValueProvider().getValueAndReplacePropertyWithValue(arg);
                try {
                    Constructor constructor = TestClass.class.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    constructor.setParameter(i, value);
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"field1\":\"foo\",\"field2\":42}";
        TestClass testClass = mapper.readValue(json, TestClass.class);
        assertEquals("foo", testClass.getField1());
        assertEquals(42, testClass.getField2());
    }

    @Test
    public void testValueInstantiator() {
        ObjectMapper mapper = new ObjectMapper();
        JavaType testClassType = mapper.getTypeFactory().constructType(TestClass.class);
        ValueInstantiator instantiator = new TestValueInstantiator(testClassType);
        assertEquals(TestClass.class, instantiator.getValueClass());
        assertTrue(instantiator.canCreateFromObjectWith());
        assertFalse(instantiator.canCreateUsingDefault());

        Object json = "{\"field1\":\"foo\",\"field2\":42}";
        TestClass testClass = instantiator.createFromObjectWith(mockDeserializationContext(), new Object[]{json});
        assertEquals("foo", testClass.getField1());
        assertEquals(42, testClass.getField2());
    }

    @Test
    public void testValueInstantiator_noCreator() {
        ObjectMapper mapper = new ObjectMapper();
        JavaType testClassType = mapper.getTypeFactory().constructType(TestClass.class);
        ValueInstantiator instantiator = new TestValueInstantiator(testClassType) {
            @Override
            public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
                return new SettableBeanProperty[]{new SettableBeanProperty.Std("field1", String.class), new SettableBeanProperty.Std("field2", Integer.class)};
            }
        };
        assertEquals(TestClass.class, instantiator.getValueClass());
        assertFalse(instantiator.canCreateFromObjectWith());
        assertFalse(instantiator.canCreateUsingDefault());

        Object json = "{\"field1\":\"foo\",\"field2\":42}";
        try {
            instantiator.createFromObjectWith(mockDeserializationContext(), new Object[]{json});
            fail();
        } catch (IllegalStateException e) {
            // expected
        }
    }

    private DeserializationContext mockDeserializationContext() {
        return new MinimalDeserializationContext();
    }

    static class TestClass {
        private String field1;
        private int field2;

        public String getField1() {
            return field1;
        }

        public int getField2() {
            return field2;
        }

        public TestClass() {}

        public TestClass(String field1, int field2) {
            this.field1 = field1;
            this.field2 = field2;
        }
    }
}
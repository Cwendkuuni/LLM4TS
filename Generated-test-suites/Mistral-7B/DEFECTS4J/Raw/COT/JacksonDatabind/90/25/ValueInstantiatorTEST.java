package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

public class ValueInstantiatorTest {

    @Test
    public void testValueInstantiator_Default() throws IOException, JsonProcessingException {
        StdValueInstantiator testee = new StdValueInstantiator(String.class);

        DeserializationContext ctx = new TestContext();
        assertFalse(testee.canInstantiate());
        assertEquals("UNKNOWN", testee.getValueTypeDesc());
        assertNull(testee.getDefaultCreator());

        Object result = testee.createUsingDefault(ctx);
        assertTrue(result instanceof String);
    }

    @Test
    public void testValueInstantiator_StringFromObject() throws IOException, JsonProcessingException {
        StdValueInstantiator testee = new StdValueInstantiator(String.class);

        String arg = "test";
        Object[] args = new Object[] { arg };
        DeserializationContext ctx = new TestContext();
        SettableBeanProperty[] props = new SettableBeanProperty[0];
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        Object result = testee.createFromObjectWith(ctx, props, buffer);
        assertTrue(result instanceof String);
        assertEquals(arg, result);
    }

    @Test
    public void testValueInstantiator_CanCreateFromString() throws IOException, JsonProcessingException {
        StdValueInstantiator testee = new StdValueInstantiator(String.class) {
            @JsonCreator
            public static String createFromString(DeserializationContext ctxt, String value) throws IOException {
                return value;
            }
        };

        DeserializationContext ctx = new TestContext();
        assertTrue(testee.canCreateFromString());
        assertEquals("java.lang.String", testee.getValueClass().getName());
        assertEquals("java.lang.String", testee.getValueTypeDesc());
        assertSame(testee.createFromString(ctx, "test"), testee.createFromObjectWith(ctx, new Object[0]));
    }

    @Test
    public void testValueInstantiator_CanCreateFromInt() throws IOException, JsonProcessingException {
        StdValueInstantiator testee = new StdValueInstantiator(Integer.class) {
            @JsonCreator
            public static Integer createFromInt(DeserializationContext ctxt, int value) throws IOException {
                return value;
            }
        };

        DeserializationContext ctx = new TestContext();
        assertTrue(testee.canCreateFromInt());
        assertEquals("int", testee.getValueClass().getName());
        assertEquals("int", testee.getValueTypeDesc());
        assertEquals(42, testee.createFromInt(ctx, 42));
    }

    @Test
    public void testValueInstantiator_CanCreateFromLong() throws IOException, JsonProcessingException {
        StdValueInstantiator testee = new StdValueInstantiator(Long.class) {
            @JsonCreator
            public static Long createFromLong(DeserializationContext ctxt, long value) throws IOException {
                return value;
            }
        };

        DeserializationContext ctx = new TestContext();
        assertTrue(testee.canCreateFromLong());
        assertEquals("long", testee.getValueClass().getName());
        assertEquals("long", testee.getValueTypeDesc());
        assertEquals(42L, testee.createFromLong(ctx, 42L));
    }

    @Test
    public void testValueInstantiator_CanCreateFromDouble() throws IOException, JsonProcessingException {
        StdValueInstantiator testee = new StdValueInstantiator(Double.class) {
            @JsonCreator
            public static Double createFromDouble(DeserializationContext ctxt, double value) throws IOException {
                return value;
            }
        };

        DeserializationContext ctx = new TestContext();
        assertTrue(testee.canCreateFromDouble());
        assertEquals("double", testee.getValueClass().getName());
        assertEquals("double", testee.getValueTypeDesc());
        assertEquals(3.141592653589793, testee.createFromDouble(ctx, 3.141592653589793), 0.001);
    }

    @Test
    public void testValueInstantiator_CanCreateFromBoolean() throws IOException, JsonProcessingException {
        StdValueInstantiator testee = new StdValueInstantiator(Boolean.class) {
            @JsonCreator
            public static Boolean createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
                return value;
            }
        };

        DeserializationContext ctx = new TestContext();
        assertTrue(testee.canCreateFromBoolean());
        assertEquals("boolean", testee.getValueClass().getName());
        assertEquals("boolean", testee.getValueTypeDesc());
        assertEquals(true, testee.createFromBoolean(ctx, true));
    }

    @Test
    public void testValueInstantiator_CanCreateUsingDefault() throws IOException, JsonProcessingException {
        StdValueInstantiator testee = new StdValueInstantiator(String.class);

        Object result = testee.createUsingDefault(new TestContext());
        assertTrue(result instanceof String);
    }

    @Test
    public void testValueInstantiator_CanCreateUsingDelegate() throws IOException, JsonProcessingException {
        StdValueInstantiator testee = new TestDelegateValueInstantiator(String.class);

        DeserializationContext ctx = new TestContext();
        Object delegate = new Object();
        Object result = testee.createUsingDelegate(ctx, delegate);
        assertTrue(result instanceof String);
    }

    @Test
    public void testValueInstantiator_CanCreateUsingArrayDelegate() throws IOException, JsonProcessingException {
        StdValueInstantiator testee = new TestArrayDelegateValueInstantiator(String.class);

        DeserializationContext ctx = new TestContext();
        Object delegate = new Object[0];
        Object result = testee.createUsingArrayDelegate(ctx, delegate);
        assertTrue(result instanceof String);
    }

    @Test
    public void testValueInstantiator_CannotCreateUsingArgs() throws IOException, JsonProcessingException {
        StdValueInstantiator testee = new TestArgValueInstantiator(String.class);

        DeserializationContext ctx = new TestContext();
        SettableBeanProperty[] props = new SettableBeanProperty[] { new TestProperty() };
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        Object result = testee.createFromObjectWith(ctx, props, buffer);
        assertNull(result);
    }

    @Test
    public void testValueInstantiator_CannotCreateUsingArgsWithType() throws IOException, JsonProcessingException {
        StdValueInstantiator testee = new TestArgValueInstantiator(String.class);

        DeserializationContext ctx = new TestContext();
        SettableBeanProperty[] props = new SettableBeanProperty[] { new TestProperty() };
        TypeDeserializer typeDeserializer = ctx.findValueDeserializer(String.class, null);
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        Object result = testee.createFromObjectWith(ctx, props, buffer, typeDeserializer);
        assertNull(result);
    }

    @Test
    public void testValueInstantiator_CannotCreateUsingArgsWithInvalidType() throws IOException, JsonProcessingException {
        StdValueInstantiator testee = new TestArgValueInstantiator(Integer.class);

        DeserializationContext ctx = new TestContext();
        SettableBeanProperty[] props = new SettableBeanProperty[] { new TestProperty() };
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        Object result = testee.createFromObjectWith(ctx, props, buffer);
        assertNull(result);
    }

    private static class TestProperty extends SettableBeanProperty {
        TestProperty() {
            super("test", String.class, null);
        }
    }

    private static class TestDelegateValueInstantiator extends StdValueInstantiator {
        TestDelegateValueInstantiator(Class<?> valueType) {
            super(valueType);
        }

        @Override
        public AnnotatedWithParams getDelegateCreator() {
            return new AnnotatedWithParams(TestDelegateCreator.class);
        }
    }

    private static class TestArgValueInstantiator extends StdValueInstantiator {
        TestArgValueInstantiator(Class<?> valueType) {
            super(valueType);
        }

        @Override
        public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
            return new SettableBeanProperty[] { new TestProperty() };
        }
    }

    private static class TestArrayDelegateValueInstantiator extends StdValueInstantiator {
        TestArrayDelegateValueInstantiator(Class<?> valueType) {
            super(valueType);
        }

        @Override
        public JavaType getArrayDelegateType(DeserializationConfig config) {
            return ClassUtil.primitiveToWrapperType(String.class);
        }
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectReader getRootValueFactory() {
            return null;
        }

        @Override
        public JavaType findValueType(Class<?> valueClass, DeserializationConfig config, BeanDescription valueDescription) {
            return null;
        }
    }

    private static class TestDelegateCreator {
        @JsonCreator
        public static String create(DeserializationContext ctxt, Object[] args) throws IOException {
            return "test";
        }
    }
}
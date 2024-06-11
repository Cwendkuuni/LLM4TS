package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.base.ParserBounds;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ValueInstantiatorTest {

    @Test
    public void testValueInstantiatorBase() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        assertEquals(String.class, instantiator.getValueClass());
        assertEquals("java.lang.String", instantiator.getValueTypeDesc());
        assertTrue(instantiator.canInstantiate());

        Object created = instantiator.createUsingDefault(null);
        assertInstanceOf(String.class, created);
        assertEquals("", (String) created);

        Object createdFromString = instantiator.createFromString(null, "test");
        assertInstanceOf(String.class, createdFromString);
        assertEquals("test", (String) createdFromString);
    }

    @Test
    public void testValueInstantiatorBase_NullArgs() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        Object created = instantiator.createFromObjectWith(null, null);
        assertNull(created);

        Object createdUsingDelegate = instantiator.createUsingDelegate(null, null);
        assertNull(createdUsingDelegate);

        Object createdFromInt = instantiator.createFromInt(null, 0);
        assertNull(createdFromInt);

        Object createdFromLong = instantiator.createFromLong(null, 0L);
        assertNull(createdFromLong);

        Object createdFromDouble = instantiator.createFromDouble(null, 0.0);
        assertNull(createdFromDouble);

        Object createdFromBoolean = instantiator.createFromBoolean(null, false);
        assertNull(createdFromBoolean);
    }

    @Test
    public void testValueInstantiatorBase_StringArgs() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);

        Object createdFromObjectWith = instantiator.createFromObjectWith(null, new Object[]{"test"});
        assertInstanceOf(String.class, createdFromObjectWith);
        assertEquals("test", (String) createdFromObjectWith);

        Object createdUsingDelegate = instantiator.createUsingDelegate(null, "test");
        assertInstanceOf(String.class, createdUsingDelegate);
        assertEquals("test", (String) createdUsingDelegate);

        Object createdFromInt = instantiator.createFromInt(null, 42);
        assertInstanceOf(Integer.class, createdFromInt);
        assertEquals(42, ((Number) createdFromInt).intValue());

        Object createdFromLong = instantiator.createFromLong(null, 42L);
        assertInstanceOf(Long.class, createdFromLong);
        assertEquals(42L, ((Number) createdFromLong).longValue());

        Object createdFromDouble = instantiator.createFromDouble(null, 42.0);
        assertInstanceOf(Double.class, createdFromDouble);
        assertEquals(42.0, ((Number) createdFromDouble).doubleValue());

        Object createdFromBoolean = instantiator.createFromBoolean(null, true);
        assertInstanceOf(Boolean.class, createdFromBoolean);
        assertEquals(true, ((Boolean) createdFromBoolean).booleanValue());
    }

    @Test
    public void testValueInstantiatorBase_CreatorProperties() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(MyClassWithCreatorProperties.class);

        Object createdFromObjectWith = instantiator.createFromObjectWith(null, new Object[]{"test", 42, true});
        assertInstanceOf(MyClassWithCreatorProperties.class, createdFromObjectWith);
        assertEquals("test", ((MyClassWithCreatorProperties) createdFromObjectWith).getValue1());
        assertEquals(42, ((MyClassWithCreatorProperties) createdFromObjectWith).getValue2());
        assertEquals(true, ((MyClassWithCreatorProperties) createdFromObjectWith).getValue3());

        Object createdUsingDelegate = instantiator.createUsingDelegate(null, "test", 42, true);
        assertInstanceOf(MyClassWithCreatorProperties.class, createdUsingDelegate);
        assertEquals("test", ((MyClassWithCreatorProperties) createdUsingDelegate).getValue1());
        assertEquals(42, ((MyClassWithCreatorProperties) createdUsingDelegate).getValue2());
        assertEquals(true, ((MyClassWithCreatorProperties) createdUsingDelegate).getValue3());
    }

    @Test
    public void testValueInstantiatorBase_ToStringSerialization() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(ToStringSerializer.Instance.getValueClass());
        Object createdFromObjectWith = instantiator.createFromObjectWith(null, new Object[]{"test", 42, true});
        assertEquals("test", ToStringSerializer.valueToString(createdFromObjectWith));

        Object createdUsingDelegate = instantiator.createUsingDelegate(null, "test", 42, true);
        assertEquals("test", ToStringSerializer.valueToString(createdUsingDelegate));
    }

    @Test(expected = IllegalStateException.class)
    public void testValueInstantiatorBase_InvalidCreateFromObjectWith() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        instantiator.createFromObjectWith(null, new Object[]{"test", 42, true});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueInstantiatorBase_InvalidCreateUsingDelegate() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        instantiator.createUsingDelegate(null, new Object[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueInstantiatorBase_InvalidCreateFromInt() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        instantiator.createFromInt(null, "test".charAt(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueInstantiatorBase_InvalidCreateFromLong() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        instantiator.createFromLong(null, Short.MIN_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueInstantiatorBase_InvalidCreateFromDouble() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        instantiator.createFromDouble(null, Double.NEGATIVE_INFINITY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueInstantiatorBase_InvalidCreateFromBoolean() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        instantiator.createFromBoolean(null, true);
        instantiator.createFromBoolean(null, false);
        instantiator.createFromBoolean(null, true);
    }

    public static class MyClassWithCreatorProperties {
        private final String value1;
        private final int value2;
        private final boolean value3;

        public MyClassWithCreatorProperties(String value1, int value2, boolean value3) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
        }

        public String getValue1() {
            return value1;
        }

        public int getValue2() {
            return value2;
        }

        public boolean getValue3() {
            return value3;
        }
    }
}

class MyAnnotatedWithParams implements AnnotatedWithParams {
    private final AnnotatedParameter[] _creationProps;

    public MyAnnotatedWithParams(AnnotatedParameter[] params) {
        _creationProps = params;
    }

    @Override
    public AnnotatedParameter[] getCreationProperties() {
        return _creationProps;
    }
}

class MyCreator {
    @javax.annotation.ParametersAreNonnullByDefault
    public static MyClass create(String str, int i, boolean b) {
        return new MyClass(str, i, b);
    }
}

class MyClass {
    private final String _value1;
    private final int _value2;
    private final boolean _value3;

    public MyClass(String value1, int value2, boolean value3) {
        _value1 = value1;
        _value2 = value2;
        _value3 = value3;
    }

    public String getValue1() {
        return _value1;
    }

    public int getValue2() {
        return _value2;
    }

    public boolean getValue3() {
        return _value3;
    }
}
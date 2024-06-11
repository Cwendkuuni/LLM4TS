package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import static org.junit.Assert.*;

public class ValueInstantiatorTest {

    @Test
    public void testBaseClass() throws Exception {
        ValueInstantiator vi = new ValueInstantiator.Base(String.class);
        assertEquals(String.class.getName(), vi.getValueTypeDesc());
        assertEquals(String.class, vi.getValueClass());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetFromObjectArgumentsEmptyList() throws Exception {
        ValueInstantiator vi = new ValueInstantiatorBaseTest();
        SettableBeanProperty[] props = new SettableBeanProperty[0];
        vi.getFromObjectArguments(null, props);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateUsingDefault() throws Exception {
        ValueInstantiator vi = new ValueInstantiatorBaseTest();
        Object ctxt = mock(DeserializationContext.class);
        vi.createUsingDefault(ctxt);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFromObjectWithEmptyArgs() throws Exception {
        ValueInstantiator vi = new ValueInstantiatorBaseTest();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object[] args = new Object[0];
        vi.createFromObjectWith(ctxt, args);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFromObjectWithNullArgs() throws Exception {
        ValueInstantiator vi = new ValueInstantiatorBaseTest();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        SettableBeanProperty[] props = new SettableBeanProperty[0];
        vi.createFromObjectWith(ctxt, props, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateUsingDelegate() throws Exception {
        ValueInstantiator vi = new ValueInstantiatorBaseTest();
        Object delegate = mock(Object.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        vi.createUsingDelegate(ctxt, delegate);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateUsingArrayDelegate() throws Exception {
        ValueInstantiator vi = new ValueInstantiatorBaseTest();
        Object delegate = mock(Object.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        vi.createUsingArrayDelegate(ctxt, delegate);
    }

    @Test
    public void testCreateFromString() throws Exception {
        ValueInstantiator vi = new ValueInstantiatorBaseTest();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        ObjectNode node = mock(ObjectNode.class);
        when(node.getTextValue()).thenReturn("foo");
        String value = vi.createFromString(ctxt, node);
        assertEquals("foo", value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFromInt() throws Exception {
        ValueInstantiator vi = new ValueInstantiatorBaseTest();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        int value = 42;
        vi.createFromInt(ctxt, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFromLong() throws Exception {
        ValueInstantiator vi = new ValueInstantiatorBaseTest();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        long value = 42L;
        vi.createFromLong(ctxt, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFromDouble() throws Exception {
        ValueInstantiator vi = new ValueInstantiatorBaseTest();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        double value = 42.0;
        vi.createFromDouble(ctxt, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFromBoolean() throws Exception {
        ValueInstantiator vi = new ValueInstantiatorBaseTest();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        boolean value = true;
        vi.createFromBoolean(ctxt, value);
    }
}

class ValueInstantiatorBaseTest extends ValueInstantiator {
    // Empty implementation, all methods throw UnsupportedOperationException
}
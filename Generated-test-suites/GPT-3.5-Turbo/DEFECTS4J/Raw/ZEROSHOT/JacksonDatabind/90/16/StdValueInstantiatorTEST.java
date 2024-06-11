package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdValueInstantiatorTest {

    private StdValueInstantiator valueInstantiator;

    @Before
    public void setUp() {
        valueInstantiator = new StdValueInstantiator(null, String.class);
    }

    @Test
    public void testGetValueTypeDesc() {
        assertEquals("java.lang.String", valueInstantiator.getValueTypeDesc());
    }

    @Test
    public void testGetValueClass() {
        assertEquals(String.class, valueInstantiator.getValueClass());
    }

    @Test
    public void testCanCreateFromString() {
        assertFalse(valueInstantiator.canCreateFromString());
    }

    @Test
    public void testCanCreateFromInt() {
        assertFalse(valueInstantiator.canCreateFromInt());
    }

    @Test
    public void testCanCreateFromLong() {
        assertFalse(valueInstantiator.canCreateFromLong());
    }

    @Test
    public void testCanCreateFromDouble() {
        assertFalse(valueInstantiator.canCreateFromDouble());
    }

    @Test
    public void testCanCreateFromBoolean() {
        assertFalse(valueInstantiator.canCreateFromBoolean());
    }

    @Test
    public void testCanCreateUsingDefault() {
        assertFalse(valueInstantiator.canCreateUsingDefault());
    }

    @Test
    public void testCanCreateUsingDelegate() {
        assertFalse(valueInstantiator.canCreateUsingDelegate());
    }

    @Test
    public void testCanCreateUsingArrayDelegate() {
        assertFalse(valueInstantiator.canCreateUsingArrayDelegate());
    }

    @Test
    public void testCanCreateFromObjectWith() {
        assertFalse(valueInstantiator.canCreateFromObjectWith());
    }
}
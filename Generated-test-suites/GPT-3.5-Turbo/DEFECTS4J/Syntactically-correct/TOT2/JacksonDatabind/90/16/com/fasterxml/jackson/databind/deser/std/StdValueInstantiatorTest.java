package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdValueInstantiatorTest {

    private StdValueInstantiator instantiator;

    @Test
    public void testValueTypeDesc() {
        instantiator = new StdValueInstantiator(null, String.class);
        assertEquals("String", instantiator.getValueTypeDesc());
    }

    @Test
    public void testValueClass() {
        instantiator = new StdValueInstantiator(null, Integer.class);
        assertEquals(Integer.class, instantiator.getValueClass());
    }

    @Test
    public void testCanCreateFromString() {
        instantiator = new StdValueInstantiator(null, Boolean.class);
        assertFalse(instantiator.canCreateFromString());
    }

    @Test
    public void testCanCreateFromInt() {
        instantiator = new StdValueInstantiator(null, Float.class);
        assertFalse(instantiator.canCreateFromInt());
    }

    @Test
    public void testCanCreateFromLong() {
        instantiator = new StdValueInstantiator(null, Double.class);
        assertFalse(instantiator.canCreateFromLong());
    }

    @Test
    public void testCanCreateFromDouble() {
        instantiator = new StdValueInstantiator(null, Short.class);
        assertFalse(instantiator.canCreateFromDouble());
    }

    @Test
    public void testCanCreateFromBoolean() {
        instantiator = new StdValueInstantiator(null, Character.class);
        assertFalse(instantiator.canCreateFromBoolean());
    }

    @Test
    public void testCanCreateUsingDefault() {
        instantiator = new StdValueInstantiator(null, Integer.class);
        assertFalse(instantiator.canCreateUsingDefault());
    }

    @Test
    public void testCanCreateUsingDelegate() {
        instantiator = new StdValueInstantiator(null, Float.class);
        assertFalse(instantiator.canCreateUsingDelegate());
    }

    @Test
    public void testCanCreateUsingArrayDelegate() {
        instantiator = new StdValueInstantiator(null, Double.class);
        assertFalse(instantiator.canCreateUsingArrayDelegate());
    }

    @Test
    public void testCanCreateFromObjectWith() {
        instantiator = new StdValueInstantiator(null, Short.class);
        assertFalse(instantiator.canCreateFromObjectWith());
    }

}
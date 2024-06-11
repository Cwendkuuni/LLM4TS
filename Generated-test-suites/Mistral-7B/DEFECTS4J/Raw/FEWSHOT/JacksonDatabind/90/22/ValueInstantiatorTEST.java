package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ValueInstantiatorTest {

    @Test
    public void testDefaultValueInstantiator() throws Exception {
        ValueInstantiator instantiator = new StdValueInstantiator();
        assertEquals(Object.class, instantiator.getValueClass());
        assertEquals("java.lang.Object", instantiator.getValueTypeDesc());
        assertFalse(instantiator.canInstantiate());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedMethods() throws Exception {
        ValueInstantiator instantiator = new StdValueInstantiator();
        instantiator.getDelegateType(null);
        instantiator.createUsingDefault(null);
        instantiator.createFromObjectWith(null, null);
        instantiator.createUsingDelegate(null, null);
        instantiator.createFromString(null, "");
        instantiator.createFromInt(null, 0);
        instantiator.createFromLong(null, 0L);
        instantiator.createFromDouble(null, 0.0);
        instantiator.createFromBoolean(null, false);
        instantiator.getDefaultCreator();
        instantiator.getDelegateCreator();
        instantiator.getArrayDelegateCreator();
        instantiator.getWithArgsCreator();
        instantiator.getIncompleteParameter();
    }

    @Test
    public void testStringBasedInstantiator() throws Exception {
        ValueInstantiator instantiator = new StringBasedValueInstantiator();
        assertEquals(String.class, instantiator.getValueClass());
        assertEquals("java.lang.String", instantiator.getValueTypeDesc());
        assertTrue(instantiator.canInstantiate());
        assertTrue(instantiator.canCreateFromString());
        assertNull(instantiator.getDefaultCreator());
        assertNull(instantiator.getDelegateCreator());
        assertNull(instantiator.getArrayDelegateCreator());
        assertNull(instantiator.getWithArgsCreator());
        assertNull(instantiator.getIncompleteParameter());

        Object created = instantiator.createFromString(null, "test");
        assertEquals("test", created);
    }

    @Test
    public void testIntBasedInstantiator() throws Exception {
        ValueInstantiator instantiator = new IntBasedValueInstantiator();
        assertEquals(Integer.class, instantiator.getValueClass());
        assertEquals("java.lang.Integer", instantiator.getValueTypeDesc());
        assertTrue(instantiator.canInstantiate());
        assertTrue(instantiator.canCreateFromInt());
        assertNull(instantiator.getDefaultCreator());
        assertNull(instantiator.getDelegateCreator());
        assertNull(instantiator.getArrayDelegateCreator());
        assertNull(instantiator.getWithArgsCreator());
        assertNull(instantiator.getIncompleteParameter());

        Object created = instantiator.createFromInt(null, 42);
        assertEquals(42, created);
    }

    @Test
    public void testDelegateBasedInstantiator() throws Exception {
        ValueInstantiator instantiator = new DelegateBasedValueInstantiator();
        assertEquals(Object.class, instantiator.getValueClass());
        assertEquals("java.lang.Object", instantiator.getValueTypeDesc());
        assertTrue(instantiator.canInstantiate());
        assertTrue(instantiator.canCreateUsingDelegate());
        assertNull(instantiator.getDefaultCreator());
        assertNull(instantiator.getDelegateCreator());
        assertNull(instantiator.getArrayDelegateCreator());
        assertNull(instantiator.getWithArgsCreator());
        assertNull(instantiator.getIncompleteParameter());

        Object delegate = new Object();
        Object created = instantiator.createUsingDelegate(null, delegate);
        assertNotNull(created);
    }

    @Test
    public void testCreatorFromObjectWith() throws Exception {
        ValueInstantiator instantiator = new CreatorFromObjectWithValueInstantiator();
        assertEquals(Object.class, instantiator.getValueClass());
        assertEquals("java.lang.Object", instantiator.getValueTypeDesc());
        assertTrue(instantiator.canInstantiate());
        assertTrue(instantiator.canCreateFromObjectWith());
        assertNull(instantiator.getDefaultCreator());
        assertNull(instantiator.getDelegateCreator());
        assertNull(instantiator.getArrayDelegateCreator());
        assertNull(instantiator.getWithArgsCreator());
        assertNull(instantiator.getIncompleteParameter());

        Object[] arguments = new Object[]{"arg1", "arg2"};
        Object created = instantiator.createFromObjectWith(null, arguments);
        assertNotNull(created);
    }

}

class StringBasedValueInstantiator extends ValueInstantiator {
    @Override
    public boolean canCreateFromString() { return true; }
}

class IntBasedValueInstantiator extends ValueInstantiator {
    @Override
    public boolean canCreateFromInt() { return true; }
}

class DelegateBasedValueInstantiator extends ValueInstantiator {
    @Override
    public boolean canCreateUsingDelegate() { return true; }
}

class CreatorFromObjectWithValueInstantiator extends ValueInstantiator {
    @Override
    public boolean canCreateFromObjectWith() { return true; }

    @Override
    public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
        return new SettableBeanProperty[]{new SettableBeanProperty()};
    }
}
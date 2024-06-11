package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.Test;

public class ValueInstantiatorTest {

    @Test
    public void testCanInstantiate() {
        ValueInstantiator inst = new ValueInstantiator() {
        };
        assertTrue(inst.canInstantiate());
    }

    @Test
    public void testCanCreateFromString() {
        ValueInstantiator inst = new ValueInstantiator() {
        };
        assertFalse(inst.canCreateFromString());
    }

    @Test
    public void testCanCreateFromInt() {
        ValueInstantiator inst = new ValueInstantiator() {
        };
        assertFalse(inst.canCreateFromInt());
    }

    @Test
    public void testCanCreateFromLong() {
        ValueInstantiator inst = new ValueInstantiator() {
        };
        assertFalse(inst.canCreateFromLong());
    }

    @Test
    public void testCanCreateFromDouble() {
        ValueInstantiator inst = new ValueInstantiator() {
        };
        assertFalse(inst.canCreateFromDouble());
    }

    @Test
    public void testCanCreateFromBoolean() {
        ValueInstantiator inst = new ValueInstantiator() {
        };
        assertFalse(inst.canCreateFromBoolean());
    }

    @Test
    public void testCanCreateUsingDefault() {
        ValueInstantiator inst = new ValueInstantiator() {
        };
        assertFalse(inst.canCreateUsingDefault());
    }

    @Test
    public void testCanCreateUsingDelegate() {
        ValueInstantiator inst = new ValueInstantiator() {
        };
        assertFalse(inst.canCreateUsingDelegate());
    }

    @Test
    public void testCanCreateUsingArrayDelegate() {
        ValueInstantiator inst = new ValueInstantiator() {
        };
        assertFalse(inst.canCreateUsingArrayDelegate());
    }

    @Test
    public void testCanCreateFromObjectWith() {
        ValueInstantiator inst = new ValueInstantiator() {
        };
        assertFalse(inst.canCreateFromObjectWith());
    }

    @Test
    public void testDefaultValueClass() {
        ValueInstantiator inst = new ValueInstantiator() {
        };
        assertEquals(Object.class, inst.getValueClass());
    }

    @Test
    public void testValueClass() {
        ValueInstantiator inst = new ValueInstantiator() {
        };
        assertEquals("UNKNOWN", inst.getValueTypeDesc());
    }

    @Test(expected=IOException.class)
    public void testCreateUsingDefault() throws IOException {
        ValueInstantiator inst = new ValueInstantiator() {
        };
        inst.createUsingDefault(null);
    }
}
package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() {
        assertNull("Should return null", oidValueProperty.deserializeAndSet(p, ctxt, instance));
    }

    @Test
    public void testDeserializeSetAndReturn() {
        assertNull("Should return null", oidValueProperty.deserializeSetAndReturn(p, ctxt, instance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        oidValueProperty.set(instance, value);
    }

    @Test
    public void testSetAndReturn() {
        assertNull("Should return null", oidValueProperty.setAndReturn(instance, value));
    }
}
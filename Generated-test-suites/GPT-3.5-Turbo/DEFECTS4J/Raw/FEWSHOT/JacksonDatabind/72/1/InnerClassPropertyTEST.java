package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class InnerClassPropertyTest {

    private InnerClassProperty innerClassProperty;

    @Before
    public void setUp() {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> ctor = mock(Constructor.class);
        innerClassProperty = new InnerClassProperty(delegate, ctor);
    }

    @Test
    public void testWithCreator() {
        Object instance = new Object();
        Object value = new Object();
        innerClassProperty.set(instance, value);
        assertEquals(value, innerClassProperty.setAndReturn(instance, value));
    }

    @Test
    public void testDeserializeAndSet() {
        // TODO: Implement test-case
    }

    @Test
    public void testDeserializeSetAndReturn() {
        // TODO: Implement test-case
    }
}
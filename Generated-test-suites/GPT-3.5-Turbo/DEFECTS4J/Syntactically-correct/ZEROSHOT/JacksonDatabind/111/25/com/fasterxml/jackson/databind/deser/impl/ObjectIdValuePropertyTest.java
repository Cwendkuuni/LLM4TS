package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty property;

    @Before
    public void setUp() {
        property = new ObjectIdValueProperty(null, null);
    }

    @Test
    public void testWithName() {
        assertNull(property.withName(null));
    }

    @Test
    public void testWithValueDeserializer() {
        assertNull(property.withValueDeserializer(null));
    }

    @Test
    public void testWithNullProvider() {
        assertNull(property.withNullProvider(null));
    }

    @Test
    public void testGetAnnotation() {
        assertNull(property.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        // TODO: Implement test
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        // TODO: Implement test
    }

    @Test
    public void testSet() throws Exception {
        // TODO: Implement test
    }

    @Test
    public void testSetAndReturn() throws Exception {
        // TODO: Implement test
    }
}
package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;

import static org.junit.Assert.*;

public class SetterlessPropertyTest {

    @Test
    public void testGetAnnotation() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        assertNull(property.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        assertNull(property.getMember());
    }

    @Test
    public void testWithName() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        assertSame(property, property.withName(null));
    }

    @Test
    public void testWithValueDeserializer() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        assertSame(property, property.withValueDeserializer(null));
    }

    @Test
    public void testWithNullProvider() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        assertSame(property, property.withNullProvider(null));
    }

    @Test
    public void testFixAccess() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        property.fixAccess(null);
    }

    @Test
    public void testDeserializeAndSet() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        // TODO: Add test case
    }

    @Test
    public void testDeserializeSetAndReturn() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        // TODO: Add test case

    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        property.set(null, null);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        // TODO: Add test case
    }
}
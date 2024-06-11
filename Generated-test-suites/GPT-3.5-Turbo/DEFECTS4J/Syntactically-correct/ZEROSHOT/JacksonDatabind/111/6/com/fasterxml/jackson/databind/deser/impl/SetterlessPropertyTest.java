package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class SetterlessPropertyTest {

    @Test
    public void testWithNullProvider() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        SetterlessProperty newProperty = property.withNullProvider(null);
        assertNotNull(newProperty);
        assertEquals(null, newProperty.getNullValueProvider());
    }

    @Test
    public void testWithDefaultValueDeserializer() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        SetterlessProperty newProperty = property.withValueDeserializer(null);
        assertNotNull(newProperty);
        assertEquals(null, newProperty.getValueDeserializer());
    }

    @Test
    public void testWithDeserializationConfig() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        DeserializationConfig config = new DeserializationConfig();
        property.fixAccess(config);
        // test if no exception is thrown
    }

    @Test
    public void testGetAnnotation() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        assertEquals(null, property.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        assertEquals(null, property.getMember());
    }

    @Test
    public void testDeserializeAndSetWithNullToken() throws IOException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext(null, null, null);
        Object instance = new Object();
        property.deserializeAndSet(parser, context, instance);
        // test if no exception is thrown
    }

    @Test
    public void testDeserializeAndSetWithNonNullToken() throws IOException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext(null, null, null);
        Object instance = new Object();
        property.deserializeAndSet(parser, context, instance);
        // test if no exception is thrown
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext(null, null, null);
        Object instance = new Object();
        Object result = property.deserializeSetAndReturn(parser, context, instance);
        assertSame(instance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        property.set(null, null);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = new Object();
        Object result = property.setAndReturn(instance, null);
        assertSame(instance, result);
    }

}
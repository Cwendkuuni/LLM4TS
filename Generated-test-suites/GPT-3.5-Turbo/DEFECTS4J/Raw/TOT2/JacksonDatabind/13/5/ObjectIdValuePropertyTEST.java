package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {

    @Test
    public void testWithJsonParser() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNotNull(property);
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        assertThrows(IOException.class, () -> {
            property.deserializeAndSet(jp, ctxt, instance);
        });
    }

    @Test
    public void testSetAndReturn() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNotNull(property);
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        assertThrows(IOException.class, () -> {
            property.deserializeSetAndReturn(jp, ctxt, instance);
        });
    }

    @Test
    public void testSetObject() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNotNull(property);
        Object instance = new Object();
        Object value = new Object();
        assertThrows(UnsupportedOperationException.class, () -> {
            property.set(instance, value);
        });
    }

    @Test
    public void testSetAndReturnObject() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNotNull(property);
        Object instance = new Object();
        Object value = new Object();
        assertThrows(UnsupportedOperationException.class, () -> {
            property.setAndReturn(instance, value);
        });
    }
}
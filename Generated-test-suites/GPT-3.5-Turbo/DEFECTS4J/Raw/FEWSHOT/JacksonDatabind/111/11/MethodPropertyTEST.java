package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MethodPropertyTest {
    @Test
    public void testConstructor1() {
        MethodProperty property = new MethodProperty(/* propDef */ null, /* type */ null, /* typeDeser */ null, /* contextAnnotations */ null, /* method */ null);
        assertNull(property.getNullProvider());
        assertNull(property.getMember());
    }

    @Test
    public void testConstructor2() {
        MethodProperty property = new MethodProperty(/* src */ null, /* deser */ null, /* nva */ null);
        assertNull(property.getNullProvider());
        assertNull(property.getMember());
    }

    @Test
    public void testConstructor3() {
        MethodProperty property = new MethodProperty(/* src */ null, /* newName */ null);
        assertNull(property.getNullProvider());
        assertNull(property.getMember());
    }

    @Test
    public void testConstructor4() {
        MethodProperty property = new MethodProperty(/* src */ null, /* m */ null);
        assertNull(property.getNullProvider());
        assertNull(property.getMember());
    }

    @Test
    public void testWithName() {
        MethodProperty src = new MethodProperty(/* src */ null, /* newName */ null);
        MethodProperty property = src.withName(/* newName */ null);
        assertNull(property.getNullProvider());
        assertNull(property.getMember());
    }

    @Test
    public void testWithValueDeserializer() {
        MethodProperty src = new MethodProperty(/* src */ null, /* deser */ null, /* nva */ null);
        MethodProperty property = src.withValueDeserializer(/* deser */ null);
        assertNull(property.getNullProvider());
        assertNull(property.getMember());
    }

    @Test
    public void testWithNullProvider() {
        MethodProperty src = new MethodProperty(/* src */ null, /* deser */ null, /* nva */ null);
        MethodProperty property = src.withNullProvider(/* nva */ null);
        assertNull(property.getNullProvider());
        assertNull(property.getMember());
    }

    @Test
    public void testFixAccess() {
        MethodProperty property = new MethodProperty(/* src */ null, /* m */ null);
        property.fixAccess(/* config */ null);
        assertNull(property.getNullProvider());
        assertNull(property.getMember());
    }

    @Test
    public void testGetAnnotation() {
        MethodProperty property = new MethodProperty(/* src */ null, /* deser */ null, /* nva */ null);
        assertNull(property.getAnnotation(/* acls */ null));
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        MethodProperty property = new MethodProperty(/* src */ null, /* deser */ null, /* nva */ null);
        property.deserializeAndSet(p, ctxt, instance);
        assertNull(property.getNullProvider());
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        MethodProperty property = new MethodProperty(/* src */ null, /* deser */ null, /* nva */ null);
        property.deserializeSetAndReturn(p, ctxt, instance);
        assertNull(property.getNullProvider());
        assertNull(property.getMember());
    }

    @Test
    public void testSet() {
        Object instance = null;
        Object value = null;
        MethodProperty property = new MethodProperty(/* src */ null, /* deser */ null, /* nva */ null);
        try {
            property.set(instance, value);
        } catch (Exception e) {
            assertNull(property.getNullProvider());
            assertNull(property.getMember());
        }
    }

    @Test
    public void testSetAndReturn() {
        Object instance = null;
        Object value = null;
        MethodProperty property = new MethodProperty(/* src */ null, /* deser */ null, /* nva */ null);
        try {
            property.setAndReturn(instance, value);
        } catch (Exception e) {
            assertNull(property.getNullProvider());
            assertNull(property.getMember());
        }
    }
}
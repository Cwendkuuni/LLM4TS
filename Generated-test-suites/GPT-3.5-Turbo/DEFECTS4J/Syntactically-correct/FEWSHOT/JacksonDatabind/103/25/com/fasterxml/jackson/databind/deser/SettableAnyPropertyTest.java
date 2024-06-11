package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SettableAnyPropertyTest {
    @Test
    public void testDeserializeAndSet() {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);
        assertEquals("value", property.deserializeAndSet(null, null, "instance", "propName"));
    }

    @Test
    public void testDeserialize() {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);
        assertNull(property.deserialize(null, null));
    }

    @Test
    public void testSet() throws Exception {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);
        property.set("instance", "propName", "value");
    }

    @Test
    public void testThrowAsIOE() throws Exception {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);
        property._throwAsIOE(new IllegalArgumentException("Error"), "propName", "value");
    }

    @Test
    public void testGetClassName() {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);
        assertEquals("com.fasterxml.jackson.databind.deser.SettableAnyProperty", property.getClassName());
    }

    @Test
    public void testAnySetterReferring() {
        SettableAnyProperty.AnySetterReferring referring = new SettableAnyProperty.AnySetterReferring(new SettableAnyProperty(null, null, null, null, null, null), null, Object.class, "instance", "propName");
        referring.handleResolvedForwardReference("id", "value");
    }
}
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.PascalCaseStrategy;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import org.junit.Before;
import org.junit.Test;

public class MethodPropertyTest {

    private SettableBeanProperty property1;
    private SettableBeanProperty property2;
    private SettableBeanProperty property3;
    private SettableBeanProperty property4;
    private SettableBeanProperty property5;

    @Before
    public void setUp() {
        property1 = new MethodProperty(null, null, null, null, null);
        property2 = new MethodProperty(null, null, null, null, null);
        property3 = new MethodProperty(null, null, null, null, null);
        property4 = new MethodProperty(null, null, null, null, null);
        property5 = new MethodProperty(null, null, null, null, null);
    }

    @Test
    public void testWithNullProvider() {
        PropertyName newName = new PropertyName("newName");
        SettableBeanProperty result1 = property1.withNullProvider(null);
        SettableBeanProperty result2 = property2.withNullProvider(null);
        SettableBeanProperty result3 = property3.withNullProvider(null);
        SettableBeanProperty result4 = property4.withNullProvider(null);
        SettableBeanProperty result5 = property5.withNullProvider(null);

        assertNull(result1.getNullProvider());
        assertNull(result2.getNullProvider());
        assertNull(result3.getNullProvider());
        assertNull(result4.getNullProvider());
        assertNull(result5.getNullProvider());
    }

    @Test
    public void testFixAccess() {
        property1.fixAccess(null);
        property2.fixAccess(null);
        property3.fixAccess(null);
        property4.fixAccess(null);
        property5.fixAccess(null);

        // Test that the method called with null parameter does not throw NullPointerException
    }

    @Test
    public void testGetMember() {
        assertNotNull(property1.getMember());
        assertNotNull(property2.getMember());
        assertNotNull(property3.getMember());
        assertNotNull(property4.getMember());
        assertNotNull(property5.getMember());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(property1.getAnnotation(null));
        assertNull(property2.getAnnotation(null));
        assertNull(property3.getAnnotation(null));
        assertNull(property4.getAnnotation(null));
        assertNull(property5.getAnnotation(null));
    }

    @Test
    public void testDeserializeAndSet() {
        // Create ObjectMapper for testing
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(new PascalCaseStrategy());

        // Create JsonParser for testing
        JsonParser p1 = null;
        JsonParser p2 = null;
        JsonParser p3 = null;
        JsonParser p4 = null;
        JsonParser p5 = null;

        // Create DeserializationContext for testing
        DeserializationContext ctxt1 = null;
        DeserializationContext ctxt2 = null;
        DeserializationContext ctxt3 = null;
        DeserializationContext ctxt4 = null;
        DeserializationContext ctxt5 = null;

        // Create Object instance for testing
        Object instance1 = null;
        Object instance2 = null;
        Object instance3 = null;
        Object instance4 = null;
        Object instance5 = null;

        // Perform the test
        assertNull(property1.deserializeAndSet(p1, ctxt1, instance1));
        assertNull(property2.deserializeAndSet(p2, ctxt2, instance2));
        assertNull(property3.deserializeAndSet(p3, ctxt3, instance3));
        assertNull(property4.deserializeAndSet(p4, ctxt4, instance4));
        assertNull(property5.deserializeAndSet(p5, ctxt5, instance5));
    }

    @Test
    public void testDeserializeSetAndReturn() {
        // Create ObjectMapper for testing
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(new PascalCaseStrategy());

        // Create JsonParser for testing
        JsonParser p1 = null;
        JsonParser p2 = null;
        JsonParser p3 = null;
        JsonParser p4 = null;
        JsonParser p5 = null;

        // Create DeserializationContext for testing
        DeserializationContext ctxt1 = null;
        DeserializationContext ctxt2 = null;
        DeserializationContext ctxt3 = null;
        DeserializationContext ctxt4 = null;
        DeserializationContext ctxt5 = null;

        // Create Object instance for testing
        Object instance1 = null;
        Object instance2 = null;
        Object instance3 = null;
        Object instance4 = null;
        Object instance5 = null;

        // Perform the test
        assertEquals(instance1, property1.deserializeSetAndReturn(p1, ctxt1, instance1));
        assertEquals(instance2, property2.deserializeSetAndReturn(p2, ctxt2, instance2));
        assertEquals(instance3, property3.deserializeSetAndReturn(p3, ctxt3, instance3));
        assertEquals(instance4, property4.deserializeSetAndReturn(p4, ctxt4, instance4));
        assertEquals(instance5, property5.deserializeSetAndReturn(p5, ctxt5, instance5));
    }

    @Test
    public void testSet() {
        // Create ObjectMapper for testing
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(new PascalCaseStrategy());

        // Create Object instance for testing
        Object instance1 = null;
        Object instance2 = null;
        Object instance3 = null;
        Object instance4 = null;
        Object instance5 = null;

        // Perform the test
        property1.set(instance1, instance1);
        property2.set(instance2, instance2);
        property3.set(instance3, instance3);
        property4.set(instance4, instance4);
        property5.set(instance5, instance5);
    }

    @Test
    public void testSetAndReturn() {
        // Create ObjectMapper for testing
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(new PascalCaseStrategy());

        // Create Object instance for testing
        Object instance1 = null;
        Object instance2 = null;
        Object instance3 = null;
        Object instance4 = null;
        Object instance5 = null;

        // Perform the test
        assertEquals(instance1, property1.setAndReturn(instance1, instance1));
        assertEquals(instance2, property2.setAndReturn(instance2, instance2));
        assertEquals(instance3, property3.setAndReturn(instance3, instance3));
        assertEquals(instance4, property4.setAndReturn(instance4, instance4));
        assertEquals(instance5, property5.setAndReturn(instance5, instance5));
    }
}
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testConstructor1() {
        ObjectIdReferenceProperty prop = new ObjectIdReferenceProperty(new SettableBeanProperty(), new ObjectIdInfo());
        assertNotNull(prop);
    }

    @Test
    public void testConstructor2() {
        ObjectIdReferenceProperty prop = new ObjectIdReferenceProperty(new ObjectIdReferenceProperty(), new JsonDeserializer<>(), new NullValueProvider());
        assertNotNull(prop);
    }

    @Test
    public void testConstructor3() {
        ObjectIdReferenceProperty prop = new ObjectIdReferenceProperty(new ObjectIdReferenceProperty(), new PropertyName());
        assertNotNull(prop);
    }

    @Test
    public void testWithName() {
        ObjectIdReferenceProperty prop = new ObjectIdReferenceProperty(new ObjectIdReferenceProperty(), new PropertyName());
        SettableBeanProperty newProp = prop.withName(new PropertyName());
        assertNotNull(newProp);
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReferenceProperty prop = new ObjectIdReferenceProperty(new ObjectIdReferenceProperty(), new JsonDeserializer<>(), new NullValueProvider());
        SettableBeanProperty newProp = prop.withValueDeserializer(new JsonDeserializer<>());
        assertNotNull(newProp);
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReferenceProperty prop = new ObjectIdReferenceProperty(new ObjectIdReferenceProperty(), new JsonDeserializer<>(), new NullValueProvider());
        SettableBeanProperty newProp = prop.withNullProvider(new NullValueProvider());
        assertNotNull(newProp);
    }

    @Test
    public void testFixAccess() {
        ObjectIdReferenceProperty prop = new ObjectIdReferenceProperty(new ObjectIdReferenceProperty(), new ObjectIdInfo());
        prop.fixAccess(new DeserializationConfig());
        assertNotNull(prop);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReferenceProperty prop = new ObjectIdReferenceProperty(new ObjectIdReferenceProperty(), new ObjectIdInfo());
        Annotation ann = prop.getAnnotation(Annotation.class);
        assertNull(ann);
    }

    @Test
    public void testGetMember() {
        ObjectIdReferenceProperty prop = new ObjectIdReferenceProperty(new ObjectIdReferenceProperty(), new ObjectIdInfo());
        AnnotatedMember member = prop.getMember();
        assertNotNull(member);
    }

    @Test
    public void testGetCreatorIndex() {
        ObjectIdReferenceProperty prop = new ObjectIdReferenceProperty(new ObjectIdReferenceProperty(), new ObjectIdInfo());
        int index = prop.getCreatorIndex();
        assertEquals(0, index);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReferenceProperty prop = new ObjectIdReferenceProperty(new ObjectIdReferenceProperty(), new ObjectIdInfo());
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        prop.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReferenceProperty prop = new ObjectIdReferenceProperty(new ObjectIdReferenceProperty(), new ObjectIdInfo());
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        Object result = prop.deserializeSetAndReturn(p, ctxt, instance);
        assertNull(result);
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdReferenceProperty prop = new ObjectIdReferenceProperty(new ObjectIdReferenceProperty(), new ObjectIdInfo());
        Object instance = new Object();
        Object value = new Object();
        
        prop.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReferenceProperty prop = new ObjectIdReferenceProperty(new ObjectIdReferenceProperty(), new ObjectIdInfo());
        Object instance = new Object();
        Object value = new Object();
        
        Object result = prop.setAndReturn(instance, value);
        assertNotNull(result);
    }

    @Test
    public void testPropertyReferring() {
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(new ObjectIdReferenceProperty(), new UnresolvedForwardReference(), new Class<>(), new Object());
        assertNotNull(referring);
    }

    @Test
    public void testHandleResolvedForwardReference() throws IOException {
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(new ObjectIdReferenceProperty(), new UnresolvedForwardReference(), new Class<>(), new Object());
        referring.handleResolvedForwardReference(new Object(), new Object());
    }
}
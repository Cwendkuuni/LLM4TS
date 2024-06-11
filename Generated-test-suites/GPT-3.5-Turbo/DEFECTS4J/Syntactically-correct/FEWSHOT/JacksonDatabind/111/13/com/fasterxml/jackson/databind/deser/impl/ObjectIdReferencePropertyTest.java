package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReferenceProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdReferencePropertyTest {
    private ObjectIdReferenceProperty property;

    @Before
    public void setUp() {
        JsonDeserializer<?> deser = null;
        NullValueProvider nva = null;
        ObjectIdInfo objectIdInfo = null;
        
        property = new ObjectIdReferenceProperty(null, objectIdInfo);
        property = new ObjectIdReferenceProperty(property, deser, nva);
        property = new ObjectIdReferenceProperty(property, new PropertyName("newName"));
        property = (ObjectIdReferenceProperty) property.withName(new PropertyName("newName"));
        property = (ObjectIdReferenceProperty) property.withValueDeserializer(deser);
        property = (ObjectIdReferenceProperty) property.withNullProvider(nva);
        property.fixAccess(null);
    }

    @Test
    public void testGetAnnotation() {
        Class<Override> acls = Override.class;
        AnnotatedMember member = null;
        assertNull(property.getAnnotation(acls));
    }

    @Test
    public void testGetMember() {
        AnnotatedMember member = null;
        assertSame(member, property.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        assertEquals(0, property.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        try {
            property.deserializeAndSet(p, ctxt, instance);
            fail("Expected UnresolvedForwardReference");
        } catch (UnresolvedForwardReference e) {
            assertTrue(true);
        }
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        try {
            property.deserializeSetAndReturn(p, ctxt, instance);
            fail("Expected UnresolvedForwardReference");
        } catch (UnresolvedForwardReference e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSet() throws IOException {
        Object instance = null;
        Object value = null;
        property.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = null;
        Object value = null;
        assertSame(instance, property.setAndReturn(instance, value));
    }

    @Test
    public void testPropertyReferring() throws IOException {
        ObjectIdReferenceProperty.PropertyReferring referring = property.new PropertyReferring(property, null, Object.class, null);
        referring.handleResolvedForwardReference(null, null);
        referring.resolveId(null, null);
        assertNotNull(referring.toString());
        assertNull(referring.getTypeId());
        assertNull(referring.getUnresolvedId());
        referring.getWantedType();
    }
}
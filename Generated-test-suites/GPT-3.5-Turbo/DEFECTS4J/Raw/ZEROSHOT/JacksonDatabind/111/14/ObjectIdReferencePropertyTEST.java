package com.fasterxml.jackson.databind.deser.impl;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testWithNullProvider() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        NullValueProvider nva = null;
        ObjectIdReferenceProperty result = property.withNullProvider(nva);

        assertNull(result.getValueDeserializer());
        assertEquals(nva, result.getNullValueProvider());
    }

    @Test
    public void testWithNullProvider_WhenNvaIsNotNull() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        NullValueProvider nva = new NullValueProvider();
        ObjectIdReferenceProperty result = property.withNullProvider(nva);

        assertNull(result.getValueDeserializer());
        assertEquals(nva, result.getNullValueProvider());
    }

    @Test
    public void testFixAccess() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        DeserializationConfig config = new DeserializationConfig();
        property.fixAccess(config);

        // no assertion logic specified
    }

    @Test
    public void testGetAnnotation() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        Class<Annotation> acls = Annotation.class;
        Annotation result = property.getAnnotation(acls);

        assertNull(result);
    }

    @Test
    public void testGetMember() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        AnnotatedMember result = property.getMember();

        assertNull(result);
    }

    @Test
    public void testGetCreatorIndex() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        int result = property.getCreatorIndex();

        assertEquals(0, result);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        property.deserializeAndSet(p, ctxt, instance);

        // no assertion logic specified
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        Object result = property.deserializeSetAndReturn(p, ctxt, instance);

        assertNull(result);
    }

    @Test
    public void testSet() throws IOException {
        SettableBeanProperty forward = new SettableBeanProperty();
        forward.set(new Object(), new Object());
    }

    @Test
    public void testSetAndReturn() throws IOException {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        Object instance = new Object();
        Object value = new Object();
        Object result = property.setAndReturn(instance, value);

        assertNull(result);
    }

    @Test
    public void testPropertyReferring_WhenHasId() throws IOException {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        UnresolvedForwardReference ref = new UnresolvedForwardReference();
        Class<?> type = Object.class;
        Object ob = new Object();
        ObjectIdReferenceProperty.PropertyReferring referring = property.new PropertyReferring(property, ref, type, ob);

        Object id = new Object();
        Object value = new Object();
        referring.handleResolvedForwardReference(id, value);

        // no assertion logic specified
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPropertyReferring_WhenHasNoId() throws IOException {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        UnresolvedForwardReference ref = new UnresolvedForwardReference();
        Class<?> type = Object.class;
        Object ob = new Object();
        ObjectIdReferenceProperty.PropertyReferring referring = property.new PropertyReferring(property, ref, type, ob);

        Object id = new Object();
        Object value = new Object();
        referring.handleResolvedForwardReference(id, value);
    }
}
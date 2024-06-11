package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdReferencePropertyTest {

    private ObjectIdReferenceProperty property;

    @Before
    public void setUp() {
        SettableBeanProperty forward = new SettableBeanProperty() {
            @Override
            public void fixAccess(DeserializationConfig config) {

            }

            @Override
            public Annotation getAnnotation(Class<A> acls) {
                return null;
            }

            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public int getCreatorIndex() {
                return 0;
            }

            @Override
            public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {

            }

            @Override
            public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
                return null;
            }

            @Override
            public void set(Object instance, Object value) throws IOException {

            }

            @Override
            public Object setAndReturn(Object instance, Object value) throws IOException {
                return null;
            }
        };
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        property = new ObjectIdReferenceProperty(forward, objectIdInfo);
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newName");
        SettableBeanProperty result = property.withName(newName);
        assertEquals(newName, result.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = null;
        SettableBeanProperty result = property.withValueDeserializer(deser);
        assertEquals(deser, result.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = null;
        SettableBeanProperty result = property.withNullProvider(nva);
        assertEquals(nva, result.getNullProvider());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = null;
        // No assertion as the method does not return anything
        property.fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        Class<A> acls = null;
        Annotation result = property.getAnnotation(acls);
        assertNull(result);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember result = property.getMember();
        assertNull(result);
    }

    @Test
    public void testGetCreatorIndex() {
        int result = property.getCreatorIndex();
        assertEquals(0, result);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        // No assertion as the method does not return anything
        property.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        Object result = property.deserializeSetAndReturn(p, ctxt, instance);
        assertNull(result);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = null;
        Object value = null;
        // No assertion as the method does not return anything
        property.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = null;
        Object value = null;
        Object result = property.setAndReturn(instance, value);
        assertNull(result);
    }

    @Test
    public void testPropertyReferringHandleResolvedForwardReference() throws IOException {
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = property.new PropertyReferring(property, null, null, null);
        Object id = null;
        Object value = null;
        // No assertion as the method does not return anything
        propertyReferring.handleResolvedForwardReference(id, value);
    }
}
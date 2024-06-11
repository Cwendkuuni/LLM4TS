package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ObjectIdReferencePropertyTest {

    private SettableBeanProperty forward;
    private ObjectIdInfo objectIdInfo;

    @Before
    public void setUp() {
        forward = new SettableBeanProperty() {
            @Override
            public SettableBeanProperty withName(PropertyName newName) {
                return null;
            }

            @Override
            public SettableBeanProperty withValueDeserializer(JsonDeserializer<?> deser) {
                return null;
            }

            @Override
            public SettableBeanProperty withNullProvider(NullValueProvider nva) {
                return null;
            }

            @Override
            public void fixAccess(DeserializationConfig config) {

            }

            @Override
            public <A extends Annotation> A getAnnotation(Class<A> acls) {
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
        objectIdInfo = new ObjectIdInfo(null, null, null, null);
    }

    @Test
    public void testWithNullPropertyName() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty newProperty = property.withName(null);
        assertNull(newProperty.getName());
    }

    @Test
    public void testWithNonNullPropertyName() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty newProperty = property.withName(new PropertyName("test"));
        assertEquals("test", newProperty.getName().getSimpleName());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertNull(property.withValueDeserializer(null));
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertNull(property.withNullProvider(null));
    }

    @Test
    public void testFixAccess() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.fixAccess(null);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertNull(property.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertNull(property.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertEquals(0, property.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.deserializeAndSet(null, null, null);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertNull(property.deserializeSetAndReturn(null, null, null));
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.set(null, null);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertNull(property.setAndReturn(null, null));
    }

    @Test
    public void testHandleResolvedForwardReference() throws IOException {
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(null, null, null, null);
        try {
            referring.handleResolvedForwardReference(null, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Trying to resolve a forward reference with id [null] that wasn't previously seen as unresolved.", e.getMessage());
        }
    }
}
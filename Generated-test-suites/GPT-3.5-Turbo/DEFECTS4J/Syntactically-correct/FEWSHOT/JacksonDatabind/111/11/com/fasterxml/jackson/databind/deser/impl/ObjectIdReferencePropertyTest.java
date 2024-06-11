package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdReferencePropertyTest {

    private ObjectIdReferenceProperty property;
    private SettableBeanProperty forward;
    private ObjectIdInfo objectIdInfo;

    @Before
    public void setUp() {
        forward = new SettableBeanProperty();
        objectIdInfo = new ObjectIdInfo();
        property = new ObjectIdReferenceProperty(forward, objectIdInfo);
    }

    @Test
    public void testConstructor() {
        assertSame(forward, property._forward);
        assertSame(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testCopyConstructor() {
        JsonDeserializer<?> deser = new JsonDeserializer<?>();
        NullValueProvider nva = new NullValueProvider();
        property = new ObjectIdReferenceProperty(property, deser, nva);

        assertSame(forward, property._forward);
        assertSame(objectIdInfo, property._objectIdInfo);
        assertSame(deser, property._valueDeserializer);
        assertSame(nva, property._nullProvider);
    }

    @Test
    public void testCopyConstructorWithName() {
        PropertyName newName = new PropertyName("newName");
        property = new ObjectIdReferenceProperty(property, newName);

        assertSame(forward, property._forward);
        assertSame(objectIdInfo, property._objectIdInfo);
        assertEquals(newName, property._propName);
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newName");
        SettableBeanProperty result = property.withName(newName);

        assertSame(forward, result._forward);
        assertSame(objectIdInfo, result._objectIdInfo);
        assertEquals(newName, result._propName);
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = new JsonDeserializer<?>();
        property = property.withValueDeserializer(deser);

        assertSame(forward, property._forward);
        assertSame(objectIdInfo, property._objectIdInfo);
        assertSame(deser, property._valueDeserializer);
        assertNull(property._nullProvider);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = new NullValueProvider();
        property = property.withNullProvider(nva);

        assertSame(forward, property._forward);
        assertSame(objectIdInfo, property._objectIdInfo);
        assertNull(property._valueDeserializer);
        assertSame(nva, property._nullProvider);
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = new DeserializationConfig();
        property.fixAccess(config);

        assertSame(forward, property._forward);
        assertSame(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testGetAnnotation() {
        Class<A> acls = A.class;
        A result = property.getAnnotation(acls);

        assertSame(forward, property._forward);
        assertSame(objectIdInfo, property._objectIdInfo);
        assertSame(result, forward.getAnnotation(acls));
    }

    @Test
    public void testGetMember() {
        AnnotatedMember result = property.getMember();

        assertSame(forward, property._forward);
        assertSame(objectIdInfo, property._objectIdInfo);
        assertSame(result, forward.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        int result = property.getCreatorIndex();

        assertSame(forward, property._forward);
        assertSame(objectIdInfo, property._objectIdInfo);
        assertEquals(result, forward.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        property.deserializeAndSet(p, ctxt, instance);

        assertSame(forward, property._forward);
        assertSame(objectIdInfo, property._objectIdInfo);
        assertNull(property._valueDeserializer);
        assertNull(property._nullProvider);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        Object result = property.deserializeSetAndReturn(p, ctxt, instance);

        assertSame(forward, property._forward);
        assertSame(objectIdInfo, property._objectIdInfo);
        assertNull(property._valueDeserializer);
        assertNull(property._nullProvider);
        assertNull(result);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        property.set(instance, value);

        assertSame(forward, property._forward);
        assertSame(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        Object result = property.setAndReturn(instance, value);

        assertSame(forward, property._forward);
        assertSame(objectIdInfo, property._objectIdInfo);
        assertSame(result, forward.setAndReturn(instance, value));
    }

    @Test
    public void testPropertyReferringConstructor() {
        UnresolvedForwardReference ref = new UnresolvedForwardReference();
        Class<?> type = Object.class;
        Object ob = new Object();
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(property, ref, type, ob);

        assertSame(property, referring._parent);
        assertSame(ob, referring._pojo);
        assertSame(ref, referring._reference);
        assertSame(type, referring._beanType);
    }

    @Test
    public void testHandleResolvedForwardReference() throws IOException {
        Object id = new Object();
        Object value = new Object();
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(property, null, null, null);
        referring.handleResolvedForwardReference(id, value);

        assertSame(property, referring._parent);
        assertSame(value, referring._pojo);
    }
}
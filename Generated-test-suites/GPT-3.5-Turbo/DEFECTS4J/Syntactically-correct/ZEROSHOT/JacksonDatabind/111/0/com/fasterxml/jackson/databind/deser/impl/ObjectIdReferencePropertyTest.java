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

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class ObjectIdReferencePropertyTest {

    private ObjectIdReferenceProperty _objectIdReferenceProperty;
    private SettableBeanProperty _forward;
    private ObjectIdInfo _objectIdInfo;

    @Before
    public void setUp() {
        _forward = new SettableBeanProperty();
        _objectIdInfo = new ObjectIdInfo();
        _objectIdReferenceProperty = new ObjectIdReferenceProperty(_forward, _objectIdInfo);
    }

    @Test
    public void testConstructor1() {
        Assert.assertEquals(1L, _objectIdReferenceProperty.serialVersionUID);
        Assert.assertEquals(_forward, _objectIdReferenceProperty._forward);
        Assert.assertEquals(_objectIdInfo, _objectIdReferenceProperty._objectIdInfo);
    }

    @Test
    public void testConstructor2() {
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty(_objectIdReferenceProperty, deser, nva);
        Assert.assertEquals(_forward, src._forward);
        Assert.assertEquals(_objectIdInfo, src._objectIdInfo);
        Assert.assertEquals(deser, src._valueDeserializer);
        Assert.assertEquals(nva, src._nullProvider);
    }

    @Test
    public void testConstructor3() {
        PropertyName newName = new PropertyName();
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty(_objectIdReferenceProperty, newName);
        Assert.assertEquals(_forward, src._forward);
        Assert.assertEquals(_objectIdInfo, src._objectIdInfo);
        Assert.assertEquals(newName, src._name);
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName();
        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) _objectIdReferenceProperty.withName(newName);
        Assert.assertEquals(newName, newProperty._name);
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) _objectIdReferenceProperty.withValueDeserializer(deser);
        Assert.assertEquals(deser, newProperty._valueDeserializer);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = new NullValueProvider();
        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) _objectIdReferenceProperty.withNullProvider(nva);
        Assert.assertEquals(nva, newProperty._nullProvider);
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = new DeserializationConfig();
        _forward.fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        Class<A> acls = A.class;
        _forward.getAnnotation(acls);
    }

    @Test
    public void testGetMember() {
        _forward.getMember();
    }

    @Test
    public void testGetCreatorIndex() {
        _forward.getCreatorIndex();
    }

    @Test
    public void testDeserializeAndSet() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            _objectIdReferenceProperty.deserializeAndSet(p, ctxt, instance);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            _objectIdReferenceProperty.deserializeSetAndReturn(p, ctxt, instance);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSet() {
        Object instance = new Object();
        Object value = new Object();
        try {
            _objectIdReferenceProperty.set(instance, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSetAndReturn() {
        Object instance = new Object();
        Object value = new Object();
        try {
            _objectIdReferenceProperty.setAndReturn(instance, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPropertyReferring() {
        UnresolvedForwardReference ref = new UnresolvedForwardReference();
        Class<?> type = Object.class;
        Object ob = new Object();
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(_objectIdReferenceProperty, ref, type, ob);
        Assert.assertEquals(_objectIdReferenceProperty, propertyReferring._parent);
        Assert.assertEquals(ob, propertyReferring._pojo);
    }

    @Test
    public void testHandleResolvedForwardReference() {
        Object id = new Object();
        Object value = new Object();
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(_objectIdReferenceProperty, ref, type, ob);
        try {
            propertyReferring.handleResolvedForwardReference(id, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
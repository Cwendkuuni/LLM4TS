package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testObjectIdReferencePropertyConstructor() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyConstructorWithDeserializer() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, deser, nva);

        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(deser, newProperty._valueDeserializer);
        assertEquals(nva, newProperty._nullProvider);
    }

    @Test
    public void testObjectIdPropertyConstructorWithNewName() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        PropertyName newName = new PropertyName("");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, newName);

        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(newName, newProperty._name);
    }

    @Test
    public void testObjectIdPropertyWithName() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        PropertyName newName = new PropertyName("");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) property.withName(newName);

        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(newName, newProperty._name);
    }

    @Test
    public void testObjectIdPropertyWithValueDeserializer() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) property
                .withValueDeserializer(deser);

        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(deser, newProperty._valueDeserializer);
    }

    @Test
    public void testObjectIdPropertyWithNullProvider() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        NullValueProvider nva = new NullValueProvider();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) property
                .withNullProvider(nva);

        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(nva, newProperty._nullProvider);
    }

    @Test
    public void testObjectIdPropertyFixAccess() {
        SettableBeanProperty forward = new SettableBeanProperty();
        forward.fixAccess(null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        property.fixAccess(null);
    }

    @Test
    public void testObjectIdPropertyGetAnnotation() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        assertNull(property.getAnnotation(null));
    }

    @Test
    public void testObjectIdPropertyGetMember() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        assertEquals(forward.getMember(), property.getMember());
    }

    @Test
    public void testObjectIdPropertyGetCreatorIndex() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        assertEquals(forward.getCreatorIndex(), property.getCreatorIndex());
    }

    @Test
    public void testObjectIdPropertyDeserializeAndSet() throws IOException {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        JsonParser p = new ObjectMapper().getFactory().createParser("test");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Object instance = new Object();

        assertNull(property.deserializeAndSet(p, ctxt, instance));
    }

    @Test
    public void testObjectIdPropertyDeserializeSetAndReturn() throws IOException {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        JsonParser p = new ObjectMapper().getFactory().createParser("test");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Object instance = new Object();

        assertNull(property.deserializeSetAndReturn(p, ctxt, instance));
    }

    @Test
    public void testObjectIdPropertySet() throws IOException {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        Object instance = new Object();
        Object value = new Object();

        property.set(instance, value);
    }

    @Test
    public void testObjectIdPropertySetAndReturn() throws IOException {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        Object instance = new Object();
        Object value = new Object();

        assertEquals(value, property.setAndReturn(instance, value));
    }

    @Test
    public void testPropertyReferringConstructor() {
        ObjectIdReferenceProperty parent = new ObjectIdReferenceProperty();
        UnresolvedForwardReference ref = new UnresolvedForwardReference();
        Class<?> type = Object.class;
        Object ob = new Object();
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(
                parent, ref, type, ob);

        assertEquals(parent, propertyReferring._parent);
        assertEquals(ob, propertyReferring._pojo);
        assertEquals(ref, propertyReferring._roid);
        
    }

    @Test
    public void testPropertyReferringHandleResolvedForwardReference() throws IOException {
        ObjectIdReferenceProperty parent = new ObjectIdReferenceProperty();
        UnresolvedForwardReference ref = new UnresolvedForwardReference();
        Class<?> type = Object.class;
        Object ob = new Object();
        Object id = new Object();
        Object value = new Object();
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(
                parent, ref, type, ob);

        propertyReferring.handleResolvedForwardReference(id, value);
    }
}
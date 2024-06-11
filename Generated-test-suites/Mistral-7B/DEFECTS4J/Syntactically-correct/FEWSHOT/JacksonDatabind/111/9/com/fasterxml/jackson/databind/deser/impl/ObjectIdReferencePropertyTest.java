package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testObjectIdReferencePropertyConstruction() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, objectIdInfo);
        assertNull(property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyCopyConstruction() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        SettableBeanProperty forward = new SimpleBeanProperty("test");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty copy = new ObjectIdReferenceProperty(property, null, null);
        assertEquals(property, copy);
        assertEquals(forward, copy._forward);
        assertEquals(objectIdInfo, copy._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyNameChange() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        SettableBeanProperty forward = new SimpleBeanProperty("test");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        PropertyName newName = PropertyName.construct("newTest");
        ObjectIdReferenceProperty nameChanged = new ObjectIdReferenceProperty(property, newName);
        assertEquals(newName, nameChanged.getName());
        assertEquals(property, nameChanged._forward);
        assertEquals(objectIdInfo, nameChanged._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyValueDeserializerChange() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        SettableBeanProperty forward = new SimpleBeanProperty("test");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        JsonDeserializer<?> deserializer = new SimpleDeserializer<>();
        ObjectIdReferenceProperty deserializerChanged = new ObjectIdReferenceProperty(property, deserializer, null);
        assertEquals(property, deserializerChanged._forward);
        assertEquals(deserializer, deserializerChanged._valueDeserializer);
        assertEquals(objectIdInfo, deserializerChanged._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyNullProviderChange() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        SettableBeanProperty forward = new SimpleBeanProperty("test");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        NullValueProvider nva = new SimpleNullValueProvider();
        ObjectIdReferenceProperty nullProviderChanged = new ObjectIdReferenceProperty(property, null, nva);
        assertEquals(property, nullProviderChanged._forward);
        assertNull(nullProviderChanged._nullProvider);
        assertEquals(objectIdInfo, nullProviderChanged._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyFixAccess() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        SettableBeanProperty forward = new SimpleBeanProperty("test");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        DeserializationConfig config = new DeserializationConfig();
        property.fixAccess(config);
        assertNull(property._forward);
    }

    @Test
    public void testObjectIdReferencePropertyGetAnnotation() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        SettableBeanProperty forward = new SimpleBeanProperty("test");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Annotation annotation = new SimpleAnnotation();
        assertEquals(annotation, property.getAnnotation(Annotation.class));
    }

    @Test
    public void testObjectIdReferencePropertyGetMember() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        SettableBeanProperty forward = new SimpleBeanProperty("test");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        AnnotatedMember member = new SimpleAnnotatedMember();
        assertEquals(member, property.getMember());
    }

    @Test
    public void testObjectIdReferencePropertyGetCreatorIndex() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        SettableBeanProperty forward = new SimpleBeanProperty("test");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertEquals(-1, property.getCreatorIndex());
    }

    @Test
    public void testObjectIdReferencePropertyDeserializeAndSet() throws IOException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        SettableBeanProperty forward = new SimpleBeanProperty("test");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectNode node = new ObjectMapper().createObjectNode();
        JsonParser parser = node.asParser();
        Object instance = new Object();
        property.deserializeAndSet(parser, new DeserializationContext(), instance);
        assertEquals(forward.getValue(), instance.getClass().getDeclaredField("test").get(instance));
    }

    @Test
    public void testObjectIdReferencePropertyDeserializeSetAndReturn() throws IOException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        SettableBeanProperty forward = new SimpleBeanProperty("test");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectNode node = new ObjectMapper().createObjectNode();
        Object instance = new Object();
        Object value = new Object();
        Object result = property.deserializeSetAndReturn(node.asParser(), new DeserializationContext(), instance);
        assertEquals(value, result);
        assertEquals(forward.getValue(), instance.getClass().getDeclaredField("test").get(instance));
    }

    @Test
    public void testObjectIdReferencePropertySet() throws IOException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        SettableBeanProperty forward = new SimpleBeanProperty("test");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object instance = new Object();
        Object value = new Object();
        property.set(instance, value);
        assertEquals(value, instance.getClass().getDeclaredField("test").get(instance));
    }

    @Test
    public void testObjectIdReferencePropertySetAndReturn() throws IOException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        SettableBeanProperty forward = new SimpleBeanProperty("test");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object instance = new Object();
        Object value = new Object();
        Object result = property.setAndReturn(instance, value);
        assertEquals(value, result);
        assertEquals(value, instance.getClass().getDeclaredField("test").get(instance));
    }
}
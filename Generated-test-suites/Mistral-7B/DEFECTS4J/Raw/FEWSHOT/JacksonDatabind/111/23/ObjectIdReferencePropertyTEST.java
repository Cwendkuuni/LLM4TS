package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generate(Object.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(null, objectIdInfo);
        assertNull(objectIdReferenceProperty.getForward());
        assertNotNull(objectIdReferenceProperty.getObjectIdInfo());
    }

    @Test
    public void testObjectIdReferencePropertyWithForward() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generate(Object.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertEquals(forward, objectIdReferenceProperty.getForward());
        assertNotNull(objectIdReferenceProperty.getObjectIdInfo());
    }

    @Test
    public void testObjectIdReferencePropertyWithDeserializer() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generate(Object.class);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(null, deserializer, objectIdInfo);
        assertNull(objectIdReferenceProperty.getForward());
        assertNotNull(objectIdReferenceProperty.getObjectIdInfo());
        assertEquals(deserializer, objectIdReferenceProperty.getValueDeserializer());
    }

    @Test
    public void testObjectIdReferencePropertyWithName() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generate(Object.class);
        PropertyName newName = PropertyName.construct("testProperty");
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(null, newName, objectIdInfo);
        assertEquals(newName, objectIdReferenceProperty.getName());
        assertNotNull(objectIdReferenceProperty.getObjectIdInfo());
    }

    @Test
    public void testObjectIdReferencePropertyFixAccess() throws IOException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generate(Object.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(null, objectIdInfo);
        Object obj = new Object();
        objectIdReferenceProperty.fixAccess(null);
        objectIdReferenceProperty.set(obj, new Object());
        Object expected = new Object();
        assertEquals(expected, obj);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generate(Object.class);
        JsonIdentityInfo jsonIdentityInfo = new JsonIdentityInfo(ObjectIdGenerators.IntArrayIdGenerator.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(null, objectIdInfo);
        assertNull(objectIdReferenceProperty.getAnnotation(JsonIdentityInfo.class));
        objectIdReferenceProperty.setObjectIdInfo(jsonIdentityInfo);
        assertEquals(jsonIdentityInfo, objectIdReferenceProperty.getAnnotation(JsonIdentityInfo.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generate(Object.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(null, objectIdInfo);
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(objectIdReferenceProperty.getMember()).thenReturn(member);
        assertEquals(member, objectIdReferenceProperty.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(null, null);
        assertEquals(-1, objectIdReferenceProperty.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generate(Object.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(null, objectIdInfo);
        when(parser.getValueAsToken()).thenReturn(ContextualTests.contextualObject("testValue"));

        objectIdReferenceProperty.deserializeAndSet(parser, ctxt, instance);
        Object expected = new Object();
        assertEquals(expected, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generate(Object.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(null, objectIdInfo);
        when(parser.getValueAsToken()).thenReturn(ContextualTests.contextualObject("testValue"));

        Object result = objectIdReferenceProperty.deserializeSetAndReturn(parser, ctxt, instance);
        Object expected = new Object();
        assertEquals(expected, result);
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generate(Object.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(null, objectIdInfo);
        Object instance = new Object();
        Object value = new Object();

        objectIdReferenceProperty.set(instance, value);
        Object expected = value;
        assertEquals(expected, instance);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generate(Object.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(null, objectIdInfo);
        Object instance = new Object();
        Object value = new Object();

        Object result = objectIdReferenceProperty.setAndReturn(instance, value);
        Object expected = value;
        assertEquals(expected, result);
    }

    @Test
    public void testPropertyReferring() throws IOException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generate(Object.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(null, objectIdInfo);
        Object pojo = new Object();
        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
        Class<?> type = Object.class;

        PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, reference, type, pojo);

        Object id = ContextualTests.contextualObject("testId");
        Object value = ContextualTests.contextualObject("testValue");

        propertyReferring.handleResolvedForwardReference(id, value);

        Object expected = value;
        assertEquals(expected, pojo);
    }
}
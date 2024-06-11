package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectIdReferencePropertyTest {

    ObjectIdReferenceProperty objIdReferenceProperty;
    SettableBeanProperty forward;
    ObjectIdInfo objectIdInfo;
    JsonDeserializer<?> jsonDeserializer;
    NullValueProvider nullValueProvider;
    Object pojo;
    Object referencedObject;

    @org.junit.Before
    public void setup() throws InstantiationException, IllegalAccessException {
        forward = mock(SettableBeanProperty.class);
        objectIdInfo = mock(ObjectIdInfo.class);
        jsonDeserializer = mock(JsonDeserializer.class);
        nullValueProvider = mock(NullValueProvider.class);
        pojo = mock(Object.class);
        objIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    }

    @org.junit.Test
    public void testConstructorWithForward() {
        ObjectIdReferenceProperty testObject = new ObjectIdReferenceProperty(objIdReferenceProperty, jsonDeserializer, nullValueProvider);
        assertSame(objIdReferenceProperty, testObject._forward);
    }

    @org.junit.Test
    public void testConstructorWithName() {
        PropertyName newName = mock(PropertyName.class);
        ObjectIdReferenceProperty testObject = new ObjectIdReferenceProperty(objIdReferenceProperty, newName);
        assertSame(objIdReferenceProperty, testObject);
    }

    @org.junit.Test
    public void testConstructWithSourceAndName() {
        ObjectIdReferenceProperty src = spy(objIdReferenceProperty);
        PropertyName newName = mock(PropertyName.class);
        ObjectIdReferenceProperty testObject = new ObjectIdReferenceProperty(src, newName);
        assertSame(src, testObject);
    }

    @org.junit.Test
    public void testConstructorWithSourceAndDeserializer() {
        ObjectIdReferenceProperty src = spy(objIdReferenceProperty);
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        ObjectIdReferenceProperty testObject = new ObjectIdReferenceProperty(src, deserializer, nullValueProvider);
        assertSame(src, testObject);
    }

    @org.junit.Test
    public void testConstructorWithSourceAndNullProvider() {
        ObjectIdReferenceProperty src = spy(objIdReferenceProperty);
        NullValueProvider nva = mock(NullValueProvider.class);
        ObjectIdReferenceProperty testObject = new ObjectIdReferenceProperty(src, jsonDeserializer, nva);
        assertSame(src, testObject);
    }

    @org.junit.Test
    public void testGetAnnotation() {
        Annotation annotation = mock(Annotation.class);
        when(forward.getAnnotation(JsonDeserializer.class)).thenReturn(annotation);
        assertSame(annotation, objIdReferenceProperty.getAnnotation(JsonDeserializer.class));
    }

    @org.junit.Test
    public void testGetMember() {
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        when(forward.getMember()).thenReturn(annotatedMember);
        assertSame(annotatedMember, objIdReferenceProperty.getMember());
    }

    @org.junit.Test
    public void testGetCreatorIndex() {
        int creatorIndex = 1;
        when(forward.getCreatorIndex()).thenReturn(creatorIndex);
        assertEquals(creatorIndex, objIdReferenceProperty.getCreatorIndex());
    }

    @org.junit.Test
    public void testDeserializeAndSet() throws IOException {
        ObjectNode node = mock(ObjectNode.class);
        Object deserializedObject = mock(Object.class);
        when(jsonDeserializer.deserialize(eq(node), any(DeserializationContext.class))).thenReturn(deserializedObject);
        when(forward.set(eq(pojo), anyObject())).thenAnswer(invocationOnMock -> deserializedObject);
        objIdReferenceProperty.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), pojo);
        verify(jsonDeserializer).deserialize(eq(node), any(DeserializationContext.class));
        verify(forward).set(eq(pojo), anyObject());
    }

    @org.junit.Test
    public void testSet() throws IOException {
        Object value = mock(Object.class);
        objIdReferenceProperty.set(pojo, value);
        verify(forward).set(eq(pojo), anyObject());
    }

    @org.junit.Test
    public void testSetAndReturn() throws IOException {
        Object value = mock(Object.class);
        Object returnedValue = mock(Object.class);
        when(forward.setAndReturn(eq(pojo), anyObject())).thenReturn(returnedValue);
        assertSame(returnedValue, objIdReferenceProperty.setAndReturn(pojo, value));
    }

    @org.junit.Test
    public void testFixAccess() {
        objIdReferenceProperty.fixAccess(mock(DeserializationConfig.class));
        if (forward != null) {
            forward.fixAccess(mock(DeserializationConfig.class));
        }
    }

    @org.junit.Test
    public void testDeserializeSetAndReturnWithUnresolvedForwardReference() throws IOException {
        ObjectIdReferenceProperty refProp = mock(ObjectIdReferenceProperty.class);
        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
        Class<?> type = mock(Class.class);
        Object ob = mock(Object.class);
        Object id = mock(Object.class);
        Object value = mock(Object.class);
        when(reference.getId()).thenReturn(id);
        when(reference.getValue()).thenReturn(value);
        when(objIdReferenceProperty._objectIdInfo).thenReturn(objectIdInfo);
        when(objectIdInfo.getObjectDeserializer()).thenReturn(jsonDeserializer);
        when(jsonDeserializer.getObjectIdReader()).thenReturn(mock(ObjectIdReader.class));
        when(refProp.getObjectIdInfo()).thenReturn(objectIdInfo);
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(objIdReferenceProperty, reference, type, ob);
        objIdReferenceProperty.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), pojo);
        verify(reference).getRoid().appendReferring(propertyReferring);
        assertNull(objIdReferenceProperty.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), pojo));
    }
}
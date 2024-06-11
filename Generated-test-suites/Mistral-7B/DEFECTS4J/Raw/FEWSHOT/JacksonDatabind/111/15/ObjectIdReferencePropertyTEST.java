package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectIdNode;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testObjectIdReferencePropertyCreation() {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertNotNull(property);
        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyCopyConstruction() {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty(forward, objectIdInfo);

        ObjectIdReferenceProperty dest = new ObjectIdReferenceProperty(src, null);
        assertEquals(src, dest);
        assertEquals(forward, dest._forward);
        assertEquals(objectIdInfo, dest._objectIdInfo);

        dest = new ObjectIdReferenceProperty(src, new PropertyName("newName"));
        assertEquals(src, dest);
        assertEquals(forward, dest._forward);
        assertEquals(objectIdInfo, dest._objectIdInfo);
        assertEquals("newName", dest.getName());

        dest = new ObjectIdReferenceProperty(src, mock(JsonDeserializer.class));
        assertEquals(src, dest);
        assertEquals(forward, dest._forward);
        assertEquals(objectIdInfo, dest._objectIdInfo);
        assertNotNull(dest.getValueDeserializer());

        dest = new ObjectIdReferenceProperty(src, mock(NullValueProvider.class));
        assertEquals(src, dest);
        assertEquals(forward, dest._forward);
        assertEquals(objectIdInfo, dest._objectIdInfo);
        assertNotNull(dest.getNullProvider());
    }

    @Test
    public void testObjectIdReferencePropertyWithName() {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        PropertyName newName = new PropertyName("newName");
        ObjectIdReferenceProperty newProperty = property.withName(newName);
        assertEquals(property, newProperty);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(newName, newProperty.getName());
    }

    @Test
    public void testObjectIdReferencePropertyWithValueDeserializer() {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        ObjectIdReferenceProperty newProperty = property.withValueDeserializer(valueDeserializer);
        assertEquals(property, newProperty);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(valueDeserializer, newProperty.getValueDeserializer());
    }

    @Test
    public void testObjectIdReferencePropertyWithNullProvider() {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        ObjectIdReferenceProperty newProperty = property.withNullProvider(nva);
        assertEquals(property, newProperty);
        assertEquals(forward, newProperty._forward);
        assertEquals(objectIdInfo, newProperty._objectIdInfo);
        assertEquals(nva, newProperty.getNullProvider());
    }

    @Test
    public void testFixAccess() {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        DeserializationConfig config = mock(DeserializationConfig.class);
        property.fixAccess(config);
        if (forward != null) {
            verify(forward).fixAccess(config);
        }
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Annotation annotation = mock(Annotation.class);
        when(forward.getAnnotation(Annotation.class)).thenReturn(annotation);
        assertEquals(annotation, property.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(forward.getMember()).thenReturn(member);
        assertEquals(member, property.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        int creatorIndex = 1;
        when(forward.getCreatorIndex()).thenReturn(creatorIndex);
        assertEquals(creatorIndex, property.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        when(forward.deserializeAndSet(p, ctxt, instance)).thenReturn("result");
        Object result = property.deserializeAndSet(p, ctxt, instance);
        assertEquals("result", result);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        when(forward.deserializeSetAndReturn(p, ctxt, instance)).thenReturn("result");
        Object result = property.deserializeSetAndReturn(p, ctxt, instance);
        assertEquals("result", result);

        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
        when(p.getObjectId()).thenReturn(reference);

        Object id = new Object();
        when(reference.getId()).thenReturn(id);

        when(forward.getObjectIdReader()).thenReturn(mock(ObjectIdReader.class));
        property.set(_pojo, id);
        assertEquals(id, property.get(_pojo));
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object instance = new Object();
        Object value = new Object();

        property.set(instance, value);
        verify(forward).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object instance = new Object();
        Object value = new Object();

        when(forward.setAndReturn(instance, value)).thenReturn("result");
        Object result = property.setAndReturn(instance, value);
        assertEquals("result", result);
    }

    @Test
    public void testPropertyReferring() {
        ObjectIdReferenceProperty parent = mock(ObjectIdReferenceProperty.class);
        UnresolvedForwardReference ref = mock(UnresolvedForwardReference.class);
        Class<?> type = Object.class;
        Object pojo = new Object();

        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(parent, ref, type, pojo);

        Object id = new Object();
        referring.handleResolvedForwardReference(id, "result");
        assertEquals(pojo, referring._pojo);
        assertEquals(id, referring.getId());
        assertEquals("result", referring.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResolveForwardReferenceWithoutId() throws IOException {
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
        when(p.getObjectId()).thenReturn(reference);
        when(reference.getId()).thenReturn(null);
        property.set(_pojo, null);
        property.deserializeSetAndReturn(p, ctxt, instance);
    }
}
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectIdReferencePropertyTest {

    private ObjectIdInfo objectIdInfo;
    private SettableBeanProperty forward;
    private ObjectIdReferenceProperty objectIdReferenceProperty;
    private JsonDeserializer<?> jsonDeserializer;
    private NullValueProvider nullValueProvider;
    private Object pojo;
    private Object referencedObject;

    @org.junit.Before
    public void setUp() throws Exception {
        objectIdInfo = mock(ObjectIdInfo.class);
        forward = mock(SettableBeanProperty.class);
        jsonDeserializer = mock(JsonDeserializer.class);
        nullValueProvider = mock(NullValueProvider.class);
        pojo = new Object();
        referencedObject = new Object();
        objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    }

    @org.junit.Test
    public void testWithName() {
        ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withName(PropertyName.constructorFor("newName"));
        assertSame(objectIdReferenceProperty, newObjectIdReferenceProperty);
    }

    @org.junit.Test
    public void testWithValueDeserializer() {
        ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withValueDeserializer(jsonDeserializer);
        assertSame(objectIdReferenceProperty, newObjectIdReferenceProperty);
    }

    @org.junit.Test
    public void testWithNullProvider() {
        ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withNullProvider(nullValueProvider);
        assertSame(objectIdReferenceProperty, newObjectIdReferenceProperty);
    }

    @org.junit.Test
    public void testFixAccess() throws IOException {
        doNothing().when(forward).fixAccess(any(DeserializationConfig.class));
        objectIdReferenceProperty.fixAccess(mock(DeserializationConfig.class));
        verify(forward).fixAccess(any(DeserializationConfig.class));
    }

    @org.junit.Test
    public void testGetAnnotation() {
        when(forward.getAnnotation(any(Annotation.class))).thenReturn(mock(JsonAutoDetect.class));
        assertSame(mock(JsonAutoDetect.class), objectIdReferenceProperty.getAnnotation(JsonAutoDetect.class));
    }

    @org.junit.Test
    public void testGetMember() {
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        when(forward.getMember()).thenReturn(annotatedMember);
        assertSame(annotatedMember, objectIdReferenceProperty.getMember());
    }

    @org.junit.Test
    public void testGetCreatorIndex() {
        when(forward.getCreatorIndex()).thenReturn(0);
        assertEquals(0, objectIdReferenceProperty.getCreatorIndex());
    }

    @org.junit.Test
    public void testDeserializeAndSet() throws IOException {
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        node.put("value", "Test");
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getValueAsTree()).thenReturn(node);

        doNothing().when(forward).deserializeAndSet(jsonParser, any(DeserializationContext.class), any(Object.class));
        objectIdReferenceProperty.deserializeAndSet(jsonParser, mock(DeserializationContext.class), pojo);
        verify(forward).deserializeAndSet(jsonParser, any(DeserializationContext.class), pojo);
    }

    @org.junit.Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        node.put("value", "Test");
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getValueAsTree()).thenReturn(node);

        Object deserializedObject = mock(Object.class);
        when(jsonDeserializer.deserialize(jsonParser, mock(DeserializationContext.class))).thenReturn(deserializedObject);
        when(forward.deserialize(jsonParser, any(DeserializationContext.class))).thenReturn(deserializedObject);

        Object result = objectIdReferenceProperty.deserializeSetAndReturn(jsonParser, mock(DeserializationContext.class), pojo);
        assertSame(deserializedObject, result);
    }

    @org.junit.Test
    public void testSet() throws IOException {
        Object value = "Test";
        objectIdReferenceProperty.set(pojo, value);
        verify(forward).set(pojo, value);
    }

    @org.junit.Test
    public void testSetAndReturn() throws IOException {
        Object value = "Test";
        Object result = objectIdReferenceProperty.setAndReturn(pojo, value);
        assertSame(value, result);
    }

    @org.junit.Test
    public void testDeserializeAndSetWithUnresolvedForwardReference() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        when(deserializationContext.handleUnknownProperty("", null, null)).thenThrow(new UnresolvedForwardReference("Test"));
        when(jsonParser.getValueAsTree()).thenReturn(ObjectMapper.getDefaultInstance().createObjectNode());

        doThrow(new IOException("Test")).when(objectIdReferenceProperty).deserializeSetAndReturn(jsonParser, deserializationContext, pojo);
        UnresolvedForwardReference unresolvedForwardReference = new UnresolvedForwardReference("Test");
        objectIdReferenceProperty.deserializeAndSet(jsonParser, deserializationContext, pojo);

        verify(objectIdReferenceProperty).deserializeSetAndReturn(jsonParser, deserializationContext, pojo);
        verify(unresolvedForwardReference).getRoid().appendReferring(any(PropertyReferring.class));
        assertNull(objectIdReferenceProperty.get(pojo));
    }

    @org.junit.Test
    public void testSetWithUnresolvedForwardReference() throws IOException {
        UnresolvedForwardReference unresolvedForwardReference = new UnresolvedForwardReference("Test");
        objectIdReferenceProperty.set(pojo, unresolvedForwardReference);
        assertNull(objectIdReferenceProperty.get(pojo));
    }
}
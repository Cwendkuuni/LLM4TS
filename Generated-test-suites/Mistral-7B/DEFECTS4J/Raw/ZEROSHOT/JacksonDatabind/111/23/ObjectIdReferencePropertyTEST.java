package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ObjectIdReferencePropertyTest {

    private ObjectIdInfo objectIdInfo;
    private JsonDeserializer<?> jsonDeserializer;
    private ObjectIdReferenceProperty objectIdReferenceProperty;
    private SettableBeanProperty settableBeanProperty;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        objectIdInfo = mock(ObjectIdInfo.class);
        jsonDeserializer = mock(JsonDeserializer.class);
        settableBeanProperty = mock(SettableBeanProperty.class);
        when(settableBeanProperty.getName()).thenReturn(new PropertyName("testProperty"));
        objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanProperty, objectIdInfo);
    }

    @Test
    public void testConstructorWithForward() {
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertEquals(forward, objectIdReferenceProperty._forward);
        assertEquals(objectIdInfo, objectIdReferenceProperty._objectIdInfo);
    }

    @Test
    public void testConstructorWithDeserializer() {
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(objectIdReferenceProperty, jsonDeserializer, null);
        assertEquals(objectIdReferenceProperty, objectIdReferenceProperty._forward);
        assertEquals(jsonDeserializer, objectIdReferenceProperty._valueDeserializer);
    }

    @Test
    public void testConstructorWithName() {
        PropertyName newName = new PropertyName("testName");
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(objectIdReferenceProperty, newName);
        assertEquals(objectIdReferenceProperty, objectIdReferenceProperty._forward);
        assertEquals(newName, objectIdReferenceProperty.getName());
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("testName");
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(objectIdReferenceProperty);
        ObjectIdReferenceProperty result = objectIdReferenceProperty.withName(newName);
        assertEquals(objectIdReferenceProperty, result._forward);
        assertEquals(newName, result.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> jsonDeserializer = mock(JsonDeserializer.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(objectIdReferenceProperty, null, null);
        ObjectIdReferenceProperty result = objectIdReferenceProperty.withValueDeserializer(jsonDeserializer);
        assertEquals(objectIdReferenceProperty, result._forward);
        assertEquals(jsonDeserializer, result._valueDeserializer);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(objectIdReferenceProperty, null, null);
        ObjectIdReferenceProperty result = objectIdReferenceProperty.withNullProvider(nullValueProvider);
        assertEquals(objectIdReferenceProperty, result._forward);
        assertEquals(nullValueProvider, result._nullProvider);
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        objectIdReferenceProperty.fixAccess(config);
        verify(config).findBeanProperties();
        verifyNoMoreInteractions(config);
    }

    @Test
    public void testGetAnnotation() {
        Annotation annotation = mock(Annotation.class);
        when(objectIdInfo.getAnnotation(eq(annotation.getClass()))).thenReturn(annotation);
        assertEquals(annotation, objectIdReferenceProperty.getAnnotation(annotation.getClass()));
    }

    @Test
    public void testGetMember() {
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(settableBeanProperty.getMember()).thenReturn(member);
        assertEquals(member, objectIdReferenceProperty.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        assertEquals(-1, objectIdReferenceProperty.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = new Object();
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(p.getValueAsToken()).thenReturn(ContextualSerializationContext.emptyToken());
        Object id = new Object();
        when(jsonDeserializer.deserialize(eq(p), eq(ctxt), anyObject()))
                .thenReturn(id);
        objectIdReferenceProperty.deserializeAndSet(p, ctxt, instance);
        verify(instance).set(eq(id));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = new Object();
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object value = new Object();
        when(jsonDeserializer.deserialize(eq(p), eq(ctxt))).thenReturn(value);
        when(objectIdReferenceProperty.setAndReturn(instance, value)).thenReturn(value);
        Object result = objectIdReferenceProperty.deserializeSetAndReturn(p, ctxt, instance);
        assertEquals(value, result);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        objectIdReferenceProperty.set(instance, value);
        verify(instance).set(value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        Object result = objectIdReferenceProperty.setAndReturn(instance, value);
        assertEquals(value, result);
    }

    @Test
    public void testHandleResolvedForwardReference() throws IOException {
        Object id = new Object();
        Object value = new Object();
        ObjectNode node = mock(ObjectNode.class);
        when(jsonDeserializer.getObjectIdReader()).thenReturn(new ObjectIdReader() {
            @Override
            public ObjectId readObjectId(JsonParser p, DeserializationContext ctxt) throws IOException {
                JsonToken token = p.getValueAsToken();
                if (token.isValueIdentifierStart()) {
                    ObjectNode idNode = p.readValueAs(ObjectNode.class);
                    return new ObjectId(node.get("id").asText(), null, idNode);
                }
                return null;
            }
        });
        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
        PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, reference, Object.class, new Object());
        when(reference.getId()).thenReturn(id);
        propertyReferring.handleResolvedForwardReference(id, value);
        verify(objectIdReferenceProperty).set(eq(new Object()), value);
    }
}
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

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
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class ObjectIdReferencePropertyTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private SettableBeanProperty forward;

    @Mock
    private ObjectIdInfo objectIdInfo;

    @Mock
    private JsonDeserializer<?> deserializer;

    @Mock
    private NullValueProvider nullValueProvider;

    @Mock
    private PropertyName newName;

    @Mock
    private DeserializationConfig config;

    @Mock
    private Annotation annotation;

    @Mock
    private AnnotatedMember member;

    @Mock
    private Referring referring;

    private ObjectIdReferenceProperty objectIdReferenceProperty;

    @Before
    public void setUp() {
        objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    }

    @Test
    public void testConstructor() {
        assertEquals(forward, objectIdReferenceProperty._forward);
        assertEquals(objectIdInfo, objectIdReferenceProperty._objectIdInfo);
    }

    @Test
    public void testConstructorWithDeserializerAndNullValueProvider() {
        objectIdReferenceProperty = new ObjectIdReferenceProperty(objectIdReferenceProperty, deserializer, nullValueProvider);
        assertEquals(forward, objectIdReferenceProperty._forward);
        assertEquals(objectIdInfo, objectIdReferenceProperty._objectIdInfo);
        assertEquals(deserializer, objectIdReferenceProperty._valueDeserializer);
        assertEquals(nullValueProvider, objectIdReferenceProperty._nullProvider);
    }

    @Test
    public void testConstructorWithNewName() {
        objectIdReferenceProperty = new ObjectIdReferenceProperty(objectIdReferenceProperty, newName);
        assertEquals(forward, objectIdReferenceProperty._forward);
        assertEquals(objectIdInfo, objectIdReferenceProperty._objectIdInfo);
        assertEquals(newName, objectIdReferenceProperty._name);
    }

    @Test
    public void testWithName() {
        SettableBeanProperty result = objectIdReferenceProperty.withName(newName);
        assertEquals(ObjectIdReferenceProperty.class, result.getClass());
        assertEquals(newName, result._name);
    }

    @Test
    public void testWithValueDeserializer() {
        SettableBeanProperty result = objectIdReferenceProperty.withValueDeserializer(deserializer);
        assertEquals(ObjectIdReferenceProperty.class, result.getClass());
        assertEquals(deserializer, result._valueDeserializer);
    }

    @Test
    public void testWithNullProvider() {
        SettableBeanProperty result = objectIdReferenceProperty.withNullProvider(nullValueProvider);
        assertEquals(ObjectIdReferenceProperty.class, result.getClass());
        assertEquals(nullValueProvider, result._nullProvider);
    }

    @Test
    public void testFixAccess() {
        objectIdReferenceProperty.fixAccess(config);
        verify(forward, times(1)).fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        when(forward.getAnnotation(Annotation.class)).thenReturn(annotation);
        Annotation result = objectIdReferenceProperty.getAnnotation(Annotation.class);
        assertEquals(annotation, result);
    }

    @Test
    public void testGetMember() {
        when(forward.getMember()).thenReturn(member);
        AnnotatedMember result = objectIdReferenceProperty.getMember();
        assertEquals(member, result);
    }

    @Test
    public void testGetCreatorIndex() {
        when(forward.getCreatorIndex()).thenReturn(1);
        int result = objectIdReferenceProperty.getCreatorIndex();
        assertEquals(1, result);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        objectIdReferenceProperty.deserializeAndSet(p, ctxt, instance);
        verify(forward, times(1)).deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Object result = objectIdReferenceProperty.deserializeSetAndReturn(p, ctxt, instance);
        verify(forward, times(1)).setAndReturn(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        objectIdReferenceProperty.set(instance, value);
        verify(forward, times(1)).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        Object result = objectIdReferenceProperty.setAndReturn(instance, value);
        verify(forward, times(1)).setAndReturn(instance, result);
    }

    @Test
    public void testPropertyReferring() throws IOException {
        UnresolvedForwardReference ref = mock(UnresolvedForwardReference.class);
        Class<?> type = Object.class;
        Object ob = new Object();
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, ref, type, ob);
        propertyReferring.handleResolvedForwardReference(new Object(), new Object());
        verify(objectIdReferenceProperty, times(1)).set(ob, new Object());
    }
}
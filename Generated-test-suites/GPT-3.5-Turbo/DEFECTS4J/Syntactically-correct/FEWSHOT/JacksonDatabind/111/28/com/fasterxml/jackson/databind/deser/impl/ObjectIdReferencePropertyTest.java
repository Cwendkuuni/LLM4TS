package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.introspect.Annotation;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class ObjectIdReferencePropertyTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ObjectIdInfo objectIdInfo;
    private SettableBeanProperty forward;

    @Before
    public void setup() {
        forward = mock(SettableBeanProperty.class);
        objectIdInfo = mock(ObjectIdInfo.class);
    }

    @Test
    public void testConstructor() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testConstructorWithDeserializerAndNullValueProvider() {
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, deser, nva);

        assertEquals(property._forward, newProperty._forward);
        assertEquals(property._objectIdInfo, newProperty._objectIdInfo);
    }

    @Test
    public void testConstructorWithPropertyName() {
        com.fasterxml.jackson.databind.PropertyName newName = new com.fasterxml.jackson.databind.PropertyName("newName");

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, newName);

        assertEquals(property._forward, newProperty._forward);
        assertEquals(property._objectIdInfo, newProperty._objectIdInfo);
    }

    @Test
    public void testWithName() {
        com.fasterxml.jackson.databind.PropertyName newName = new com.fasterxml.jackson.databind.PropertyName("newName");

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty newProperty = property.withName(newName);

        assertEquals(property._forward, newProperty._forward);
        assertEquals(property._objectIdInfo, newProperty._objectIdInfo);
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty newProperty = property.withValueDeserializer(deser);

        assertEquals(property._forward, newProperty._forward);
        assertEquals(property._objectIdInfo, newProperty._objectIdInfo);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = mock(NullValueProvider.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty newProperty = property.withNullProvider(nva);

        assertEquals(property._forward, newProperty._forward);
        assertEquals(property._objectIdInfo, newProperty._objectIdInfo);
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationConfig.class);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.fixAccess(config);

        verify(forward, times(1)).fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        Class<Annotation> annotationClass = Annotation.class;

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.getAnnotation(annotationClass);

        verify(forward, times(1)).getAnnotation(annotationClass);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember expectedMember = mock(AnnotatedMember.class);

        when(forward.getMember()).thenReturn(expectedMember);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        AnnotatedMember actualMember = property.getMember();

        assertEquals(expectedMember, actualMember);
    }

    @Test
    public void testGetCreatorIndex() {
        int expectedCreatorIndex = 1;

        when(forward.getCreatorIndex()).thenReturn(expectedCreatorIndex);

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        int actualCreatorIndex = property.getCreatorIndex();

        assertEquals(expectedCreatorIndex, actualCreatorIndex);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.deserializeAndSet(p, ctxt, instance);

        verify(property, times(1)).deserializeSetAndReturn(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.deserializeSetAndReturn(p, ctxt, instance);

        verify(property, times(1)).setAndReturn(instance, contructPropertyClass(p, ctxt));
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.set(instance, value);

        verify(forward, times(1)).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.setAndReturn(instance, value);

        verify(forward, times(1)).setAndReturn(instance, value);
    }

    private Object contructPropertyClass(JsonParser p, DeserializationContext ctxt) throws IOException {
        return new Object();
    }
}
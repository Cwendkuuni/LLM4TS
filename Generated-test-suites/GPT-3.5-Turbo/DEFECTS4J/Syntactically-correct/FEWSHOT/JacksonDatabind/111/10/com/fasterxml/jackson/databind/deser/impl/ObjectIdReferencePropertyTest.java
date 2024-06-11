package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

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
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class ObjectIdReferencePropertyTest {

    private ObjectIdReferenceProperty objectIdReferenceProperty;

    @Mock
    private SettableBeanProperty forwardMock;
    @Mock
    private ObjectIdInfo objectIdInfoMock;
    @Mock
    private JsonDeserializer jsonDeserializerMock;
    @Mock
    private NullValueProvider nullValueProviderMock;
    @Mock
    private DeserializationConfig deserializationConfigMock;
    @Mock
    private Annotation annotationMock;
    @Mock
    private AnnotatedMember annotatedMemberMock;
    @Mock
    private PropertyName propertyNameMock;
    @Mock
    private JsonParser jsonParserMock;
    @Mock
    private DeserializationContext deserializationContextMock;
    @Mock
    private Object instanceMock;
    @Mock
    private UnresolvedForwardReference unresolvedForwardReferenceMock;
    @Mock
    private PropertyReferring propertyReferringMock;
    @Mock
    private Class<?> classMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        objectIdReferenceProperty = new ObjectIdReferenceProperty(forwardMock, objectIdInfoMock);
    }

    @Test
    public void testConstructor() {
        assertEquals(forwardMock, objectIdReferenceProperty._forward);
        assertEquals(objectIdInfoMock, objectIdReferenceProperty._objectIdInfo);
    }

    @Test
    public void testConstructor2() {
        ObjectIdReferenceProperty result = new ObjectIdReferenceProperty(objectIdReferenceProperty, jsonDeserializerMock, nullValueProviderMock);
        assertEquals(objectIdReferenceProperty, result._forward);
        assertEquals(jsonDeserializerMock, result._valueDeserializer);
        assertEquals(nullValueProviderMock, result._nullProvider);
        assertEquals(objectIdInfoMock, result._objectIdInfo);
    }

    @Test
    public void testConstructor3() {
        ObjectIdReferenceProperty result = new ObjectIdReferenceProperty(objectIdReferenceProperty, propertyNameMock);
        assertEquals(objectIdReferenceProperty, result._forward);
        assertEquals(propertyNameMock, result._propName);
        assertEquals(objectIdInfoMock, result._objectIdInfo);
    }

    @Test
    public void testWithName() {
        ObjectIdReferenceProperty result = (ObjectIdReferenceProperty) objectIdReferenceProperty.withName(propertyNameMock);
        assertEquals(objectIdReferenceProperty, result._forward);
        assertEquals(propertyNameMock.toString(), result._propName.toString());
        assertEquals(objectIdInfoMock, result._objectIdInfo);
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReferenceProperty result = (ObjectIdReferenceProperty) objectIdReferenceProperty.withValueDeserializer(jsonDeserializerMock);
        assertEquals(objectIdReferenceProperty, result._forward);
        assertEquals(jsonDeserializerMock, result._valueDeserializer);
        assertEquals(objectIdInfoMock, result._objectIdInfo);
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReferenceProperty result = (ObjectIdReferenceProperty) objectIdReferenceProperty.withNullProvider(nullValueProviderMock);
        assertEquals(objectIdReferenceProperty, result._forward);
        assertEquals(nullValueProviderMock, result._nullProvider);
        assertEquals(objectIdInfoMock, result._objectIdInfo);
    }

    @Test
    public void testFixAccess() {
        when(forwardMock.getMember()).thenReturn(annotatedMemberMock);
        objectIdReferenceProperty.fixAccess(deserializationConfigMock);
        verify(forwardMock, times(1)).fixAccess(deserializationConfigMock);
    }

    @Test
    public void testGetAnnotation() {
        when(forwardMock.getAnnotation(Annotation.class)).thenReturn(annotationMock);
        Annotation result = objectIdReferenceProperty.getAnnotation(Annotation.class);
        assertEquals(annotationMock, result);
    }

    @Test
    public void getMember() {
        when(forwardMock.getMember()).thenReturn(annotatedMemberMock);
        AnnotatedMember result = objectIdReferenceProperty.getMember();
        assertEquals(annotatedMemberMock, result);
    }

    @Test
    public void getCreatorIndex() {
        when(forwardMock.getCreatorIndex()).thenReturn(1);
        int result = objectIdReferenceProperty.getCreatorIndex();
        assertEquals(1, result);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        objectIdReferenceProperty.deserializeAndSet(jsonParserMock, deserializationContextMock, instanceMock);
        verify(forwardMock, times(1)).deserializeAndSet(jsonParserMock, deserializationContextMock, instanceMock);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        when(forwardMock.setAndReturn(instanceMock, null)).thenReturn(null);

        Object result = objectIdReferenceProperty.deserializeSetAndReturn(jsonParserMock, deserializationContextMock, instanceMock);

        verify(forwardMock, times(1)).setAndReturn(instanceMock, null);
        assertNull(result);
    }

    @Test
    public void testSet() throws IOException {
        objectIdReferenceProperty.set(instanceMock, null);
        verify(forwardMock, times(1)).set(instanceMock, null);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        when(forwardMock.setAndReturn(instanceMock, null)).thenReturn(null);

        Object result = objectIdReferenceProperty.setAndReturn(instanceMock, null);

        verify(forwardMock, times(1)).setAndReturn(instanceMock, null);
        assertNull(result);
    }

    @Test
    public void testPropertyReferring() throws IOException {
        when(propertyReferringMock.hasId(any())).thenReturn(true);
        objectIdReferenceProperty.set(instanceMock, null);
        verify(forwardMock, times(1)).set(instanceMock, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandleResolvedForwardReference() throws IOException {
        propertyReferringMock.hasId(any());
        objectIdReferenceProperty.set(instanceMock, null);
        verify(forwardMock, times(1)).set(instanceMock, null);
    }

}
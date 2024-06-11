package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ObjectIdReferencePropertyTest {

    @Mock
    private SettableBeanProperty forwardMock;

    @Mock
    private ObjectIdInfo objectIdInfoMock;

    @Mock
    private JsonDeserializer<?> deserializerMock;

    @Mock
    private NullValueProvider nullValueProviderMock;

    @Mock
    private PropertyName propertyNameMock;

    @Mock
    private JsonParser jsonParserMock;

    @Mock
    private DeserializationContext deserializationContextMock;

    @Mock
    private Object instanceMock;

    private ObjectIdReferenceProperty objectIdReferenceProperty;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        objectIdReferenceProperty = new ObjectIdReferenceProperty(forwardMock, objectIdInfoMock);
    }

    @Test
    public void testWithNullProvider() {
        assertNotNull(objectIdReferenceProperty.withNullProvider(null));
        verifyNoMoreInteractions(forwardMock);
        verifyZeroInteractions(objectIdInfoMock);
    }

    @Test
    public void testFixAccess() {
        objectIdReferenceProperty.fixAccess(null);
        verify(forwardMock).fixAccess(null);
        verifyNoMoreInteractions(forwardMock);
        verifyZeroInteractions(objectIdInfoMock);
    }

    @Test
    public void testGetAnnotation() {
        assertNotNull(objectIdReferenceProperty.getAnnotation(null));
        verify(forwardMock).getAnnotation(null);
        verifyNoMoreInteractions(forwardMock);
        verifyZeroInteractions(objectIdInfoMock);
    }

    @Test
    public void testGetMember() {
        assertNotNull(objectIdReferenceProperty.getMember());
        verify(forwardMock).getMember();
        verifyNoMoreInteractions(forwardMock);
        verifyZeroInteractions(objectIdInfoMock);
    }

    @Test
    public void testGetCreatorIndex() {
        assertNotNull(objectIdReferenceProperty.getCreatorIndex());
        verify(forwardMock).getCreatorIndex();
        verifyNoMoreInteractions(forwardMock);
        verifyZeroInteractions(objectIdInfoMock);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        when(forwardMock.deserialize(p, ctxt)).thenReturn("result");

        objectIdReferenceProperty.deserializeAndSet(jsonParserMock, deserializationContextMock, instanceMock);

        verify(forwardMock).deserializeSetAndReturn(jsonParserMock, deserializationContextMock, instanceMock);
        verifyNoMoreInteractions(forwardMock);
        verifyZeroInteractions(objectIdInfoMock);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        when(forwardMock.setAndReturn(instanceMock, deserializerMock)).thenReturn("result");

        assertEquals("result", objectIdReferenceProperty.deserializeSetAndReturn(jsonParserMock, deserializationContextMock, instanceMock));

        verify(forwardMock).setAndReturn(instanceMock, deserializerMock);
        verifyNoMoreInteractions(forwardMock);
        verifyZeroInteractions(objectIdInfoMock);
    }

    @Test
    public void testSet() throws Exception {
        objectIdReferenceProperty.set(instanceMock, "value");

        verify(forwardMock).set(instanceMock, "value");
        verifyNoMoreInteractions(forwardMock);
        verifyZeroInteractions(objectIdInfoMock);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        when(forwardMock.setAndReturn(instanceMock, "value")).thenReturn("result");

        assertEquals("result", objectIdReferenceProperty.setAndReturn(instanceMock, "value"));

        verify(forwardMock).setAndReturn(instanceMock, "value");
        verifyNoMoreInteractions(forwardMock);
        verifyZeroInteractions(objectIdInfoMock);
    }

    @Test
    public void testPropertyReferringHandleResolvedForwardReference() throws Exception {
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, null, null, instanceMock);

        propertyReferring.handleResolvedForwardReference("id", "value");

        verify(forwardMock).set(instanceMock, "value");
        verifyNoMoreInteractions(forwardMock);
    }
}
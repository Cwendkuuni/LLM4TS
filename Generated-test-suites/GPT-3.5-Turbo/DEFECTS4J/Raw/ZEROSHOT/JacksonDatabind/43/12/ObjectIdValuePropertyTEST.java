package com.fasterxml.jackson.databind.deser.impl;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class ObjectIdValuePropertyTest {

    @Mock
    private JsonParser mockJsonParser;
    @Mock
    private DeserializationContext mockDeserializationContext;
    @Mock
    private Object mockInstance;
    @Mock
    private ObjectIdReader mockObjectIdReader;
    @Mock
    private JsonDeserializer mockJsonDeserializer;
    @Mock
    private ReadableObjectId mockReadableObjectId;
    @Mock
    private SettableBeanProperty mockIdProperty;
    @Mock
    private PropertyMetadata mockPropertyMetadata;

    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, mockPropertyMetadata);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        when(mockJsonDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(null);
        objectIdValueProperty.deserializeAndSet(mockJsonParser, mockDeserializationContext, mockInstance);
        verify(mockJsonDeserializer).deserialize(eq(mockJsonParser), eq(mockDeserializationContext));
        verify(mockObjectIdReader).generator;
        verify(mockObjectIdReader).resolver;

        verify(mockReadableObjectId).bindItem(eq(mockInstance));

        verify(mockObjectIdReader).idProperty;
        verifyNoMoreInteractions(mockIdProperty);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        when(mockJsonDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(null);
        objectIdValueProperty.deserializeSetAndReturn(mockJsonParser, mockDeserializationContext, mockInstance);
        verify(mockJsonDeserializer).deserialize(eq(mockJsonParser), eq(mockDeserializationContext));
        verify(mockObjectIdReader).generator;
        verify(mockObjectIdReader).resolver;

        verify(mockReadableObjectId).bindItem(eq(mockInstance));

        verify(mockObjectIdReader).idProperty;
    }

    @Test
    public void testSet() throws IOException {
        when(mockObjectIdReader.idProperty).thenReturn(mockIdProperty);
        objectIdValueProperty.set(mockInstance, null);
        verify(mockObjectIdReader).idProperty;
        verify(mockIdProperty).setAndReturn(eq(mockInstance), eq(null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithNoIdProperty() throws IOException {
        when(mockObjectIdReader.idProperty).thenReturn(null);
        objectIdValueProperty.set(mockInstance, null);
    }
}
package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.PropertyName;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.annotation.Annotation;

import static org.mockito.Mockito.*;

public class ObjectIdValuePropertyTest {

    @Mock
    private ObjectIdReader objectIdReader;

    private SettableBeanProperty settableBeanProperty;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        settableBeanProperty = new ObjectIdValueProperty(objectIdReader, null);
    }

    @Test
    public void testGetName() {
        // Arrange
        PropertyName expectedName = new PropertyName("name");

        // Act
        PropertyName actualName = settableBeanProperty.getName();

        // Assert
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetType() {
        // Arrange
        Class<?> expectedType = String.class;

        // Act
        Class<?> actualType = settableBeanProperty.getType();

        // Assert
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetMetadata() {
        // Arrange
        PropertyMetadata metadata = mock(PropertyMetadata.class);
        when(objectIdReader.getDeserializer()).thenReturn(null);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("name"));
        when(objectIdReader.getIdType()).thenReturn(String.class);

        // Act
        SettableBeanProperty settableBeanProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        PropertyMetadata actualMetadata = settableBeanProperty.getMetadata();

        // Assert
        assertEquals(metadata, actualMetadata);
    }

    @Test
    public void testDeserializationAndSet() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        // Act
        settableBeanProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        // Assert
        verify(jsonParser, times(1)).hasToken(JsonToken.VALUE_NULL);
        verify(deserializationContext, never()).findObjectId(any(), any(), any());
        verify(jsonParser, never()).nextToken();
        verify(jsonParser, never()).readValueAs(any(Class.class));
    }

    @Test
    public void testDeserializationSetAndReturn() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        // Act
        settableBeanProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        // Assert
        verify(jsonParser, times(1)).hasToken(JsonToken.VALUE_NULL);
        verify(deserializationContext, never()).findObjectId(any(), any(), any());
        verify(jsonParser, never()).nextToken();
        verify(jsonParser, never()).readValueAs(any(Class.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        // Arrange
        Object instance = new Object();
        Object value = new Object();

        // Act
        settableBeanProperty.set(instance, value);

        // Assert
        // Exception expected
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws IOException {
        // Arrange
        Object instance = new Object();
        Object value = new Object();

        // Act
        settableBeanProperty.setAndReturn(instance, value);

        // Assert
        // Exception expected
    }
}
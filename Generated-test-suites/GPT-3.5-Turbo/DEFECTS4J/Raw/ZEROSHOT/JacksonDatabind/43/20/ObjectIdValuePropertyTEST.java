package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.annotation.Annotation;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty objectIdValueProperty;

    @Mock
    private ObjectIdReader mockObjectIdReader;
    @Mock
    private JsonDeserializer mockJsonDeserializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, PropertyMetadata.STD_REQUIRED);
    }

    @Test
    public void withName() {
        PropertyName propertyName = new PropertyName("newName");
        ObjectIdValueProperty withName = objectIdValueProperty.withName(propertyName);
        assert withName != null;
        assert withName.getFullName().equals(propertyName);
    }

    @Test
    public void withValueDeserializer() {
        ObjectIdValueProperty withValueDeserializer = objectIdValueProperty.withValueDeserializer(mockJsonDeserializer);
        assert withValueDeserializer != null;
        assert withValueDeserializer.getValueDeserializer().equals(mockJsonDeserializer);
    }

    @Test
    public void getAnnotation() {
        Class<Annotation> annotationClass = Annotation.class;
        Annotation annotation = objectIdValueProperty.getAnnotation(annotationClass);
        assert annotation == null;
    }

    @Test
    public void getMember() {
        assert objectIdValueProperty.getMember() == null;
    }

    @Test
    public void deserializeAndSet() throws IOException {
        JsonParser mockJsonParser = mock(JsonParser.class);
        DeserializationContext mockDeserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        objectIdValueProperty.deserializeAndSet(mockJsonParser, mockDeserializationContext, instance);

        verify(objectIdValueProperty).deserializeSetAndReturn(any(JsonParser.class), any(DeserializationContext.class), any(Object.class));
    }

    @Test
    public void deserializeSetAndReturn() throws IOException {
        JsonParser mockJsonParser = mock(JsonParser.class);
        DeserializationContext mockDeserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        when(mockObjectIdReader.getDeserializer()).thenReturn(mockJsonDeserializer);
        when(mockJsonDeserializer.deserialize(mockJsonParser, mockDeserializationContext)).thenReturn(instance);

        Object result = objectIdValueProperty.deserializeSetAndReturn(mockJsonParser, mockDeserializationContext, instance);

        assert result == instance;
        verify(mockObjectIdReader).getDeserializer();
        verify(mockJsonDeserializer).deserialize(mockJsonParser, mockDeserializationContext);
    }

    @Test
    public void set() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        SettableBeanProperty idProperty = mock(SettableBeanProperty.class);
        when(mockObjectIdReader.idProperty).thenReturn(idProperty);

        objectIdValueProperty.set(instance, value);

        verify(idProperty).setAndReturn(instance, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void setAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        SettableBeanProperty idProperty = mock(SettableBeanProperty.class);
        when(mockObjectIdReader.idProperty).thenReturn(null);

        objectIdValueProperty.setAndReturn(instance, value);
    }
}
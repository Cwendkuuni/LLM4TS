package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty objectIdValueProperty;
    private ObjectIdReader objectIdReader;
    private PropertyMetadata propertyMetadata;
    private JsonDeserializer<?> deserializer;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private Object instance;
    private SettableBeanProperty idProperty;

    @Before
    public void setup() {
        objectIdReader = mock(ObjectIdReader.class);
        propertyMetadata = mock(PropertyMetadata.class);
        deserializer = mock(JsonDeserializer.class);
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
        instance = new Object();
        idProperty = mock(SettableBeanProperty.class);

        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
    }

    @Test
    public void testConstructor() {
        assertEquals(objectIdValueProperty.getDefaultValue(), null);
        assertEquals(objectIdValueProperty.getIdType(), objectIdReader.getIdType());
        assertEquals(objectIdValueProperty.getMetadata(), propertyMetadata);
        assertEquals(objectIdValueProperty.getDeserializer(), objectIdReader.getDeserializer());
        assertEquals(objectIdValueProperty.getAnnotation(Object.class), null);
        assertEquals(objectIdValueProperty.getMember(), null);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        when(objectIdReader.getIdProperty()).thenReturn(idProperty);
        when(idProperty.setAndReturn(instance, "id")).thenReturn(instance);

        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        assertEquals(result, instance);
        verify(objectIdReader, times(1)).getIdProperty();
        verify(idProperty, times(1)).setAndReturn(instance, "id");
    }

    @Test
    public void testDeserializeAndSet_NullId() throws IOException {
        when(objectIdReader.getIdProperty()).thenReturn(idProperty);
        when(idProperty.setAndReturn(instance, null)).thenReturn(instance);

        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        assertEquals(result, instance);
        verify(objectIdReader, times(1)).getIdProperty();
        verify(idProperty, times(1)).setAndReturn(instance, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn_NoIdProperty() throws IOException {
        objectIdValueProperty.setAndReturn(instance, "id");
    }

    @Test
    public void testSetAndReturn() throws IOException {
        when(objectIdReader.getIdProperty()).thenReturn(idProperty);
        when(idProperty.setAndReturn(instance, "id")).thenReturn(instance);

        Object result = objectIdValueProperty.setAndReturn(instance, "id");

        assertEquals(result, instance);
        verify(objectIdReader, times(1)).getIdProperty();
        verify(idProperty, times(1)).setAndReturn(instance, "id");
    }
}
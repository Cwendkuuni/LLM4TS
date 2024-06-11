package com.fasterxml.jackson.databind.deser.impl;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

public class ObjectIdValuePropertyTest {

    @Mock
    private JsonParser jsonParser;
    @Mock
    private DeserializationContext deserializationContext;
    @Mock
    private Object instance;
    @Mock
    private Object value;
    @Mock
    private ObjectIdReader objectIdReader;
    @Mock
    private PropertyMetadata propertyMetadata;
    @Mock
    private JsonDeserializer<?> jsonDeserializer;
    @Mock
    private PropertyName propertyName;

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        objectIdValueProperty.setDeserializer(jsonDeserializer);
        objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        verify(jsonDeserializer, times(1)).deserialize(jsonParser, deserializationContext);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(value);
        when(objectIdReader.generator).thenReturn(new ObjectIdReader.IdGenerator());
        
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        objectIdValueProperty.setDeserializer(jsonDeserializer);
        objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
        verify(jsonDeserializer, times(1)).deserialize(jsonParser, deserializationContext);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        objectIdValueProperty.setAndReturn(instance, value);
    }

    @Test
    public void testSetWithIdProperty() throws IOException {
        when(objectIdReader.idProperty).thenReturn(null);
        
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        objectIdValueProperty.setAndReturn(instance, value);
        verify(objectIdReader, times(1)).idProperty;
    }
    
    @Test
    public void testName() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        objectIdValueProperty.withName(propertyName);
    }

    @Test
    public void testValueDeserializer() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        objectIdValueProperty.withValueDeserializer(jsonDeserializer);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        objectIdValueProperty.getAnnotation(Annotation.class);
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        objectIdValueProperty.getMember();
    }
}
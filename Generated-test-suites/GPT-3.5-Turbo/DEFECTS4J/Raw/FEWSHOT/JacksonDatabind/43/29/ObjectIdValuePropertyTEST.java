package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.deser.DeserializationContext;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {
    
    @Mock
    private ObjectIdReader objectIdReader;
    
    @Mock
    private PropertyMetadata metadata;
    
    @Mock
    private JsonDeserializer<?> deserializer;
    
    @Mock
    private JsonParser jsonParser;
    
    @Mock
    private DeserializationContext deserializationContext;
    
    @Mock
    private Object instance;
    
    @Mock
    private ReadableObjectId readableObjectId;
    
    @Mock
    private SettableBeanProperty idProperty;
    
    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    }
    
    @Test
    public void testWithJsonProperty() {
        ObjectIdValueProperty newProperty = objectIdValueProperty.withJsonProperty("newProperty");
        assertEquals("newProperty", newProperty.getJsonProperty());
    }
    
    @Test
    public void testWithIdType() {
        ObjectIdValueProperty newProperty = objectIdValueProperty.withIdType(String.class);
        assertEquals(String.class, newProperty.getIdType());
    }
    
    @Test
    public void testGetValueDeserializer() {
        ObjectIdValueProperty newProperty = objectIdValueProperty.withValueDeserializer(deserializer);
        assertEquals(deserializer, newProperty.getValueDeserializer());
    }
    
    @Test
    public void testWithDefaultValue() {
        when(objectIdReader.getIdType().hasRawClass(String.class)).thenReturn(true);
        ObjectIdValueProperty newProperty = objectIdValueProperty.withDefaultValue("default");
        assertEquals("default", newProperty.getDefaultValue());
    }
    
    @Test
    public void testWithPlaceholderValue() {
        when(objectIdReader.getIdType().hasRawClass(String.class)).thenReturn(true);
        ObjectIdValueProperty newProperty = objectIdValueProperty.withPlaceholderValue("placeholder");
        assertEquals("placeholder", newProperty.getPlaceholderValue());
    }
    
    @Test
    public void testWithDescription() {
        ObjectIdValueProperty newProperty = objectIdValueProperty.withDescription("description");
        assertEquals("description", newProperty.getDescription());
    }
    
    @Test
    public void testGetAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(Test.class));
    }
    
    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        when(objectIdReader.getIdType().hasRawClass(String.class)).thenReturn(true);
        when(objectIdReader.getDeserializer()).thenReturn(deserializer);
        when(deserializer.deserialize(jsonParser, deserializationContext)).thenReturn("id");
        when(deserializationContext.findObjectId("id", objectIdReader.generator, objectIdReader.resolver)).thenReturn(readableObjectId);
        objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        verify(readableObjectId, times(1)).bindItem(instance);
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        when(objectIdReader.getIdType().hasRawClass(String.class)).thenReturn(true);
        when(objectIdReader.getDeserializer()).thenReturn(deserializer);
        when(deserializer.deserialize(jsonParser, deserializationContext)).thenReturn("id");
        when(deserializationContext.findObjectId("id", objectIdReader.generator, objectIdReader.resolver)).thenReturn(readableObjectId);
        when(objectIdReader.idProperty).thenReturn(idProperty);
        when(idProperty.setAndReturn(instance, "id")).thenReturn(instance);
        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
        assertEquals(instance, result);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnNoIdProperty() throws IOException {
        objectIdValueProperty.setAndReturn(instance, "id");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetNoIdProperty() throws IOException {
        objectIdValueProperty.set(instance, "id");
    }

}
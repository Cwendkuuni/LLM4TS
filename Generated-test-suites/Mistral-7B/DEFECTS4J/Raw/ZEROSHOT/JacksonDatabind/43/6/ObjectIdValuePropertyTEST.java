package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Test for {@link ObjectIdValueProperty} class
 */
public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setup() throws Exception {
        objectIdReader = Mockito.mock(ObjectIdReader.class);
        JsonDeserializer<Object> deserializer = Mockito.mock(JsonDeserializer.class);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        BeanProperty property = Mockito.mock(BeanProperty.class);
        when(property.getName()).thenReturn("propertyName");
        when(property.getDeserializer()).thenReturn(deserializer);
        when(property.getBuilder()).thenReturn(Mockito.mock(PropertyBuilder.class));
        objectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, property);
    }

    @Test
    public void testCreateObjectIdValueProperty() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertEquals("propertyName", objectIdValueProperty.getName());
        assertNull(objectIdValueProperty.getIdProperty());
        assertEquals(objectIdReader, objectIdValueProperty._objectIdReader);
        assertEquals(JsonDeserializer.class, objectIdValueProperty.getValueDeserializer().getClass());
    }

    @Test
    public void testCreateObjectIdValuePropertyWithIdProperty() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, new PropertyName("idPropertyName"));
        assertEquals("propertyName", objectIdValueProperty.getName());
        assertEquals(new PropertyName("idPropertyName"), objectIdValueProperty.getIdProperty());
        assertEquals(objectIdReader, objectIdValueProperty._objectIdReader);
        assertEquals(JsonDeserializer.class, objectIdValueProperty.getValueDeserializer().getClass());
    }

    @Test
    public void testSetName() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(new PropertyName("newPropertyName"));
        assertEquals(new PropertyName("newPropertyName"), newObjectIdValueProperty.getName());
        assertEquals(new PropertyName("newPropertyName"), newObjectIdValueProperty.getBeanProperty().getName());
    }

    @Test
    public void testSetValueDeserializer() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(Mockito.mock(JsonDeserializer.class));
        assertEquals(Mockito.mock(JsonDeserializer.class), newObjectIdValueProperty.getValueDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        assertEquals(null, objectIdValueProperty.getAnnotation(Mockito.any(Annotation.class).getClass()));
    }

    @Test
    public void testGetMember() {
        assertEquals(null, objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object id = Mockito.mock(Object.class);
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object instance = Mockito.mock(Object.class);
        when(parser.getValueAsToken()).thenReturn(Mockito.mock(JsonToken.class));
        when(context.findRootValueContainer()).thenReturn(instance);
        when(objectIdReader.getIdType()).thenReturn(Integer.class);
        when(objectIdReader.generator).thenReturn(Mockito.mock(StdSerializer.class));
        when(objectIdReader.resolver).thenReturn(Mockito.mock(ObjectIdResolver.class));
        objectIdValueProperty.deserializeAndSet(parser, context, instance);
        verify(instance).setId(id);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object id = Mockito.mock(Object.class);
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object instance = Mockito.mock(Object.class);
        when(parser.getValueAsToken()).thenReturn(Mockito.mock(JsonToken.class));
        when(context.findRootValueContainer()).thenReturn(instance);
        when(objectIdReader.getIdType()).thenReturn(Integer.class);
        when(objectIdReader.generator).thenReturn(Mockito.mock(StdSerializer.class));
        when(objectIdReader.resolver).thenReturn(Mockito.mock(ObjectIdResolver.class));
        when(objectIdValueProperty._objectIdReader.idProperty).thenReturn(Mockito.mock(SettableBeanProperty.class));
        Object result = objectIdValueProperty.deserializeSetAndReturn(parser, context, instance);
        assertEquals(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object id = Mockito.mock(Object.class);
        Object instance = Mockito.mock(Object.class);
        objectIdValueProperty.set(instance, id);
        verify(instance).setId(id);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object id = Mockito.mock(Object.class);
        Object instance = Mockito.mock(Object.class);
        Object result = objectIdValueProperty.setAndReturn(instance, id);
        assertEquals(instance, result);
    }
}
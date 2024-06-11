package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.util.*;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private PropertyMetadata metadata;
    private JsonDeserializer deserializer;
    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setUp() {
        objectIdReader = mock(ObjectIdReader.class);
        metadata = mock(PropertyMetadata.class);
        deserializer = mock(JsonDeserializer.class);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testConstructor() {
        assertEquals(objectIdReader.propertyName, objectIdValueProperty.getFullName());
        assertEquals(objectIdReader.getIdType(), objectIdValueProperty.getType());
        assertEquals(metadata, objectIdValueProperty.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), objectIdValueProperty.getValueDeserializer());
        assertEquals(objectIdReader, objectIdValueProperty._objectIdReader);
    }

    @Test
    public void testCopyConstructor() {
        ObjectIdValueProperty copy = new ObjectIdValueProperty(objectIdValueProperty, deserializer);
        assertEquals(objectIdValueProperty.getFullName(), copy.getFullName());
        assertEquals(objectIdValueProperty.getType(), copy.getType());
        assertEquals(objectIdValueProperty.getMetadata(), copy.getMetadata());
        assertEquals(deserializer, copy.getValueDeserializer());
        assertEquals(objectIdValueProperty._objectIdReader, copy._objectIdReader);
    }

    @Test
    public void testCopyConstructor_newName() {
        PropertyName newName = mock(PropertyName.class);
        ObjectIdValueProperty copy = objectIdValueProperty.withName(newName);
        assertEquals(newName, copy.getFullName());
        assertEquals(objectIdValueProperty.getType(), copy.getType());
        assertEquals(objectIdValueProperty.getMetadata(), copy.getMetadata());
        assertEquals(objectIdValueProperty.getValueDeserializer(), copy.getValueDeserializer());
        assertEquals(objectIdValueProperty._objectIdReader, copy._objectIdReader);
    }

    @Test
    public void testCopyConstructor_newDeserializer() {
        JsonDeserializer newDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty copy = objectIdValueProperty.withValueDeserializer(newDeserializer);
        assertEquals(objectIdValueProperty.getFullName(), copy.getFullName());
        assertEquals(objectIdValueProperty.getType(), copy.getType());
        assertEquals(objectIdValueProperty.getMetadata(), copy.getMetadata());
        assertEquals(newDeserializer, copy.getValueDeserializer());
        assertEquals(objectIdValueProperty._objectIdReader, copy._objectIdReader);
    }

    @Test
    public void testGetAnnotation() {
        Class<A> acls = mock(Class.class);
        assertNull(objectIdValueProperty.getAnnotation(acls));
    }

    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);

        objectIdValueProperty.deserializeAndSet(jp, ctxt, instance);

        verify(objectIdReader, times(1)).getDeserializer();
        verify(objectIdValueProperty, times(1)).deserializeSetAndReturn(jp, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);

        Object result = objectIdValueProperty.deserializeSetAndReturn(jp, ctxt, instance);

        assertNull(result);
        verify(objectIdReader, times(1)).getDeserializer();
        verify(context, times(1)).findObjectId(any(), any(), any());
    }

    @Test
    public void testSet() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        
        objectIdValueProperty.set(instance, value);
        
        verify(objectIdReader, times(1)).idProperty;
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn_noIdProperty() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);

        when(objectIdReader.idProperty).thenReturn(null);
        
        objectIdValueProperty.setAndReturn(instance, value);
    }

}
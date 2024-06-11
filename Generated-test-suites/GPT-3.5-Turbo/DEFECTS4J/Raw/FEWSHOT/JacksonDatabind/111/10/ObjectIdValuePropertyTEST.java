
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {
    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        PropertyMetadata propertyMetadata = mock(PropertyMetadata.class);

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);

        assertEquals(objectIdReader.propertyName, property.getName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertEquals(propertyMetadata, property.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());
        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testCopyConstructorWithDeserializerAndNullValueProvider() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);

        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deser, nva);

        assertEquals(src, property.getSrcProperty());
        assertEquals(deser, property.getValueDeserializer());
        assertEquals(nva, property.getNullValueProvider());
    }

    @Test
    public void testCopyConstructorWithNewName() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        PropertyName newName = mock(PropertyName.class);

        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);

        assertEquals(src, property.getSrcProperty());
        assertEquals(newName, property.getFullName());
    }

    @Test
    public void testWithName() {
        ObjectIdValueProperty property = mock(ObjectIdValueProperty.class);
        PropertyName newName = mock(PropertyName.class);

        when(property.withName(newName)).thenCallRealMethod();

        SettableBeanProperty result = property.withName(newName);

        assertEquals(newName, result.getFullName());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty property = mock(ObjectIdValueProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);

        when(property.withValueDeserializer(deser)).thenCallRealMethod();

        SettableBeanProperty result = property.withValueDeserializer(deser);

        assertEquals(deser, result.getValueDeserializer());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testWithNullProvider() {
        ObjectIdValueProperty property = mock(ObjectIdValueProperty.class);
        NullValueProvider nva = mock(NullValueProvider.class);

        when(property.withNullProvider(nva)).thenCallRealMethod();

        SettableBeanProperty result = property.withNullProvider(nva);

        assertEquals(nva, result.getNullValueProvider());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);

        assertNull(property.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);

        assertNull(property.getMember());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdValueProperty property = mock(ObjectIdValueProperty.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);

        when(property.deserializeSetAndReturn(p, ctxt, instance)).thenCallRealMethod();

        property.deserializeAndSet(p, ctxt, instance);

        verify(property).deserializeSetAndReturn(p, ctxt, instance);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdValueProperty property = mock(ObjectIdValueProperty.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);

        when(property.deserializeSetAndReturn(p, ctxt, instance)).thenCallRealMethod();

        property.deserializeSetAndReturn(p, ctxt, instance);

        verify(property).deserializeSetAndReturn(p, ctxt, instance);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSet() throws IOException {
        ObjectIdValueProperty property = mock(ObjectIdValueProperty.class);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);

        when(property.setAndReturn(instance, value)).thenCallRealMethod();

        property.set(instance, value);

        verify(property).setAndReturn(instance, value);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdValueProperty property = mock(ObjectIdValueProperty.class);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);

        when(property.setAndReturn(instance, value)).thenCallRealMethod();

        property.setAndReturn(instance, value);

        verify(property).setAndReturn(instance, value);
    }
}

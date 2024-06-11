package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;

import org.junit.Test;

public class ObjectIdValuePropertyTest {
    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        PropertyMetadata metadata = mock(PropertyMetadata.class);

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertEquals(objectIdReader.propertyName, property.getName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertEquals(metadata, property.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());
        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testCopyConstructorWithDeserializerAndNullValueProvider() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);

        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deser, nva);

        assertEquals(src, property.getOriginalProperty());
        assertEquals(deser, property.getValueDeserializer());
        assertEquals(nva, property._nullProvider);
        verify(src, times(1)).getOriginalProperty();
    }

    @Test
    public void testCopyConstructorWithNewName() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        PropertyName newName = mock(PropertyName.class);

        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);

        assertEquals(src, property.getOriginalProperty());
        assertEquals(newName, property.getFullName());
        verify(src, times(1)).getOriginalProperty();
    }

    @Test
    public void testWithName() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        PropertyName newName = mock(PropertyName.class);

        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName).withName(newName);

        assertEquals(src, property.getOriginalProperty());
        assertEquals(newName, property.getFullName());
        verify(src, times(2)).getOriginalProperty();
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);

        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deser, null).withValueDeserializer(deser);

        assertEquals(src, property.getOriginalProperty());
        assertEquals(deser, property.getValueDeserializer());
        assertNull(property._nullProvider);
        verify(src, times(2)).getOriginalProperty();
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        NullValueProvider nva = mock(NullValueProvider.class);

        ObjectIdValueProperty property = new ObjectIdValueProperty(src, null, nva).withNullProvider(nva);

        assertEquals(src, property.getOriginalProperty());
        assertNull(property.getValueDeserializer());
        assertEquals(nva, property._nullProvider);
        verify(src, times(2)).getOriginalProperty();
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

    @Test
    public void testDeserializeAndSetWithNullValue() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);

        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        Object result = property.deserializeSetAndReturn(p, ctxt, instance);

        assertNull(result);
        verify(p, times(1)).hasToken(JsonToken.VALUE_NULL);
        verify(p, never()).nextToken();
        verifyZeroInteractions(ctxt, instance);
    }

    @Test
    public void testDeserializeAndSetWithNonNullValue() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        Object id = mock(Object.class);
        ReadableObjectId roid = mock(ReadableObjectId.class);
        SettableBeanProperty idProp = mock(SettableBeanProperty.class);

        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(p.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(p.readValueAs(Object.class)).thenReturn(id);
        when(ctxt.findObjectId(id, null, null)).thenReturn(roid);
        when(idProp.setAndReturn(instance, id)).thenReturn(instance);
        property._objectIdReader = mock(ObjectIdReader.class);
        property._objectIdReader.idProperty = idProp;

        Object result = property.deserializeSetAndReturn(p, ctxt, instance);

        assertEquals(instance, result);
        verify(p, times(1)).hasToken(JsonToken.VALUE_NULL);
        verify(p, times(1)).nextToken();
        verify(p, times(1)).readValueAs(Object.class);
        verify(ctxt, times(1)).findObjectId(id, null, null);
        verify(roid, times(1)).bindItem(instance);
        verify(idProp, times(1)).setAndReturn(instance, id);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnThrowsExceptionWhenIdPropertyIsNull() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        property.setAndReturn(mock(Object.class), mock(Object.class));
    }
}
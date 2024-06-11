package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReaderMock = mock(ObjectIdReader.class);
    private SettableBeanProperty idPropertyMock = mock(SettableBeanProperty.class);

    @Test
    public void testConstructor() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReaderMock, null);
        assertSame(objectIdReaderMock, property._objectIdReader);
        assertNull(property.getDeserializer());
    }

    @Test
    public void testConstructorWithDeserializer() {
        StdSerializer<Integer> deserializerMock = mock(StdSerializer.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReaderMock, null, deserializerMock);
        assertSame(objectIdReaderMock, property._objectIdReader);
        assertSame(deserializerMock, property.getDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(new ObjectIdValueProperty(null, null).getAnnotation(Object.class));
    }

    @Test
    public void testGetMember() {
        assertNull(new ObjectIdValueProperty(null, null).getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object id = new Object();
        when(objectIdReaderMock.getIdType()).thenReturn(ObjectIdGenerator.ObjectIdType.STRING);
        JsonParser jpMock = mock(JsonParser.class);
        DeserializationContext ctxtMock = mock(DeserializationContext.class);
        when(ctxtMock.findObjectId(id, objectIdReaderMock.generator, objectIdReaderMock.resolver)).thenReturn(mock(ReadableObjectId.class));

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReaderMock, null);
        property.deserializeAndSet(jpMock, ctxtMock, new Object());

        verify(ctxtMock).findObjectId(id, objectIdReaderMock.generator, objectIdReaderMock.resolver);
        verify(objectIdReaderMock).bindItem(new Object());

        if (idPropertyMock != null) {
            verify(idPropertyMock).setAndReturn(new Object(), id);
        }
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object id = new Object();
        when(objectIdReaderMock.getIdType()).thenReturn(ObjectIdGenerator.ObjectIdType.STRING);
        JsonParser jpMock = mock(JsonParser.class);
        DeserializationContext ctxtMock = mock(DeserializationContext.class);
        Object result = new Object();
        when(idPropertyMock.setAndReturn(new Object(), id)).thenReturn(result);

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReaderMock, null, idPropertyMock);
        Object object = property.deserializeSetAndReturn(jpMock, ctxtMock, new Object());

        verify(objectIdReaderMock).bindItem(new Object());
        verify(idPropertyMock).setAndReturn(new Object(), id);
        assertSame(result, object);
    }

    @Test
    public void testSet() throws IOException {
        Object id = new Object();
        Object instance = new Object();

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReaderMock, null, idPropertyMock);
        property.set(instance, id);

        verify(idPropertyMock).setAndReturn(instance, id);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithNullIdProperty() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReaderMock, null);
        property.set(new Object(), null);
    }
}
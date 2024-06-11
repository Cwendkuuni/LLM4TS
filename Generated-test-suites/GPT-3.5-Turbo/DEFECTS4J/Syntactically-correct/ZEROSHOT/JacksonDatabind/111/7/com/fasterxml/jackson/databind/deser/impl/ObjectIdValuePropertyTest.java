package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.lang.annotation.Annotation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReaderMock;
    private PropertyMetadata metadataMock;

    @Before
    public void setUp() {
        objectIdReaderMock = mock(ObjectIdReader.class);
        metadataMock = mock(PropertyMetadata.class);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jsonParserMock = mock(JsonParser.class);
        DeserializationContext deserializationContextMock = mock(DeserializationContext.class);
        Object instanceMock = mock(Object.class);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, metadataMock);

        objectIdValueProperty.deserializeAndSet(jsonParserMock, deserializationContextMock, instanceMock);

        verify(jsonParserMock, times(1)).hasToken(JsonToken.VALUE_NULL);
        verify(_valueDeserializerMock, times(1)).deserialize(jsonParserMock, deserializationContextMock);
        verify(roidMock, times(1)).bindItem(instanceMock);
        verify(_objectIdReaderMock, times(1)).idProperty();
        verify(idPropMock, times(1)).setAndReturn(instanceMock, id);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jsonParserMock = mock(JsonParser.class);
        DeserializationContext deserializationContextMock = mock(DeserializationContext.class);
        Object instanceMock = mock(Object.class);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, metadataMock);

        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParserMock, deserializationContextMock, instanceMock);

        verify(jsonParserMock, times(1)).hasToken(JsonToken.VALUE_NULL);
        verify(_valueDeserializerMock, times(1)).deserialize(jsonParserMock, deserializationContextMock);
        verify(roidMock, times(1)).bindItem(instanceMock);
        verify(_objectIdReaderMock, times(1)).idProperty();
        verify(idPropMock, times(1)).setAndReturn(instanceMock, id);
        assertEquals(instanceMock, result);
    }

    @Test
    public void testSet() throws IOException {
        Object instanceMock = mock(Object.class);
        Object valueMock = mock(Object.class);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, metadataMock);

        objectIdValueProperty.set(instanceMock, valueMock);

        verify(_objectIdReaderMock, times(1)).idProperty();
        verify(idPropMock, times(1)).setAndReturn(instanceMock, valueMock);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn_NoIdProperty() throws IOException {
        Object instanceMock = mock(Object.class);
        Object valueMock = mock(Object.class);
        
        when(_objectIdReaderMock.idProperty()).thenReturn(null);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, metadataMock);

        objectIdValueProperty.setAndReturn(instanceMock, valueMock);
    }
}
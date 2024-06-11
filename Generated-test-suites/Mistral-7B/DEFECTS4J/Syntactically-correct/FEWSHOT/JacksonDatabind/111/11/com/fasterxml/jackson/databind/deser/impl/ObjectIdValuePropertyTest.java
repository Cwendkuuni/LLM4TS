package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReaderMock;
    private JsonDeserializer<?> valueDeserializerMock;
    private NullValueProvider nullValueProviderMock;
    private Object instance;
    private JsonParser jsonParserMock;
    private DeserializationContext deserializationContextMock;

    @Before
    public void setup() {
        objectIdReaderMock = mock(ObjectIdReader.class);
        valueDeserializerMock = mock(JsonDeserializer.class);
        nullValueProviderMock = mock(NullValueProvider.class);
        instance = new Object();
        jsonParserMock = mock(JsonParser.class);
        deserializationContextMock = mock(DeserializationContext.class);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, null);
        assertNull(objectIdValueProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, null);
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        when(objectIdReaderMock.getIdType()).thenReturn(String.class);
        when(jsonParserMock.hasToken(JsonToken.VALUE_STRING)).thenReturn(true);
        when(jsonParserMock.getValueAsString()).thenReturn("idValue");
        when(deserializationContextMock.findBeanProperty(_objectIdReaderKey)).thenReturn(objectIdReaderMock);
        when(deserializationContextMock.findObjectId(any(), any(), any())).thenReturn(mock(ReadableObjectId.class));

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, null);
        objectIdValueProperty.deserializeAndSet(jsonParserMock, deserializationContextMock, instance);

        verify(objectIdReaderMock).bindItem(instance);
    }

    @Test
    public void testDeserializeSetAndReturnNull() throws IOException {
        when(jsonParserMock.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, null);
        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParserMock, deserializationContextMock, instance);

        assertNull(result);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        when(objectIdReaderMock.getIdType()).thenReturn(String.class);
        when(jsonParserMock.hasToken(JsonToken.VALUE_STRING)).thenReturn(true);
        when(jsonParserMock.getValueAsString()).thenReturn("idValue");
        when(deserializationContextMock.findBeanProperty(_objectIdReaderKey)).thenReturn(objectIdReaderMock);
        when(deserializationContextMock.findObjectId(any(), any(), any())).thenReturn(mock(ReadableObjectId.class));
        when(_objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, null);
        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParserMock, deserializationContextMock, instance);

        verify(objectIdReaderMock).bindItem(instance);
        verify(_objectIdReader.idProperty).setAndReturn(instance, "idValue");
        assertEquals("idValue", result);
    }

    @Test
    public void testSet() throws IOException {
        when(_objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, null);
        objectIdValueProperty.set(instance, "idValue");

        verify(_objectIdReader.idProperty).setAndReturn(instance, "idValue");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws IOException {
        when(_objectIdReader.idProperty).thenReturn(null);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReaderMock, null);
        objectIdValueProperty.set(instance, "idValue");
    }
}
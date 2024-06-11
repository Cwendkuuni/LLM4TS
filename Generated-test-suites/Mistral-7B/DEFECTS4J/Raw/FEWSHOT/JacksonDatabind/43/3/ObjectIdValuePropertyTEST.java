package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueDeserializer;
import com.fasterxml.jackson.databind.node.ObjectIdNode;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReaderMock;
    private JsonDeserializer<Object> idDeserializerMock;
    private SettableBeanProperty idPropertyMock;

    @org.junit.Before
    public void setUp() {
        objectIdReaderMock = mock(ObjectIdReader.class);
        idDeserializerMock = mock(JsonDeserializer.class);
        idPropertyMock = mock(SettableBeanProperty.class);
    }

    @Test
    public void testObjectIdValuePropertyWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", IdType.STRING, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty newProperty = property.withName(new PropertyName("newName"));

        assertEquals(property, newProperty);
        assertEquals("newName", newProperty.getName());
    }

    @Test
    public void testObjectIdValuePropertyWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", IdType.STRING, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);

        assertEquals(property, newProperty);
        assertEquals(deserializer, newProperty.getValueDeserializer());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", IdType.STRING, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();

        when(objectIdReader.getIdType()).thenReturn(IdType.STRING);
        ObjectIdNode idNode = mock(ObjectIdNode.class);
        when(idNode.asText()).thenReturn("objectId");
        JsonParser parser = mock(JsonParser.class);
        when(parser.getValueAsToken()).thenReturn(idNode);

        when(objectIdReader.resolver).thenReturn(mock(JsonDeserializationContext.class));
        when(objectIdReader.generator).thenReturn(mock(JsonFactory.class));

        ObjectIdValueProperty propertySpy = spy(property);
        doCallRealMethod().when(propertySpy).deserializeAndSet(parser, mock(JsonDeserializationContext.class), instance);

        property.deserializeAndSet(parser, mock(JsonDeserializationContext.class), instance);

        verify(propertySpy).deserializeAndSet(parser, mock(JsonDeserializationContext.class), instance);
        verifyNoMoreInteractions(parser);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", IdType.STRING, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        Object id = "objectId";

        ObjectIdReader objectIdReaderSpy = spy(objectIdReader);
        doCallRealMethod().when(objectIdReaderSpy).findObjectId(id, eq(objectIdReader.generator), eq(objectIdReader.resolver));
        ReadableObjectId readableObjectId = mock(ReadableObjectId.class);
        when(readableObjectId.bindItem(instance)).thenReturn(instance);
        when(objectIdReaderSpy.findObjectId(id, eq(objectIdReader.generator), eq(objectIdReader.resolver))).thenReturn(readableObjectId);
        Object idProp = mock(SettableBeanProperty.class);

        Object result = property.deserializeSetAndReturn(mock(JsonParser.class), mock(JsonDeserializationContext.class), instance);

        assertEquals(instance, result);
        verify(objectIdReaderSpy).findObjectId(id, eq(objectIdReader.generator), eq(objectIdReader.resolver));
        verify(readableObjectId).bindItem(instance);
        verify(idProp).setAndReturn(instance, id);
        verifyNoMoreInteractions(parser);
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", IdType.STRING, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        Object id = "objectId";

        property.set(instance, id);

        SettableBeanProperty idProp = property.getIdProperty();
        idProp.setAndReturn(instance, id);
        verify(idProp).setAndReturn(instance, id);
    }

    @Test
    public void testSetAndReturnNull() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", IdType.STRING, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        assertNull(property.setAndReturn(new Object(), null));
    }
}
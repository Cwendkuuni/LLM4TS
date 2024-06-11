package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectIdNode;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader mockObjectIdReader() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        IdType idType = IdType.LONG;
        when(objectIdReader.getIdType()).thenReturn(idType);
        String propertyName = "objectIdPropertyName";
        when(objectIdReader.propertyName).thenReturn(propertyName);
        ValueDeserializer idDeserializer = mock(ValueDeserializer.class);
        when(objectIdReader.getIdProperty()).thenReturn(new SettableBeanProperty(mock(JsonDeserializer.class), propertyName));
        when(objectIdReader.getIdType()).thenReturn(idType);
        when(objectIdReader.getDeserializer()).thenReturn(idDeserializer);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        return objectIdReader;
    }

    @Test
    public void testObjectIdValueProperty() {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        assertEquals("objectIdPropertyName", objectIdValueProperty.getName());
        assertEquals(objectIdReader, objectIdValueProperty.getParent());
        assertEquals(objectIdReader.getIdType(), objectIdValueProperty.getIdType());
        assertEquals(objectIdReader.getDeserializer(), objectIdValueProperty.getDeserializer());
        assertNull(objectIdValueProperty.getAnnotation(JsonDeserialize.class));
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testGetAnnotation() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        assertNull(objectIdValueProperty.getAnnotation(JsonSerializable.class));
    }

    @Test
    public void testGetMember() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        ObjectIdNode idNode = JsonMapper.getDefaultInstance().readTree("123");
        when(jsonParser.getCurrentName()).thenReturn("objectIdPropertyName");
        when(jsonParser.nextValue()).thenReturn(idNode);
        when(jsonParser.getValueAsInt()).thenReturn(123);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_OBJECT);

        when(deserializationContext.findObjectId(eq(123L), any(), any()))
                .thenReturn(mock(ReadableObjectId.class));

        objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        // verify that ObjectId was properly bound to the instance
        ReadableObjectId readableObjectId = mock(ReadableObjectId.class);
        when(readableObjectId.getId()).thenReturn(123L);
        when(readableObjectId.getPropertyName()).thenReturn("objectIdPropertyName");
        when(objectIdReader.getIdProperty()).thenReturn(new SettableBeanProperty(mock(JsonDeserializer.class), "idPropertyName"));

        SettableBeanProperty idProperty = (SettableBeanProperty) objectIdValueProperty.getParent().getIdProperty();
        assertEquals(123L, idProperty.get(instance));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);

        Object instance = new Object();
        Object id = new Object();

        SettableBeanProperty idProperty = mock(SettableBeanProperty.class);
        when(idProperty.setAndReturn(instance, id)).thenReturn(instance);

        when(objectIdReader.getIdProperty()).thenReturn(idProperty);

        objectIdValueProperty.setAndReturn(instance, id);

        assertEquals(instance, objectIdValueProperty.getParent().getIdProperty().get(instance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetNullProperty() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);

        Object instance = new Object();

        objectIdValueProperty.set(instance, null);

        throw new UnsupportedOperationException("Should not call set() on ObjectIdProperty that has no SettableBeanProperty");
    }
}
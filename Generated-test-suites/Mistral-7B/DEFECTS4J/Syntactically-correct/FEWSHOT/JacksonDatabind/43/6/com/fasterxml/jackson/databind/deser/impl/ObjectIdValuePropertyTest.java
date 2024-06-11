package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class ObjectIdValuePropertyTest {

    private static ObjectIdReader mockObjectIdReader() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn("propertyName");
        when(objectIdReader.getIdType()).thenReturn(IdType.STRING);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));
        when(objectIdReader.getIdType().getValueClass()).thenReturn(String.class);
        when(objectIdReader.getValueDeserializer()).thenReturn(mock(JsonDeserializer.class));
        return objectIdReader;
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testObjectIdValuePropertyCreation() {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        assertEquals("propertyName", objectIdValueProperty.getName());
        assertEquals(IdType.STRING, objectIdValueProperty.getIdType());
        assertNull(objectIdValueProperty.getMember());
        assertNull(objectIdValueProperty.getAnnotation(Mockito.any(Annotation.class)));
        assertEquals(objectIdReader, objectIdValueProperty.getObjectIdReader());
        assertEquals(null, objectIdValueProperty.getIdProperty());
        assertEquals(null, objectIdValueProperty.getValueDeserializer());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        Object id = "idValue";

        when(jsonParser.getValueAsToken()).thenReturn(mock(JsonToken.VALUE_STRING.class));
        when(jsonParser.getText()).thenReturn(id);

        when(deserializationContext.findObjectId(id, mock(ObjectIdGenerator.class), mock(ObjectIdResolver.class))).thenReturn(mock(ReadableObjectId.class));

        objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        assertTrue(instance instanceof ReadableObjectId);
        assertTrue(((ReadableObjectId) instance).isValidId());
        assertEquals(id, ((ReadableObjectId) instance).getId());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        Object id = "idValue";

        when(jsonParser.getValueAsToken()).thenReturn(mock(JsonToken.VALUE_STRING.class));
        when(jsonParser.getText()).thenReturn(id);

        when(deserializationContext.findObjectId(id, mock(ObjectIdGenerator.class), mock(ObjectIdResolver.class))).thenReturn(mock(ReadableObjectId.class));

        Object returnedObject = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        assertEquals(instance, returnedObject);
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = mock(Object.class);
        Object id = "idValue";

        objectIdValueProperty.set(instance, id);

        assertTrue(instance instanceof ReadableObjectId);
        assertTrue(((ReadableObjectId) instance).isValidId());
        assertEquals(id, ((ReadableObjectId) instance).getId());
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = mock(Object.class);
        Object id = "idValue";

        when(objectIdValueProperty.getIdProperty()).thenReturn(mock(SettableBeanProperty.class));

        Object returnedObject = objectIdValueProperty.setAndReturn(instance, id);

        assertEquals(instance, returnedObject);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWhenIdPropertyIsNull() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);

        objectIdValueProperty.setAndReturn(null, null);
    }

    @Test
    public void testGetValueDeserializer() {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);

        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);

        when(objectIdReader.getValueDeserializer()).thenReturn(valueDeserializer);

        assertEquals(valueDeserializer, objectIdValueProperty.getValueDeserializer());
    }
}
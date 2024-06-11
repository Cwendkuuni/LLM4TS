package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;

import org.junit.Test;

/**
 * Test class for ObjectIdValueProperty
 */
public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
    private PropertyName propertyName = mock(PropertyName.class);
    private JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
    private NullValueProvider nullProvider = mock(NullValueProvider.class);
    private ObjectIdValueProperty objectIdValueProperty;

    @Test
    public void testObjectIdValuePropertyConstructor() throws IOException {
        when(objectIdReader.propertyName).thenReturn(propertyName);
        when(objectIdReader.getIdType()).thenReturn(String.class);

        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertSame(propertyName, objectIdValueProperty.getName());
        assertSame(objectIdReader.getClass(), objectIdValueProperty._objectIdReader.getClass());
        assertNull(objectIdValueProperty.getValueDeserializer());
        assertNull(objectIdValueProperty.NullValueProvider());
    }

    @Test
    public void testObjectIdValuePropertyWithValueDeserializerConstructor() throws IOException {
        when(objectIdReader.propertyName).thenReturn(propertyName);
        when(objectIdReader.getIdType()).thenReturn(String.class);

        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, valueDeserializer, nullProvider);
        assertSame(propertyName, objectIdValueProperty.getName());
        assertSame(objectIdReader.getClass(), objectIdValueProperty._objectIdReader.getClass());
        assertSame(valueDeserializer, objectIdValueProperty.getValueDeserializer());
        assertSame(nullProvider, objectIdValueProperty.NullValueProvider());
    }

    @Test
    public void testObjectIdValuePropertyWithNameConstructor() throws IOException {
        when(objectIdReader.propertyName).thenReturn(propertyName);
        when(objectIdReader.getIdType()).thenReturn(String.class);

        PropertyName newName = mock(PropertyName.class);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, newName);
        assertSame(newName, objectIdValueProperty.getName());
        assertSame(objectIdReader.getClass(), objectIdValueProperty._objectIdReader.getClass());
        assertSame(objectIdValueProperty.getValueDeserializer(), objectIdValueProperty.getValueDeserializer());
        assertSame(objectIdValueProperty.NullValueProvider(), objectIdValueProperty.NullValueProvider());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetNullValueDeserializer() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, valueDeserializer, nullProvider);
        objectIdValueProperty.withValueDeserializer(null);
    }

    @Test
    public void testGetAnnotation() throws IOException {
        assertNull(objectIdValueProperty.getAnnotation(String.class));
    }

    @Test
    public void testGetMember() throws IOException {
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object id = "testId";
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        when(jsonParser.getCurrentName()).thenReturn(propertyName.getDecimalValue().toString());
        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getValueAsString()).thenReturn(id);
        when(valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(id);

        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, valueDeserializer, nullProvider);
        objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        assertSame(instance, objectIdValueProperty.getLeafProperty().get(instance));
        assertSame(id, objectIdValueProperty.getLeafProperty().get(instance));
        assertSame(instance, objectIdValueProperty.getValueId());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object id = "testId";
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        when(jsonParser.getCurrentName()).thenReturn(propertyName.getDecimalValue().toString());
        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getValueAsString()).thenReturn(id);
        when(valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(id);

        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, valueDeserializer, nullProvider);
        Object returnValue = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        assertSame(instance, returnValue);
        assertSame(instance, objectIdValueProperty.getLeafProperty().get(instance));
        assertSame(id, objectIdValueProperty.getLeafProperty().get(instance));
        assertSame(instance, objectIdValueProperty.getValueId());
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object id = "testId";

        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, valueDeserializer, nullProvider);
        objectIdValueProperty.set(instance, id);

        assertSame(instance, objectIdValueProperty.getValueId());
        assertSame(id, objectIdValueProperty.getLeafProperty().get(instance));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object id = "testId";

        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, valueDeserializer, nullProvider);
        Object returnValue = objectIdValueProperty.setAndReturn(instance, id);

        assertSame(instance, returnValue);
        assertSame(instance, objectIdValueProperty.getValueId());
        assertSame(id, objectIdValueProperty.getLeafProperty().get(instance));
    }
}
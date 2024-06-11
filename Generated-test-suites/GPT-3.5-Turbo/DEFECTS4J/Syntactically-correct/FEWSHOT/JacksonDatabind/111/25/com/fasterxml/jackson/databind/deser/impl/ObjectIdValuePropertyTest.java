package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

import org.mockito.Mockito;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {
    private ObjectIdValueProperty objectIdValueProperty;
    private ObjectIdReader objectIdReader;
    private PropertyMetadata propertyMetadata;
    private JsonDeserializer<?> deserializer;
    private NullValueProvider nullValueProvider;
    private PropertyName newName;
    private JsonDeserializer<?> valueDeserializer;
    private CommonContext ctxt;
    private Object instance;
    private ReadableObjectId roid;
    private SettableBeanProperty idProp;

    @Before
    public void setUp() {
        objectIdReader = mock(ObjectIdReader.class);
        propertyMetadata = mock(PropertyMetadata.class);
        deserializer = mock(JsonDeserializer.class);
        nullValueProvider = mock(NullValueProvider.class);
        newName = mock(PropertyName.class);
        valueDeserializer = mock(JsonDeserializer.class);
        ctxt = mock(CommonContext.class);
        instance = mock(Object.class);
        roid = mock(ReadableObjectId.class);
        idProp = mock(SettableBeanProperty.class);

        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
    }

    @Test
    public void testWithNullValueProvider() {
        ObjectIdValueProperty result = objectIdValueProperty.withNullProvider(nullValueProvider);
        assertEquals(result, objectIdValueProperty);
    }

    @Test
    public void testWithDifferentDeserializer() {
        ObjectIdValueProperty newObjectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, deserializer, nullValueProvider);
        ObjectIdValueProperty result = newObjectIdValueProperty.withValueDeserializer(valueDeserializer);
        assertEquals(result.getValueDeserializer(), valueDeserializer);
    }

    @Test
    public void testWithSameDeserializer() {
        ObjectIdValueProperty newObjectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, deserializer, nullValueProvider);
        ObjectIdValueProperty result = newObjectIdValueProperty.withValueDeserializer(null);
        assertEquals(result, newObjectIdValueProperty);
    }

    @Test
    public void testWithDifferentName() {
        ObjectIdValueProperty result = objectIdValueProperty.withName(newName);
        assertEquals(result.getFullName(), newName);
    }

    @Test
    public void testDeserializeSetAndReturnNullToken() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        assertNull(objectIdValueProperty.deserializeSetAndReturn(p, context, instance));
    }

    @Test
    public void testDeserializeSetAndReturnNonNullToken() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object id = mock(Object.class);

        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(deserializer.deserialize(p, context)).thenReturn(id);
        when(context.findObjectId(id, objectIdReader.generator, objectIdReader.resolver)).thenReturn(roid);
        when(roid.getBindItem()).thenReturn(instance);
        when(objectIdReader.idProperty).thenReturn(idProp);

        assertEquals(instance, objectIdValueProperty.deserializeSetAndReturn(p, context, instance));
        verify(idProp, times(1)).setAndReturn(instance, id);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithNullIdProperty() throws IOException {
        objectIdValueProperty.setAndReturn(instance, null);
    }

    @Test
    public void testSetAndReturnWithNonNullIdProperty() throws IOException {
        when(objectIdReader.idProperty).thenReturn(idProp);
        assertEquals(instance, objectIdValueProperty.setAndReturn(instance, null));
        verify(idProp, times(1)).setAndReturn(instance, null);
    }
}
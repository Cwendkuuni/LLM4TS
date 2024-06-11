package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), valueDeserializer);

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        Object id = "someId";
        when(valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(id);

        ReadableObjectId readableObjectId = mock(ReadableObjectId.class);
        when(objectIdReader.generator).thenReturn(readableObjectId);
        when(objectIdReader.resolver).thenReturn(readableObjectId);
        when(readableObjectId.bindItem(instance)).thenReturn(instance);
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(readableObjectId).bindItem(instance);
        verify(objectIdReader.idProperty).setAndReturn(instance, id);
    }

    @Test
    public void testDeserializeSetAndReturnNull() throws Exception {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        Assert.assertNull(instance);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), mock(JsonDeserializer.class), mock(NullValueProvider.class));
        Object instance = new Object();
        Object value = "someValue";

        SettableBeanProperty idProperty = mock(SettableBeanProperty.class);
        when(objectIdReader.idProperty).thenReturn(idProperty);

        Object result = objectIdValueProperty.setAndReturn(instance, value);

        verify(idProperty).setAndReturn(instance, value);
        Assert.assertEquals(instance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetShouldNotBeCalled() throws Exception {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), mock(JsonDeserializer.class), mock(NullValueProvider.class));
        Object instance = new Object();
        Object value = "someValue";

        objectIdValueProperty.set(instance, value);
    }
}
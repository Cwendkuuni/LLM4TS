package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader.IdType;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentName()).thenReturn("idPropertyName");

        Object id = new Object();
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        when(valueDeserializer.deserialize(parser, any(DeserializationContext.class))).thenReturn(id);

        DeserializationContext context = mock(DeserializationContext.class);
        when(context.findObjectId(any(), any(), any()))
                .thenReturn(mock(ReadableObjectId.class));
        when(context.findBeanProperties(any(), any(), any()))
                .thenReturn(new PropertyName[]{new PropertyName("idPropertyName")});

        SettableBeanProperty idProperty = mock(SettableBeanProperty.class);
        when(idProperty.setAndReturn(any(), any()))
                .thenReturn(new Object());

        Object instance = new Object();

        property.deserializeAndSet(parser, context, instance);

        verify(idProperty).setAndReturn(instance, id);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentName()).thenReturn("idPropertyName");
        when(parser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getText()).thenReturn("idValue");

        Object id = new Object();
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        when(valueDeserializer.deserialize(parser, any(DeserializationContext.class))).thenReturn(id);

        DeserializationContext context = mock(DeserializationContext.class);
        when(context.findObjectId(eq(id), any(), any()))
                .thenReturn(mock(ReadableObjectId.class));
        when(context.findBeanProperties(any(), any(), any()))
                .thenReturn(new PropertyName[]{new PropertyName("idPropertyName")});

        SettableBeanProperty idProperty = mock(SettableBeanProperty.class);
        when(idProperty.setAndReturn(any(), any()))
                .thenReturn(new Object());

        Object instance = new Object();

        Assert.assertEquals(instance, property.deserializeSetAndReturn(parser, context, instance));
        verify(idProperty).setAndReturn(instance, id);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        Object instance = new Object();
        Object value = new Object();

        SettableBeanProperty idProperty = mock(SettableBeanProperty.class);
        when(idProperty.setAndReturn(instance, value))
                .thenReturn(new Object());

        Assert.assertEquals(instance, property.setAndReturn(instance, value));
        verify(idProperty).setAndReturn(instance, value);
    }

    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", IdType.LONG, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        PropertyName newName = new PropertyName("newName");

        ObjectIdValueProperty newProperty = property.withName(newName);

        Assert.assertEquals(newName, newProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", IdType.LONG, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {};

        ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);

        Assert.assertEquals(deserializer, newProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", IdType.LONG, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        NullValueProvider nva = new NullValueProvider() {};

        ObjectIdValueProperty newProperty = property.withNullProvider(nva);

        Assert.assertEquals(nva, newProperty.getNullProvider());
    }
}
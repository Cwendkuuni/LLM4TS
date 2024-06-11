package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private JsonDeserializer<ObjectIdNode> objectIdDeserializer;
    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setup() {
        objectIdReader = Mockito.mock(ObjectIdReader.class);
        objectIdDeserializer = Mockito.mock(JsonDeserializer.class);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader,
                new PropertyMetadata(), objectIdDeserializer);
    }

    @Test
    public void testGetAnnotation() {
        Assert.assertNull(objectIdValueProperty.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        Assert.assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        String json = "{\"id\":1}";
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        Mockito.when(jsonParser.getValueAsToken()).thenReturn(JsonToken.START_OBJECT);
        Mockito.when(jsonParser.nextFieldName()).thenReturn("id");
        Mockito.when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        Mockito.when(jsonParser.getValueAsInt()).thenReturn(1);
        Mockito.when(jsonParser.nextValue()).thenReturn(null);

        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();

        objectIdValueProperty.deserializeAndSet(jsonParser, context, instance);

        Field idField = ReflectionUtils.findField(instance.getClass(), "id");
        Assert.assertEquals(1, idField.get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        String json = "{\"id\":1}";
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        Mockito.when(jsonParser.getValueAsToken()).thenReturn(JsonToken.START_OBJECT);
        Mockito.when(jsonParser.nextFieldName()).thenReturn("id");
        Mockito.when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        Mockito.when(jsonParser.getValueAsInt()).thenReturn(1);
        Mockito.when(jsonParser.nextValue()).thenReturn(null);

        Object id = objectIdDeserializer.deserialize(jsonParser, Mockito.mock(DeserializationContext.class));
        ObjectIdNode objectIdNode = Mockito.mock(ObjectIdNode.class);
        Mockito.when(objectIdNode.asInt()).thenReturn(1);
        Mockito.when(objectIdDeserializer.deserialize(jsonParser, Mockito.mock(DeserializationContext.class))).thenReturn(objectIdNode);

        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();

        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, context, instance);

        Assert.assertEquals(instance, result);
        Assert.assertEquals(1, ReflectionUtils.getFieldValue(instance, "id"));
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        int id = 1;

        objectIdValueProperty.set(instance, id);

        Assert.assertEquals(id, ReflectionUtils.getFieldValue(instance, "id"));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        int id = 1;

        Object result = objectIdValueProperty.setAndReturn(instance, id);

        Assert.assertEquals(instance, result);
        Assert.assertEquals(id, ReflectionUtils.getFieldValue(instance, "id"));
    }
}
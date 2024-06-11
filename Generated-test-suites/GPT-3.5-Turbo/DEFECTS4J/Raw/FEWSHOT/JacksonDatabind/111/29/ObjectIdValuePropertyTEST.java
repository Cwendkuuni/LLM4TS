package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo.Property;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfoWrapper;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfoWrapperProperty;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private SettableBeanProperty idProperty;
    private JsonDeserializer<?> valueDeserializer;

    @Before
    public void setUp() {
        idProperty = mock(SettableBeanProperty.class);
        objectIdReader = new ObjectIdInfoWrapperProperty(new ObjectIdInfo.Property("idProperty", idProperty));
        valueDeserializer = mock(JsonDeserializer.class);
    }

    @Test
    public void testConstructor() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testCopyConstructorWithDeserializer() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src,
                valueDeserializer, null);
        assertEquals(valueDeserializer, property._valueDeserializer);
        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testCopyConstructorWithPropertyName() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src,
                new com.fasterxml.jackson.databind.PropertyName("newName"));
        assertEquals(objectIdReader, property._objectIdReader);
        String expected = "newName";
        assertEquals(expected, property.getPropertyName().getSimpleName());
    }

    @Test
    public void testOverrideWithName() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, null);
        ObjectIdValueProperty property = (ObjectIdValueProperty) src.withName(new com.fasterxml.jackson.databind.PropertyName("newName"));
        assertEquals(objectIdReader, property._objectIdReader);
        String expected = "newName";
        assertEquals(expected, property.getPropertyName().getSimpleName());
    }

    @Test
    public void testOverrideWithValueDeserializer() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, null);
        ObjectIdValueProperty property = (ObjectIdValueProperty) src.withValueDeserializer(valueDeserializer);
        assertEquals(valueDeserializer, property._valueDeserializer);
        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testOverrideWithNullProvider() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, null);
        ObjectIdValueProperty property = (ObjectIdValueProperty) src.withNullProvider(null);
        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        assertNull(property.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSetWithNullValue() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object();
        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        assertNull(property.deserializeSetAndReturn(parser, context, instance));
        verifyZeroInteractions(context);
    }

    @Test
    public void testDeserializeAndSetWithNonNullValue() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object();
        Object idValue = new Object();
        ReadableObjectId roid = mock(ReadableObjectId.class);
        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(valueDeserializer.deserialize(parser, context)).thenReturn(idValue);
        when(context.findObjectId(idValue, objectIdReader.generator, objectIdReader.resolver)).thenReturn(roid);
        property.deserializeAndSet(parser, context, instance);
        verify(roid).bindItem(instance);
        verifyZeroInteractions(idProperty);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithNoIdProperty() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        property.setAndReturn(new Object(), new Object());
    }

    @Test
    public void testSetAndReturnWithIdProperty() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        SettableBeanProperty idProp = mock(SettableBeanProperty.class);
        when(idProp.setAndReturn(any(), any())).thenReturn(new Object());
        when(objectIdReader.idProperty).thenReturn(idProp);

        Object instance = new Object();
        Object idValue = new Object();

        ReadableObjectId roid = mock(ReadableObjectId.class);
        when(context.findObjectId(idValue, objectIdReader.generator, objectIdReader.resolver)).thenReturn(roid);

        Object obj = property.setAndReturn(instance, idValue);
        verify(idProp).setAndReturn(instance, idValue);
        assertEquals(instance, obj);
    }
}
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdGenerator;
import com.fasterxml.jackson.databind.node.ObjectIdResolver;
import com.fasterxml.jackson.databind.ser.NullValueProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Unit tests for {@link ObjectIdValueProperty} class.
 */
public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setUp() {
        objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("objectId"));
        when(objectIdReader.getIdType()).thenReturn(Integer.class);
        when(objectIdReader.getDeserializer()).thenReturn(mock(JsonDeserializer.class));
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));

        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
    }

    @Test
    public void testDefaultConstructor() {
        assertNull(objectIdValueProperty.getAnnotation(ObjectId.class));
        assertSame(objectIdReader.propertyName, objectIdValueProperty.getName());
        assertSame(Integer.class, objectIdValueProperty.getType());
        assertNull(objectIdValueProperty.getDeserializer());
        assertSame(objectIdReader._objectIdReader, objectIdValueProperty._objectIdReader);
    }

    @Test
    public void testCopyConstructor() throws IOException {
        JsonDeserializer<Object> jsonDeserializer = mock(JsonDeserializer.class);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);

        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, jsonDeserializer, nullValueProvider);
        ObjectIdValueProperty deserialized = new ObjectIdValueProperty(src, nullValueProvider);

        assertSame(src._objectIdReader, deserialized._objectIdReader);
        assertSame(src.getType(), deserialized.getType());
        assertSame(src.getName(), deserialized.getName());
        assertSame(src.getMetadata(), deserialized.getMetadata());
        assertSame(src.getDeserializer(), deserialized.getDeserializer());
    }

    @Test
    public void testWithName() {
        SettableBeanProperty property = mock(SettableBeanProperty.class);

        ObjectIdValueProperty result = objectIdValueProperty.withName(new PropertyName("newName"));

        assertSame(new PropertyName("newName"), result.getName());
        assertSame(objectIdReader, result._objectIdReader);
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<Object> jsonDeserializer = mock(JsonDeserializer.class);

        ObjectIdValueProperty result = objectIdValueProperty.withValueDeserializer(jsonDeserializer);

        assertSame(jsonDeserializer, result.getDeserializer());
        assertSame(objectIdReader, result._objectIdReader);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);

        ObjectIdValueProperty result = objectIdValueProperty.withNullProvider(nullValueProvider);

        assertSame(nullValueProvider, result._nullProvider);
        assertSame(objectIdReader, result._objectIdReader);
    }

    @Test
    public void testGetAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        AnnotatedMember member = mock(AnnotatedMember.class);

        ObjectIdValueProperty result = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), member);

        assertSame(member, result.getMember());
        assertSame(objectIdReader, result._objectIdReader);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        ArgumentCaptor<JsonParser> parserArgumentCaptor = ArgumentCaptor.forClass(JsonParser.class);
        ArgumentCaptor<DeserializationContext> ctxtArgumentCaptor = ArgumentCaptor.forClass(DeserializationContext.class);
        ArgumentCaptor<Object> instanceArgumentCaptor = ArgumentCaptor.forClass(Object.class);

        when(jsonParser.hasToken(JsonToken.VALUE_NUMBER_INT)).thenReturn(true);
        when(jsonParser.getParserNextValueAsInt()).thenReturn(1);

        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(deserializationContext).findObjectId(1, objectIdReader.generator, objectIdReader.resolver);
        verify(objectIdReader.idProperty).bindItem(instance);
        verify(objectIdReader.idProperty).setAndReturn(instance, 1);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        ArgumentCaptor<JsonParser> parserArgumentCaptor = ArgumentCaptor.forClass(JsonParser.class);
        ArgumentCaptor<DeserializationContext> ctxtArgumentCaptor = ArgumentCaptor.forClass(DeserializationContext.class);
        ArgumentCaptor<Object> instanceArgumentCaptor = ArgumentCaptor.forClass(Object.class);

        when(jsonParser.hasToken(JsonToken.VALUE_NUMBER_INT)).thenReturn(true);
        when(jsonParser.getParserNextValueAsInt()).thenReturn(1);

        Object id = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        verify(deserializationContext).findObjectId(1, objectIdReader.generator, objectIdReader.resolver);
        verify(objectIdReader.idProperty).bindItem(instance);
        verify(objectIdReader.idProperty).setAndReturn(instance, 1);
        assertSame(instance, id);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        Object result = objectIdValueProperty.setAndReturn(instance, value);

        verify(objectIdReader.idProperty).setAndReturn(instance, value);
        assertSame(instance, result);
    }
}
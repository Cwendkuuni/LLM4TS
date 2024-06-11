package com.fasterxml.jackson.databind.deser.impl;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ObjectIdValuePropertyTest {
   
    private ObjectIdValueProperty objectIdValueProperty;

    @Mock
    private ObjectIdReader objectIdReader;

    @Mock
    private PropertyMetadata propertyMetadata;

    @Mock
    private JsonDeserializer<?> deserializer;

    @Mock
    private Object instance;

    @Mock
    private JsonParser parser;

    @Mock
    private DeserializationContext context;

    @Mock
    private ReadableObjectId readableObjectId;

    @Mock
    private SettableBeanProperty idProperty;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nullValueProvider = Mockito.mock(NullValueProvider.class);
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withNullProvider(nullValueProvider);
        Assert.assertEquals(nullValueProvider, newObjectIdValueProperty.nullProvider());
    }

    @Test
    public void testWithAnnotation() {
        Annotation annotation = Mockito.mock(Annotation.class);
        Annotation newAnnotation = objectIdValueProperty.withAnnotation(annotation);
        Assert.assertEquals(annotation, newAnnotation);
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(deserializer);
        Assert.assertEquals(deserializer, newObjectIdValueProperty.valueDeserializer());
    }

    @Test
    public void testWithSimpleName() {
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withSimpleName(newName);
        Assert.assertEquals(newName, newObjectIdValueProperty.getFullName());
    }

    @Test
    public void testWithMember() {
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withMember(member);
        Assert.assertEquals(member, newObjectIdValueProperty.getMember());
    }

    @Test
    public void testGetAnnotation() {
        Class<Annotation> acls = Annotation.class;
        Assert.assertNull(objectIdValueProperty.getAnnotation(acls));
    }

    @Test
    public void testDeserializeAndSet_NullId() throws IOException {
        Mockito.when(parser.hasToken(JsonToken.VALUE_NULL))
            .thenReturn(true);

        Object result = objectIdValueProperty.deserializeSetAndReturn(parser, context, instance);
        Assert.assertNull(result);
        Mockito.verifyZeroInteractions(context);
    }

    @Test
    public void testDeserializeAndSet_WithIdProperty() throws IOException {
        Object id = new Object();
        Mockito.when(parser.hasToken(JsonToken.VALUE_NULL))
            .thenReturn(false);
        Mockito.when(deserializer.deserialize(parser, context))
            .thenReturn(id);
        Mockito.when(context.findObjectId(id, objectIdReader.generator, objectIdReader.resolver))
            .thenReturn(readableObjectId);
        Mockito.when(idProperty.setAndReturn(instance, id))
            .thenReturn(instance);

        objectIdValueProperty._valueDeserializer = deserializer;
        objectIdValueProperty._objectIdReader = objectIdReader;
        objectIdValueProperty._objectIdReader.idProperty = idProperty;

        Object result = objectIdValueProperty.deserializeSetAndReturn(parser, context, instance);
        Assert.assertEquals(instance, result);
        Mockito.verify(readableObjectId).bindItem(instance);
        Mockito.verifyZeroInteractions(context);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn_NoIdProperty() throws IOException {
        objectIdValueProperty.setAndReturn(instance, new Object());
    }

    @Test
    public void testSetAndReturn_WithIdProperty() throws IOException {
        Object value = new Object();
        Mockito.when(idProperty.setAndReturn(instance, value))
            .thenReturn(instance);

        objectIdValueProperty._objectIdReader = objectIdReader;
        objectIdValueProperty._objectIdReader.idProperty = idProperty;

        Object result = objectIdValueProperty.setAndReturn(instance, value);
        Assert.assertEquals(instance, result);
    }
}
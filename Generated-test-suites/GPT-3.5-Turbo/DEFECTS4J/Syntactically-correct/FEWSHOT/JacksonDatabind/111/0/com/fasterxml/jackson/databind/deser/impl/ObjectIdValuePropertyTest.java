package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.PropertyMetadata;
import com.fasterxml.jackson.databind.deser.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.ObjectIdReader;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty objectIdValueProperty;
    private ObjectIdReader objectIdReader;
    private PropertyMetadata propertyMetadata;
    private JsonDeserializer<?> deserializer;
    private NullValueProvider nullValueProvider;

    @Before
    public void setUp() {
        objectIdReader = mock(ObjectIdReader.class);
        propertyMetadata = mock(PropertyMetadata.class);
        deserializer = mock(JsonDeserializer.class);
        nullValueProvider = mock(NullValueProvider.class);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
    }

    @Test
    public void testConstructor() {
        assertEquals(objectIdReader.propertyName, objectIdValueProperty.getName());
        assertEquals(objectIdReader.getIdType(), objectIdValueProperty.getType());
        assertEquals(propertyMetadata, objectIdValueProperty.getMetadata());
        assertEquals(deserializer, objectIdValueProperty.getValueDeserializer());
        assertEquals(objectIdReader, objectIdValueProperty._objectIdReader);
    }

    @Test
    public void testConstructorWithDeserializerAndNullValueProvider() {
        objectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, deserializer, nullValueProvider);
        assertEquals(objectIdReader.propertyName, objectIdValueProperty.getName());
        assertEquals(objectIdReader.getIdType(), objectIdValueProperty.getType());
        assertEquals(propertyMetadata, objectIdValueProperty.getMetadata());
        assertEquals(deserializer, objectIdValueProperty.getValueDeserializer());
        assertEquals(nullValueProvider, objectIdValueProperty._nullProvider);
    }

    @Test
    public void testConstructorWithNewName() {
        PropertyName newName = mock(PropertyName.class);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, newName);
        assertEquals(newName, objectIdValueProperty.getName());
        assertEquals(objectIdReader.getIdType(), objectIdValueProperty.getType());
        assertEquals(propertyMetadata, objectIdValueProperty.getMetadata());
        assertEquals(deserializer, objectIdValueProperty.getValueDeserializer());
        assertEquals(objectIdReader, objectIdValueProperty._objectIdReader);
    }

    @Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);
        SettableBeanProperty result = objectIdValueProperty.withName(newName);
        assertEquals(newName, result.getName());
        assertEquals(objectIdReader.getIdType(), result.getType());
        assertEquals(propertyMetadata, result.getMetadata());
        assertEquals(deserializer, result.getValueDeserializer());
        assertEquals(objectIdReader, ((ObjectIdValueProperty)result)._objectIdReader);
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> newDeserializer = mock(JsonDeserializer.class);
        SettableBeanProperty result = objectIdValueProperty.withValueDeserializer(newDeserializer);
        assertEquals(objectIdReader.propertyName, result.getName());
        assertEquals(objectIdReader.getIdType(), result.getType());
        assertEquals(propertyMetadata, result.getMetadata());
        assertEquals(newDeserializer, result.getValueDeserializer());
        assertEquals(objectIdReader, ((ObjectIdValueProperty)result)._objectIdReader);
    }

    @Test
    public void testWithNullProvider() {
        SettableBeanProperty result = objectIdValueProperty.withNullProvider(nullValueProvider);
        assertEquals(objectIdReader.propertyName, result.getName());
        assertEquals(objectIdReader.getIdType(), result.getType());
        assertEquals(propertyMetadata, result.getMetadata());
        assertEquals(deserializer, result.getValueDeserializer());
        assertEquals(nullValueProvider, ((ObjectIdValueProperty)result)._nullProvider);
    }

    @Test
    public void testGetAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(Object.class));
    }

    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        objectIdValueProperty.deserializeAndSet(p, ctxt, instance);
        verify(objectIdValueProperty).deserializeSetAndReturn(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturnWithNullId() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        assertNull(objectIdValueProperty.deserializeSetAndReturn(p, ctxt, instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        Object id = mock(Object.class);
        ReadableObjectId roid = mock(ReadableObjectId.class);
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(_valueDeserializer.deserialize(p, ctxt)).thenReturn(id);
        when(ctxt.findObjectId(eq(id), any(ObjectIdGenerator.class), any(ObjectIdResolver.class))).thenReturn(roid);
        objectIdValueProperty.deserializeSetAndReturn(p, ctxt, instance);
        verify(roid).bindItem(instance);
        verify(objectIdValueProperty, times(2)).setAndReturn(instance, id);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithNoIdProperty() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        when(_objectIdReader.idProperty).thenReturn(null);
        objectIdValueProperty.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        SettableBeanProperty idProp = mock(SettableBeanProperty.class);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        when(_objectIdReader.idProperty).thenReturn(idProp);
        when(idProp.setAndReturn(instance, value)).thenReturn(instance);
        assertEquals(instance, objectIdValueProperty.setAndReturn(instance, value));
    }
}
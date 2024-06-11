package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.node.ObjectIdReader.IdType.StringIdType;
import com.fasterxml.jackson.databind.ser.NullValueProvider;
import com.fasterxml.jackson.databind.util.BeanUtil;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link ObjectIdValueProperty} class of package {@code com.fasterxml.jackson.databind.deser.impl}.
 */
public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private ObjectIdValueProperty property;
    private JsonParser jsonParser;
    private JsonDeserializer<?> valueDeserializer;
    private NullValueProvider nullValueProvider;

    @Before
    public void setUp() throws Exception {
        objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(PropertyName.construct("id"));
        when(objectIdReader.getIdType()).thenReturn(IdType.STRING);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        valueDeserializer = mock(JsonDeserializer.class);
        nullValueProvider = mock(NullValueProvider.class);

        property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        property = new ObjectIdValueProperty(property, valueDeserializer, nullValueProvider);
        jsonParser = mock(JsonParser.class);
    }

    @Test
    public void testConstructor() {
        assertEquals(objectIdReader.propertyName, property.getName());
        assertEquals(IdType.STRING, property.getIdType());
        assertNull(property.getMember());
        assertEquals(valueDeserializer, property.getValueDeserializer());
        assertEquals(nullValueProvider, property.getNullProvider());
    }

    @Test
    public void testWithName() {
        String newName = PropertyName.construct("newName");
        property = property.withName(newName);
        assertEquals(newName, property.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> newDeserializer = mock(JsonDeserializer.class);
        property = property.withValueDeserializer(newDeserializer);
        assertEquals(newDeserializer, property.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider newNullProvider = mock(NullValueProvider.class);
        property = property.withNullProvider(newNullProvider);
        assertEquals(newNullProvider, property.getNullProvider());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetNullProperty() {
        property.set(new Object(), null);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        String id = "existingId";
        when(jsonParser.getValueAsString()).thenReturn(id);
        when(jsonParser.getCurrentName()).thenReturn(objectIdReader.propertyName);

        Object instance = new Object();
        property.deserializeAndSet(jsonParser, mock(DeserializationContext.class), instance);

        assertEquals(id, objectIdReader.getIdProperty().get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object id = new Object();
        when(valueDeserializer.deserialize(jsonParser, mock(DeserializationContext.class))).thenReturn(id);

        Object instance = new Object();
        Object result = property.deserializeSetAndReturn(jsonParser, mock(DeserializationContext.class), instance);

        assertEquals(id, result);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object id = new Object();
        Object instance = new Object();
        when(objectIdReader.idProperty.setAndReturn(instance, id)).thenReturn(instance);

        Object result = property.setAndReturn(instance, id);

        assertEquals(instance, result);
    }
}
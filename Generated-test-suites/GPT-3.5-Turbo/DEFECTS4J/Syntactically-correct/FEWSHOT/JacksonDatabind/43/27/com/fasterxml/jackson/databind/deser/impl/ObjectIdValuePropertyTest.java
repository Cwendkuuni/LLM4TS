package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty property;

    @Mock
    private ObjectIdReader objectIdReader;

    @Mock
    private PropertyMetadata metadata;

    @Mock
    private JsonDeserializer<?> deserializer;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        property = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testConstructor() {
        assertEquals(objectIdReader.propertyName, property.getFullName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertEquals(metadata, property.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());
    }

    @Test
    public void testWithNewName() {
        PropertyName newName = mock(PropertyName.class);
        ObjectIdValueProperty newProperty = property.withName(newName);
        
        assertEquals(newName, newProperty.getFullName());
        assertEquals(objectIdReader.getIdType(), newProperty.getType());
        assertEquals(metadata, newProperty.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), newProperty.getValueDeserializer());
    }

    @Test
    public void testWithNewDeserializer() {
        JsonDeserializer<?> newDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty newProperty = property.withValueDeserializer(newDeserializer);
        
        assertEquals(objectIdReader.propertyName, newProperty.getFullName());
        assertEquals(objectIdReader.getIdType(), newProperty.getType());
        assertEquals(metadata, newProperty.getMetadata());
        assertEquals(newDeserializer, newProperty.getValueDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(property.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = new Object();
        Object id = new Object();
        ReadableObjectId roid = mock(ReadableObjectId.class);
        SettableBeanProperty idProp = mock(SettableBeanProperty.class);

        when(objectIdReader.getIdProperty()).thenReturn(idProp);
        when(objectIdReader.getResolver()).thenReturn(mock(ObjectBuffer.class));
        when(objectIdReader.generator).thenReturn(mock(ObjectBuffer.class));
        when(deserializationContext.findObjectId(id, objectIdReader.generator, objectIdReader.resolver)).thenReturn(roid);
        when(jsonParser.getCurrentToken()).thenReturn(mock(ObjectBuffer.class));
        when(objectIdReader.idProperty).thenReturn(idProp);
        when(idProp.setAndReturn(instance, id)).thenReturn(instance);

        Object result = property.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        verify(objectIdReader.idProperty).setAndReturn(instance, id);
        assertEquals(instance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        when(objectIdReader.getIdProperty()).thenReturn(null);

        property.setAndReturn(instance, value);
    }

    @Test
    public void testSetWithIdProperty() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        SettableBeanProperty idProp = mock(SettableBeanProperty.class);

        when(objectIdReader.getIdProperty()).thenReturn(idProp);
        when(idProp.setAndReturn(instance, value)).thenReturn(value);

        Object result = property.setAndReturn(instance, value);

        verify(idProp).setAndReturn(instance, value);
        assertEquals(value, result);
    }
}
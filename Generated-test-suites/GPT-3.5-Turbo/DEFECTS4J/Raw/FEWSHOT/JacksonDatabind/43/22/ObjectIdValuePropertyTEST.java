package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.util.ReadableObjectId;
import com.fasterxml.jackson.databind.PropertyName;
import java.io.IOException;
import java.lang.annotation.Annotation;

public class ObjectIdValuePropertyTest {

    @Mock
    private ObjectIdReader objectIdReader;

    @Mock
    private JsonDeserializer<?> valueDeserializer;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @InjectMocks
    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructor() {
        PropertyMetadata propertyMetadata = mock(PropertyMetadata.class);
        assertNotNull(objectIdValueProperty);

        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        assertNotNull(objectIdValueProperty);

        verify(objectIdValueProperty, times(1))._objectIdReader = objectIdReader;
        verify(objectIdValueProperty, times(1)).setObjectIdReader(objectIdReader);
        verify(objectIdValueProperty, times(1)).setName(objectIdReader.propertyName);
        verify(objectIdValueProperty, times(1)).setType(objectIdReader.getIdType());
        verify(objectIdValueProperty, times(1)).setValueDeserializer(objectIdReader.getDeserializer());
        verify(objectIdValueProperty, times(1)).setMetadata(propertyMetadata);
    }

    @Test
    public void testConstructorWithDeserializer() {
        PropertyName propertyName = mock(PropertyName.class);
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        assertNotNull(objectIdValueProperty);

        objectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, deserializer);
        assertNotNull(objectIdValueProperty);

        verify(objectIdValueProperty, times(1))._objectIdReader = objectIdValueProperty._objectIdReader;
        verify(objectIdValueProperty, times(1)).setValueDeserializer(deserializer);
        verify(objectIdValueProperty, times(1)).setName(objectIdValueProperty.getName());
        verify(objectIdValueProperty, times(1)).setType(objectIdValueProperty.getType());
        verify(objectIdValueProperty, times(1)).setMetadata(objectIdValueProperty.getMetadata());
    }

    @Test
    public void testConstructorWithNewName() {
        PropertyName newName = mock(PropertyName.class);
        assertNotNull(objectIdValueProperty);

        objectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, newName);
        assertNotNull(objectIdValueProperty);

        verify(objectIdValueProperty, times(1))._objectIdReader = objectIdValueProperty._objectIdReader;
        verify(objectIdValueProperty, times(1)).setObjectIdReader(objectIdValueProperty.getObjectIdReader());
        verify(objectIdValueProperty, times(1)).setName(newName);
        verify(objectIdValueProperty, times(1)).setType(objectIdValueProperty.getType());
        verify(objectIdValueProperty, times(1)).setValueDeserializer(objectIdValueProperty.getValueDeserializer());
        verify(objectIdValueProperty, times(1)).setMetadata(objectIdValueProperty.getMetadata());
    }

    @Test
    public void testWithNewName() {
        PropertyName newName = mock(PropertyName.class);
        assertNotNull(objectIdValueProperty);

        objectIdValueProperty.withName(newName);

        verify(objectIdValueProperty, times(1)).withName(newName);
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        assertNotNull(objectIdValueProperty);

        objectIdValueProperty.withValueDeserializer(deserializer);

        verify(objectIdValueProperty, times(1)).withValueDeserializer(deserializer);
    }

    @Test
    public void testGetAnnotation() {
        Class<A> acls = mock(Class.class);
        objectIdValueProperty.getAnnotation(acls);

        verify(objectIdValueProperty, times(1)).getAnnotation(acls);
    }

    @Test
    public void testGetMember() {
        objectIdValueProperty.getMember();

        verify(objectIdValueProperty, times(1)).getMember();
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = mock(Object.class);
        objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(objectIdValueProperty, times(1)).deserializeAndSet(jsonParser, deserializationContext, instance);
        verify(objectIdValueProperty, times(1)).deserializeSetAndReturn(jsonParser, deserializationContext, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = mock(Object.class);
        objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        verify(objectIdValueProperty, times(1)).deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        verify(valueDeserializer, times(1)).deserialize(jsonParser, deserializationContext);
        verify(jsonParser, times(1)).findObjectId(id, generator, resolver);
        verify(roid, times(1)).bindItem(instance);

        if (idProp != null) {
            verify(idProp, times(1)).setAndReturn(instance, id);
        }
    }

    @Test
    public void testSet() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        objectIdValueProperty.set(instance, value);

        verify(objectIdValueProperty, times(1)).set(instance, value);
        verify(objectIdValueProperty, times(1)).setAndReturn(instance, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithNullIdPropertyException() throws IOException {
        ObjectIdValuePropertyTest testObj = new ObjectIdValuePropertyTest();
        when(testObj.getSettableObjectIdProperty()).thenReturn(null);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        testObj.setAndReturn(instance, value);

        verify(idProp, times(1)).setAndReturn(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        testObj.setAndReturn(instance, value);

        verify(objectIdValueProperty, times(1)).setAndReturn(instance, value);
        verify(idProp, times(1)).setAndReturn(instance, value);
    }

}
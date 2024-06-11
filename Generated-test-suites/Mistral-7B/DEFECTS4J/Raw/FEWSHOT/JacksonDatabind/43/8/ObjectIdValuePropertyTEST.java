package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.DeserializationConfig.Feature;
import com.fasterxml.jackson.databind.ser.std.ToStringBeanProperty;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ObjectIdValuePropertyTest {

    private ObjectMapper objectMapper;
    private ObjectIdReader objectIdReaderMock;
    private SettableBeanProperty idPropertyMock;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectIdReaderMock = mock(ObjectIdReader.class);
        idPropertyMock = mock(SettableBeanProperty.class);
    }

    @After
    public void tearDown() throws Exception {
        reset(objectIdReaderMock);
    }

    @Test
    public void testObjectIdValuePropertyCreation() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public BeanPropertyDefinition propertyName() {
                return new BeanPropertyDefinition("idPropertyName", null);
            }

            @Override
            public Class<?> getIdType() {
                return String.class;
            }

            @Override
            public JsonDeserializer<?> getDeserializer() {
                return null;
            }

            @Override
            public String getPropertyName() {
                return "idPropertyName";
            }

            @Override
            public SettableBeanProperty idProperty() {
                return idPropertyMock;
            }
        };

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);

        assertEquals("idPropertyName", objectIdValueProperty.getName());
        assertEquals(String.class, objectIdValueProperty.getType());
        assertEquals(null, objectIdValueProperty.getMetadata());
        assertEquals(null, objectIdValueProperty.getValueDeserializer());
        assertEquals(objectIdReader, objectIdValueProperty._objectIdReader);
    }

    @Test
    public void testSettableBeanPropertyCreationWithValueDeserializer() throws Exception {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        JsonDeserializer<String> jsonDeserializer = mock(JsonDeserializer.class);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, jsonDeserializer);

        assertEquals(objectIdReader, objectIdValueProperty._objectIdReader);
        assertEquals(jsonDeserializer, objectIdValueProperty.getValueDeserializer());
    }

    @Test
    public void testSettableBeanPropertyCreationWithNewName() throws Exception {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        PropertyName newName = PropertyName.construct("newName");

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, newName);

        assertEquals(newName, objectIdValueProperty.getName());
        assertEquals(objectIdReader, objectIdValueProperty._objectIdReader);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty objectIdValueProperty = createObjectIdValueProperty();

        assertNull(objectIdValueProperty.getAnnotation(Mockito.any(Annotation.class)));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty objectIdValueProperty = createObjectIdValueProperty();

        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        JsonDeserializer<String> jsonDeserializer = mock(JsonDeserializer.class);
        Object id = "123";
        Object instance = new Object();

        when(objectIdReader.idProperty).thenReturn(idPropertyMock);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, jsonDeserializer);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setAnnotationIntrospector(new AnnotationIntrospector());
        ObjectReader objectReader = objectMapper.readerForUpdating(instance.getClass());

        JsonParser jsonParser = objectMapper.getFactory().createParser(new StringReader("{\"id\":\"" + id + "\"}"));

        objectIdValueProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), instance);

        verify(idPropertyMock).setAndReturn(instance, id);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        JsonDeserializer<String> jsonDeserializer = mock(JsonDeserializer.class);
        Object id = "123";
        Object instance = new Object();

        when(objectIdReader.idProperty).thenReturn(idPropertyMock);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, jsonDeserializer);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.readerForUpdating(instance.getClass());

        JsonParser jsonParser = objectMapper.getFactory().createParser(new StringReader("{\"id\":\"" + id + "\"}"));

        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, objectMapper.getDeserializationContext(), instance);

        assertEquals(instance, result);

        verify(idPropertyMock, times(1)).setAndReturn(instance, id);
    }

    @Test
    public void testSet() throws Exception {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        JsonDeserializer<String> jsonDeserializer = mock(JsonDeserializer.class);
        Object id = "123";
        Object instance = new Object();

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, jsonDeserializer);

        when(objectIdReader.idProperty).thenReturn(idPropertyMock);

        objectIdValueProperty.set(instance, id);

        verify(idPropertyMock, times(1)).setAndReturn(instance, id);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        JsonDeserializer<String> jsonDeserializer = mock(JsonDeserializer.class);
        Object id = "123";
        Object instance = new Object();
        Object result = new Object();

        when(objectIdReader.idProperty).thenReturn(idPropertyMock);
        when(idPropertyMock.setAndReturn(instance, anyObject())).thenReturn(result);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, jsonDeserializer);

        Object object = objectIdValueProperty.setAndReturn(instance, id);

        assertEquals(result, object);
    }

    private ObjectIdValueProperty createObjectIdValueProperty() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        JsonDeserializer<String> jsonDeserializer = mock(JsonDeserializer.class);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, jsonDeserializer);

        return objectIdValueProperty;
    }
}
package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ObjectIdValuePropertyTest {

    private JsonDeserializer<Object> idDeserializer;
    private ObjectIdReader objectIdReader;
    private ObjectIdValueProperty objectIdValueProperty;

    @BeforeAll
    public void setUp() throws Exception {
        idDeserializer = Mockito.mock(JsonDeserializer.class);
        ObjectNode idNode = Mockito.mock(ObjectNode.class);
        when(idDeserializer.deserialize(ArgumentMatchers.any(JsonParser.class), ArgumentMatchers.any(DeserializationContext.class))).thenReturn("testId");
        when(idNode.textValue()).thenReturn("testIdValue");

        objectIdReader = Mockito.spy(new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("testPropertyName");
            }

            @Override
            public Class<Object> idType() {
                return Object.class;
            }

            @Override
            public JsonDeserializer<Object> idDeserializer() {
                return idDeserializer;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }

            @Override
            public void setIdGenerator(ObjectIdGenerator idGenerator) {
            }

            @Override
            public void setIdResolver(ObjectIdResolver idResolver) {
            }
        });

        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
    }

    @Test
    public void testDeserializeAndSetNull() throws IOException, JsonProcessingException {
        Object instance = new Object();
        objectIdValueProperty.deserializeAndSet(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), instance);
        assertNull(objectIdValueProperty.get(instance));
    }

    @Test
    public void testDeserializeAndSetId() throws IOException, JsonProcessingException {
        Object instance = new Object();
        Object id = "testId";
        Object idValue = objectIdValueProperty.deserializeSetAndReturn(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), instance);
        assertEquals(id, idValue);
        assertNotNull(objectIdValueProperty.get(instance));
        verify(objectIdReader, times(1)).idProperty();
    }

    @Test
    public void testSetValue() throws IOException {
        Object instance = new Object();
        Object idValue = "testIdValue";
        objectIdValueProperty.setAndReturn(instance, idValue);
        assertEquals(idValue, objectIdValueProperty.get(instance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetValueWithoutIdProperty() throws IOException {
        Object instance = new Object();
        ObjectIdReader objectIdReaderWithoutIdProperty = Mockito.spy(new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("testPropertyName");
            }

            @Override
            public Class<Object> idType() {
                return Object.class;
            }

            @Override
            public JsonDeserializer<Object> idDeserializer() {
                return null;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }

            @Override
            public void setIdGenerator(ObjectIdGenerator idGenerator) {
            }

            @Override
            public void setIdResolver(ObjectIdResolver idResolver) {
            }

            @Override
            public SettableBeanProperty idProperty() {
                return null;
            }
        });
        ObjectIdValueProperty objectIdValuePropertyWithoutIdProperty = new ObjectIdValueProperty(objectIdReaderWithoutIdProperty, new PropertyMetadata());
        objectIdValuePropertyWithoutIdProperty.setAndReturn(instance, idValue);
    }
}
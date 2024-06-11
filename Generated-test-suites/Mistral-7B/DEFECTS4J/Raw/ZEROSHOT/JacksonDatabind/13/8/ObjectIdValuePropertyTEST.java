package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test class for {@link ObjectIdValueProperty}.
 */
public class ObjectIdValuePropertyTest {

    private static final ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
    private static final String propertyName = "objectId";
    private static final Class<Long> idType = Long.class;
    private static final JsonDeserializer<Long> valueDeserializer = mock(JsonDeserializer.class);
    private ObjectIdValueProperty objectIdValueProperty;
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        when(objectIdReader.propertyName).thenReturn(new PropertyName(propertyName));
        when(objectIdReader.getIdType()).thenReturn(idType);
        when(objectIdReader.getDeserializer()).thenReturn(valueDeserializer);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(new SettableBeanProperty(new PropertyName("id"), Long.class, null, null));

        objectMapper = new ObjectMapper();
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ_ID_PROPERTY_METADATA);
    }

    @Test
    public void testGetAnnotation() throws Exception {
        assertNull(objectIdValueProperty.getAnnotation(JsonId.class));
        assertNull(objectIdValueProperty.getAnnotation(JsonDeserialize.class));
        assertNull(objectIdValueProperty.getAnnotation(JsonDeserialize.With.class));
    }

    @Test
    public void testGetMember() throws Exception {
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        final Long id = 1L;
        when(valueDeserializer.deserialize(mock(JsonParser.class), mock(DeserializationContext.class)))
                .thenReturn(id);

        final Object instance = new Object();

        objectIdValueProperty.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), instance);
        final SettableBeanProperty idProperty = objectIdReader.idProperty;
        assertEquals(id, idProperty.get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        final Long id = 1L;
        when(valueDeserializer.deserialize(mock(JsonParser.class), mock(DeserializationContext.class)))
                .thenReturn(id);
        when(objectIdReader.idProperty.get(mock(Object.class)))
                .thenReturn(null);

        final Object instance = new Object();
        final Object result = objectIdValueProperty.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), instance);
        assertEquals(instance, result);
    }

    @Test
    public void testSet() throws Exception {
        final Long id = 1L;
        final Object instance = new Object();

        objectIdValueProperty.set(instance, id);
        final SettableBeanProperty idProperty = objectIdReader.idProperty;
        assertEquals(id, idProperty.get(instance));
    }

    @Test
    public void testSetAndReturn() throws Exception {
        final Long id = 1L;
        final Object instance = new Object();

        when(objectIdReader.idProperty.get(instance))
                .thenReturn(null);

        final Object result = objectIdValueProperty.setAndReturn(instance, id);
        assertEquals(instance, result);
        assertEquals(id, objectIdReader.idProperty.get(instance));
    }

    @Test
    public void testWithName() throws Exception {
        final String newName = "newPropertyName";
        final ObjectIdValueProperty result = objectIdValueProperty.withName(new PropertyName(newName));
        assertEquals(newName, result.getName().getSimpleName());
    }

    @Test
    public void testWithValueDeserializer() throws Exception {
        final JsonDeserializer<String> newValueDeserializer = mock(JsonDeserializer.class);
        final ObjectIdValueProperty result = objectIdValueProperty
                .withValueDeserializer(newValueDeserializer);
        assertEquals(newValueDeserializer, result.getValueDeserializer());
    }
}
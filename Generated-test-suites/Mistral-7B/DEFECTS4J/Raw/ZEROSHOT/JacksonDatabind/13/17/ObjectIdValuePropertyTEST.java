package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdGenerator;
import com.fasterxml.jackson.databind.node.ObjectIdResolver;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link ObjectIdValueProperty}.
 */
public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private ObjectIdValueProperty property;

    @Before
    public void setup() {
        ObjectIdGenerator generator = mock(ObjectIdGenerator.class);
        ObjectIdResolver resolver = mock(ObjectIdResolver.class);
        JsonDeserializer<ObjectId> deserializer = mock(JsonDeserializer.class);
        when(deserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class))).thenReturn(1L);

        objectIdReader = new ObjectIdReader("testPropertyName", ObjectId.class, _ -> deserializer, generator, resolver);
        property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(property.getAnnotation(Mockito.any(Annotation.class).getClass()));
    }

    @Test
    public void testGetMember() {
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object id = property.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), new Object());
        assertEquals(1L, id);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object id = property.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), new Object());
        assertEquals(1L, id);
    }

    @Test
    public void testSet() throws IOException {
        Object id = property.setAndReturn(new Object(), 1L);
        assertEquals(1L, id);
    }
}
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

/**
 * Test class for ObjectIdValueProperty.
 */
public class ObjectIdValuePropertyTest {

    private ObjectMapper mapper;
    private JsonParser parser;
    private ObjectIdReader objectIdReader;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        parser = Mockito.mock(JsonParser.class);
        objectIdReader = Mockito.mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
    }

    @Test
    public void testObjectIdValuePropertyCreation() {
        JsonDeserializer<String> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, valueDeserializer, new PropertyMetadata(), null);
        assertEquals("id", objectIdValueProperty.getName());
        assertEquals(String.class, objectIdValueProperty.getType());
        assertEquals(valueDeserializer, objectIdValueProperty.getValueDeserializer());
        assertEquals(objectIdReader, objectIdValueProperty.getObjectIdReader());
    }

    @Test
    public void testObjectIdValuePropertySerialization() throws IOException {
        JsonDeserializer<String> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, valueDeserializer, new PropertyMetadata(), null);
        ObjectWriter objectWriter = mapper.writerWithView(SimpleBeanPropertyFilter.filterOutAllExcept("id"));
        String id = "123";
        ObjectNode objectNode = objectWriter.writeValueAsObjectNode(objectIdValueProperty.setAndReturn(new Object(), id));
        assertEquals("\"id\":\"" + id + "\"", objectNode.toString());
    }

    @Test
    public void testObjectIdValuePropertyDeserialization() throws IOException {
        JsonDeserializer<String> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, valueDeserializer, new PropertyMetadata(), null);
        ObjectIdReader objectIdReaderSpy = Mockito.spy(objectIdReader);
        ObjectReader objectReader = mapper.readerForUpdating(Object.class);

        when(parser.getValueAsString()).thenReturn("123");
        when(objectIdReaderSpy.readObjectId(parser, Mockito.any(DeserializationContext.class))).thenReturn(new ReadableObjectId(123L));
        when(objectIdReaderSpy.generator).thenReturn(Mockito.mock(ObjectIdGenerator.class));

        Object instance = new Object();
        Object result = objectIdValueProperty.deserializeAndSet(parser, Mockito.mock(DeserializationContext.class), instance);
        assertEquals(123L, objectIdReaderSpy.generator.asLongId(result));
        Mockito.verify(objectIdReaderSpy).bindItem(instance);

        when(objectIdReaderSpy.idProperty).thenReturn(Mockito.mock(SettableBeanProperty.class));
        result = objectIdValueProperty.deserializeSetAndReturn(parser, Mockito.mock(DeserializationContext.class), instance);
        assertEquals(instance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testObjectIdValuePropertySet() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null, new PropertyMetadata(), null);
        Object instance = new Object();
        objectIdValueProperty.set(instance, "id");
        fail("Should not call set() on ObjectIdProperty that has no SettableBeanProperty");
    }
}
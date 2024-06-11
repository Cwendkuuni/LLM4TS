package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectIdReader;

public class ObjectIdValuePropertyTest {

    private JsonDeserializer<Object> noOpDeserializer = mock(JsonDeserializer.class);;
    private ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
    private ObjectIdValueProperty property;

    @Before
    public void setUp() throws Exception {
        when(objectIdReader.getIdType()).thenReturn(String.class);
        when(objectIdReader.getPropertyName()).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdProperty()).thenReturn(new SettableBeanProperty("idProperty", noOpDeserializer, null, null, null));
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(property.getAnnotation(Object.class));
    }

    @Test
    public void testGetMember() {
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object id = "123";
        Object instance = new Object();
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(parser.getValueAsString()).thenReturn(id);

        property.deserializeAndSet(parser, ctxt, instance);

        verify(ctxt, times(1)).findObjectId(id, eq(objectIdReader.generator), eq(objectIdReader.resolver));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object id = "123";
        Object instance = new Object();
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(parser.getValueAsString()).thenReturn(id);

        Object result = property.deserializeSetAndReturn(parser, ctxt, instance);

        verify(ctxt, times(1)).findObjectId(id, eq(objectIdReader.generator), eq(objectIdReader.resolver));
        assertSame(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        Object id = "123";
        Object instance = new Object();
        JsonParser parser = mock(JsonParser.class);

        when(parser.getValueAsString()).thenReturn(id);

        property.set(instance, id);

        verify(objectIdReader.idProperty, times(1)).setAndReturn(instance, id);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object id = "123";
        Object instance = new Object();

        Object result = property.setAndReturn(instance, id);

        verify(objectIdReader.idProperty, times(1)).setAndReturn(instance, id);
        assertSame(instance, result);
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty result = property.withName(newName);

        assertEquals(newName, result.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<String> stringDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty result = property.withValueDeserializer(stringDeserializer);

        assertEquals(stringDeserializer, result.getValueDeserializer());
    }

    @Test
    public void testCopyConstructor() {
        ObjectIdValueProperty src = property;
        ObjectIdValueProperty result = new ObjectIdValueProperty(src, noOpDeserializer);

        assertEquals(src.getName(), result.getName());
        assertEquals(src._objectIdReader, result._objectIdReader);
        assertEquals(src.getPropertyMetadata(), result.getPropertyMetadata());
        assertEquals(src.getValueDeserializer(), result.getValueDeserializer());
    }
}
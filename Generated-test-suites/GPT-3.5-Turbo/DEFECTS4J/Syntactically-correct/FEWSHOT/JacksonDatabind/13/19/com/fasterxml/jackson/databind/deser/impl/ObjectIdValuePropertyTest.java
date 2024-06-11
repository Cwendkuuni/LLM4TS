package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private PropertyMetadata metadata;
    private JsonDeserializer<?> deserializer;
    private JsonParser jp;
    private DeserializationContext ctxt;
    private Object instance;
    private SettableBeanProperty idProp;
    private ReadableObjectId roid;

    @Before
    public void setUp() {
        objectIdReader = mock(ObjectIdReader.class);
        metadata = mock(PropertyMetadata.class);
        deserializer = mock(JsonDeserializer.class);
        jp = mock(JsonParser.class);
        ctxt = mock(DeserializationContext.class);
        instance = new Object();
        idProp = mock(SettableBeanProperty.class);
        roid = mock(ReadableObjectId.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("property"));
        when(objectIdReader.getIdType()).thenReturn(Object.class);
        when(objectIdReader.getDeserializer()).thenReturn(deserializer);
        when(ctxt.findObjectId(any(), any(), any())).thenReturn(roid);
        when(roid.bindItem(any())).thenReturn(instance);
        when(objectIdReader.idProperty).thenReturn(idProp);
    }

    @Test
    public void testConstructor() {
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);
        assertEquals(objectIdReader.propertyName, obj.getFullName());
        assertEquals(objectIdReader.getIdType(), obj.getType());
        assertSame(metadata, obj.getMetadata());
        assertSame(deserializer, obj.getValueDeserializer());
        assertSame(objectIdReader, obj.getObjectIdReader());
    }

    @Test
    public void testConstructorWithDeserializer() {
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, deserializer);
        assertEquals(objectIdReader.propertyName, obj.getFullName());
        assertEquals(objectIdReader.getIdType(), obj.getType());
        assertSame(objectIdReader, obj.getObjectIdReader());
        assertSame(deserializer, obj.getValueDeserializer());
    }

    @Test
    public void testConstructorWithPropertyName() {
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, "name");
        assertEquals("name", obj.getFullName().getSimpleName());
        assertEquals(Object.class, obj.getType());
        assertNull(obj.getMetadata());
        assertNull(obj.getValueDeserializer());
        assertSame(objectIdReader, obj.getObjectIdReader());
    }

    @Test
    public void testConstructorWithPropertyNameAndDeserializer() {
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, deserializer);
        assertEquals(objectIdReader.propertyName, obj.getFullName());
        assertEquals(objectIdReader.getIdType(), obj.getType());
        assertNull(obj.getMetadata());
        assertSame(objectIdReader, obj.getObjectIdReader());
        assertSame(deserializer, obj.getValueDeserializer());
    }

    @Test
    public void testWithName() {
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty newObj = obj.withName(new PropertyName("newName"));
        assertEquals("newName", newObj.getFullName().getSimpleName());
        assertEquals(obj.getType(), newObj.getType());
        assertSame(obj.getMetadata(), newObj.getMetadata());
        assertSame(obj.getValueDeserializer(), newObj.getValueDeserializer());
        assertSame(obj.getObjectIdReader(), newObj.getObjectIdReader());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty newObj = obj.withValueDeserializer(deserializer);
        assertEquals(obj.getFullName(), newObj.getFullName());
        assertEquals(obj.getType(), newObj.getType());
        assertSame(obj.getMetadata(), newObj.getMetadata());
        assertSame(deserializer, newObj.getValueDeserializer());
        assertSame(obj.getObjectIdReader(), newObj.getObjectIdReader());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNull(obj.getAnnotation(Object.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNull(obj.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);
        obj.deserializeAndSet(jp, ctxt, instance);
        obj.deserializeSetAndReturn(jp, ctxt, instance);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);
        obj.set(instance, instance);
        obj.setAndReturn(instance, instance);
    }

}
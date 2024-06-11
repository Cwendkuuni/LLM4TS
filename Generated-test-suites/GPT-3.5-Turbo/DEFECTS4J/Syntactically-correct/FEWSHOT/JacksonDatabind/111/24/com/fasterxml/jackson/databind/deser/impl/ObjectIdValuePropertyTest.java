
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private PropertyMetadata metadata;
    private JsonDeserializer<?> deserializer;
    private NullValueProvider nvp;
    private ObjectIdValueProperty property;

    private JsonParser p;
    private DeserializationContext ctxt;
    private Object instance;
    private ObjectBuffer objectBuffer;

    @Before
    public void setup() {
        objectIdReader = mock(ObjectIdReader.class);
        metadata = mock(PropertyMetadata.class);
        deserializer = mock(JsonDeserializer.class);
        nvp = mock(NullValueProvider.class);
        property = new ObjectIdValueProperty(objectIdReader, metadata);

        p = mock(JsonParser.class);
        ctxt = mock(DeserializationContext.class);
        instance = new Object();
        objectBuffer = mock(ObjectBuffer.class);
    }

    @Test
    public void testConstructors() {
        assertEquals(objectIdReader.propertyName, property.getName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertEquals(metadata, property.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());

        ObjectIdValueProperty newProperty = new ObjectIdValueProperty(property, deserializer, nvp);
        assertSame(deserializer, newProperty.getValueDeserializer());
        assertSame(nvp, newProperty.getNullValueProvider());

        ObjectIdValueProperty newProperty2 = new ObjectIdValueProperty(property, new PropertyName("newName"));
        assertEquals(new PropertyName("newName"), newProperty2.getName());
    }

    @Test
    public void testWithMethods() {
        ObjectIdValueProperty newProperty = (ObjectIdValueProperty) property.withName(new PropertyName("newName"));
        assertEquals(new PropertyName("newName"), newProperty.getName());

        ObjectIdValueProperty newProperty2 = (ObjectIdValueProperty) property.withValueDeserializer(deserializer);
        assertSame(deserializer, newProperty2.getValueDeserializer());

        ObjectIdValueProperty newProperty3 = (ObjectIdValueProperty) property.withNullProvider(nvp);
        assertSame(nvp, newProperty3.getNullValueProvider());
    }

    @Test
    public void testGetMethods() {
        assertNull(property.getAnnotation(Object.class));
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        when(deserializer.deserialize(p, ctxt)).thenReturn("id");
        when(ctxt.findObjectId("id", objectIdReader.generator, objectIdReader.resolver)).thenReturn(new ReadableObjectId());
        when(ctxt.getParser()).thenReturn(p);
        when(metadata.getNullValue()).thenReturn(null);

        assertNull(property.deserializeSetAndReturn(p, ctxt, instance));
        verify(p).hasToken(JsonToken.VALUE_NULL);
        verify(ctxt).findObjectId("id", objectIdReader.generator, objectIdReader.resolver);
        verify(ctxt).getParser();
        verify(metadata).getNullValue();
    }

    @Test
    public void testSetAndReturn() throws IOException {
        SettableBeanProperty idProperty = mock(SettableBeanProperty.class);
        when(objectIdReader.idProperty).thenReturn(idProperty);

        assertNull(property.setAndReturn(instance, "value"));
        verify(objectIdReader).idProperty;
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithoutIdProperty() throws IOException {
        assertNull(property.setAndReturn(instance, "value"));
    }
}

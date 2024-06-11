
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;

public class ObjectIdValuePropertyTest {
    private ObjectIdValueProperty property;
    private JsonDeserializer<?> deserializer;
    private ObjectIdReader objectIdReader;

    @Before
    public void setup() {
        objectIdReader = mock(ObjectIdReader.class);
        deserializer = mock(JsonDeserializer.class);
        property = new ObjectIdValueProperty(objectIdReader, null);
    }

    @Test
    public void testWithMember() {
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        property.deserializeAndSet(jp, ctxt, instance);

        verify(objectIdReader.getGenerator()).bindItem(instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Object id = new Object();

        when(objectIdReader.getGenerator().bindItem(instance)).thenReturn(null);
        when(objectIdReader.idProperty).thenReturn(null);
        when(deserializer.deserialize(jp, ctxt)).thenReturn(id);

        Object result = property.deserializeSetAndReturn(jp, ctxt, instance);

        assertEquals(instance, result);
        verify(deserializer).deserialize(jp, ctxt);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithNoIdProperty() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        when(objectIdReader.idProperty).thenReturn(null);

        property.setAndReturn(instance, value);
    }
}

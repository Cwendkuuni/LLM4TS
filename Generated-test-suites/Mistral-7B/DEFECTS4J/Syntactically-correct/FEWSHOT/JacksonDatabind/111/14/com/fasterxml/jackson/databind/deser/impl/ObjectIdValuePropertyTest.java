package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        String idPropertyName = "idPropertyName";
        when(objectIdReader.propertyName).thenReturn(idPropertyName);
        when(objectIdReader.getIdType()).thenReturn(String.class);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(new ObjectIdValueProperty.Std(idPropertyName, null, null));

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentName()).thenReturn("objectId");
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getValueAsString()).thenReturn("123");

        Object instance = new Object();

        Object id = objectIdValueProperty.deserializeAndSet(parser, mock(DeserializationContext.class), instance);
        assertNotNull(id);
        assertEquals("123", id);

        Object expectedInstance = new Object() {{
            setId("123");
        }};
        verify(instance).setId(eq("123"));
        verify(instance).setIdProperty("idPropertyName");
        verify(parser).nextToken();
        assertNull(parser.getValueAsString());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn("objectIdPropertyName");
        when(objectIdReader.getIdType()).thenReturn(Integer.class);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentName()).thenReturn("objectId");
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER);
        when(parser.getValueAsInt()).thenReturn(123);

        Object instance = new Object();

        Object id = objectIdValueProperty.deserializeSetAndReturn(parser, mock(DeserializationContext.class), instance);
        assertNotNull(id);
        assertEquals(123, id);
        verify(instance).setId(eq(123));
        verify(instance).setIdProperty("objectIdPropertyName");
        verify(parser).nextToken();
        assertNull(parser.getValueAsString());
    }

    @Test
    public void testSetAndReturnNull() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        Object instance = new Object();

        Object id = objectIdValueProperty.setAndReturn(instance, null);
        assertNull(id);
        verify(instance).setId(null);
        verify(instance).setIdProperty(null);
    }
}
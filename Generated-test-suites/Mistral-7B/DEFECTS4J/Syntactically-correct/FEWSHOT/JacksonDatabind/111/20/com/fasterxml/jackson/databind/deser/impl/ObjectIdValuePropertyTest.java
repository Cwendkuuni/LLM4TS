package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty _property;
    private JsonDeserializer<String> _stringDeserializer = new StdSerializer<String>(String.class) {};
    private JsonDeserializer<Object> _objectDeserializer = new StdSerializer<Object>(Object.class) {};
    private ObjectIdReader _objectIdReader = mock(ObjectIdReader.class);
    private Object _instance;

    @org.junit.Before
    public void setUp() throws Exception {
        when(_objectIdReader.propertyName).thenReturn(new PropertyName("objectId"));
        when(_objectIdReader.getIdType()).thenReturn(String.class);
        when(_objectIdReader.getDeserializer()).thenReturn(_stringDeserializer);
        when(_objectIdReader.idProperty).thenReturn(new ObjectIdValueProperty(_objectIdReader, new PropertyMetadata()) {
            @Override
            public SettableBeanProperty withName(PropertyName newName) {
                return this;
            }
        });
        _property = new ObjectIdValueProperty(_objectIdReader, new PropertyMetadata());
        _instance = new Object();
    }

    @Test
    public void testGetAnnotation() {
        assertNull(_property.getAnnotation(JsonId.class));
    }

    @Test
    public void testGetMember() {
        assertNull(_property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentName()).thenReturn("objectId");
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getValueAsString()).thenReturn("123");

        _property.deserializeAndSet(parser, new DeserializationContext(), _instance);

        assertEquals("123", _instance.getClass().getDeclaredField("objectId").get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentName()).thenReturn("objectId");
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getValueAsString()).thenReturn("123");

        Object id = _property.deserializeSetAndReturn(parser, new DeserializationContext(), _instance);

        assertEquals("123", id);
    }

    @Test
    public void testSet() throws IOException {
        _property.set(_instance, "123");

        assertEquals("123", _instance.getClass().getDeclaredField("objectId").get(instance));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object id = "123";
        when(_objectIdReader.getIdType()).thenReturn(id.getClass());

        Object result = _property.setAndReturn(_instance, id);

        assertEquals(_instance, result);
    }
}
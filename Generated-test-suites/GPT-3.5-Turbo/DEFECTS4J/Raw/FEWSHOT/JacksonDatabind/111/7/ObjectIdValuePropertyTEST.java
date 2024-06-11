package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    private SettableBeanProperty settableBeanProperty;
    private Object instance;
    private JsonParser jsonParser;
    private DeserializationContext context;

    @Before
    public void setup() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        PropertyMetadata propertyMetadata = mock(PropertyMetadata.class);
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);

        settableBeanProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        instance = new Object();
        jsonParser = mock(JsonParser.class);
        context = mock(DeserializationContext.class);

        when(objectIdReader.propertyName).thenReturn("propertyName");
        when(objectIdReader.getIdType()).thenReturn(Object.class);
        when(objectIdReader.getDeserializer()).thenReturn(deserializer);
    }

    @Test
    public void testWithNullToken() throws IOException {
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        assertNull(settableBeanProperty.deserializeSetAndReturn(jsonParser, context, instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty objectIdProperty = new ObjectIdValueProperty(objectIdReader, null);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object();
 
        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(objectIdReader.idProperty).thenReturn(null);
        when(objectIdReader.generator).thenReturn(null);
        when(objectIdReader.resolver).thenReturn(null);
        when(context.findObjectId(null, null, null)).thenReturn(null);

        assertNull(objectIdProperty.deserializeSetAndReturn(parser, context, instance));
    }

    @Test
    public void testSetAndReturnWithNullIdProperty() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        SettableBeanProperty idProp = mock(SettableBeanProperty.class);
        ObjectIdValueProperty objectIdProperty = new ObjectIdValueProperty(objectIdReader, null);

        when(objectIdReader.idProperty).thenReturn(null);

        try {
            objectIdProperty.setAndReturn(instance, "123");
        } catch (UnsupportedOperationException e) {
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty",
                    e.getMessage());
        }
    }
}
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link ObjectIdValueProperty}.
 */
public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private ObjectIdValueProperty objectIdValueProperty;
    private JsonDeserializer<Object> deserializer;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;

    @Before
    public void setUp() {
        objectIdReader = Mockito.mock(ObjectIdReader.class);
        deserializer = Mockito.mock(JsonDeserializer.class);
        jsonParser = Mockito.mock(JsonParser.class);
        deserializationContext = Mockito.mock(DeserializationContext.class);

        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader,
                PropertyMetadata.STD_OBJ, null, deserializer);
    }

    @Test
    public void testGetAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(Object.class));
    }

    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        String idValue = "someIdValue";
        Object id = Mockito.mock(Object.class);
        ReadableObjectId roid = Mockito.mock(ReadableObjectId.class);
        Mockito.when(objectIdReader.generator).thenReturn(Mockito.mock(ObjectIdGenerator.class));
        Mockito.when(objectIdReader.resolver).thenReturn(Mockito.mock(ObjectIdResolver.class));
        Mockito.when(objectIdReader.idProperty).thenReturn(null);

        Mockito.when(jsonParser.getValueAsToken()).thenReturn(Mockito.mock( JsonToken.class ));
        Mockito.when(jsonParser.getValueAsString()).thenReturn(idValue);
        Mockito.when(deserializer.deserialize(jsonParser, deserializationContext)).thenReturn(id);
        Mockito.when(objectIdReader.generator.generateId(idValue)).thenReturn(roid);

        objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, null);

        Mockito.verify(deserializationContext).findObjectId(id, objectIdReader.generator, objectIdReader.resolver);
        Mockito.verify(instance).bindItem(roid);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        String idValue = "someIdValue";
        Object id = Mockito.mock(Object.class);
        ReadableObjectId roid = Mockito.mock(ReadableObjectId.class);
        Object idProp = Mockito.mock(SettableBeanProperty.class);

        Mockito.when(jsonParser.getValueAsToken()).thenReturn(Mockito.mock( JsonToken.class ));
        Mockito.when(jsonParser.getValueAsString()).thenReturn(idValue);
        Mockito.when(deserializer.deserialize(jsonParser, deserializationContext)).thenReturn(id);
        Mockito.when(objectIdReader.generator.generateId(idValue)).thenReturn(roid);
        Mockito.when(objectIdReader.idProperty).thenReturn(idProp);

        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, null);

        Mockito.verify(deserializationContext).findObjectId(id, objectIdReader.generator, objectIdReader.resolver);
        Mockito.verify(idProp).setAndReturn(null, id);
        assertEquals(id, result);
    }

    @Test
    public void testSet() throws IOException {
        Object id = Mockito.mock(Object.class);
        Object idProp = Mockito.mock(SettableBeanProperty.class);

        Mockito.when(objectIdReader.idProperty).thenReturn(idProp);

        objectIdValueProperty.set(null, id);

        Mockito.verify(idProp).setAndReturn(null, id);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object id = Mockito.mock(Object.class);
        Object idProp = Mockito.mock(SettableBeanProperty.class);
        Object instance = Mockito.mock(Object.class);

        Mockito.when(objectIdReader.idProperty).thenReturn(idProp);

        Object result = objectIdValueProperty.setAndReturn(instance, id);

        Mockito.verify(idProp).setAndReturn(instance, id);
        assertEquals(id, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetThrowsUOE() throws IOException {
        Object id = Mockito.mock(Object.class);

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ, null, deserializer);

        property.setAndReturn(null, id);
    }
}
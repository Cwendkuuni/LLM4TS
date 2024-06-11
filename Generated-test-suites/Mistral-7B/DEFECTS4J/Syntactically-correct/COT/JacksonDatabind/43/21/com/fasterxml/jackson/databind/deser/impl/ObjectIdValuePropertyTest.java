package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

/**
 * Test class for ObjectIdValueProperty
 */
public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void init() {
        objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn("objectIdPropertyName");
        when(objectIdReader.getIdType()).thenReturn(int.class);
        when(objectIdReader.getDeserializer()).thenReturn(mock(JsonDeserializer.class));
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
    }

    @Test
    public void testConstructor() {
        assertNotNull(objectIdValueProperty);
    }

    @Test
    public void testWithName() throws IOException {
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(new PropertyName("newName"));
        assertEquals("newName", newObjectIdValueProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() throws IOException {
        StdSerializer<String> stdSerializer = mock(StdSerializer.class);
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(stdSerializer);
        assertEquals(stdSerializer, newObjectIdValueProperty.getValueDeserializer());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetNullValue() throws IOException {
        objectIdValueProperty.set(null, null);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        when(jsonParser.getValueAsToken()).thenReturn(mock(JsonToken.class));

        objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(deserializationContext, times(1)).findObjectId(any(), eq(objectIdReader), eq(objectIdReader.resolver));
    }

    @Test
    public void testDeserializeSetAndReturnNullId() throws IOException {
        Object id = null;
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        assertNull(instance);
    }

    @Test
    public void testDeserializeSetAndReturnId() throws IOException {
        Object id = new Object();
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        when(jsonParser.getValueAsToken()).thenReturn(mock(JsonToken.class));

        ReadableObjectId readableObjectId = mock(ReadableObjectId.class);
        when(deserializationContext.findObjectId(eq(id), eq(objectIdReader), eq(objectIdReader.resolver))).thenReturn(readableObjectId);
        when(readableObjectId.bindItem(instance)).thenReturn(instance);

        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        assertNotNull(result);
        assertEquals(instance, result);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object id = new Object();
        Object instance = new Object();
        Object value = new Object();

        SettableBeanProperty idProp = mock(SettableBeanProperty.class);
        when(idProp.setAndReturn(instance, id)).thenReturn(instance);

        when(objectIdValueProperty._objectIdReader.idProperty).thenReturn(idProp);

        Object result = objectIdValueProperty.setAndReturn(instance, value);

        assertNotNull(result);
        assertEquals(instance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithoutIdProperty() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        when(objectIdValueProperty._objectIdReader.idProperty).thenReturn(null);

        objectIdValueProperty.setAndReturn(instance, value);
    }
}
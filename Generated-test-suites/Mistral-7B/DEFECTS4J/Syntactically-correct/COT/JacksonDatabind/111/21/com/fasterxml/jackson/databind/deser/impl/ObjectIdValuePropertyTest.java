package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.NullValueProvider;
import com.fasterxml.jackson.databind.util.NameTransformer;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Test for ObjectIdValueProperty class.
 */
public class ObjectIdValuePropertyTest {

    @Test
    public void testObjectIdValuePropertyConstructor() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        PropertyMetadata metadata = new PropertyMetadata();
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata, deserializer);
        assertSame(objectIdReader, objectIdValueProperty._objectIdReader);
        assertSame(metadata, objectIdValueProperty.getMetadata());
        assertSame(deserializer, objectIdValueProperty.getValueDeserializer());
    }

    @Test
    public void testObjectIdValuePropertyCopyConstructor() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, deserializer);
        ObjectIdValueProperty newObjectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, deserializer);
        assertSame(objectIdValueProperty, newObjectIdValueProperty);
        assertSame(objectIdReader, newObjectIdValueProperty._objectIdReader);
        assertSame(deserializer, newObjectIdValueProperty.getValueDeserializer());
    }

    @Test
    public void testObjectIdValuePropertyWithNameConstructor() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        PropertyName newName = new PropertyName("testName");
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, newName);
        assertSame(objectIdReader, objectIdValueProperty._objectIdReader);
        assertSame(newName, objectIdValueProperty.getName());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(null, null, null);
        assertNull(objectIdValueProperty.getAnnotation(Object.class));
        assertNull(objectIdValueProperty.getAnnotation(Map.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(null, null, null);
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null, valueDeserializer);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object();

        Object id = new Object();
        ReadableObjectId readableObjectId = mock(ReadableObjectId.class);
        when(readableObjectId.bindItem(instance)).thenReturn(instance);
        when(objectIdReader.generator).thenReturn(new ObjectIdGenerator());
        when(objectIdReader.resolver).thenReturn(new ObjectIdResolver());
        when(context.findObjectId(anyObject(), eq(objectIdReader.generator), eq(objectIdReader.resolver))).thenReturn(readableObjectId);
        when(valueDeserializer.deserialize(jsonParser, context)).thenReturn(id);

        objectIdValueProperty.deserializeAndSet(jsonParser, context, instance);

        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        verify(readableObjectId).bindItem(argumentCaptor.capture());
        assertSame(instance, argumentCaptor.getValue());
        reset(readableObjectId);

        verify(objectIdReader.generator).generateId(jsonParser, context);
        verifyNoMoreInteractions(objectIdReader, context, readableObjectId, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null, valueDeserializer);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object();

        Object id = new Object();
        ReadableObjectId readableObjectId = mock(ReadableObjectId.class);
        when(readableObjectId.bindItem(instance)).thenReturn(instance);
        when(objectIdReader.generator).thenReturn(new ObjectIdGenerator());
        when(objectIdReader.resolver).thenReturn(new ObjectIdResolver());
        when(context.findObjectId(anyObject(), eq(objectIdReader.generator), eq(objectIdReader.resolver))).thenReturn(readableObjectId);
        when(valueDeserializer.deserialize(jsonParser, context)).thenReturn(id);

        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, context, instance);

        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        verify(readableObjectId).bindItem(argumentCaptor.capture());
        assertSame(instance, argumentCaptor.getValue());
        reset(readableObjectId);

        verify(objectIdReader.generator).generateId(jsonParser, context);
        verify(objectIdValueProperty.getValueDeserializer()).deserialize(jsonParser, context);
        assertSame(instance, result);
        verifyNoMoreInteractions(objectIdReader, context, readableObjectId, instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithNullIdProperty() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null, valueDeserializer);
        Object instance = new Object();
        Object id = new Object();

        objectIdValueProperty.setAndReturn(instance, id);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        SettableBeanProperty idProperty = mock(SettableBeanProperty.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null, valueDeserializer, idProperty);
        Object instance = new Object();
        Object id = new Object();

        when(idProperty.setAndReturn(instance, id)).thenReturn(instance);

        Object result = objectIdValueProperty.setAndReturn(instance, id);

        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        verify(idProperty).setAndReturn(instance, id);
        assertSame(instance, result);
        verifyNoMoreInteractions(objectIdReader, idProperty, instance);
    }

    private static class ObjectIdGenerator implements ObjectIdGenerator {
        @Override
        public Object generateId(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new Object();
        }
    }

    private static class ObjectIdResolver implements ObjectIdResolver {
        @Override
        public Object idFromString(String id, DeserializationContext ctxt) throws IOException {
            return new Object();
        }

        @Override
        public void resolveId(Object id, JsonParser p, DeserializationContext ctxt) throws IOException {
            // no-op
        }

        @Override
        public void reportError(MessageBase msg, DeserializationContext ctxt) throws IOException {
            // no-op
        }
    }
}
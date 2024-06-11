package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdGenerator;
import com.fasterxml.jackson.databind.node.ObjectIdResolver;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class ObjectIdValuePropertyTest {

    private static ObjectIdReader getMockObjectIdReader() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(Long.TYPE);
        when(objectIdReader.idProperty).thenReturn(new MockSettableBeanProperty());
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.getValueDeserializer()).thenReturn(mock(JsonDeserializer.class));
        return objectIdReader;
    }

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = getMockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertSame(objectIdReader.propertyName, property.getName());
        assertSame(objectIdReader.getIdType(), property.getType());
        assertSame(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = getMockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(property.getAnnotation(JsonId.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = getMockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = getMockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonParser jsonParser = mock(JsonParser.class);
        Object instance = new Object();
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        when(jsonParser.getCurrentName()).thenReturn("id");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getValueAsString()).thenReturn("123");

        property.deserializeAndSet(jsonParser, deserializationContext, instance);

        ObjectId id = mock(ObjectId.class);
        when(deserializationContext.findObjectId(anyLong(), any(), any())).thenReturn(id);
        when(id.bindItem(instance)).thenReturn(instance);

        SettableBeanProperty idProp = mock(SettableBeanProperty.class);
        when(objectIdReader.idProperty).thenReturn(idProp);
        when(idProp.setAndReturn(instance, any())).thenReturn(instance);

        assertSame(instance, property.deserializeAndSet(jsonParser, deserializationContext, instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = getMockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonParser jsonParser = mock(JsonParser.class);
        Object instance = new Object();
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

        Object result = property.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
        assertNull(result);

        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        when(jsonParser.getValueAsInt()).thenReturn(123);

        Object id = mock(Object.class);
        ObjectId idObjectId = mock(ObjectId.class);
        when(idObjectId.bindItem(instance)).thenReturn(instance);
        when(deserializationContext.findObjectId(anyInt(), any(), any())).thenReturn(idObjectId);
        when(idObjectId.getId()).thenReturn(id);

        when(property._valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(id);

        SettableBeanProperty idProp = mock(SettableBeanProperty.class);
        when(objectIdReader.idProperty).thenReturn(idProp);
        when(idProp.setAndReturn(instance, any())).thenReturn(instance);

        Object result2 = property.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
        assertSame(id, result2);
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdReader objectIdReader = getMockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        Object instance = new Object();
        Object value = new Object();

        when(property._objectIdReader.idProperty).thenReturn(new MockSettableBeanProperty());

        property.set(instance, value);

        SettableBeanProperty idProp = mock(SettableBeanProperty.class);
        when(objectIdReader.idProperty).thenReturn(idProp);
        when(idProp.setAndReturn(instance, value)).thenReturn(instance);

        assertSame(instance, property.set(instance, value));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = getMockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        Object instance = new Object();
        Object value = new Object();

        when(property._objectIdReader.idProperty).thenReturn(new MockSettableBeanProperty());

        Object result = property.setAndReturn(instance, value);
        assertSame(instance, result);

        SettableBeanProperty idProp = mock(SettableBeanProperty.class);
        when(objectIdReader.idProperty).thenReturn(idProp);
        when(idProp.setAndReturn(instance, value)).thenReturn(instance);

        assertSame(instance, property.setAndReturn(instance, value));
    }

    private static class MockSettableBeanProperty extends SettableBeanProperty {
        @Override
        public SettableBeanProperty withName(PropertyName newName) {
            return this;
        }

        @Override
        public SettableBeanProperty withValueDeserializer(JsonDeserializer<?> deser) {
            return this;
        }

        @Override
        public SettableBeanProperty withNullProvider(NullValueProvider nva) {
            return this;
        }

        @Override
        public <A extends Annotation> A getAnnotation(Class<A> acls) {
            return null;
        }

        @Override
        public AnnotatedMember getMember() {
            return null;
        }

        @Override
        public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
        }

        @Override
        public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
            return null;
        }

        @Override
        public void set(Object instance, Object value) throws IOException {
        }

        @Override
        public Object setAndReturn(Object instance, Object value) throws IOException {
            return instance;
        }
    }
}
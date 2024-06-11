package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.node.*;

public class ObjectIdReferencePropertyTest {

    @SuppressWarnings("unchecked")
    private static <T> ObjectIdReferenceProperty<T> createObjectIdReferenceProperty(PropertyName name, ObjectIdInfo objectIdInfo) {
        return new ObjectIdReferenceProperty<>(null, name, objectIdInfo) {
            @Override
            public T get(Object instance) throws IOException {
                fail("Should not call getter method");
                return null;
            }
        };
    }

    @Test
    public void testConstructorWithForward() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator.Std().generate(Object.class, null);
        SettableBeanProperty forward = createObjectIdReferenceProperty(PropertyName.construct("forward"), null);
        ObjectIdReferenceProperty<Object> property = new ObjectIdReferenceProperty<>(forward, objectIdInfo);
        assertEquals(forward, property._forward);
    }

    @Test
    public void testConstructorWithAnotherInstance() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator.Std().generate(Object.class, null);
        SettableBeanProperty forward = createObjectIdReferenceProperty(PropertyName.construct("forward"), null);
        ObjectIdReferenceProperty<Object> src = new ObjectIdReferenceProperty<>(forward, objectIdInfo);
        ObjectIdReferenceProperty<Object> property = new ObjectIdReferenceProperty<>(src);
        assertEquals(forward, property._forward);
    }

    @Test
    public void testConstructorWithNewName() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator.Std().generate(Object.class, null);
        SettableBeanProperty forward = createObjectIdReferenceProperty(PropertyName.construct("forward"), null);
        ObjectIdReferenceProperty<Object> src = new ObjectIdReferenceProperty<>(forward, objectIdInfo);
        ObjectIdReferenceProperty<Object> property = new ObjectIdReferenceProperty<>(src, PropertyName.construct("newName"));
        assertEquals(forward, property._forward);
        assertEquals("newName", property.getName());
    }

    @Test
    public void testWithName() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator.Std().generate(Object.class, null);
        SettableBeanProperty forward = createObjectIdReferenceProperty(PropertyName.construct("forward"), null);
        ObjectIdReferenceProperty<Object> property = new ObjectIdReferenceProperty<>(forward, objectIdInfo);
        ObjectIdReferenceProperty<Object> newProperty = property.withName(PropertyName.construct("newName"));
        assertEquals("newName", newProperty.getName());
        assertEquals(forward, newProperty._forward);
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator.Std().generate(Object.class, null);
        SettableBeanProperty forward = createObjectIdReferenceProperty(PropertyName.construct("forward"), null);
        ObjectIdReferenceProperty<Object> property = new ObjectIdReferenceProperty<>(forward, objectIdInfo);
        JsonDeserializer<Object> deserializer = createMockDeserializer();
        ObjectIdReferenceProperty<Object> newProperty = property.withValueDeserializer(deserializer);
        assertEquals(deserializer, newProperty._valueDeserializer);
        assertEquals(forward, newProperty._forward);
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator.Std().generate(Object.class, null);
        SettableBeanProperty forward = createObjectIdReferenceProperty(PropertyName.construct("forward"), null);
        ObjectIdReferenceProperty<Object> property = new ObjectIdReferenceProperty<>(forward, objectIdInfo);
        NullValueProvider nva = createMockNullValueProvider();
        ObjectIdReferenceProperty<Object> newProperty = property.withNullProvider(nva);
        assertEquals(nva, newProperty._nullProvider);
        assertEquals(forward, newProperty._forward);
    }

    @Test
    public void testFixAccess() throws IOException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator.Std().generate(Object.class, null);
        SettableBeanProperty forward = createObjectIdReferenceProperty(PropertyName.construct("forward"), null);
        ObjectIdReferenceProperty<Object> property = new ObjectIdReferenceProperty<>(forward, objectIdInfo);
        JsonParser mockParser = createMockParser();
        DeserializationContext mockContext = createMockContext();
        Object instance = new Object();
        property.fixAccess(mockContext);
        forward.fixAccess(mockContext);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator.Std().generate(Object.class, null);
        SettableBeanProperty forward = createObjectIdReferenceProperty(PropertyName.construct("forward"), null);
        ObjectIdReferenceProperty<Object> property = new ObjectIdReferenceProperty<>(forward, objectIdInfo);
        Class<Annotation> annotationType = Object.class.getAnnotation(Annotation.class);
        assertNull(property.getAnnotation(annotationType));
    }

    @Test
    public void testGetMember() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator.Std().generate(Object.class, null);
        SettableBeanProperty forward = createObjectIdReferenceProperty(PropertyName.construct("forward"), null);
        ObjectIdReferenceProperty<Object> property = new ObjectIdReferenceProperty<>(forward, objectIdInfo);
        assertNull(property.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator.Std().generate(Object.class, null);
        SettableBeanProperty forward = createObjectIdReferenceProperty(PropertyName.construct("forward"), null);
        ObjectIdReferenceProperty<Object> property = new ObjectIdReferenceProperty<>(forward, objectIdInfo);
        assertEquals(-1, property.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator.Std().generate(Object.class, null);
        SettableBeanProperty forward = createObjectIdReferenceProperty(PropertyName.construct("forward"), null);
        JsonDeserializer<Object> deserializer = createMockDeserializer();
        ObjectIdReferenceProperty<Object> property = new ObjectIdReferenceProperty<>(forward, objectIdInfo, deserializer);
        ObjectParser parser = new ObjectMapper().treeAsTokens(new ObjectMapper().readValueAsTree(new ByteArrayInputStream(new byte[0])));
        DeserializationContext context = createMockContext();
        Object instance = new Object();
        property.deserializeAndSet(parser.getParser(), context, instance);
        assertEquals(null, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator.Std().generate(Object.class, null);
        SettableBeanProperty forward = createObjectIdReferenceProperty(PropertyName.construct("forward"), null);
        JsonDeserializer<Object> deserializer = createMockDeserializer();
        ObjectIdReferenceProperty<Object> property = new ObjectIdReferenceProperty<>(forward, objectIdInfo, deserializer);
        ObjectParser parser = new ObjectMapper().treeAsTokens(new ObjectMapper().readValueAsTree(new ByteArrayInputStream(new byte[0])));
        DeserializationContext context = createMockContext();
        Object instance = new Object();
        Object result = property.deserializeSetAndReturn(parser.getParser(), context, instance);
        assertEquals(null, result);
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator.Std().generate(Object.class, null);
        SettableBeanProperty forward = createObjectIdReferenceProperty(PropertyName.construct("forward"), null);
        ObjectIdReferenceProperty<Object> property = new ObjectIdReferenceProperty<>(forward, objectIdInfo);
        Object instance = new Object();
        Object value = new Object();
        property.set(instance, value);
        assertEquals(value, instance);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator.Std().generate(Object.class, null);
        SettableBeanProperty forward = createObjectIdReferenceProperty(PropertyName.construct("forward"), null);
        ObjectIdReferenceProperty<Object> property = new ObjectIdReferenceProperty<>(forward, objectIdInfo);
        Object instance = new Object();
        Object value = new Object();
        Object result = property.setAndReturn(instance, value);
        assertEquals(value, result);
        assertEquals(value, instance);
    }

    @Test
    public void testPropertyReferring() throws IOException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator.Std().generate(Object.class, null);
        SettableBeanProperty forward = createObjectIdReferenceProperty(PropertyName.construct("forward"), null);
        ObjectIdReferenceProperty<Object> property = new ObjectIdReferenceProperty<>(forward, objectIdInfo);
        ObjectParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        Object instance = new Object();
        Object id = new Object();
        PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(property, null, Object.class, instance) {
            @Override
            public void handleResolvedForwardReference(Object id, Object value) throws IOException {
                assertEquals(id, property.getId());
                assertEquals(value, instance);
            }
        };
        parser.getCurrentName().setAsProperty(parser, referring);
        parser.nextToken();
        assertEquals(id, referring.getId());
    }
}
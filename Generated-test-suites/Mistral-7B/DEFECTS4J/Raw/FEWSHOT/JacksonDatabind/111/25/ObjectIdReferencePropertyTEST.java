package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testConstructorWithForwardAndObjectIdInfo() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class, "id");
        SettableBeanProperty forward = new SettableBeanProperty(Object.class, "name");
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertEquals(forward, objectIdReferenceProperty._forward);
        assertEquals(objectIdInfo, objectIdReferenceProperty._objectIdInfo);
    }

    @Test
    public void testConstructorWithSrcDeserAndNullValueProvider() {
        JsonDeserializer<?> jsonDeserializer = new StandardDeserializer<>();
        NullValueProvider nullValueProvider = new StandardNullValueProvider();
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(null, jsonDeserializer, nullValueProvider);
        assertEquals(null, objectIdReferenceProperty._forward);
        assertNull(objectIdReferenceProperty._objectIdInfo);
        assertEquals(jsonDeserializer, objectIdReferenceProperty._valueDeserializer);
        assertEquals(nullValueProvider, objectIdReferenceProperty._nullProvider);
    }

    @Test
    public void testConstructorWithSrcAndNewName() {
        SettableBeanProperty src = new SettableBeanProperty(Object.class, "name");
        PropertyName newName = PropertyName.construct("id");
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(src, newName);
        assertEquals(src, objectIdReferenceProperty);
        assertEquals(newName, objectIdReferenceProperty.getName());
    }

    @Test
    public void testWithName() {
        SettableBeanProperty src = new SettableBeanProperty(Object.class, "name");
        PropertyName newName = PropertyName.construct("id");
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(src);
        ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withName(newName);
        assertEquals(newName, newObjectIdReferenceProperty.getName());
        assertEquals(src, newObjectIdReferenceProperty);
        assertEquals(src._forward, newObjectIdReferenceProperty._forward);
        assertEquals(src._objectIdInfo, newObjectIdReferenceProperty._objectIdInfo);
        assertEquals(src._valueDeserializer, newObjectIdReferenceProperty._valueDeserializer);
        assertEquals(src._nullProvider, newObjectIdReferenceProperty._nullProvider);
    }

    @Test
    public void testWithValueDeserializer() {
        SettableBeanProperty src = new SettableBeanProperty(Object.class, "name");
        JsonDeserializer<?> jsonDeserializer = new StandardDeserializer<>();
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(src);
        ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withValueDeserializer(jsonDeserializer);
        assertEquals(jsonDeserializer, newObjectIdReferenceProperty._valueDeserializer);
        assertEquals(src._forward, newObjectIdReferenceProperty._forward);
        assertEquals(src._objectIdInfo, newObjectIdReferenceProperty._objectIdInfo);
        assertEquals(src._nullProvider, newObjectIdReferenceProperty._nullProvider);
    }

    @Test
    public void testWithNullProvider() {
        SettableBeanProperty src = new SettableBeanProperty(Object.class, "name");
        NullValueProvider nullValueProvider = new StandardNullValueProvider();
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(src);
        ObjectIdReferenceProperty newObjectIdReferenceProperty = objectIdReferenceProperty.withNullProvider(nullValueProvider);
        assertEquals(src._forward, newObjectIdReferenceProperty._forward);
        assertEquals(src._objectIdInfo, newObjectIdReferenceProperty._objectIdInfo);
        assertEquals(src._valueDeserializer, newObjectIdReferenceProperty._valueDeserializer);
        assertEquals(nullValueProvider, newObjectIdReferenceProperty._nullProvider);
    }

    @Test
    public void testFixAccess() {
        SettableBeanProperty src = new SettableBeanProperty(Object.class, "name");
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(src);
        objectIdReferenceProperty.fixAccess(new DeserializationConfig());
        assertNull(objectIdReferenceProperty._forward);
    }

    @Test
    public void testGetAnnotation() {
        SettableBeanProperty src = new SettableBeanProperty(Object.class, "name");
        ObjectIdAnnotation annotation = new ObjectIdAnnotation();
        src.addClassAnnotation(annotation.getClass(), annotation);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(src);
        assertEquals(annotation, objectIdReferenceProperty.getAnnotation(ObjectIdAnnotation.class));
        assertNull(objectIdReferenceProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        SettableBeanProperty src = new SettableBeanProperty(Object.class, "name");
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(src);
        assertEquals(src.getMember(), objectIdReferenceProperty.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        SettableBeanProperty src = new SettableBeanProperty(Object.class, "name");
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(src);
        assertEquals(src.getCreatorIndex(), objectIdReferenceProperty.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        ObjectMapper objectMapper = new ObjectMapper();
        SettableBeanProperty src = new SettableBeanProperty(Object.class, "name");
        JsonDeserializer<?> jsonDeserializer = new StandardDeserializer<>();
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(src, jsonDeserializer);
        Object pojo = new Object();
        objectMapper.readValue(objectNode, pojo.getClass(), objectIdReferenceProperty);
        assertEquals(null, src.getValue(pojo));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        ObjectMapper objectMapper = new ObjectMapper();
        SettableBeanProperty src = new SettableBeanProperty(Object.class, "name");
        JsonDeserializer<?> jsonDeserializer = new StandardDeserializer<>();
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(src, jsonDeserializer);
        Object pojo = new Object();
        Object value = new Object();
        ObjectNode newObjectNode = objectMapper.readTree(objectNode.toString());
        JsonParser jsonParser = objectMapper.treeToValue(newObjectNode, Object.class);
        Object deser = jsonDeserializer.deserialize(jsonParser, Object.class, null);
        Object result = objectIdReferenceProperty.deserializeSetAndReturn(jsonParser, new DeserializationContext(), pojo);
        assertEquals(value, result);
        assertEquals(value, src.getValue(pojo));
    }

    @Test
    public void testSet() throws IOException {
        SettableBeanProperty src = new SettableBeanProperty(Object.class, "name");
        Object pojo = new Object();
        Object value = new Object();
        src.set(pojo, value);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(src);
        objectIdReferenceProperty.set(pojo, value);
        assertEquals(value, src.getValue(pojo));
        assertEquals(value, objectIdReferenceProperty.getValue(pojo));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        SettableBeanProperty src = new SettableBeanProperty(Object.class, "name");
        Object pojo = new Object();
        Object value = new Object();
        Object result = src.setAndReturn(pojo, value);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(src);
        result = objectIdReferenceProperty.setAndReturn(pojo, value);
        assertEquals(value, result);
        assertEquals(value, src.getValue(pojo));
        assertEquals(value, objectIdReferenceProperty.getValue(pojo));
    }
}
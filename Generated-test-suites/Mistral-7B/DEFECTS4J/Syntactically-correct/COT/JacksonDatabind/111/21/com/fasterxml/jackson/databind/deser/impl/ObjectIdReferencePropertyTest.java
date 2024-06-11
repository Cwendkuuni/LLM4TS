package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testObjectIdReferencePropertyConstructor() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, Object.class);
        SettableBeanProperty forward = new SettableBeanProperty(null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertNotNull(property);
        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyCopyConstructor() {
        SettableBeanProperty srcForward = new SettableBeanProperty(null, null, null);
        ObjectIdReferenceProperty srcProperty = new ObjectIdReferenceProperty(srcForward, null, null);
        JsonDeserializer<?> deserializer = new JsonDeserializer<Object>() {};
        NullValueProvider nva = new AlwaysNull();
        ObjectIdReferenceProperty destProperty = new ObjectIdReferenceProperty(srcProperty, deserializer, nva);
        assertNotNull(destProperty);
        assertEquals(srcProperty._forward, destProperty._forward);
        assertEquals(srcProperty._objectIdInfo, destProperty._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyNameConstructor() {
        ObjectIdReferenceProperty srcProperty = new ObjectIdReferenceProperty(null, null, null);
        PropertyName newName = PropertyName.construct("testProperty");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(srcProperty, newName);
        assertNotNull(property);
        assertEquals(srcProperty, property);
        assertEquals(newName, property.getName());
    }

    @Test
    public void testOverrideValueDeserializer() {
        SettableBeanProperty srcForward = new SettableBeanProperty(null, null, null);
        ObjectIdReferenceProperty srcProperty = new ObjectIdReferenceProperty(srcForward, null, null);
        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {};
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(srcProperty, deserializer, null);
        assertNotEquals(srcProperty._valueDeserializer, property._valueDeserializer);
        assertEquals(deserializer, property._valueDeserializer);
    }

    @Test
    public void testOverrideNullProvider() {
        SettableBeanProperty srcForward = new SettableBeanProperty(null, null, null);
        ObjectIdReferenceProperty srcProperty = new ObjectIdReferenceProperty(srcForward, null, null);
        NullValueProvider nva = new AlwaysNull();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(srcProperty, null, nva);
        assertNotEquals(srcProperty._nullProvider, property._nullProvider);
        assertEquals(nva, property._nullProvider);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFixAccessNullConfig() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        property.fixAccess(null);
    }

    @Test
    public void testGetAnnotation() {
        Annotation annotation = new MyAnnotation();
        SettableBeanProperty forward = new SettableBeanProperty(null, null, null) {
            @Override
            public <A extends Annotation> A getAnnotation(Class<A> acls) {
                return annotation;
            }
        };
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, null, null);
        assertEquals(annotation, property.getAnnotation(MyAnnotation.class));
    }

    @Test
    public void testGetMember() {
        SettableBeanProperty forward = new SettableBeanProperty(null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, null, null);
        assertEquals(forward.getMember(), property.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        SettableBeanProperty forward = new SettableBeanProperty(null, null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, null, null);
        assertEquals(forward.getCreatorIndex(), property.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectNode node = new ObjectMapper().createObjectNode();
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser(node.toString());
        Object id = new Object();
        Object instance = new Object();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        property.deserializeAndSet(p, mapper.getDeserializationContext(), instance);
        assertEquals(instance, p.getValueAsObject());
        assertEquals(id, property.get(instance));
    }

    @Test
    public void testDeserializeSetAndReturnNull() throws IOException {
        ObjectNode node = new ObjectMapper().createObjectNode();
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser(node.toString());
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        assertNull(property.deserializeSetAndReturn(p, mapper.getDeserializationContext(), null));
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testDeserializeSetAndReturnUnresolvedForwardReference() throws IOException {
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("$ref", "#/properties/testProperty");
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser(node.toString());
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        property.deserializeSetAndReturn(p, mapper.getDeserializationContext(), new Object());
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        property.set(instance, value);
        assertEquals(value, property.get(instance));
    }

    @Test
    public void testSetAndReturnNull() throws IOException {
        Object instance = new Object();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        assertNull(property.setAndReturn(instance, null));
    }

    @Test
    public void testSetAndReturnNotNull() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        assertEquals(value, property.setAndReturn(instance, value));
    }

    @Test
    public void testHandleResolvedForwardReference() {
        Object id = new Object();
        Object instance = new Object();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        property.getReferring().handleResolvedForwardReference(id, new Object());
        assertEquals(instance, id);
    }

    private static class AlwaysNull implements NullValueProvider {
        @Override
        public Object getValue(DeserializationContext ctxt, JsonParser p) throws IOException {
            return null;
        }
    }

    private static class MyAnnotation implements Annotation {

    }

}